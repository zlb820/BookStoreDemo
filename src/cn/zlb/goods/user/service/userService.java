package cn.zlb.goods.user.service;

import java.io.IOException;

import cn.zlb.goods.user.exception.*;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.zlb.goods.user.dao.userDao;
import cn.zlb.goods.user.domin.User;
 
public class userService {
	userDao dao = new userDao();

	// one:验证用户名
	public boolean ajaxValidateLoginname(String loginname) {

		 boolean bool=false;
			try {
				 bool=dao.ajaxValidateLoginname(loginname);
				 return bool;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bool;
	 
		 

	}

	// two:验证email
	public boolean ajaxValidateEmail(String email) {
		boolean bool=false;
		try {
			  bool= dao.ajaxValidateLoginname(email);
			  return bool;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
	}

	// three:用戶注册的信息，对传入的用户信息进行处理，并进行激活邮件的发送

	public void regist(User user) {

		/**
		 * 1.对用户的信息进行补全，注册时的用户信息只有用户名 ，密码，邮箱信息，
		 * 需要补全的信息有:用户的激活状态，用户的激活码，用户的唯一标示id
		 * 
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());

		/**
		 * 2.把补全后的user添加到数据库t_user表
		 * 
		 */
		try {
			dao.add(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/**
		 * 3.激活邮件发送 邮件的所有信息保存在配置文件eamil_template.properties 文件中
		 * 需要的到该文件的properties对象
		 * 
		 */
		Properties prop = new Properties();
		// 读取文件
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 01>登陆邮件服务器
		String host = prop.getProperty("host");
		String name = prop.getProperty("username");
		String pass = prop.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);

		// 02>设置邮件的内容
		String from = prop.getProperty("from");

		// 发送到用户注册的邮箱
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		/*
		 * 字符串模板替换方法，从位置0开始用后面的参数替换之 例子： String msg = "{0}{1}{2}{3} "; Object []
		 * array = new Object[]{"A","B","C","D" }; String value =
		 * MessageFormat.format(msg, array); System.out.println(value); //
		 * 输出：ABCD
		 */
		String content = MessageFormat.format(prop.getProperty("content"),
				user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);

		// 03>发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {

		} catch (IOException e) {

		}

	}

	//four:用户验证码的校验，对比相同则更改，如果已经激活或者不同，抛出自定义异常
	public void activation(String code) throws userException{
		/*
		 * 1.首先根据激活码查询用户，
		 * 	为空，则激活码错误
		 *  不为空，则查看激活码状态是否已经激活*/
		 
			User user;
			try {
				user = dao.checkCode(code);
				//这俩异常必须抛出-------------------
				if (user==null) 
					throw new userException("激活码错误！"); 
				if (user.isStatus()) 
					throw new userException("已经激活！");
				
				
				//上述连个皆不存在，更改用户激活码,
				dao.changeStatus(user.getUid(), true);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
	 	
	}
	
	//five 登录的servvice
	public User checkLogin(User user) throws SQLException  {
		 
	 return dao.findUserByNameAndPass(user.getLoginname(), user.getLoginpass());
	 
	  
	 
	} 
	
	
}








