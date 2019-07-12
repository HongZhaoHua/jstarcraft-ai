package com.jstarcraft.ai.search;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.BulkScorer;
import org.apache.lucene.search.CollectionTerminatedException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Scorable;
import org.apache.lucene.search.Weight;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

public class StarCraftSearcher extends IndexSearcher {

    /** 创建标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Set<String> createdIds;

    /** 更新标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Object2LongMap<String> updatedIds;

    /** 删除标识 */
    // TODO 准备重构为Object2LongMap,支持查询
    private Set<String> deletedIds;

    public StarCraftSearcher(IndexReader reader, Set<String> createdIds, Object2LongMap<String> updatedIds, Set<String> deletedIds) {
        super(reader);
        this.createdIds = createdIds;
        this.updatedIds = updatedIds;
        this.deletedIds = deletedIds;
    }

    public StarCraftSearcher(IndexReaderContext context, Set<String> createdIds, Object2LongMap<String> updatedIds, Set<String> deletedIds) {
        super(context);
        this.createdIds = createdIds;
        this.updatedIds = updatedIds;
        this.deletedIds = deletedIds;
    }

    @Override
    protected void search(List<LeafReaderContext> leaves, Weight weight, Collector collector) throws IOException {
        for (LeafReaderContext context : leaves) {
            final LeafCollector leafCollector;
            try {
                // 此处刻意通过重载LeafCollector.
                LeafCollector component = collector.getLeafCollector(context);
                LeafReader reader = context.reader();
                BinaryDocValues ids = DocValues.getBinary(reader, LuceneSearcher.ID);
                NumericDocValues versions = DocValues.getNumeric(reader, LuceneSearcher.VERSION);

                leafCollector = new LeafCollector() {

                    @Override
                    public void setScorer(Scorable scorer) throws IOException {
                        component.setScorer(scorer);
                    }

                    @Override
                    public void collect(int index) throws IOException {
                        ids.advanceExact(index);
                        String id = ids.binaryValue().utf8ToString();
                        if (StarCraftSearcher.this.deletedIds.contains(id)) {
                            return;
                        }
                        long newVersion = StarCraftSearcher.this.updatedIds.get(id);
                        if (newVersion != 0) {
                            versions.advanceExact(index);
                            long oldVersion = versions.longValue();
                            if (newVersion != oldVersion) {
                                return;
                            }
                        }
                        component.collect(index);
                    }

                };
            } catch (CollectionTerminatedException exception) {
                continue;
            }
            BulkScorer scorer = weight.bulkScorer(context);
            if (scorer != null) {
                try {
                    scorer.score(leafCollector, context.reader().getLiveDocs());
                } catch (CollectionTerminatedException exception) {
                }
            }
        }
    }

}
