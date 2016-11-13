package cn.zlb.goods.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import cn.zlb.goods.user.domin.User;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
			/**
			 * 获取 session中的 user，
			 * 如果session为空，那么返回登陆
			 * session不为空 ，通过
			 */
		//首先转换ServletRequest  为 HttpServletRequest 
		HttpServletRequest req=(HttpServletRequest) request;
		User user=(User) req.getSession().getAttribute("sessionuser");
		if (user==null) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "该功能需登陆！！");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		} else{
		
		chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
