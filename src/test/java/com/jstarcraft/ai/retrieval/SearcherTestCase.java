package com.jstarcraft.ai.retrieval;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
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

public class SearcherTestCase {

    private IndexSearcher searcher;

    {
        try {
            Directory directory = new ByteBuffersDirectory();
            String[] ids = { "1", "2" };
            String[] countries = { "Netherlands", "Italy" };
            String[] cities = { "Amsterdam", "Venice" };
            String[] contents = { "Amsterdam has lots of bridges", "Venice has lots of canals, not bridges" };

            // 使用WhitespaceAnalyzer分析器不会忽略大小写
            Analyzer analyzer = new WhitespaceAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            for (int index = 0; index < 2; index++) {
                Document document = new Document();
                Field idField = new StringField("id", ids[index], Field.Store.YES);
                Field countryField = new StringField("country", countries[index], Field.Store.YES);
                Field cityField = new StringField("city", cities[index], Field.Store.YES);
                Field contentField = new TextField("content", contents[index], Field.Store.NO);
                document.add(idField);
                document.add(countryField);
                document.add(cityField);
                document.add(contentField);
                indexWriter.addDocument(document);
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
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(2, search.totalHits.value);
    }

    @Test
    public void testMatchNoDocsQuery() throws Exception {
        Query query = new MatchNoDocsQuery();
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(0, search.totalHits.value);
    }

    // 词项查询

    @Test
    public void testTermQuery() throws Exception {
        Term term = new Term("id", "2");
        TopDocs search = searcher.search(new TermQuery(term), 10);
        Assert.assertEquals(1, search.totalHits.value);
    }

    @Test
    public void testTermRangeQuery() throws Exception {
        // 范围搜索
        Query query = new TermRangeQuery("city", new BytesRef("A"), new BytesRef("Z"), true, true);
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(2, search.totalHits.value);
    }

    @Test
    public void testPrefixQuery() throws Exception {
        // 前缀搜索
        PrefixQuery query = new PrefixQuery(new Term("country", "It"));
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(1, search.totalHits.value);
    }

    @Test
    public void testWildcardQuery() throws Exception {
        // 通配符搜索
        // *代表0个或者多个字母
        Query query = new WildcardQuery(new Term("content", "*dam"));
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(1, search.totalHits.value);
        // ?代表0个或者1个字母
        query = new WildcardQuery(new Term("content", "?ridges"));
        search = searcher.search(query, 10);
        Assert.assertEquals(2, search.totalHits.value);
        query = new WildcardQuery(new Term("content", "b*s"));
        search = searcher.search(query, 10);
        Assert.assertEquals(2, search.totalHits.value);
    }

    @Test
    public void testFuzzyQuery() throws Exception, ParseException {
        // 模糊搜索
        Query query = new FuzzyQuery(new Term("city", "Veni"));
        TopDocs search = searcher.search(query, 10);
        Assert.assertEquals(1, search.totalHits.value);
    }

    // 短语查询

    @Test
    public void testPhraseQuery() throws Exception {
        // 短语搜索
        // 设置短语之间的跨度为2,也就是说has和bridges之间的短语小于等于均可检索
        PhraseQuery build = new PhraseQuery.Builder().setSlop(2).add(new Term("content", "has")).add(new Term("content", "bridges")).build();
        TopDocs search = searcher.search(build, 10);
        Assert.assertEquals(1, search.totalHits.value);
        build = new PhraseQuery.Builder().setSlop(1).add(new Term("content", "Venice")).add(new Term("content", "lots")).add(new Term("content", "canals")).build();
        search = searcher.search(build, 10);
        Assert.assertNotEquals(1, search.totalHits.value);
    }

    @Test
    public void testMultiPhraseQuery() throws Exception {
        // 多短语搜索
        Term[] terms = new Term[] { new Term("content", "has"), new Term("content", "lots") };
        Term term2 = new Term("content", "bridges");
        // 多个add之间认为是OR操作，即(has lots)和bridges之间的slop不大于3，不计算标点
        MultiPhraseQuery multiPhraseQuery = new MultiPhraseQuery.Builder().add(terms).add(term2).setSlop(3).build();
        TopDocs search = searcher.search(multiPhraseQuery, 10);
        Assert.assertEquals(2, search.totalHits.value);
    }

    // 数值查询

    // 组合查询

    @Test
    public void testBooleanQuery() throws Exception {
        // 与或搜索
        Query termQuery = new TermQuery(new Term("country", "Beijing"));
        Query termQuery1 = new TermQuery(new Term("city", "Venice"));
        // 测试OR查询，或者出现Beijing或者出现Venice
        BooleanQuery build = new BooleanQuery.Builder().add(termQuery, BooleanClause.Occur.SHOULD).add(termQuery1, BooleanClause.Occur.SHOULD).build();
        TopDocs search = searcher.search(build, 10);
        Assert.assertEquals(1, search.totalHits.value);
        // 使用BooleanQuery实现 国家是(Italy OR Netherlands) AND contents中包含(Amsterdam)操作
        BooleanQuery build1 = new BooleanQuery.Builder().add(new TermQuery(new Term("country", "Italy")), BooleanClause.Occur.SHOULD).add(new TermQuery(new Term("country", "Netherlands")), BooleanClause.Occur.SHOULD).build();
        BooleanQuery build2 = new BooleanQuery.Builder().add(build1, BooleanClause.Occur.MUST).add(new TermQuery(new Term("content", "Amsterdam")), BooleanClause.Occur.MUST).build();
        search = searcher.search(build2, 10);
        Assert.assertEquals(1, search.totalHits.value);
    }

    @Test
    public void testBooleanQueryImitateMultiPhraseQuery() throws Exception {
        // 使用BooleanQuery+PhraseQuery模拟MultiPhraseQuery功能
        PhraseQuery first = new PhraseQuery.Builder().setSlop(3).add(new Term("content", "Amsterdam")).add(new Term("content", "bridges")).build();
        PhraseQuery second = new PhraseQuery.Builder().setSlop(1).add(new Term("content", "Venice")).add(new Term("content", "lots")).build();
        BooleanQuery booleanQuery = new BooleanQuery.Builder().add(first, BooleanClause.Occur.SHOULD).add(second, BooleanClause.Occur.SHOULD).build();
        TopDocs search = searcher.search(booleanQuery, 10);
        Assert.assertEquals(2, search.totalHits.value);
    }

    // 功能查询

    // 查询解析器

    @Test
    public void testQueryParser() throws Exception {
        // 使用WhitespaceAnalyzer分析器不会忽略大小写，也就是说大小写敏感
        QueryParser queryParser = new QueryParser("content", new WhitespaceAnalyzer());
        Query query = queryParser.parse("+lots +has");
        TopDocs search = searcher.search(query, 1);
        Assert.assertEquals(2, search.totalHits.value);
        query = queryParser.parse("lots OR bridges");
        search = searcher.search(query, 10);
        Assert.assertEquals(2, search.totalHits.value);

        // 有点需要注意，在QueryParser解析通配符表达式的时候，一定要用引号包起来，然后作为字符串传递给parse函数
        query = new QueryParser("field", new StandardAnalyzer()).parse("\"This is some phrase*\"");
        Assert.assertEquals("analyzed", "\"this is some phrase\"", query.toString("field"));

        // 语法参考：http://lucene.apache.org/core/6_0_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description
        // 使用QueryParser解析"~"，~代表编辑距离，~后面参数的取值在0-2之间，默认值是2，不要使用浮点数
        QueryParser parser = new QueryParser("city", new WhitespaceAnalyzer());
        // 例如，roam~，该查询会匹配foam和roams，如果~后不跟参数，则默认值是2
        // QueryParser在解析的时候不区分大小写（会全部转成小写字母），所以虽少了一个字母，但是首字母被解析为小写的v，依然不匹配，所以编辑距离是2
        query = parser.parse("Venic~2");
        search = searcher.search(query, 10);
        Assert.assertEquals(1, search.totalHits.value);
    }

}
