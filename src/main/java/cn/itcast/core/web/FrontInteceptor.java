package cn.itcast.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itcast.core.Constants;
import cn.itcast.core.bean.user.Buyer;

/**
 * 前台拦截
 * 上下文
 * 用户判断是否登录
 * @author liliang
 *
 */
public class FrontInteceptor implements HandlerInterceptor{
	
	private static final String URL_INTECEPTOR = "/buyer";
	/**
	 * 方法前
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Buyer buyer = (Buyer) request.getSession().getAttribute(Constants.BUYER_NAME);
		if(buyer!=null){
			request.setAttribute("isLogin", true);
		}else{
			request.setAttribute("isLogin", false);
		}
		String requestURI = request.getRequestURI();
		if(requestURI.startsWith(URL_INTECEPTOR)){
			if(buyer!=null){
				return true;
			}else{
				String returnUrl = request.getParameter("returnUrl");
				response.sendRedirect("/shopping/login.shtml?returnUrl="+returnUrl);
				return false;
			}
		}
		return true;
	}

	/**
	 * 方法后
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 页面渲染后
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
