package cn.zlb.goods.admin.admin.service;

import java.sql.SQLException;

import cn.zlb.goods.admin.admin.dao.AdminDao;
import cn.zlb.goods.admin.admin.domin.Admin;

public class Adminservice {
private AdminDao dao=new AdminDao();

public Admin login(Admin adm){
	Admin admin=null;
	
	try {
		admin=dao.login(adm);
		return admin;
	} catch (SQLException e) {
		System.out.println("admin login error");
	}
	
	return  admin;
}
}
