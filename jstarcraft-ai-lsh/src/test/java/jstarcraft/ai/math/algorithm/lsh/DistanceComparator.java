package jstarcraft.ai.math.algorithm.lsh;

import java.util.Comparator;

import com.jstarcraft.ai.math.algorithm.correlation.MathDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

public class DistanceComparator implements Comparator<NameVector> {

    private final MathVector query;

    private final MathDistance distance;

    public DistanceComparator(NameVector query, MathDistance distance) {
        this.query = query;
        this.distance = distance;
    }

    @Override
    public int compare(NameVector left, NameVector right) {
        float leftDistance = distance.getCoefficient(query, left);
        float rightDistance = distance.getCoefficient(query, right);
        int compare = Float.compare(leftDistance, rightDistance);
        if (compare == 0) {
            compare = left.getName().compareTo(right.getName());
        }
        return compare;
    }

}
