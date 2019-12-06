package wekalearning.dataset.loaddata;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.XRFFLoader;

import java.io.File;

public class LoadXrffFile {

    public static void main(String[] args) {
        // 使用DataSource类的read方法来加载 XRFF 文件
        System.out.println("使用DataSource类的read方法来加载 XRFF文件");
        System.out.println("由于文件扩展名与数据集格式不匹配，肯定加载失败");
        // 同样也要捕获程序异常，这里已抛出
        try {
            Instances data = DataSource.read("data/weather.nominal.xml");
            System.out.println("\n数据集内容：");
            System.out.println(data);
        } catch (Exception e) {
            System.out.println("加载文件失败!");
        }

        System.out.println("\n\n使用直接指定加载器的方法来加载XRFF文件");
        System.out.println("由于直接指定符合数据集格式的加载器，肯定加载成功");
        try {
            XRFFLoader loader = new XRFFLoader();
            loader.setSource(new File("data/weather.nominal.xml"));
            Instances data = loader.getDataSet();
            System.out.println("\n数据集内容：");
            System.out.println(data);
        } catch (Exception e) {
            System.out.println("加载文件失败!");
        }

    }

}
