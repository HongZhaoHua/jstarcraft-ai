package api.clusterers;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 * 本例展示如何执行“classes-to-clusters”评估
 */
public class ClassesToClusters {
    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = DataSource.read("data/contact-lenses.arff");
        data.setClassIndex(data.numAttributes() - 1);

        // 生成聚类器数据,过滤以去除类别属性
        Remove filter = new Remove();
        filter.setAttributeIndices("" + (data.classIndex() + 1));
        filter.setInputFormat(data);
        Instances dataClusterer = Filter.useFilter(data, filter);

        // 训练聚类器
        EM clusterer = new EM();
        // 如果有必要,可在这里设置更多选项
        // 构建聚类器
        clusterer.buildClusterer(dataClusterer);

        // 评估聚类器
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(data);

        // 输出结果
        System.out.println(eval.clusterResultsToString());
    }
}
