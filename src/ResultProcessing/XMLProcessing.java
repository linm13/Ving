package ResultProcessing;

/**
 * Created by zzt_cc on 2016/6/29.
 */

import Definition.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.xpath.XPath;

import java.io.*;
import java.util.List;


public class XMLProcessing {
    public static void XmlProcess(String sDateTime) {
        XMLPreprocessing.XMLPreprocess(sDateTime);
        File f = new File(sDateTime + "c.xml");
        SAXBuilder saxb = new SAXBuilder();
        try {
            Document xmldoc = saxb.build(f);
            XPath xpath = XPath.newInstance("//node");//创建一个查询node元素的XPATH。
            List nodes = xpath.selectNodes(xmldoc);//进行查询
//            System.out.println("返回记录数：" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                node.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //返回该node的名称的文本值。
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("nominal_voltage"),
                        ((Element) nodes.get(i)).getChildText("voltage_A"),         //返回该node的电压值。
                        ((Element) nodes.get(i)).getChildText("voltage_B"),         //返回该node的电压值。
                        ((Element) nodes.get(i)).getChildText("voltage_C"),         //返回该node的电压值。
                        ((Element) nodes.get(i)).getChildText("bustype"));
            }
            xpath = XPath.newInstance("//load");//创建一个查询load元素的XPATH。
            nodes = xpath.selectNodes(xmldoc);//进行查询
//            System.out.println("返回记录数：" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                load.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //返回该load的名称的文本值。
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("nominal_voltage"),
                        ((Element) nodes.get(i)).getChildText("voltage_A"),         //返回该load的电压值。
                        ((Element) nodes.get(i)).getChildText("voltage_B"),         //返回该load的电压值。
                        ((Element) nodes.get(i)).getChildText("voltage_C"),         //返回该load的电压值。
                        ((Element) nodes.get(i)).getChildText("constant_power_A"),         //返回该load的功率值。
                        ((Element) nodes.get(i)).getChildText("constant_power_B"),         //返回该load的功率值。
                        ((Element) nodes.get(i)).getChildText("constant_power_B")         //返回该load的功率值。
                );
            }
            xpath = XPath.newInstance("//overhead_line");//创建一个查询架空线元素的XPATH。
            nodes = xpath.selectNodes(xmldoc);//进行查询
//            System.out.println("返回记录数：" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                overhead_line.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //返回该架空线的名称的文本值。
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("from"),
                        ((Element) nodes.get(i)).getChildText("to"),
                        ((Element) nodes.get(i)).getChildText("length"),
                        ((Element) nodes.get(i)).getChildText("configuration"),
                        ((Element) nodes.get(i)).getChildText("power_in"),
                        ((Element) nodes.get(i)).getChildText("power_out"),
                        ((Element) nodes.get(i)).getChildText("power_losses")
                );
            }
            xpath = XPath.newInstance("//underground_line");//创建一个查询地下电缆元素的XPATH。
            nodes = xpath.selectNodes(xmldoc);//进行查询
//            System.out.println("返回记录数：" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                underground_line.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //返回该地下电缆的名称的文本值。
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("from"),
                        ((Element) nodes.get(i)).getChildText("to"),
                        ((Element) nodes.get(i)).getChildText("length"),
                        ((Element) nodes.get(i)).getChildText("configuration"),
                        ((Element) nodes.get(i)).getChildText("power_in"),
                        ((Element) nodes.get(i)).getChildText("power_out"),
                        ((Element) nodes.get(i)).getChildText("power_losses")
                );
            }
            xpath = XPath.newInstance("//transformer");//创建一个查询地下电缆元素的XPATH。
            nodes = xpath.selectNodes(xmldoc);//进行查询
//            System.out.println("返回记录数：" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                transformer.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //返回该地下电缆的名称的文本值。
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("from"),
                        ((Element) nodes.get(i)).getChildText("to"),
                        ((Element) nodes.get(i)).getChildText("configuration"),
                        ((Element) nodes.get(i)).getChildText("power_in"),
                        ((Element) nodes.get(i)).getChildText("power_out"),
                        ((Element) nodes.get(i)).getChildText("power_losses")
                );
            }
            System.out.println(node.nodeResultMap);
            System.out.println(node.nodeMap);
            System.out.println(load.loadResultMap);
            System.out.println(load.loadMap);
            System.out.println(overhead_line.overhead_lineResultMap);
            System.out.println(overhead_line.overhead_lineMap);
            System.out.println(underground_line.underground_lineResultMap);
            System.out.println(underground_line.underground_lineMap);
            System.out.println(transformer.transformerResultMap);
            System.out.println(transformer.transformerMap);

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
