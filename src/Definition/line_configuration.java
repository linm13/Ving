package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/27.
 */
public class line_configuration {
    public static HashMap<String,HashMap<String, String>> line_configurationMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String conductor_A, String conductor_B, String conductor_C,String conductor_N,String spacing) {
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("conductor_A",conductor_A);
        tempMap.put("conductor_B",conductor_B);
        tempMap.put("conductor_C",conductor_C);
        tempMap.put("conductor_N",conductor_N);
        tempMap.put("spacing",spacing);
        line_configurationMap.put(name,tempMap);
    }
}
