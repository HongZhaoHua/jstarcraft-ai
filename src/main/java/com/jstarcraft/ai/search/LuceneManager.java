package com.jstarcraft.ai.search;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

/**
 * Lucene管理器
 * 
 * @author Birdy
 *
 */
public interface LuceneManager {

    /**
     * 是否变更
     * 
     * @return
     */
    boolean isChanged();

    /**
     * 获取写入器
     * 
     * @return
     */
    IndexWriter getWriter() throws Exception;

    /**
     * 获取读取器
     * 
     * @return
     */
    IndexReader getReader() throws Exception;

}
