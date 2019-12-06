package wekalearning.dataset.optionshandling;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class OptionHandler {

    public static void main(String[] args) throws Exception {
        // 加载数据文件
        Instances data = new Instances(DataSource.read("data/weather.nominal.arff"));
        System.out.println("数据集内容：");
        System.out.println(data);

        // 手工组装String数组
        System.out.println("\n\n手工组装String数组");
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "1";
        Remove rm = new Remove();
        rm.setOptions(options);
        // 数据集过滤
        rm.setInputFormat(data);
        Instances inst1 = Filter.useFilter(data, rm);
        System.out.println("\n数据集过滤后的内容：");
        System.out.println(inst1);

        // 使用weka.core.Utils类的splitOptions(String)方法
        System.out.println("\n\n使用Utils类的splitOptions(String)方法");
        String[] options2 = Utils.splitOptions("-R 1");
        Remove rm2 = new Remove();
        rm2.setOptions(options2);
        rm2.setInputFormat(data);
        Instances inst2 = Filter.useFilter(data, rm2);
        System.out.println("\n数据集过滤后的内容：");
        System.out.println(inst2);

        // 使用属性的set方法
        System.out.println("\n\n使用属性的set方法");
        Remove rm3 = new Remove();
        rm3.setAttributeIndices("1");
        rm3.setInputFormat(data);
        Instances inst3 = Filter.useFilter(data, rm3);
        System.out.println("\n数据集过滤后的内容：");
        System.out.println(inst3);
    }
}
