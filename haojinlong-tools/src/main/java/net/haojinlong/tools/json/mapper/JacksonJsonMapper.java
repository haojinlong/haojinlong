/**
 * # JacksonJsonMapper.java -- (2014年8月9日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.json.mapper;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用Jackson的JsonMapper实现类
 * @author 郝金隆
 *
 */
public class JacksonJsonMapper implements JsonMapper {
	static Logger logger = LoggerFactory.getLogger(JacksonJsonMapper.class);

	static ObjectMapper objectMapper = new ObjectMapper();

	/* 
	 * (non-Javadoc)
	 * 
	 * @see net.haojinlong.tools.json.JsonMapper#toJson(java.lang.Object)
	 */
	@Override
	public String toJson(Object obj) {
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, obj);
		} catch (JsonGenerationException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		} catch (JsonMappingException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		} catch (IOException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		}
		return sw.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.haojinlong.tools.json.JsonMapper#fromJson(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public <T> T fromJson(String json, Class<T> objType) {
		T result = null;
		try {
			result = objectMapper.readValue(json, objType);
		} catch (JsonParseException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		} catch (JsonMappingException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		} catch (IOException e) {
			logger.error("error: {}\n {}", e.getCause(), e.getStackTrace());
		}
		return result;
	}
}
