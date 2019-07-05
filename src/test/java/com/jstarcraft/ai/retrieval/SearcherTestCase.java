package com.jstarcraft.ai.retrieval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.core.utility.StringUtility;

public class SearcherTestCase {

    private IndexSearcher searcher;

    {
        try {
            Directory directory = new ByteBuffersDirectory();
            Analyzer analyzer = new WhitespaceAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            File file = new File(this.getClass().getResource("movie.csv").toURI());
            InputStream stream = new FileInputStream(file);
            String format = "dd-MMM-yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.US);
            try (InputStreamReader reader = new InputStreamReader(stream); BufferedReader buffer = new BufferedReader(reader)) {
                try (CSVParser parser = new CSVParser(buffer, CSVFormat.newFormat('|'))) {
                    Iterator<CSVRecord> iterator = parser.iterator();
                    while (iterator.hasNext()) {
                        CSVRecord datas = iterator.next();
                        Document document = new Document();
                        // 电影标识
                        Field idField = new IntPoint("id", Integer.parseInt(datas.get(0)));
                        document.add(idField);
                        // 电影标题
                        Field titleField = new TextField("title", datas.get(1), Store.YES);
                        document.add(titleField);
                        // 电影日期
                        if (StringUtility.isEmpty(datas.get(2))) {
                            continue;
                        }
                        LocalDate date = LocalDate.parse(datas.get(2), formatter);
                        Field dateField = new SortedDocValuesField("date", new BytesRef(date.toString()));
                        document.add(dateField);
                        // 电影URL
                        datas.get(4);
                        indexWriter.addDocument(document);
                    }
                }
            }
            DirectoryReader directoryReader = DirectoryReader.open(indexWriter);
            searcher = new IndexSearcher(directoryReader);
        } catch (Exception exception) {

        }
    }

    // 测试查询

    @Test
    public void testMatchAllDocsQuery() throws Exception {
        Query query = new MatchAllDocsQuery();
        TopDocs search = searcher.search(query, 1000000);
        Assert.assertEquals(1681, search.totalHits.value);
    }

    @Test
    public void testMatchNoDocsQuery() throws Exception {
        Query query = new MatchNoDocsQuery();
        TopDocs search = searcher.search(query, 1000);
        Assert.assertEquals(0, search.totalHits.value);
    }

    // 词项查询

    @Test
    public void testTermQuery() throws Exception {
        Term term = new Term("title", "Toy");
        TopDocs search = searcher.search(new TermQuery(term), 1000);
        Assert.assertEquals(1, search.totalHits.value);
    }

    @Test
    public void testTermRangeQuery() throws Exception {
        // 范围搜索
        Query query = new TermRangeQuery("title", new BytesRef("Toa"), new BytesRef("Toz"), true, true);
        TopDocs search = searcher.search(query, 1000);
        Assert.assertEquals(22, search.totalHits.value);
    }

    @Test
    public void testPrefixQuery() throws Exception {
        // 前缀搜索
        PrefixQuery query = new PrefixQuery(new Term("title", "Touc"));
        TopDocs search = searcher.search(query, 1000);
        Assert.assertEquals(2, search.totalHits.value);
    }

    @Test
    public void testWildcardQuery() throws Exception {
        // 通配符搜索
        {
            // *代表0个或者多个字母
            Query query = new WildcardQuery(new Term("title", "*ouc*"));
            TopDocs search = searcher.search(query, 1000);
            Assert.assertEquals(2, search.totalHits.value);
        }
        {
            // ?代表0个或者1个字母
            Query query = new WildcardQuery(new Term("title", "?ouc?"));
            TopDocs search = searcher.search(query, 1000);
            Assert.assertEquals(2, search.totalHits.value);
        }
    }

    @Test
    public void testFuzzyQuery() throws Exception {
        // 模糊搜索
        Query query = new FuzzyQuery(new Term("title", "Stori"));
        TopDocs search = searcher.search(query, 1000);
        Assert.assertEquals(32, search.totalHits.value);
    }

    // 短语查询

    @Test
    public void testPhraseQuery() throws Exception {
        // 短语搜索
        // 设置短语之间的跨度为2,也就是说Story和The之间的短语小于等于均可检索
        PhraseQuery build = new PhraseQuery.Builder().setSlop(2).add(new Term("title", "Story")).add(new Term("title", "The")).build();
        TopDocs search = searcher.search(build, 1000);
        Assert.assertEquals(2, search.totalHits.value);

    }

    @Test
    public void testMultiPhraseQuery() throws Exception {
        // 多短语搜索
        Term[] terms = new Term[] { new Term("title", "NeverEnding"), new Term("title", "Xinghua,") };
        Term term = new Term("title", "The");
        // add之间认为是OR操作,即"NeverEnding", "Xinghua,"和"The"之间的slop不大于3
        MultiPhraseQuery multiPhraseQuery = new MultiPhraseQuery.Builder().add(terms).add(term).setSlop(3).build();
        TopDocs search = searcher.search(multiPhraseQuery, 1000);
        Assert.assertEquals(2, search.totalHits.value);
    }

    // 数值查询

    @Test
    public void testPointExactQuery() throws Exception {
        // 精确查询
        Query exactQuery = IntPoint.newExactQuery("id", 1);
        TopDocs search = searcher.search(exactQuery, 1000);
        Assert.assertEquals(1, search.totalHits.value);
    }

    @Test
    public void testPointRangeQuery() throws Exception {
        // 范围查询
        Query rangeQuery = IntPoint.newRangeQuery("id", 501, 1000);
        TopDocs search = searcher.search(rangeQuery, 1000);
        Assert.assertEquals(500, search.totalHits.value);
    }

    @Test
    public void testPointSetQuery() throws Exception {
        // 集合查询
        Query setQuery = IntPoint.newSetQuery("id", 1, 10, 100, 1000);
        TopDocs search = searcher.search(setQuery, 1000);
        Assert.assertEquals(4, search.totalHits.value);
    }

    // 组合查询

    @Test
    public void testBooleanQuery() throws Exception {
        // 与或搜索
        Query leftQuery = new TermQuery(new Term("title", "Toy"));
        Query rightQuery = new TermQuery(new Term("title", "Story"));
        {
            // 与查询
            BooleanQuery build = new BooleanQuery.Builder().add(leftQuery, BooleanClause.Occur.MUST).add(rightQuery, BooleanClause.Occur.MUST).build();
            TopDocs search = searcher.search(build, 1000);
            Assert.assertEquals(1, search.totalHits.value);
        }
        {
            // 或查询
            BooleanQuery build = new BooleanQuery.Builder().add(leftQuery, BooleanClause.Occur.SHOULD).add(rightQuery, BooleanClause.Occur.SHOULD).build();
            TopDocs search = searcher.search(build, 1000);
            Assert.assertEquals(6, search.totalHits.value);
        }
    }

    // 功能查询

    // 查询解析器

    @Test
    public void testQueryParser() throws Exception {
    }

}
