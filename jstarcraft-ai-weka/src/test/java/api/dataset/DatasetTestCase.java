package api.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

public class DatasetTestCase {

    /**
     * 获取简单定义
     * 
     * @return
     */
    private ArrayList<Attribute> getSimpleDefinition() {
        int size = 5;
        ArrayList<Attribute> attributes = new ArrayList<Attribute>(2);
        // - 数值型
        attributes.add(new Attribute("numeric"));
        // - 标称型
        ArrayList<String> nominals = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            nominals.add("nominal." + index);
        }
        attributes.add(new Attribute("nominal", nominals));
        return attributes;
    }

    /**
     * 获取复杂定义
     * 
     * @return
     */
    private ArrayList<Attribute> getComplexDefinition() {
        int size = 5;
        // 设置属性
        ArrayList<Attribute> attributes = new ArrayList<>(size);
        // - 数值型
        attributes.add(new Attribute("numeric"));
        // - 标称型
        ArrayList<String> nominals = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            nominals.add("nominal" + index);
        }
        attributes.add(new Attribute("nominal.", nominals));
        // - 字符串型
        attributes.add(new Attribute("string", (ArrayList<String>) null));
        // - 日期型
        attributes.add(new Attribute("date", "yyyy-MM-dd"));
        // - 关系型
        Instances relational = new Instances("relational", getSimpleDefinition(), 0);
        attributes.add(new Attribute("relational", relational, 0));
        return attributes;
    }

    private double[] getValues(ArrayList<Attribute> attributes) throws Exception {
        double[] simple = new double[] { 0D, 0D };
        double[] complex = new double[attributes.size()];
        // - 数值型
        complex[0] = Math.PI;
        // - 标称型
        complex[1] = 0D;
        // - 字符串型
        complex[2] = attributes.get(2).addStringValue("string");
        // - 日期型
        complex[3] = attributes.get(3).parseDate("2010-01-01");
        // - 关系型
        Instances relational = new Instances(attributes.get(4).relation(), 0);
        // -- 稠密实例
        relational.add(new DenseInstance(1D, simple));
        // -- 稀疏实例
        relational.add(new SparseInstance(1D, simple));
        complex[4] = attributes.get(4).addRelation(relational);
        return complex;
    }

    private DenseInstance getDenseInstance(List<Attribute> attributes, double[] values) {
        return new DenseInstance(1D, Arrays.copyOf(values, values.length));
    }

    private SparseInstance getSparseInstance(List<Attribute> attributes, double[] values) {
        return new SparseInstance(1D, Arrays.copyOf(values, values.length));
    }

    @Test
    public void testDataset() throws Exception {
        ArrayList<Attribute> attributes = getComplexDefinition();
        Instances instances = new Instances("data", attributes, 0);
        double[] values = getValues(attributes);
        Instance dense = getDenseInstance(attributes, values);
        Instance sparse = getSparseInstance(attributes, values);
        instances.add(dense);
        instances.add(sparse);
        System.out.println(instances);

        String[] files = new String[] { "test.arff", "test.xrff" };
        for (String flie : files) {
            DataSink.write(flie, instances);
            Instances data = DataSource.read(flie);
            System.out.println(flie);
            System.out.println(data);
            Assert.assertEquals(2, data.size());
        }
    }

}
