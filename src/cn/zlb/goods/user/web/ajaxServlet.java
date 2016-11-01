package cn.zlb.goods.user.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.zlb.goods.user.service.userService;

public class ajaxServlet extends HttpServlet {
	userService service=new userService();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");

		System.out.println("用户名的ajax校验");
		String loginname=req.getParameter("loginname");
		boolean b=service.ajaxValidateLoginname(loginname);
		resp.getWriter().print(b);		
		System.out.println("返回用户名校验结果");
	}
}
