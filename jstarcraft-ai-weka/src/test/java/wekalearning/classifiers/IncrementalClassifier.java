package wekalearning.classifiers;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

/**
 * 增量方式构建NaiveBayes分类器，并输出生成模型
 */
public class IncrementalClassifier {

	public static void main(String[] args) throws Exception {
		// 加载数据
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File("data/weather.nominal.arff"));
		Instances structure = loader.getStructure();
		structure.setClassIndex(structure.numAttributes() - 1);

		// 训练NaiveBayes分类器
		NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
		nb.buildClassifier(structure);
		Instance instance;
		while ((instance = loader.getNextInstance(structure)) != null)
			nb.updateClassifier(instance);

		// 输出生成模型
		System.out.println(nb);
	}
}
