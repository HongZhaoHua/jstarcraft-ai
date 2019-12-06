package wekalearning.attributeSelection;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * 使用过滤器
 */
public class UseFilter {

    public static void main(String[] args) throws Exception {
        // 加载数据
        DataSource source = new DataSource("data/weather.numeric.arff");
        Instances data = source.getDataSet();
        // 设置类别属性索引
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        System.out.println("\n 使用过滤器");
        AttributeSelection filter = new AttributeSelection();
        CfsSubsetEval eval = new CfsSubsetEval();
        GreedyStepwise search = new GreedyStepwise();
        search.setSearchBackwards(true);
        filter.setEvaluator(eval);
        filter.setSearch(search);
        filter.setInputFormat(data);
        Instances newData = Filter.useFilter(data, filter);
        System.out.println(newData);
    }

}
