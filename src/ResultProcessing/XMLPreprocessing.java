package ResultProcessing;

/**
 * Created by zzt_cc on 2016/6/29.
 */

import gridlabd.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class XMLPreprocessing {
	public static void XMLPreprocess(String sDateTime) {

		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							sDateTime + ".xml"))));
			StringBuffer strBuf = new StringBuffer();
			for (String tmp1 = null; (tmp1 = bufReader.readLine()) != null; tmp1 = null) {
				String tmp = new String(tmp1.toString().getBytes("UTF-8"));
				tmp = tmp.replaceAll("&", "&amp;");
				// System.out.println(tmp);
				strBuf.append(tmp);
				strBuf.append(System.getProperty("line.separator"));
			}
			bufReader.close();
			String strBuf2 = new String(strBuf.toString().getBytes("UTF-8"));
			// System.out.println(strBuf2);
			PrintWriter printWriter = new PrintWriter(sDateTime + "c.xml");
			printWriter.write(strBuf2.toString().toCharArray());
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			if (SetCase.isCorrect) {
				e.printStackTrace();
				SetCase.isCorrect = false;
				JOptionPane.showMessageDialog(SetCase.setCase, "输入数据有误！",
						"错误！", JOptionPane.WARNING_MESSAGE);
			}
		}
		if (!SetCase.isRunned) {
			JOptionPane.showMessageDialog(SetCase.setCase, "仿真程序运行成功！", "提示！",
					JOptionPane.WARNING_MESSAGE);
			SetCase.isRunned = true;
		}
	}

}