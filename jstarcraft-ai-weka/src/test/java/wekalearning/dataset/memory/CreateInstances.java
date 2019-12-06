package wekalearning.dataset.memory;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;

/**
 * 使用不同属性类型生成weka.core.Instances对象
 */
public class CreateInstances {

	/**
	 * 生成Instances对象并以ARFF格式输出到控制台
	 */
	public static void main(String[] args) throws Exception {
		ArrayList<Attribute> atts;
		ArrayList<Attribute> attsRel;
		ArrayList<String> attVals;
		ArrayList<String> attValsRel;
		Instances data;
		Instances dataRel;
		double[] vals;
		double[] valsRel;
		int i;

		// 1. 设置属性
		atts = new ArrayList<Attribute>();
		// - 数值型
		atts.add(new Attribute("att1"));
		// - 标称型
		// 需创建标签
		attVals = new ArrayList<String>();
		for (i = 0; i < 5; i++)
			attVals.add("val" + (i + 1));
		atts.add(new Attribute("att2", attVals));
		// - 字符串型
		atts.add(new Attribute("att3", (ArrayList<String>) null));
		// - 日期型
		atts.add(new Attribute("att4", "yyyy-MM-dd"));
		// - 关系型
		attsRel = new ArrayList<Attribute>();
		// -- 数值型
		attsRel.add(new Attribute("att5.1"));
		// -- 标称型
		attValsRel = new ArrayList<String>();
		for (i = 0; i < 5; i++)
			attValsRel.add("val5." + (i + 1));
		attsRel.add(new Attribute("att5.2", attValsRel));
		dataRel = new Instances("att5", attsRel, 0);
		atts.add(new Attribute("att5", dataRel, 0));

		// 2. 创建Instances对象
		data = new Instances("MyRelation", atts, 0);

		// 3. 添加数据
		// 第一个实例
		vals = new double[data.numAttributes()];
		// - 数值型
		vals[0] = Math.PI;
		// - 标称型
		vals[1] = attVals.indexOf("val3");
		// - 字符串型
		vals[2] = data.attribute(2).addStringValue("A string.");
		// - 日期型
		vals[3] = data.attribute(3).parseDate("2013-04-05");
		// - 关系型
		dataRel = new Instances(data.attribute(4).relation(), 0);
		// -- 第一个实例
		valsRel = new double[2];
		valsRel[0] = Math.PI + 1;
		valsRel[1] = attValsRel.indexOf("val5.3");
		dataRel.add(new DenseInstance(1.0, valsRel));
		// -- 第二个实例
		valsRel = new double[2];
		valsRel[0] = Math.PI + 2;
		valsRel[1] = attValsRel.indexOf("val5.2");
		dataRel.add(new DenseInstance(1.0, valsRel));
		vals[4] = data.attribute(4).addRelation(dataRel);
		// 添加
		data.add(new DenseInstance(1.0, vals));

		// 第二个实例
		vals = new double[data.numAttributes()]; // 重要：必须new新建double数组！
		// - 数值型
		vals[0] = Math.E;
		// - 标称型
		vals[1] = attVals.indexOf("val1");
		// - 字符串型
		vals[2] = data.attribute(2).addStringValue("Yet another string.");
		// - 日期型
		vals[3] = data.attribute(3).parseDate("2013-04-10");
		// - 关系型
		dataRel = new Instances(data.attribute(4).relation(), 0);
		// -- 第一个实例
		valsRel = new double[2];
		valsRel[0] = Math.E + 1;
		valsRel[1] = attValsRel.indexOf("val5.4");
		dataRel.add(new DenseInstance(1.0, valsRel));
		// -- 第二个实例
		valsRel = new double[2];
		valsRel[0] = Math.E + 2;
		valsRel[1] = attValsRel.indexOf("val5.1");
		dataRel.add(new DenseInstance(1.0, valsRel));
		vals[4] = data.attribute(4).addRelation(dataRel);
		// 添加
		data.add(new DenseInstance(1.0, vals));

		// 4. 输出数据
		System.out.println(data);
	}
}
