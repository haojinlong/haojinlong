/**
 * # AllPassAuthorityServiceImpl.java -- (2014年11月29日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.service;

import net.haojinlong.commons.webauth.consts.OperType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 所有验证均通过的验证接口实现类，仅为避免出错，实际使用过程中请采用实际的实现类进行替换
 * 
 * @author 郝金隆
 * @since 2014年11月29日
 */
public class AllPassAuthorityServiceImpl implements AuthorityService {
	static Logger logger = LoggerFactory
			.getLogger(AllPassAuthorityServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.haojinlong.commons.webauth.service.AuthorityService#validate(int,
	 * String, OperType)
	 */
	public boolean validate(int userId, String module, OperType operType) {
		logger.warn("warnning：使用默认的验证服务，所有请求将全部通过！！");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.haojinlong.commons.webauth.service.AuthorityService#validate(int,
	 * String, OperType, int)
	 */
	public boolean validate(int userId, String module, OperType operType,
			int entityId) {
		logger.warn("warnning：使用默认的验证服务，所有请求将全部通过！！");
		return true;
	}
}
