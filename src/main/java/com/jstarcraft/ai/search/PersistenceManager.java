package com.jstarcraft.ai.search;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

/**
 * 持久化搜索器
 * 
 * @author Birdy
 *
 */
public class PersistenceManager implements LuceneManager {

    private AtomicBoolean changed = new AtomicBoolean(false);

    private TransienceManager transienceManager;

    private Set<String> createdIds;

    private Object2LongMap<String> updatedIds;

    private Set<String> deletedIds;

    private IndexWriterConfig config;

    private Directory directory;

    private IndexWriter writer;

    private DirectoryReader reader;

    public PersistenceManager(IndexWriterConfig config, Path path) throws Exception {
        this.config = config;
        this.directory = FSDirectory.open(path);
        this.writer = new IndexWriter(this.directory, this.config);
        this.reader = DirectoryReader.open(this.writer);
    }

    public synchronized void mergeInstances(Set<String> createdIds, Object2LongMap<String> updatedIds, Set<String> deletedIds, TransienceManager transienceManager) throws Exception {
        this.createdIds.addAll(createdIds);
        this.updatedIds.putAll(updatedIds);
        this.deletedIds.addAll(deletedIds);

        this.transienceManager = transienceManager;
        Term[] terms = new Term[updatedIds.size() + updatedIds.size()];
        int index = 0;
        for (String id : updatedIds.keySet()) {
            terms[index++] = new Term(LuceneSearcher.ID, id.toString());
        }
        for (String id : deletedIds) {
            terms[index++] = new Term(LuceneSearcher.ID, id.toString());
        }
        this.writer.deleteDocuments(terms);
        this.writer.addIndexes(this.transienceManager.getDirectory());
        this.transienceManager = null;

        this.createdIds.clear();
        this.updatedIds.clear();
        this.deletedIds.clear();
    }

    public IndexReader getReader() throws Exception {
        if (this.changed.compareAndSet(true, false)) {
            if (this.transienceManager != null) {
                IndexReader reader = DirectoryReader.open(this.transienceManager.getDirectory());
                reader = new MultiReader(reader, this.reader);
                return reader;
            }
        }
        DirectoryReader reader = DirectoryReader.openIfChanged(this.reader);
        if (reader != null) {
            this.reader.close();
            this.reader = reader;
        }
        return this.reader;
    }

    @Override
    public boolean isChanged() {
        return this.changed.get();
    }

    @Override
    public IndexWriter getWriter() {
        return this.writer;
    }

}
