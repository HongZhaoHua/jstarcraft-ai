package com.jstarcraft.ai.search;

import java.io.IOException;

import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;

public abstract class CountCollector extends SimpleCollector {

    protected BinaryDocValues ids;

    protected NumericDocValues versions;

    protected int count;

    @Override
    protected void doSetNextReader(LeafReaderContext context) throws IOException {
        LeafReader reader = context.reader();
        this.ids = DocValues.getBinary(reader, LuceneSearcher.ID);
        this.versions = DocValues.getNumeric(reader, LuceneSearcher.VERSION);
    }

    public int getCount() {
        return count;
    }

    @Override
    public ScoreMode scoreMode() {
        return ScoreMode.COMPLETE_NO_SCORES;
    }

}
