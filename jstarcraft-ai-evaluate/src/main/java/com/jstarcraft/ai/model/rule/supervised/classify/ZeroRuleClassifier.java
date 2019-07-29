package com.jstarcraft.ai.model.rule.supervised.classify;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.model.supervised.Practicer;
import com.jstarcraft.ai.model.supervised.Predictor;

/**
 * 零规则分类器
 * 
 * @author Birdy
 *
 */
public class ZeroRuleClassifier implements Practicer, Predictor {

	private int dimension;

	private int quality;

	public ZeroRuleClassifier(int dimension) {
		this.dimension = dimension;
	}

	@Override
	public void practice(DataModule module, DataModule... contexts) {
		float[] markSums = new float[dimension];
		for (DataInstance instance : module) {
			markSums[instance.getQualityMark()] += instance.getWeight();
		}
		float current = Float.MIN_VALUE;
		for (int index = 0; index < dimension; index++) {
			if (current < markSums[index]) {
				quality = index;
				current = markSums[index];
			}
		}
	}

	@Override
	public void predict(DataInstance instance, DataInstance... contexts) {
		instance.setQualityMark(quality);
	}

}
