/**
 * # AuthorityInterceptor.java -- (2014年11月13日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.springmvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.haojinlong.commons.webauth.anno.Authority;
import net.haojinlong.commons.webauth.consts.OperType;
import net.haojinlong.commons.webauth.entity.AuthorityException;
import net.haojinlong.commons.webauth.entity.UserProps;
import net.haojinlong.commons.webauth.service.AllPassAuthorityServiceImpl;
import net.haojinlong.commons.webauth.service.AuthorityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 权限验证拦截器，用于进行权限验证
 * 
 * @author 郝金隆
 * @since 2014年11月13日
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {
	static Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

	/**
	 * 权限验证的服务，若useService为true，或者设置为要验证每个操作对象的权限，且useServiceForId为true时才使用<br />
	 * 默认值仅为避免系统报错，所有的求情将全部通过，实际使用过程中请注意替换
	 */
	private AuthorityService authorityService = new AllPassAuthorityServiceImpl();

	/**
	 * 请求未通过将跳转的uri地址
	 */
	private String loginUri = null;

	/**
	 * 是否验证到模块实例的权限，默认维只验证模块的操作，而不验证对应对象的操作权限<br />
	 * 举例说明，若存在一个客户管理的模块，若validateId设置维false，则只验证这个模块的增删改查的权限，而不会验证某个客户的删改查的权限 <br />
	 * 注意，此处为通用设置，若这里设置为true，但在注解中设置了validateId为false，则此处的验证例外，不验证具体对象的权限；
	 * 若注解中forceValidateId设置为true，则强制要求必须验证
	 */
	private boolean validateId = false;

	/**
	 * 确定是使用service方式来验证还是通过session中的UserProps的方法属性来验证权限，
	 * 默认对于模块的操作权限使用session中的信息进行验证
	 */
	private boolean useService = false;

	/**
	 * 确定是否使用service的方法来验证具体实例的权限，默认要通过service来验证
	 */
	private boolean useServiceForId = true;

	/**
	 * 从session中获取UserProps对象的关键词
	 */
	private String userPropsKey = "userProps";

	/**
	 * @return the authorityService
	 */
	public AuthorityService getAuthorityService() {
		return authorityService;
	}

	/**
	 * @param authorityService
	 *            the authorityService to set
	 */
	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}

	/**
	 * @return the validateId
	 */
	public boolean isValidateId() {
		return validateId;
	}

	/**
	 * @param validateId
	 *            the validateId to set
	 */
	public void setValidateId(boolean validateId) {
		this.validateId = validateId;
	}

	/**
	 * @return the useService
	 */
	public boolean isUseService() {
		return useService;
	}

	/**
	 * @param useService
	 *            the useService to set
	 */
	public void setUseService(boolean useService) {
		this.useService = useService;
	}

	/**
	 * @return the useServiceForId
	 */
	public boolean isUseServiceForId() {
		return useServiceForId;
	}

	/**
	 * @param useServiceForId
	 *            the useServiceForId to set
	 */
	public void setUseServiceForId(boolean useServiceForId) {
		this.useServiceForId = useServiceForId;
	}

	/**
	 * @return the userPropsKey
	 */
	public String getUserPropsKey() {
		return userPropsKey;
	}

	/**
	 * @param userPropsKey
	 *            the userPropsKey to set
	 */
	public void setUserPropsKey(String userPropsKey) {
		this.userPropsKey = userPropsKey;
	}

	/**
	 * @return the loginUri
	 */
	public String getLoginUri() {
		return loginUri;
	}

	/**
	 * @param loginUri
	 *            the loginUri to set
	 */
	public void setLoginUri(String loginUri) {
		this.loginUri = loginUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws AuthorityException, IOException {
		if (!(handler instanceof HandlerMethod)) {
			logger.debug("not HandlerMethod");
			return true;
		}
		HandlerMethod handler2 = (HandlerMethod) handler;
		Authority authority = handler2.getMethodAnnotation(Authority.class);
		// 无注解，直接通过
		if (authority == null) {
			return true;
		}

		// 获取模块和操作类型
		String module = authority.module();
		OperType operType = authority.operType();
		boolean result = false;
		if (operType.hasId()
				&& ((validateId && authority.validateId()) || authority
						.forceValidateId())) { // 此操作类型可验证id，并且配置要验证id
			result = validateId(request, authority, module, operType);
		} else { // 不验证id
			result = validate(request, module, operType);
		}
		if (result == false) {
			if (loginUri != null) {
				response.sendRedirect(request.getContextPath() + loginUri);
			}
		}
		return result;
	}

	/**
	 * 验证模块实例的权限
	 * 
	 * @param request
	 * @param authority
	 * @param module
	 * @param operType
	 * @return
	 * @throws AuthorityException
	 */
	@SuppressWarnings("rawtypes")
	private boolean validateId(HttpServletRequest request, Authority authority,
			String module, OperType operType) throws AuthorityException {
		int entityId = getEntityId(authority, request);
		logger.debug("operType:{}, module:{}, entityId:{}", operType, module,
				entityId);
		if (entityId == -1) { // 找不到实体类id
			throw new AuthorityException("无法获取实体Id");
		}
		if (useServiceForId) { // 使用远程验证
			int userId = getUserId(request, userPropsKey);
			if (userId == -1) {
				return false;
			}
			return authorityService
					.validate(userId, module, operType, entityId);
		} else {
			Object obj = request.getSession().getAttribute(userPropsKey);
			if (obj == null || !(obj instanceof UserProps)) {
				return false;
			}
			return ((UserProps) obj).hasAuth(module, operType, entityId);
		}
	}

	/**
	 * 验证用户的角色和权限
	 * 
	 * @param request
	 * @param module
	 * @param operType
	 * @return
	 * @throws AuthorityException
	 */
	@SuppressWarnings("rawtypes")
	private boolean validate(HttpServletRequest request, String module,
			OperType operType) throws AuthorityException {
		if (useService) {
			int userId = getUserId(request, userPropsKey);
			if (userId == -1) {
				return false;
			}
			return authorityService.validate(userId, module, operType);
		} else {
			Object obj = request.getSession().getAttribute(userPropsKey);
			if (obj == null || !(obj instanceof UserProps)) {
				return false;
			}
			return ((UserProps) obj).hasAuth(module, operType);
		}
	}

	/**
	 * 从session中获取用户的id
	 * 
	 * @param request
	 *            http请求
	 * @param userPropsKey
	 *            session中保存用户信息的key
	 * @return 用户id
	 */
	@SuppressWarnings("rawtypes")
	private int getUserId(HttpServletRequest request, String userPropsKey) {
		Object user = request.getSession().getAttribute(userPropsKey);
		if (user == null) {
			logger.error("session中没有{}属性", userPropsKey);
			return -1;
		}

		if (user instanceof UserProps) {
			return ((UserProps) user).getUserId();
		}

		Method method = null;
		try { // 获取getId方法
			method = user.getClass().getMethod("getId");
		} catch (NoSuchMethodException e) {
			logger.debug("没有getId方法");
		} catch (SecurityException e) {
			logger.error("error: {}\n{}", e.getMessage(), e.getStackTrace());
		}

		if (method == null) {
			try { // 尝试getUserId方法
				method = user.getClass().getMethod("getUserId");
			} catch (NoSuchMethodException e) {
				logger.debug("没有getUserId方法");
			} catch (SecurityException e) {
				logger.error("error: {}\n{}", e.getMessage(), e.getStackTrace());
			}
		}

		if (method == null) {
			return -1;
		}

		try { // 执行获取id的方法，并返回
			int result = (Integer) method.invoke(user);
			return result;
		} catch (IllegalAccessException e) {
			logger.debug("error: {}\n{}", e.getMessage(), e.getStackTrace());
		} catch (IllegalArgumentException e) {
			logger.debug("error: {}\n{}", e.getMessage(), e.getStackTrace());
		} catch (InvocationTargetException e) {
			logger.debug("error: {}\n{}", e.getMessage(), e.getStackTrace());
		}
		return -1;
	}

	/**
	 * 获取对应的实体id
	 * 
	 * @param authority
	 * @param request
	 * @return
	 */
	private int getEntityId(Authority authority, HttpServletRequest request) {
		try {
			if (authority.usePathVariable()) {
				String requestPath = request.getRequestURI();
				logger.debug("requestURI:{}", requestPath);
				String prefix = authority.prefix();
				if (prefix == null || prefix.trim().length() == 0) {
					prefix = authority.module() + "/" + authority.operType()
							+ "/";
				}
				if (!prefix.endsWith("/")) {
					prefix = prefix + "/";
				}
				return getEntityId(requestPath, prefix);
			} else {
				String idStr = request.getParameter(authority.idKey());
				if (idStr != null) {
					return Integer.parseInt(idStr);
				}
			}
		} catch (Exception e) {
			logger.debug("error: {} \n {}", e.getMessage(), e.getStackTrace());
		}
		return -1;
	}

	/**
	 * 获取对应的实体Id
	 * 
	 * @param requestURI
	 * @param prefix
	 * @return
	 */
	private int getEntityId(String requestURI, String prefix) {
		logger.debug("requestPath:{},prefix:{}", requestURI, prefix);
		if (requestURI.contains(prefix)) {
			String afterStr = requestURI.substring(requestURI.indexOf(prefix)
					+ prefix.length());
			if (afterStr != null && afterStr.length() > 0) {
				String idStr;
				if (afterStr.contains("/")) {
					idStr = afterStr.substring(0, afterStr.indexOf("/"));
				} else {
					idStr = afterStr;
				}
				return Integer.parseInt(idStr);
			}
		}
		return -1;
	}

}
