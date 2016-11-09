package cn.zlb.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.cart.domin.CartItem;
import cn.zlb.goods.user.domin.User;

public class CartItemDao {
	private TxQueryRunner qr=new TxQueryRunner();

	/**
	 * 1.0 按照用户 查找该用户的购物车信息
	 * @throws SQLException 
	 */
	
	public List<CartItem> findByUser(String uid) throws SQLException{
		System.out.println("购物车查找");
		//order by t.orderBy sql中必须指明 是哪个表的orderBy字段。
		String sql="SELECT * FROM t_cartitem t,t_book b WHERE t.bid=b.bid  and t.uid = ? order by t.orderBy";
		
		List<Map<String, Object>>  map=qr.query(sql, new MapListHandler(), uid);
		
		return toCartItemList(map);
 
	}
	/**
	 * 2.0购物的流程 
	 * 1>首先查询用户 数据库中是不是存在这本书 的记录
	 * 2.2>存在的话就 update quantity
	 * 2.2>不存在就 insert
	 * @throws SQLException 
	 */
	       //查询用户选择的 该本书 是否存在
	public CartItem findBookInCart(String uid,String bid) throws SQLException{
		String sql="select * from t_cartitem where uid=? and bid=?";
		/*Object param[] ={uid,bid};*/
		Map<String , Object> map=qr.query(sql, new MapHandler(),uid,bid);
		CartItem cartitem =toCartItem(map);
		return cartitem;
		
	} 
		//如果该本书存在 那么更新记录
	public void updateBookInCart(String cartitemid,int quantity) throws SQLException{
		String sql="update t_cartitem set quantity=? where cartItemId=?";
		qr.update(sql, quantity,cartitemid);
		 
	}
		//该本书不存在 那么插入一条记录
	public void insertInCart(CartItem cartitem) throws SQLException{
		System.out.println("--insert into cartitem");
		/*String sql="insert into t_cartitem(cartItemId,quantity,bid,uid) values(?,?,?,?)";*/
		String sql = "insert into t_cartitem(cartItemId, quantity, bid, uid)" +
				" values(?,?,?,?)";
		Object param[]={cartitem.getCartItemId(),cartitem.getQuantity(),
				cartitem.getBook().getBid(),cartitem.getUser().getUid()};
		qr.update(sql,param);
		
	}
	
	/**
	 * 3.0删除cartitem
	 * 实现功能 删除一条记录和 多条记录 
	 * ：参数是String类型 ：gaistring类型是多个cartItemId连接的结果
	 * ：定义拼装sql 方法，多少个参数 就添加多少个？
	 * @throws SQLException 
	 */
	
	public void deleteCartItem(String cartitems) throws SQLException{
		Object params[]=cartitems.split(",");
		//计算数组的长度 ，决定？ 参数的个数
		String str=toSql(params.length);
		String sql="delete from t_cartitem where "+str;
		System.out.println("delete sql:"+sql);
		//取得参数
		
		qr.update(sql,params);
	}
	/**
	 * 4.0 购物车条目数量的修改
	 * 		1》购物条目数量的修改
	 * 		2》查询修改后的购物条目
	 * @throws SQLException 
	 */
		//购物车中条目的查询
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql="select * from t_cartitem c , t_book b where c.bid=b.bid and c.cartItemId=?";
		
	    Map<String, Object> map=qr.query(sql, new MapHandler(),cartItemId);
	     
		return toCartItem(map); 
	}
	
	/**
	 * 5.0查找选中的物品项  返回  结算功能
	 * 传入 cartItemId 字符创，需要解析 拼接
	 * @param cartItemId
	 * @return
	 * @throws SQLException 
	 */
	public List<CartItem> findCheckedCartItem(String cartItemId) throws SQLException{
		Object params[]=cartItemId.split(",");
		
		String strsql=toSql(params.length);
		
		String sql="select * from t_cartitem t ,t_book b where t.bid=b.bid and  "+strsql;
		System.out.println("结算sql："+sql);
		List<Map<String, Object>> map=qr.query(sql, new MapListHandler(),params);
		return toCartItemList(map);
	}
	
	/**
	 * 辅助工具类1：
	 * 把查询出来的map结果映射到bean中，相当于cartitem.setString();
	 
	 */
	public CartItem toCartItem(Map<String,Object> map){
		if (map==null || map.size()==0)  
			return null;
		//set CartItem 
		CartItem cartitem=CommonUtils.toBean(map, CartItem.class);
		//set Book
		Book book=CommonUtils.toBean(map, Book.class);
		//set User
		User user=CommonUtils.toBean(map, User.class);
		cartitem.setBook(book);
		cartitem.setUser(user);
		return cartitem; 
	}
	
	/**
	 * 辅助工具类2：
	 * 把查询出来的map结果映射到bean中，相当于cartitem.setString();
	 
	 */
	 public List<CartItem> toCartItemList(List<Map<String, Object>> maplist){
		 List<CartItem> cartItemList=new ArrayList<CartItem>();
		 for (Map<String, Object> map : maplist) {
			 CartItem  cartItem=toCartItem(map);
			 cartItemList.add(cartItem);
		 		}
		return  cartItemList;
  
	 }
	 
	 /**
	  * 辅助工具类3：
	  * 拼装sql方法
	   *
	  */
public String toSql(int len){
	StringBuilder str=new StringBuilder("cartItemId in (");
	 for (int i = 0; i < len; i++) {
		str.append("?");
		if (i<(len-1)) {
			str.append(",");
		}
	}
	 str.append(")");
	return  str.toString();
} 
}














