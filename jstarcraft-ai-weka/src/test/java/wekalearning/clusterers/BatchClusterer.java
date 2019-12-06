package wekalearning.clusterers;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.clusterers.EM;

import java.io.File;

/**
 * 批量训练EM算法
 */
public class BatchClusterer {

	public static void main(String[] args) throws Exception {
		// 加载数据
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File("data/contact-lenses.arff"));
		Instances data = loader.getDataSet();

		// 构建聚类器
		String[] options = new String[2];
		options[0] = "-I"; // 最大迭代次数
		options[1] = "100";
		EM clusterer = new EM(); // 聚类器的新实例
		clusterer.setOptions(options); // 设置选项
		clusterer.buildClusterer(data);

		// 输出生成模型
		System.out.println(clusterer);
	}
}
