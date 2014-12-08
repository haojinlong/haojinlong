/**
 * # Authority.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.haojinlong.commons.webauth.consts.OperType;

/**
 * @author 郝金隆
 * @since 2014年11月13日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {

	/**
	 * 对应的模块code值，用于传递给拦截器，确定用户是否有此权限
	 */
	String module();

	/**
	 * 操作类型，包括LIST、ADD、UPATE、DELETE、VIEW五种权限，分别对应于OperType中的常量
	 * 
	 * @see net.haojinlong.commons.webauth.consts.OperType
	 * 
	 */
	OperType operType();

	/**
	 * 指定是否验证模块示例的权限，若设置为false，则不验证<br />
	 * 若设置为true，则必须AuthorityInterceptor中也设置为true才验证
	 */
	boolean validateId() default true;

	/**
	 * 指定是否强制要求验证对应模块实例的权限，即便AuthorityInterceptor中未设置要验证validateId，也强制验证
	 * 
	 */
	boolean forceValidateId() default false;

	/**
	 * 确定是否采用REST方式获取id，仅设置为要验证实例权限时才有效
	 * 
	 * @return
	 */
	boolean usePathVariable() default true;

	/**
	 * 获取采用REST方式的模块实例id的前缀，默认会是OperType.getURI的方法，
	 * 如/table/upate/1/column/list，将获取到"/update/"， 仅usePathVariable设置为true时有效
	 * 
	 * @see net.haojinlong.commons.webauth.consts.OperType
	 */
	String prefix() default "";

	/**
	 * 从request中获取id的请求字段名称，仅usePathVariable为false的时候有效
	 */
	String idKey() default "id";

}
