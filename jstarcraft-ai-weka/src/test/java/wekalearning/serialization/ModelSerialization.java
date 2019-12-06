package wekalearning.serialization;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 模型序列化和反序列化示例
 */
public class ModelSerialization {

	public static void main(String[] args) throws Exception {
		// 加载数据
		Instances inst = DataSource.read("data/weather.numeric.arff");
		inst.setClassIndex(inst.numAttributes() - 1);
		// 训练J48
		Classifier cls = new J48();
		cls.buildClassifier(inst);
		// 序列化模型
		SerializationHelper.write("data/j48.model", cls);
		System.out.println("序列化模型成功！\n");

		// 反序列化模型
		Classifier cls2 = (Classifier) SerializationHelper.read("data/j48.model");
		System.out.println("反序列化模型成功！");
		System.out.println("反序列化模型如下：");
		System.out.println(cls2);
	}

}
