package api.classifiers;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 本示例用训练集构建J48分类器，预测测试集的类别，并输出实际的和预测的类别标签以及分布
 */
public class OutputClassDistribution {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances train = DataSource.read("data/segment-challenge.arff");
        Instances test = DataSource.read("data/segment-test.arff");
        // 设置类别索引
        train.setClassIndex(train.numAttributes() - 1);
        test.setClassIndex(test.numAttributes() - 1);
        // 检查训练集和测试集是否兼容
        if (!train.equalHeaders(test))
            throw new Exception("训练集和测试集不兼容：" + train.equalHeadersMsg(test));

        // 训练分类器
        J48 classifier = new J48();
        classifier.buildClassifier(train);

        // 输出预测
        System.out.println("编号\t-\t实际\t-\t预测\t-\t错误\t-\t分布");
        for (int i = 0; i < test.numInstances(); i++) {
            // 得到预测值
            double pred = classifier.classifyInstance(test.instance(i));
            // 得到分布
            double[] dist = classifier.distributionForInstance(test.instance(i));
            System.out.print((i + 1));
            System.out.print("\t-\t");
            System.out.print(test.instance(i).toString(test.classIndex()));
            System.out.print("\t-\t");
            System.out.print(test.classAttribute().value((int) pred));
            System.out.print("\t-\t");
            // 判断是否预测错误
            if (pred != test.instance(i).classValue())
                System.out.print("是");
            else
                System.out.print("否");
            System.out.print("\t-\t");
            System.out.print(Utils.arrayToString(dist));
            System.out.println();
        }
    }
}
