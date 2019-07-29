package com.jstarcraft.ai.model.neuralnetwork.vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.environment.EnvironmentContext;
import com.jstarcraft.ai.environment.EnvironmentFactory;
import com.jstarcraft.ai.math.structure.DenseCache;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.math.structure.matrix.DenseMatrix;
import com.jstarcraft.ai.model.neuralnetwork.EpochMonitor;
import com.jstarcraft.ai.model.neuralnetwork.Graph;
import com.jstarcraft.ai.model.neuralnetwork.GraphConfigurator;
import com.jstarcraft.ai.model.neuralnetwork.activation.SigmoidActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.layer.EmbedLayer;
import com.jstarcraft.ai.model.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.model.neuralnetwork.layer.ParameterConfigurator;
import com.jstarcraft.ai.model.neuralnetwork.layer.WeightLayer;
import com.jstarcraft.ai.model.neuralnetwork.learn.SgdLearner;
import com.jstarcraft.ai.model.neuralnetwork.loss.MSELossFunction;
import com.jstarcraft.ai.model.neuralnetwork.normalization.IgnoreNormalizer;
import com.jstarcraft.ai.model.neuralnetwork.optimization.StochasticGradientOptimizer;
import com.jstarcraft.ai.model.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.model.neuralnetwork.schedule.Schedule;
import com.jstarcraft.ai.model.neuralnetwork.vertex.accumulation.SumVertex;
import com.jstarcraft.ai.model.neuralnetwork.vertex.transformation.HorizontalAttachVertex;
import com.jstarcraft.ai.model.neuralnetwork.vertex.transformation.HorizontalDetachVertex;
import com.jstarcraft.ai.modem.ModemCodec;
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
    private int qualityDimension = 5;
    private int quantityOrder = 5;
    private int numberOfSamples = 20;
    private int numberOfIterations = 10;

    private Graph getCrossGraph(Vertex shareVertex, DenseMatrix leftMarks, DenseMatrix rightMarks) {
        GraphConfigurator configurator = new GraphConfigurator();
        MathCache factory = new DenseCache();
        // 离散特征部分
        Layer qualityLayer = new EmbedLayer(qualityDimension, numberOfFactors, factory, configurators, new SigmoidActivationFunction());
        configurator.connect(new LayerVertex("qualityFeature", factory, qualityLayer, new SgdLearner(schedule), new IgnoreNormalizer()));
        configurator.connect(new SumVertex("qualityNumber", factory));

        // 连续特征部分(归一化)
        Layer quantityLayer = new WeightLayer(quantityOrder, numberOfFactors, factory, configurators, new SigmoidActivationFunction());
        configurator.connect(new LayerVertex("quantityFeatures", factory, quantityLayer, new SgdLearner(schedule), new IgnoreNormalizer()));
        configurator.connect(new SumVertex("quantityNumber", factory));

        // 编解码部分
        configurator.connect(new HorizontalAttachVertex("leftStack", factory), "qualityFeature", "qualityNumber", "quantityNumber");
        configurator.connect(new HorizontalAttachVertex("rightStack", factory), "quantityFeatures", "quantityNumber", "qualityNumber");
        configurator.connect(new HorizontalAttachVertex("factorStack", factory), "leftStack", "rightStack");

        configurator.connect(shareVertex, "factorStack");

        configurator.connect(new HorizontalDetachVertex("leftUnstack", factory, 0, numberOfFactors), "factorCodec");
        configurator.connect(new HorizontalDetachVertex("rightUntack", factory, numberOfFactors, numberOfFactors * 2), "factorCodec");

        Graph graph = new Graph(configurator, new StochasticGradientOptimizer(), new MSELossFunction(), new MSELossFunction());

        EpochMonitor monitor = new EpochMonitor() {

            @Override
            public void beforeForward() {
            }

            @Override
            public void afterForward() {
                leftMarks.copyMatrix(qualityLayer.getOutputKeyValue().getKey(), false);
                rightMarks.copyMatrix(quantityLayer.getOutputKeyValue().getKey(), false);
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
        EnvironmentContext context = EnvironmentFactory.getContext();
        Future<?> task = context.doTask(() -> {
            MathCache factory = new DenseCache();

            DenseMatrix layerLeftInputs = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix layerLeftNumber = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix layerRightInputs = DenseMatrix.valueOf(numberOfSamples, quantityOrder);
            DenseMatrix layerRightNumber = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix layerLeftMarks = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix layerLeftOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix layerRightMarks = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix layerRightOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            double layerScore;
            {
                RandomUtility.setSeed(10);
                for (int point = 0; point < numberOfSamples; point++) {
                    layerLeftInputs.setValue(point, 0, RandomUtility.randomInteger(5));
                    layerLeftNumber.setValue(point, 0, RandomUtility.randomInteger(10) + 1);
                    for (int index = 0; index < quantityOrder; index++) {
                        layerRightInputs.setValue(point, index, RandomUtility.randomFloat(1F));
                    }
                    layerRightNumber.setValue(point, 0, RandomUtility.randomInteger(5) + 1);
                }

                Layer factorLayer = new ShareLayer(numberOfFactors + 2, numberOfFactors, 2, factory, configurators, new SigmoidActivationFunction());
                Vertex shareVertex = new LayerVertex("factorCodec", factory, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
                Graph graph = getCrossGraph(shareVertex, layerLeftMarks, layerRightMarks);

                layerScore = graph.practice(numberOfIterations, new DenseMatrix[] { layerLeftInputs, layerLeftNumber, layerRightInputs, layerRightNumber }, new DenseMatrix[] { layerRightMarks, layerLeftMarks });
                graph.predict(new DenseMatrix[] { layerLeftInputs, layerLeftNumber, layerRightInputs, layerRightNumber }, new DenseMatrix[] { layerRightOutputs, layerLeftOutputs });
            }

            DenseMatrix vertexLeftInputs = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix vertexLeftNumber = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix vertexRightInputs = DenseMatrix.valueOf(numberOfSamples, quantityOrder);
            DenseMatrix vertexRightNumber = DenseMatrix.valueOf(numberOfSamples, 1);
            DenseMatrix vertexLeftMarks = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix vertexLeftOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix vertexRightMarks = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            DenseMatrix vertexRightOutputs = DenseMatrix.valueOf(numberOfSamples, numberOfFactors);
            double vertexScore;
            {
                RandomUtility.setSeed(10);
                for (int point = 0; point < numberOfSamples; point++) {
                    vertexLeftInputs.setValue(point, 0, RandomUtility.randomInteger(5));
                    vertexLeftNumber.setValue(point, 0, RandomUtility.randomInteger(10) + 1);
                    for (int index = 0; index < quantityOrder; index++) {
                        vertexRightInputs.setValue(point, index, RandomUtility.randomFloat(1F));
                    }
                    vertexRightNumber.setValue(point, 0, RandomUtility.randomInteger(5) + 1);
                }

                Layer factorLayer = new WeightLayer(numberOfFactors + 2, numberOfFactors, factory, configurators, new SigmoidActivationFunction());
                Vertex shareVertex = new ShareVertex("factorCodec", factory, 2, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
                Graph graph = getCrossGraph(shareVertex, vertexLeftMarks, vertexRightMarks);

                vertexScore = graph.practice(numberOfIterations, new DenseMatrix[] { vertexLeftInputs, vertexLeftNumber, vertexRightInputs, vertexRightNumber }, new DenseMatrix[] { vertexRightMarks, vertexLeftMarks });
                graph.predict(new DenseMatrix[] { vertexLeftInputs, vertexLeftNumber, vertexRightInputs, vertexRightNumber }, new DenseMatrix[] { vertexRightOutputs, vertexLeftOutputs });
            }

            Assert.assertThat(layerScore, CoreMatchers.equalTo(vertexScore));
            Assert.assertTrue(equalMatrix(layerLeftMarks, vertexLeftMarks));
            Assert.assertTrue(equalMatrix(layerLeftOutputs, vertexLeftOutputs));
            Assert.assertTrue(equalMatrix(layerRightMarks, vertexRightMarks));
            Assert.assertTrue(equalMatrix(layerRightOutputs, vertexRightOutputs));
        });
        task.get();
    }

    @Test
    public void testModel() {
        MathCache factory = new DenseCache();
        Layer factorLayer = new WeightLayer(numberOfFactors + 2, numberOfFactors, factory, configurators, new SigmoidActivationFunction());
        Vertex oldModel = new ShareVertex("factorCodec", factory, 2, factorLayer, new SgdLearner(schedule), new IgnoreNormalizer());
        for (ModemCodec codec : ModemCodec.values()) {
            byte[] data = codec.encodeModel(oldModel);
            Vertex newModel = (Vertex) codec.decodeModel(data);
            Assert.assertThat(newModel, CoreMatchers.equalTo(oldModel));
        }
    }

}
