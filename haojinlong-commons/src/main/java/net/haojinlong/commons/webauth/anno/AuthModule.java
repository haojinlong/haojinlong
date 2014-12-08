/**
 * # AuthModule.java -- (2014年12月8日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.anno;

import net.haojinlong.commons.webauth.consts.OperType;

/**
 * @author 郝金隆
 * @since 2014年12月8日
 */
public @interface AuthModule {

	/**
	 * 要验证的模块名称
	 */
	public String module();

	/**
	 * 操作类型
	 */
	public OperType operType();

}
