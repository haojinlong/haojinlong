/**
 * # UserProperties.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.haojinlong.commons.webauth.consts.OperType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 郝金隆
 * @since 2014年11月13日
 */
public class UserProps<T> {
	static Logger logger = LoggerFactory.getLogger(UserProps.class);

	/**
	 * 用户信息
	 */
	private T userInfo;

	/**
	 * 用户的Id
	 */
	private int userId = 0;

	/**
	 * 用户权限列表，其中key是module，value是操作类型的列表
	 */
	private Map<String, List<String>> authMap = new HashMap<String, List<String>>();

	/**
	 * 用户权限列表，key是 {module}_{operType}，value是对应的实体列表
	 */
	private Map<String, List<Integer>> authMapWithId = new HashMap<String, List<Integer>>();

	/**
	 * @return 用户信息
	 */
	public T getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo
	 *            用户信息
	 */
	public void setUserInfo(T userInfo) {
		this.userInfo = userInfo;
		Method method;
		if (userId == 0) {
			try {
				method = userInfo.getClass().getMethod("getId");
				if (method != null) {
					Object obj = method.invoke(userInfo);
					if (obj instanceof Integer) {
						this.userId = (Integer) obj;
					}
				}
			} catch (NoSuchMethodException e) {
				logger.debug("没有getId方法，无法获取用户id");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (userId == 0) {
			try {
				method = userInfo.getClass().getMethod("getUserId");
				if (method != null) {
					Object obj = method.invoke(userInfo);
					if (obj instanceof Integer) {
						this.userId = (Integer) obj;
					}
				}
			} catch (NoSuchMethodException e) {
				logger.debug("没有getUserId方法，无法获取用户id");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 设置用户的权限，细化到模块和操作
	 * 
	 * @param module
	 *            模块Id
	 * @param operType
	 *            操作类型id
	 */
	public void addAuth(String module, String operType) {
		List<String> operTypeList = authMap.get(module);
		if (operTypeList == null) {
			operTypeList = new ArrayList<String>();
			authMap.put(module, operTypeList);
		}
		if (!operTypeList.contains(operType)) {
			operTypeList.add(operType);
		}
	}

	/**
	 * @param module
	 * @param operType
	 * @return
	 */
	public boolean hasAuth(String module, OperType operType) {
		List<String> operTypeList = authMap.get(module);
		if (operTypeList != null && operTypeList.contains(operType.toString())) {
			return true;
		}
		return false;
	}

	/**
	 * 设置用户的权限，仅细化到模块和操作类型
	 * 
	 * @param operType
	 *            操作类型
	 * @param module
	 *            模块名称
	 * @param entityId
	 *            对应的实体id
	 */
	public void addAuth(String module, String operType, int entityId) {
		addAuth(module, operType);
		String key = buildKey(module, operType);
		List<Integer> idList = authMapWithId.get(key);
		if (idList == null) {
			idList = new ArrayList<Integer>();
			authMapWithId.put(key, idList);
		}
		if (!idList.contains(entityId)) {
			idList.add(entityId);
		}

	}

	/**
	 * 判断用户是否有相应的权限
	 * 
	 * @param operType
	 * @param module
	 * @return
	 */
	public boolean hasAuth(String module, OperType operType, int entityId) {
		if (!operType.hasId()) {
			return hasAuth(module, operType);
		}
		String key = buildKey(module, operType.toString());
		List<Integer> idList = authMapWithId.get(key);
		if (idList != null && idList.contains(entityId)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据操作类型和模块名，生成authMapWithId的key
	 * 
	 * @param operType
	 * @param module
	 * @return
	 */
	private String buildKey(String module, String operType) {
		return module + "_" + operType;
	}

	// public static void main(String[] args) {
	// UserProps<String> props = new UserProps<String>();
	// props.addAuth(1, 2, 1);
	// System.out.println(JsonUtils.toJson(props));
	// System.out.println(props.hasAuth(1, 2, 1));
	// System.out.println(props.hasAuth(1, 2));
	// }
}
