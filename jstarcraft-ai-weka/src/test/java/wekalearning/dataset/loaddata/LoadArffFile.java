package wekalearning.dataset.loaddata;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;

public class LoadArffFile {

    public static void main(String[] args) throws Exception {
        // 使用DataSource类的read方法来加载ARFF文件
        System.out.println("\n\n使用DataSource类的read方法来加载 ARFF文件");
        // 同样也要捕获程序异常，这里已抛出
        Instances data1 = DataSource.read("data/weather.nominal.arff");
        System.out.println("\n数据集内容：");
        System.out.println(data1);

        // 使用直接指定加载器的方法来加载ARFF文件
        System.out.println("\n\n使用直接指定加载器的方法来加载ARFF文件");
        // 创建一个ArffLoader 类实例
        ArffLoader loader = new ArffLoader();
        // 加载ARFF文件，
        // 此时从系统中读文件时要捕获异常，这里通过在main函数中抛出
        loader.setSource(new File("data/weather.numeric.arff"));
        Instances data2 = loader.getDataSet();
        System.out.println("\n数据集内容：");
        System.out.println(data2);

    }

}
