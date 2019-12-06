package wekalearning.serialization;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 分类器及头信息序列化和反序列化示例
 */
public class HeaderSerialization {

    public static void main(String[] args) throws Exception {
        // 加载训练集
        Instances train = DataSource.read("data/segment-challenge.arff");
        train.setClassIndex(train.numAttributes() - 1);
        // 训练J48
        Classifier cls = new J48();
        cls.buildClassifier(train);
        // 序列化分类器及头信息
        Instances header = new Instances(train, 0);
        SerializationHelper.writeAll("data/j48.model", new Object[] { cls, header });
        System.out.println("序列化分类器及头信息成功！\n");

        // 加载测试集
        Instances test = DataSource.read("data/segment-test.arff");
        test.setClassIndex(test.numAttributes() - 1);
        // 反序列化模型
        Object o[] = SerializationHelper.readAll("data/j48.model");
        Classifier cls2 = (Classifier) o[0];
        Instances data = (Instances) o[1];
        // 模型与测试集是否兼容？
        if (!data.equalHeaders(test))
            throw new Exception("数据不兼容！");
        System.out.println("反序列化分类器及头信息成功！");
        System.out.println("反序列化模型如下：");
        System.out.println(cls2);
    }

}
