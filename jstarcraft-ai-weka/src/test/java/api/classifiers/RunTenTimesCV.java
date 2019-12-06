package api.classifiers;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Random;

/**
 * 执行10次运行的10折交叉验证
 */
public class RunTenTimesCV {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = DataSource.read("data/labor.arff");
        // 设置类别索引
        data.setClassIndex(data.numAttributes() - 1);

        // 分类器
        String[] tmpOptions = new String[2];
        String classname = "weka.classifiers.trees.J48";
        tmpOptions[0] = "-C"; // 默认参数
        tmpOptions[1] = "0.25";
        Classifier classifier = (Classifier) Utils.forName(Classifier.class, classname, tmpOptions);

        // 其他选项
        int runs = 10; // 运行次数
        int folds = 10; // 折数

        // 执行交叉验证
        for (int i = 0; i < runs; i++) {
            // 随机化数据
            int seed = i + 1234; // 随机种子
            Random rand = new Random(seed);
            Instances newData = new Instances(data);
            newData.randomize(rand);
            // 如果类别为标称型，则根据其类别值进行分层
            if (newData.classAttribute().isNominal())
                newData.stratify(folds);

            Evaluation eval = new Evaluation(newData);
            for (int j = 0; j < folds; j++) {
                // 训练集
                Instances train = newData.trainCV(folds, j);
                // 测试集
                Instances test = newData.testCV(folds, j);

                // 构建并评估分类器
                Classifier clsCopy = AbstractClassifier.makeCopy(classifier);
                clsCopy.buildClassifier(train);
                eval.evaluateModel(clsCopy, test);
            }

            // 评估结果输出
            System.out.println();
            System.out.println("=== 运行第 " + (i + 1) + "次的分类器设置 ===");
            System.out.println("分类器：" + Utils.toCommandLine(classifier));
            System.out.println("数据集：" + data.relationName());
            System.out.println("折数：" + folds);
            System.out.println("随机种子：" + seed);
            System.out.println();
            System.out.println(eval.toSummaryString("=== " + folds + "折交叉验证 运行第" + (i + 1) + "次 ===", false));
        }
    }
}
