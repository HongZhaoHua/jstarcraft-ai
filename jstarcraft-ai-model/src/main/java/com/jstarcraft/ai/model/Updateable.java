package com.jstarcraft.ai.model;

/**
 * 可更新
 * 
 * @author Birdy
 *
 */
public interface Updateable<I> {

    void update(I instance);

    default void update(Iterable<I> instances) {
        for (I instance : instances) {
            update(instance);
        }
    }

}
