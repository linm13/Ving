package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class transformer {
    public static HashMap<String,HashMap<String, String>> transformerMap = new HashMap<String,HashMap<String, String>>();
    public static HashMap<String,HashMap<String, String>> transformerResultMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String phases, String from, String to,String configuration) {
        HashMap<String, String> tempMap = new HashMap<>();
//        name xfrmr_709_775;
//        phases "ABC";
//        from node_709;
//        to node_775;
//        configuration xfrmr_config_400;
        tempMap.put("phases",phases);
        tempMap.put("from",from);
        tempMap.put("to",to);
        tempMap.put("configuration",configuration);
        transformerMap.put(name,tempMap);
    }
    public static void addResultMap(String name, String phases, String from,String to,String configuration,String power_in,String power_out,String power_losses) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("phases",phases);
        tempMap.put("from",from);
        tempMap.put("to",to);
        tempMap.put("configuration",configuration);
        tempMap.put("power_in",power_in);
        tempMap.put("power_out",power_out);
        tempMap.put("power_losses",power_losses);
        transformerResultMap.put(name,tempMap);
    }
}
