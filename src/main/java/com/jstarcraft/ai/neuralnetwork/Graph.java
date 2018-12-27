package com.jstarcraft.ai.neuralnetwork;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.MathCalculator;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.ModelCycle;
import com.jstarcraft.ai.model.ModelDefinition;
import com.jstarcraft.ai.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.neuralnetwork.loss.LossFunction;
import com.jstarcraft.ai.neuralnetwork.optimization.Optimizer;
import com.jstarcraft.ai.neuralnetwork.vertex.LayerVertex;
import com.jstarcraft.ai.neuralnetwork.vertex.Vertex;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.core.utility.StringUtility;

/**
 * 计算图
 * 
 * @author Birdy
 *
 */
@ModelDefinition(value = { "topologicalOrder", "forwardDependencies", "backwardDependencies", "vertices", "optimizer", "lossFunctions" })
public class Graph implements ModelCycle {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private EpochMonitor monitor;

	private int numberOfSamples;

	/** 节点映射(索引,名称,实例) */
	private Vertex[] vertices;
	/** 输入节点 */
	private Vertex[] inputVertices;
	/** 输出节点 */
	private Vertex[] outputVertices;
	/** 层 */
	private KeyValue<String, Layer>[] layers;
	/** 目标函数 */
	private LossFunction[] lossFunctions;

	/** 拓扑排序 */
	private int[] topologicalOrder;
	/** 正向依赖 */
	private List<Integer>[] forwardDependencies;
	/** 反向依赖 */
	private List<Integer>[] backwardDependencies;

	protected Semaphore[] semaphores;

	protected CountDownLatch latch;

	/** 优化器 */
	protected Optimizer optimizer;

	Graph() {
	}

	public Graph(GraphConfigurator configurator, Optimizer optimizer, LossFunction... lossFunctions) {
		KeyValue<int[], KeyValue<List<Integer>[], List<Integer>[]>> configuration = configurator.calculateTopologicalOrder();
		this.topologicalOrder = configuration.getKey();
		this.forwardDependencies = configuration.getValue().getKey();
		this.backwardDependencies = configuration.getValue().getValue();

		Map<String, KeyValue<Integer, Vertex>> vertices = configurator.getVertices();
		this.vertices = new Vertex[vertices.size()];
		this.semaphores = new Semaphore[vertices.size()];
		for (KeyValue<Integer, Vertex> keyValue : vertices.values()) {
			this.vertices[keyValue.getKey()] = keyValue.getValue();
			this.semaphores[keyValue.getKey()] = new Semaphore(0);
		}

		List<KeyValue<String, Layer>> layers = new LinkedList<>();
		// 没有正向依赖的节点为输入节点
		List<Vertex> inputVertices = new LinkedList<>();
		// 没有反向依赖的节点为输出节点
		List<Vertex> outputVertices = new LinkedList<>();
		for (int index : this.topologicalOrder) {
			if (this.vertices[index] instanceof LayerVertex) {
				LayerVertex vertex = LayerVertex.class.cast(this.vertices[index]);
				KeyValue<String, Layer> keyValue = new KeyValue<String, Layer>(vertex.getVertexName(), vertex.getLayer());
				layers.add(keyValue);
			}
			if (this.forwardDependencies[index].isEmpty()) {
				inputVertices.add(this.vertices[index]);
			}
			if (this.backwardDependencies[index].isEmpty()) {
				outputVertices.add(this.vertices[index]);
			}
		}
		this.layers = layers.toArray(new KeyValue[layers.size()]);
		this.inputVertices = inputVertices.toArray(new Vertex[inputVertices.size()]);
		this.outputVertices = outputVertices.toArray(new Vertex[outputVertices.size()]);

		this.optimizer = optimizer;
		this.lossFunctions = lossFunctions;
	}

	public void setMonitor(EpochMonitor monitor) {
		this.monitor = monitor;
	}

	public EpochMonitor getMonitor() {
		return monitor;
	}

	public void doCache(MathMatrix[] samples, MathMatrix[] labels) {
		numberOfSamples = samples[0].getRowSize();
		for (int index = 1, size = samples.length; index < size; index++) {
			// 检查数量
			if (samples[index].getRowSize() != numberOfSamples) {
				throw new IllegalArgumentException();
			}
		}

		int position = 0;
		for (int size = samples.length; position < size; position++) {
			vertices[position].doCache(new KeyValue<>(samples[position], null));
		}
		for (int size = vertices.length; position < size; position++) {
			List<Integer> dependencies = forwardDependencies[position];
			KeyValue<MathMatrix, MathMatrix>[] keyValues = new KeyValue[dependencies.size()];
			for (int index = 0; index < keyValues.length; index++) {
				Vertex vertex = vertices[dependencies.get(index)];
				keyValues[index] = vertex.getOutputKeyValue();
			}
			vertices[position].doCache(keyValues);
		}

		for (int index = 0, size = outputVertices.length; index < size; index++) {
			Vertex vertex = outputVertices[index];
			LossFunction lossFunction = lossFunctions[index];
			KeyValue<MathMatrix, MathMatrix> keyValue = vertex.getOutputKeyValue();
			lossFunction.doCache(labels[index], keyValue.getKey());
		}
	}

	public void doForward() {
		latch = new CountDownLatch(topologicalOrder.length);
		for (int index = 0, size = topologicalOrder.length; index < size; index++) {
			Semaphore semaphore = semaphores[index];
			List<Integer> inputDependencies = forwardDependencies[index];
			List<Integer> outputDependencies = backwardDependencies[index];
			Vertex vertex = vertices[index];
			EnvironmentContext context = EnvironmentContext.getContext();
			context.doAlgorithmByAny(index, () -> {
				try {
					// 消耗inputDependencies的信号量
					semaphore.acquire(inputDependencies.size());
					long time = System.currentTimeMillis();
					vertex.doForward();
					if (logger.isDebugEnabled()) {
						logger.debug(StringUtility.format("{}节点{}正向传播耗时{}毫秒", vertex.getClass().getSimpleName(), vertex.getVertexName(), System.currentTimeMillis() - time));
					}
					for (int position : outputDependencies) {
						// 产生outputDependencies的信号量
						semaphores[position].release();
					}
					latch.countDown();
				} catch (Exception exception) {
					logger.error("exception", exception);
				}
			});
		}
		try {
			latch.await();
		} catch (Exception exception) {
			logger.error("exception", exception);
		}
	}

	public void doBackward() {
		latch = new CountDownLatch(topologicalOrder.length);
		for (int index = topologicalOrder.length - 1; index > -1; index--) {
			Semaphore semaphore = semaphores[index];
			List<Integer> inputDependencies = backwardDependencies[index];
			List<Integer> outputDependencies = forwardDependencies[index];
			List<Integer> beforeDependencies = new LinkedList<>();
			List<Integer> afterDependencies = new LinkedList<>();
			// 此处在单线程会存在死锁.需要重新排序topologicalOrder
			for (int position : outputDependencies) {
				List<Integer> dependencies = beforeDependencies;
				for (int dependency : backwardDependencies[position]) {
					if (dependency == index) {
						dependencies = afterDependencies;
					} else {
						dependencies.add(dependency);
					}
				}
			}
			Vertex vertex = vertices[index];
			EnvironmentContext context = EnvironmentContext.getContext();
			context.doAlgorithmByAny(index, () -> {
				try {
					// 消耗inputDependencies与beforeDependencies的信号量
					semaphore.acquire(inputDependencies.size() + afterDependencies.size());
					long time = System.currentTimeMillis();
					vertex.doBackward();
					if (logger.isDebugEnabled()) {
						logger.debug(StringUtility.format("{}节点{}反向传播耗时{}毫秒", vertex.getClass().getSimpleName(), vertex.getVertexName(), System.currentTimeMillis() - time));
					}
					for (int position : beforeDependencies) {
						// 产生beforeDependencies的信号量
						semaphores[position].release();
					}
					for (int position : outputDependencies) {
						// 产生outputDependencies的信号量
						semaphores[position].release();
					}
					latch.countDown();
				} catch (Exception exception) {
					logger.error("exception", exception);
				}
			});
		}
		try {
			latch.await();
		} catch (Exception exception) {
			logger.error("exception", exception);
		}
	}

	/**
	 * 训练
	 * 
	 * @param samples
	 * @param labels
	 */
	public float practice(int numberOfIterations, MathMatrix[] samples, MathMatrix[] labels) {
		doCache(samples, labels);
		for (int index = 0, size = labels.length; index < size; index++) {
			// 检查数量
			if (labels[index].getRowSize() != numberOfSamples) {
				throw new IllegalArgumentException();
			}
		}

		// TODO 获取所有层的梯度与参数
		Map<String, MathMatrix> parameters = new HashMap<>();
		Map<String, MathMatrix> gradients = new HashMap<>();
		for (KeyValue<String, Layer> term : layers) {
			String name = term.getKey();
			Layer layer = term.getValue();
			for (Entry<String, MathMatrix> parameter : layer.getParameters().entrySet()) {
				if (parameters.put(name + ":" + parameter.getKey(), parameter.getValue()) != null) {
					throw new RuntimeException(StringUtility.format("Layer参数名称{}冲突", name));
				}
			}
			for (Entry<String, MathMatrix> gradient : layer.getGradients().entrySet()) {
				if (gradients.put(name + ":" + gradient.getKey(), gradient.getValue()) != null) {
					throw new RuntimeException(StringUtility.format("Layer参数名称{}冲突", name));
				}
			}
		}

		Callable<Float> scorer = () -> {
			if (monitor != null) {
				monitor.beforeForward();
			}
			doForward();
			if (monitor != null) {
				monitor.afterForward();
			}
			// TODO 执行目标函数(loss)
			float score = 0F;
			for (int index = 0, size = outputVertices.length; index < size; index++) {
				long time = System.currentTimeMillis();
				Vertex vertex = outputVertices[index];
				LossFunction lossFunction = lossFunctions[index];
				KeyValue<MathMatrix, MathMatrix> keyValue = vertex.getOutputKeyValue();
				// TODO 考虑computeGradient与computeScore整合,避免重复迭代.
				lossFunction.computeGradient(labels[index], keyValue.getKey(), null, keyValue.getValue());
				score += lossFunction.computeScore(labels[index], keyValue.getKey(), null);
				if (logger.isDebugEnabled()) {
					logger.debug(StringUtility.format("{}目标函数计算耗时{}毫秒", lossFunction.getClass().getSimpleName(), System.currentTimeMillis() - time));
				}
			}
			if (monitor != null) {
				monitor.beforeBackward();
			}
			doBackward();
			if (monitor != null) {
				monitor.afterBackward();
			}
			for (KeyValue<String, Layer> term : layers) {
				Layer layer = term.getValue();
				score += (layer.calculateL1Norm() + layer.calculateL2Norm());
			}
			score /= numberOfSamples;
			return score;
		};

		optimizer.doCache(scorer, gradients, parameters);

		// 迭代
		float score = 0F;
		// 按照样本数量缩放梯度比例
		float scale = 1F / numberOfSamples;
		for (int iteration = 0; iteration < numberOfIterations; iteration++) {
			try {
				score = scorer.call();

				for (KeyValue<String, Layer> term : layers) {
					Layer layer = term.getValue();
					layer.regularize();
				}

				for (MathMatrix gradient : gradients.values()) {
					// TODO 此处应该判断是否由于scale导致NaN或者无穷
					gradient.scaleValues(scale);
				}

				if (optimizer.optimize(score)) {
					// TODO 收敛条件(termination)
				}
			} catch (Exception exception) {
				logger.error("exception", exception);
				throw new RuntimeException(exception);
			}
		}
		return score;
	}

	/**
	 * 预测
	 * 
	 * @param inputs
	 * @param outputs
	 */
	public void predict(MathMatrix[] samples, MathMatrix[] labels) {
		doCache(samples, labels);
		for (int index = 0, size = labels.length; index < size; index++) {
			// 检查数量
			if (labels[index].getRowSize() != numberOfSamples) {
				throw new IllegalArgumentException();
			}
		}

		doForward();
		for (int index = 0, size = outputVertices.length; index < size; index++) {
			Vertex vertex = outputVertices[index];
			KeyValue<MathMatrix, MathMatrix> keyValue = vertex.getOutputKeyValue();
			MathMatrix outputData = keyValue.getKey();
			labels[index].iterateElement(MathCalculator.PARALLEL, (scalar) -> {
				scalar.setValue(outputData.getValue(scalar.getRow(), scalar.getColumn()));
			});
		}
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterLoad() {
		this.semaphores = new Semaphore[vertices.length];
		for (int index = 0; index < vertices.length; index++) {
			this.semaphores[index] = new Semaphore(0);
		}

		List<KeyValue<String, Layer>> layers = new LinkedList<>();
		// 没有正向依赖的节点为输入节点
		List<Vertex> inputVertices = new LinkedList<>();
		// 没有反向依赖的节点为输出节点
		List<Vertex> outputVertices = new LinkedList<>();
		for (int index : this.topologicalOrder) {
			if (this.vertices[index] instanceof LayerVertex) {
				LayerVertex vertex = LayerVertex.class.cast(this.vertices[index]);
				KeyValue<String, Layer> keyValue = new KeyValue<String, Layer>(vertex.getVertexName(), vertex.getLayer());
				layers.add(keyValue);
			}
			if (this.forwardDependencies[index].isEmpty()) {
				inputVertices.add(this.vertices[index]);
			}
			if (this.backwardDependencies[index].isEmpty()) {
				outputVertices.add(this.vertices[index]);
			}
		}
		this.layers = layers.toArray(new KeyValue[layers.size()]);
		this.inputVertices = inputVertices.toArray(new Vertex[inputVertices.size()]);
		this.outputVertices = outputVertices.toArray(new Vertex[outputVertices.size()]);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		} else {
			Graph that = (Graph) object;
			EqualsBuilder equal = new EqualsBuilder();
			equal.append(this.topologicalOrder, that.topologicalOrder);
			equal.append(this.forwardDependencies, that.forwardDependencies);
			equal.append(this.backwardDependencies, that.backwardDependencies);
			equal.append(this.vertices, that.vertices);
			equal.append(this.optimizer, that.optimizer);
			equal.append(this.lossFunctions, that.lossFunctions);
			return equal.isEquals();
		}
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hash = new HashCodeBuilder();
		hash.append(topologicalOrder);
		hash.append(forwardDependencies);
		hash.append(backwardDependencies);
		hash.append(vertices);
		hash.append(optimizer);
		hash.append(lossFunctions);
		return hash.toHashCode();
	}

	@Override
	public String toString() {
		return "Graph()";
	}

}
