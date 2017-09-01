package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class load {
    public static HashMap<String,HashMap<String, String>> loadMap = new HashMap<String,HashMap<String, String>>();
    public static HashMap<String,HashMap<String, String>> loadResultMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String phases, String nominal_voltage, String voltage_A,String voltage_B,String voltage_C,String constant_power_A,String constant_power_B,String constant_power_C) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("nominal_voltage",nominal_voltage);
        tempMap.put("voltage_A",voltage_A);
        tempMap.put("voltage_B",voltage_B);
        tempMap.put("voltage_C",voltage_C);
        tempMap.put("constant_power_A",constant_power_A);
        tempMap.put("constant_power_B",constant_power_B);
        tempMap.put("constant_power_C",constant_power_C);
        loadMap.put(name,tempMap);
    }
    public static void addResultMap(String name, String phases, String nominal_voltage, String voltage_A,String voltage_B,String voltage_C,String constant_power_A,String constant_power_B,String constant_power_C) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("nominal_voltage",nominal_voltage);
        tempMap.put("voltage_A",voltage_A);
        tempMap.put("voltage_B",voltage_B);
        tempMap.put("voltage_C",voltage_C);
        tempMap.put("constant_power_A",constant_power_A);
        tempMap.put("constant_power_B",constant_power_B);
        tempMap.put("constant_power_C",constant_power_C);
        loadResultMap.put(name,tempMap);
    }
}
