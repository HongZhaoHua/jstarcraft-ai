package api.visualize;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.graphvisualizer.GraphVisualizer;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * 显示训练好的贝叶斯网络图形
 */
public class VisualizeBayesNet {

    public static void main(String args[]) throws Exception {
        // 贝叶斯网络分类器
        BayesNet cls = new BayesNet();
        // 数据集
        Instances data = DataSource.read("data/weather.nominal.arff");
        // 设置类别属性
        data.setClassIndex(data.numAttributes() - 1);
        // 构建分类器
        cls.buildClassifier(data);

        // 显示图形
        // 图可视化器
        GraphVisualizer gv = new GraphVisualizer();
        gv.readBIF(cls.graph());
        // 定义一个窗体对象jframe，窗体名称为"贝叶斯网络图形"
        JFrame jframe = new JFrame("贝叶斯网络图形");
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗体的大小为500*300像素
        jframe.setSize(500, 300);
        jframe.getContentPane().setLayout(new BorderLayout());
        jframe.getContentPane().add(gv, BorderLayout.CENTER);
        // 设置窗体可见
        jframe.setVisible(true);

        // 布局图形
        gv.layoutGraph();
    }
}
