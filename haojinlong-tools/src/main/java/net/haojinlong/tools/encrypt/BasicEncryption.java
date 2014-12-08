/**
 * # Encryption.java -- (2014年7月7日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个简单的对称加密解密算法，通过字符串的顺序颠倒，进行数据加密解密
 * 
 * @author 郝金隆
 * 
 */
public class BasicEncryption {
	static Logger logger = LoggerFactory.getLogger(BasicEncryption.class);

	/**
	 * 采用简单加密算法进行加密
	 * 
	 * @param content
	 *            要加密的内容
	 * @return 加密结果
	 */
	public static String encrypt(String content) {
		return encrypt(content, "haojinlong");
	}

	/**
	 * 采用简单加密算法进行加密
	 * 
	 * @param content
	 *            要加密的内容
	 * @param keyword
	 *            加密密码
	 * @return 加密结果
	 */
	public static String encrypt(String content, String keyword) {
		logger.debug("content: {}", content);
		StringBuffer contentBuffer = new StringBuffer(content);
		StringBuffer midBuffer = new StringBuffer();
		StringBuffer midBuffer2 = new StringBuffer();
		StringBuffer resultBuffer = new StringBuffer();
		int[] key1 = transform(keyword, 3);
		int[] key2 = transform(keyword, 7);
		int[] key3 = transform(keyword, 2);
		int pos = 0;
		while (pos < contentBuffer.length()) {
			for (int i = 0; i < key1.length; i++) {
				if (contentBuffer.length() >= pos + key1[i] * 2) {
					String s = contentBuffer.substring(pos, pos + key1[i] * 2);
					midBuffer.append(s.substring(key1[i]));
					midBuffer.append(s.substring(0, key1[i]));
					pos += key1[i] * 2;
				} else {
					midBuffer.append(contentBuffer.substring(pos,
							contentBuffer.length()));
					pos = contentBuffer.length();
					break;
				}
			}
		}

		pos = 0;
		while (pos < midBuffer.length()) {
			for (int i = 0; i < key2.length; i++) {
				if (midBuffer.length() >= pos + key2[i] * 2) {
					String s = midBuffer.substring(pos, pos + key2[i] * 2);
					midBuffer2.append(s.substring(key2[i]));
					midBuffer2.append(s.substring(0, key2[i]));
					pos += key2[i] * 2;
				} else {
					midBuffer2.append(midBuffer.substring(pos,
							midBuffer.length()));
					pos = midBuffer.length();
					break;
				}
			}
		}

		pos = 0;
		while (pos < midBuffer2.length()) {
			for (int i = 0; i < key3.length; i++) {
				if (midBuffer2.length() >= pos + key3[i] * 2) {
					String s = midBuffer2.substring(pos, pos + key3[i] * 2);
					resultBuffer.append(s.substring(key3[i]));
					resultBuffer.append(s.substring(0, key3[i]));
					pos += key3[i] * 2;
				} else {
					resultBuffer.append(midBuffer2.substring(pos,
							midBuffer2.length()));
					pos = midBuffer2.length();
					break;
				}
			}
		}
		logger.debug("result: {}", resultBuffer);

		return resultBuffer.toString();
	}

	/**
	 * 采用价单加密解密算法进行数据解密
	 * 
	 * @param content
	 *            要解密的内容
	 * @return 解密结果
	 */
	public static String decrypt(String content) {
		return decrypt(content, "haojinlong");
	}

	/**
	 * 采用价单加密解密算法进行数据解密
	 * 
	 * @param content
	 *            要解密的内容
	 * @param keyword
	 *            密码
	 * @return 解密结果
	 */
	public static String decrypt(String content, String keyword) {

		logger.debug("content: {}", content);
		StringBuffer contentBuffer = new StringBuffer(content);
		StringBuffer midBuffer = new StringBuffer();
		StringBuffer midBuffer2 = new StringBuffer();
		StringBuffer resultBuffer = new StringBuffer();
		int[] key1 = transform(keyword, 2);
		int[] key2 = transform(keyword, 7);
		int[] key3 = transform(keyword, 3);
		int pos = 0;
		while (pos < contentBuffer.length()) {
			for (int i = 0; i < key1.length; i++) {
				if (contentBuffer.length() >= pos + key1[i] * 2) {
					String s = contentBuffer.substring(pos, pos + key1[i] * 2);
					midBuffer.append(s.substring(key1[i]));
					midBuffer.append(s.substring(0, key1[i]));
					pos += key1[i] * 2;
				} else {
					midBuffer.append(contentBuffer.substring(pos,
							contentBuffer.length()));
					pos = contentBuffer.length();
					break;
				}
			}
		}

		pos = 0;
		while (pos < midBuffer.length()) {
			for (int i = 0; i < key2.length; i++) {
				if (midBuffer.length() >= pos + key2[i] * 2) {
					String s = midBuffer.substring(pos, pos + key2[i] * 2);
					midBuffer2.append(s.substring(key2[i]));
					midBuffer2.append(s.substring(0, key2[i]));
					pos += key2[i] * 2;
				} else {
					midBuffer2.append(midBuffer.substring(pos,
							midBuffer.length()));
					pos = midBuffer.length();
					break;
				}
			}
		}

		pos = 0;
		while (pos < midBuffer2.length()) {
			for (int i = 0; i < key3.length; i++) {
				if (midBuffer2.length() >= pos + key3[i] * 2) {
					String s = midBuffer2.substring(pos, pos + key3[i] * 2);
					resultBuffer.append(s.substring(key3[i]));
					resultBuffer.append(s.substring(0, key3[i]));
					pos += key3[i] * 2;
				} else {
					resultBuffer.append(midBuffer2.substring(pos,
							midBuffer2.length()));
					pos = midBuffer2.length();
					break;
				}
			}
		}
		logger.debug("result: {}", resultBuffer);

		return resultBuffer.toString();
	}

	/**
	 * 根据输入的密码获取字符混淆码
	 * 
	 * @param keyword
	 *            混淆码数据结果
	 * @param val
	 *            混淆码最大值
	 * @return 混淆码
	 */
	private static int[] transform(String keyword, int val) {
		int[] result = new int[keyword.length()];
		for (int i = 0; i < keyword.length(); i++) {
			result[i] = (int) keyword.charAt(i) % val + 1;
		}
		return result;
	}

}
