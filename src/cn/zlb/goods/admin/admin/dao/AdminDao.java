package cn.zlb.goods.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.admin.admin.domin.Admin;

public class AdminDao {
private TxQueryRunner qr=new TxQueryRunner();


/**
 * 1.0 管理员登录校验
 * @param adm
 * @return
 * @throws SQLException 
 */
public Admin login(Admin adm) throws SQLException{
	String sqlStr="select * from t_admin where adminname=? and adminpwd= ?";
	Object params[]={adm.getAdminname(),adm.getAdminpwd()};
	Admin admin=qr.query(sqlStr, new BeanHandler<Admin>(Admin.class),params);
	return admin;
}
}
