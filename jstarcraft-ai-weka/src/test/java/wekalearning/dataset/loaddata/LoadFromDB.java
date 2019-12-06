package wekalearning.dataset.loaddata;

import weka.core.Instances;
import weka.core.converters.DatabaseLoader;
import weka.experiment.InstanceQuery;

public class LoadFromDB {

	public static void main(String[] args) throws Exception {
		InstanceQuery query = null;
		Instances data = null;

		// 使用InstanceQuery类
		System.out.println("使用InstanceQuery类从数据库中检索数据");
		query = new InstanceQuery();
		query.setDatabaseURL("jdbc:mysql://localhost:3306/weka");
		query.setUsername("weka");
		query.setPassword("weka");
		query.setQuery("select * from weather");
		data = query.retrieveInstances();
		// 使用最后一个属性作为类别属性
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		System.out.println("数据集内容：");
		System.out.println(data);

		// 使用DatabaseLoader类进行“批量检索”
		System.out.println("\n\n使用DatabaseLoader类从数据库中批量检索数据");
		DatabaseLoader loader = null;
		loader = new DatabaseLoader();
		loader.setSource("jdbc:mysql://localhost:3306/weka", "weka", "weka");
		loader.setQuery("select * from weather");
		Instances data1 = loader.getDataSet();
		// 使用最后一个属性作为类别属性
		if (data1.classIndex() == -1)
			data1.setClassIndex(data1.numAttributes() - 1);
		System.out.println("数据集内容：");
		System.out.println(data1);
	}

}
