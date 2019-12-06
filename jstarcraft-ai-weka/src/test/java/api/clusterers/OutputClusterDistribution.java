package api.clusterers;

import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 本例展示在训练集上构建EM聚类器，然后在测试集上预测簇并输出簇的隶属度
 */
public class OutputClusterDistribution {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances train = DataSource.read("data/segment-challenge.arff");
        Instances test = DataSource.read("data/segment-test.arff");
        if (!train.equalHeaders(test))
            throw new Exception("训练集和测试集不兼容：" + train.equalHeadersMsg(test));

        // 构建聚类器
        EM clusterer = new EM();
        clusterer.buildClusterer(train);

        // 输出预测
        System.out.println("编号 - 簇  \t-\t 分布");
        for (int i = 0; i < test.numInstances(); i++) {
            int cluster = clusterer.clusterInstance(test.instance(i));
            double[] dist = clusterer.distributionForInstance(test.instance(i));
            System.out.print((i + 1));
            System.out.print(" - ");
            System.out.print(cluster);
            System.out.print(" - ");
            System.out.print(Utils.arrayToString(dist));
            System.out.println();
        }
    }
}
