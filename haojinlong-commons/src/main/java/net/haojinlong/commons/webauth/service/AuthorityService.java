/**
 * # AuthorService.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.service;

import net.haojinlong.commons.webauth.consts.OperType;

/**
 * 权限验证的接口类，提供了必须的验证方法，若
 * 
 * @author 郝金隆
 * @since 2014年11月13日
 */
public interface AuthorityService {

	/**
	 * 验证模块操作的权限
	 * 
	 * @param userId
	 *            用户的id
	 * @param module
	 *            模块的code指
	 * @param operType
	 *            操作类型的code值
	 * @return 是否通过
	 */
	public boolean validate(int userId, String module, OperType operType);

	/**
	 * 验证对应模块实例操作的权限
	 * 
	 * @param userId
	 *            用户id
	 * @param module
	 *            模块的code
	 * @param operType
	 *            操作类型的code
	 * @param entityId
	 *            实体id
	 * @return 是否通过权限验证
	 */
	public boolean validate(int userId, String module, OperType operType, int entityId);

}
