package cn.zlb.goods.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import cn.zlb.goods.order.dao.OrderDao;
import cn.zlb.goods.order.domin.Order;
import cn.zlb.goods.pager.PagerBean;

public class OrderService {
private OrderDao dao=new OrderDao();
public void addOrder(Order order){
	
	try {
		JdbcUtils.beginTransaction();
		dao.addOrder(order);
		JdbcUtils.commitTransaction();
	} catch (SQLException e) {
		try {
			JdbcUtils.rollbackTransaction();
		} catch (Exception e2) {
		}
	}
}
/**
 * 1.0 查找用户的Order
 * @param uid
 * @param pc
 * @return
 */
public PagerBean<Order> findByUser(String uid,int pc){
	
	PagerBean<Order> pagerbean=null;
	try {
		JdbcUtils.beginTransaction();
		pagerbean = dao.findByUser(uid, pc);
		JdbcUtils.commitTransaction();
		return pagerbean;
	} catch (SQLException e) {
		try {
			JdbcUtils.rollbackTransaction();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	if (pagerbean==null) {
		System.out.println("user Order is null");
		return null;
	}
	return  pagerbean;
}
}
