package com.jstarcraft.ai.model.rule.supervised.regress;

import com.jstarcraft.ai.data.DataInstance;
import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.model.supervised.Practicer;
import com.jstarcraft.ai.model.supervised.Predictor;

/**
 * 零规则回归器
 * 
 * @author Birdy
 *
 */
public class ZeroRuleRegressor implements Practicer, Predictor {

    private float quantity;

    @Override
    public void practice(DataModule module, DataModule... contexts) {
        float markSum = 0F;
        float weightSum = 0F;
        for (DataInstance instance : module) {
            markSum += instance.getQuantityMark() * instance.getWeight();
            weightSum += instance.getWeight();
        }
        quantity = markSum / weightSum;
    }

    @Override
    public void predict(DataInstance instance, DataInstance... contexts) {
        instance.setQuantityMark(quantity);
    }

}
