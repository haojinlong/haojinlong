/**
 * # IdKey.java -- (2014年11月30日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.entity;

import java.io.Serializable;

/**
 * @author 郝金隆
 * @since 2014年11月30日
 */
public class CodeText implements Serializable {
	private static final long serialVersionUID = -461321064139766446L;
	private int code;
	private String text;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
