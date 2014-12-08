/**
 * # GsonJsonMapper.java -- (2014年8月9日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.json.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * 使用Gson的JsonMapper实现类
 * @author 郝金隆
 *
 */
public class GsonJsonMapper implements JsonMapper {
	static Logger logger = LoggerFactory.getLogger(GsonJsonMapper.class);
	static Gson gson = new Gson();

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.haojinlong.tools.json.JsonMapper#toJson(java.lang.Object)
	 */
	@Override
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.haojinlong.tools.json.JsonMapper#fromJson(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public <T> T fromJson(String json, Class<T> objType) {
		return gson.fromJson(json, objType);
	}
}
