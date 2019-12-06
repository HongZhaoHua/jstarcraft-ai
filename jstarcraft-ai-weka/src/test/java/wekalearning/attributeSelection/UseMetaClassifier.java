package wekalearning.attributeSelection;

import java.util.Random;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 使用元分类器
 */
public class UseMetaClassifier {

	public static void main(String[] args) throws Exception {
		// 加载数据
		DataSource source = new DataSource("data/weather.numeric.arff");
		Instances data = source.getDataSet();
		// 设置类别属性索引
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		System.out.println("\n 使用元分类器");
		AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
		CfsSubsetEval eval = new CfsSubsetEval();
		GreedyStepwise search = new GreedyStepwise();
		search.setSearchBackwards(true);
		J48 base = new J48();
		classifier.setClassifier(base);
		classifier.setEvaluator(eval);
		classifier.setSearch(search);
		Evaluation evaluation = new Evaluation(data);
		evaluation.crossValidateModel(classifier, data, 10, new Random(1234));
		System.out.println(evaluation.toSummaryString());
	}

}
