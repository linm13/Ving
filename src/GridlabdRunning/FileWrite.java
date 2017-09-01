package GridlabdRunning;

/**
 * Created by zzt_cc on 2016/6/28.
 */

import Definition.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileWrite {
    public void writeDefaultSettings(String fileDirectory){
        File file = new File(fileDirectory);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file,true);
            writer = new BufferedWriter(fw);
            writer.write("#set iteration_limit=100000;\n" +
                    "\n" +
                    "clock {\n" +
                    "\ttimezone EST+5EDT;\n" +
                    "\tstarttime '2000-01-01 0:00:00';\n" +
                    "\tstoptime '2000-01-01 0:00:01';\n" +
                    "}\n" +
                    "\n" +
                    "module powerflow {\n" +
                    "\tsolver_method NR;\n" +
//                    "\tline_capacitance true;\n" +
                    "\t}\n" +
                    "module assert;\n\n");
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToGlm(HashMap<String,HashMap<String, String>> hashMapForWrite,String className,String fileDirectory){
        Iterator iter = hashMapForWrite.entrySet().iterator();
        File file = new File(fileDirectory);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file,true);
            writer = new BufferedWriter(fw);
            while(iter.hasNext()){
                writer.write("object "+className+" {"+"\n");
                Map.Entry entry = (Map.Entry) iter.next();
                writer.write("\tname "+entry.getKey().toString()+";\n");
                Iterator iterTemp=hashMapForWrite.get(entry.getKey()).entrySet().iterator();
                while(iterTemp.hasNext()){
                    Map.Entry entryTemp = (Map.Entry) iterTemp.next();
                    writer.write("\t"+entryTemp.getKey().toString()+" "+entryTemp.getValue().toString()+";\n");
                }
                writer.write("}\n\n");
            }

            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToTxt(String str,String fileDirectory){
        File file = new File(fileDirectory);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file,true);
            writer = new BufferedWriter(fw);
            writer.write(str);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WriteAll(String fileDirectory){
//        overhead_line_conductor.addMap("overhead_line_conductor100","0.0244","0.306");
//        overhead_line_conductor.addMap("overhead_line_conductor101","0.00814","0.592");
//        line_spacing.addMap("line_spacing200","2.5","4.5","7.0","5.656854","4.272002","5.0");
//        line_configuration.addMap("line_configuration300","overhead_line_conductor100","overhead_line_conductor100","overhead_line_conductor100","overhead_line_conductor101","line_spacing200");
//        transformer_configuration.addMap("transformer_configuration400","1","0","6000","12470","4160","0.01","0.06");
//        node.addMap("node1","\"ABCN\"","7200","7199.558+0.000j","-3599.779-6235.0000j","-3599.779+6235.000j","SWING");
//        overhead_line.addMap("overhead_line12","\"ABCN\"","node1","node2","2000","line_configuration300");
//        node.addMap("node2","\"ABCN\"","7200","7199.558+0.000j","-3599.779-6235.0000j","-3599.779+6235.000j","PQ");
//        transformer.addMap("transformer23","\"ABCN\"","node2","node3","transformer_configuration400");
//        node.addMap("node3","\"ABCN\"","2400","2401.777+0.000j","-1200.889-2080.000j","-1200.889+2080.000j","PQ");
//        overhead_line.addMap("overhead_line34","\"ABCN\"","node3","load4","2500","line_configuration300");
//        load.addMap("load4","\"ABCN\"","2400","2401.777+0.000j","-1200.889-2080.000j","-1200.889+2080.000j","1800000.000+871779.789j","1800000.000+871779.789j","1800000.000+871779.789j");
        FileWrite test=new FileWrite();
        test.writeDefaultSettings(fileDirectory);
        test.writeToGlm(line_spacing.line_spacingMap,"line_spacing",fileDirectory);
        test.writeToGlm(line_configuration.line_configurationMap,"line_configuration",fileDirectory);
        test.writeToGlm(node.nodeMap,"node",fileDirectory);
        test.writeToGlm(overhead_line.overhead_lineMap,"overhead_line",fileDirectory);
        test.writeToGlm(overhead_line_conductor.overhead_line_conductorMap,"overhead_line_conductor",fileDirectory);
        test.writeToGlm(transformer.transformerMap,"transformer",fileDirectory);
        test.writeToGlm(transformer_configuration.transformer_configurationMap,"transformer_configuration",fileDirectory);
        test.writeToGlm(underground_line.underground_lineMap,"underground_line",fileDirectory);
        test.writeToGlm(underground_line_conductor.underground_line_conductorMap,"underground_line_conductor",fileDirectory);
        test.writeToGlm(load.loadMap,"load",fileDirectory);
    }
}
