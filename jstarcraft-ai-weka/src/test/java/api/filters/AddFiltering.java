package api.filters;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

import java.util.Random;

/**
 * 添加一个数值属性和一个标称属性到数据集中，并用随机值填充后输出
 */
public class AddFiltering {

    public static void main(String[] args) throws Exception {
        // 加载数据集
        Instances data = DataSource.read("data/weather.numeric.arff");
        Instances result = null;

        Add filter;
        result = new Instances(data);

        // 新增数值属性
        filter = new Add();
        filter.setAttributeIndex("last");
        filter.setAttributeName("NumericAttribute");
        filter.setInputFormat(result);
        result = Filter.useFilter(result, filter);
        // 新增标称属性
        filter = new Add();
        filter.setAttributeIndex("last");
        filter.setNominalLabels("A,B,C"); // 设置标签
        filter.setAttributeName("NominalAttribute");
        filter.setInputFormat(result);
        result = Filter.useFilter(result, filter);

        // 用随机值填充新增的两个属性
        Random rand = new Random(1234);
        for (int i = 0; i < result.numInstances(); i++) {
            // 填充数值属性
            result.instance(i).setValue(result.numAttributes() - 2, rand.nextDouble());
            // 填充标称属性
            result.instance(i).setValue(result.numAttributes() - 1, rand.nextInt(3)); // 标签索引：
                                                                                      // A:0、B:1、C:2
        }

        // 输出数据
        System.out.println("过滤后的数据集：");
        System.out.println(result);
    }
}
