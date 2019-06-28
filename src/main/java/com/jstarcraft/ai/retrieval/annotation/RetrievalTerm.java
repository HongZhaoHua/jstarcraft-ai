package com.jstarcraft.ai.retrieval.annotation;

/**
 * 检索词向量
 * 
 * @author Birdy
 *
 */
public @interface RetrievalTerm {

    /** 词频 */
    boolean frequency();

    /** 位置 */
    boolean position();

    /** 偏移 */
    boolean offset();

}
