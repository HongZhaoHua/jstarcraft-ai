package com.jstarcraft.ai.parquet;

import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RawLocalFileSystem;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParquetTestCase {

    private Logger logger = LoggerFactory.getLogger(ParquetTestCase.class);

    private MessageType schema = MessageTypeParser.parseMessageType("message Parquet { required binary title (UTF8); required binary when (UTF8); repeated group where { required float longitude; required float latitude; } }");

    @Test
    public void testReadWrite() throws Exception {
        parquetWriter("parquet");
        parquetReader("parquet");
    }

    private void parquetReader(String inPath) throws Exception {
        GroupReadSupport readSupport = new GroupReadSupport();
        Path path = new Path(inPath);

        ParquetReader<Group> reader = new ParquetReader<Group>(path, readSupport);
        Group group = null;
        while ((group = reader.read()) != null) {
            Group where = group.getGroup("where", 0);
            Assert.assertEquals("title", group.getString("title", 0));
            Assert.assertEquals("2020-01-01 00:00:00", group.getString("when", 0));
            Assert.assertEquals(0F, where.getFloat("longitude", 0), 0F);
            Assert.assertEquals(0F, where.getFloat("latitude", 0), 0F);
        }
    }

    private void parquetWriter(String outPath) throws Exception {
        GroupWriteSupport writeSupport = new GroupWriteSupport();
        Path path = new Path(outPath);

        Configuration configuration = new Configuration();
        FileSystem fileSystem = RawLocalFileSystem.get(configuration);
        fileSystem.delete(path, true);

        GroupFactory factory = new SimpleGroupFactory(schema);
        Group group = factory.newGroup().append("title", "title").append("when", "2020-01-01 00:00:00");
        Group where = group.addGroup("where");
        where.append("longitude", 0F);
        where.append("latitude", 0F);

        writeSupport.setSchema(schema, configuration);
        ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);
        writer.write(group);
        writer.close();
    }

}
