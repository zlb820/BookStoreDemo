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
}





