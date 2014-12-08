/**
 * # JsonFactory.java -- (2014年8月9日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.json;

import net.haojinlong.tools.json.mapper.EmptyJsonMapper;
import net.haojinlong.tools.json.mapper.GsonJsonMapper;
import net.haojinlong.tools.json.mapper.JacksonJsonMapper;
import net.haojinlong.tools.json.mapper.JsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json的工具类，实现json字符串与java对象的互转<br />
 * 优先使用jackson，其次使用gson，如果这两个json的类库都不存在，则转换结果全部为null
 * @author 郝金隆
 *
 */
public class JsonUtils {
	static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private static JsonMapper jsonMapper = getJsonMapper();

	/**
	 * 根据javabean获取对应的json语句
	 * 
	 * @param obj
	 *            要转换的对象
	 * @return javabean对应的json语句
	 */
	public static String toJson(Object obj) {
		return jsonMapper.toJson(obj);
	}

	/**
	 * 读取json语句，获取对应的javabean实例
	 * 
	 * @param json
	 *            json语句
	 * @param objType
	 *            javabean类
	 * @return 相应的javabean实例
	 */
	public static <T> T fromJson(String json, Class<T> objType) {
		return jsonMapper.fromJson(json, objType);
	}

	/**
	 * 通过判断引用的jar包，确定实例化的JsonMapper实例
	 * 
	 * @return 获取一个JsonMapper实例
	 */
	private static JsonMapper getJsonMapper() {
		logger.debug("begin init JsonMapper class");
		try {
			Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
			Class.forName("com.fasterxml.jackson.core.JsonParser");
			Class.forName("com.fasterxml.jackson.annotation.JsonCreator");
			logger.info("use jackson");
			return new JacksonJsonMapper();
		} catch (ClassNotFoundException e) {
			logger.info("cannot use jackson libs, try gson ");
		} catch (NoClassDefFoundError e) {
			logger.info("cannot use jackson libs, try gson ");
		}

		try {
			Class.forName("com.google.gson.Gson");
			logger.info("use gson");
			return new GsonJsonMapper();
		} catch (ClassNotFoundException e) {
			logger.info("cannot use gson libs, use the empty JsonMapper");
		} catch (NoClassDefFoundError e) {
			logger.info("cannot use gson libs, use the empty JsonMapper");
		}
		return new EmptyJsonMapper();
	}

}
