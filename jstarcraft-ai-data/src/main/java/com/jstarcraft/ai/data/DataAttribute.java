package com.jstarcraft.ai.data;

/**
 * 数据属性
 * 
 * @author Birdy
 *
 */
public interface DataAttribute<T> {

    /**
     * 获取属性名称
     * 
     * @return
     */
    String getName();

    /**
     * 获取属性类型
     * 
     * @return
     */
    Class<T> getType();

}
