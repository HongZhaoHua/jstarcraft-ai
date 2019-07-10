package com.jstarcraft.ai.search;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

/**
 * 瞬时化搜索器
 * 
 * @author Birdy
 *
 */
public class TransienceManager implements LuceneManager {

    private AtomicBoolean changed = new AtomicBoolean(false);

    private IndexWriterConfig config;

    private Directory directory;

    private IndexWriter writer;

    private DirectoryReader reader;

    public TransienceManager(IndexWriterConfig config) throws Exception {
        this.config = config;
        this.directory = new ByteBuffersDirectory();
        this.writer = new IndexWriter(this.directory, this.config);
        this.reader = DirectoryReader.open(this.writer);
    }

    @Override
    public IndexWriter getWriter() {
        return writer;
    }

    @Override
    public IndexReader getReader() throws Exception {
        if (changed.compareAndSet(true, false)) {
            this.writer.flush();
            DirectoryReader reader = DirectoryReader.openIfChanged(this.reader);
            if (reader != null) {
                this.reader.close();
                this.reader = reader;
            }
        }
        return this.reader;
    }

    @Override
    public boolean isChanged() {
        return changed.get();
    }

    Directory getDirectory() {
        return this.directory;
    }

}
