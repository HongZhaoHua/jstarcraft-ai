package com.jstarcraft.ai.search;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CollectorManager;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TotalHitCountCollector;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

/**
 * 缓存搜索器
 * 
 * @author Birdy
 *
 * @param <I>
 * @param <T>
 */
public class CacheSearcher implements LuceneSearcher {

    /** 创建标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Set<String> createdIds;

    /** 更新标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Object2LongMap<String> updatedIds;

    /** 删除标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Set<String> deletedIds;

    private TransienceManager transienceManager;

    private PersistenceManager persistenceManager;

    private IndexSearcher searcher;

    public CacheSearcher(IndexWriterConfig config, Path path) throws Exception {
        this.createdIds = new HashSet<>();
        this.updatedIds = new Object2LongOpenHashMap<>();
        this.deletedIds = new HashSet<>();

        this.transienceManager = new TransienceManager(config);
        this.persistenceManager = new PersistenceManager(config, path);

        IndexReader reader = new MultiReader(this.transienceManager.getReader(), this.persistenceManager.getReader());
        this.searcher = new IndexSearcher(reader);
    }

    @Override
    public void createDocuments(Document... documents) throws Exception {
        IndexWriter writer = this.transienceManager.getWriter();
        for (Document document : documents) {
            String id = getId(document);
            if (this.deletedIds.remove(id)) {
                long version = getVersion(document);
                this.updatedIds.put(id, version);
            } else {
                this.createdIds.add(id);
            }
            writer.addDocument(document);
        }
    }

    @Override
    public void updateDocuments(Document... documents) throws Exception {
        IndexWriter writer = this.transienceManager.getWriter();
        for (Document document : documents) {
            String id = getId(document);
            long version = getVersion(document);
            this.updatedIds.put(id, version);
            writer.addDocument(document);
        }
    }

    @Override
    public void deleteDocuments(String... ids) throws Exception {
        IndexWriter writer = this.transienceManager.getWriter();
        for (String id : ids) {
            if (this.createdIds.remove(id)) {
                Term term = new Term(ID, id);
                writer.deleteDocuments(term);
            } else {
                this.deletedIds.add(id);
            }
        }
    }

    @Override
    public List<Document> retrieveDocuments(Query query, Sort sort, int size) throws Exception {
        if (this.transienceManager.isChanged() || this.persistenceManager.isChanged()) {
            IndexReader reader = new MultiReader(this.transienceManager.getReader(), this.persistenceManager.getReader());
            this.searcher = new IndexSearcher(reader);
        }

        List<Document> documents = new ArrayList<>(size);
        TopFieldDocs instances = this.searcher.search(query, size + this.updatedIds.size() + this.deletedIds.size(), sort);
        int index = 0;
        for (ScoreDoc instance : instances.scoreDocs) {
            Document document = this.searcher.doc(instance.doc);
            String id = getId(document);
            if (this.deletedIds.contains(id)) {
                continue;
            }
            long newVersion = this.updatedIds.get(id);
            if (newVersion != 0) {
                long oldVersion = getVersion(document);
                if (newVersion != oldVersion) {
                    continue;
                }
            }
            documents.add(document);
            index++;
            if (index == size) {
                break;
            }
        }
        return documents;
    }

    @Override
    public int countDocuments(Query query) throws Exception {
        if (this.transienceManager.isChanged() || this.persistenceManager.isChanged()) {
            IndexReader reader = new MultiReader(this.transienceManager.getReader(), this.persistenceManager.getReader());
            this.searcher = new IndexSearcher(reader);
        }

        final CollectorManager<CountCollector, Integer> collectorManager = new CollectorManager<CountCollector, Integer>() {

            @Override
            public CountCollector newCollector() throws IOException {
                CountCollector collector = new CountCollector() {

                    @Override
                    public void collect(int index) throws IOException {
                        this.ids.advanceExact(index);
                        String id = this.ids.binaryValue().utf8ToString();
                        if (CacheSearcher.this.deletedIds.contains(id)) {
                            return;
                        }
                        long newVersion = CacheSearcher.this.updatedIds.get(id);
                        if (newVersion != 0) {
                            this.versions.advanceExact(index);
                            long oldVersion = this.versions.longValue();
                            if (newVersion != oldVersion) {
                                return;
                            }
                        }
                        this.count++;
                    }

                };
                return collector;
            }

            @Override
            public Integer reduce(Collection<CountCollector> collectors) throws IOException {
                int count = 0;
                for (CountCollector collector : collectors) {
                    count += collector.getCount();
                }
                return count;
            }

        };
        return this.searcher.search(query, collectorManager);
    }

}
