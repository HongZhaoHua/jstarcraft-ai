package com.jstarcraft.ai.math.algorithm.correlation.distance;

import com.jstarcraft.ai.math.algorithm.correlation.AbstractDistance;
import com.jstarcraft.ai.math.structure.vector.MathVector;

/**
 * 角距离
 * 
 * @author Birdy
 *
 */
public class AngularDistance extends AbstractDistance {

    private static float calculateTheta(MathVector vector) {
        float theta = 0F;
        float r = (float) Math.sqrt(vector.getValue(0) * vector.getValue(0) + vector.getValue(1) * vector.getValue(1));
        theta = (float) Math.atan2(r, vector.getValue(2));
        return theta;
    }

    private static float calculatePhi(MathVector vector) {
        float phi = 0F;
        phi = (float) Math.atan2(vector.getValue(1), vector.getValue(0));
        if (phi < 0F) {
            phi += 2F * Math.PI;
        }
        return phi;
    }

    @Override
    public float getCoefficient(MathVector leftVector, MathVector rightVector) {
        float leftTheta = calculateTheta(leftVector);
        float leftPhi = calculatePhi(leftVector);
        float rightTheta = calculateTheta(rightVector);
        float rightPhi = calculatePhi(rightVector);
        float distanceTheta = Math.abs(leftTheta - rightTheta);
        float distancePhi = Math.abs(leftPhi - rightPhi);
        if (distancePhi > Math.PI) {
            distancePhi = (float) (2F * Math.PI - distancePhi);
        }
        float distance = distanceTheta * distanceTheta + distancePhi * distancePhi;
        if (distance != 0F)
            distance = (float) Math.sqrt(distance);
        return distance;
    }

}
