package ResultExhibition;

/**
 * Created by zzt_cc on 2016/6/30.
 */
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import Definition.overhead_line;
import Definition.transformer;
import Definition.underground_line;
import ResultProcessing.StringToNum;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart {
    ChartPanel frame1;
    public PieChart(){
        DefaultPieDataset data = getDataSet();
        JFreeChart chart = ChartFactory.createPieChart(null,data,true,false,false);
//        JFreeChart chart = ChartFactory.createPieChart3D(null,data,true,false,false);
//        JFreeChart chart = ChartFactory.createPieChart3D("��·������",data,true,false,false);
        //���ðٷֱ�
        PiePlot pieplot = (PiePlot) chart.getPlot();
        DecimalFormat df = new DecimalFormat("0.00%");//���һ��DecimalFormat������Ҫ������С������
        NumberFormat nf = NumberFormat.getNumberInstance();//���һ��NumberFormat����
        StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {2}", nf, df);//���StandardPieSectionLabelGenerator����
        pieplot.setLabelGenerator(sp1);//���ñ�ͼ��ʾ�ٷֱ�

        //û�����ݵ�ʱ����ʾ������
        pieplot.setNoDataMessage("��������ʾ");
        pieplot.setCircular(false);
        pieplot.setLabelGap(0.02D);

        pieplot.setIgnoreNullValues(true);//���ò���ʾ��ֵ
        pieplot.setIgnoreZeroValues(true);//���ò���ʾ��ֵ
        frame1=new ChartPanel (chart,true);

//        chart.getTitle().setFont(new Font("����",Font.BOLD,20));//���ñ�������
        PiePlot piePlot= (PiePlot) chart.getPlot();//��ȡͼ���������
        piePlot.setLabelFont(new Font("����",Font.BOLD,15));//�������
        chart.getLegend().setItemFont(new Font("����",Font.BOLD,15));


    }
    private static DefaultPieDataset getDataSet() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Iterator iter = overhead_line.overhead_lineResultMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            dataset.setValue(entry.getKey().toString(),StringToNum.getpower_losses(overhead_line.overhead_lineResultMap.get(entry.getKey()).get("power_losses")));
        }
        iter = underground_line.underground_lineResultMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            dataset.setValue(entry.getKey().toString(),StringToNum.getpower_losses(underground_line.underground_lineResultMap.get(entry.getKey()).get("power_losses")));
        }
        iter = transformer.transformerResultMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            dataset.setValue(entry.getKey().toString(),StringToNum.getpower_losses(transformer.transformerResultMap.get(entry.getKey()).get("power_losses")));
        }
//        dataset.setValue("ƻ��",100);
//        dataset.setValue("����",200);
//        dataset.setValue("����",300);
//        dataset.setValue("�㽶",400);
//        dataset.setValue("��֦",500);
        return dataset;
    }
    public ChartPanel getChartPanel(){
        return frame1;
    }
}
