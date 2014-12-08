/**
 * # EmptyJsonMapper.java -- (2014年8月9日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.json.mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个空的JsonMapper实现类，生成的json字符串和读取json字符串后生成的Java对象都是null
 * @author 郝金隆
 *
 */
public class EmptyJsonMapper implements JsonMapper {
	static Logger logger = LoggerFactory.getLogger(EmptyJsonMapper.class);

	/* (non-Javadoc)
	 * @see net.haojinlong.tools.json.JsonMapper#toJson(java.lang.Object)
	 */
	@Override
	public String toJson(Object obj) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.haojinlong.tools.json.JsonMapper#fromJson(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T fromJson(String json, Class<T> objType) {
		return null;
	}
}
