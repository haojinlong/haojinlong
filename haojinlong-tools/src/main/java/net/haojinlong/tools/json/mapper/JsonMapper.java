/**
 * # JsonMapper.java -- (2014年8月9日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.json.mapper;


/**
 * JsonMapper的接口类，实现了JavaBean与json字符串的互转
 * @author 郝金隆
 *
 */
public interface JsonMapper {
	
	/** 
	 * 将一个javabean转换为json
	 * @param obj 要转换的javabean对象
	 * @return 转换成的json结果
	 */
	public String toJson(Object obj);

	/**
	 * 读取json数据，转换为javabean
	 * @param json 要转换的json语句
	 * @param objType 要转换的目标类
	 * @return 转换的javabean实体
	 */
	public <T> T fromJson(String json, Class<T> objType);

}
