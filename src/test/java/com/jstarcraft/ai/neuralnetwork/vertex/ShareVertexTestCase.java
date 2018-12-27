package com.jstarcraft.ai.neuralnetwork.vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.model.ModelCodec;
import com.jstarcraft.ai.neuralnetwork.DenseMatrixFactory;
import com.jstarcraft.ai.neuralnetwork.EpochMonitor;
import com.jstarcraft.ai.neuralnetwork.Graph;
import com.jstarcraft.ai.neuralnetwork.GraphConfigurator;
import com.jstarcraft.ai.neuralnetwork.MatrixFactory;
import com.jstarcraft.ai.neuralnetwork.activation.SigmoidActivationFunction;
import com.jstarcraft.ai.neuralnetwork.layer.EmbedLayer;
import com.jstarcraft.ai.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.neuralnetwork.layer.Layer.Mode;
import com.jstarcraft.ai.neuralnetwork.layer.ParameterConfigurator;
import com.jstarcraft.ai.neuralnetwork.layer.WeightLayer;
import com.jstarcraft.ai.neuralnetwork.learn.SgdLearner;
import com.jstarcraft.ai.neuralnetwork.loss.MSELossFunction;
import com.jstarcraft.ai.neuralnetwork.normalization.IgnoreNormalizer;
import com.jstarcraft.ai.neuralnetwork.optimization.StochasticGradientOptimizer;
import com.jstarcraft.ai.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.neuralnetwork.schedule.Schedule;
import com.jstarcraft.ai.neuralnetwork.vertex.accumulation.SumVertex;
import com.jstarcraft.ai.neuralnetwork.vertex.transformation.HorizontalStackVertex;
import com.jstarcraft.ai.neuralnetwork.vertex.transformation.HorizontalUnstackVertex;
import com.jstarcraft.core.utility.RandomUtility;

public class ShareVertexTestCase {

	private Schedule schedule = new ConstantSchedule(0.01F);

	private Map<String, ParameterConfigurator> configurators = new HashMap<>();
	{
		ParameterConfigurator parameter = new ParameterConfigurator(0F, 0F, new MockParameterFactory());
		configurators.put(WeightLayer.WEIGHT_KEY, parameter);
		configurators.put(WeightLayer.BIAS_KEY, new ParameterConfigurator(0F, 0F));
	}

	// 因子数量
	private int numberOfFactors = 10;
	private int discreteDimension = 5;
	private int continuousOrder = 5;
	private int numberOfSamples = 20;
	private int numberOfIterations = 10;

	private Graph getCrossGraph(Vertex shareVertex, DenseMatrix leftLabels, DenseMatrix rightLabels) {
		GraphConfigurator configurator = new GraphConfigurator();
		MatrixFactory factory = new DenseMatrixFactory();
		// 离散特征部分
		Layer discreteLayer = new EmbedLayer(discreteDimension, numberOfFactors, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
		configurator.connect(new LayerVertex("discreteFeature", factory, discreteLayer, new SgdLearner(schedule), new IgnoreNormalizer()));
		configurator.connect(new SumVertex("discreteNumber", factory));

		// 连续特征部分(归一化)
		Layer continuousLayer = new WeightLayer(continuousOrder, numberOfFactors, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
		configurator.connect(new LayerVertex("continuousFeatures", factory, continuousLayer, new SgdLearner(schedule), new IgnoreNormalizer()));
		configurator.connect(new SumVertex("continuousNumber", factory));

		// 编解码部分
		configurator.connect(new HorizontalStackVertex("leftStack", factory), "discreteFeature", "discreteNumber", "continuousNumber");
		configurator.connect(new HorizontalStackVertex("rightStack", factory), "continuousFeatures", "continuousNumber", "discreteNumber");
		configurator.connect(new HorizontalStackVertex("factorStack", factory), "leftStack", "rightStack");

		configurator.connect(shareVertex, "factorStack");

		configurator.connect(new HorizontalUnstackVertex("leftUnstack", factory, 0, numberOfFactors), "factorCodec");
		configurator.connect(new HorizontalUnstackVertex("rightUntack", factory, numberOfFactors, numberOfFactors * 2), "factorCodec");

		Graph graph = new Graph(configurator, new StochasticGradientOptimizer(), new MSELossFunction(), new MSELossFunction());

		EpochMonitor monitor = new EpochMonitor() {

			@Override
			public void beforeForward() {
			}

			@Override
			public void afterForward() {
				leftLabels.copyMatrix(discreteLayer.getOutputKeyValue().getKey(), false);
				rightLabels.copyMatrix(continuousLayer.getOutputKeyValue().getKey(), false);
			}

			@Override
			public void beforeBackward() {
			}

			@Override
			public void afterBackward() {
			}

		};
		graph.setMonitor(monitor);

		return graph;
	}

	private static boolean equalMatrix(DenseMatrix left, DenseMatrix right) {
		for (int row = 0; row < left.getRowSize(); row++) {
			for (int column = 0; column < left.getColumnSize(); column++) {
				if (left.getValue(row, column) != right.getValue(row, column)) {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void testShare() throws Exception {
		EnvironmentContext context = Nd4j.getAffinityManager().getClass().getSimpleName().equals("CpuAffinityManager") ? EnvironmentContext.CPU : EnvironmentContext.GPU;
		Future<?> task = context.doTask(() -> {
			MatrixFactory factory = new DenseMatrixFactory();

			DenseMatrix layerLeftInputs = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix layerLeftNumber = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix layerRightInputs = DenseMatrix.valueOf(numberOfSamples, continuousOrder);
			DenseMatrix layerRightNumber = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix layerLeftLabels = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix layerLeftOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix layerRightLabels = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix layerRightOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			double layerScore;
			{
				RandomUtility.setSeed(10);
				for (int point = 0; point < numberOfSamples; point++) {
					layerLeftInputs.setValue(point, 0, RandomUtility.randomInteger(5));
					layerLeftNumber.setValue(point, 0, RandomUtility.randomInteger(10) + 1);
					for (int index = 0; index < continuousOrder; index++) {
						layerRightInputs.setValue(point, index, RandomUtility.randomFloat(1F));
					}
					layerRightNumber.setValue(point, 0, RandomUtility.randomInteger(5) + 1);
				}

				Layer factorLayer = new ShareLayer(numberOfFactors + 2, numberOfFactors, 2, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
				Vertex shareVertex = new LayerVertex("factorCodec", factory, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
				Graph graph = getCrossGraph(shareVertex, layerLeftLabels, layerRightLabels);

				layerScore = graph.practice(numberOfIterations, new DenseMatrix[] { layerLeftInputs, layerLeftNumber, layerRightInputs, layerRightNumber }, new DenseMatrix[] { layerRightLabels, layerLeftLabels });
				graph.predict(new DenseMatrix[] { layerLeftInputs, layerLeftNumber, layerRightInputs, layerRightNumber }, new DenseMatrix[] { layerRightOutputs, layerLeftOutputs });
			}

			DenseMatrix vertexLeftInputs = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix vertexLeftNumber = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix vertexRightInputs = DenseMatrix.valueOf(numberOfSamples, continuousOrder);
			DenseMatrix vertexRightNumber = DenseMatrix.valueOf(numberOfSamples, 1);
			DenseMatrix vertexLeftLabels = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix vertexLeftOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix vertexRightLabels = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			DenseMatrix vertexRightOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
			double vertexScore;
			{
				RandomUtility.setSeed(10);
				for (int point = 0; point < numberOfSamples; point++) {
					vertexLeftInputs.setValue(point, 0, RandomUtility.randomInteger(5));
					vertexLeftNumber.setValue(point, 0, RandomUtility.randomInteger(10) + 1);
					for (int index = 0; index < continuousOrder; index++) {
						vertexRightInputs.setValue(point, index, RandomUtility.randomFloat(1F));
					}
					vertexRightNumber.setValue(point, 0, RandomUtility.randomInteger(5) + 1);
				}

				Layer factorLayer = new WeightLayer(numberOfFactors + 2, numberOfFactors, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
				Vertex shareVertex = new ShareVertex("factorCodec", factory, 2, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
				Graph graph = getCrossGraph(shareVertex, vertexLeftLabels, vertexRightLabels);

				vertexScore = graph.practice(numberOfIterations, new DenseMatrix[] { vertexLeftInputs, vertexLeftNumber, vertexRightInputs, vertexRightNumber }, new DenseMatrix[] { vertexRightLabels, vertexLeftLabels });
				graph.predict(new DenseMatrix[] { vertexLeftInputs, vertexLeftNumber, vertexRightInputs, vertexRightNumber }, new DenseMatrix[] { vertexRightOutputs, vertexLeftOutputs });
			}

			Assert.assertThat(layerScore, CoreMatchers.equalTo(vertexScore));
			Assert.assertTrue(equalMatrix(layerLeftLabels, vertexLeftLabels));
			Assert.assertTrue(equalMatrix(layerLeftOutputs, vertexLeftOutputs));
			Assert.assertTrue(equalMatrix(layerRightLabels, vertexRightLabels));
			Assert.assertTrue(equalMatrix(layerRightOutputs, vertexRightOutputs));
		});
		task.get();
	}

	@Test
	public void testModel() {
		MatrixFactory factory = new DenseMatrixFactory();
		Layer factorLayer = new WeightLayer(numberOfFactors + 2, numberOfFactors, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
		Vertex oldModel = new ShareVertex("factorCodec", factory, 2, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
		for (ModelCodec codec : ModelCodec.values()) {
			byte[] data = codec.encodeModel(oldModel);
			Vertex newModel = (Vertex) codec.decodeModel(data);
			Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
		}
	}

}
