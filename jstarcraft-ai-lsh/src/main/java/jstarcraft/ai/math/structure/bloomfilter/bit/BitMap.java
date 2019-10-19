package jstarcraft.ai.math.structure.bloomfilter.bit;

/**
 * 
 * @author Birdy
 *
 */
// TODO 考虑实现Int2BooleanMap接口
public interface BitMap {

    boolean get(int index);

    void set(int index);

    void unset(int index);
    
    int capacity();
    
    int size();

}