package cn.zlb.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.user.domin.User;

public class userDao {
	TxQueryRunner qr=new TxQueryRunner();
	
	
	
	//one:验证用户名的方法
	public boolean ajaxValidateLoginname(String loginname) throws Exception{
		String sql="select count(1) from t_user where loginname=?";
		Number number=(Number)qr.query(sql,new ScalarHandler(),loginname);
				
		return number.intValue()==0;
	}
	//two:验证email
	public boolean ajaxValidateEmail(String email) throws Exception{
		String sql="select count(1) from t_user where email=?";
		Number number=(Number)qr.query(sql,new ScalarHandler(),email);
				
		return number.intValue()==0;
	}
	
	//three: 注册用户的方法：把用户信息添加到数据库
	public void add(User user) throws SQLException{

		String sql="insert into t_user values(?,?,?,?,?,?)";
		Object param[]={user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActivationCode()};
		qr.update(sql, param);
	}
	
	//four: 登陆的查找，根据用户名和密码查询用户
	public User findUserByNameAndPass(String loginname,String loginpass) throws SQLException{
		String sql="select * from t_user where loginname=? and loginpass =?";
		Object param[]={loginname,loginpass};
		return qr.query(sql, new BeanHandler<User>(User.class),param);
		
		
	}
	/**
	 * 注册激活部分
	 * 1.根据用户传递的激活码查询用户信息
	 * 2.修改用户的激活状态
	 * @throws SQLException 
	 */
	public User checkCode(String code) throws SQLException{
		String sql="select * from t_user where activationCode=?";
		return qr.query(sql,new  BeanHandler<User>(User.class), code); 
	}
	
	//four:把用户的激活状态更改，激活码在数据库中以boolean值存储
	public void changeStatus(String uid,boolean status) throws SQLException{
		String sql="update t_user set status=? where uid =?";
		 Object param[]={status,uid};
		 qr.update(sql, param);
		
	};
}	













