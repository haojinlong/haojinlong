/**
 * # DefaultAuthorityInteceptor.java -- (2014年12月8日)
 * 作者：郝金隆
 * 联系方式：haojinlong@189.cn
 */
package net.haojinlong.commons.webauth.springmvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.haojinlong.commons.webauth.anno.AuthEntity;
import net.haojinlong.commons.webauth.anno.AuthModule;
import net.haojinlong.commons.webauth.entity.UserProps;
import net.haojinlong.commons.webauth.service.AllPassAuthorityServiceImpl;
import net.haojinlong.commons.webauth.service.AuthorityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author 郝金隆
 * @since 2014年12月8日
 */
public class DefaultAuthorityInteceptor extends HandlerInterceptorAdapter {
	static Logger logger = LoggerFactory
			.getLogger(DefaultAuthorityInteceptor.class);

	/**
	 * 权限验证的服务，若useService为true，或者设置为要验证每个操作对象的权限，且useServiceForId为true时才使用<br />
	 * 默认值仅为避免系统报错，所有的求情将全部通过，实际使用过程中请注意替换
	 */
	private AuthorityService authorityService = new AllPassAuthorityServiceImpl();

	/**
	 * 失败后要跳转的页面
	 */
	private String failedUri = null;

	/**
	 * 从session中获取UserProps对象的关键词
	 */
	private String userPropsKey = "userProps";

	/**
	 * 是否使用service进行moudule验证
	 */
	private boolean useServiceForModule = false;

	/**
	 * 是否使用service进行id的验证
	 */
	private boolean useServiceForId = false;

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
	 * @return the failedUri
	 */
	public String getFailedUri() {
		return failedUri;
	}

	/**
	 * @param failedUri
	 *            the failedUri to set
	 */
	public void setFailedUri(String failedUri) {
		this.failedUri = failedUri;
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
	 * @return the useServiceForModule
	 */
	public boolean isUseServiceForModule() {
		return useServiceForModule;
	}

	/**
	 * @param useServiceForModule
	 *            the useServiceForModule to set
	 */
	public void setUseServiceForModule(boolean useServiceForModule) {
		this.useServiceForModule = useServiceForModule;
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
			HttpServletResponse response, Object handler) throws Exception {
		// 判断是否为HandlerMethod
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		AuthModule authModule = handlerMethod
				.getMethodAnnotation(AuthModule.class);
		AuthEntity authEntity = handlerMethod
				.getMethodAnnotation(AuthEntity.class);

		if (auth(authEntity, request) && auth(authModule, request)) {
			return true;
		}

		// 判断是否有错误
		if (failedUri != null) {
			response.sendRedirect(request.getContextPath() + failedUri);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证模块
	 * 
	 * @param authModule
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean auth(AuthModule authModule, HttpServletRequest request) {
		if (authModule == null) {
			return true;
		}

		if (useServiceForModule) {
			int userId = getUserId(request);
			if (userId == -1) {
				return false;
			}
			return authorityService.validate(userId, authModule.module(),
					authModule.operType());
		} else {
			Object obj = request.getSession().getAttribute(userPropsKey);
			if (obj == null || !(obj instanceof UserProps)) {
				return false;
			}
			return ((UserProps) obj).hasAuth(authModule.module(),
					authModule.operType());
		}
	}

	/**
	 * 验证id
	 * 
	 * @param authEntity
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean auth(AuthEntity authEntity, HttpServletRequest request) {
		if (authEntity == null) {
			return true;
		}

		int userId = getUserId(request);
		int entityId = getEntityId(authEntity, request);
		if (userId == -1 || entityId == -1) {
			return false;
		}

		if (useServiceForId) {
			return authorityService.validate(userId, authEntity.module(),
					authEntity.operType(), entityId);
		} else {
			Object obj = request.getSession().getAttribute(userPropsKey);
			if (obj == null || !(obj instanceof UserProps)) {
				return false;
			}
			return ((UserProps) obj).hasAuth(authEntity.module(),
					authEntity.operType(), entityId);
		}
	}

	/**
	 * 获取用户id
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private int getUserId(HttpServletRequest request) {
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
	 * @param authEntity
	 * @param request
	 * @return
	 */
	private int getEntityId(AuthEntity authEntity, HttpServletRequest request) {
		try {
			if (authEntity.usePathVariable()) {
				String requestPath = request.getRequestURI();
				logger.debug("requestURI:{}", requestPath);
				String prefix = authEntity.prefix();
				if (prefix == null || prefix.trim().length() == 0) {
					prefix = authEntity.module() + "/" + authEntity.operType()
							+ "/";
				}
				if (!prefix.endsWith("/")) {
					prefix = prefix + "/";
				}
				return getEntityId(requestPath, prefix);
			} else {
				String idStr = request.getParameter(authEntity.idKey());
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
