package com.jstarcraft.ai.model;

/**
 * 可更新
 * 
 * @author Birdy
 *
 */
public interface Updateable<I> {

    /**
     * 更新(独立)
     * 
     * @param instance
     */
    void update(I instance);

    /**
     * 更新(批量)
     * 
     * @param instances
     */
    default void update(Iterable<I> instances) {
        for (I instance : instances) {
            update(instance);
        }
    }

}
