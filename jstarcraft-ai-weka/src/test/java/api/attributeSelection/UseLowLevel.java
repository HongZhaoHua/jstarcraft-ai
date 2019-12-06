package api.attributeSelection;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * 使用底层API
 */
public class UseLowLevel {

    public static void main(String[] args) throws Exception {
        // 加载数据
        DataSource source = new DataSource("data/weather.numeric.arff");
        Instances data = source.getDataSet();
        // 设置类别属性索引
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        System.out.println("\n 使用底层API");
        AttributeSelection attsel = new AttributeSelection();
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        attsel.setEvaluator(eval);
        attsel.setSearch(search);
        attsel.SelectAttributes(data);
        int[] indices = attsel.selectedAttributes();
        System.out.println("选择属性索引(从0开始):\n" + Utils.arrayToString(indices));
    }

}
