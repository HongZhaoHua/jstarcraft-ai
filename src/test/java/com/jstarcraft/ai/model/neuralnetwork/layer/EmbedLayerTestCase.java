package com.jstarcraft.ai.model.neuralnetwork.layer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.layers.AbstractLayer;
import org.deeplearning4j.nn.params.DefaultParamInitializer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import com.jstarcraft.ai.math.structure.DenseCache;
import com.jstarcraft.ai.math.structure.MathCache;
import com.jstarcraft.ai.model.neuralnetwork.activation.SigmoidActivationFunction;
import com.jstarcraft.ai.model.neuralnetwork.layer.EmbedLayer;
import com.jstarcraft.ai.model.neuralnetwork.layer.Layer;
import com.jstarcraft.ai.model.neuralnetwork.layer.ParameterConfigurator;
import com.jstarcraft.ai.model.neuralnetwork.layer.WeightLayer;
import com.jstarcraft.ai.model.neuralnetwork.layer.Layer.Mode;
import com.jstarcraft.ai.model.neuralnetwork.parameter.CopyParameterFactory;
import com.jstarcraft.core.utility.KeyValue;

public class EmbedLayerTestCase extends LayerTestCase {

	@Override
	protected INDArray getData() {
		return Nd4j.linspace(0, 4, 5).reshape(5, 1);
	}

	@Override
	protected INDArray getError() {
		return Nd4j.linspace(-2.5D, 2.5D, 10).reshape(5, 2);
	}

	@Override
	protected List<KeyValue<String, String>> getGradients() {
		List<KeyValue<String, String>> gradients = new LinkedList<>();
		gradients.add(new KeyValue<>(DefaultParamInitializer.WEIGHT_KEY, EmbedLayer.WEIGHT_KEY));
		gradients.add(new KeyValue<>(DefaultParamInitializer.BIAS_KEY, EmbedLayer.BIAS_KEY));
		return gradients;
	}

	@Override
	protected AbstractLayer<?> getOldFunction() {
		NeuralNetConfiguration neuralNetConfiguration = new NeuralNetConfiguration();
		EmbeddingLayer layerConfiguration = new EmbeddingLayer();
		layerConfiguration.setWeightInit(WeightInit.UNIFORM);
		layerConfiguration.setNIn(5);
		layerConfiguration.setNOut(2);
		layerConfiguration.setActivationFn(new ActivationSigmoid());
		layerConfiguration.setL1(0.01D);
		layerConfiguration.setL1Bias(0.01D);
		layerConfiguration.setL2(0.05D);
		layerConfiguration.setL2Bias(0.05D);
		neuralNetConfiguration.setLayer(layerConfiguration);
		AbstractLayer<?> layer = AbstractLayer.class.cast(layerConfiguration.instantiate(neuralNetConfiguration, null, 0, Nd4j.zeros(12), true));
		layer.setBackpropGradientsViewArray(Nd4j.zeros(12));
		return layer;
	}

	@Override
	protected Layer getNewFunction(AbstractLayer<?> layer) {
		Map<String, ParameterConfigurator> configurators = new HashMap<>();
		CopyParameterFactory weight = new CopyParameterFactory(getMatrix(layer.getParam(DefaultParamInitializer.WEIGHT_KEY)));
		configurators.put(WeightLayer.WEIGHT_KEY, new ParameterConfigurator(0.01F, 0.05F, weight));
		CopyParameterFactory bias = new CopyParameterFactory(getMatrix(layer.getParam(DefaultParamInitializer.BIAS_KEY)));
		configurators.put(WeightLayer.BIAS_KEY, new ParameterConfigurator(0.01F, 0.05F, bias));
		MathCache factory = new DenseCache();
		return new EmbedLayer(5, 2, factory, configurators, Mode.TRAIN, new SigmoidActivationFunction());
	}

}
