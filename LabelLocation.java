package gridlabd;

//import gridlabd.SetCase;

import ResultExhibition.ColouredTopo;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Definition.overhead_line;
import ResultProcessing.StringToNum;

public class LabelLocation {

	public static Point getLocation(String name) {

		String fromName, toName, lineName;
		double length = 40;
		int nodeOffsetX = 12, nodeOffsetY = 20;

		Iterator iter = ColouredTopo.overhead_linePoint.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			lineName = (String) entry.getKey();

			fromName = overhead_line.overhead_lineMap.get(lineName).get("from");
			toName = overhead_line.overhead_lineMap.get(lineName).get("to");

			if (ColouredTopo.overhead_linePoint.get(lineName)[1].x
					- ColouredTopo.overhead_linePoint.get(lineName)[0].x != 0) {
				double k = (double) (ColouredTopo.overhead_linePoint
						.get(lineName)[1].y - ColouredTopo.overhead_linePoint
						.get(lineName)[0].y)
						/ (double) (ColouredTopo.overhead_linePoint
								.get(lineName)[1].x - ColouredTopo.overhead_linePoint
								.get(lineName)[0].x);
				boolean isPositive = ColouredTopo.overhead_linePoint
						.get(lineName)[1].x
						- ColouredTopo.overhead_linePoint.get(lineName)[0].x > 0 ? true
						: false;
				length = 0.25 * Math
						.sqrt((double) (ColouredTopo.overhead_linePoint
								.get(lineName)[1].y - ColouredTopo.overhead_linePoint
								.get(lineName)[0].y)
								* (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].y - ColouredTopo.overhead_linePoint
										.get(lineName)[0].y)
								+ (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].x - ColouredTopo.overhead_linePoint
										.get(lineName)[0].x)
								* (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].x - ColouredTopo.overhead_linePoint
										.get(lineName)[0].x));
				double offsetXFrom = 0.5 * length / Math.sqrt(1 + k * k);
				double offsetYFrom = 0.5 * length / Math.sqrt(1 + k * k) * k;
				// - (isPositive ? 10 : -10);
				double offsetXTo = 1.5 * length / Math.sqrt(1 + k * k)
						+ (isPositive ? 17 : -17);
				double offsetYTo = 0.8 * length / Math.sqrt(1 + k * k) * k;

				allPoint.put(lineName + "From", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x
								+ (isPositive ? (int) offsetXFrom
										: -(int) offsetXFrom),
						ColouredTopo.overhead_linePoint.get(lineName)[0].y
								+ (isPositive ? (int) offsetYFrom
										: -(int) offsetYFrom)));

				allPoint.put(lineName + "To", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[1].x
								- (isPositive ? (int) offsetXTo
										: -(int) offsetXTo),
						ColouredTopo.overhead_linePoint.get(lineName)[1].y
								- (isPositive ? (int) offsetYTo
										: -(int) offsetYTo)));

				allPoint.put(fromName, new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[0].y
								- nodeOffsetY));

				allPoint.put(toName, new Point(ColouredTopo.overhead_linePoint
						.get(lineName)[1].x
						- nodeOffsetX, ColouredTopo.overhead_linePoint
						.get(lineName)[1].y
						- nodeOffsetY));

				allPoint.put(fromName + "name", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[0].y
								+ nodeOffsetY / 8));

				allPoint.put(toName + "name", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[1].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[1].y
								+ nodeOffsetY / 8));

				allPoint
						.put(
								lineName + "name",
								new Point(
										(ColouredTopo.overhead_linePoint
												.get(lineName)[0].x + ColouredTopo.overhead_linePoint
												.get(lineName)[1].x) / 2,
										(ColouredTopo.overhead_linePoint
												.get(lineName)[0].y + ColouredTopo.overhead_linePoint
												.get(lineName)[1].y) / 2));

			} else {
				length = 0.25 * Math
						.sqrt((double) (ColouredTopo.overhead_linePoint
								.get(lineName)[1].y - ColouredTopo.overhead_linePoint
								.get(lineName)[0].y)
								* (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].y - ColouredTopo.overhead_linePoint
										.get(lineName)[0].y)
								+ (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].x - ColouredTopo.overhead_linePoint
										.get(lineName)[0].x)
								* (double) (ColouredTopo.overhead_linePoint
										.get(lineName)[1].x - ColouredTopo.overhead_linePoint
										.get(lineName)[0].x));

				length = ColouredTopo.overhead_linePoint.get(lineName)[1].y
						- ColouredTopo.overhead_linePoint.get(lineName)[0].y > 0 ? -length
						: length;

				allPoint.put(lineName + "From", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x-30,
						ColouredTopo.overhead_linePoint.get(lineName)[0].y
								- (int) length));

				allPoint.put(lineName + "To", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[1].x-30,
						ColouredTopo.overhead_linePoint.get(lineName)[1].y
								+ (int) length));

				allPoint.put(fromName, new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[0].y
								- nodeOffsetY));

				allPoint.put(toName, new Point(ColouredTopo.overhead_linePoint
						.get(lineName)[1].x
						- nodeOffsetX, ColouredTopo.overhead_linePoint
						.get(lineName)[1].y
						- nodeOffsetY));

				allPoint.put(fromName + "name", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[0].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[0].y
								+ nodeOffsetY / 8));

				allPoint.put(toName + "name", new Point(
						ColouredTopo.overhead_linePoint.get(lineName)[1].x
								- nodeOffsetX, ColouredTopo.overhead_linePoint
								.get(lineName)[1].y
								+ nodeOffsetY / 8));

				allPoint
						.put(
								lineName + "name",
								new Point(
										(ColouredTopo.overhead_linePoint
												.get(lineName)[0].x + ColouredTopo.overhead_linePoint
												.get(lineName)[1].x) / 2,
										(ColouredTopo.overhead_linePoint
												.get(lineName)[0].y + ColouredTopo.overhead_linePoint
												.get(lineName)[1].y) / 2));

			}
		}

		return allPoint.get(name);

	}

	private static HashMap<String, Point> allPoint = new HashMap<String, Point>();

}
