package be.tarsos.lsh.families;

import be.tarsos.lsh.Vector;

public class CosineDistance implements DistanceMeasure {

    @Override
    public float distance(Vector one, Vector other) {
        float distance = 0F;
        float similarity = one.dot(other) / (float) Math.sqrt(one.dot(one) * other.dot(other));
        distance = 1 - similarity;
        return distance;
    }
}
