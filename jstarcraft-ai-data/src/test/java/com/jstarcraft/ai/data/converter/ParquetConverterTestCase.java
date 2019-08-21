package com.jstarcraft.ai.data.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RawLocalFileSystem;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.junit.Assert;
import org.junit.Test;

import com.jstarcraft.ai.data.DataModule;
import com.jstarcraft.ai.data.DataSpace;

public class ParquetConverterTestCase {

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
    public void testConvert() throws Exception {
        Map<String, Class<?>> qualityDifinitions = new HashMap<>();
        Map<String, Class<?>> quantityDifinitions = new HashMap<>();
        qualityDifinitions.put("user", int.class);
        qualityDifinitions.put("item", int.class);
        quantityDifinitions.put("score", float.class);
        DataSpace space = new DataSpace(qualityDifinitions, quantityDifinitions);

        TreeMap<Integer, String> configuration = new TreeMap<>();
        configuration.put(2, "user");
        configuration.put(4, "item");
        configuration.put(5, "score");

        Schema schema = SchemaBuilder.record("parquet").fields().optionalInt("leftUser").optionalInt("rightUser").optionalInt("leftItem").optionalInt("rightItem").optionalFloat("score").endRecord();
        ParquetConverter converter = new ParquetConverter(space.getQualityAttributes(), space.getQuantityAttributes());
        {
            Path path = new Path("dense.parquet");
            fileSystem.delete(path, true);
            HadoopOutputFile output = HadoopOutputFile.fromPath(path, this.configuration);
            try (ParquetWriter writer = AvroParquetWriter.builder(output).withSchema(schema).build()) {
                for (int index = 0; index < 5; index++) {
                    GenericRecord parquet = new GenericData.Record(schema);
                    parquet.put(0, 1);
                    parquet.put(1, 2);
                    parquet.put(2, 3);
                    parquet.put(3, 4);
                    parquet.put(4, index / 10F);
                    writer.write(parquet);
                }
            }

            DataModule dense = space.makeDenseModule("dense", configuration, 1000);
            HadoopInputFile input = HadoopInputFile.fromPath(path, this.configuration);
            try (ParquetReader reader = AvroParquetReader.builder(input).build()) {
                int count = converter.convert(dense, reader);
                Assert.assertEquals(5, count);
            }
        }
        {
            Path path = new Path("sparse.parquet");
            fileSystem.delete(path, true);
            HadoopOutputFile output = HadoopOutputFile.fromPath(path, this.configuration);
            try (ParquetWriter writer = AvroParquetWriter.builder(output).withSchema(schema).build()) {
                for (int index = 0; index < 5; index++) {
                    GenericRecord parquet = new GenericData.Record(schema);
                    parquet.put(0, 1);
                    if (index > 0) {
                        parquet.put(1, 2);
                    }
                    if (index > 1) {
                        parquet.put(2, 3);
                    }
                    if (index > 2) {
                        parquet.put(3, 4);
                    }
                    if (index > 3) {
                        parquet.put(4, index / 10F);
                    }
                    writer.write(parquet);
                }
            }

            DataModule sparse = space.makeSparseModule("sparse", configuration, 1000);
            HadoopInputFile input = HadoopInputFile.fromPath(path, this.configuration);
            try (ParquetReader reader = AvroParquetReader.builder(input).build()) {
                int count = converter.convert(sparse, reader);
                Assert.assertEquals(5, count);
            }
        }
    }

}
