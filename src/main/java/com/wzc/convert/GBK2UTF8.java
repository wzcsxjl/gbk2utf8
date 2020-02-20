package com.wzc.convert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class GBK2UTF8 {
	
	public static void main(String[] args) {
		GBK2UTF8.readfile("D:\\eclipse-workspace\\HadoopDemo");
	}

	public static void readfile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.isDirectory()) {
				transferCharset(file);
			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filePath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
							transferCharset(readfile);
					} else if (readfile.isDirectory()) {
						if(!"excel,jdbc,nosql,spring".contains( filelist[i])){
							readfile(filePath + "\\" + filelist[i]);
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void transferCharset(File file){
		try {
			String charset=codeString(file);
			if("GBK".equals(codeString(file))){
				System.out.println(file.getAbsolutePath()+"------------"+charset);
				String content = getFileContentWithCharsetName(file, "GBK");
				saveFileWithCharsetName(file, "UTF-8", content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void saveFileWithCharsetName(File file, String charsetName, String content)
			throws Exception {
		FileOutputStream fileOut = new FileOutputStream(file);
		// CharsetDecoder
		OutputStreamWriter outWrite = new OutputStreamWriter(fileOut, Charset.forName(charsetName));
		outWrite.write(content);
		outWrite.close();
	}
	
	/**
	 * 判断文件的编码格式
	 * @param fileName :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(File fileName) throws Exception{
		BufferedInputStream bin = new BufferedInputStream(
		new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		
		switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			default:
				code = "GBK";
		}
		IOUtils.closeQuietly(bin);
		return code;
	}

	public static String getFileContentWithCharsetName(File file, String charsetName)
			throws Exception {
		FileInputStream fileIn = new FileInputStream(file);
		// CharsetDecoder
		InputStreamReader inRead = new InputStreamReader(fileIn, Charset.forName(charsetName));
		int size = (int) (file.length());
		char[] chs = new char[size];
		inRead.read(chs);
		String str = new String(chs).trim();
		inRead.close();
		return str;
	}

}
