package api.dataset.savedata;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.DatabaseSaver;

/**
 * 将数据集保存到数据库
 */
public class Save2DB {

    public static void main(String[] args) {

        try {
            // 加载数据
            Instances data = new Instances(DataSource.read("data/iris.arff"));
            System.out.println("完成加载数据");

            // 批量方式保存数据到数据库
            DatabaseSaver saver = new DatabaseSaver();
            saver.setDestination("jdbc:mysql://localhost:3306/weka", "weka", "weka");
            // 在这里明确指定表名：
            saver.setTableName("iris");
            saver.setRelationForTableName(false);
            saver.setInstances(data);
            saver.writeBatch();
            System.out.println("完成批量方式保存数据");

            // 增量方式保存数据到数据库
            DatabaseSaver saver2 = new DatabaseSaver();
            saver2.setDestination("jdbc:mysql://localhost:3306/weka", "weka", "weka");
            // 在这里明确指定表名：
            saver2.setTableName("iris2");
            saver2.setRelationForTableName(false);
            saver2.setRetrieval(DatabaseSaver.INCREMENTAL);
            saver2.setStructure(data);
            for (int i = 0; i < data.numInstances(); i++) {
                saver2.writeIncremental(data.instance(i));
            }
            // 通知保存器已经完成
            saver2.writeIncremental(null);
            System.out.println("完成增量方式保存数据");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
