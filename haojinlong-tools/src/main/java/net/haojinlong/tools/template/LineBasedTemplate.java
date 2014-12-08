/**
 * # ExistTemplate.java -- (2014年7月3日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.haojinlong.tools.json.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 郝金隆
 * 
 */
public class LineBasedTemplate {
	static Logger logger = LoggerFactory.getLogger(LineBasedTemplate.class);

	public static String EOF_LINE = "\n";

	private String startLine;

	private String endLine;

	private String startStr;

	private String endStr;

	private String encoding;

	private List<String> beforeLines;

	private List<String> afterLines;

	/**
	 * @param fileName
	 * @param startStr
	 * @param endStr
	 * @throws IOException
	 */
	public LineBasedTemplate(String fileName, String startStr, String endStr)
			throws IOException {
		this(new File(fileName), "UTF-8", startStr, endStr);
	}

	/**
	 * @param fileName
	 *            要读取的文件绝对地址
	 * @param encoding
	 *            文件字字符编码
	 * @param startStr
	 *            要替换的开始行中的开始标识
	 * @param endStr
	 *            要替换的结束行中结束标识
	 * @throws IOException
	 *             读写异常
	 */
	public LineBasedTemplate(String fileName, String encoding, String startStr,
			String endStr) throws IOException {
		this(new File(fileName), encoding, startStr, endStr);
	}

	/**
	 * @param file
	 *            已经存在要分章节进行替换的文件
	 * @param startStr
	 *            片段替换开始标识
	 * @param endStr
	 *            片段替换结束标识
	 * @throws IOException
	 *             读取异常
	 */
	public LineBasedTemplate(File file, String startStr, String endStr)
			throws IOException {
		this(new FileInputStream(file), "UTF-8", startStr, endStr);
	}

	/**
	 * @param file
	 *            要读取的文件
	 * @param encoding
	 *            文件字符编码
	 * @param startStr
	 *            片段替换开始标识
	 * @param endStr
	 *            片段替换结束标识
	 * @throws IOException
	 *             读取异常
	 */
	public LineBasedTemplate(File file, String encoding, String startStr,
			String endStr) throws IOException {
		this(new FileInputStream(file), encoding, startStr, endStr);
	}

	/**
	 * 按照UTF-8作为默认编码进行模板的初始化
	 * 
	 * @param is
	 *            模板的输入六
	 * @param startStr
	 *            片段替换开始标识
	 * @param endStr
	 *            片段替换结束标识
	 * @throws IOException
	 *             读取异常
	 */
	public LineBasedTemplate(InputStream is, String startStr, String endStr)
			throws IOException {
		this(is, "UTF-8", startStr, endStr);
	}

	/**
	 * @param is
	 *            输入流
	 * @param encoding
	 *            编码格式
	 * @param startStr
	 *            要提替换的开始标识
	 * @param endStr
	 *            要替换的结束标识
	 * @throws IOException
	 */
	public LineBasedTemplate(InputStream is, String encoding, String startStr,
			String endStr) throws IOException {
		this(new InputStreamReader(is, encoding), startStr, endStr);
	}

	/**
	 * @param reader reader属性
	 * @param startStr 要提替换的开始标识
	 * @param endStr 要替换的结束标识
	 * @throws IOException
	 */
	public LineBasedTemplate(Reader reader, String startStr, String endStr)
			throws IOException {
		this.startStr = startStr;
		this.endStr = endStr;
		this.beforeLines = new ArrayList<String>();
		this.afterLines = new ArrayList<String>();

		BufferedReader br = new BufferedReader(reader);

		// 读取文件，并按行进行拆分
		String line;
		int status = 0;
		while ((line = br.readLine()) != null) {
			if (line.indexOf(startStr) != -1) {
				status = 1;
				this.startLine = line;
				continue;
			}
			if (line.indexOf(endStr) != -1) {
				status = 2;
				this.endLine = line;
				continue;
			}
			switch (status) {
			case 0:
				this.beforeLines.add(line);
			case 1:
				continue;
			case 2:
				this.afterLines.add(line);
			}
		}
		br.close();
		logger.debug("the value of init: {}", this.toString());
	}

	/**
	 * @return the startLine
	 */
	public String getStartLine() {
		return startLine;
	}

	/**
	 * @param startLine
	 *            the startLine to set
	 */
	public void setStartLine(String startLine) {
		this.startLine = startLine;
	}

	/**
	 * @return the endLine
	 */
	public String getEndLine() {
		return endLine;
	}

	/**
	 * @param endLine
	 *            the endLine to set
	 */
	public void setEndLine(String endLine) {
		this.endLine = endLine;
	}

	/**
	 * @return the startStr
	 */
	public String getStartStr() {
		return startStr;
	}

	/**
	 * @param startStr
	 *            the startStr to set
	 */
	public void setStartStr(String startStr) {
		this.startStr = startStr;
	}

	/**
	 * @return the endStr
	 */
	public String getEndStr() {
		return endStr;
	}

	/**
	 * @param endStr
	 *            the endStr to set
	 */
	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the beforeLines
	 */
	public List<String> getBeforeLines() {
		return beforeLines;
	}

	/**
	 * @param beforeLines
	 *            the beforeLines to set
	 */
	public void setBeforeLines(List<String> beforeLines) {
		this.beforeLines = beforeLines;
	}

	/**
	 * @return the afterLines
	 */
	public List<String> getAfterLines() {
		return afterLines;
	}

	/**
	 * @param afterLines
	 *            the afterLines to set
	 */
	public void setAfterLines(List<String> afterLines) {
		this.afterLines = afterLines;
	}

	/**
	 * 替换中间的内容
	 * 
	 * @param content
	 *            要替换的内容
	 */
	public String replace(String content) {
		StringBuffer sb = new StringBuffer();
		for (String str : beforeLines) {
			sb.append(str + EOF_LINE);
		}
		sb.append(startLine + EOF_LINE);
		sb.append(content);
		sb.append(endLine + EOF_LINE);
		for (String str : afterLines) {
			sb.append(str + EOF_LINE);
		}
		return sb.toString();

	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
