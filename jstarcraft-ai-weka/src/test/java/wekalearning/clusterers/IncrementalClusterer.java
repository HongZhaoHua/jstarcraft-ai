package wekalearning.clusterers;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.clusterers.Cobweb;

import java.io.File;

/**
 * 增量训练Cobweb算法
 */
public class IncrementalClusterer {

    public static void main(String[] args) throws Exception {
        // 加载数据
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File("data/contact-lenses.arff"));
        Instances structure = loader.getStructure();

        // 训练Cobweb
        Cobweb cw = new Cobweb();
        cw.buildClusterer(structure);
        Instance current;
        while ((current = loader.getNextInstance(structure)) != null)
            cw.updateClusterer(current);
        cw.updateFinished();

        // 输出生成模型
        System.out.println(cw);
    }
}
