package wekalearning.classifiers;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Random;

/**
 * 执行单次运行的10折交叉验证
 */
public class RunOnceCV {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = DataSource.read("data/ionosphere.arff");
        // 设置类别索引
        data.setClassIndex(data.numAttributes() - 1);

        // 分类器
        String[] options = new String[2];
        String classname = "weka.classifiers.trees.J48";
        options[0] = "-C"; // 默认参数
        options[1] = "0.25";
        Classifier classifier = (Classifier) Utils.forName(Classifier.class, classname, options);

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

        // 执行交叉验证
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
        }

        // 输出评估
        System.out.println();
        System.out.println("=== 分类器设置  ===");
        System.out.println("分类器：" + Utils.toCommandLine(classifier));
        System.out.println("数据集：" + data.relationName());
        System.out.println("折数：" + folds);
        System.out.println("随机种子：" + seed);
        System.out.println();
        System.out.println(eval.toSummaryString("=== " + folds + "折交叉验证 ===", false));
    }
}
