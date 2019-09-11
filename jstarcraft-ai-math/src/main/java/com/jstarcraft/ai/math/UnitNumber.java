package com.jstarcraft.ai.math;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.math3.util.FastMath;

/**
 * 单位数值
 * 
 * <pre>
 * 运算速度比Big快很多,存储空间比Big小很多.
 * </pre>
 * 
 * @author Birdy
 *
 */
public class UnitNumber implements Comparable<UnitNumber> {

    public final static float ZERO = 0F;

    public final static float ONE = 1F;

    public final static float KILO = 1000F;

    private final static float[] powers = new float[10];

    static {
        for (int power = 0; power < 10; power++) {
            powers[power] = (float) FastMath.pow(KILO, power);
        }
    }

    private static int ignore = 5;

    /** 单位(1代表千位,2代表百万位,3代表十亿位,以此类推) */
    private int unit;

    /** 值 */
    private float number;

    public UnitNumber() {
    }

    public UnitNumber(int unit, float number) {
        if (!Float.isFinite(number)) {
            throw new IllegalArgumentException();
        }
        if (number != ZERO) {
            this.unit = unit;
            this.number = number;
            this.adjust();
        }
    }

    private void adjust() {
        if (this.number == ZERO) {
            this.unit = 0;
            return;
        }
        while (FastMath.abs(this.number) < ONE) {
            this.unit--;
            this.number *= KILO;
        }
        while (FastMath.abs(this.number) >= KILO) {
            this.unit++;
            this.number /= KILO;
        }
    }

    public UnitNumber add(UnitNumber that) {
        return add(that.unit, that.number);
    }

    public UnitNumber add(int unit, float number) {
        int leftUnit = this.unit;
        float leftNumber = this.number;
        int rightUnit = unit;
        float rightNumber = number;

        if (leftNumber == ZERO) {
            this.unit = rightUnit;
            this.number = rightNumber;
            return this;
        }
        if (rightNumber == ZERO) {
            return this;
        }

        if (FastMath.abs(leftUnit - rightUnit) >= ignore) {
            if (leftUnit > rightUnit) {
                return this;
            } else {
                this.unit = rightUnit;
                this.number = rightNumber;
                return this;
            }
        } else {
            unit = leftUnit < rightUnit ? leftUnit : rightUnit;
            // 加法必须在相同基准运算
            leftNumber = leftNumber * powers[leftUnit - unit];
            rightNumber = rightNumber * powers[rightUnit - unit];
            number = leftNumber + rightNumber;
            this.unit = unit;
            this.number = number;
            adjust();
            return this;
        }
    }

    public UnitNumber subtract(UnitNumber that) {
        return subtract(that.unit, that.number);
    }

    public UnitNumber subtract(int unit, float number) {
        int leftUnit = this.unit;
        float leftNumber = this.number;
        int rightUnit = unit;
        float rightNumber = number;

        if (leftNumber == ZERO) {
            this.unit = rightUnit;
            this.number = -rightNumber;
            return this;
        }
        if (rightNumber == ZERO) {
            return this;
        }

        if (FastMath.abs(leftUnit - rightUnit) >= ignore) {
            if (leftUnit > rightUnit) {
                return this;
            } else {
                this.unit = rightUnit;
                this.number = -rightNumber;
                return this;
            }
        } else {
            unit = leftUnit < rightUnit ? leftUnit : rightUnit;
            // 减法必须在相同基准运算
            leftNumber = leftNumber * powers[leftUnit - unit];
            rightNumber = rightNumber * powers[rightUnit - unit];
            number = leftNumber - rightNumber;
            this.unit = unit;
            this.number = number;
            adjust();
            return this;
        }
    }

    public UnitNumber multiply(UnitNumber that) {
        return multiply(that.unit, that.number);
    }

    public UnitNumber multiply(int unit, float number) {
        int leftUnit = this.unit;
        float leftNumber = this.number;
        int rightUnit = unit;
        float rightNumber = number;

        if (leftNumber == ZERO) {
            return this;
        }
        if (rightNumber == ZERO) {
            this.number = ZERO;
            adjust();
            return this;
        }

        this.unit = leftUnit + rightUnit;
        this.number = leftNumber * rightNumber;
        adjust();
        return this;
    }

    public UnitNumber divide(UnitNumber that) {
        return divide(that.unit, that.number);
    }

    public UnitNumber divide(int unit, float number) {
        int leftUnit = this.unit;
        float leftNumber = this.number;
        int rightUnit = unit;
        float rightNumber = number;

        if (leftNumber == ZERO) {
            return this;
        }
        if (rightNumber == ZERO) {
            throw new ArithmeticException();
        }

        this.unit = leftUnit - rightUnit;
        this.number = leftNumber / rightNumber;
        adjust();
        return this;
    }

    public int getUnit() {
        return unit;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public int compareTo(UnitNumber that) {
        if (this == that) {
            return 0;
        }
        if (this.number == ZERO || that.number == ZERO) {

            if (this.number < that.number) {
                return -1;
            } else if (this.number > that.number) {
                return 1;
            } else {
                return 0;
            }
        }

        if (this.unit < that.unit) {
            return -1;
        }
        if (this.unit > that.unit) {
            return 1;
        }

        if (this.number < that.number) {
            return -1;
        } else if (this.number > that.number) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        UnitNumber that = (UnitNumber) object;
        EqualsBuilder equal = new EqualsBuilder();
        equal.append(this.unit, that.unit);
        equal.append(this.number, that.number);
        return equal.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hash = new HashCodeBuilder();
        hash.append(unit);
        hash.append(number);
        return hash.toHashCode();
    }

    @Override
    public String toString() {
        return "[unit=" + this.unit + ",value=" + this.number + "]";
    }

    public static UnitNumber add(UnitNumber left, UnitNumber right) {
        int leftUnit = left.unit;
        float leftNumber = left.number;
        int rightUnit = right.unit;
        float rightNumber = right.number;

        if (leftNumber == ZERO) {
            return right;
        }
        if (rightNumber == ZERO) {
            return left;
        }

        if (FastMath.abs(leftUnit - rightUnit) >= ignore) {
            if (leftUnit > rightUnit) {
                return left;
            } else {
                return right;
            }
        } else {
            int unit = leftUnit < rightUnit ? leftUnit : rightUnit;
            // 加法必须在相同基准运算
            leftNumber = leftNumber * powers[leftUnit - unit];
            rightNumber = rightNumber * powers[rightUnit - unit];
            float value = leftNumber + rightNumber;
            UnitNumber instance = new UnitNumber(unit, value);
            return instance;
        }
    }

    public static UnitNumber subtract(UnitNumber left, UnitNumber right) {
        int leftUnit = left.unit;
        float leftNumber = left.number;
        int rightUnit = right.unit;
        float rightNumber = right.number;

        if (leftNumber == ZERO) {
            return new UnitNumber(rightUnit, -rightNumber);
        }
        if (rightNumber == ZERO) {
            return left;
        }

        if (FastMath.abs(leftUnit - rightUnit) >= ignore) {
            if (leftUnit > rightUnit) {
                return left;
            } else {
                return new UnitNumber(rightUnit, -rightNumber);
            }
        } else {
            int unit = leftUnit < rightUnit ? leftUnit : rightUnit;
            // 减法必须在相同基准运算
            leftNumber = leftNumber * powers[leftUnit - unit];
            rightNumber = rightNumber * powers[rightUnit - unit];
            float value = leftNumber - rightNumber;
            UnitNumber instance = new UnitNumber(unit, value);
            return instance;
        }
    }

    public static UnitNumber multiply(UnitNumber left, UnitNumber right) {
        int leftUnit = left.unit;
        float leftNumber = left.number;
        int rightUnit = right.unit;
        float rightNumber = right.number;

        if (leftNumber == ZERO) {
            return new UnitNumber();
        }
        if (rightNumber == ZERO) {
            return new UnitNumber();
        }

        float value = leftNumber * rightNumber;
        UnitNumber instance = new UnitNumber(leftUnit + rightUnit, value);
        return instance;
    }

    public static UnitNumber divide(UnitNumber left, UnitNumber right) {
        int leftUnit = left.unit;
        float leftNumber = left.number;
        int rightUnit = right.unit;
        float rightNumber = right.number;

        if (leftNumber == ZERO) {
            return new UnitNumber();
        }
        if (rightNumber == ZERO) {
            throw new ArithmeticException();
        }

        float value = leftNumber / rightNumber;
        UnitNumber instance = new UnitNumber(leftUnit - rightUnit, value);
        return instance;
    }

    public static void setIgnore(int ignore) {
        assert ignore >= 0 && ignore < 10;
        UnitNumber.ignore = ignore;
    }

    public static int getIgnore() {
        return UnitNumber.ignore;
    }

}
