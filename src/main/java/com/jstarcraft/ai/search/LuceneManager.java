package com.jstarcraft.ai.search;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.LeafCollector;
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
     * 获取采集器
     * 
     * @param context
     * @param collector
     * @return
     * @throws IOException
     */
    LeafCollector getCollector(LeafReaderContext context, LeafCollector collector) throws IOException;

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
