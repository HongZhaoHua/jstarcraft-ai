package wekalearning.visualize;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JFrame;

/**
 * 由数据集产生并显示ROC曲线，使用NaiveBayes的默认设置来产生ROC数据
 */
public class VisualizeROC {

    public static void main(String[] args) throws Exception {
        // 加载数据
        Instances data = DataSource.read("data/weather.nominal.arff");
        data.setClassIndex(data.numAttributes() - 1);

        // 评估分类器
        Classifier classifier = new NaiveBayes();
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(classifier, data, 10, new Random(1234));

        // 第一步，生成可绘制的数据
        ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0;
        Instances curve = tc.getCurve(eval.predictions(), classIndex);

        // 第二步，将可绘制数据放入绘图容器
        PlotData2D plotdata = new PlotData2D(curve);
        plotdata.setPlotName(curve.relationName());
        plotdata.addInstanceNumberAttribute();

        // 第三步，将绘图容器添加至可视化面板
        ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
        tvp.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
        tvp.setName(curve.relationName());
        // 指定连接哪些点
        boolean[] cp = new boolean[curve.numInstances()];
        for (int i = 1; i < cp.length; i++)
            cp[i] = true;
        plotdata.setConnectPoints(cp);
        // 添加绘图
        tvp.addPlot(plotdata);

        // 第四步，将可视化面板添加到JFrame
        final JFrame jf = new JFrame("WEKA ROC: " + tvp.getName());
        // 设置窗体的大小为500*400像素
        jf.setSize(500, 400);
        // 设置布局管理器
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(tvp, BorderLayout.CENTER);
        // 自动隐藏并释放该窗体
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置窗体可见
        jf.setVisible(true);
    }
}
