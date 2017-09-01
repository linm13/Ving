package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/27.
 */
public class node {
    public static HashMap<String,HashMap<String, String>> nodeMap = new HashMap<String,HashMap<String, String>>();
    public static HashMap<String,HashMap<String, String>> nodeResultMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String phases, String nominal_voltage, String voltage_A,String voltage_B,String voltage_C,String bustype) {
        HashMap<String, String> tempMap = new HashMap<>();
        //bustype="PQ";
        tempMap.put("phases",phases);
        tempMap.put("nominal_voltage",nominal_voltage);
        tempMap.put("voltage_A",voltage_A);
        tempMap.put("voltage_B",voltage_B);
        tempMap.put("voltage_C",voltage_C);
        tempMap.put("bustype",bustype);
        nodeMap.put(name,tempMap);
    }
    public static void addResultMap(String name, String phases, String nominal_voltage, String voltage_A,String voltage_B,String voltage_C,String bustype) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("nominal_voltage",nominal_voltage);
        tempMap.put("voltage_A",voltage_A);
        tempMap.put("voltage_B",voltage_B);
        tempMap.put("voltage_C",voltage_C);
        tempMap.put("bustype",bustype);
        nodeResultMap.put(name,tempMap);
    }
}
