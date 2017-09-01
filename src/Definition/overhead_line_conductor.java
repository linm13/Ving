package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class overhead_line_conductor {
    public static HashMap<String,HashMap<String, String>> overhead_line_conductorMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String geometric_mean_radius, String resistance) {
        HashMap<String, String> tempMap = new HashMap<>();
//        name=overhead_line_conductor_100;
//        geometric_mean_radius=.00446;
//        resistance=1.12;
        tempMap.put("geometric_mean_radius",geometric_mean_radius);
        tempMap.put("resistance",resistance);
        overhead_line_conductorMap.put(name,tempMap);
    }
}
