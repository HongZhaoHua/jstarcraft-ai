package wekalearning.filters;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * 批量过滤。训练集用于初始化过滤器，并使用过滤器来过滤测试集
 */
public class BatchFiltering {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances train = DataSource.read("data/segment-challenge.arff");
        Instances test = DataSource.read("data/segment-test.arff");

        // 过滤数据
        // 使用标准化过滤器
        Standardize filter = new Standardize();
        // 使用训练集一次初始化过滤器
        filter.setInputFormat(train);
        // 基于训练集配置过滤器，并返回过滤后的实例
        Instances newTrain = Filter.useFilter(train, filter);
        // 过滤并创建新测试集
        Instances newTest = Filter.useFilter(test, filter);

        // 输出数据集
        System.out.println("新训练集：");
        System.out.println(newTrain);
        System.out.println("新测试集：");
        System.out.println(newTest);
    }
}
