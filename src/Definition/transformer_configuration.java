package Definition;

import java.util.HashMap;

/**
 * Created by zzt_cc on 2016/6/28.
 */
public class transformer_configuration {
    public static HashMap<String,HashMap<String, String>> transformer_configurationMap = new HashMap<String,HashMap<String, String>>();
    public static void addMap(String name, String connect_type, String install_type , String power_rating,String primary_voltage,String secondary_voltage,String resistance,String reactance) {
//        name xfrm_config_400;
//        connect_type DELTA_DELTA;
//        install_type PADMOUNT;
//        power_rating 500;
//        primary_voltage 4800;
//        secondary_voltage 480;
//        resistance 0.09;
//        reactance 1.81;
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("connect_type",connect_type);
        tempMap.put("install_type",install_type);
        tempMap.put("power_rating",power_rating);
        tempMap.put("primary_voltage", primary_voltage);
        tempMap.put("secondary_voltage", secondary_voltage);
        tempMap.put("resistance", resistance);
        tempMap.put("reactance",reactance);
        transformer_configurationMap.put(name,tempMap);
    }
}
