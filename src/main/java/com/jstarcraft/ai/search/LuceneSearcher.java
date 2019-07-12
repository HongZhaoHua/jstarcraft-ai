package com.jstarcraft.ai.search;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.BulkScorer;
import org.apache.lucene.search.CollectionTerminatedException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Weight;

public class LuceneSearcher extends IndexSearcher {

    private TransienceManager[] transienceManagers;

    public LuceneSearcher(IndexReader reader, TransienceManager... transienceManagers) {
        super(reader);
        this.transienceManagers = transienceManagers;
    }

    public LuceneSearcher(IndexReaderContext context, TransienceManager... transienceManagers) {
        super(context);
        this.transienceManagers = transienceManagers;
    }

    @Override
    protected void search(List<LeafReaderContext> leaves, Weight weight, Collector collector) throws IOException {
        for (LeafReaderContext context : leaves) {
            LeafCollector instance;
            try {
                // 此处刻意通过TransienceManager重载LeafCollector.
                instance = collector.getLeafCollector(context);
                for (TransienceManager transienceManager : transienceManagers) {
                    instance = transienceManager.getCollector(context, instance);
                }
            } catch (CollectionTerminatedException exception) {
                continue;
            }
            BulkScorer scorer = weight.bulkScorer(context);
            if (scorer != null) {
                try {
                    scorer.score(instance, context.reader().getLiveDocs());
                } catch (CollectionTerminatedException exception) {
                }
            }
        }
    }

}
