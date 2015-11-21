package com.ceb.hdqs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class CopyFileTest {
	/**
	 * 使用Buffer的java api 处理性能比较稳定
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public void copyTestUseBuffer(String srcFile, String destFile) throws IOException {
		long start = System.currentTimeMillis();
		File dest = new File(destFile);
		if (dest.exists()) {
			dest.delete();
		}
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));
			// BufferedWriter writer = new BufferedWriter(new
			// FileWriter(destFile));
			while (reader.ready()) {
				String line = reader.readLine();
				writer.write(line);
				writer.newLine();
			}

		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("copyTestUseBuffer: "+time);
	}

	/**
	 * 使用字节块来处理，小于5M的文件，处理性能最好
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public int copyUseByteBlock(String srcFile, String destFile) throws IOException {
		long start = System.currentTimeMillis();
		File dest = new File(destFile);
		if (dest.exists()) {
			dest.delete();
		}
		InputStream in = new FileInputStream(srcFile);
		OutputStream out = new FileOutputStream(dest);
		try {
			int byteCount = 0;
			byte[] buffer = new byte[8192];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			long end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("copyUseByteBlock: "+time);
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 使用NIO API
	 * 
	 * @param srcFile
	 * @param destFile
	 */

	public void copyUseNIO1(String srcFile, String destFile) {
		long start = System.currentTimeMillis();
		File dest = new File(destFile);
		if (dest.exists()) {
			dest.delete();
		}
		try {
			FileChannel infc = new FileInputStream(srcFile).getChannel();
			FileChannel outfc = new FileOutputStream(destFile).getChannel();
			outfc.transferFrom(infc, 0, infc.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("copyUseNIO1: "+time);
	}

	/**
	 * 使用NIO API
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public void copyUseNIO2(String srcFile, String destFile) throws IOException {
		long start = System.currentTimeMillis();
		File dest = new File(srcFile);
		if (dest.exists()) {
			dest.delete();
		}
		File inFile = new File(srcFile);
		File outFile = new File(destFile);
		FileChannel infc = null;
		FileChannel outfc = null;
		try {
			RandomAccessFile inRaf = new RandomAccessFile(inFile, "r");
			RandomAccessFile outRaf = new RandomAccessFile(outFile, "rw");
			infc = inRaf.getChannel();
			outfc = outRaf.getChannel();
			infc.transferTo(0, inFile.length(), outfc);
		} finally {
			if (infc != null)
				infc.close();
			if (outfc != null)
				outfc.close();
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("copyUseNIO2: "+time);
	}

	public void testCopy() throws IOException {
		copyTestUseBuffer("D:/123.xls", "D:/1.xls");
		copyUseByteBlock("D:/123.xls", "D:/2.xls");
		copyUseNIO1("D:/123.xls", "D:/3.xls");
		copyUseNIO2("D:/123.xls", "D:/4.xls");
	}

	public static void main(String[] args) throws IOException {
		CopyFileTest fileTest = new CopyFileTest();
		fileTest.testCopy();
	}
}