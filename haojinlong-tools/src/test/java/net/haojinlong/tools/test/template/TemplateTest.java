/**
 * # TemplateTest.java -- (2014年9月26日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.tools.test.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.haojinlong.tools.template.Template;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 郝金隆
 * @since 2014年9月26日
 *
 */
public class TemplateTest {
	static Logger logger = LoggerFactory.getLogger(TemplateTest.class);
	Template template = new Template(
			TemplateTest.class
					.getResourceAsStream("/net/haojinlong/tools/test/resources/from.template"));
	
	InputStream is = TemplateTest.class
			.getResourceAsStream("/net/haojinlong/tools/test/resources/to.template");
	

	@Test
	public void testTemplate() {
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("className", "Test");
		replaceMap.put("fieldName", "id");
		template.replace(replaceMap);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		int a;
		try {
			while ((a = br.read()) != -1){
				sb.append((char)a);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals("判断读写是否一致：", template.toString(), sb.toString());
	}
}
