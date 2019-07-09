package com.jstarcraft.ai.search;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.jstarcraft.core.common.identification.IdentityObject;

public class LuceneManager<I extends Comparable<I>, T extends IdentityObject<I>> {

    private Set<I> createdIds;

    private Set<I> updatedIds;

    private Set<I> deletedIds;

    private Analyzer analyzer;

    private IndexWriterConfig config;

    private Directory fileDirectory;

    private Directory memoryDirectory;

    private Directory copyDirectory;

    private IndexWriter fileWriter;

    private IndexWriter memoryWriter;

    private DirectoryReader fileReader;

    private DirectoryReader memoryReader;

    private IndexSearcher fileSearcher;

    private IndexSearcher memorySearcher;

    public LuceneManager(Analyzer analyzer, Path path) throws Exception {
        this.createdIds = new HashSet<>();
        this.updatedIds = new HashSet<>();
        this.deletedIds = new HashSet<>();

        this.analyzer = analyzer;
        this.fileDirectory = FSDirectory.open(path);
        this.memoryDirectory = new ByteBuffersDirectory();

        this.config = new IndexWriterConfig(this.analyzer);
        this.fileWriter = new IndexWriter(this.fileDirectory, this.config);
        this.memoryWriter = new IndexWriter(this.memoryDirectory, this.config);

        this.fileReader = DirectoryReader.open(this.fileWriter);
        this.memoryReader = DirectoryReader.open(this.memoryWriter);

        this.fileSearcher = new IndexSearcher(this.fileReader);
        this.memorySearcher = new IndexSearcher(this.memoryReader);
    }

    public void createInstance(T instance) throws Exception {
        I id = instance.getId();
        if (this.deletedIds.remove(id)) {
            this.updatedIds.add(id);
        } else {
            this.createdIds.add(id);
        }
        this.memoryWriter.addDocument(null);
        DirectoryReader memoryReader = DirectoryReader.openIfChanged(this.memoryReader);
        if (memoryReader != null) {
            this.memorySearcher = new IndexSearcher(memoryReader);
            this.memoryReader.close();
            this.memoryReader = memoryReader;
        }
    }

    public void updateInstance(T instance) throws Exception {
        I id = instance.getId();
        this.updatedIds.add(id);
        Term term = new Term("id", id.toString());
        this.memoryWriter.updateDocument(term, null);
        DirectoryReader memoryReader = DirectoryReader.openIfChanged(this.memoryReader);
        if (memoryReader != null) {
            this.memorySearcher = new IndexSearcher(memoryReader);
            this.memoryReader.close();
            this.memoryReader = memoryReader;
        }
    }

    public void deleteInstance(T instance) throws Exception {
        I id = instance.getId();
        if (this.createdIds.remove(id)) {
            Term term = new Term("id", id.toString());
            this.memoryWriter.deleteDocuments(term);
            DirectoryReader memoryReader = DirectoryReader.openIfChanged(this.memoryReader);
            if (memoryReader != null) {
                this.memorySearcher = new IndexSearcher(memoryReader);
                this.memoryReader.close();
                this.memoryReader = memoryReader;
            }
        } else {
            this.deletedIds.add(id);
        }
    }

    public synchronized void mergeInstances() throws Exception {
        Set<I> createdIds = new HashSet<>(this.createdIds);
        Set<I> updatedIds = new HashSet<>(this.updatedIds);
        Set<I> deletedIds = new HashSet<>(this.deletedIds);

        this.memoryReader.close();
        this.memoryWriter.close();

        this.copyDirectory = this.memoryDirectory;

        this.memoryDirectory = new ByteBuffersDirectory();
        this.memoryWriter = new IndexWriter(this.memoryDirectory, this.config);
        this.memoryWriter.addIndexes(this.copyDirectory);
        this.memoryReader = DirectoryReader.open(this.memoryWriter);
        this.memorySearcher = new IndexSearcher(this.memoryReader);

        for (I id : deletedIds) {
            Term term = new Term("id", id.toString());
            this.fileWriter.deleteDocuments(term);
        }
        for (I id : updatedIds) {
            Term term = new Term("id", id.toString());
            this.fileWriter.deleteDocuments(term);
        }

        this.fileWriter.addIndexes(this.copyDirectory);
    }

    public void getIdentities(Query query, int size) {

    }

    public void getInstances(Query query, int size) {

    }

}
