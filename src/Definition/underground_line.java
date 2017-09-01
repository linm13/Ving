package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/27.
 */
public class underground_line {
    public static HashMap<String,HashMap<String, String>> underground_lineMap = new HashMap<String,HashMap<String, String>>();
    public static HashMap<String,HashMap<String, String>> underground_lineResultMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String phases, String from,String to,String length,String configuration) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("from",from);
        tempMap.put("to",to);
        tempMap.put("length",length);
        tempMap.put("configuration",configuration);
        underground_lineMap.put(name,tempMap);
    }
    public static void addResultMap(String name, String phases, String from,String to,String length,String configuration,String power_in,String power_out,String power_losses) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("from",from);
        tempMap.put("to",to);
        tempMap.put("length",length);
        tempMap.put("configuration",configuration);
        tempMap.put("power_in",power_in);
        tempMap.put("power_out",power_out);
        tempMap.put("power_losses",power_losses);
        underground_lineResultMap.put(name,tempMap);
    }
}
