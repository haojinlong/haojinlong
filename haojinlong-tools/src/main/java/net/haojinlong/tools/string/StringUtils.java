/**
 * # StringFormat.java -- (2006-10-22)
 * 作者：郝金隆
 * 联系方式：hjl_100@qq.com
 */
package net.haojinlong.tools.string;

/**
 * 对字符串进行一些格式化的操作
 * 
 * @author 郝金隆
 * 
 */
public class StringUtils {

	/**
	 * 将输入的字符串的首字母变成大写
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 将str的首字母转换成为大写后的结果
	 */
	public static String firstUpper(String str) {
		char ch = str.charAt(0);
		ch = Character.toUpperCase(ch);
		return ch + str.substring(1);
	}

	/**
	 * 将输入的字符串的首字母变成小写
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 将str的首字母转换成为小写后的结果
	 */
	public static String firstLower(String str) {
		char ch = str.charAt(0);
		ch = Character.toLowerCase(ch);
		return ch + str.substring(1);
	}
}
