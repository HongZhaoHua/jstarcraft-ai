package wekalearning.classifiers;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

/**
 * 批量方式构建J48分类器，并输出决策树模型
 */
public class BatchClassifier {

    public static void main(String[] args) throws Exception {
        // 加载数据
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File("data/weather.nominal.arff"));
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

        // 训练J48分类器
        String[] options = new String[1];
        options[0] = "-U"; // 不修剪树选项
        J48 tree = new J48(); // J48分类器对象
        tree.setOptions(options); // 设置选项
        tree.buildClassifier(data); // 构建分类器

        // 输出生成模型
        System.out.println(tree);
    }
}
