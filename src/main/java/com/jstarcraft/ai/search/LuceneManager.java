package com.jstarcraft.ai.search;

import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

/**
 * Lucene管理器
 * 
 * @author Birdy
 *
 */
public interface LuceneManager {

    /**
     * 开启
     */
    void open();

    /**
     * 关闭
     */
    void close();

    /**
     * 是否变更
     * 
     * @return
     */
    boolean isChanged();

    /**
     * 获取已创建标识
     * 
     * @return
     */
    Set<String> getCreatedIds();

    /**
     * 获取已变更标识
     * 
     * @return
     */
    Object2LongMap<String> getUpdatedIds();

    /**
     * 获取已删除标识
     * 
     * @return
     */
    Set<String> getDeletedIds();

    /**
     * 获取目录
     * 
     * @return
     */
    Directory getDirectory();

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
