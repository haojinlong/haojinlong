/**
 * # DateFormatter.java -- 2006-10-14
 * 作者：郝金隆
 * 联系方式：hjl_100@qq.com
 */
package net.haojinlong.tools.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期的格式化工具
 * 
 * @author 郝金隆
 * 
 */
public class DateUtils {

	static DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

	static DateFormat dayTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	static DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
	
	static DateFormat yearFormat = new SimpleDateFormat("yyyy");

	/**
	 * 获取当前的格式化日期，格式类型：<br />
	 * "yyyy-MM-dd"
	 * 
	 * @return 当前的格式化日期
	 */
	public static String getCurrentDay() {
		return dayFormat.format(new Date());
	}

	/**
	 * 获取当前的格式化日期时间，格式类型：<br />
	 * "yyyy-MM-dd hh:mm:ss"
	 * 
	 * @return 当前格式化的日期时间
	 */
	public static String getCurrentDayTime() {
		return dayTimeFormat.format(new Date());
	}

	/**
	 * 获取当前格式化的时间，格式类型：<br />
	 * "hh:mm:ss"
	 * @return 当前格式化的时间字符串
	 */
	public static String getCurrentTime() {
		return timeFormat.format(new Date());
	}

	/**
	 * 获取当前的格式化日期
	 * 
	 * @param format
	 *            日期时间格式
	 * @return 当前的格式化日期字符串
	 */
	public static String getCurrentDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * 获取当前的年份
	 * 
	 * @return 当前的年份
	 */
	public static String getCurrentYear() {
		return yearFormat.format(new Date());
	}

}
