package be.tarsos.lsh.families;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class CosineDistance implements DistanceMeasure {

    @Override
    public float distance(MathVector left, MathVector right) {
        MathScalar scalar = DefaultScalar.getInstance();
        float distance = 0F;
        float similarity = scalar.dotProduct(left, right).getValue() / (float) Math.sqrt(scalar.dotProduct(left, left).getValue() * scalar.dotProduct(right, right).getValue());
        distance = 1 - similarity;
        return distance;
    }
}
