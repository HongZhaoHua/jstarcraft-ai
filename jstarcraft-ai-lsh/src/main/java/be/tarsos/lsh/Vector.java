package be.tarsos.lsh;

import java.util.Arrays;

import com.jstarcraft.ai.math.structure.DefaultScalar;
import com.jstarcraft.ai.math.structure.MathScalar;
import com.jstarcraft.ai.math.structure.vector.ArrayVector;

/**
 * An Vector contains a vector of 'dimension' values. It serves as the main data
 * structure that is stored and retrieved. It also has an identifier (key).
 * 
 * @author Joren Six
 */
public class Vector extends ArrayVector {

    /**
     * An optional key, identifier for the vector.
     */
    private String key;

    /**
     * Creates a new vector with the requested number of dimensions.
     * 
     * @param dimensions The number of dimensions.
     */
    public Vector(String key, int dimensions) {
        this(key, new float[dimensions]);
    }

    /**
     * Copy constructor.
     * 
     * @param other The other vector.
     */
    public Vector(String key, Vector other) {
        // copy the values
        this(key, Arrays.copyOf(other.values, other.values.length));
    }

    /**
     * Creates a vector with the values and a key
     * 
     * @param key    The key of the vector.
     * @param values The values of the vector.
     */
    public Vector(String key, float[] values) {
        super(values.length, values);
        this.key = key;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    /**
     * Calculates the dot product, or scalar product, of this vector with the other
     * vector.
     * 
     * @param other The other vector, should have the same number of dimensions.
     * @return The dot product of this vector with the other vector.
     * @exception ArrayIndexOutOfBoundsException when the two vectors do not have
     *                                           the same dimensions.
     */
    public float dot(Vector other) {
        MathScalar scalar = DefaultScalar.getInstance();
        return scalar.dotProduct(this, other).getValue();
//        float sum = 0F;
//        for (int i = 0; i < getDimensions(); i++) {
//            sum += values[i] * other.values[i];
//        }
//        return sum;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        Vector that = (Vector) object;
        return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

}
