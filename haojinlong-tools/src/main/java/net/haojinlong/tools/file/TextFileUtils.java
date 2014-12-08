/**
 * # FileUtils.java -- (2014年11月2日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文本文件读写的工具类
 * 
 * @author 郝金隆
 * @since 2014年11月2日
 *
 */
public class TextFileUtils {
	static Logger logger = LoggerFactory.getLogger(TextFileUtils.class);

	/**
	 * 将文本文件的全部文本内容
	 * 
	 * @param filePath
	 *            文件的绝对地址
	 * @return 全部内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readFile(String filePath) throws IOException {
		return readFile(new File(filePath));
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param filePath
	 *            文件绝对地址
	 * @param encoding
	 *            文件编码
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readFile(String filePath, String encoding)
			throws IOException {
		return readFile(new File(filePath), encoding);
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param file
	 *            要读取的文件
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readFile(File file) throws IOException {
		return readInputStream(new FileInputStream(file));
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param file
	 *            要读取的文件
	 * @param encoding
	 *            文件编码
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readFile(File file, String encoding)
			throws IOException {
		return readInputStream(new FileInputStream(file), encoding);
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param is
	 *            输入流
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readInputStream(InputStream is) throws IOException {
		return readInputStream(is, "UTF-8");
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param is
	 *            输入流
	 * @param encoding
	 *            编码
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String readInputStream(InputStream is, String encoding)
			throws IOException {
		return read(new InputStreamReader(is, encoding));
	}

	/**
	 * 读取文本文件的全部内容
	 * 
	 * @param reader
	 *            Reader对象
	 * @return 全部文本内容
	 * @throws IOException
	 *             文件读取异常
	 */
	public static String read(Reader reader) throws IOException {
		BufferedReader br = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer();
		int data;
		while ((data = br.read()) != -1) {
			sb.append((char) data);
		}
		br.close();
		return sb.toString();
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param filePath
	 *            文件绝对地址
	 * @param encoding
	 *            文件编码
	 * @return 分行文件列表
	 * @throws IOException
	 *             文件读取异常
	 */
	public static List<String> readToList(String filePath) throws IOException {
		return readToList(new File(filePath));
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param filePath
	 *            文件绝对地址
	 * @param encoding
	 *            文件编码
	 * @return 分行文件列表
	 * @throws IOException
	 *             文件读取异常
	 */
	public static List<String> readToList(String filePath, String encoding)
			throws IOException {
		return readToList(new File(filePath), encoding);
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param file
	 *            要读取的文件对象
	 * @return 分行文本列表
	 * @throws IOException
	 *             文件读写异常
	 */
	public static List<String> readToList(File file) throws IOException {
		return readToList(new FileInputStream(file));
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param file
	 *            要读取的文件对象
	 * @param encoding
	 *            文件编码
	 * @return 分行文本列表
	 * @throws IOException
	 *             文件读写异常
	 */
	public static List<String> readToList(File file, String encoding)
			throws IOException {
		return readToList(new FileInputStream(file), encoding);
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param is
	 *            输入流
	 * @return 分行文本列表
	 * @throws IOException
	 *             文件读取异常
	 */
	public static List<String> readToList(InputStream is) throws IOException {
		return readToList(is, "UTF-8");
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param is
	 *            输入流
	 * @param encoding
	 *            编码
	 * @return 分行文本列表
	 * @throws IOException
	 *             文件读取异常
	 */
	public static List<String> readToList(InputStream is, String encoding)
			throws IOException {
		return readToList(new InputStreamReader(is, encoding));
	}

	/**
	 * 将文本内容分行保存在List对象中
	 * 
	 * @param reader
	 *            Reader对象
	 * @return 分行文本列表
	 * @throws IOException
	 *             我那件读取异常
	 */
	public static List<String> readToList(Reader reader) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(reader);
		String str;
		while ((str = br.readLine()) != null) {
			list.add(str);
		}

		br.close();
		return list;
	}

	/**
	 * 将文本写入到文件中
	 * 
	 * @param content
	 *            文件内容
	 * @param filePath
	 *            文件绝对地址
	 * @throws IOException
	 *             文件写异常
	 */
	public static void writeFile(String content, String filePath)
			throws IOException {
		writeFile(content, new File(filePath));
	}

	/**
	 * 将文本写入到文件中
	 * 
	 * @param content
	 *            文件内容
	 * @param filePath
	 *            文件绝对地址
	 * @param encoding
	 *            编码
	 * @throws IOException
	 *             文件写异常
	 */
	public static void writeFile(String content, String filePath,
			String encoding) throws IOException {
		writeFile(content, new File(filePath), encoding);
	}

	/**
	 * 将文本写入到文件中
	 * 
	 * @param content
	 *            文件内容
	 * @param file
	 *            文件对象
	 * @throws IOException
	 *             文件写异常
	 */
	public static void writeFile(String content, File file) throws IOException {
		writeFile(content, file, "UTF-8");
	}

	/**
	 * 将文本写入到文件中
	 * 
	 * @param content
	 *            文件内容
	 * @param file
	 *            文件对象
	 * @param encoding
	 *            编码
	 * @throws IOException
	 *             文件写异常
	 */
	public static void writeFile(String content, File file, String encoding)
			throws IOException {
		OutputStreamWriter writer = null;
		writer = new OutputStreamWriter(new FileOutputStream(file), encoding);
		writer.write(content);
		writer.flush();
		writer.close();
	}
}
