/**
 * # AuthorityException.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.entity;

/**
 * 权限验证异常
 * 
 * @author 郝金隆
 * @since 2014年11月13日
 */
public class AuthorityException extends Exception {

	private static final long serialVersionUID = 9210080233881683652L;

	public AuthorityException(String msg) {
		super(msg);
	}

}
