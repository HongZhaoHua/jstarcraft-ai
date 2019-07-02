package com.jstarcraft.ai.retrieval;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.retrieval.hanlp.HanlpIndexAnalyzer;
import com.jstarcraft.core.utility.StringUtility;

public class LuceneTestCase {

    @Test
    public void test() throws Exception {
        Directory directory = new ByteBuffersDirectory();
        Analyzer analyzer = new HanlpIndexAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        {
            String fileContent = "我爱你";
            Document document = new Document();
            // 文件名称
            Field fileNameField = new TextField("fileName", "chinese.txt", Store.YES);
            document.add(fileNameField);
            // 文件大小
            Field fileSizeField = new StoredField("fileSize", fileContent.getBytes(StringUtility.CHARSET).length);
            document.add(fileSizeField);
            // 文件内容
            Field fileContentField = new TextField("fileContent", fileContent, Store.YES);
            document.add(fileContentField);
            indexWriter.addDocument(document);
        }
        {
            String fileContent = "i love you";
            Document document = new Document();
            // 文件名称
            Field fileNameField = new TextField("fileName", "english.txt", Store.YES);
            document.add(fileNameField);
            // 文件大小
            Field fileSizeField = new StoredField("fileSize", fileContent.getBytes(StringUtility.CHARSET).length);
            document.add(fileSizeField);
            // 文件内容
            Field fileContentField = new TextField("fileContent", fileContent, Store.YES);
            document.add(fileContentField);
            indexWriter.addDocument(document);
        }
        indexWriter.close();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new MatchAllDocsQuery();
        TopDocs topDocs = indexSearcher.search(query, 10);
        Assert.assertEquals(2L, topDocs.totalHits.value);
        {
            Document document = indexSearcher.doc(topDocs.scoreDocs[0].doc);
            // 文件名称
            Assert.assertEquals("chinese.txt", document.get("fileName"));
            // 文件内容
            Assert.assertEquals("我爱你", document.get("fileContent"));
            // 文件大小
            Assert.assertEquals("9", document.get("fileSize"));
        }
        {
            Document document = indexSearcher.doc(topDocs.scoreDocs[1].doc);
            // 文件名称
            Assert.assertEquals("english.txt", document.get("fileName"));
            // 文件内容
            Assert.assertEquals("i love you", document.get("fileContent"));
            // 文件大小
            Assert.assertEquals("10", document.get("fileSize"));
        }
        indexReader.close();
    }

}
