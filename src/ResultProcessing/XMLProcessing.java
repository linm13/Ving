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
            XPath xpath = XPath.newInstance("//node");//����һ����ѯnodeԪ�ص�XPATH��
            List nodes = xpath.selectNodes(xmldoc);//���в�ѯ
//            System.out.println("���ؼ�¼����" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                node.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //���ظ�node�����Ƶ��ı�ֵ��
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("nominal_voltage"),
                        ((Element) nodes.get(i)).getChildText("voltage_A"),         //���ظ�node�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("voltage_B"),         //���ظ�node�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("voltage_C"),         //���ظ�node�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("bustype"));
            }
            xpath = XPath.newInstance("//load");//����һ����ѯloadԪ�ص�XPATH��
            nodes = xpath.selectNodes(xmldoc);//���в�ѯ
//            System.out.println("���ؼ�¼����" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                load.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //���ظ�load�����Ƶ��ı�ֵ��
                        ((Element) nodes.get(i)).getChildText("phases"),
                        ((Element) nodes.get(i)).getChildText("nominal_voltage"),
                        ((Element) nodes.get(i)).getChildText("voltage_A"),         //���ظ�load�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("voltage_B"),         //���ظ�load�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("voltage_C"),         //���ظ�load�ĵ�ѹֵ��
                        ((Element) nodes.get(i)).getChildText("constant_power_A"),         //���ظ�load�Ĺ���ֵ��
                        ((Element) nodes.get(i)).getChildText("constant_power_B"),         //���ظ�load�Ĺ���ֵ��
                        ((Element) nodes.get(i)).getChildText("constant_power_B")         //���ظ�load�Ĺ���ֵ��
                );
            }
            xpath = XPath.newInstance("//overhead_line");//����һ����ѯ�ܿ���Ԫ�ص�XPATH��
            nodes = xpath.selectNodes(xmldoc);//���в�ѯ
//            System.out.println("���ؼ�¼����" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                overhead_line.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //���ظüܿ��ߵ����Ƶ��ı�ֵ��
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
            xpath = XPath.newInstance("//underground_line");//����һ����ѯ���µ���Ԫ�ص�XPATH��
            nodes = xpath.selectNodes(xmldoc);//���в�ѯ
//            System.out.println("���ؼ�¼����" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                underground_line.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //���ظõ��µ��µ����Ƶ��ı�ֵ��
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
            xpath = XPath.newInstance("//transformer");//����һ����ѯ���µ���Ԫ�ص�XPATH��
            nodes = xpath.selectNodes(xmldoc);//���в�ѯ
//            System.out.println("���ؼ�¼����" + nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                transformer.addResultMap(((Element) nodes.get(i)).getChildText("name"),    //���ظõ��µ��µ����Ƶ��ı�ֵ��
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
