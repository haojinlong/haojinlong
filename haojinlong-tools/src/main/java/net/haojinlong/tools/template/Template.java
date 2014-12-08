/**
 * # Template.java -- (2006-11-6)
 * 作者：郝金隆
 * 联系方式：hjl_100@qq.com
 */
package net.haojinlong.tools.template;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 根据模版进行替换，在模版中，参数以：${paramName}方式存在，对其可以进行替换<br>
 * $则使用${$}来表示
 * 
 * @author 郝金隆
 * 
 */
public class Template {

	static Logger logger = LoggerFactory.getLogger(Template.class);

	/**
	 * 参数的标识
	 */
	public static final String PARAM_ID = "$";

	/**
	 * 参数开始的标识
	 */
	public static final String PARAM_START = "${";

	/**
	 * 参数结束的标识
	 */
	public static final String PARAM_END = "}";

	/**
	 * 模版的内容
	 */
	private StringBuffer templateContent;

	/**
	 * 保存对模版文件进行解析以后的内容
	 */
	private List<Segment> segmengList = new ArrayList<Segment>();

	/**
	 * 根据模版内容进行初始化
	 * 
	 * @param templateContent
	 *            模版内容
	 */
	public Template(StringBuffer templateContent) {
		this.templateContent = templateContent;
		init();
	}

	/**
	 * 根据模版文件进行初始化
	 * 
	 * @param templateFile
	 *            模版文件的绝对地址
	 * @param encoding
	 *            模版文件的编码格式
	 * @throws FileNotFoundException
	 *             文件不存在
	 */
	public Template(String templateFile, String encoding)
			throws FileNotFoundException {
		this(new File(templateFile), encoding);
	}

	/**
	 * 根据模版文件进行初始化，默认的编码格式是UTF-8
	 * 
	 * @param templateFile
	 *            模版文件的绝对地址
	 * @throws FileNotFoundException
	 *             文件不存在
	 */
	public Template(String templateFile) throws FileNotFoundException {
		this(templateFile, "UTF-8");
	}

	/**
	 * 根据模版文件对象进行初始化
	 * 
	 * @param file
	 *            模版文件对象
	 * @param encoding
	 *            模版文件使用的编码
	 * @throws FileNotFoundException
	 *             文件不存在
	 */
	public Template(File file, String encoding) throws FileNotFoundException {
		this(new FileInputStream(file), encoding);
	}

	/**
	 * 根据模版文件进行初始化，默认的编码格式是UTF-8
	 * 
	 * @param file
	 *            模版文件的文件对象
	 * @throws FileNotFoundException
	 *             文件不存在
	 */
	public Template(File file) throws FileNotFoundException {
		this(file, "UTF-8");
	}

	/**
	 * 根据输入流进行初始化
	 * 
	 * @param is
	 *            输入流
	 * @param encoding
	 *            编码格式
	 */
	public Template(InputStream is, String encoding) {
		this.templateContent = new StringBuffer();
		InputStreamReader reader;
		try { // 初始化reader
			reader = new InputStreamReader(is, encoding);
		} catch (UnsupportedEncodingException e) {
			reader = new InputStreamReader(is);
			logger.error(
					"error turn the inputstream encoding to: {} \n {}\n {}\n",
					encoding, e.getMessage(), e.getStackTrace());
		}

		BufferedReader br = new BufferedReader(reader);
		int data;
		try { // 内容读取
			while ((data = br.read()) != -1) {
				templateContent.append((char) data);
			}
		} catch (IOException e) {
			logger.error("BufferedReader Read error: {} \n {}", e.getMessage(),
					e.getStackTrace());
		}
		try { // 关闭BufferedReader
			br.close();
		} catch (IOException e) {
			logger.error("BufferedReader close error: {} \n {}",
					e.getMessage(), e.getStackTrace());
		}
		init();
		if (logger.isDebugEnabled()) {
			logger.debug("Template Init: {}", segmengList);
		}
	}

	public Template(InputStream is) {
		this(is, "UTF-8");
	}

	/**
	 * 对模版内容进行解析，将其放入paramList中
	 * 
	 */
	private void init() {
		segmengList = new ArrayList<Segment>();
		int startPos = 0;
		while (startPos < templateContent.length()) {
			int beginPos = templateContent.indexOf(PARAM_START, startPos);
			if (beginPos < 0) { // 不存在PARAM_START，没有参数
				String value = templateContent.substring(startPos);
				segmengList.add(new Segment(false, value));
				break;
			}
			int endPos = templateContent.indexOf(PARAM_END, beginPos);
			if (endPos < 0) { // 不存在PARAM_END，没有参数
				String value = templateContent.substring(startPos);
				segmengList.add(new Segment(false, value));
				break;
			}
			if (beginPos > startPos) { // 参数前面有内容
				String value = templateContent.substring(startPos, beginPos);
				segmengList.add(new Segment(false, value));
			}

			if (endPos - beginPos > PARAM_START.length()) { // 参数不为空
				String value = templateContent.substring(beginPos + 2, endPos);
				if (value.trim().length() > 0) {
					segmengList.add(new Segment(true, value.trim()));
				}
			}
			startPos = endPos + PARAM_END.length();
		}
	}

	/**
	 * 根据参数名称进行替换
	 * 
	 * @param paramName
	 *            参数的名称
	 * @param value
	 *            要替换的内容
	 * @return 总共替换的参数的个数
	 */
	public int replace(String paramName, String value) {
		int result = 0;
		for (Segment param : segmengList) {
			if (param.isParam() && param.getParamName().equals(paramName)) {
				param.replace(value);
				result++;
			}
		}
		return result;
	}

	/**
	 * Map中的key替换为value
	 * 
	 * @param map
	 *            参数的名称
	 * @return 总共替换的参数的个数
	 */
	public int replace(Map<String, String> map) {
		int result = 0;
		if (map != null) {
			for (String key : map.keySet()) {
				for (Segment param : segmengList) {
					if (param.isParam() && param.getParamName().equals(key)) {
						param.replace(map.get(key));
						result++;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据要替换的参数列表和对应的替换内容列表进行一一替换
	 * 
	 * @param params
	 *            要替换的参数
	 * @param values
	 *            替换的内容
	 * @return 总共替换的参数个数
	 */
	public int replace(List<String> params, List<String> values) {
		int result = 0;
		int n = params.size() < values.size() ? params.size() : values.size();
		for (int i = 0; i < n; i++) {
			replace(params.get(i), values.get(i));
		}
		return result;
	}

	/**
	 * 检查是否所有的参数都已经被替换完成了
	 * 
	 * @return 是否所有的参数都已经被替换了
	 */
	public boolean isAllReplaced() {
		boolean result = true;
		for (Segment s : segmengList) {
			if (s.isParam() && !s.isReplaced()) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		int n = segmengList.size();
		for (int i = 0; i < n; i++) {
			result.append(segmengList.get(i).toString());
		}
		return result.toString();
	}

	/**
	 * 按照指定的编码格式，模版替换后的内容写入到文件中
	 * 
	 * @param filePath
	 *            目标文件的绝对地址
	 * @param encoding
	 *            编码格式
	 * @throws IOException
	 *             写文件异常，可能是地址错误，也可能是权限限制
	 */
	public void toFile(String filePath, String encoding) throws IOException {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(filePath),
					encoding);
		} catch (UnsupportedEncodingException e) {
			writer = new OutputStreamWriter(new FileOutputStream(filePath));
			e.printStackTrace();
		}

		BufferedWriter bw = new BufferedWriter(writer);
		int n = segmengList.size();
		for (int i = 0; i < n; i++) {
			bw.write(segmengList.get(i).toString());
		}
		bw.flush();
		bw.close();
	}
}
