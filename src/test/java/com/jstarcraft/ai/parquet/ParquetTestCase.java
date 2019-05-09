package com.jstarcraft.ai.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RawLocalFileSystem;
import org.apache.parquet.column.ParquetProperties;
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

    private Path path = new Path("data.parquet");
    private Configuration configuration = new Configuration();
    private FileSystem fileSystem;
    {
        try {
            fileSystem = RawLocalFileSystem.get(configuration);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testReadWriteSimple() throws Exception {
        fileSystem.delete(path, true);

        MessageType schema = MessageTypeParser.parseMessageType("message KeyValue { required binary left (UTF8); required binary right (UTF8); }");

        {
            GroupFactory factory = new SimpleGroupFactory(schema);
            Group group = factory.newGroup().append("left", "Left").append("right", "Right");

            GroupWriteSupport writeSupport = new GroupWriteSupport();
            writeSupport.setSchema(schema, configuration);
            ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);
            writer.write(group);
            writer.close();
        }

        {
            GroupReadSupport readSupport = new GroupReadSupport();
            ParquetReader<Group> reader = new ParquetReader<Group>(path, readSupport);

            Group group = reader.read();
            Assert.assertEquals("Left", group.getString("left", 0));
            Assert.assertEquals("Right", group.getString("right", 0));
            reader.close();
        }
    }

    @Test
    public void testReadWriteComplex() throws Exception {
        fileSystem.delete(path, true);

        MessageType schema = MessageTypeParser.parseMessageType("message Record { required binary title (UTF8); required binary when (UTF8); repeated group where { required float longitude; required float latitude; } }");

        {
            GroupFactory factory = new SimpleGroupFactory(schema);
            Group group = factory.newGroup().append("title", "title").append("when", "2020-01-01 00:00:00");
            Group where = group.addGroup("where");
            where.append("longitude", 0F);
            where.append("latitude", 0F);

            GroupWriteSupport writeSupport = new GroupWriteSupport();
            writeSupport.setSchema(schema, configuration);
            ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);
            writer.write(group);
            writer.close();
        }

        {
            GroupReadSupport readSupport = new GroupReadSupport();
            ParquetReader<Group> reader = new ParquetReader<Group>(path, readSupport);

            Group group = reader.read();
            Group where = group.getGroup("where", 0);
            Assert.assertEquals("title", group.getString("title", 0));
            Assert.assertEquals("2020-01-01 00:00:00", group.getString("when", 0));
            Assert.assertEquals(0F, where.getFloat("longitude", 0), 0F);
            Assert.assertEquals(0F, where.getFloat("latitude", 0), 0F);
            reader.close();
        }
    }

}
