package cn.zlb.goods.user.web;
/**
 * 这是后台对于注册的每条信息的逐条验证，返回一个map集合
 * 
 */
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.zlb.goods.user.domin.User;
import cn.zlb.goods.user.service.userService;

public   class validateRegist {
	
	protected Map<String,String> checkRegist(User user,userService service,HttpSession session){
		Map<String, String> error=new HashMap<String, String>();	
		/*
		 * 1.用户名的校验
		 * */
		String loginname=user.getLoginname();
		if(loginname==null&&loginname.trim().isEmpty()){
			error.put("loginname", "用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			error.put("loginname", "用户名必须在3-20之间！");
		}else if(!service.ajaxValidateLoginname(loginname)){
			error.put("loginname", "用户名已经注册！");
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
		 * 3.0-确认密码的校验
		 */
		String relogin=user.getReloginpass();
		if (relogin==null && relogin.trim().isEmpty()) {
			error.put("relogin", "密码不能为空");
			
		} else if(!relogin.equals(loginpass)){
			error.put("relogin", "两次密码不一致");

		}
		
		/**
		 * 4.0 email的校验
		 */
		String email=user.getEmail();
		String regex="^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
		if(email==null&&email.trim().isEmpty()){
			error.put("email", "email不能为空！");
		}else if(!email.matches(regex)){
			error.put("email", "email格式错误！！");
		}else if( !service.ajaxValidateEmail(email)){
			error.put("email", "email已经注册！");
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












