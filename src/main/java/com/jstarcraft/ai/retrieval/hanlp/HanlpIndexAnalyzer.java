package com.jstarcraft.ai.retrieval.hanlp;

import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.util.Set;

/**
 * HanLP索引分析器(仅用于索引)
 * 
 * @author Birdy
 *
 */
public class HanlpIndexAnalyzer extends Analyzer {

    private boolean pstemming;
    private Set<String> filter;

    /**
     * @param filter    停用词
     * @param pstemming 是否分析词干
     */
    public HanlpIndexAnalyzer(Set<String> filter, boolean pstemming) {
        this.filter = filter;
        this.pstemming = pstemming;
    }

    /**
     * @param pstemming 是否分析词干.进行单复数,时态的转换
     */
    public HanlpIndexAnalyzer(boolean pstemming) {
        this.pstemming = pstemming;
    }

    public HanlpIndexAnalyzer() {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanlpTokenizer(HanLP.newSegment().enableIndexMode(true), filter, pstemming);
        return new TokenStreamComponents(tokenizer);
    }

}
