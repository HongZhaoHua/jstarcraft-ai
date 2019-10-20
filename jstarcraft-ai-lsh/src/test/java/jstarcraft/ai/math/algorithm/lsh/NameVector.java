package jstarcraft.ai.math.algorithm.lsh;

import java.util.Arrays;

import com.jstarcraft.ai.math.structure.vector.ArrayVector;

public class NameVector extends ArrayVector {

    private String name;

    public NameVector(String name, int dimensions) {
        this(name, new float[dimensions]);
    }

    public NameVector(String name, NameVector vector) {
        this(name, Arrays.copyOf(vector.values, vector.values.length));
    }

    public NameVector(String name, float[] values) {
        super(values.length, values);
        this.name = name;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        NameVector that = (NameVector) object;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return name + ":" + super.toString();
    }

}
