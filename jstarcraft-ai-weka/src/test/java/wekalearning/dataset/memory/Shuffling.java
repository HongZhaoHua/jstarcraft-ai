package wekalearning.dataset.memory;

import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Shuffling {

	public static void main(String[] args) throws Exception {
		Instances data = DataSource
				.read("data/weather.nominal.arff");
		System.out.println("\n原数据集内容：");
		System.out.println(data);

		// 以下使用Random缺省构造函数。如果要得到可重复的伪随机序列，这种方式不可取
		Instances data1 = new Instances(data);
		data1.randomize(new Random());
		System.out.println("\n使用缺省构造函数后第一次的数据集内容：");
		System.out.println(data1);

		Instances data2 = new Instances(data);
		data2.randomize(new Random());
		System.out.println("\n使用缺省构造函数后第二次的数据集内容：");
		System.out.println(data2);

		// 以下使用Random提供随机数种子的构造函数。推荐采用这种方式
		long seed = 1234l;
		Instances data3 = new Instances(data);
		data3.randomize(new Random(seed));
		System.out.println("\n使用提供随机数种子的构造函数后第一次的数据集内容：");
		System.out.println(data3);

		Instances data4 = new Instances(data);
		data4.randomize(new Random(seed));
		System.out.println("\n使用提供随机数种子的构造函数后第二次的数据集内容：");
		System.out.println(data4);

	}

}
