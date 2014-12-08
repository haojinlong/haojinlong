/**
 * # AuthEntity.java -- (2014年12月8日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.anno;

import net.haojinlong.commons.webauth.consts.OperType;

/**
 * @author 郝金隆
 * @since 2014年12月8日
 */
public @interface AuthEntity {

	/**
	 * 要验证的实体名称
	 */
	public String module();

	/**
	 * 要验证的操作名称
	 */
	public OperType operType();

	/**
	 * 是否是使用pathVariable
	 */
	public boolean usePathVariable() default true;

	/**
	 * 获取采用REST方式的模块实例id的前缀，默认会是OperType.getURI的方法，
	 * 如/table/upate/1/column/list，将获取到"/update/"， 仅usePathVariable设置为true时有效
	 * 
	 * @see net.haojinlong.commons.webauth.consts.OperType
	 */
	public String prefix() default "";

	/**
	 * 从request中获取id的请求字段名称，仅usePathVariable为false的时候有效
	 */
	public String idKey() default "id";
}
