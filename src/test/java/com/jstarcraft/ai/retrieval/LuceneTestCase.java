package com.jstarcraft.ai.retrieval;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.retrieval.hanlp.HanLPIndexAnalyzer;
import com.jstarcraft.core.utility.StringUtility;

public class LuceneTestCase {

    @Test
    public void test() throws Exception {
        // 索引路径
        Path indexPath = Paths.get("lucene", "indexes");
        FileUtils.forceDelete(indexPath.toFile());
        Directory directory = FSDirectory.open(indexPath);

        Analyzer analyzer = new HanLPIndexAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 文档路径
        Path documentPath = Paths.get("lucene", "documents");
        for (File file : documentPath.toFile().listFiles()) {
            Document document = new Document();

            // 文件名称
            String fileName = file.getName();
            Field fileNameField = new TextField("fileName", fileName, Store.YES);
            document.add(fileNameField);

            // 文件大小
            long fileSize = FileUtils.sizeOf(file);
            Field fileSizeField = new StoredField("fileSize", fileSize);
            document.add(fileSizeField);

            // 文件路径
            String filePath = file.getPath();
            Field filePathField = new StoredField("filePath", filePath);
            document.add(filePathField);

            // 文件内容
            String fileContent = FileUtils.readFileToString(file, StringUtility.CHARSET);
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
            // 文件路径
            Assert.assertEquals("lucene\\documents\\chinese.txt", document.get("filePath"));
        }
        {
            Document document = indexSearcher.doc(topDocs.scoreDocs[1].doc);
            // 文件名称
            Assert.assertEquals("english.txt", document.get("fileName"));
            // 文件内容
            Assert.assertEquals("i love you", document.get("fileContent"));
            // 文件大小
            Assert.assertEquals("10", document.get("fileSize"));
            // 文件路径
            Assert.assertEquals("lucene\\documents\\english.txt", document.get("filePath"));
        }
        indexReader.close();
    }
}
