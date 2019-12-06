package wekalearning.classifiers;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;

import java.util.Random;

/**
 * 执行单次交叉验证，并将预测结果保存为文件
 */
public class CVPrediction {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = DataSource.read("data/ionosphere.arff");
        // 设置类别索引
        data.setClassIndex(data.numAttributes() - 1);

        // 分类器
        String[] tmpOptions = new String[2];
        String classname = "weka.classifiers.trees.J48";
        tmpOptions[0] = "-C"; // 默认参数
        tmpOptions[1] = "0.25";
        Classifier classifier = (Classifier) Utils.forName(Classifier.class, classname, tmpOptions);

        // 其他选项
        int seed = 1234; // 随机种子
        int folds = 10; // 折数

        // 随机化数据
        Random rand = new Random(seed);
        Instances newData = new Instances(data);
        newData.randomize(rand);
        // 如果类别为标称型，则根据其类别值进行分层
        if (newData.classAttribute().isNominal())
            newData.stratify(folds);

        // 执行交叉验证，并添加预测
        Instances predictedData = null; // 预测数据
        Evaluation eval = new Evaluation(newData);
        for (int i = 0; i < folds; i++) {
            // 训练集
            Instances train = newData.trainCV(folds, i);
            // 测试集
            Instances test = newData.testCV(folds, i);

            // 构建并评估分类器
            Classifier clsCopy = AbstractClassifier.makeCopy(classifier);
            clsCopy.buildClassifier(train);
            eval.evaluateModel(clsCopy, test);

            // 添加预测
            AddClassification filter = new AddClassification();
            filter.setClassifier(classifier);
            filter.setOutputClassification(true);
            filter.setOutputDistribution(true);
            filter.setOutputErrorFlag(true);
            filter.setInputFormat(train);
            // 训练分类器
            Filter.useFilter(train, filter);
            // 在测试集上预测
            Instances pred = Filter.useFilter(test, filter);

            if (predictedData == null)
                predictedData = new Instances(pred, 0); // 防止预测数据集为空
            for (int j = 0; j < pred.numInstances(); j++)
                predictedData.add(pred.instance(j));
        }

        // 评估结果输出
        System.out.println();
        System.out.println("=== 分类器设置 ===");
        // 分类器是否实现OptionHandler接口？
        if (classifier instanceof OptionHandler)
            System.out.println("分类器：" + classifier.getClass().getName() + " " + Utils.joinOptions(((OptionHandler) classifier).getOptions()));
        else
            System.out.println("分类器：" + classifier.getClass().getName());
        System.out.println("数据集：" + data.relationName());
        System.out.println("折数：" + folds);
        System.out.println("随机种子：" + seed);
        System.out.println();
        System.out.println(eval.toSummaryString("=== " + folds + "折交叉验证 ===", false));

        // 写入数据文件
        DataSink.write("d:/predictions.arff", predictedData);
    }
}
