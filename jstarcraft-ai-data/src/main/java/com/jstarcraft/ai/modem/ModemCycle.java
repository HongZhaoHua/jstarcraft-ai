package com.jstarcraft.ai.modem;

/**
 * 调制解调周期
 * 
 * @author Birdy
 *
 */
public interface ModemCycle {

    /**
     * 在保存之前执行
     */
    void beforeSave();

    /**
     * 在加载之后执行
     */
    void afterLoad();

}
