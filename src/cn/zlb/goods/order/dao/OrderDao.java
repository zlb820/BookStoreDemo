package cn.zlb.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.book.dao.Expression;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.book.pager.PageConstant;
import cn.zlb.goods.order.domin.Order;
import cn.zlb.goods.order.domin.OrderItem;
import cn.zlb.goods.order.tools.OrderItemTools;
import cn.zlb.goods.pager.PagerBean;
import cn.zlb.goods.order.tools.*;
public class OrderDao {
private TxQueryRunner qr=new TxQueryRunner();

/**
 * 2.0 添加 一个 Order
 * 1》 首先把Order订单 信息插入
 * 2》一个Order可能包含多个OrderItem因此 需要迭代添加
 * @param order
 * @throws SQLException 
 */
public void addOrder(Order order) throws SQLException{
	//添加Order 
	String sql="insert into t_order values(?,?,?,?,?,?)";
	Object []params={order.getOid(),order.getOrdertime(),order.getTotal(),order.getStatus(),
			order.getAddress(),order.getOwner().getUid()};
	qr.update(sql,params);
	
	//添加OrderItem
	sql="insert into t_orderitem values(?,?,?,?,?,?,?,?)";
	List<OrderItem> orderItemList=order.getOrderItemList();
	//创建二维数组，object[][]  ，使用批处理添加OrderItem,二维数组的每一维度对应一个OrderItem
	 Object [][]items=new Object[orderItemList.size()][];
	//循环遍历orderItemList
	for (int i=0;i<orderItemList.size();i++) {
		//每有一个OrderItem便 初始化一个数组
		  items[i]=new Object[]{orderItemList.get(i).getOrderItemId(),orderItemList.get(i).getQuantity(),
				  orderItemList.get(i).getSubtotal(),orderItemList.get(i).getBook().getBid(),
				  orderItemList.get(i).getBook().getBname(),
			orderItemList.get(i).getBook().getCurrPrice(),orderItemList.get(i).getBook().getImage_b(),
			orderItemList.get(i).getOrder().getOid()};
		  System.out.println("items[][]:"+items[i]);
	}
	
	//批处理
	qr.batch(sql, items);
 }


/**1.0
 * 查询用户的订单
 * @param uid
 * @param pc
 * @return
 * @throws SQLException
 */
public PagerBean<Order> findByUser(String uid,int pc) throws SQLException{
	List<Expression> list=new ArrayList<Expression>();
	list.add(new Expression("uid","=",uid));
	
	return findByCriteria(list, pc);
 
}

/**
 * sql查询的通用类
 * 参数：Expression 表达式 、pc当前页码
 * @throws SQLException 
 */
@SuppressWarnings("unused")
private PagerBean<Order>findByCriteria(List<Expression> explist,int pc) throws SQLException{
	/**
	 * 1.获得每页记录数  ps 常量
	 * 2.获得查询结果总记录数 tr
	 * 3.查询结果 Book bean的list集合
	 * 4.把结果集合 封装到PagerBean里
	 */
	
	//1.0 获取 每页记录
	int ps=PageConstant.Page_Order_Size;
	
	/**
	 * 2.0 拼接sql语句的where条件，
	 */
		//定义whereSql
	StringBuilder whereSql=new StringBuilder(" where 1=1");
	 	//定义sql查询的参数,sql语句中"?"对应的参数
	List<Object> params=new ArrayList<Object>();
		//开始拼接sql语句。遍历Expression表达式list
	 for (Expression exp:explist) {
		//添加and条件
		 whereSql.append(" and ").append(exp.getName()).append(" ").append(exp.getOperator()).append(" ");
		 //判断Expression 中的operator 操作符是否是 is null
		 if (!exp.getOperator().equalsIgnoreCase("is null")) {
			//如果不是is null 那么添加一个 ？ 占位符
			 whereSql.append("?");
			  //把表达式中的，要进行判断的值 取出，添加到 params 中
			 params.add(exp.getValue());
		}
	}
	 
	 /**
	  * 2.0 查询符合条件的总记录数
	  */
	 String sql="select count(*) from t_order"+whereSql;
	 //ScalarHandler 获取结果集某一行数据并转换成返回类型的对象
	 Number num=(Number)qr.query(sql, new ScalarHandler(),params.toArray());
	 int tr=num.intValue();
	 
	 /**
	  * 3.0 查询当前页的结果集
	  * 
	  */
	 //limit 从第一个问号记录的吓一条记录开始，取个数为第二个问号数量的 记录数
	sql="select * from t_order"+whereSql+"order by oid desc limit ?,?";
	/*System.out.println(sql);*/
	//根据页码计算查询记录数，把结果存入params，params 是可变参数，底层是数组，直接传入就好，可以同上面的占位符？一起计算
	params.add((pc-1)*ps);
	params.add(ps);
	/**
	 * 按照 Order.class  映射查询数据库表 表中的外键即  不包含在Order.class 中的字段不会被映射到BeanListHander()
	 * 
	 * >每个订单Order 都包含多个订单项：需要在每一订单中加入他包含的订单项OrderItem
	 * 	循环遍历Order-->找出相应的子项-->add
	 */
	List<Order> beanlist=qr.query(sql, new BeanListHandler<Order>(Order.class),params.toArray());
	
	for (Order order : beanlist) {
		 findOrderItems(order);
	}
	
	/**
	 * 4.0把数据封装到PagerBean中
	 */
	PagerBean<Order> pagerbean=new PagerBean<Order>();
	pagerbean.setBeanlist(beanlist);
	pagerbean.setPc(pc);
	pagerbean.setTr(tr);
	pagerbean.setPs(ps);
	
	return pagerbean;
	
	
}
/**辅助类01：
 * 查询Order子项OrderItem
 * @param order
 * @throws SQLException 
 */
private void findOrderItems(Order order) throws SQLException {
	String sql="select * from t_orderitem where oid = ?";
	List<Map<String, Object>> mapList=qr.query(sql, new MapListHandler(),order.getOid());
    order.setOrderItemList(OrderItemTools.toOrderItemList(mapList));
	
}
 
 
}











