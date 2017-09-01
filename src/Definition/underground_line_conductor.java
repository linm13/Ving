package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class underground_line_conductor {
    public static HashMap<String,HashMap<String, String>> underground_line_conductorMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String outer_diameter , String conductor_gmr,String conductor_diameter,
                                                 String conductor_resistance,String neutral_gmr, String neutral_resistance,String neutral_diameter,
                                                 String neutral_strands,String shield_gmr,String shield_resistance) {
        HashMap<String, String> tempMap = new HashMap<>();
//        name ug_conduct_7210;
//        outer_diameter 1.980000;
//        conductor_gmr 0.036800;
//        conductor_diameter 1.150000;
//        conductor_resistance 0.105000;
//        neutral_gmr 0.003310;
//        neutral_resistance 5.903000;
//        neutral_diameter 0.102000;
//        neutral_strands 20.000000;
//        shield_gmr 0.000000;
//        shield_resistance 0.000000;
        tempMap.put("outer_diameter",outer_diameter);
        tempMap.put("conductor_gmr",conductor_gmr);
        tempMap.put("conductor_diameter",conductor_diameter);
        tempMap.put("conductor_resistance",conductor_resistance);
        tempMap.put("neutral_gmr",neutral_gmr);
        tempMap.put("neutral_resistance",neutral_resistance);
        tempMap.put("neutral_diameter",neutral_diameter);
        tempMap.put("neutral_strands",neutral_strands);
        tempMap.put("shield_gmr",shield_gmr);
        tempMap.put("shield_resistance",shield_resistance);
        underground_line_conductorMap.put(name,tempMap);
    }
}
