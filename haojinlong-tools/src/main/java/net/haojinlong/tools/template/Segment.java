/**
 * # Segment.java -- (2006-10-26)
 * 作者：郝金隆
 * 联系方式：hjl_100@qq.com
 */
package net.haojinlong.tools.template;

/**
 * 将模版文件中的分段内容，使之便于替换
 * 
 * @author 郝金隆
 * 
 */
public class Segment {
	/**
	 * 标识这一段内容是否已经被替换
	 */
	private boolean isReplaced = false;

	/**
	 * 这段内容是否为参数
	 */
	private boolean isParam;

	/**
	 * 参数的名称
	 */
	private String paramName;

	/**
	 * 这一段的内容，或者是替换后的结果
	 */
	private String value;

	/**
	 * 根据是否为参数进行初始化
	 * 
	 * @param isParam
	 *            是否是参数
	 * @param value
	 *            参数的名称或者非参数字符串
	 */
	public Segment(boolean isParam, String value) {
		this.isParam = isParam;
		if (isParam) {
			if (value.equals(Template.PARAM_ID)) {
				this.value = value;
				this.isParam = false;
			} else {
				this.paramName = value;
			}
		} else {
			this.value = value;
		}
	}

	/**
	 * 判断此短内容是否为参数
	 * 
	 * @return 是否是参数
	 */
	public boolean isParam() {
		return isParam;
	}

	/**
	 * 是否已经替换
	 * 
	 * @return 是否已替换
	 */
	public boolean isReplaced() {
		return isReplaced;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return 参数名称
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * 将此参数替换成为对应的字符串
	 * 
	 * @param value
	 *            此参数要替换成为的内容
	 */
	public void replace(String value) {
		if (this.isParam) {
			this.value = value;
			this.isReplaced = true;
		}
	}

	@Override
	public String toString() {
		if (this.isParam) {
			if (this.isReplaced) {
				return value;
			} else {
				return Template.PARAM_START + paramName + Template.PARAM_END;
			}
		} else {
			return value;
		}
	}
}
