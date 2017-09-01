package ResultProcessing;

import java.math.BigDecimal;

/**
 * Created by zzt_cc on 2016/6/30.
 */
public class StringToNum {
	public static double getVoltage(String nominal_voltage) {
		nominal_voltage = nominal_voltage.replaceAll("[+]", " +");
		nominal_voltage = nominal_voltage.replaceAll("[-]", " -");
		nominal_voltage = nominal_voltage.replaceAll("e ", "e");
		String[] items = nominal_voltage.split(" ");
		String t = items[1];
		BigDecimal b = new BigDecimal(t);
		return Double.parseDouble(b.toPlainString());
	}
	

	public static double getVoltage(String[] voltages) {
		double result = 0;
		for (int i = 0; i < voltages.length; i++) {
			voltages[i] = voltages[i].replaceAll("[+]", " +");
			voltages[i] = voltages[i].replaceAll("[-]", " -");
			voltages[i] = voltages[i].replaceAll("e ", "e");
			String[] items = voltages[i].split(" ");
			String t = items[1];
			BigDecimal b = new BigDecimal(t);
			result += Double.parseDouble(b.toPlainString()) / 3.0;
		}
		
		return result;
	}

	public static double getpower_losses(String power_losses) {
		power_losses = power_losses.replaceAll("[+]", " +");
		power_losses = power_losses.replaceAll("[-]", " -");
		power_losses = power_losses.replaceAll("e ", "e");
		String[] items = power_losses.split(" ");
		String t = items[1];
		BigDecimal b = new BigDecimal(t);
		return Double.parseDouble(b.toPlainString());
	}

	
	public static String getpower(String power, boolean inGraph) {
		power = power.replaceAll("[+]", " +");
		power = power.replaceAll("[-]", " -");
		power = power.replaceAll("e ", "e");
		String[] items = power.split(" ");
		String t1 = items[1];
		String t2 = items[2].split("i")[0];
		BigDecimal b1 = new BigDecimal(t1);
		BigDecimal b2 = new BigDecimal(t2);
		double a1 = b1.doubleValue() / 1000000.0;
		double a2 = b2.doubleValue() / 1000000.0;

		a1 = new BigDecimal(a1).setScale(3, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		a2 = new BigDecimal(a2).setScale(3, BigDecimal.ROUND_HALF_UP)
				.doubleValue();

		if(inGraph) return a1 + "+" + a2 + "j";
		else return a1 + "+" + a2 + "j"+" MVA";
	}
	
	
	public static String getLoadPower(String power[]) {
		double result1 = 0;
		double result2 = 0;
		for (int i = 0; i < power.length; i++) {
			power[i] = power[i].replaceAll("[+]", " +");
			power[i] = power[i].replaceAll("[-]", " -");
			power[i] = power[i].replaceAll("e ", "e");
			String[] items = power[i].split(" ");
			String t1 = items[1];
			String t2 = items[2].split("j")[0];
			BigDecimal b1 = new BigDecimal(t1);
			BigDecimal b2 = new BigDecimal(t2);
			double a1 = b1.doubleValue() / 1000000.0;
			double a2 = b2.doubleValue() / 1000000.0;

			a1 = new BigDecimal(a1).setScale(3, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			a2 = new BigDecimal(a2).setScale(3, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			result1=result1+a1;
			result2=result2+a2;
		}
		
		return result1+"+"+result2+"i";
	}
	
	
	public static String getVoltageWithDegree(String vol,boolean inGraph) {
		vol = vol.replaceAll("[+]", " +");
		vol = vol.replaceAll("[-]", " -");
		vol = vol.replaceAll("e ", "e");
		String[] items = vol.split(" ");
		String t = items[1];
		BigDecimal b = new BigDecimal(t);
		double a = b.doubleValue() / 1000.0;
		a=new BigDecimal(a).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		if(inGraph)
		return a+items[2];
		else return a+items[2]+" kV";
	}
	
	public static String getVoltage2(String vol[]) {
		double result = 0;
		for (int i = 0; i < vol.length; i++) {
			vol[i] = vol[i].replaceAll("[+]", " +");
			vol[i] = vol[i].replaceAll("[-]", " -");
			vol[i] = vol[i].replaceAll("e ", "e");
			String[] items = vol[i].split(" ");
			String t = items[1];
			BigDecimal b = new BigDecimal(t);
			double a = b.doubleValue() / 1000.0;
			result = result + a / 3.0;
		}
		result = new BigDecimal(result).setScale(3, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		return "" + result;
	}
}
