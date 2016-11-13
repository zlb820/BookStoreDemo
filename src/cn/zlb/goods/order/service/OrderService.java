package cn.zlb.goods.order.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.RollbackException;

import cn.itcast.jdbc.JdbcUtils;
import cn.zlb.goods.order.dao.OrderDao;
import cn.zlb.goods.order.domin.Order;
import cn.zlb.goods.pager.PagerBean;

public class OrderService {
private OrderDao dao=new OrderDao();
/**
 * 5.0 取消订单
 * 5.1 确认收货
 * @param ord
 * @throws SQLException 
 */
public  int  viewOrderStatus(Order ord)  {
  try {
	return dao.viewOrderStatus(ord);
} catch (SQLException e) {
	throw new RuntimeException(e);
}
 
}

public void changeOrderStatus(Order ord){
	try {
		dao.changeOrderStatus(ord);
	} catch (SQLException e) {
		 
	}
	 
}
/**
 * 3.0 查看Order
 * @param ord
 * @return
 */
public Order viewOrder(Order ord ){
	Order order=null;
	try {
		JdbcUtils.beginTransaction();
		order = dao.viewOrder(ord );
		JdbcUtils.commitTransaction();
	} catch (SQLException e) {
		try {
			JdbcUtils.rollbackTransaction();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	return order;
}
/**
 * 2.0 添加订单
 * @param order
 */
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
