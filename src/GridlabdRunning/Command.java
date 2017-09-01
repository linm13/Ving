package GridlabdRunning;

/**
 * Created by zzt_cc on 2016/6/28.
 */

import Definition.*;
import ResultExhibition.ColouredTopo;
import ResultProcessing.XMLPreprocessing;
import ResultProcessing.XMLProcessing;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class Command {
    public static void exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        java.util.Date dt = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        String sDateTime = sdf.format(dt);
//        String fileDirectory = sDateTime + ".glm";
//        String outputDirectory = sDateTime + ".xml";
//        FileWrite.WriteAll(fileDirectory);
//        String commandStr = "gridlabd " + fileDirectory + " --output " + outputDirectory;
//        Command.exeCmd(commandStr);
//        XMLPreprocessing.XMLPreprocess(sDateTime);
//        XMLProcessing.XmlProcess(sDateTime);
//
//        HashMap<String,Point> nodePoint=new HashMap<String,Point>();
//        HashMap<String,Point> loadPointHash=new HashMap<String,Point>();
//        HashMap<String,Point[]> overhead_linePoint=new HashMap<String,Point[]>();
//        HashMap<String,Point[]> underground_linePoint=new HashMap<String,Point[]>();
//        HashMap<String,Point[]> transformer_linePoint=new HashMap<String,Point[]>();
//
//        Iterator iter = node.nodeResultMap.entrySet().iterator();
//        int i=1;
//        while(iter.hasNext()){
//            Map.Entry entry = (Map.Entry) iter.next();
//            nodePoint.put(entry.getKey().toString(),new Point(200*i,170*i));
//            i++;
//        }
//        iter = load.loadResultMap.entrySet().iterator();
//        while(iter.hasNext()){
//            Map.Entry entry = (Map.Entry) iter.next();
//            loadPointHash.put(entry.getKey().toString(),new Point(800,150));
//        }
//        iter = overhead_line.overhead_lineResultMap.entrySet().iterator();
//        i=1;
//        while(iter.hasNext()){
//            Map.Entry entry = (Map.Entry) iter.next();
//            Point[] pointTemp=new Point[2];
//            pointTemp[0]=new Point(133*i,33*i);
//            i++;
//            pointTemp[1]=new Point(230*i,40*i);
//            i++;
//            overhead_linePoint.put(entry.getKey().toString(),pointTemp);
//        }
//        iter = underground_line.underground_lineResultMap.entrySet().iterator();
//        i=1;
//        while(iter.hasNext()){
//            Map.Entry entry = (Map.Entry) iter.next();
//            Point[] pointTemp=new Point[2];
//            pointTemp[0]=new Point(100*i,50*i);
//            i++;
//            pointTemp[1]=new Point(156*i,10*i);
//            underground_linePoint.put(entry.getKey().toString(),pointTemp);
//        }
//        iter = transformer.transformerResultMap.entrySet().iterator();
//        i=1;
//        while(iter.hasNext()){
//            Map.Entry entry = (Map.Entry) iter.next();
//            Point[] pointTemp=new Point[2];
//            pointTemp[0]=new Point(133*i,87*i);
//            i++;
//            pointTemp[1]=new Point(250*i,50*i);
//            transformer_linePoint.put(entry.getKey().toString(),pointTemp);
//        }
//
//        ColouredTopo colouredTopoFrame=new ColouredTopo(nodePoint,loadPointHash,overhead_linePoint,underground_linePoint,transformer_linePoint);
//        colouredTopoFrame.setVisible(true);


//    }



}
