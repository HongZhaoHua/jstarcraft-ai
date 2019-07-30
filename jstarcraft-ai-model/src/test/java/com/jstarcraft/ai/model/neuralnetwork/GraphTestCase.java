package com.jstarcraft.ai.model.neuralnetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.AbstractLayer;
import org.deeplearning4j.nn.params.DefaultParamInitializer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.concurrency.AffinityManager;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.MathUtility;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.Nd4jCache;
import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.math.structure.matrix.Nd4jMatrix;
import com.jstarcraft.ai.model.neuralnetwork.activation.IdentityActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.layer.EmbedLayer;
import com.jstarcraft.ai.model.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.model.neuralnetwork.layer.ParameterConfigurator;
import com.jstarcraft.ai.model.neuralnetwork.layer.WeightLayer;
import com.jstarcraft.ai.model.neuralnetwork.learn.SgdLearner;
import com.jstarcraft.ai.model.neuralnetwork.loss.MSELossFunction;
import com.jstarcraft.ai.model.neuralnetwork.normalization.IgnoreNormalizer;
import com.jstarcraft.ai.model.neuralnetwork.optimization.StochasticGradientOptimizer;
import com.jstarcraft.ai.model.neuralnetwork.parameter.CopyParameterFactory;
import com.jstarcraft.ai.model.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.model.neuralnetwork.schedule.Schedule;
import com.jstarcraft.ai.model.neuralnetwork.vertex.LayerVertex;
import com.jstarcraft.ai.model.neuralnetwork.vertex.Nd4jVertex;
import com.jstarcraft.ai.model.neuralnetwork.vertex.transformation.HorizontalAttachVertex;
import com.jstarcraft.core.utility.RandomUtility;

public class GraphTestCase {

	private final static float learnRatio = 0.01F;
	private final static float l1Regularization = 0.01F;
	private final static float l2Regularization = 0.05F;

	private static MathMatrix getMatrix(MathCache factory, INDArray array) {
		MathMatrix matrix = factory.makeMatrix(array.rows(), array.columns());
		matrix.copyMatrix(new Nd4jMatrix(array), false);
		return matrix;
	}

	private static Map<String, ParameterConfigurator> getConfigurators(MathCache factory, AbstractLayer<?> layer) {
		Map<String, ParameterConfigurator> configurators = new HashMap<>();
		CopyParameterFactory weight = new CopyParameterFactory(getMatrix(factory, layer.getParam(DefaultParamInitializer.WEIGHT_KEY)));
		configurators.put(WeightLayer.WEIGHT_KEY, new ParameterConfigurator(l1Regularization, l2Regularization, weight));
		CopyParameterFactory bias = new CopyParameterFactory(getMatrix(factory, layer.getParam(DefaultParamInitializer.BIAS_KEY)));
		configurators.put(WeightLayer.BIAS_KEY, new ParameterConfigurator(l1Regularization, l2Regularization, bias));
		return configurators;
	}

	private ComputationGraph getOldFunction() {
		NeuralNetConfiguration.Builder netBuilder = new NeuralNetConfiguration.Builder();
		// 设置随机种子
		netBuilder.seed(6);
		netBuilder.setL1(l1Regularization);
		netBuilder.setL1Bias(l1Regularization);
		netBuilder.setL2(l2Regularization);
		netBuilder.setL2Bias(l2Regularization);
		netBuilder.weightInit(WeightInit.XAVIER_UNIFORM);
		netBuilder.updater(new Sgd(learnRatio)).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);

		GraphBuilder graphBuilder = netBuilder.graphBuilder();
		graphBuilder.addInputs("leftInput", "rightInput");
		graphBuilder.addLayer("leftEmbed", new EmbeddingLayer.Builder().nIn(5).nOut(5).hasBias(true).activation(Activation.IDENTITY).build(), "leftInput");
		graphBuilder.addLayer("rightEmbed", new EmbeddingLayer.Builder().nIn(5).nOut(5).hasBias(true).activation(Activation.IDENTITY).build(), "rightInput");
		graphBuilder.addVertex("embed", new MergeVertex(), "leftEmbed", "rightEmbed");
		graphBuilder.addLayer("output", new OutputLayer.Builder(LossFunctions.LossFunction.MSE).activation(Activation.IDENTITY).nIn(10).nOut(1).build(), "embed");
		graphBuilder.setOutputs("output");

		ComputationGraphConfiguration configuration = graphBuilder.build();
		ComputationGraph graph = new ComputationGraph(configuration);
		graph.init();
		return graph;
	}

	private Graph getNewFunction(MathCache factory, ComputationGraph computationGraph) {
		Schedule schedule = new ConstantSchedule(learnRatio);
		GraphConfigurator configurator = new GraphConfigurator();
		Layer leftEmbed = new EmbedLayer(5, 5, factory, getConfigurators(factory, (AbstractLayer<?>) computationGraph.getLayer("leftEmbed")), new IdentityActivationFunction());
		Layer rightEmbed = new EmbedLayer(5, 5, factory, getConfigurators(factory, (AbstractLayer<?>) computationGraph.getLayer("rightEmbed")), new IdentityActivationFunction());

		configurator.connect(new LayerVertex("leftEmbed", factory, leftEmbed, new SgdLearner(schedule), new IgnoreNormalizer()));
		configurator.connect(new LayerVertex("rightEmbed", factory, rightEmbed, new SgdLearner(schedule), new IgnoreNormalizer()));
		configurator.connect(new HorizontalAttachVertex("embed", factory), "leftEmbed", "rightEmbed");
		configurator.connect(new Nd4jVertex("nd4j", factory, true), "embed");
		Layer weightLayer = new WeightLayer(10, 1, factory, getConfigurators(factory, (AbstractLayer<?>) computationGraph.getLayer("output")), new IdentityActivationFunction());
		configurator.connect(new LayerVertex("output", factory, weightLayer, new SgdLearner(schedule), new IgnoreNormalizer()), "nd4j");

		Graph graph = new Graph(configurator, new StochasticGradientOptimizer(), new MSELossFunction());
		return graph;
	}

	private static boolean equalMatrix(MathMatrix matrix, INDArray array) {
		for (int row = 0; row < matrix.getRowSize(); row++) {
			for (int column = 0; column < matrix.getColumnSize(); column++) {
				if (!MathUtility.equal(matrix.getValue(row, column), array.getFloat(row, column))) {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void testPropagate() throws Exception {
		MathCache factory = new Nd4jCache();

		EnvironmentContext context = EnvironmentFactory.getContext();
		Future<?> task = context.doTask(() -> {
			ComputationGraph oldGraph = getOldFunction();
			Graph graph = getNewFunction(factory, oldGraph);

			int size = 5;
			INDArray oldLeftInputs = Nd4j.zeros(size, 1);
			INDArray oldRightInputs = Nd4j.zeros(size, 1);
			INDArray oldMarks = Nd4j.zeros(size, 1).assign(5);
			for (int point = 0; point < 5; point++) {
				oldLeftInputs.put(point, 0, RandomUtility.randomInteger(5));
				oldRightInputs.put(point, 0, RandomUtility.randomInteger(5));
			}
			for (int index = 0; index < 50; index++) {
				oldGraph.setInputs(oldLeftInputs, oldRightInputs);
				oldGraph.setLabels(oldMarks);
				// 设置fit过程的迭代次数
				for (int iteration = 0; iteration < 2; iteration++) {
					oldGraph.fit();
					double oldScore = oldGraph.score();
					System.out.println(oldScore);
				}
			}
			INDArray oldOutputs = oldGraph.outputSingle(oldLeftInputs, oldRightInputs);
			System.out.println(oldOutputs);

			AffinityManager manager = Nd4j.getAffinityManager();
			manager.attachThreadToDevice(Thread.currentThread(), 0);
			MathMatrix newLeftInputs = getMatrix(factory, oldLeftInputs);
			MathMatrix newRightInputs = getMatrix(factory, oldRightInputs);
			MathMatrix newMarks = getMatrix(factory, oldMarks);
			MathMatrix newOutputs = getMatrix(factory, oldOutputs);

			for (int index = 0; index < 50; index++) {
				double newScore = graph.practice(2, new MathMatrix[] { newLeftInputs, newRightInputs }, new MathMatrix[] { newMarks });
				System.out.println(newScore);
			}

			graph.predict(new MathMatrix[] { newLeftInputs, newRightInputs }, new MathMatrix[] { newOutputs });
			System.out.println(newOutputs);
			Assert.assertTrue(equalMatrix(newOutputs, oldOutputs));
		});
		task.get();
	}

}
