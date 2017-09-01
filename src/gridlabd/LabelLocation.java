package gridlabd;

import gridlabd.SetCase;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Definition.overhead_line;
import ResultProcessing.StringToNum;

public class LabelLocation {

	public static Point getLocation(String name) {

		String fromName, toName, lineName;
		double length=40;
		int nodeOffsetX=12,nodeOffsetY=20;
		
		
		Iterator iter = SetCase.overhead_linePoint.entrySet().iterator();		
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			lineName = (String) entry.getKey();

			fromName = overhead_line.overhead_lineMap.get(lineName).get("from");
			toName = overhead_line.overhead_lineMap.get(lineName).get("to");

			if (SetCase.overhead_linePoint.get(lineName)[1].x
					- SetCase.overhead_linePoint.get(lineName)[0].x != 0) {
				double k = (double) (SetCase.overhead_linePoint.get(lineName)[1].y - SetCase.overhead_linePoint
						.get(lineName)[0].y)
						/ (double) (SetCase.overhead_linePoint.get(lineName)[1].x - SetCase.overhead_linePoint
								.get(lineName)[0].x);
				double offsetXFrom=0.5*length/Math.sqrt(1+k*k);
				double offsetYFrom=0.5*length/Math.sqrt(1+k*k)*k-10;
				double offsetXTo=1.5*length/Math.sqrt(1+k*k)+17;
				double offsetYTo=0.8*length/Math.sqrt(1+k*k)*k;

				allPoint.put(lineName + "From", new Point(
						SetCase.overhead_linePoint.get(lineName)[0].x+(int)offsetXFrom,
						SetCase.overhead_linePoint.get(lineName)[0].y+(int)offsetYFrom));

				allPoint.put(lineName + "To", new Point(
						SetCase.overhead_linePoint.get(lineName)[1].x-(int)offsetXTo,
						SetCase.overhead_linePoint.get(lineName)[1].y-(int)offsetYTo));

				allPoint.put(fromName, new Point(SetCase.overhead_linePoint
						.get(lineName)[0].x-nodeOffsetX, SetCase.overhead_linePoint
						.get(lineName)[0].y-nodeOffsetY));

				allPoint.put(toName, new Point(SetCase.overhead_linePoint
						.get(lineName)[1].x-nodeOffsetX, SetCase.overhead_linePoint
						.get(lineName)[1].y-nodeOffsetY));

			}else{
				allPoint.put(lineName + "From", new Point(
						SetCase.overhead_linePoint.get(lineName)[0].x,
						SetCase.overhead_linePoint.get(lineName)[0].y-(int)length));

				allPoint.put(lineName + "To", new Point(
						SetCase.overhead_linePoint.get(lineName)[1].x,
						SetCase.overhead_linePoint.get(lineName)[1].y+(int)length));

				allPoint.put(fromName, new Point(SetCase.overhead_linePoint
						.get(lineName)[0].x-nodeOffsetX, SetCase.overhead_linePoint
						.get(lineName)[0].y-nodeOffsetY));

				allPoint.put(toName, new Point(SetCase.overhead_linePoint
						.get(lineName)[1].x-nodeOffsetX, SetCase.overhead_linePoint
						.get(lineName)[1].y-nodeOffsetY));
			}
		}

		return allPoint.get(name);

	}

	private static HashMap<String, Point> allPoint = new HashMap<String, Point>();

}
