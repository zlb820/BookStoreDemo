package cn.zlb.goods.user.web;

/**
 * ajax 异步校验 ：
 * 这是后台代码，还需要页面上的ajax代码，来提交异步请求
 * 
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.user.domin.User;
import cn.zlb.goods.user.exception.userException;
import cn.zlb.goods.user.service.userService;

@WebServlet("/userServlet")
public class userServlet extends BaseServlet {
	userService service = new userService();

	/*
	 * 用户名是否注册的校验*
	 */
	public String ajaxValidateLoginname(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("用户名的ajax校验");
		String loginname = request.getParameter("loginname");
		boolean b = service.ajaxValidateLoginname(loginname);
		response.getWriter().print(b);
		System.out.println("返回用户名校验结果");
		return null;

	}

	/*
	 * email是否注册的校验*
	 */
	public String ajaxValidateEmail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("email");
		boolean b = service.ajaxValidateEmail(email);
		response.getWriter().print(b);

		return null;

	}

	/*
	 * 验证码是否正确*
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取填写的验证码
		String verifycode = request.getParameter("verifyCode");

		// 获取图片的验证码
		String vcode = (String) request.getSession().getAttribute("vCode");

		boolean b = verifycode.equalsIgnoreCase(vcode);
		response.getWriter().print(b);
		return null;

	}

	/**
	 * 用户注册的校验
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)

	throws ServletException, IOException {
		System.out.println("servlet-regist----------");
		// 1.把表单提交的数据进行封装
		User formuser = CommonUtils.toBean(req.getParameterMap(), User.class);

		// 2.对用户表单信息校验：即 后台校验
		validateRegist rg = new validateRegist();
		Map<String, String> errors = rg.checkRegist(formuser, service,
				req.getSession());
		// 如果错误信息errors 不为空，把校验后的错误信息 和 用户已经填写的 信息 返回到注册页面
		if (errors.size() > 0) {
			req.setAttribute("form", formuser);
			req.setAttribute("errors", errors);
			/*
			 * req.getRequestDispatcher("/jsps/user/regist.jsp").forward(req,
			 * resp);
			 */
			return "f:/jsps/user/regist.jsp";
		}

		// 3.将用户信息进行储存
		service.regist(formuser);

		// 4.保存成功信息，返回页面
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册成功,前往邮箱激活验证！！");
		/* req.getRequestDispatcher("/jsps/msg.jsp").forward(req, resp); */
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 
	 * 用户注册激活
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException,IOException{
		/**
		 * 1.获取验证码，进行比较
		 *  2.激活码调用 
		 *  3.失败返回自定义异常的信息
		 */
		String code = req.getParameter("activationCode");
		try {
			service.activation(code);
			// 成功就返回成功信息
			req.setAttribute("code", "success");
			req.setAttribute("msg", "激活成功！！");
		} catch (userException e) {
			// 激活异常
			req.setAttribute("code", "error");
			// 把service抛出的异常信息返回
			req.setAttribute("msg", e.getMessage());
		}

		return null;

	}

	/**
	 * 用户登录
	 * @throws SQLException 
	 * 
	 */
 
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		System.out.println("login--------------");
		// 1.把用户登录的信息封装到bean
		User formuser = CommonUtils.toBean(req.getParameterMap(), User.class);

		// 2.对登陆的用户信息进行后台校验
			validateLogin vl=new validateLogin();
		   Map<String, String>errors=vl.checkLogin(formuser, service, req.getSession()); //如果map大于零那么登录信息校验成功
		   if (errors.size()>0) { 
		   //有错误返回登录 
			   req.setAttribute("errors", errors);
			   req.setAttribute("msg", "用户名或者密码错误"); 
		   //把用户信息返回，用于回显
			   req.setAttribute("form", formuser);
				return "f:/jsps/user/login.jsp";
		  }
		

		// 3.0校验无误 ，数据库查询
	  
		 User checkUser=checkUser = service.checkLogin(formuser);
		 System.out.println(checkUser.getLoginname());
		 
		/*
		 * 4.0 判断数据库查询结果 
		 * 1.查询结果是否为空,空则返回错误信息
		 * 2.不为空则查看是否激活，未激活返回错误信息
		 * 3.成功，返回成功信息
		 */
		if (checkUser == null) {
			req.setAttribute("msg", "用户名或者密码错误！");
			req.setAttribute("form", formuser);
			return "f:/jsps/user/login.jsp";
		} else {
			if (!checkUser.isStatus()) {
				// 未激活
				req.setAttribute("form", formuser);
				req.setAttribute("msg", "用户未激活！！！");
				return "f:/jsps/user/login.jsp";
			} else {
				// 登陆成功,保存用户性到cookie
				System.out.println("登陆成功！");
				req.getSession().setAttribute("sessionuser", checkUser);
				String loginname = checkUser.getLoginname();
				try {
					loginname = URLEncoder.encode(loginname, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cookie cookie = new Cookie("cookie", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);
				resp.addCookie(cookie);
				return "r:/index.jsp";

			}
		}

	}

	/**
	 * 退出登录
	 */
	public String quitLogin(HttpServletRequest req,HttpServletResponse resp)
	throws  ServletException,IOException{
		req.getSession().invalidate();
		System.out.println("退出登录");
		return "f:/jsps/user/login.jsp";
		
		
	}
}
