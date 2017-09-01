package ResultExhibition;

/**
 * Created by zzt_cc on 2016/6/29.
 */

import gridlabd.LabelLocation;

import Definition.*;
import GridlabdRunning.FileWrite;
import ResultProcessing.CopyFileUtil;
import ResultProcessing.StringToNum;
import gridlabd.SetCase;
import org.jfree.chart.ChartPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ResultProcessing.StringToNum;

public class ColouredTopo extends JFrame {

	public ColouredTopo(HashMap<String, Point> nodePoint,
			HashMap<String, Point> loadPointHash,
			HashMap<String, Point[]> overhead_linePoint,
			HashMap<String, Point[]> underground_linePoint,
			HashMap<String, Point[]> transformer_linePoint) {

		this.nodePoint = nodePoint;
		this.loadPointHash = loadPointHash;
		this.overhead_linePoint = overhead_linePoint;
		this.underground_linePoint = underground_linePoint;
		this.transformer_linePoint = transformer_linePoint;
		init();
		// setVisible(true);
	}

	private void init() {

		Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
		Image myimage = tool.getImage("src\\gridlabd\\Picture\\Ving.PNG"); // ��tool��ȡͼ��
		setIconImage(myimage);

		setCenter();
		setTitle("�����ֲ�");
		setSize(SetCase.frameSize.width, SetCase.frameSize.height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {

				SetCase.isRunned = false;

			}
		});

		setBackground(new Color(248, 248, 248));
		setLocationRelativeTo(null);
		busName = new Label("�ڵ����ƣ�");
		lineName = new Label("��·���ƣ�");
		busVol = new Label("�ڵ��ѹ��");
		linePower = new Label("��·���ʣ�");
		busName.setFont(new Font("����", Font.BOLD, 15));
		lineName.setFont(new Font("����", Font.BOLD, 15));
		busVol.setFont(new Font("����", Font.BOLD, 15));
		linePower.setFont(new Font("����", Font.BOLD, 15));

		showName = new JPanel();
		showName.setBackground(Color.white);
		draw = new Draw();
		draw.addMouseMotionListener(new getName());
		draw.setBackground(Color.white);
		draw.addMouseMotionListener(new getTempLocation());
		draw.addMouseWheelListener(new MouseDemo());
		// draw.add(showName);
		showName.setLayout(new GridLayout(4, 1));
		showName.add(busName);
		showName.add(busVol);
		showName.add(lineName);
		showName.add(linePower);

		buttonPanel = new JPanel();
		// buttonPanel.setLayout(new GridLayout(3,1));
		button1 = new JButton("��ʾ��������");
		JButton button2 = new JButton("��ʾ�������");
		JButton button3 = new JButton("���������");

		button4 = new JButton("��ʾ��ѹ����");

		button1.setFont(new Font("����", Font.BOLD, 15));
		button2.setFont(new Font("����", Font.BOLD, 15));
		button3.setFont(new Font("����", Font.BOLD, 15));
		button4.setFont(new Font("����", Font.BOLD, 15));

		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// �֝�
				addVolInfo();

			}
		});

		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// �֝�
				addFlowInfo();

			}
		});

		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Dimension screenSize = Toolkit.getDefaultToolkit()
						.getScreenSize();
				Dimension frameSize = getSize();
				JFrame frame = new JFrame("��·��ķֲ�");
				frame.setLayout(new GridLayout(1, 1));
				frame.add(new PieChart().getChartPanel()); // ��ӱ�״ͼ
				frame.setSize((int) (((double) frameSize.height)),
						(int) (((double) frameSize.height) * 0.8));
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});
		setLocationRelativeTo(null);
		buttonPanel.add(button1);
		buttonPanel.add(button4);

		buttonPanel.add(button2);
		buttonPanel.add(button3);

		textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(1, 1));

		text = new JTextArea("");
		text.setEditable(false);
		text.setBackground(new Color(220, 220, 220));
		text.setLineWrap(true);

		java.util.Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String sDateTime = sdf.format(dt);
		fileDirectory = sDateTime + ".txt";

		text.append("�ڵ��б�\r\n");
		for (int i = 1; i <= SetCase.orderOfComs[0]; i++) {
			text.append("  generator" + i + "��\r\n");
			Iterator iter = node.nodeResultMap.get("generator" + i).entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}
		for (int i = 1; i <= SetCase.orderOfComs[1]; i++) {
			text.append("  node" + i + "��\r\n");
			Iterator iter = node.nodeResultMap.get("node" + i).entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}
		for (int i = 1; i <= SetCase.orderOfComs[2]; i++) {
			text.append("  load" + i + "��\r\n");
			Iterator iter = load.loadResultMap.get("load" + i).entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}
		for (int i = 1; i <= SetCase.orderOfComs[3]; i++) {
			text.append("  transformer" + i + "_primary��\r\n");
			Iterator iter = node.nodeResultMap.get(
					"transformer" + i + "_primary").entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
			text.append("  transformer" + i + "_secondary��\r\n");
			iter = node.nodeResultMap.get("transformer" + i + "_secondary")
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}

		text.append("��ѹ���б�\r\n");
		for (int i = 1; i <= SetCase.orderOfComs[3]; i++) {
			text.append("  transformer" + i + "��\r\n");
			Iterator iter = transformer.transformerResultMap.get(
					"transformer" + i).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}

		text.append("��·�б�\r\n");
		for (int i = 1; i <= SetCase.orderOfComs[4]; i++) {
			text.append("  line" + i + "��\r\n");
			Iterator iter = overhead_line.overhead_lineResultMap
					.get("line" + i).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entryTemp = (Map.Entry) iter.next();
				text.append("      " + entryTemp.getKey().toString() + ":"
						+ entryTemp.getValue().toString() + "\r\n");
			}
			text.append("\r\n");
		}
		FileWrite.writeToTxt(text.getText(), fileDirectory);

		text.setFont(new Font("����", Font.PLAIN, 15));
		JScrollPane jsp = new JScrollPane(text);
		jsp
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		textPanel.add(jsp);
		jsp.getVerticalScrollBar().setValue(0);

		// textPanel.add(text);

		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1, 1));

		info = new JTextArea(
				"    Powered by GridLab-D\n GridLAB-D is a new power distribution system simulation and analysis tool that provides valuable information to users who design and operate distribution systems, and to utilities that wish to take advantage of the latest energy technologies. It incorporates the most advanced modeling techniques, with high-performance algorithms to deliver the best in end-use modeling. GridLAB-D? is coupled with distribution automation models and software integration tools for users of many power system analysis tools.");
		int fontSize = (int) (((double) SetCase.westPanelSize.height) * 0.02);
		info.setFont(new Font("����", Font.PLAIN, fontSize));
		info.setLineWrap(true);
		info.setEditable(false);
		info.setBackground(Color.white);
		infoPanel.add(info);
		infoPanel.setBackground(Color.white);

		draw.setLayout(null);
		setLayout(null);
		draw.setBackground(Color.white);

		setSize(SetCase.frameSize);
		setLayout(null);
		infoPanel.setBounds(0, 0, SetCase.westPanelSize.width,
				(int) (((double) SetCase.westPanelSize.height) * 0.6));
		buttonPanel.setBounds(0,
				(int) (((double) SetCase.westPanelSize.height) * 0.6),
				SetCase.westPanelSize.width,
				(int) (((double) SetCase.westPanelSize.height) * 0.4) - 5);
		draw.setBounds(SetCase.westPanelSize.width, 0, SetCase.graphSize.width,
				SetCase.graphSize.height);

		showName.setBounds(SetCase.westPanelSize.width,
				SetCase.graphSize.height,
				(int) (((double) SetCase.graphSize.width) * 0.6), 250);

		showName.setBorder(BorderFactory.createTitledBorder("��ϸ����"));

		textPanel.setBounds(SetCase.westPanelSize.width
				+ (int) (((double) SetCase.graphSize.width) * 0.6),
				SetCase.graphSize.height,
				(int) (((double) SetCase.graphSize.width) * 0.4), 250);

		add(infoPanel);
		add(textPanel);
		add(buttonPanel);
		add(draw);
		add(showName);
		draw.setBorder(BorderFactory.createTitledBorder("�����ֲ����"));
		buttonPanel.setBorder(BorderFactory.createTitledBorder("��ѡ����"));
		infoPanel.setBorder(BorderFactory.createTitledBorder("���������Ϣ"));
		textPanel.setBorder(BorderFactory.createTitledBorder("�����������ļ�Ԥ��"));

		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveTxtFile();
			}
		});

		JMenuBar menu = new JMenuBar();
		JMenu menu1 = new JMenu("�ļ�");
		// JMenuItem menu1Item1 = new JMenuItem("��");
		JMenuItem menu1Item2 = new JMenuItem("����");
		JMenuItem menu1Item3 = new JMenuItem("�ر�");
		// menu1.add(menu1Item1);
		menu1.add(menu1Item2);
		menu1.add(menu1Item3);

		// menu1Item1.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // TODO Auto-generated method stub
		// // openFile();
		// }
		//
		// });

		menu1Item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveTxtFile();
			}

		});

		menu1Item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}

		});

		JMenu menu2 = new JMenu("�༭");
		JMenuItem menu2Item1 = new JMenuItem("���");
		menu2.add(menu2Item1);

		menu2Item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

		JMenu menu3 = new JMenu("����");
		JMenuItem menu3Item1 = new JMenuItem("˵��");
		menu3Item1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new helpDialog();
			}

		});
		JMenuItem menu3Item2 = new JMenuItem("����");
		menu3Item2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new aboutDialog();
			}

		});
		menu3.add(menu3Item1);
		menu3.add(menu3Item2);

		menu.add(menu1);
		menu.add(menu2);
		menu.add(menu3);
		setJMenuBar(menu);
		setVisible(true);

	}

	private void addFlowInfo() {
		if (button1.getText().equals("��ʾ��������")) {
			String fromName, toName, lineName;
			JLabel flowFrom;
			JLabel flowTo;
			// JLabel[] nodeLabel = new JLabel[2 * SetCase.numOfComs[4]];
			Iterator iter = SetCase.overhead_linePoint.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				lineName = (String) entry.getKey();
				fromName = overhead_line.overhead_lineMap.get(lineName).get(
						"from");
				toName = overhead_line.overhead_lineMap.get(lineName).get("to");
				flowFrom = new JLabel();
				flowFrom.setText(StringToNum.getpower(
						overhead_line.overhead_lineResultMap.get(lineName).get(
								"power_in"), true));
				flowFrom.setBounds(
						LabelLocation.getLocation(lineName + "From").x,
						LabelLocation.getLocation(lineName + "From").y, 80, 20);
				draw.add(flowFrom);
				flowTo = new JLabel();
				flowTo.setText(StringToNum.getpower(
						overhead_line.overhead_lineResultMap.get(lineName).get(
								"power_out"), true));
				flowTo.setBounds(LabelLocation.getLocation(lineName + "To").x,
						LabelLocation.getLocation(lineName + "To").y, 80, 20);
				draw.add(flowTo);

			}
			iter = loadFlowMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				flowFrom = new JLabel();

				flowFrom.setText(ResultProcessing.StringToNum
						.getLoadPower(new String[] {
								load.loadResultMap.get(entry.getKey()).get(
										"constant_power_A"),
								load.loadResultMap.get(entry.getKey()).get(
										"constant_power_B"),
								load.loadResultMap.get(entry.getKey()).get(
										"constant_power_C") }));

				flowFrom.setBounds(
						loadFlowMap.get((String) entry.getKey()).x - 20,
						loadFlowMap.get((String) entry.getKey()).y, 80, 20);
				draw.add(flowFrom);
			}

			draw.add();
			button1.setText("���س�������");
		} else {
			draw.removeAll();
			draw.add();
			if (button4.getText().equals("���ص�ѹ����")) {
				button4.setText("��ʾ��ѹ����");
				addVolInfo();
			}

			button1.setText("��ʾ��������");

		}
	}

	private void addVolInfo() {
		if (button4.getText().equals("��ʾ��ѹ����")) {
			String fromName, toName, lineName;
			JLabel[] nodeLabel = new JLabel[2 * SetCase.numOfComs[4]];
			Iterator iter = SetCase.overhead_linePoint.entrySet().iterator();
			int i = 0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				lineName = (String) entry.getKey();
				fromName = overhead_line.overhead_lineMap.get(lineName).get(
						"from");
				toName = overhead_line.overhead_lineMap.get(lineName).get("to");
				nodeLabel[2 * i] = new JLabel();
				nodeLabel[2 * i + 1] = new JLabel();
				if (fromName.startsWith("generator")
						|| fromName.startsWith("node")
						|| fromName.startsWith("transformer")) {

					nodeLabel[2 * i].setText(ResultProcessing.StringToNum
							.getVoltage2(new String[] {
									node.nodeResultMap.get(fromName).get(
											"voltage_A"),
									node.nodeResultMap.get(fromName).get(
											"voltage_B"),
									node.nodeResultMap.get(fromName).get(
											"voltage_C") }));

					nodeLabel[2 * i].setBounds(LabelLocation
							.getLocation(fromName).x, LabelLocation
							.getLocation(fromName).y, 40, 20);

				} else {
					nodeLabel[2 * i].setText(ResultProcessing.StringToNum
							.getVoltage2(new String[] {
									load.loadResultMap.get(fromName).get(
											"voltage_A"),
									load.loadResultMap.get(fromName).get(
											"voltage_B"),
									load.loadResultMap.get(fromName).get(
											"voltage_C") }));

					nodeLabel[2 * i].setBounds(LabelLocation
							.getLocation(fromName).x, LabelLocation
							.getLocation(fromName).y, 40, 20);
				}
				draw.add(nodeLabel[2 * i]);
				if (toName.startsWith("generator") || toName.startsWith("node")
						|| toName.startsWith("transformer")) {

					nodeLabel[2 * i + 1].setText(ResultProcessing.StringToNum
							.getVoltage2(new String[] {
									node.nodeResultMap.get(toName).get(
											"voltage_A"),
									node.nodeResultMap.get(toName).get(
											"voltage_B"),
									node.nodeResultMap.get(toName).get(
											"voltage_C") }));

					nodeLabel[2 * i + 1].setBounds(LabelLocation
							.getLocation(toName).x, LabelLocation
							.getLocation(toName).y, 40, 20);

				} else {
					nodeLabel[2 * i + 1].setText(ResultProcessing.StringToNum
							.getVoltage2(new String[] {
									load.loadResultMap.get(toName).get(
											"voltage_A"),
									load.loadResultMap.get(toName).get(
											"voltage_B"),
									load.loadResultMap.get(toName).get(
											"voltage_C") }));

					nodeLabel[2 * i + 1].setBounds(LabelLocation
							.getLocation(toName).x, LabelLocation
							.getLocation(toName).y, 40, 20);
				}
				draw.add(nodeLabel[2 * i + 1]);
			}

			draw.add();
			button4.setText("���ص�ѹ����");
		} else {
			draw.removeAll();
			draw.add();
			button4.setText("��ʾ��ѹ����");
			if (button1.getText().equals("���س�������")) {
				button1.setText("��ʾ��������");
				addFlowInfo();
			}
		}
	}

	private double getPuVol(String name) {
		double temp = 0;
		if (name.startsWith("node") || name.startsWith("generator")
				|| name.startsWith("transformer")) {
			String[] voltages = {
					node.nodeResultMap.get(name).get("voltage_A"),
					node.nodeResultMap.get(name).get("voltage_B"),
					node.nodeResultMap.get(name).get("voltage_C") };
			String nominal_voltage = node.nodeResultMap.get(name).get(
					"nominal_voltage");
			temp = StringToNum.getVoltage(voltages)
					/ StringToNum.getVoltage(nominal_voltage);

		} else if (name.startsWith("load")) {
			String[] voltages = {
					load.loadResultMap.get(name).get("voltage_A"),
					load.loadResultMap.get(name).get("voltage_B"),
					load.loadResultMap.get(name).get("voltage_C") };
			String nominal_voltage = load.loadResultMap.get(name).get(
					"nominal_voltage");
			temp = StringToNum.getVoltage(voltages)
					/ StringToNum.getVoltage(nominal_voltage);
		}

		return temp;
	}

	private class aboutDialog extends JFrame {
		public aboutDialog() {

			Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
			Image myimage = tool.getImage("src\\gridlabd\\Picture\\Ving.PNG"); // ��tool��ȡͼ��
			setIconImage(myimage);

			setSize(500, 300);
			setLocationRelativeTo(null);
			setTitle("����");
			JTextArea aboutInfo = new JTextArea(10, 20);
			aboutInfo.setEditable(false);
			aboutInfo.setBackground(new Color(240, 240, 240));
			setVisible(true);
			add(aboutInfo);

			aboutInfo
					.append("�汾��1.0\n���ߣ��֝� ֣����\n��ϵ��ʽ��linm13@mails.tsinghua.edu.cn zhengzt13@mails.tsinghua.edu.cn");
		}
	}

	private class helpDialog extends JFrame {
		public helpDialog() {
			Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
			Image myimage = tool.getImage("src\\gridlabd\\Picture\\Ving.PNG"); // ��tool��ȡͼ��
			setIconImage(myimage);

			setSize(500, 300);
			setLocationRelativeTo(null);
			setTitle("������Ϣ");
			JTextArea helpInfo = new JTextArea(10, 20);
			helpInfo.setEditable(false);
			helpInfo.setBackground(new Color(240, 240, 240));
			setVisible(true);
			add(helpInfo);

			helpInfo.append("1.����Ƶ��ڵ����·�Ͽ���ʾ��Ϣ\n" + "2.������ѡ��·����д�ļ���");

		}
	}

	private class Draw extends JPanel {

		public void paint(Graphics g1) {
			super.paint(g1);
			

			Graphics2D g2 = (Graphics2D) g1;
			Graphics2D g = g2;
			g.setStroke(new BasicStroke(1.7f));
			getMaxMin();
			Iterator iter = node.nodeResultMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				// int x = nodePoint.get(entry.getKey()).x - diam / 2;
				// int y = nodePoint.get(entry.getKey()).y - diam / 2;

				String[] voltages = {
						node.nodeResultMap.get(entry.getKey()).get("voltage_A"),
						node.nodeResultMap.get(entry.getKey()).get("voltage_B"),
						node.nodeResultMap.get(entry.getKey()).get("voltage_C") };
				String nominal_voltage = node.nodeResultMap.get(entry.getKey())
						.get("nominal_voltage");
				double temp = StringToNum.getVoltage(voltages)
						/ StringToNum.getVoltage(nominal_voltage);
				int x = nodePoint.get(entry.getKey()).x - (int) (temp * diam)
						/ 2;
				int y = nodePoint.get(entry.getKey()).y - (int) (temp * diam)
						/ 2;
				g.setColor(getColor(temp));
				g.fillOval(x, y, (int) (temp * diam), (int) (temp * diam));
				// g.fillOval(x, y, diam, diam);
			}

			loadNum = 0;
			iter = load.loadResultMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				// int x = loadPointHash.get(entry.getKey()).x - diam / 2;
				// int y = loadPointHash.get(entry.getKey()).y - diam / 2;

				String[] voltages = {
						load.loadResultMap.get(entry.getKey()).get("voltage_A"),
						load.loadResultMap.get(entry.getKey()).get("voltage_B"),
						load.loadResultMap.get(entry.getKey()).get("voltage_C") };
				String nominal_voltage = load.loadResultMap.get(entry.getKey())
						.get("nominal_voltage");
				double temp = StringToNum.getVoltage(voltages)
						/ StringToNum.getVoltage(nominal_voltage);
				int x = loadPointHash.get(entry.getKey()).x
						- (int) (temp * diam) / 2;
				int y = loadPointHash.get(entry.getKey()).y
						- (int) (temp * diam) / 2;
				g.setColor(getColor(temp));
				g.fillOval(x, y, (int) (temp * diam), (int) (temp * diam));

				x1[loadNum] = 0;
				x1[loadNum] = loadPointHash.get(entry.getKey()).x;
				double tempx1 = (double) (x1[loadNum]);
				double tempxc = (double) (xc);
				y1[loadNum] = loadPointHash.get(entry.getKey()).y;
				double tempy1 = (double) (y1[loadNum]);
				double tempyc = (double) (yc);
				double sin = (tempy1 - tempyc)
						/ Math.sqrt((tempy1 - tempyc) * (tempy1 - tempyc)
								+ (tempx1 - tempxc) * (tempx1 - tempxc));
				double cos = (tempx1 - tempxc)
						/ Math.sqrt((tempy1 - tempyc) * (tempy1 - tempyc)
								+ (tempx1 - tempxc) * (tempx1 - tempxc));

				double tempx2 = tempx1 + length * cos;
				double tempy2 = tempy1 + length * sin;

				x2[loadNum] = (int) tempx2;
				y2[loadNum] = (int) tempy2;

				loadFlowMap.put((String) entry.getKey(), new Point(x2[loadNum],
						y2[loadNum]));
				loadNum++;
			}

			iter = overhead_line.overhead_lineResultMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int x1 = overhead_linePoint.get(entry.getKey())[0].x;
				int y1 = overhead_linePoint.get(entry.getKey())[0].y;
				int x2 = overhead_linePoint.get(entry.getKey())[1].x;
				int y2 = overhead_linePoint.get(entry.getKey())[1].y;

				if (x1 * x2 * y1 * y2 != 0) {

					String nodeFrom = overhead_line.overhead_lineMap.get(
							entry.getKey()).get("from");
					String nodeTo = overhead_line.overhead_lineMap.get(
							entry.getKey()).get("to");
					Color colorFrom = getColor(getPuVol(nodeFrom));
					Color colorTo = getColor(getPuVol(nodeTo));

					GradientPaint mask = new GradientPaint(x1, y1, colorFrom,
							x2, y2, colorTo);

					g2.setPaint(mask);
					g2.drawLine(x1, y1, x2, y2);
				}
			}
			iter = underground_line.underground_lineResultMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int x1 = underground_linePoint.get(entry.getKey())[0].x;
				int y1 = underground_linePoint.get(entry.getKey())[0].y;
				int x2 = underground_linePoint.get(entry.getKey())[1].x;
				int y2 = underground_linePoint.get(entry.getKey())[1].y;
				if (x1 * x2 * y1 * y2 != 0) {
					g.setColor(new Color(0, 100, 0));
					g.drawLine(x1, y1, x2, y2);
				}
			}
			iter = transformer.transformerResultMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int x1 = transformer_linePoint.get(entry.getKey())[0].x;
				int y1 = transformer_linePoint.get(entry.getKey())[0].y;
				int x2 = transformer_linePoint.get(entry.getKey())[1].x;
				int y2 = transformer_linePoint.get(entry.getKey())[1].y;
				if (x1 * x2 * y1 * y2 != 0) {
					String nodeFrom = transformer.transformerMap.get(
							entry.getKey()).get("from");
					String nodeTo = transformer.transformerMap.get(
							entry.getKey()).get("to");
					Color colorFrom = getColor(getPuVol(nodeFrom));
					Color colorTo = getColor(getPuVol(nodeTo));

					GradientPaint mask = new GradientPaint(x1, y1, colorFrom,
							x2, y2, colorTo);

					g2.setPaint(mask);
					g2.drawLine(x1, y1, x2, y2);
				}
			}

			g2.setStroke(new BasicStroke(1f));
			g2.setPaint(Color.black);
			for (int j = 0; j < loadNum; j++) {
				drawAL(x1[j], y1[j], x2[j], y2[j], g2);
			}

		}

		private void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2)

		{

			double H = 6; // ��ͷ�߶�
			double L = 6; // �ױߵ�һ��
			int x3 = 0;
			int y3 = 0;
			int x4 = 0;
			int y4 = 0;
			double awrad = Math.atan(L / H); // ��ͷ�Ƕ�
			double arraow_len = Math.sqrt(L * L + H * H); // ��ͷ�ĳ���
			double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true,
					arraow_len);
			double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true,
					arraow_len);
			double x_3 = ex - arrXY_1[0]; // (x3,y3)�ǵ�һ�˵�
			double y_3 = ey - arrXY_1[1];
			double x_4 = ex - arrXY_2[0]; // (x4,y4)�ǵڶ��˵�
			double y_4 = ey - arrXY_2[1];

			Double X3 = new Double(x_3);
			x3 = X3.intValue();
			Double Y3 = new Double(y_3);
			y3 = Y3.intValue();
			Double X4 = new Double(x_4);
			x4 = X4.intValue();
			Double Y4 = new Double(y_4);
			y4 = Y4.intValue();
			// g.setColor(SWT.COLOR_WHITE);
			// ����
			g2.drawLine(sx, sy, ex, ey);
			// ����ͷ��һ��
			g2.drawLine(ex, ey, x3, y3);
			// ����ͷ����һ��
			g2.drawLine(ex, ey, x4, y4);

		}

		// ����
		private double[] rotateVec(int px, int py, double ang, boolean isChLen,
				double newLen) {

			double mathstr[] = new double[2];
			// ʸ����ת��������������ֱ���x������y��������ת�ǡ��Ƿ�ı䳤�ȡ��³���
			double vx = px * Math.cos(ang) - py * Math.sin(ang);
			double vy = px * Math.sin(ang) + py * Math.cos(ang);
			if (isChLen) {
				double d = Math.sqrt(vx * vx + vy * vy);
				vx = vx / d * newLen;
				vy = vy / d * newLen;
				mathstr[0] = vx;
				mathstr[1] = vy;
			}
			return mathstr;
		}

		public void add() {

			repaint();
		}

		int loadNum = 0;
		int[] x1 = new int[1000];
		int[] y1 = new int[1000];
		int[] x2 = new int[1000];
		int[] y2 = new int[1000];
	}

	private void getMaxMin() {
		maxVoltage = 1;
		minVoltage = 1;
		Iterator iter = node.nodeResultMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String[] voltages = {
					node.nodeResultMap.get(entry.getKey()).get("voltage_A"),
					node.nodeResultMap.get(entry.getKey()).get("voltage_B"),
					node.nodeResultMap.get(entry.getKey()).get("voltage_C") };
			String nominal_voltage = node.nodeResultMap.get(entry.getKey())
					.get("nominal_voltage");
			double temp = StringToNum.getVoltage(voltages)
					/ StringToNum.getVoltage(nominal_voltage);
			if (temp > maxVoltage)
				maxVoltage = temp;
			if (temp < minVoltage)
				minVoltage = temp;
		}
		iter = load.loadResultMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String[] voltages = {
					load.loadResultMap.get(entry.getKey()).get("voltage_A"),
					load.loadResultMap.get(entry.getKey()).get("voltage_B"),
					load.loadResultMap.get(entry.getKey()).get("voltage_C") };
			String nominal_voltage = load.loadResultMap.get(entry.getKey())
					.get("nominal_voltage");
			double temp = StringToNum.getVoltage(voltages)
					/ StringToNum.getVoltage(nominal_voltage);
			if (temp > maxVoltage)
				maxVoltage = temp;
			if (temp < minVoltage)
				minVoltage = temp;
		}
	}

	private Color getColor(double vol) {
		double range = maxVoltage - minVoltage;
		int i = (int) ((vol - minVoltage) * 255 / range);
		i = i > 255 ? 255 : i;
		i = i < 0 ? 0 : i;
		return new Color(i, 0, 255 - i);
	}

	public class getName implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			Boolean isShowing = false;
			Iterator iter = node.nodeResultMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int x = nodePoint.get(entry.getKey()).x;
				int y = nodePoint.get(entry.getKey()).y;
				int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
						* (e.getY() - y);
				if (dis <= diam * diam / 4) {
					busName.setText("�ڵ����ƣ�" + entry.getKey());
					busVol.setText("�ڵ��ѹ��"
							+ "A�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(node.nodeResultMap
											.get(entry.getKey()).get(
													"voltage_A"), false)
							+ "  B�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(node.nodeResultMap
											.get(entry.getKey()).get(
													"voltage_B"), false)
							+ "  C�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(node.nodeResultMap
											.get(entry.getKey()).get(
													"voltage_C"), false));
					isShowing = true;
					break;
				}
				busName.setText("�ڵ����ƣ�");
				busVol.setText("�ڵ��ѹ��");
			}

			Iterator iter2 = load.loadResultMap.entrySet().iterator();
			while (iter2.hasNext()) {
				if (isShowing == true)
					break;
				Map.Entry entry2 = (Map.Entry) iter2.next();
				int x = loadPointHash.get(entry2.getKey()).x;
				int y = loadPointHash.get(entry2.getKey()).y;
				int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
						* (e.getY() - y);
				if (dis <= diam * diam / 4) {
					busName.setText("�ڵ����ƣ�" + entry2.getKey());
					busVol.setText("�ڵ��ѹ��"
							+ "A�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(load.loadResultMap
											.get(entry2.getKey()).get(
													"voltage_A"), false)
							+ "  B�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(load.loadResultMap
											.get(entry2.getKey()).get(
													"voltage_B"), false)
							+ "  C�ࣺ"
							+ ResultProcessing.StringToNum
									.getVoltageWithDegree(load.loadResultMap
											.get(entry2.getKey()).get(
													"voltage_C"), false));
					isShowing = true;
					break;
				}
				busName.setText("�ڵ����ƣ�");
				busVol.setText("�ڵ��ѹ��");
			}
			Iterator iter3 = overhead_line.overhead_lineResultMap.entrySet()
					.iterator();
			while (iter3.hasNext()) {
				if (isShowing == true)
					break;
				Map.Entry entry = (Map.Entry) iter3.next();
				int x1 = overhead_linePoint.get(entry.getKey())[0].x;
				int y1 = overhead_linePoint.get(entry.getKey())[0].y;
				int x2 = overhead_linePoint.get(entry.getKey())[1].x;
				int y2 = overhead_linePoint.get(entry.getKey())[1].y;
				if (x1 * x2 * y1 * y2 != 0) {
					if ((x1 - x2) == 0) {
						if (e.getY() > Math.min(y1, y2)
								&& e.getY() < Math.max(y1, y2)
								&& Math.abs(e.getX() - x1) < 10) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															overhead_line.overhead_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															overhead_line.overhead_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					} else {
						float dy = y1 - y2;
						float dx = x1 - x2;
						float dx0 = e.getX() - x1;
						int y0 = (int) (dx0 * dy / dx) + y1;
						if (Math.abs(y0 - e.getY()) < 10
								&& e.getX() > Math.min(x1, x2)
								&& e.getX() < Math.max(x1, x2)) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															overhead_line.overhead_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															overhead_line.overhead_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					}

					lineName.setText("��·���ƣ�");
					linePower.setText("��·���ʣ�");

				}
			}
			Iterator iter4 = underground_line.underground_lineResultMap
					.entrySet().iterator();
			while (iter4.hasNext()) {
				if (isShowing == true)
					break;
				Map.Entry entry = (Map.Entry) iter4.next();
				int x1 = underground_linePoint.get(entry.getKey())[0].x;
				int y1 = underground_linePoint.get(entry.getKey())[0].y;
				int x2 = underground_linePoint.get(entry.getKey())[1].x;
				int y2 = underground_linePoint.get(entry.getKey())[1].y;
				if (x1 * x2 * y1 * y2 != 0) {
					if ((x1 - x2) == 0) {
						if (e.getY() > Math.min(y1, y2)
								&& e.getY() < Math.max(y1, y2)
								&& Math.abs(e.getX() - x1) < 10) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															underground_line.underground_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															underground_line.underground_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					} else {
						float dy = y1 - y2;
						float dx = x1 - x2;
						float dx0 = e.getX() - x1;
						int y0 = (int) (dx0 * dy / dx) + y1;
						if (Math.abs(y0 - e.getY()) < 10
								&& e.getX() > Math.min(x1, x2)
								&& e.getX() < Math.max(x1, x2)) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															underground_line.underground_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															underground_line.underground_lineResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					}

					lineName.setText("��·���ƣ�");
					linePower.setText("��·���ʣ�");

				}
			}
			Iterator iter5 = transformer.transformerResultMap.entrySet()
					.iterator();
			while (iter5.hasNext()) {
				if (isShowing == true)
					break;
				Map.Entry entry = (Map.Entry) iter5.next();
				int x1 = transformer_linePoint.get(entry.getKey())[0].x;
				int y1 = transformer_linePoint.get(entry.getKey())[0].y;
				int x2 = transformer_linePoint.get(entry.getKey())[1].x;
				int y2 = transformer_linePoint.get(entry.getKey())[1].y;
				if (x1 * x2 * y1 * y2 != 0) {
					if ((x1 - x2) == 0) {
						if (e.getY() > Math.min(y1, y2)
								&& e.getY() < Math.max(y1, y2)
								&& Math.abs(e.getX() - x1) < 10) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															transformer.transformerResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															transformer.transformerResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					} else {
						float dy = y1 - y2;
						float dx = x1 - x2;
						float dx0 = e.getX() - x1;
						int y0 = (int) (dx0 * dy / dx) + y1;
						if (Math.abs(y0 - e.getY()) < 10
								&& e.getX() > Math.min(x1, x2)
								&& e.getX() < Math.max(x1, x2)) {
							lineName.setText("��·���ƣ�" + entry.getKey());
							linePower
									.setText("��·���ʣ�"
											+ "����"
											+ ResultProcessing.StringToNum
													.getpower(
															transformer.transformerResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_in"),
															false)
											+ "  ����"
											+ ResultProcessing.StringToNum
													.getpower(
															transformer.transformerResultMap
																	.get(
																			entry
																					.getKey())
																	.get(
																			"power_out"),
															false));
							isShowing = true;
							break;
						}
					}

					lineName.setText("��·���ƣ�");
					linePower.setText("��·���ʣ�");

				}
			}
		}
	}

	private void setCenter() {
		int count = 0;
		double allX = 0, allY = 0;
		Iterator iter = node.nodeMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			allX = allX + nodePoint.get(entry.getKey()).x;
			allY = allY + nodePoint.get(entry.getKey()).y;
			count++;
		}
		load.loadMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			allX = allX + loadPointHash.get(entry.getKey()).x;
			allY = allY + loadPointHash.get(entry.getKey()).y;
			count++;
		}
		xc = (int) (allX / ((double) count));
		yc = (int) (allY / ((double) count));

	}

	private void saveTxtFile() {
		JFileChooser saveJFC = new JFileChooser();
		saveJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		saveJFC.showDialog(this, "ȷ��");
		File path = saveJFC.getSelectedFile();

		if (!path.toString().isEmpty()) {
			String file = JOptionPane.showInputDialog("�������ļ���") + ".txt";
			if (CopyFileUtil.copyFile(fileDirectory, path + "\\" + file, true)) {
				JOptionPane.showMessageDialog(showName, "�ļ�����ɹ���");
			} else
				JOptionPane.showMessageDialog(showName, "�ļ�����ʧ�ܣ�");
		}
	}

	private class getTempLocation implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			tempLocation.x = e.getX();
			tempLocation.y = e.getY();
		}

	}

	private class MouseDemo implements MouseWheelListener {

		public void mouseClicked(MouseEvent e) {

		}

		@Override
		// ���ù����¼������ڷŴ����С
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() == 1) {
				sizeLevel = 0.9;
			}
			if (e.getWheelRotation() == -1) {
				sizeLevel = 1.1;
			}
			newGraph();
		}
	}

	private void newGraph() {

		length*=sizeLevel;
		Iterator iter = nodePoint.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int tempx = nodePoint.get(entry.getKey()).x, tempy = nodePoint
					.get(entry.getKey()).y;
			tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
					* sizeLevel);
			tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
					* sizeLevel);
			nodePoint.put(entry.getKey().toString(), new Point(tempx, tempy));
		}

		iter = loadPointHash.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int tempx = loadPointHash.get(entry.getKey()).x, tempy = loadPointHash
					.get(entry.getKey()).y;
			tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
					* sizeLevel);
			tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
					* sizeLevel);
			loadPointHash.put(entry.getKey().toString(),
					new Point(tempx, tempy));
		}
		
		iter = loadFlowMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int tempx = loadFlowMap.get(entry.getKey()).x, tempy = loadFlowMap
					.get(entry.getKey()).y;
			tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
					* sizeLevel);
			tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
					* sizeLevel);
			loadFlowMap.put(entry.getKey().toString(),
					new Point(tempx, tempy));
		}

		iter = transformer_linePoint.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int tempx1 = transformer_linePoint.get(entry.getKey())[0].x, tempy1 = transformer_linePoint
					.get(entry.getKey())[0].y, tempx2 = transformer_linePoint
					.get(entry.getKey())[1].x, tempy2 = transformer_linePoint
					.get(entry.getKey())[1].y;

			tempx1 = (int) ((double) (tempLocation.x) + (double) (tempx1 - tempLocation.x)
					* sizeLevel);
			tempy1 = (int) ((double) (tempLocation.y) + (double) (tempy1 - tempLocation.y)
					* sizeLevel);
			tempx2 = (int) ((double) (tempLocation.x) + (double) (tempx2 - tempLocation.x)
					* sizeLevel);
			tempy2 = (int) ((double) (tempLocation.y) + (double) (tempy2 - tempLocation.y)
					* sizeLevel);
			transformer_linePoint.put(entry.getKey().toString(), new Point[] {
					new Point(tempx1, tempy1), new Point(tempx2, tempy2) });
		}

		iter = overhead_linePoint.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			int tempx1 = overhead_linePoint.get(entry.getKey())[0].x, tempy1 = overhead_linePoint
					.get(entry.getKey())[0].y, tempx2 = overhead_linePoint
					.get(entry.getKey())[1].x, tempy2 = overhead_linePoint
					.get(entry.getKey())[1].y;

			tempx1 = (int) ((double) (tempLocation.x) + (double) (tempx1 - tempLocation.x)
					* sizeLevel);
			tempy1 = (int) ((double) (tempLocation.y) + (double) (tempy1 - tempLocation.y)
					* sizeLevel);
			tempx2 = (int) ((double) (tempLocation.x) + (double) (tempx2 - tempLocation.x)
					* sizeLevel);
			tempy2 = (int) ((double) (tempLocation.y) + (double) (tempy2 - tempLocation.y)
					* sizeLevel);
			overhead_linePoint.put(entry.getKey().toString(), new Point[] {
					new Point(tempx1, tempy1), new Point(tempx2, tempy2) });
		}
		
		setCenter();

		addFlowInfo();
		addVolInfo();

		addFlowInfo();
		addVolInfo();
		
		draw.repaint();
	}

	private String fileDirectory;
	private double maxVoltage;
	private double minVoltage;
	private static HashMap<String, Point> nodePoint;
	private static HashMap<String, Point> loadPointHash;
	private static HashMap<String, Point[]> overhead_linePoint;
	private static HashMap<String, Point[]> underground_linePoint;
	private static HashMap<String, Point[]> transformer_linePoint;
	private Draw draw;
	private JPanel showName;
	private JPanel buttonPanel;
	private JPanel infoPanel;
	private JPanel textPanel;

	private Label busName;
	private Label lineName;
	private Label busVol;
	private Label linePower;

	private JTextArea text;
	private JTextArea info;

	private int diam = 12;
	private int xSize = 1270;
	private int ySize = 830;
	JButton button1, button4;
	int xc, yc;

	private double sizeLevel = 1;

	private Point tempLocation = new Point();

	double length=60;
	HashMap<String, Point> loadFlowMap = new HashMap<String, Point>();
}
