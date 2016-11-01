package cn.zlb.goods.user.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.zlb.goods.user.domin.User;
import cn.zlb.goods.user.service.userService;

public class validateLogin {
	protected Map<String, String> checkLogin(User user,userService service,HttpSession session){
		Map<String, String> error=new HashMap<String, String>();	
		/*
		 * 1.用户名的校验
		 * */
		String loginname=user.getLoginname();
		if(loginname==null&&loginname.trim().isEmpty()){
			error.put("loginname", "用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			error.put("loginname", "用户名必须在3-20之间！");
		} 
		
		/**
		 * 2.0密码的校验
		 * 
		 */
		String loginpass=user.getLoginpass();
		if (loginpass==null && loginpass.trim().isEmpty()) {
			error.put("loginpass", "密码不能为空！！");
		} else if(loginpass.length()<3 || loginpass.length()>20){
			error.put("loginpass", "密码长度2-20！！");
		}
		
		/**
		 * 5.0 验证码的校验
		 */
		String verifycode=user.getVerifyCode();
		//获取session 中的 验证码
		String vcode=(String) session.getAttribute("vCode");
		if (verifycode==null && verifycode.trim().isEmpty()) {
			error.put("verifyCode", "验证码不能为空！！");
		}else if(!verifycode.equalsIgnoreCase(vcode)){
			error.put("verifyCode", "验证码错误！！");
			
		}
		
		return error;
		
		
	}
}
