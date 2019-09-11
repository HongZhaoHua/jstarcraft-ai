package com.jstarcraft.ai.model.neuralnetwork.learn;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jstarcraft.ai.math.structure.matrix.MathMatrix;
import com.jstarcraft.ai.model.neuralnetwork.schedule.ConstantSchedule;
import com.jstarcraft.ai.model.neuralnetwork.schedule.Schedule;
import com.jstarcraft.ai.modem.ModemDefinition;

/**
 * SGD学习器
 * 
 * <pre>
 * 参考Deeplearning4j团队
 * </pre>
 * 
 * @author Birdy
 *
 */
@ModemDefinition(value = { "learnSchedule" })
public class SgdLearner implements Learner {

    public static final float DEFAULT_SGD_LR = 1E-3F;

    private Schedule learnSchedule;

    public SgdLearner() {
        this(new ConstantSchedule(DEFAULT_SGD_LR));
    }

    public SgdLearner(Schedule learnSchedule) {
        this.learnSchedule = learnSchedule;
    }

    @Override
    public void doCache(Map<String, MathMatrix> gradients) {
    }

    @Override
    public void learn(Map<String, MathMatrix> gradients, int iteration, int epoch) {
        float learnRatio = learnSchedule.valueAt(iteration, epoch);
        for (MathMatrix gradient : gradients.values()) {
            gradient.scaleValues(learnRatio);
        }
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
            SgdLearner that = (SgdLearner) object;
            EqualsBuilder equal = new EqualsBuilder();
            equal.append(this.learnSchedule, that.learnSchedule);
            return equal.isEquals();
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(learnSchedule);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "SgdLearner(learnSchedule=" + learnSchedule + ")";
    }

}
