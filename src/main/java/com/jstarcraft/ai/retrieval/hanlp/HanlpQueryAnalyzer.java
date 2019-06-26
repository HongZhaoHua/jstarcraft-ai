package com.jstarcraft.ai.retrieval.hanlp;

import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.util.Set;

/**
 * HanLP查询分析器(仅用于查询)
 * 
 * @author Birdy
 *
 */
public class HanlpQueryAnalyzer extends Analyzer {

    private boolean enablePorterStemming;
    private Set<String> filter;

    /**
     * @param filter               停用词
     * @param enablePorterStemming 是否分析词干(仅限英文)
     */
    public HanlpQueryAnalyzer(Set<String> filter, boolean enablePorterStemming) {
        this.filter = filter;
        this.enablePorterStemming = enablePorterStemming;
    }

    /**
     * @param enablePorterStemming 是否分析词干.进行单复数,时态的转换
     */
    public HanlpQueryAnalyzer(boolean enablePorterStemming) {
        this.enablePorterStemming = enablePorterStemming;
    }

    public HanlpQueryAnalyzer() {
        super();
    }

    /**
     * 重载Analyzer接口,构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanlpTokenizer(HanLP.newSegment().enableOffset(true), filter, enablePorterStemming);
        return new TokenStreamComponents(tokenizer);
    }

}
