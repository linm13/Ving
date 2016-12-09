package gridlabd;

/*���ߣ��֝�*/
/*���䣺linm13@mails.tsinghua.edu.cn*/
//JOptionPane.showMessageDialog(this, "������Ϣ��������", "����",
//		JOptionPane.WARNING_MESSAGE);
import java.awt.*;
import java.awt.event.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import Definition.*;
import GridlabdRunning.Command;
import GridlabdRunning.FileWrite;
import ResultExhibition.ColouredTopo;
import ResultProcessing.StringToNum;
import ResultProcessing.XMLPreprocessing;
import ResultProcessing.XMLProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SetCase extends JFrame {

	public SetCase() {
		init();

	}

	private void init() {
		initLine();
		initNewLineData();
		nodePoint = new HashMap<String, Point>();
		loadPointHash = new HashMap<String, Point>();
		overhead_linePoint = new HashMap<String, Point[]>();
		underground_linePoint = new HashMap<String, Point[]>();
		transformer_linePoint = new HashMap<String, Point[]>();

		sizeLevel = 1;

		iconScale = 50 * sizeLevel;

		offset = iconScale / 2;

		Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
		Image myimage = tool.getImage(this.getClass().getResource(
				"Picture/Ving.png")); // ��tool��ȡͼ��
		setIconImage(myimage);

		numOfColumns = new int[] { 5, 4, 4, 7, 5 };
		isFilled = false;
		isCleaning = false;
		isRunned = false;
		isSaved = false;
		isCorrect = true;

		numOfComs = new int[5];
		orderOfComs = new int[5];

		trPic = new JLabel[maxComs];
		loadPic = new JLabel[maxComs];
		genPic = new JLabel[maxComs];
		busPic = new JLabel[maxComs];
		trPoint = new Point[maxComs];
		loadPoint = new Point[maxComs];
		genPoint = new Point[maxComs];
		busPoint = new Point[maxComs];
		linePoint = new Point[2][maxComs];

		table = new JTable[5];

		p = new JPanel[5];

		freshGrey = new Color(250, 250, 250);
		tableMode = new DefaultTableModel[5];

		setTitle("Ving:GridLAB-D��װ���");

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitConfirm();
			}
		});

		graph = new pic();

		graph.addMouseMotionListener(new getTempLocation());
		// graph.addMouseListener(new mapLine());

		graph.setBackground(Color.white);
		graph.addMouseListener(new GetLine());
		graph.setLayout(null);

		JButton delCom = new JButton("ɾ��Ԫ��");
		JButton addCom = new JButton("���Ԫ��");
		JButton run = new JButton("���з���");
		JButton seeResult = new JButton("�鿴���");

		seeResult.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getResult();
			}
		});

		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					createData();
					runGridlabd();
				} catch (Exception ce) {
					JOptionPane.showMessageDialog(setCase, "�����Ϣ��������", "����",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		JPanel westPanel = new JPanel(new BorderLayout());

		b1 = new JRadioButton("�����");
		b1.setIcon(new ImageIcon(this.getClass()
				.getResource("Picture/gen2.png")));
		b1.setBackground(Color.white);
		b2 = new JRadioButton("�ڵ�");
		b2.setIcon(new ImageIcon(this.getClass()
				.getResource("Picture/bus2.png")));
		b2.setBackground(Color.white);
		b3 = new JRadioButton("����");
		b3.setIcon(new ImageIcon(this.getClass().getResource(
				"Picture/load2.png")));
		b3.setBackground(Color.white);
		b4 = new JRadioButton("��ѹ��");
		b4.setIcon(new ImageIcon(this.getClass().getResource(
				"Picture/trans2.png")));
		b4.setBackground(Color.white);
		b5 = new JRadioButton("��·");
		b5.setBackground(Color.white);
		b5.setIcon(new ImageIcon(this.getClass()
				.getResource("Picture/line.png")));

		ButtonGroup selCom = new ButtonGroup();
		selCom.add(b1);
		selCom.add(b2);
		selCom.add(b3);
		selCom.add(b4);
		selCom.add(b5);

		JPanel westNorthPanel = new JPanel();
		JLabel info1 = new JLabel("��ѡ��Ԫ����");
		westNorthPanel.add(info1);
		westNorthPanel.add(b1);
		westNorthPanel.add(b2);
		westNorthPanel.add(b3);
		westNorthPanel.add(b4);
		westNorthPanel.add(b5);

		westNorthPanel.setLayout(new GridLayout(6, 1));

		JPanel westCenterPanel = new JPanel();

		// westCenterPanel.setLayout(new GridLayout(2, 2));

		JPanel westSouthPanel = new JPanel();
		//nameOfCom = new JLabel("���ƣ�");

		comInfo = new JTextArea(7, 17);
		comInfo.setEditable(false);
		comInfo.setText("");
		comInfo.setBorder(BorderFactory.createTitledBorder("��ϸ��Ϣ"));
		Color freshGray = new Color(235, 235, 235);
		comInfo.setBackground(freshGray);

		westSouthPanel.add(comInfo);

		westNorthPanel.setBorder(BorderFactory.createTitledBorder("���Ԫ��"));

		westPanel.add(westNorthPanel, BorderLayout.NORTH);
		westCenterPanel.setBorder(BorderFactory.createTitledBorder("�ؼ�"));
		westPanel.add(westCenterPanel, BorderLayout.CENTER);

		westPanel.add(westSouthPanel, BorderLayout.SOUTH);

		addCom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCom();
			}
		});

		delCom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				delComFromGraph();

			}
		});

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frameSize = new Dimension();
		double ratio1 = 0.95;
		frameSize.width = (int) (ratio1 * ((double) (screenSize.width)));
		frameSize.height = (int) (ratio1 * ((double) (screenSize.height)));

		westPanelSize = new Dimension();
		westPanelSize.width = (int) (1.0 / 6.0 * ((double) (frameSize.width)));
		westPanelSize.height = (int) (0.917 * ((double) (frameSize.height)));

		graphSize = new Dimension();
		graphSize.width = (int) (5.0 / 6.0 * ((double) (frameSize.width))) - 18;
		graphSize.height = (int) (((double) (frameSize.height) - 315));

		westCenterPanel.setLayout(null);

		addCom.setBounds((int) (((double) westPanelSize.width) * 0.05),
				(int) (((double) westPanelSize.width) * 0.1),
				(int) (((double) westPanelSize.width) * 0.4),
				(int) (((double) westPanelSize.width) * 0.12));

		delCom.setBounds((int) (((double) westPanelSize.width) * 0.55),
				(int) (((double) westPanelSize.width) * 0.1),
				(int) (((double) westPanelSize.width) * 0.4),
				(int) (((double) westPanelSize.width) * 0.12));

		run.setBounds((int) (((double) westPanelSize.width) * 0.05),
				(int) (((double) westPanelSize.width) * 0.28),
				(int) (((double) westPanelSize.width) * 0.4),
				(int) (((double) westPanelSize.width) * 0.12));

		seeResult.setBounds((int) (((double) westPanelSize.width) * 0.55),
				(int) (((double) westPanelSize.width) * 0.28),
				(int) (((double) westPanelSize.width) * 0.4),
				(int) (((double) westPanelSize.width) * 0.12));

		JLabel freLabel = new JLabel("ϵͳƵ�ʣ�");
		fre = new JTextField();
		fre.setText("50.0");
		fre.setBackground(new Color(240, 240, 240));
		JLabel freUnit = new JLabel("Hz");

		showName = new JButton("��ʾ����");
		showName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setNameLabel();
				graph.repaint();
			}

		});
		showName.setBounds((int) (((double) westPanelSize.width) * 0.05),
				(int) (((double) westPanelSize.width) * 0.46),
				(int) (((double) westPanelSize.width) * 0.4),
				(int) (((double) westPanelSize.width) * 0.12));

		freLabel.setBounds((int) (((double) westPanelSize.width) * 0.5),
				(int) (((double) westPanelSize.width) * 0.46),
				(int) (((double) westPanelSize.width) * 0.31),
				(int) (((double) westPanelSize.width) * 0.1));

		fre.setBounds((int) (((double) westPanelSize.width) * 0.81),
				(int) (((double) westPanelSize.width) * 0.46),
				(int) (((double) westPanelSize.width) * 0.18),
				(int) (((double) westPanelSize.width) * 0.12));

		// freUnit.setBounds((int) (((double) westPanelSize.width) * 0.65),
		// (int) (((double) westPanelSize.width) * 0.46),
		// (int) (((double) westPanelSize.width) * 0.1),
		// (int) (((double) westPanelSize.width) * 0.12));

		westCenterPanel.add(showName);
		westCenterPanel.add(freUnit);
		westCenterPanel.add(freLabel);
		westCenterPanel.add(fre);
		westCenterPanel.add(addCom);
		westCenterPanel.add(delCom);
		westCenterPanel.add(run);
		westCenterPanel.add(seeResult);

		initInfoTable();
		setSize(frameSize);

		setLocationRelativeTo(null);

		setLayout(null);
		westPanel.setBounds(0, 0, westPanelSize.width, westPanelSize.height);

		graph.setBounds(westPanelSize.width, 0, graphSize.width,
				graphSize.height);
		graph.setBorder(BorderFactory.createTitledBorder("����Ԫ��"));

//		nameOfCom.setBounds(5, graphSize.height - 40, 100, 40);
//		graph.add(nameOfCom);
		
		tabbedPane.setBounds(westPanelSize.width, graphSize.height,
				graphSize.width, 255);
		tabbedPane.setBackground(freshGray);

		tabbedPane.setBorder(BorderFactory.createTitledBorder("����Ԫ������"));

		graph.addMouseWheelListener(new MouseDemo());
		add(westPanel);
		add(graph);
		add(tabbedPane);

		JMenuBar menu = new JMenuBar();

		JMenu menu1 = new JMenu("�ļ�");
		JMenuItem menu1Item1 = new JMenuItem("��");
		JMenuItem menu1Item2 = new JMenuItem("����");
		JMenuItem menu1Item3 = new JMenuItem("�ر�");
		menu1.add(menu1Item1);
		menu1.add(menu1Item2);
		menu1.add(menu1Item3);

		menu1Item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				openFile();
			}

		});

		menu1Item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveFile();
			}

		});

		menu1Item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				exitConfirm();
			}

		});

		JMenu menu2 = new JMenu("�༭");
		JMenuItem menu2Item1 = new JMenuItem("���");
		menu2.add(menu2Item1);

		menu2Item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cleanAll();
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

	private void getResult() {
		if (isRunned) {
			if (isCorrect) {
				ColouredTopo colouredTopoFrame = new ColouredTopo(nodePoint,
						loadPointHash, overhead_linePoint,
						underground_linePoint, transformer_linePoint);
				colouredTopoFrame.setVisible(true);
			}
		} else {
			JOptionPane.showMessageDialog(this, "�������з������", "����",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void cleanAll() {
		if (!isSaved) {
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "ȷ�ϲ���������", "��ʾ",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				isCleaning = true;
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < numOfComs[i]; j++) {
						tableMode[i].removeRow(0);

					}
				}
				numOfComs = new int[] { 0, 0, 0, 0, 0 };
				orderOfComs = new int[] { 0, 0, 0, 0, 0 };
				graph.removeAll();
			//	nameOfCom.setText("���ƣ�");
				comInfo.setText("");
				graph.clear();
		//		graph.add(nameOfCom);

				isCleaning = false;

			} else {
			}
		} else {
			isCleaning = true;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < numOfComs[i]; j++) {
					tableMode[i].removeRow(0);

				}
			}
			numOfComs = new int[] { 0, 0, 0, 0, 0 };
			orderOfComs = new int[] { 0, 0, 0, 0, 0 };
			graph.removeAll();
		//	nameOfCom.setText("���ƣ�");
			comInfo.setText("");
			graph.clear();
		//	graph.add(nameOfCom);

			isCleaning = false;
		}
	}

	private void exitConfirm() {
		if (!isSaved) {
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "�������˳���", "��ʾ",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0)
				System.exit(0);
			else {
			}
		} else {
			System.exit(0);
		}
	}

	private class aboutDialog extends JFrame {
		public aboutDialog() {
			Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
			Image myimage = tool.getImage(this.getClass().getResource(
					"Picture/Ving.png")); // ��tool��ȡͼ��
			setIconImage(myimage);
			setSize(480, 300);
			setLocationRelativeTo(null);
			setTitle("����Ving");
			JTextArea aboutInfo = new JTextArea(10, 20);
			aboutInfo.setEditable(false);
			aboutInfo.setBackground(freshGrey);
			setVisible(true);
			add(aboutInfo);
			aboutInfo.setFont(new Font("����", Font.PLAIN, 15));

			aboutInfo.append("\t\tVing: Ving Is Not GridLAB-D\n\n" + "�汾��2.0\n"
					+ "���ߣ��֝� ֣����\n" + "��ϵ��ʽ��linm13@mails.tsinghua.edu.cn\n"
					+ "\tzhengzt13@mails.tsinghua.edu.cn");
			this.setResizable(false);
		}
	}

	private class helpDialog extends JFrame {
		public helpDialog() {
			Toolkit tool = getToolkit(); // �õ�һ��Toolkit����
			Image myimage = tool.getImage(this.getClass().getResource(
					"Picture/Ving.png")); // ��tool��ȡͼ��
			setIconImage(myimage);
			setSize(700, 600);
			setLocationRelativeTo(null);
			setTitle("������Ϣ");
			JTextArea helpInfo = new JTextArea(10, 20);
			helpInfo.setEditable(false);
			helpInfo.setBackground(freshGrey);
			setVisible(true);
			add(helpInfo);

			helpInfo
					.append(" ʹ�����̼�˵����\n"
							+ " \n"
							+ " �����棺\n"
							+ " �������̣�\n"
							+ " 1���ڡ����Ԫ�������ѡ����Ҫ��ӵĽڵ�Ԫ����\n"
							+ " 2��������ؼ�������С����Ԫ������ť��Ԫ�������ڡ�����Ԫ�����������Ͻǣ�\n"
							+ " 3���϶�Ԫ�������ʵ�λ�ã���ʹ�������ָı仭����ʾ��������\n"
							+ " 4���ظ�����1-3ֱ�����нڵ�Ԫ����������·��������ɣ�\n"
							+ " 5���ڡ����Ԫ�������ѡ�С���·����\n"
							+ " 6���ڡ�����Ԫ�������������ε����Ҫ���ߵ�Ԫ��������ߣ�\n"
							+ " 7���ظ�����5-6ֱ��������·�����ɣ�\n"
							+ " 8���ڡ�����Ԫ�����������ѡ�����д����Ԫ������·��Ϣ��\n"
							+ " 9�����÷���ϵͳƵ�ʣ�Ĭ��Ϊ50Hz��\n"
							+ " 10����������з��桱��ť��\n"
							+ " 11�����гɹ��󣬵�����鿴�������ť�򿪷�����չʾ���档\n"
							+ " \n"
							+ " ע�⣺\n"
							+ " 1��ʹ�ñ����֮ǰ���밲װGridLAB-D�������ȷ��ϵͳ��������������ȷ��\n"
							+ " 2�����������Ϣ���������������ݲ��ܰ����ո����\n"
							+ " 3����·�������й����мܿ���·����Ԥ��ѡ������Զ��壬���ڡ���·��ѡ���ѡ�񡰵������ݿ⡱����¼�룻\n"
							+ " 4�������µ��ߺ���뵥�����°�ť�����ɼ��뵼�����ݿ⣻\n"
							+ " 5��������ɺ����������·���˽ڵ㣬��ֱ���ڡ���·��ѡ��µĽڵ�1�ͽڵ�2����ѡ����ɺ��������²��ߡ����ɣ�\n"
							+ " 6��ѡ�л����е�Ԫ������·�����������½ǲ鿴����ϸ��Ϣ��\n"
							+ " 7������ͼ��Ԫ����Ϣ������ͨ���˵�������Ͷ�ȡ��\n"
							+ " 8��һ�������������ڵ�Ķ��ѹ֮��ܳ���0.1%��\n"
							+ " 9�������϶���������ʱ�п��٣���ر�ͼ����ʾ��Ϣ�����ԣ�\n"
							+ " 10����ѹ�������Ϊԭ�ߣ��Ҳ�Ϊ���ߣ�\n"
							+ " 11������ʱ��������·�Ľڵ�1ѡΪ������Դ�⡣\n\n"
							+ " ������չʾ���棺\n"
							+ " �������̣�\n"
							+ " 1���ڡ������ֲ����������пɲ鿴��������\n"
							+ " 2���ڵ��ѹ����ֵԽ�ߣ��ڵ�Խ����ɫԽ�죻�ڵ��ѹ����ֵԽ�ͣ��ڵ�ԽС����ɫԽ����\n"
							+ " 3������ƶ����ڵ����·�Ϸ������Զ�ȡ��Ӧ����ϸ���ݣ�\n"
							+ " 4���ڿ�ѡ��������У��������ʾ�������ݡ���ť�����ڻ�������ʾ���г���������Ϣ��\n"
							+ " 5���ڿ�ѡ��������У��������ʾ��ѹ���ݡ���ť�����ڻ�������ʾ���е�ѹ������Ϣ��\n"
							+ " 6���ڿ�ѡ��������У��������ʾ������Ϣ����ť�����ڻ�������ʾ���нڵ����·�����ƣ�\n"
							+ " 7���ڲ���5��6�У�����·���˹��ڸ��ӵ�����������Ϣ�໥�ص�������ͨ���������������ű������϶��ڵ�ķ�ʽʹ�������ڱ��ϣ�\n"
							+ " 8���ڿ�ѡ��������У��������ʾ������ġ���ť���Ե�����·������ķֲ���ͼ��\n"
							+ " 9���ڿ�ѡ��������У�������������������ť�������ı���ʽ�������������Ԥ�����������½ǿ�����\n"
							+ " 10���ڿ�ѡ��������У������ȫ����ʾ�������ť��������������ʾ��������������׶�ȡ��Ϣ��\n");

			helpInfo.setLineWrap(true);
			helpInfo.setFont(new Font("����", Font.PLAIN, 15));
			JScrollPane jsp = new JScrollPane(helpInfo);
			jsp
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			this.add(jsp);
			jsp.getVerticalScrollBar().setValue(0);

		}
	}

	private void saveFile() {

		JFileChooser saveJFC = new JFileChooser();
		saveJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		saveJFC.showDialog(this, "ȷ��");
		File path = saveJFC.getSelectedFile();

		if (!path.toString().isEmpty()) {
			String file = JOptionPane.showInputDialog("�������ļ���") + ".lin";

			try {
				BufferedWriter w = new BufferedWriter(new FileWriter(path
						+ "\\" + file));

				w.write(iconScale + token);
				w.newLine();
				w.write(orderOfComs[0] + token);
				w.write(numOfComs[0] + token);
				for (int i = 0; i < orderOfComs[0]; i++) {
					w.write(genPoint[i].x + token + genPoint[i].y + token);
				}
				for (int i = 0; i < numOfComs[0]; i++) {
					for (int j = 0; j < numOfColumns[0]; j++) {
						String temp = ((String) tableMode[0].getValueAt(i, j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(orderOfComs[1] + token);
				w.write(numOfComs[1] + token);
				for (int i = 0; i < orderOfComs[1]; i++) {
					w.write(busPoint[i].x + token + busPoint[i].y + token);
				}
				for (int i = 0; i < numOfComs[1]; i++) {
					for (int j = 0; j < numOfColumns[1]; j++) {
						String temp = ((String) tableMode[1].getValueAt(i, j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(orderOfComs[2] + token);
				w.write(numOfComs[2] + token);
				for (int i = 0; i < orderOfComs[2]; i++) {
					w.write(loadPoint[i].x + token + loadPoint[i].y + token);
				}
				for (int i = 0; i < numOfComs[2]; i++) {
					for (int j = 0; j < numOfColumns[2]; j++) {
						String temp = ((String) tableMode[2].getValueAt(i, j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(orderOfComs[3] + token);
				w.write(numOfComs[3] + token);
				for (int i = 0; i < orderOfComs[3]; i++) {
					w.write(trPoint[i].x + token + trPoint[i].y + token);
				}
				for (int i = 0; i < numOfComs[3]; i++) {
					for (int j = 0; j < numOfColumns[3]; j++) {
						String temp = (String) (tableMode[3].getValueAt(i, j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(orderOfComs[4] + token);
				w.write(numOfComs[4] + token);
				for (int i = 0; i < orderOfComs[4]; i++) {
					w.write(linePoint[0][i].x + token + linePoint[0][i].y
							+ token + linePoint[1][i].x + token
							+ linePoint[1][i].y + token);
				}
				for (int i = 0; i < numOfComs[4]; i++) {
					for (int j = 0; j < numOfColumns[4]; j++) {
						String temp = ((String) tableMode[4].getValueAt(i, j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(numOfLine[0] + token);
				for (int i = 0; i < (numOfLine[0] - LineDatabase.oriNum[0]); i++) {
					for (int j = 0; j < 6; j++) {
						String temp = ((String) mainTableModel[0].getValueAt(i
								+ LineDatabase.oriNum[0], j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(numOfLine[1] + token);
				for (int i = 0; i < (numOfLine[1] - LineDatabase.oriNum[1]); i++) {
					for (int j = 0; j < 3; j++) {
						String temp = ((String) mainTableModel[1].getValueAt(i
								+ LineDatabase.oriNum[1], j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.write(numOfLine[2] + token);
				for (int i = 0; i < (numOfLine[2] - LineDatabase.oriNum[2]); i++) {
					for (int j = 0; j < 7; j++) {
						String temp = ((String) mainTableModel[2].getValueAt(i
								+ LineDatabase.oriNum[2], j));
						temp = temp == null ? "null" : temp;
						temp = temp.isEmpty() ? "null" : temp;
						w.write(temp + token);
					}
				}
				w.newLine();

				w.close();
				isSaved = true;
				JOptionPane.showMessageDialog(this, "����ɹ���", "���ѣ�",
						JOptionPane.WARNING_MESSAGE);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private void openFile() {

		Object[] options = { "����", "ȡ��" };
		int response = JOptionPane.showOptionDialog(this, "�����ļ��Ḳ�������ļ���������",
				"����", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
		if (response == 1)
			;

		else {
			showName.setText("��ʾ����");

			graph.removeAll();
	//		graph.add(nameOfCom);
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < numOfComs[i]; j++)
					tableMode[i].removeRow(0);
			}
			JFileChooser openJFC = new JFileChooser();
			openJFC.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			openJFC.showDialog(this, "ȷ��");

			File file = openJFC.getSelectedFile();

			if (!file.toString().endsWith(".lin")) {
				JOptionPane.showMessageDialog(this, "��ѡ��.lin�ļ���", "����",
						JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					BufferedReader r = new BufferedReader(new FileReader(file));

					String[] data = r.readLine().split(token);
					iconScale = Double.parseDouble(data[0]);
					offset = iconScale / 2;
					data = r.readLine().split(token);

					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];

					orderOfComs[0] = Integer.parseInt(data[0]);
					numOfComs[0] = Integer.parseInt(data[1]);
					for (int i = 0; i < orderOfComs[0]; i++) {
						genPoint[i] = new Point(Integer
								.parseInt(data[2 * i + 2]), Integer
								.parseInt(data[2 * i + 3]));
						if (genPoint[i].x != -1)
							addOpenCom("gen", i);
					}

					for (int i = 0; i < numOfComs[0]; i++) {
						tableMode[0].addRow(new Object[] {
								data[2 + 2 * orderOfComs[0] + 5 * i],
								data[2 + 2 * orderOfComs[0] + 5 * i + 1],
								data[2 + 2 * orderOfComs[0] + 5 * i + 2],
								data[2 + 2 * orderOfComs[0] + 5 * i + 3],
								data[2 + 2 * orderOfComs[0] + 5 * i + 4] });
					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					orderOfComs[1] = Integer.parseInt(data[0]);
					numOfComs[1] = Integer.parseInt(data[1]);
					for (int i = 0; i < orderOfComs[1]; i++) {
						busPoint[i] = new Point(Integer
								.parseInt(data[2 * i + 2]), Integer
								.parseInt(data[2 * i + 3]));
						if (busPoint[i].x != -1)
							addOpenCom("bus", i);
					}

					for (int i = 0; i < numOfComs[1]; i++) {
						tableMode[1].addRow(new Object[] {
								data[2 + 2 * orderOfComs[1] + 4 * i],
								data[2 + 2 * orderOfComs[1] + 4 * i + 1],
								data[2 + 2 * orderOfComs[1] + 4 * i + 2],
								data[2 + 2 * orderOfComs[1] + 4 * i + 3] });
					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					orderOfComs[2] = Integer.parseInt(data[0]);
					numOfComs[2] = Integer.parseInt(data[1]);
					for (int i = 0; i < orderOfComs[2]; i++) {
						loadPoint[i] = new Point(Integer
								.parseInt(data[2 * i + 2]), Integer
								.parseInt(data[2 * i + 3]));
						if (loadPoint[i].x != -1)
							addOpenCom("load", i);
					}

					for (int i = 0; i < numOfComs[2]; i++) {
						tableMode[2].addRow(new Object[] {
								data[2 + 2 * orderOfComs[2] + 4 * i],
								data[2 + 2 * orderOfComs[2] + 4 * i + 1],
								data[2 + 2 * orderOfComs[2] + 4 * i + 2],
								data[2 + 2 * orderOfComs[2] + 4 * i + 3] });
					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					orderOfComs[3] = Integer.parseInt(data[0]);
					numOfComs[3] = Integer.parseInt(data[1]);
					for (int i = 0; i < orderOfComs[3]; i++) {
						trPoint[i] = new Point(Integer
								.parseInt(data[2 * i + 2]), Integer
								.parseInt(data[2 * i + 3]));
						if (trPoint[i].x != -1)
							addOpenCom("trans", i);
					}

					for (int i = 0; i < numOfComs[3]; i++) {
						tableMode[3].addRow(new Object[] {
								data[2 + 2 * orderOfComs[3] + 7 * i],
								data[2 + 2 * orderOfComs[3] + 7 * i + 1],
								data[2 + 2 * orderOfComs[3] + 7 * i + 2],
								data[2 + 2 * orderOfComs[3] + 7 * i + 3],
								data[2 + 2 * orderOfComs[3] + 7 * i + 4],
								data[2 + 2 * orderOfComs[3] + 7 * i + 5],
								data[2 + 2 * orderOfComs[3] + 7 * i + 6] });
					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					orderOfComs[4] = Integer.parseInt(data[0]);
					numOfComs[4] = Integer.parseInt(data[1]);
					for (int i = 0; i < orderOfComs[4]; i++) {
						linePoint[0][i] = new Point(Integer
								.parseInt(data[4 * i + 2]), Integer
								.parseInt(data[4 * i + 3]));
						linePoint[1][i] = new Point(Integer
								.parseInt(data[4 * i + 4]), Integer
								.parseInt(data[4 * i + 5]));
						if (linePoint[0][i].x != -1)
							addOpenCom("line", i);
					}

					for (int i = 0; i < numOfComs[4]; i++) {
						tableMode[4].addRow(new Object[] {
								data[2 + 4 * orderOfComs[4] + 5 * i],
								data[2 + 4 * orderOfComs[4] + 5 * i + 1],
								data[2 + 4 * orderOfComs[4] + 5 * i + 2],
								data[2 + 4 * orderOfComs[4] + 5 * i + 3],
								data[2 + 4 * orderOfComs[4] + 5 * i + 4] });
					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					numOfLine[0] = Integer.parseInt(data[0]);
					for (int i = 0; i < numOfLine[0] - LineDatabase.oriNum[0]; i++) {

						newLineData[0].addRow(new Object[] {
								data[1 + 6 * i + 0], data[1 + 6 * i + 1],
								data[1 + 6 * i + 2], data[1 + 6 * i + 3],
								data[1 + 6 * i + 4], data[1 + 6 * i + 5] });

					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					numOfLine[1] = Integer.parseInt(data[0]);
					for (int i = 0; i < numOfLine[1] - LineDatabase.oriNum[1]; i++) {

						newLineData[1].addRow(new Object[] {
								data[1 + 3 * i + 0], data[1 + 3 * i + 1],
								data[1 + 3 * i + 2] });

					}

					data = r.readLine().split(token);
					for (int i = 0; i < data.length; i++)
						data[i] = data[i].equals("null") ? "" : data[i];
					numOfLine[2] = Integer.parseInt(data[0]);
					for (int i = 0; i < numOfLine[2] - LineDatabase.oriNum[2]; i++) {

						newLineData[2].addRow(new Object[] {
								data[1 + 7 * i + 0], data[1 + 7 * i + 1],
								data[1 + 7 * i + 2], data[1 + 7 * i + 3],
								data[1 + 7 * i + 4], data[1 + 7 * i + 5],
								data[1 + 7 * i + 6] });

					}

					r.close();
					wiring();

				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(this, "��ȡ�ļ�����", "����",
							JOptionPane.WARNING_MESSAGE);
				}

			}

		}
	}

	private void initInfoTable() {
		tabbedPane = new JTabbedPane();

		for (int i = 0; i < 5; i++) {
			p[i] = new JPanel();
		}
		
		

		tabbedPane.addTab("�����", null, p[0], "�������Ϣ");
		tabbedPane.addTab("�ڵ�", null, p[1], "�ڵ���Ϣ");
		tabbedPane.addTab("����", null, p[2], "������Ϣ");
		tabbedPane.addTab("��ѹ��", null, p[3], "��ѹ����Ϣ");
		tabbedPane.addTab("��·", null, p[4], "��·��Ϣ");

		tableMode[0] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "��ѹ����λ��kV��", "�Ƕȣ���λ��d��", "�й�����λ��MW��", "�ڵ�����" }) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return false;
				else
					return true;
			}
		};

		tableMode[1] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "��ѹ��kV��", "�Ƕȣ���λ��d��", "�ڵ�����" }) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return false;
				else
					return true;
			}
		};

		tableMode[2] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "�й���MW��", "�޹���Mvar��", "���ѹ����λ��kV��" }) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return false;
				else
					return true;
			}
		};

		tableMode[3] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "һ�β���ѹ��kV��", "���β���ѹ��kV��", "���裨pu��", "��У�pu��", "���ӷ�ʽ",
				"����ʣ���λ��MVA��" }) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return false;
				else
					return true;
			}
		};

		tableMode[4] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "�����ͺ�", "���ȣ�km��", "�ڵ�1", "�ڵ�2" }) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0)
					return false;
				else
					return true;
			}
		};

		table = new JTable[5];
		JScrollPane[] JSP = new JScrollPane[5];

		for (int i = 0; i < 5; i++) {
			table[i] = new JTable(tableMode[i]);

			table[i].addMouseListener(new delFromTableToGraph(i));
			JSP[i] = new JScrollPane(table[i]);
			table[i].setRowHeight(20);
			JSP[i].setBounds(0, 0, graphSize.width, 198);

			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
			tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
			table[i].setDefaultRenderer(Object.class, tcr);

			p[i].add(JSP[i]);
			p[i].setLayout(null);
		}

		JSP[4].setBounds(0, 0, graphSize.width - 130, 198);
		JButton mapLine = new JButton("���²���");
		JButton addLine = new JButton("�������ݿ�");
		mapLine.setBounds(graphSize.width - 125, 30, 105, 30);
		addLine.setBounds(graphSize.width - 125, 100, 105, 30);

		addLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new LineDatabase();
			}

		});

		p[4].add(addLine);

		mapLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				wiring();
			}

		});

		p[4].add(mapLine);

		bus1 = new JComboBox();
		bus2 = new JComboBox();
		typeOfLine = new JComboBox();
		typeOfLine.addItem("LGJ-240/30");
		typeOfLine.addItem("LGJ-240/40");
		typeOfLine.addItem("LGJ-300/25");
		typeOfLine.addItem("LGJ-300/40");
		typeOfLine.addItem("LGJ-400/35");
		typeOfLine.addItem("LGJ-400/50");
		typeOfLine.addItem("testLine");

		JComboBox typeOfTrConnect = new JComboBox();
		typeOfTrConnect.addItem("Y-Y");
		typeOfTrConnect.addItem("Delta-Delta");
		typeOfTrConnect.addItem("Y-Delta");
		typeOfTrConnect.addItem("Delta-Y");

		JComboBox typeOfGen = new JComboBox();
		// typeOfGen.addItem("PV");
		typeOfGen.addItem("SWING");

		JComboBox typeOfNode = new JComboBox();

		typeOfNode.addItem("PQ");
		// typeOfNode.addItem("PV");
		typeOfNode.addItem("SWING");

		table[0].getColumn("�ڵ�����").setCellEditor(
				new DefaultCellEditor(typeOfGen));
		table[1].getColumn("�ڵ�����").setCellEditor(
				new DefaultCellEditor(typeOfNode));

		table[3].getColumn("���ӷ�ʽ").setCellEditor(
				new DefaultCellEditor(typeOfTrConnect));

		table[4].getColumn("�����ͺ�").setCellEditor(
				new DefaultCellEditor(typeOfLine));
		table[4].getColumn("�ڵ�1").setCellEditor(new DefaultCellEditor(bus1));
		table[4].getColumn("�ڵ�2").setCellEditor(new DefaultCellEditor(bus2));

	}

	private class delFromTableToGraph implements MouseInputListener {

		int i = 0;

		public delFromTableToGraph(int i) {
			this.i = i;
		}

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			isRunned = false;
			int toDelRow = table[i].getSelectedRow();
			int tempNum = 0;
			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher("");

			if (toDelRow != -1) {
				if (i == 0) {
					m = p.matcher(tableMode[i].getValueAt(toDelRow, 0)
							.toString());
					while (m.find()) {
						if (!"".equals(m.group()))
							tempNum = Integer.parseInt(m.group());
					}
	//				nameOfCom.setText("���ƣ������" + tempNum);
					setComInfo("gen", tempNum - 1);
					toDelName = "gen";
					toDelNum = tempNum - 1;
				}
				if (i == 1) {
					m = p.matcher(tableMode[i].getValueAt(toDelRow, 0)
							.toString());
					while (m.find()) {
						if (!"".equals(m.group()))
							tempNum = Integer.parseInt(m.group());
					}
			//		nameOfCom.setText("���ƣ��ڵ�" + tempNum);
					setComInfo("bus", tempNum - 1);
					toDelName = "bus";
					toDelNum = tempNum - 1;
				}

				if (i == 2) {
					m = p.matcher(tableMode[i].getValueAt(toDelRow, 0)
							.toString());
					while (m.find()) {
						if (!"".equals(m.group()))
							tempNum = Integer.parseInt(m.group());
					}
			//		nameOfCom.setText("���ƣ�����" + tempNum);
					setComInfo("load", tempNum - 1);
					toDelName = "load";
					toDelNum = tempNum - 1;
				}
				if (i == 3) {
					m = p.matcher(tableMode[i].getValueAt(toDelRow, 0)
							.toString());
					while (m.find()) {
						if (!"".equals(m.group()))
							tempNum = Integer.parseInt(m.group());
					}
		//			nameOfCom.setText("���ƣ���ѹ��" + tempNum);
					setComInfo("trans", tempNum - 1);
					toDelName = "trans";
					toDelNum = tempNum - 1;
				}
				if (i == 4) {
					m = p.matcher(tableMode[i].getValueAt(toDelRow, 0)
							.toString());
					while (m.find()) {
						if (!"".equals(m.group()))
							tempNum = Integer.parseInt(m.group());
					}
			//		nameOfCom.setText("���ƣ���·" + tempNum);
					setComInfo("line", tempNum - 1);
					toDelName = "line";
					toDelNum = tempNum - 1;

				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private void wiring() {

		int[][] x, y;
		x = new int[2][maxComs];
		y = new int[2][maxComs];
		int num = 0;

		num = numOfComs[4];

		for (int i = 0; i < num; i++) {
			int order[] = new int[maxComs];
			String bus[] = new String[2], temp[] = new String[2];
			bus[0] = tableMode[4].getValueAt(i, 3) == null ? "" : tableMode[4]
					.getValueAt(i, 3).toString();
			bus[1] = tableMode[4].getValueAt(i, 4) == null ? "" : tableMode[4]
					.getValueAt(i, 4).toString();

			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(bus[0]);
			for (int j = 0; j < 2; j++) {
				m = p.matcher(bus[j]);
				while (m.find()) {
					if (!"".equals(m.group()))
						order[j] = Integer.parseInt(m.group());
				}

				temp[j] = bus[j].split("" + order[j])[0];

				if (bus[0].isEmpty() || bus[1].isEmpty()) {
					x[0][i] = -100;
					x[1][i] = -100;

					y[0][i] = -100;
					y[1][i] = -100;

				} else {
					if (temp[j].equals("�����")) {
						x[j][i] = genPoint[order[j] - 1].x
								+ (genPoint[order[j] - 1].x == -100 ? 0
										: (int) offset);
						y[j][i] = genPoint[order[j] - 1].y
								+ (genPoint[order[j] - 1].y == -100 ? 0
										: (int) offset);
					} else if (temp[j].equals("�ڵ�")) {
						x[j][i] = busPoint[order[j] - 1].x
								+ (busPoint[order[j] - 1].x == -100 ? 0
										: (int) offset);
						y[j][i] = busPoint[order[j] - 1].y
								+ (busPoint[order[j] - 1].y == -100 ? 0
										: (int) offset);
					} else if (temp[j].equals("����")) {
						x[j][i] = loadPoint[order[j] - 1].x
								+ (loadPoint[order[j] - 1].x == -100 ? 0
										: (int) offset);
						y[j][i] = loadPoint[order[j] - 1].y
								+ (loadPoint[order[j] - 1].y == -100 ? 0
										: (int) offset);
					} else if (temp[j].equals("��ѹ��")) {
						String priOrSec = temp[j] = bus[j].split("" + order[j])[1];
						if (priOrSec.equals("ԭ��")) {
							x[j][i] = trPoint[order[j] - 1].x;
							y[j][i] = trPoint[order[j] - 1].y
									+ (trPoint[order[j] - 1].y == -100 ? 0
											: (int) offset);
						} else {
							x[j][i] = trPoint[order[j] - 1].x
									+ (trPoint[order[j] - 1].x == -100 ? 0
											: 2 * (int) offset);
							y[j][i] = trPoint[order[j] - 1].y
									+ (trPoint[order[j] - 1].y == -100 ? 0
											: (int) offset);
						}
					} else if (bus[j].equals(" ")) {
						x[j][i] = tempLocation.x;
						y[j][i] = tempLocation.y;
					}

				}
			}

		}

		graph.drawLine(x[0], y[0], x[1], y[1], num);

		for (int i = 0; i < numOfComs[4]; i++) {
			String lineName = tableMode[4].getValueAt(i, 0).toString();
			int lineOrder = 1;
			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(lineName);
			while (m.find()) {
				if (!"".equals(m.group()))
					lineOrder = Integer.parseInt(m.group());
			}

			linePoint[0][lineOrder - 1] = new Point(x[0][i], y[0][i]);
			linePoint[1][lineOrder - 1] = new Point(x[1][i], y[1][i]);
		}

		isSaved = false;

	}

	private void addOpenCom(String name, int num) {

		if (name.equals("gen")) {

			ImageIcon genIcon = new ImageIcon(this.getClass().getResource(
					"Picture/gen.png"));

			try {
				Image genImage = genIcon.getImage();
				genImage = genImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				genIcon = new ImageIcon(genImage);
			} catch (Exception e) {
			}

			genPic[num] = new JLabel(genIcon);

			graph.add(genPic[num]);

			genPic[num].setBounds(genPoint[num].x, genPoint[num].y,
					(int) iconScale, (int) iconScale);

			setMove(genPic[num], "gen", num);

			graph.validate();

			bus1.addItem("�����" + (num + 1));
			bus2.addItem("�����" + (num + 1));

		} else if (name.equals("bus")) {

			ImageIcon busIcon = new ImageIcon(this.getClass().getResource(
					"Picture/bus.png"));

			try {
				Image busImage = busIcon.getImage();
				busImage = busImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				busIcon = new ImageIcon(busImage);
			} catch (Exception e) {
			}

			busPic[num] = new JLabel(busIcon);
			graph.add(busPic[num]);
			busPic[num].setBounds(busPoint[num].x, busPoint[num].y,
					(int) iconScale, (int) iconScale);
			setMove(busPic[num], "bus", num);
			validate();

			bus1.addItem("�ڵ�" + (num + 1));
			bus2.addItem("�ڵ�" + (num + 1));

		} else if (name.equals("load")) {

			ImageIcon loadIcon = new ImageIcon(this.getClass().getResource(
					"Picture/load.png"));

			try {
				Image loadImage = loadIcon.getImage();
				loadImage = loadImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				loadIcon = new ImageIcon(loadImage);
			} catch (Exception e) {
			}

			loadPic[num] = new JLabel(loadIcon);
			graph.add(loadPic[num]);
			loadPic[num].setBounds(loadPoint[num].x, loadPoint[num].y,
					(int) iconScale, (int) iconScale);
			setMove(loadPic[num], "load", num);
			validate();

			bus1.addItem("����" + (num + 1));
			bus2.addItem("����" + (num + 1));

		} else if (name.equals("trans")) {

			ImageIcon trIcon = new ImageIcon(this.getClass().getResource(
					"Picture/trans.png"));

			try {
				Image trImage = trIcon.getImage();
				trImage = trImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				trIcon = new ImageIcon(trImage);
			} catch (Exception e) {
			}

			trPic[num] = new JLabel(trIcon);

			graph.add(trPic[num]);
			trPic[num].setBounds(trPoint[num].x, trPoint[num].y,
					(int) iconScale, (int) iconScale);

			setMove(trPic[num], "trans", num);

			validate();

			bus1.addItem("��ѹ��" + (num + 1) + "ԭ��");
			bus1.addItem("��ѹ��" + (num + 1) + "����");
			bus2.addItem("��ѹ��" + (num + 1) + "ԭ��");
			bus2.addItem("��ѹ��" + (num + 1) + "����");

		} else if (name.equals("line")) {

		} else {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��Ԫ��", "����",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void addCom() {
		isRunned = false;
		boolean sig = false;
		if (showName.getText().equals("��������")) {
			setNameLabel();
			sig = true;

		}
		if (b1.isSelected()) {
			ImageIcon genIcon = new ImageIcon(this.getClass().getResource(
					"Picture/gen.png"));

			try {
				Image genImage = genIcon.getImage();
				genImage = genImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				genIcon = new ImageIcon(genImage);
			} catch (Exception e) {
			}

			genPic[numOfComs[0]] = new JLabel(genIcon);

			graph.add(genPic[numOfComs[0]]);

			genPic[numOfComs[0]].setBounds(10, 15, (int) iconScale,
					(int) iconScale);

			setMove(genPic[numOfComs[0]], "gen", orderOfComs[0]);

			genPoint[orderOfComs[0]] = new Point(10, 15);
			graph.validate();
			orderOfComs[0]++;
			tableMode[0].addRow(new Object[] { "�����" + orderOfComs[0], "", "0",
					"0", "SWING" });
			bus1.addItem("�����" + orderOfComs[0]);
			bus2.addItem("�����" + orderOfComs[0]);
			numOfComs[0]++;

		} else if (b2.isSelected()) {

			ImageIcon busIcon = new ImageIcon(this.getClass().getResource(
					"Picture/bus.png"));

			try {
				Image busImage = busIcon.getImage();
				busImage = busImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				busIcon = new ImageIcon(busImage);
			} catch (Exception e) {
			}

			busPic[numOfComs[1]] = new JLabel(busIcon);
			graph.add(busPic[numOfComs[1]]);
			busPic[numOfComs[1]].setBounds(10, 15, (int) iconScale,
					(int) iconScale);
			setMove(busPic[numOfComs[1]], "bus", orderOfComs[1]);
			validate();
			busPoint[orderOfComs[1]] = new Point(10, 15);
			orderOfComs[1]++;

			tableMode[1].addRow(new Object[] { "�ڵ�" + orderOfComs[1], "", "0",
					"PQ" });
			bus1.addItem("�ڵ�" + orderOfComs[1]);
			bus2.addItem("�ڵ�" + orderOfComs[1]);

			numOfComs[1]++;

		} else if (b3.isSelected()) {

			ImageIcon loadIcon = new ImageIcon(this.getClass().getResource(
					"Picture/load.png"));

			try {
				Image loadImage = loadIcon.getImage();
				loadImage = loadImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				loadIcon = new ImageIcon(loadImage);
			} catch (Exception e) {
			}

			loadPic[numOfComs[2]] = new JLabel(loadIcon);
			graph.add(loadPic[numOfComs[2]]);
			loadPic[numOfComs[2]].setBounds(10, 15, (int) iconScale,
					(int) iconScale);
			setMove(loadPic[numOfComs[2]], "load", orderOfComs[2]);
			validate();
			loadPoint[orderOfComs[2]] = new Point(10, 15);
			orderOfComs[2]++;
			tableMode[2].addRow(new Object[] { "����" + orderOfComs[2] });
			bus1.addItem("����" + orderOfComs[2]);
			bus2.addItem("����" + orderOfComs[2]);
			numOfComs[2]++;

		} else if (b4.isSelected()) {

			ImageIcon trIcon = new ImageIcon(this.getClass().getResource(
					"Picture/trans.png"));

			try {
				Image trImage = trIcon.getImage();
				trImage = trImage.getScaledInstance((int) iconScale,
						(int) iconScale, Image.SCALE_FAST);
				trIcon = new ImageIcon(trImage);
			} catch (Exception e) {
			}

			trPic[numOfComs[3]] = new JLabel(trIcon);

			graph.add(trPic[numOfComs[3]]);
			trPic[numOfComs[3]].setBounds(10, 15, (int) iconScale,
					(int) iconScale);

			setMove(trPic[numOfComs[3]], "trans", orderOfComs[3]);

			validate();
			trPoint[orderOfComs[3]] = new Point(10, 15);
			orderOfComs[3]++;
			tableMode[3].addRow(new Object[] { "��ѹ��" + orderOfComs[3], "", "",
					"0.01", "0.06", "Y-Y" });
			bus1.addItem("��ѹ��" + orderOfComs[3] + "ԭ��");
			bus1.addItem("��ѹ��" + orderOfComs[3] + "����");
			bus2.addItem("��ѹ��" + orderOfComs[3] + "ԭ��");
			bus2.addItem("��ѹ��" + orderOfComs[3] + "����");
			numOfComs[3]++;

		} else if (b5.isSelected()) {
			// wiring();
			linePoint[0][orderOfComs[0]] = new Point(-100, -100);
			linePoint[1][orderOfComs[0]] = new Point(-100, -100);
			orderOfComs[4]++;
			tableMode[4].addRow(new Object[] { "��·" + orderOfComs[4] });
			numOfComs[4]++;

			numOfLineNodes = 0;
			createLocationData();

		} else {
			JOptionPane.showMessageDialog(this, "��ѡ��һ��Ԫ��", "����",
					JOptionPane.WARNING_MESSAGE);
		}

		if (sig) {
			setNameLabel();

		}
	}

	private void delComFromGraph() {
		isRunned = false;
		boolean sig = false;
		if (showName.getText().equals("��������")) {
			setNameLabel();
			sig = true;

		}

		if (toDelName.equals("gen")) {

			graph.remove(genPic[toDelNum]);
			genPoint[toDelNum].setLocation(-100, -100);

			for (int i = 0; i < tableMode[0].getRowCount(); i++) {
				int j = toDelNum + 1;
				if (tableMode[0].getValueAt(i, 0).toString().equals("�����" + j)) {

					tableMode[0].removeRow(i);
					for (int m = 3; m < 5; m++) {
						for (int n = 0; n < numOfComs[4]; n++) {
							if (tableMode[4].getValueAt(n, m).toString() != null
									&& tableMode[4].getValueAt(n, m).toString()
											.equals("�����" + j)) {
								tableMode[4].setValueAt("", n, m);
							}
						}
					}
					bus1.removeItem("�����" + j);
					bus2.removeItem("�����" + j);
				}

			}
			numOfComs[0]--;
	//		nameOfCom.setText("���ƣ�");
			toDelName = "";
			graph.repaint();

		} else if (toDelName.equals("bus")) {

			graph.remove(busPic[toDelNum]);
			busPoint[toDelNum].setLocation(-100, -100);

			for (int i = 0; i < tableMode[1].getRowCount(); i++) {
				int j = toDelNum + 1;
				if (tableMode[1].getValueAt(i, 0).toString().equals("�ڵ�" + j)) {
					for (int m = 3; m < 5; m++) {
						for (int n = 0; n < numOfComs[4]; n++) {
							if (tableMode[4].getValueAt(n, m).toString() != null
									&& tableMode[4].getValueAt(n, m).toString()
											.equals("�ڵ�" + j)) {
								tableMode[4].setValueAt("", n, m);
							}
						}
					}
					bus1.removeItem("�ڵ�" + j);
					bus2.removeItem("�ڵ�" + j);
					tableMode[1].removeRow(i);
				}

			}
			numOfComs[1]--;
	//		nameOfCom.setText("���ƣ�");
			toDelName = "";
			graph.repaint();

		} else if (toDelName.equals("load")) {

			graph.remove(loadPic[toDelNum]);
			loadPoint[toDelNum].setLocation(-100, -100);

			for (int i = 0; i < tableMode[2].getRowCount(); i++) {
				int j = toDelNum + 1;
				if (tableMode[2].getValueAt(i, 0).toString().equals("����" + j)) {
					for (int m = 3; m < 5; m++) {
						for (int n = 0; n < numOfComs[4]; n++) {
							if (tableMode[4].getValueAt(n, m).toString() != null
									&& tableMode[4].getValueAt(n, m).toString()
											.equals("����" + j)) {
								tableMode[4].setValueAt("", n, m);
							}
						}
					}
					bus1.removeItem("����" + j);
					bus2.removeItem("����" + j);
					tableMode[2].removeRow(i);
				}
			}
			numOfComs[2]--;
		//	nameOfCom.setText("���ƣ�");
			toDelName = "";
			graph.repaint();

		} else if (toDelName.equals("trans")) {

			graph.remove(trPic[toDelNum]);
			trPoint[toDelNum].setLocation(-100, -100);

			for (int i = 0; i < tableMode[3].getRowCount(); i++) {
				int j = toDelNum + 1;
				if (tableMode[3].getValueAt(i, 0).toString().equals("��ѹ��" + j)) {

					for (int m = 3; m < 5; m++) {
						for (int n = 0; n < numOfComs[4]; n++) {
							if (tableMode[4].getValueAt(n, m).toString() != null
									&& (tableMode[4].getValueAt(n, m)
											.toString()
											.equals("��ѹ��" + j + "ԭ��") || tableMode[4]
											.getValueAt(n, m).toString()
											.equals("��ѹ��" + j + "����"))) {
								tableMode[4].setValueAt("", n, m);
							}
						}
					}

					bus1.removeItem("��ѹ��" + j + "ԭ��");
					bus1.removeItem("��ѹ��" + j + "����");
					bus2.removeItem("��ѹ��" + j + "ԭ��");
					bus2.removeItem("��ѹ��" + j + "����");
					tableMode[3].removeRow(i);
				}
			}
			numOfComs[3]--;
	//		nameOfCom.setText("���ƣ�");
			toDelName = "";
			graph.repaint();

		} else if (toDelName.equals("line")) {

			linePoint[0][toDelNum] = new Point(-100, -100);
			linePoint[1][toDelNum] = new Point(-100, -100);
			for (int i = 0; i < tableMode[4].getRowCount(); i++) {

				if (tableMode[4].getValueAt(i, 0).toString().equals(
						"��·" + (toDelNum + 1))) {

					tableMode[4].removeRow(i);
				}
			}
			numOfComs[4]--;
	//		nameOfCom.setText("���ƣ�");

			toDelName = "";

		} else {
			JOptionPane.showMessageDialog(this, "����ѡ��һ��Ԫ��", "����",
					JOptionPane.WARNING_MESSAGE);
		}

		wiring();

		if (sig) {
			setNameLabel();

		}
	}

	private void setMove(JLabel l, String s, int i) {

		DragPicListener listener = new DragPicListener(l, s, i); // ����¼�����
		l.addMouseListener(listener); // ���ӱ�ǩ������¼�����
		l.addMouseMotionListener(listener);

	}

	private class pic extends JPanel {

		private int x1[], x2[], y1[], y2[], num;

		public void paint(Graphics g) {

			super.paint(g);
			if (!isCleaning) {
				for (int i = 0; i < num; i++) {
					if (x1[i] != -100 && y1[i] != -100 && x2[i] != -100
							&& y2[i] != -100)

					{
						g.setColor(Color.black);
						g.drawLine(x1[i], y1[i], x2[i], y2[i]);
					}
				}
			}

			else {
				g.setColor(Color.white);
				g.fillRect(0, 0, graph.getWidth(), graph.getHeight());
			}
		}

		public void drawLine(int x1[], int y1[], int x2[], int y2[], int num) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.num = num;
			repaint();
		}

		public void clear() {
			for (int i = 0; i < num; i++) {
				x1[i] = -1;
				x2[i] = -1;
				y1[i] = -1;
				y2[i] = -1;
			}
			repaint();
		}

	}

	private void setComInfo(String s, int i) {
		try {
			comInfo.setText("");
			if (s.equals("gen")) {
				for (int j = 0; j < numOfComs[0]; j++) {
					int k = i + 1;
					if (tableMode[0].getValueAt(j, 0).toString().equals(
							"�����" + k)) {
						for (int m = 0; m < numOfColumns[0]; m++) {
							if (m != numOfColumns[0] - 1)
								comInfo.append(tableMode[0].getColumnName(m)
										+ "��" + tableMode[0].getValueAt(j, m)
										+ "\n");
							else
								comInfo.append(tableMode[0].getColumnName(m)
										+ "��" + tableMode[0].getValueAt(j, m));
						}
					}
				}
			} else if (s.equals("bus")) {
				for (int j = 0; j < numOfComs[1]; j++) {
					int k = i + 1;
					if (tableMode[1].getValueAt(j, 0).toString().equals(
							"�ڵ�" + k)) {
						for (int m = 0; m < numOfColumns[1]; m++) {
							if (m != numOfColumns[1] - 1)
								comInfo.append(tableMode[1].getColumnName(m)
										+ "��" + tableMode[1].getValueAt(j, m)
										+ "\n");
							else
								comInfo.append(tableMode[1].getColumnName(m)
										+ "��" + tableMode[1].getValueAt(j, m));
						}
					}
				}
			} else if (s.equals("load")) {
				for (int j = 0; j < numOfComs[2]; j++) {
					int k = i + 1;
					if (tableMode[2].getValueAt(j, 0).toString().equals(
							"����" + k)) {
						for (int m = 0; m < numOfColumns[2]; m++) {
							if (m != numOfColumns[2] - 1)
								comInfo.append(tableMode[2].getColumnName(m)
										+ "��" + tableMode[2].getValueAt(j, m)
										+ "\n");
							else
								comInfo.append(tableMode[2].getColumnName(m)
										+ "��" + tableMode[2].getValueAt(j, m));
						}
					}
				}
			} else if (s.equals("trans")) {
				for (int j = 0; j < numOfComs[3]; j++) {
					int k = i + 1;
					if (tableMode[3].getValueAt(j, 0).toString().equals(
							"��ѹ��" + k)) {
						for (int m = 0; m < numOfColumns[3]; m++) {
							if (m != numOfColumns[3] - 1)
								comInfo.append(tableMode[3].getColumnName(m)
										+ "��" + tableMode[3].getValueAt(j, m)
										+ "\n");
							else
								comInfo.append(tableMode[3].getColumnName(m)
										+ "��" + tableMode[3].getValueAt(j, m));
						}
					}
				}
			} else if (s.equals("line")) {
				for (int j = 0; j < numOfComs[4]; j++) {
					int k = i + 1;
					if (tableMode[4].getValueAt(j, 0).toString().equals(
							"��·" + k)) {
						for (int m = 0; m < numOfColumns[4]; m++) {
							if (m != numOfColumns[4] - 1)
								comInfo.append(tableMode[4].getColumnName(m)
										+ "��" + tableMode[4].getValueAt(j, m)
										+ "\n");
							else
								comInfo.append(tableMode[4].getColumnName(m)
										+ "��" + tableMode[4].getValueAt(j, m));
						}
					}
				}
			} else {
			}
		} catch (ArrayIndexOutOfBoundsException AIOFBE) {
		}
	}

	private void setSelectedRow(String s, int i) {

		if (s.equals("gen")) {
			for (int j = 0; j < tableMode[0].getRowCount(); j++) {
				if (tableMode[0].getValueAt(j, 0).toString().equals(
						"�����" + (i + 1))) {
					tabbedPane.setSelectedIndex(0);
					table[0].setRowSelectionInterval(j, j);
				}
			}
		} else if (s.equals("bus")) {
			for (int j = 0; j < tableMode[1].getRowCount(); j++) {
				if (tableMode[1].getValueAt(j, 0).toString().equals(
						"�ڵ�" + (i + 1))) {
					tabbedPane.setSelectedIndex(1);
					table[1].setRowSelectionInterval(j, j);
				}
			}
		} else if (s.equals("load")) {
			for (int j = 0; j < tableMode[2].getRowCount(); j++) {
				if (tableMode[2].getValueAt(j, 0).toString().equals(
						"����" + (i + 1))) {
					tabbedPane.setSelectedIndex(2);
					table[2].setRowSelectionInterval(j, j);
				}
			}
		} else if (s.equals("trans")) {
			for (int j = 0; j < tableMode[3].getRowCount(); j++) {
				if (tableMode[3].getValueAt(j, 0).toString().equals(
						"��ѹ��" + (i + 1))) {
					tabbedPane.setSelectedIndex(3);
					table[3].setRowSelectionInterval(j, j);
				}
			}
		} else if (s.equals("line")) {
			for (int j = 0; j < tableMode[4].getRowCount(); j++) {
				if (tableMode[4].getValueAt(j, 0).toString().equals(
						"��·" + (i + 1))) {
					tabbedPane.setSelectedIndex(4);
					table[4].setRowSelectionInterval(j, j);
				}
			}
		}

	}

	class DragPicListener implements MouseInputListener {

		public DragPicListener(JLabel Pic, String s, int i) {
			this.Pic = Pic;
			this.s = s;
			this.i = i;
			if (s.equals("gen"))
				cnName = "�����";
			else if (s.equals("bus"))
				cnName = "�ڵ�";
			else if (s.equals("load"))
				cnName = "����";
			else if (s.equals("trans"))
				cnName = "��ѹ��";
		}

		String s;
		int i;
		JLabel Pic;
		Point point = new Point(0, 0); // �����
		String cnName;

		public void mousePressed(MouseEvent e) {

			setSelectedRow(s, i);
			setComInfo(s, i);
			if (s.equals("trans")) {
				int j = i + 1;

	//			nameOfCom.setText("���ƣ���ѹ��" + j);
				toDelName = "trans";
				toDelNum = i;
			}
			if (s.equals("gen")) {
				int j = i + 1;
		//		nameOfCom.setText("���ƣ������" + j);
				toDelName = "gen";
				toDelNum = i;
			}
			if (s.equals("bus")) {
				int j = i + 1;
	//			nameOfCom.setText("���ƣ��ڵ�" + j);
				toDelName = "bus";
				toDelNum = i;
			}
			if (s.equals("load")) {
				int j = i + 1;
	//			nameOfCom.setText("���ƣ�����" + j);
				toDelName = "load";
				toDelNum = i;
			}

			point = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic
					.getParent()); // �õ���ǰ�����

			int tempNum = i;
			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher("");

			if (numOfLineNodes == 0) {

				if (s.equals("gen")) {
					tableMode[4].setValueAt("�����" + (tempNum + 1),
							numOfComs[4] - 1, 3);
					tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
				} else if (s.equals("bus")) {
					tableMode[4].setValueAt("�ڵ�" + (tempNum + 1),
							numOfComs[4] - 1, 3);
					tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
				} else if (s.equals("load")) {
					tableMode[4].setValueAt("����" + (tempNum + 1),
							numOfComs[4] - 1, 3);
					tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
				} else if (s.equals("trans")) {
					if (e.getX() < (int) offset)
						tableMode[4].setValueAt("��ѹ��" + (tempNum + 1) + "ԭ��",
								numOfComs[4] - 1, 3);

					else
						tableMode[4].setValueAt("��ѹ��" + (tempNum + 1) + "����",
								numOfComs[4] - 1, 3);
					tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
				}
				numOfLineNodes++;
				wiring();

			} else if (numOfLineNodes == 1) {
				if (s.equals("gen")) {
					tableMode[4].setValueAt("�����" + (tempNum + 1),
							numOfComs[4] - 1, 4);

				} else if (s.equals("bus")) {
					tableMode[4].setValueAt("�ڵ�" + (tempNum + 1),
							numOfComs[4] - 1, 4);

				} else if (s.equals("load")) {
					tableMode[4].setValueAt("����" + (tempNum + 1),
							numOfComs[4] - 1, 4);

				} else if (s.equals("trans")) {
					if (e.getX() < (int) offset)
						tableMode[4].setValueAt("��ѹ��" + (tempNum + 1) + "ԭ��",
								numOfComs[4] - 1, 4);

					else
						tableMode[4].setValueAt("��ѹ��" + (tempNum + 1) + "����",
								numOfComs[4] - 1, 4);

				}
				numOfLineNodes++;

				wiring();
			}
		}

		public void mouseDragged(MouseEvent e) {

			Point newPoint = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic
					.getParent()); // ת������ϵͳ
			Pic.setLocation(Pic.getX() + (newPoint.x - point.x), Pic.getY()
					+ (newPoint.y - point.y)); // ���ñ�ǩͼƬ����λ��
			point = newPoint; // ���������

			isSaved = false;

			if (s.equals("trans"))
				trPoint[i] = Pic.getLocation();
			else if (s.equals("gen"))
				genPoint[i] = Pic.getLocation();
			else if (s.equals("bus"))
				busPoint[i] = Pic.getLocation();
			else if (s.equals("load"))
				loadPoint[i] = Pic.getLocation();

			// boolean permit = true;
			//
			// for (int i = 0; i < numOfComs[4]; i++) {
			// if (tableMode[4].getValueAt(i, 3) == null
			// || tableMode[4].getValueAt(i, 3).toString().isEmpty()
			// || tableMode[4].getValueAt(i, 4) == null
			// || tableMode[4].getValueAt(i, 4).toString().isEmpty())
			// permit = false;
			// }
			//
			// if (permit)
			if (showName.getText().equals("��������")) {
				setNameLabel();
				setNameLabel();
			}

			isRunned = false;
			wiring();

			graph.repaint();
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {
			if (numOfLineNodes == 0) {
				if (s.equals("trans")) {
					if (e.getX() < (int) offset)
						comInfo.setText("����ѡ�����·���׶�\n" + "��ǰѡ��" + cnName
								+ (i + 1) + "ԭ��");
					else
						comInfo.setText("����ѡ�����·���׶�\n" + "��ǰѡ��" + cnName
								+ (i + 1) + "����");
				} else {
					comInfo.setText("����ѡ�����·���׶�\n" + "��ǰѡ��" + cnName
							+ (i + 1));
				}
			} else if (numOfLineNodes == 1) {
				if (s.equals("trans")) {
					if (e.getX() < (int) offset)
						comInfo.setText("����ѡ�����·��ĩ��\n" + "��ǰѡ��" + cnName
								+ (i + 1) + "ԭ��");
					else
						comInfo.setText("����ѡ�����·��ĩ��\n" + "��ǰѡ��" + cnName
								+ (i + 1) + "����");
				} else {
					comInfo.setText("����ѡ�����·��ĩ��\n" + "��ǰѡ��" + cnName
							+ (i + 1));
				}
				wiring();
			}

			tempLocation.x = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic
					.getParent()).x;
			tempLocation.y = SwingUtilities.convertPoint(Pic, e.getPoint(), Pic
					.getParent()).y;

		}

	}

	class GetLine implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher("");

			String lineName;
			int lineOrder = 0;

			for (int i = 0; i < numOfComs[4]; i++) {
				lineName = tableMode[4].getValueAt(i, 0).toString();
				m = p.matcher(lineName);
				while (m.find()) {
					if (!"".equals(m.group()))
						lineOrder = Integer.parseInt(m.group());
				}
				if ((linePoint[0][lineOrder - 1].x - linePoint[1][lineOrder - 1].x) == 0) {
					if (e.getY() > Math.min(linePoint[0][lineOrder - 1].y,
							linePoint[1][lineOrder - 1].y)
							&& e.getY() < Math.max(
									linePoint[0][lineOrder - 1].y,
									linePoint[1][lineOrder - 1].y)
							&& Math.abs(e.getX()
									- (linePoint[0][lineOrder - 1].x)) < 10) {
		//				nameOfCom.setText("���ƣ�" + lineName);
						setSelectedRow("line", lineOrder - 1);
						setComInfo("line", lineOrder - 1);
						toDelName = "line";
						toDelNum = lineOrder;
						break;
					}
				} else {
					float dy = linePoint[0][lineOrder - 1].y
							- linePoint[1][lineOrder - 1].y;
					float dx = linePoint[0][lineOrder - 1].x
							- linePoint[1][lineOrder - 1].x;
					float dx0 = e.getX() - linePoint[0][lineOrder - 1].x;
					int ycompare = (int) (dx0 * dy / dx)
							+ linePoint[0][lineOrder - 1].y;
					if (Math.abs(ycompare - e.getY()) < 20
							&& e.getX() > Math.min(
									linePoint[0][lineOrder - 1].x,
									linePoint[1][lineOrder - 1].x)
							&& e.getX() < Math.max(
									linePoint[0][lineOrder - 1].x,
									linePoint[1][lineOrder - 1].x)) {
		//				nameOfCom.setText("���ƣ�" + lineName);
						toDelName = "line";
						toDelNum = lineOrder - 1;

						setSelectedRow("line", lineOrder - 1);
						setComInfo("line", lineOrder - 1);
						break;
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private void initLine() {
		overhead_line_conductor.addMap("LGJ-240/30_single", "1.08 cm",
				"0.1181 Ohm/km");
		overhead_line_conductor.addMap("LGJ-240/40_single", "1.083 cm",
				"0.1209 Ohm/km");
		overhead_line_conductor.addMap("LGJ-300/25_single", "1.188 cm",
				"0.09433 Ohm/km");
		overhead_line_conductor.addMap("LGJ-300/40_single", "1.197 cm",
				"0.09614 Ohm/km");
		overhead_line_conductor.addMap("LGJ-400/35_single", "1.341 cm",
				"0.07389 Ohm/km");
		overhead_line_conductor.addMap("LGJ-400/50_single", "1.3815 cm",
				"0.07232 Ohm/km");
		overhead_line_conductor.addMap("neutral_line", "0.2815 in ",
				"0.592 Ohm/mile");

		overhead_line_conductor.addMap("overhead_line_conductor100", "0.0244 in",
				"0.306 Ohm/mile");
		overhead_line_conductor.addMap("overhead_line_conductor101", "0.00814 in",
				"0.592 Ohm/mile");

		line_configuration.addMap("LGJ-240/30", "LGJ-240/30_single",
				"LGJ-240/30_single", "LGJ-240/30_single", "neutral_line",
				"universel_line_spacing");
		line_configuration.addMap("LGJ-240/40", "LGJ-240/40_single",
				"LGJ-240/40_single", "LGJ-240/40_single", "neutral_line",
				"universel_line_spacing");
		line_configuration.addMap("LGJ-300/25", "LGJ-300/25_single",
				"LGJ-300/25_single", "LGJ-300/25_single", "neutral_line",
				"universel_line_spacing");
		line_configuration.addMap("LGJ-300/40", "LGJ-300/40_single",
				"LGJ-300/40_single", "LGJ-300/40_single", "neutral_line",
				"universel_line_spacing");
		line_configuration.addMap("LGJ-400/35", "LGJ-400/35_single",
				"LGJ-400/35_single", "LGJ-400/35_single", "neutral_line",
				"universel_line_spacing");
		line_configuration.addMap("LGJ-400/50", "LGJ-400/50_single",
				"LGJ-400/50_single", "LGJ-400/50_single", "neutral_line",
				"universel_line_spacing");

		line_configuration.addMap("testLine", "overhead_line_conductor100",
				"overhead_line_conductor100", "overhead_line_conductor100",
				"overhead_line_conductor101", "universel_line_spacing");

//		line_spacing.addMap("universel_line_spacing", "2.5", "4.5", "7.0",
//				"5.656854", "4.272002", "5.0");
		
		line_spacing.addMap("universel_line_spacing", "6 m", "6 m", "12 m",
				"32 m", "26 m", "20 m");

	}

	private class mapLine implements MouseInputListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int tempNum = 0;
			String regex = "\\d*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher("");
			if (numOfLineNodes == 0) {
				Iterator iter = nodePoint.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					int x = nodePoint.get(entry.getKey()).x;
					int y = nodePoint.get(entry.getKey()).y;
					int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
							* (e.getY() - y);
					if (dis <= (int) (offset * offset / 4.0)) {

						m = p.matcher(entry.getKey().toString());
						while (m.find()) {
							if (!"".equals(m.group()))
								tempNum = Integer.parseInt(m.group());
						}
						if (entry.getKey().toString().startsWith("generator")) {
							tableMode[4].setValueAt("�����" + (tempNum + 1),
									numOfComs[4] - 1, 3);
							tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
						} else if (entry.getKey().toString().startsWith("node")) {
							tableMode[4].setValueAt("�ڵ�" + (tempNum + 1),
									numOfComs[4] - 1, 3);
							tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
						} else if (entry.getKey().toString().startsWith(
								"transformer")) {
							if (entry.getKey().toString().contains("primary"))
								tableMode[4].setValueAt("�����" + (tempNum + 1)
										+ "ԭ��", numOfComs[4] - 1, 3);

							else
								tableMode[4].setValueAt("�����" + (tempNum + 1)
										+ "����", numOfComs[4] - 1, 3);
							tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
						}
						numOfLineNodes++;
						break;

					}

				}

				Iterator iter2 = loadPointHash.entrySet().iterator();
				while (iter2.hasNext()) {
					Map.Entry entry2 = (Map.Entry) iter2.next();
					int x = loadPointHash.get(entry2.getKey()).x;
					int y = loadPointHash.get(entry2.getKey()).y;
					int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
							* (e.getY() - y);
					if (dis <= (int) (offset * offset / 4.0)) {

						m = p.matcher(entry2.getKey().toString());
						while (m.find()) {
							if (!"".equals(m.group()))
								tempNum = Integer.parseInt(m.group());
						}
						if (entry2.getKey().toString().startsWith("load")) {
							tableMode[4].setValueAt("����" + (tempNum + 1),
									numOfComs[4] - 1, 3);
							tableMode[4].setValueAt(" ", numOfComs[4] - 1, 4);
						}
						numOfLineNodes++;
						break;
					}

				}
			} else if (numOfLineNodes == 1) {
				Iterator iter = nodePoint.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					int x = nodePoint.get(entry.getKey()).x;
					int y = nodePoint.get(entry.getKey()).y;
					int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
							* (e.getY() - y);
					if (dis <= (int) (offset * offset / 4.0)) {

						m = p.matcher(entry.getKey().toString());
						while (m.find()) {
							if (!"".equals(m.group()))
								tempNum = Integer.parseInt(m.group());
						}
						if (entry.getKey().toString().startsWith("generator")) {
							tableMode[4].setValueAt("�����" + (tempNum + 1),
									numOfComs[4] - 1, 4);

						} else if (entry.getKey().toString().startsWith("node")) {
							tableMode[4].setValueAt("�ڵ�" + (tempNum + 1),
									numOfComs[4] - 1, 4);

						} else if (entry.getKey().toString().startsWith(
								"transformer")) {
							if (entry.getKey().toString().contains("primary"))
								tableMode[4].setValueAt("�����" + (tempNum + 1)
										+ "ԭ��", numOfComs[4] - 1, 4);

							else
								tableMode[4].setValueAt("�����" + (tempNum + 1)
										+ "����", numOfComs[4] - 1, 4);

						}
						numOfLineNodes++;
						break;

					}

				}

				Iterator iter2 = loadPointHash.entrySet().iterator();
				while (iter2.hasNext()) {
					Map.Entry entry2 = (Map.Entry) iter2.next();
					int x = loadPointHash.get(entry2.getKey()).x;
					int y = loadPointHash.get(entry2.getKey()).y;
					int dis = (e.getX() - x) * (e.getX() - x) + (e.getY() - y)
							* (e.getY() - y);
					if (dis <= (int) (offset * offset / 4.0)) {

						m = p.matcher(entry2.getKey().toString());
						while (m.find()) {
							if (!"".equals(m.group()))
								tempNum = Integer.parseInt(m.group());
						}
						if (entry2.getKey().toString().startsWith("load")) {
							tableMode[4].setValueAt("����" + (tempNum + 1),
									numOfComs[4] - 1, 4);

						}
						numOfLineNodes++;
						break;
					}

				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

			if (numOfLineNodes == 1)
				wiring();
		}

	}

	private void createLocationData() {

		String regex = "\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("");
		int tempNum = 0;
		nodePoint.clear();
		for (int i = 0; i < numOfComs[0]; i++) {
			m = p.matcher(tableMode[0].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}

			nodePoint.put("generator" + tempNum, new Point(
					genPoint[tempNum - 1].x + (int) offset,
					genPoint[tempNum - 1].y + (int) offset));
		}

		for (int i = 0; i < numOfComs[1]; i++) {
			m = p.matcher(tableMode[1].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}

			nodePoint.put("node" + tempNum, new Point(busPoint[tempNum - 1].x
					+ (int) offset, busPoint[tempNum - 1].y + (int) offset));
		}

		loadPointHash.clear();
		for (int i = 0; i < numOfComs[2]; i++) {
			m = p.matcher(tableMode[2].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}

			loadPointHash.put("load" + tempNum, new Point(
					loadPoint[tempNum - 1].x + (int) offset,
					loadPoint[tempNum - 1].y + (int) offset));
		}

		for (int i = 0; i < numOfComs[3]; i++) {
			m = p.matcher(tableMode[3].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}

			nodePoint.put("transformer" + tempNum + "_primary", new Point(
					trPoint[tempNum - 1].x, trPoint[tempNum - 1].y
							+ (int) offset));

			nodePoint.put("transformer" + tempNum + "_secondary", new Point(
					trPoint[tempNum - 1].x + 2 * (int) offset,
					trPoint[tempNum - 1].y + (int) offset));

		}

		tempNum = 0;
	}

	private void createData() {

		load.loadMap.clear();
		node.nodeMap.clear();
		overhead_line.overhead_lineMap.clear();
		transformer.transformerMap.clear();
		underground_line.underground_lineMap.clear();

		load.loadResultMap.clear();
		node.nodeResultMap.clear();
		overhead_line.overhead_lineResultMap.clear();
		transformer.transformerResultMap.clear();
		underground_line.underground_lineResultMap.clear();

		frequency = Double.parseDouble(fre.getText());
		String regex = "\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("");
		int tempNum = 0;
		String triPhase = "\"ABCN\"";

		String PQ = "PQ";

		initLine();

		nodePoint.clear();
		transformer_linePoint.clear();
		loadPointHash.clear();
		overhead_linePoint.clear();
		underground_linePoint.clear();

		for (int i = 0; i < numOfComs[0]; i++) {
			m = p.matcher(tableMode[0].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}
			double tempVol = 1e3 * Double.parseDouble(tableMode[0].getValueAt(
					i, 1).toString());
			String tempType = tableMode[0].getValueAt(i, 4).toString();
			double tempDeg = Double.parseDouble(tableMode[0].getValueAt(i, 2)
					.toString());
			String volA = tempVol + (tempDeg > -1e-10 ? "+" : "") + tempDeg
					+ "d";
			String volB = tempVol + ((tempDeg - 120) > -1e-10 ? "+" : "")
					+ (tempDeg - 120) + "d";
			String volC = tempVol + ((tempDeg + 120) > -1e-10 ? "+" : "")
					+ (tempDeg + 120) + "d";

			double tempPower = 1e6 / 3 * Double.parseDouble(tableMode[0]
					.getValueAt(i, 3).toString());

			node.addMap("generator" + tempNum, triPhase, "" + tempVol, volA,
					volB, volC, tempType, "" + tempPower, "" + tempPower, ""
							+ tempPower);

			nodePoint.put("generator" + tempNum, new Point(
					genPoint[tempNum - 1].x + (int) offset,
					genPoint[tempNum - 1].y + (int) offset));
		}

		for (int i = 0; i < numOfComs[1]; i++) {
			m = p.matcher(tableMode[1].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}
			double tempVol = 1e3 * Double.parseDouble(tableMode[1].getValueAt(
					i, 1).toString());
			String tempType = tableMode[1].getValueAt(i, 3).toString();
			double tempDeg = Double.parseDouble(tableMode[1].getValueAt(i, 2)
					.toString());
			String volA = tempVol + (tempDeg > -1e-5 ? "+" : "") + tempDeg
					+ "d";
			String volB = tempVol + ((tempDeg - 120) > -1e-5 ? "+" : "")
					+ (tempDeg - 120) + "d";
			String volC = tempVol + ((tempDeg + 120) > -1e-5 ? "+" : "")
					+ (tempDeg + 120) + "d";
			double tempPower = 0;

			node.addMap("node" + tempNum, triPhase, "" + tempVol, volA, volB,
					volC, tempType, "" + tempPower, "" + tempPower, ""
							+ tempPower);

			nodePoint.put("node" + tempNum, new Point(busPoint[tempNum - 1].x
					+ (int) offset, busPoint[tempNum - 1].y + (int) offset));
		}

		for (int i = 0; i < numOfComs[2]; i++) {
			m = p.matcher(tableMode[2].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}
			double tempVol = 1e3 * Double.parseDouble(tableMode[2].getValueAt(
					i, 3).toString());
			double allPowerP = 1e6 * Double.parseDouble(tableMode[2]
					.getValueAt(i, 1).toString());
			double allPowerQ = 1e6 * Double.parseDouble(tableMode[2]
					.getValueAt(i, 2).toString());
			String sig = allPowerQ > -1e-5 ? "+" : "";
			String ABCPower = "" + allPowerP / 3 + sig + allPowerQ / 3 + "j";
			load.addMap("load" + tempNum, triPhase, "" + tempVol, "" + tempVol,
					tempVol + "-120d", tempVol + "+120d", ABCPower, ABCPower,
					ABCPower);

			loadPointHash.put("load" + tempNum, new Point(
					loadPoint[tempNum - 1].x + (int) offset,
					loadPoint[tempNum - 1].y + (int) offset));
		}

		for (int i = 0; i < numOfComs[4]; i++) {
			m = p.matcher(tableMode[4].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}
			String nodeFrom = tableMode[4].getValueAt(i, 3).toString();
			m = p.matcher(nodeFrom);
			int temp = 0;
			while (m.find()) {
				if (!"".equals(m.group()))
					temp = Integer.parseInt(m.group());
			}
			if (nodeFrom.startsWith("�����")) {
				nodeFrom = "generator" + temp;
			} else if (nodeFrom.startsWith("�ڵ�")) {
				nodeFrom = "node" + temp;
			} else if (nodeFrom.startsWith("����")) {
				nodeFrom = "load" + temp;
			} else if (nodeFrom.startsWith("��ѹ��")) {
				if (nodeFrom.split("" + temp)[1].equals("ԭ��"))
					nodeFrom = "transformer" + temp + "_primary";
				else if (nodeFrom.split("" + temp)[1].equals("����"))
					nodeFrom = "transformer" + temp + "_secondary";
				else {
					JOptionPane.showMessageDialog(this, "��·�ڵ����һ������(ԭ�߸���)",
							"����", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "��·�ڵ����һ������", "����",
						JOptionPane.WARNING_MESSAGE);
			}
			String nodeTo = tableMode[4].getValueAt(i, 4).toString();
			m = p.matcher(nodeTo);

			while (m.find()) {
				if (!"".equals(m.group()))
					temp = Integer.parseInt(m.group());
			}
			if (nodeTo.startsWith("�����")) {
				nodeTo = "generator" + temp;
			} else if (nodeTo.startsWith("�ڵ�")) {
				nodeTo = "node" + temp;
			} else if (nodeTo.startsWith("����")) {
				nodeTo = "load" + temp;
			} else if (nodeTo.startsWith("��ѹ��")) {
				if (nodeTo.split("" + temp)[1].equals("ԭ��"))
					nodeTo = "transformer" + temp + "_primary";
				else if (nodeTo.split("" + temp)[1].equals("����"))
					nodeTo = "transformer" + temp + "_secondary";
				else {
					JOptionPane.showMessageDialog(this, "��·�ڵ����һ������(ԭ�߸���)",
							"����", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "��·�ڵ����һ������", "����",
						JOptionPane.WARNING_MESSAGE);
			}
			String length = tableMode[4].getValueAt(i, 2).toString();
			String lineType = tableMode[4].getValueAt(i, 1).toString();

			overhead_line.addMap("line" + tempNum, triPhase, nodeFrom, nodeTo,
					length + "km", lineType);
			overhead_linePoint.put("line" + tempNum, new Point[] {
					linePoint[0][tempNum - 1], linePoint[1][tempNum - 1] });

		}

		for (int i = 0; i < numOfComs[3]; i++) {
			m = p.matcher(tableMode[3].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group()))
					tempNum = Integer.parseInt(m.group());
			}
			String typeOfConn;
			String temp = tableMode[3].getValueAt(i, 5).toString();
			if (temp.equals("Y-Y"))
				typeOfConn = "1";
			else if (temp.equals("delta-delta"))
				typeOfConn = "2";
			else
				typeOfConn = "3";

			double powerRating = 1e3 * Double.parseDouble(tableMode[3]
					.getValueAt(i, 6).toString());
			double primaryVol = 1e3 * Double.parseDouble(tableMode[3]
					.getValueAt(i, 1).toString());
			double secondVol = 1e3 * Double.parseDouble(tableMode[3]
					.getValueAt(i, 2).toString());
			String resistance = tableMode[3].getValueAt(i, 3).toString();
			String reactance = tableMode[3].getValueAt(i, 4).toString();

			String primaryVolTest = "";
			String secondVolTest = "";

			Iterator iter = overhead_line.overhead_lineMap.entrySet()
					.iterator();
			while (iter.hasNext()) {

				Map.Entry entry = (Map.Entry) iter.next();

				String temp1 = overhead_line.overhead_lineMap.get(
						entry.getKey()).get("from");
				String temp2 = overhead_line.overhead_lineMap.get(
						entry.getKey()).get("to");

				if (temp1.startsWith("transformer" + tempNum + "_primary")
						&& (temp2.startsWith("generator")
								|| temp2.startsWith("node") || temp2
								.startsWith("load"))) {
					if (temp2.startsWith("generator")
							|| temp2.startsWith("node")) {
						primaryVolTest = node.nodeMap.get(temp2).get(
								"nominal_voltage");

					} else {
						primaryVolTest = load.loadMap.get(temp2).get(
								"nominal_voltage");
					}

				}

				if (temp2.startsWith("transformer" + tempNum + "_primary")
						&& (temp1.startsWith("generator")
								|| temp1.startsWith("node") || temp1
								.startsWith("load"))) {
					if (temp1.startsWith("generator")
							|| temp1.startsWith("node")) {
						primaryVolTest = node.nodeMap.get(temp1).get(
								"nominal_voltage");

					} else {
						primaryVolTest = load.loadMap.get(temp1).get(
								"nominal_voltage");
					}

				}

				if (temp1.startsWith("transformer" + tempNum + "_secondary")
						&& (temp2.startsWith("generator")
								|| temp2.startsWith("node") || temp2
								.startsWith("load"))) {
					if (temp2.startsWith("generator")
							|| temp2.startsWith("node")) {
						secondVolTest = node.nodeMap.get(temp2).get(
								"nominal_voltage");

					} else {
						secondVolTest = load.loadMap.get(temp2).get(
								"nominal_voltage");
					}

				}

				if (temp2.startsWith("transformer" + tempNum + "_secondary")
						&& (temp1.startsWith("generator")
								|| temp1.startsWith("node") || temp1
								.startsWith("load"))) {
					if (temp1.startsWith("generator")
							|| temp1.startsWith("node")) {
						secondVolTest = node.nodeMap.get(temp1).get(
								"nominal_voltage");

					} else {
						secondVolTest = load.loadMap.get(temp1).get(
								"nominal_voltage");
					}

				}

			}

			node.addMap("transformer" + tempNum + "_primary", triPhase, ""
					+ primaryVolTest, "" + primaryVolTest, primaryVolTest
					+ "-120d", primaryVolTest + "+120d", PQ, "", "", "");

			nodePoint.put("transformer" + tempNum + "_primary", new Point(
					trPoint[tempNum - 1].x, trPoint[tempNum - 1].y
							+ (int) offset));

			node.addMap("transformer" + tempNum + "_secondary", triPhase, ""
					+ secondVolTest, "" + secondVolTest, secondVolTest
					+ "-120d", secondVolTest + "+120d", PQ, "", "", "");

			nodePoint.put("transformer" + tempNum + "_secondary", new Point(
					trPoint[tempNum - 1].x + 2 * (int) offset,
					trPoint[tempNum - 1].y + (int) offset));

			transformer_linePoint.put("transformer" + tempNum, new Point[] {
					new Point(trPoint[tempNum - 1].x, trPoint[tempNum - 1].y
							+ (int) offset),
					new Point(trPoint[tempNum - 1].x + 2 * (int) offset,
							trPoint[tempNum - 1].y + (int) offset) });

			transformer_configuration.addMap("transformer_configuration"
					+ tempNum, typeOfConn, "0", "" + powerRating, ""
					+ primaryVol, "" + secondVol, resistance, reactance);

			transformer.addMap("transformer" + tempNum, triPhase, "transformer"
					+ tempNum + "_primary", "transformer" + tempNum
					+ "_secondary", "transformer_configuration" + tempNum);

		}

		tempNum = 0;

	}

	private void runGridlabd() {
		isCorrect = true;
		isFilled = true;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < numOfComs[i]; j++)
				for (int m = 0; m < numOfColumns[i]; m++)
					if (tableMode[i].getValueAt(j, m) == null
							|| tableMode[i].getValueAt(j, m).toString()
									.isEmpty())
						isFilled = false;

		}
		if (!isFilled) {
			JOptionPane.showMessageDialog(this, "�����Ϣ��������", "����",
					JOptionPane.WARNING_MESSAGE);
		} else {
			java.util.Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String sDateTime = sdf.format(dt);
			String fileDirectory = sDateTime + ".glm";
			String outputDirectory = sDateTime + ".xml";
			FileWrite.WriteAll(fileDirectory);
			String commandStr = "gridlabd " + fileDirectory + " --output "
					+ outputDirectory;

			System.out.println("Start ...");

			ExecutorService exec = Executors.newCachedThreadPool();
			Command.testTask(exec, 2, commandStr); // ֻ�ȴ�2�룬����û���������Խ�������ֹ
			exec.shutdown();
			System.out.println("End!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Command.exeCmd(commandStr);

			XMLProcessing.XmlProcess(sDateTime);
		}

	}

	private void newGraph() {
		iconScale = sizeLevel * iconScale;
		offset = iconScale / 2;
		
		boolean sig = false;
		
		if (showName.getText().equals("��������")) {
			setNameLabel();
			sig = true;

		}
		
		
		
		String regex = "\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("");
		int tempNum = 0;
		for (int i = 0; i < numOfComs[0]; i++) {
			m = p.matcher(tableMode[0].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group())) {
					tempNum = Integer.parseInt(m.group());
					ImageIcon icon = new ImageIcon(this.getClass().getResource(
							"Picture/gen.png"));

					try {
						Image image = icon.getImage();
						image = image.getScaledInstance((int) iconScale,
								(int) iconScale, Image.SCALE_FAST);
						icon = new ImageIcon(image);
					} catch (Exception e) {
					}

					graph.remove(genPic[tempNum - 1]);
					genPic[tempNum - 1] = new JLabel(icon);

					int tempx = genPoint[tempNum - 1].x, tempy = genPoint[tempNum - 1].y;
					tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
							* sizeLevel);
					tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
							* sizeLevel);

					// tempx=(int)((double)tempx*sizeLevel);
					// tempy=(int)((double)tempy*sizeLevel);

					genPoint[tempNum - 1] = new Point(tempx, tempy);

					genPic[tempNum - 1].setBounds(genPoint[tempNum - 1].x,
							genPoint[tempNum - 1].y, (int) iconScale,
							(int) iconScale);
					graph.add(genPic[tempNum - 1]);
					setMove(genPic[tempNum - 1], "gen", tempNum - 1);
				}
			}
		}

		for (int i = 0; i < numOfComs[1]; i++) {
			m = p.matcher(tableMode[1].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group())) {
					tempNum = Integer.parseInt(m.group());
					ImageIcon icon = new ImageIcon(this.getClass().getResource(
							"Picture/bus.png"));

					try {
						Image image = icon.getImage();
						image = image.getScaledInstance((int) iconScale,
								(int) iconScale, Image.SCALE_FAST);
						icon = new ImageIcon(image);
					} catch (Exception e) {
					}

					graph.remove(busPic[tempNum - 1]);
					busPic[tempNum - 1] = new JLabel(icon);

					int tempx = busPoint[tempNum - 1].x, tempy = busPoint[tempNum - 1].y;
					tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
							* sizeLevel);
					tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
							* sizeLevel);

					// tempx=(int)((double)tempx*sizeLevel);
					// tempy=(int)((double)tempy*sizeLevel);

					busPoint[tempNum - 1] = new Point(tempx, tempy);

					busPic[tempNum - 1].setBounds(busPoint[tempNum - 1].x,
							busPoint[tempNum - 1].y, (int) iconScale,
							(int) iconScale);
					graph.add(busPic[tempNum - 1]);
					setMove(busPic[tempNum - 1], "bus", tempNum - 1);
				}
			}
		}

		for (int i = 0; i < numOfComs[2]; i++) {
			m = p.matcher(tableMode[2].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group())) {
					tempNum = Integer.parseInt(m.group());
					ImageIcon icon = new ImageIcon(this.getClass().getResource(
							"Picture/load.png"));

					try {
						Image image = icon.getImage();
						image = image.getScaledInstance((int) iconScale,
								(int) iconScale, Image.SCALE_FAST);
						icon = new ImageIcon(image);
					} catch (Exception e) {
					}

					graph.remove(loadPic[tempNum - 1]);
					loadPic[tempNum - 1] = new JLabel(icon);

					int tempx = loadPoint[tempNum - 1].x, tempy = loadPoint[tempNum - 1].y;
					tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
							* sizeLevel);
					tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
							* sizeLevel);

					// tempx=(int)((double)tempx*sizeLevel);
					// tempy=(int)((double)tempy*sizeLevel);

					loadPoint[tempNum - 1] = new Point(tempx, tempy);

					loadPic[tempNum - 1].setBounds(loadPoint[tempNum - 1].x,
							loadPoint[tempNum - 1].y, (int) iconScale,
							(int) iconScale);
					graph.add(loadPic[tempNum - 1]);
					setMove(loadPic[tempNum - 1], "load", tempNum - 1);
				}
			}
		}

		for (int i = 0; i < numOfComs[3]; i++) {
			m = p.matcher(tableMode[3].getValueAt(i, 0).toString());
			while (m.find()) {
				if (!"".equals(m.group())) {
					tempNum = Integer.parseInt(m.group());
					ImageIcon icon = new ImageIcon(this.getClass().getResource(
							"Picture/trans.png"));

					try {
						Image image = icon.getImage();
						image = image.getScaledInstance((int) iconScale,
								(int) iconScale, Image.SCALE_FAST);
						icon = new ImageIcon(image);
					} catch (Exception e) {
					}

					graph.remove(trPic[tempNum - 1]);
					trPic[tempNum - 1] = new JLabel(icon);

					int tempx = trPoint[tempNum - 1].x, tempy = trPoint[tempNum - 1].y;
					tempx = (int) ((double) (tempLocation.x) + (double) (tempx - tempLocation.x)
							* sizeLevel);
					tempy = (int) ((double) (tempLocation.y) + (double) (tempy - tempLocation.y)
							* sizeLevel);

					// tempx=(int)((double)tempx*sizeLevel);
					// tempy=(int)((double)tempy*sizeLevel);

					trPoint[tempNum - 1] = new Point(tempx, tempy);

					trPic[tempNum - 1].setBounds(trPoint[tempNum - 1].x,
							trPoint[tempNum - 1].y, (int) iconScale,
							(int) iconScale);
					graph.add(trPic[tempNum - 1]);
					setMove(trPic[tempNum - 1], "trans", tempNum - 1);
				}
			}
		}
		wiring();

		
		isRunned = false;
		
		if (sig) {
			setNameLabel();
		}
		
		graph.repaint();
		
		

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
			if (numOfLineNodes == 0) {
				comInfo.setText("����ѡ�����·���׶�\n" + "��ǰѡ����");
			} else if (numOfLineNodes == 1) {
				comInfo.setText("����ѡ�����·��ĩ��\n" + "��ǰѡ����");
				wiring();
			}

		}

	}

	private void initNewLineData() {

		newLineData[0] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "A�ർ��", "B�ർ��", "C�ർ��", "���ߵ���", "�ռ�ṹ" }) {
		};
		newLineData[1] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "����ƽ���뾶", "��λ����" }) {
		};
		newLineData[2] = new DefaultTableModel(new Object[][] {}, new String[] {
				"����", "AB���", "BC���", "AC���", "AN���", "BN���", "CN���" }) {
		};
	}

	JLabel [] tempLabel=new JLabel[4*maxComs];
	int numOfLabel=0;
	
	private void setNameLabel() {
		String regex = "\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("");
		
		int tempNum = 0;
		if (showName.getText().equals("��ʾ����")) {
			showName.setText("��������");
			for (int i = 0; i < numOfComs[0]; i++) {

				String tempName = tableMode[0].getValueAt(i, 0).toString();
				m = p.matcher(tempName);
				while (m.find()) {
					if (!"".equals(m.group()))
						tempNum = Integer.parseInt(m.group());
				}
				tempLabel[numOfLabel] = new JLabel();
				Point tempPoint = genPoint[tempNum - 1];
				int x = tempPoint.x;// -(int)(iconScale);
				int y = tempPoint.y - (int) (iconScale / 2);
				int w = (int) (iconScale);
				int h = (int) (iconScale / 2);
				int font = (int) (iconScale / 3.5);
				tempLabel[numOfLabel].setBounds(x, y, w, h);
				tempLabel[numOfLabel].setFont(new Font("����", Font.PLAIN, font));
				tempLabel[numOfLabel].setText(tempName);
				graph.add(tempLabel[numOfLabel]);
				numOfLabel++;
			}

			for (int i = 0; i < numOfComs[1]; i++) {

				String tempName = tableMode[1].getValueAt(i, 0).toString();
				m = p.matcher(tempName);
				while (m.find()) {
					if (!"".equals(m.group()))
						tempNum = Integer.parseInt(m.group());
				}
				tempLabel[numOfLabel] = new JLabel();
				Point tempPoint = busPoint[tempNum - 1];
				int x = tempPoint.x;// -(int)(iconScale);
				int y = tempPoint.y - (int) (iconScale / 2);
				int w = (int) (iconScale);
				int h = (int) (iconScale / 2);
				int font = (int) (iconScale / 3.5);
				tempLabel[numOfLabel].setBounds(x, y, w, h);
				tempLabel[numOfLabel].setFont(new Font("����", Font.PLAIN, font));
				tempLabel[numOfLabel].setText(tempName);
				graph.add(tempLabel[numOfLabel]);
				numOfLabel++;
			}
			for (int i = 0; i < numOfComs[2]; i++) {

				String tempName = tableMode[2].getValueAt(i, 0).toString();
				m = p.matcher(tempName);
				while (m.find()) {
					if (!"".equals(m.group()))
						tempNum = Integer.parseInt(m.group());
				}
				tempLabel[numOfLabel] = new JLabel();
				Point tempPoint = loadPoint[tempNum - 1];
				int x = tempPoint.x;// -(int)(iconScale);
				int y = tempPoint.y - (int) (iconScale / 2);
				int w = (int) (iconScale);
				int h = (int) (iconScale / 2);
				int font = (int) (iconScale / 3.5);
				tempLabel[numOfLabel].setBounds(x, y, w, h);
				tempLabel[numOfLabel].setFont(new Font("����", Font.PLAIN, font));
				tempLabel[numOfLabel].setText(tempName);
				graph.add(tempLabel[numOfLabel]);
				numOfLabel++;
			}

			for (int i = 0; i < numOfComs[3]; i++) {

				String tempName = tableMode[3].getValueAt(i, 0).toString();
				m = p.matcher(tempName);
				while (m.find()) {
					if (!"".equals(m.group()))
						tempNum = Integer.parseInt(m.group());
				}
				tempLabel[numOfLabel] = new JLabel();
				Point tempPoint = trPoint[tempNum - 1];
				int x = tempPoint.x;// -(int)(iconScale);
				int y = tempPoint.y - (int) (iconScale / 2);
				int w = (int) (iconScale);
				int h = (int) (iconScale / 2);
				int font = (int) (iconScale / 3.5);
				tempLabel[numOfLabel].setBounds(x, y, w, h);
				tempLabel[numOfLabel].setFont(new Font("����", Font.PLAIN, font));
				tempLabel[numOfLabel].setText(tempName);
				graph.add(tempLabel[numOfLabel]);
				numOfLabel++;
			}

			for (int i = 0; i < numOfComs[4]; i++) {

				String tempName = tableMode[4].getValueAt(i, 0).toString();
				m = p.matcher(tempName);
				while (m.find()) {
					if (!"".equals(m.group()))
						tempNum = Integer.parseInt(m.group());
				}
				tempLabel[numOfLabel] = new JLabel();
				Point tempPoint1 = linePoint[0][tempNum - 1];
				Point tempPoint2 = linePoint[1][tempNum - 1];
				int x = (tempPoint1.x + tempPoint2.x) / 2
						- (int) (iconScale / 2);// -(int)(iconScale);
				int y = (tempPoint1.y + tempPoint2.y) / 2
						- (int) (iconScale / 2);
				int w = (int) (iconScale);
				int h = (int) (iconScale / 2);
				int font = (int) (iconScale / 3.5);
				tempLabel[numOfLabel].setBounds(x, y, w, h);
				tempLabel[numOfLabel].setFont(new Font("����", Font.PLAIN, font));
				tempLabel[numOfLabel].setText(tempName);
				graph.add(tempLabel[numOfLabel]);
				numOfLabel++;
			}

		} else {
			showName.setText("��ʾ����");
			
			for (int i = 0; i < numOfLabel; i++) {
					graph.remove(tempLabel[i]);
				
			}

			numOfLabel=0;
		}

	}

	public static void main(String[] args) {
		setCase = new SetCase();
	}

	private JButton showName;

	public static boolean isRunned;

	public boolean isFilled;
	public boolean isCleaning;
	private boolean isSaved;
	public static SetCase setCase;
	private pic graph;

	public static boolean isCorrect;

	private int[] numOfColumns;
	public static int[] numOfComs;
	public static int[] orderOfComs;
	private static int maxComs = 100;

	private JLabel[] trPic;
	private JLabel[] loadPic;
	private JLabel[] genPic;
	private JLabel[] busPic;
	private Point[] trPoint;
	private Point[] loadPoint;
	private Point[] genPoint;
	private Point[] busPoint;
	private Point[][] linePoint;

	private JTable[] table;

	private JRadioButton b1, b2, b3, b4, b5;
	private JTabbedPane tabbedPane;

	//private JLabel nameOfCom;
	private String toDelName;
	private int toDelNum;

	public static JComboBox bus1, bus2;

	private JTextArea comInfo;
	private JPanel[] p;

	private Color freshGrey;
	private DefaultTableModel[] tableMode;

	public static HashMap<String, Point> nodePoint;
	public static HashMap<String, Point> loadPointHash;
	public static HashMap<String, Point[]> overhead_linePoint;
	public static HashMap<String, Point[]> underground_linePoint;
	public static HashMap<String, Point[]> transformer_linePoint;

	private String token = "fhisdufhiuwe";

	public static Dimension screenSize, frameSize, westPanelSize, graphSize;

	private static double iconScale;

	private double sizeLevel;

	private Point tempLocation = new Point();

	private static double offset;

	public static double frequency = 50;
	private int numOfLineNodes = 2;

	static DefaultTableModel[] mainTableModel;

	static DefaultTableModel[] newLineData = new DefaultTableModel[3];

	static int numOfLine[] = new int[] { 7, 9, 1 };
	private JTextField fre;
	public static JComboBox typeOfLine;
}
