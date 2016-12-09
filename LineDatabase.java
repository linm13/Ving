package gridlabd;

import Definition.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Definition.overhead_line;

import gridlabd.SetCase;

public class LineDatabase extends JFrame {
	public LineDatabase() {
		init();
	}

	JComboBox typeOfWireBox;
	JComboBox typeOfSpacingBox;
	JTabbedPane mainPanel;
	JTable[] mainTable;
	static int[] oriNum = new int[] { 7, 9, 1 };

	LineDatabase LDB;
	JPanel[] panel;

	private void init() {
		setTitle("导线数据库");
		setVisible(true);
		setSize((int) (SetCase.frameSize.getWidth() * 0.8),
				(int) (SetCase.frameSize.getHeight() * 0.8));
		Toolkit tool = getToolkit(); // 得到一个Toolkit对象
		Image myimage = tool.getImage(this.getClass().getResource(
				"Picture/Ving.png")); // 由tool获取图像
		setIconImage(myimage);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		typeOfWireBox = new JComboBox();
		typeOfSpacingBox = new JComboBox();

		mainPanel = new JTabbedPane();
		mainTable = new JTable[3];
		SetCase.mainTableModel = new DefaultTableModel[3];
		JScrollPane[] JSP = new JScrollPane[3];

		SetCase.mainTableModel[0] = new DefaultTableModel(new Object[][] {},
				new String[] { "名称", "A相导线", "B相导线", "C相导线", "中线导线", "空间结构" }) {
			public boolean isCellEditable(int row, int column) {
				if (row < 7)
					return false;
				else
					return true;
			}
		};

		Iterator iter = line_configuration.line_configurationMap.entrySet()
				.iterator();

		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			SetCase.mainTableModel[0].addRow(new Object[] {
					entry.getKey().toString(),
					line_configuration.line_configurationMap
							.get(entry.getKey()).get("conductor_A"),
					line_configuration.line_configurationMap
							.get(entry.getKey()).get("conductor_B"),
					line_configuration.line_configurationMap
							.get(entry.getKey()).get("conductor_C"),
					line_configuration.line_configurationMap
							.get(entry.getKey()).get("conductor_N"),
					line_configuration.line_configurationMap
							.get(entry.getKey()).get("spacing") });
		}

		for (int i = 0; i < (SetCase.numOfLine[0] - oriNum[0]); i++) {
			SetCase.mainTableModel[0].addRow(new Object[] {
					SetCase.newLineData[0].getValueAt(i, 0),
					SetCase.newLineData[0].getValueAt(i, 1),
					SetCase.newLineData[0].getValueAt(i, 2),
					SetCase.newLineData[0].getValueAt(i, 3),
					SetCase.newLineData[0].getValueAt(i, 4),
					SetCase.newLineData[0].getValueAt(i, 5) });
		}

		iter = overhead_line_conductor.overhead_line_conductorMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			typeOfWireBox.addItem(entry.getKey().toString());

		}
		iter = line_spacing.line_spacingMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			typeOfSpacingBox.addItem(entry.getKey().toString());

		}

		SetCase.mainTableModel[1] = new DefaultTableModel(new Object[][] {},
				new String[] { "名称", "几何平均半径(cm)", "单位电阻（Ohm/km）" }) {
			public boolean isCellEditable(int row, int column) {
				if (row < 9)
					return false;
				else
					return true;
			}
		};

		iter = overhead_line_conductor.overhead_line_conductorMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();

			SetCase.mainTableModel[1].addRow(new Object[] {
					entry.getKey().toString(),
					overhead_line_conductor.overhead_line_conductorMap.get(
							entry.getKey()).get("geometric_mean_radius"),
					overhead_line_conductor.overhead_line_conductorMap.get(
							entry.getKey()).get("resistance") });

		}

		for (int i = 0; i < (SetCase.numOfLine[1] - oriNum[1]); i++) {
			SetCase.mainTableModel[1].addRow(new Object[] {
					SetCase.newLineData[1].getValueAt(i, 0),
					SetCase.newLineData[1].getValueAt(i, 1),
					SetCase.newLineData[1].getValueAt(i, 2) });
		}

		SetCase.mainTableModel[2] = new DefaultTableModel(new Object[][] {},
				new String[] { "名称", "AB相距", "BC相距", "AC相距", "AN间距", "BN间距",
						"CN间距" }) {
			public boolean isCellEditable(int row, int column) {
				if (row < 1)
					return false;
				else
					return true;
			}
		};

		iter = line_spacing.line_spacingMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			SetCase.mainTableModel[2].addRow(new Object[] {
					entry.getKey().toString(),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_AB"),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_BC"),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_AC"),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_AN"),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_BN"),
					line_spacing.line_spacingMap.get(entry.getKey()).get(
							"distance_CN") });
		}

		for (int i = 0; i < (SetCase.numOfLine[2] - oriNum[2]); i++) {
			SetCase.mainTableModel[2].addRow(new Object[] {
					SetCase.newLineData[2].getValueAt(i, 0),
					SetCase.newLineData[2].getValueAt(i, 1),
					SetCase.newLineData[2].getValueAt(i, 2),
					SetCase.newLineData[2].getValueAt(i, 3),
					SetCase.newLineData[2].getValueAt(i, 4),
					SetCase.newLineData[2].getValueAt(i, 5),
					SetCase.newLineData[2].getValueAt(i, 6) });
		}

		panel = new JPanel[3];

		JButton add[] = new JButton[3];
		JButton del[] = new JButton[3];
		JButton renew[] = new JButton[3];

		for (int i = 0; i < 3; i++) {
			add[i] = new JButton("增加");
			del[i] = new JButton("删除");
			renew[i] = new JButton("更新");
			panel[i] = new JPanel();
			mainTable[i] = new JTable(SetCase.mainTableModel[i]);
			JSP[i] = new JScrollPane(mainTable[i]);
			mainTable[i].setRowHeight(30);

			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
			tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
			mainTable[i].setDefaultRenderer(Object.class, tcr);

			JSP[i].setBounds(0, 0,
					(int) (SetCase.frameSize.getWidth() * 0.8) - 110,
					(int) (SetCase.frameSize.getHeight() * 0.8));
			add[i].setBounds((int) (SetCase.frameSize.getWidth() * 0.8) - 105,
					(int) (SetCase.frameSize.getHeight() * 0.05), 80, 30);
			del[i].setBounds((int) (SetCase.frameSize.getWidth() * 0.8) - 105,
					(int) (SetCase.frameSize.getHeight() * 0.15), 80, 30);
			renew[i].setBounds(
					(int) (SetCase.frameSize.getWidth() * 0.8) - 105,
					(int) (SetCase.frameSize.getHeight() * 0.25), 80, 30);

			panel[i].setLayout(null);
			panel[i].add(JSP[i]);
			panel[i].add(add[i]);
			panel[i].add(del[i]);
			panel[i].add(renew[i]);

		}

		mainPanel.addTab("线路参数", null, panel[0], "线路总参数");
		mainPanel.addTab("导线参数", null, panel[1], "单相导线参数");
		mainPanel.addTab("空间结构", null, panel[2], "线路空间结构");
		add(mainPanel);
		mainTable[0].getColumn("A相导线").setCellEditor(
				new DefaultCellEditor(typeOfWireBox));
		mainTable[0].getColumn("B相导线").setCellEditor(
				new DefaultCellEditor(typeOfWireBox));
		mainTable[0].getColumn("C相导线").setCellEditor(
				new DefaultCellEditor(typeOfWireBox));
		mainTable[0].getColumn("中线导线").setCellEditor(
				new DefaultCellEditor(typeOfWireBox));
		mainTable[0].getColumn("空间结构").setCellEditor(
				new DefaultCellEditor(typeOfSpacingBox));

		add[0].addActionListener(new addLine());
		add[1].addActionListener(new addWire());
		add[2].addActionListener(new addSpacing());
		del[0].addActionListener(new delLine());
		del[1].addActionListener(new delWire());
		del[2].addActionListener(new delSpacing());
		renew[0].addActionListener(new renewLine());
		renew[1].addActionListener(new renewWire());
		renew[2].addActionListener(new renewSpacing());

	}

	private class addLine implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetCase.numOfLine[0]++;
			SetCase.mainTableModel[0].addRow(new Object[] {});
		}

	}

	private class addWire implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetCase.numOfLine[1]++;
			SetCase.mainTableModel[1].addRow(new Object[] {});
		}

	}

	private class addSpacing implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SetCase.numOfLine[2]++;
			SetCase.mainTableModel[2].addRow(new Object[] {});
		}

	}

	private class delLine implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i = mainTable[0].getSelectedRow();
			if (i >= oriNum[0]) {
				SetCase.numOfLine[0]--;
				SetCase.mainTableModel[0].removeRow(i);

			}
		}

	}

	private class delWire implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i = mainTable[1].getSelectedRow();
			if (i >= oriNum[1]) {
				SetCase.numOfLine[1]--;
				SetCase.mainTableModel[1].removeRow(i);
			}
		}

	}

	private class delSpacing implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i = mainTable[2].getSelectedRow();

			if (i >= oriNum[2]) {
				SetCase.numOfLine[2]--;
				SetCase.mainTableModel[2].removeRow(i);
			}
		}

	}

	private class renewLine implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			boolean isFull = true;
			for (int i = oriNum[0]; i < SetCase.numOfLine[0]; i++) {
				for (int j = 0; j < SetCase.mainTableModel[0].getColumnCount(); j++) {
					if (SetCase.mainTableModel[0].getValueAt(i, j) == null
							|| SetCase.mainTableModel[0].getValueAt(i, j)
									.toString().isEmpty())
						isFull = false;
				}
			}
			if (isFull) {
				for (int i = oriNum[0]; i < SetCase.numOfLine[0]; i++) {
					String name = SetCase.mainTableModel[0].getValueAt(i, 0)
							.toString();
					String conductor_A = SetCase.mainTableModel[0].getValueAt(
							i, 1).toString();
					String conductor_B = SetCase.mainTableModel[0].getValueAt(
							i, 2).toString();
					String conductor_C = SetCase.mainTableModel[0].getValueAt(
							i, 3).toString();
					String conductor_N = SetCase.mainTableModel[0].getValueAt(
							i, 4).toString();
					String spacing = SetCase.mainTableModel[0].getValueAt(i, 1)
							.toString();
					line_configuration.addMap(name, conductor_A, conductor_B,
							conductor_C, conductor_N, spacing);
					boolean isExited = false;
					for (int j = 0; j < SetCase.typeOfLine.getItemCount(); j++) {
						if (SetCase.typeOfLine.getItemAt(j).toString().equals(
								name))
							isExited = true;
					}
					if (!isExited)
						SetCase.typeOfLine.addItem(name);
				}
				JOptionPane.showMessageDialog(LDB, "更新成功！", "提示！",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(LDB, "信息不完整！", "更新失败！",
						JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private class renewWire implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			boolean isFull = true;
			for (int i = oriNum[1]; i < SetCase.numOfLine[1]; i++) {
				for (int j = 0; j < SetCase.mainTableModel[1].getColumnCount(); j++) {
					if (SetCase.mainTableModel[1].getValueAt(i, j) == null
							|| SetCase.mainTableModel[1].getValueAt(i, j)
									.toString().isEmpty())
						isFull = false;
				}
			}
			if (isFull) {
				for (int i = oriNum[1]; i < SetCase.numOfLine[1]; i++) {
					String name = SetCase.mainTableModel[1].getValueAt(i, 0)
							.toString();
					String geometric_mean_radius = SetCase.mainTableModel[1]
							.getValueAt(i, 1).toString();
					String resistance = SetCase.mainTableModel[1].getValueAt(i,
							2).toString();

					overhead_line_conductor.addMap(name, geometric_mean_radius,
							resistance);
					
					boolean isExited = false;
					for (int j = 0; j < typeOfWireBox.getItemCount(); j++) {
						if (typeOfWireBox.getItemAt(j).toString().equals(
								name))
							isExited = true;
					}
					if (!isExited)
						typeOfWireBox.addItem(name);
				}
				JOptionPane.showMessageDialog(LDB, "更新成功！", "提示！",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(LDB, "信息不完整！", "更新失败！",
						JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private class renewSpacing implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			boolean isFull = true;
			for (int i = oriNum[2]; i < SetCase.numOfLine[2]; i++) {
				for (int j = 0; j < SetCase.mainTableModel[2].getColumnCount(); j++) {
					if (SetCase.mainTableModel[2].getValueAt(i, j) == null
							|| SetCase.mainTableModel[2].getValueAt(i, j)
									.toString().isEmpty())
						isFull = false;
				}
			}
			if (isFull) {
				for (int i = oriNum[2]; i < SetCase.numOfLine[2]; i++) {
					String name = SetCase.mainTableModel[2].getValueAt(i, 0)
							.toString();
					String distance_AB = SetCase.mainTableModel[2].getValueAt(
							i, 1).toString();
					String distance_BC = SetCase.mainTableModel[2].getValueAt(
							i, 2).toString();
					String distance_AC = SetCase.mainTableModel[2].getValueAt(
							i, 3).toString();
					String distance_AN = SetCase.mainTableModel[2].getValueAt(
							i, 4).toString();
					String distance_BN = SetCase.mainTableModel[2].getValueAt(
							i, 5).toString();
					String distance_CN = SetCase.mainTableModel[2].getValueAt(
							i, 6).toString();

					line_spacing.addMap(name, distance_AB, distance_BC,
							distance_AC, distance_AN, distance_BN, distance_CN);
					boolean isExited = false;
					for (int j = 0; j < typeOfSpacingBox.getItemCount(); j++) {
						if (typeOfSpacingBox.getItemAt(j).toString().equals(
								name))
							isExited = true;
					}
					if (!isExited)
						typeOfSpacingBox.addItem(name);
				}
				JOptionPane.showMessageDialog(LDB, "更新成功！", "提示！",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(LDB, "信息不完整！", "更新失败！",
						JOptionPane.WARNING_MESSAGE);
			}
		}

	}

}
