package com.jstarcraft.ai.retrieval.annotation;

import java.io.Reader;

/**
 * 检索分词
 * 
 * <pre>
 *  仅作用于{@link String}/{@link Reader}类型字段的注解.
 * </pre>
 * 
 * @author Birdy
 *
 */
public @interface RetrievalAnalyze {

    /** 反向词向量(取决于是否需要查询) */
    RetrievalTerm negative() default @RetrievalTerm(frequency = false, position = false, offset = false);

    /** 正向词向量(取决于是否需要高亮) */
    RetrievalTerm positive() default @RetrievalTerm(frequency = false, position = false, offset = false);

}
