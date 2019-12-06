package api.visualize;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * 训练J48并可视化决策树
 */
public class VisualizeTree {

    public static void main(String args[]) throws Exception {
        // 构建J48分类器
        J48 cls = new J48();
        Instances data = DataSource.read("data/weather.nominal.arff");
        data.setClassIndex(data.numAttributes() - 1);
        cls.buildClassifier(data);

        // 显示树
        TreeVisualizer tv = new TreeVisualizer(null, cls.graph(), new PlaceNode2());
        JFrame jf = new JFrame("J48分类器树可视化器");
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setSize(600, 400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.setVisible(true);

        // 调整树
        tv.fitToScreen();
    }
}
