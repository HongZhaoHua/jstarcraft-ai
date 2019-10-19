package jstarcraft.ai.math.algorithm.lsh;

import java.util.Comparator;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class DistanceComparator implements Comparator<MathVector> {

    private final MathVector query;

    private final MathDistance distance;

    public DistanceComparator(MathVector query, MathDistance distanceMeasure) {
        this.query = query;
        this.distance = distanceMeasure;
    }

    @Override
    public int compare(MathVector left, MathVector right) {
        float leftDistance = distance.getCoefficient(query, left);
        float rightDistance = distance.getCoefficient(query, right);
        return Float.compare(leftDistance, rightDistance);
    }

}
