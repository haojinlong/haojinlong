/**
 * # Const.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.consts;

/**
 * 操作类型
 * 
 * @author 郝金隆
 * @since 2014年11月13日
 */
public enum OperType {
	list, view, add, update, delete;

	/**
	 * 判断是否有id
	 * 
	 * @return 是否有id
	 */
	public boolean hasId() {
		if (this == list || this == add) {
			return false;
		}
		return true;
	}
}
