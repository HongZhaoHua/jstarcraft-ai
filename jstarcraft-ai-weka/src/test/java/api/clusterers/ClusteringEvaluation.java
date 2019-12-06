package api.clusterers;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 三种评估聚类器的方式
 */
public class ClusteringEvaluation {

    public static void main(String[] args) throws Exception {
        String filename = "data/contact-lenses.arff";
        ClusterEvaluation clusterEval;
        Instances data;
        String[] options;
        DensityBasedClusterer dbc; // 基于密度的聚类器
        double logLikelyhood;

        // 加载数据
        data = DataSource.read(filename);

        // 常规方法
        System.out.println("\n****** 常规方法");
        options = new String[2];
        options[0] = "-t"; // 指定训练文件
        options[1] = filename;
        String output = ClusterEvaluation.evaluateClusterer(new EM(), options);
        System.out.println(output);

        // 手工调用
        System.out.println("\n****** 手工调用");
        dbc = new EM();
        dbc.buildClusterer(data);
        clusterEval = new ClusterEvaluation();
        clusterEval.setClusterer(dbc);
        clusterEval.evaluateClusterer(new Instances(data));
        System.out.println(clusterEval.clusterResultsToString());

        // 基于密度的聚类器交叉验证
        System.out.println("\n****** 交叉验证");
        dbc = new EM();
        logLikelyhood = ClusterEvaluation.crossValidateModel(dbc, data, 10, data.getRandomNumberGenerator(1234));
        System.out.println("对数似然: " + logLikelyhood);
    }
}
