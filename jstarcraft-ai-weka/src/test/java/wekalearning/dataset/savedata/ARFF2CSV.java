package wekalearning.dataset.savedata;

import java.io.File;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * ARFF文件转换为CSV文件
 */
public class ARFF2CSV {

    public static void main(String[] args) {

        try {
            // 加载数据
            Instances data = new Instances(DataSource.read("data/iris.arff"));
            System.out.println("完成加载数据");

            // 使用DataSink类，保存为CSV
            DataSink.write("data/iris.csv", data);
            System.out.println("完成使用DataSink类保存数据");

            // 明确指定转换器，保存为CSV
            CSVSaver saver = new CSVSaver();
            saver.setInstances(data);
            saver.setFile(new File("data/iris2.csv"));
            saver.writeBatch();
            System.out.println("完成指定CSVSaver转换器保存数据");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
