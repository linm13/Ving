package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class line_spacing {
    public static HashMap<String,HashMap<String, String>> line_spacingMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String distance_AB, String distance_BC, String distance_AC, String distance_AN, String distance_BN, String distance_CN) {
        HashMap<String, String> tempMap = new HashMap<>();
//        name line_spacing_200;
//        distance_AB 2.5;
//        distance_BC 4.5;
//        distance_AC 7.0;
//        distance_AN 5.656854;
//        distance_BN 4.272002;
//        distance_CN 5.0;
        tempMap.put("distance_AB", distance_AB);
        tempMap.put("distance_BC", distance_BC);
        tempMap.put("distance_AC", distance_AC);
        tempMap.put("distance_AN", distance_AN);
        tempMap.put("distance_BN", distance_BN);
        tempMap.put("distance_CN", distance_CN);
        line_spacingMap.put(name, tempMap);
    }
}
