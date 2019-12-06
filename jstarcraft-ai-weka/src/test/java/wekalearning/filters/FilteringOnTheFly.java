package wekalearning.filters;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;

/**
 * 即时过滤实例程序。演示如何使用FilteredClassifier元过滤器， 程序使用Remove过滤器和J48分类器。
 */
public class FilteringOnTheFly {

	public static void main(String[] args) throws Exception {
		// 加载数据
		Instances train = DataSource.read("data/segment-challenge.arff");
		Instances test = DataSource.read("data/segment-test.arff");
		// 设置类别属性
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(test.numAttributes() - 1);
		// 检查训练集和测试集是否兼容
		if (!train.equalHeaders(test))
			throw new Exception("训练集和测试集不兼容：\n" + train.equalHeadersMsg(test));

		// 过滤器
		Remove rm = new Remove();
		rm.setAttributeIndices("1"); // 删除第1个属性

		// 分类器
		J48 j48 = new J48();
		j48.setUnpruned(true); // 使用未裁剪的J48

		// 元分类器
		FilteredClassifier fc = new FilteredClassifier();
		fc.setFilter(rm);
		fc.setClassifier(j48);

		// 训练并预测
		fc.buildClassifier(train);
		for (int i = 0; i < test.numInstances(); i++) {
			double pred = fc.classifyInstance(test.instance(i));
			System.out.print("编号：" + (i + 1));
			System.out.print("，实际类别：" + test.classAttribute().value((int) test.instance(i).classValue()));
			System.out.println("，预测类别：" + test.classAttribute().value((int) pred));
		}
	}
}
