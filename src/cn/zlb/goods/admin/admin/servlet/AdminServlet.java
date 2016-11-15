package cn.zlb.goods.admin.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.admin.admin.domin.Admin;
import cn.zlb.goods.admin.admin.service.Adminservice;

public class AdminServlet extends BaseServlet {
private Adminservice service =new Adminservice();

/**
 * 1.0 admin登陆
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
	public  String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			Admin adm=CommonUtils.toBean(req.getParameterMap(), Admin.class);
			Admin admin=service.login(adm);
			if (admin==null) {
				req.setAttribute("msg", "管理员账户不存在！！");
				return "f:/adminjsps/login.jsp";
			}else{
				req.getSession().setAttribute("adminuser", admin);
				return "f:/adminjsps/admin/index.jsp";
			}
	}

	/**
	 * 2.0 admin 登出
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String loginOut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//清除session中的  用户 ，并返回到登陆
		req.getSession().removeAttribute("adminyser");
		
		return "f:/adminjsps/login.jsp";
	}
	
	
	
}
