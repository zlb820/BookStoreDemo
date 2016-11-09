package cn.zlb.goods.cart.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.zlb.goods.cart.dao.CartItemDao;
import cn.zlb.goods.cart.domin.CartItem;

public class CartItemService {
	private CartItemDao dao=new CartItemDao();
	
	/**
	 * 1.0 按照用户查询CartItem
	 * 
	 */
	public List<CartItem > findByUser(String uid){
		List<CartItem> cartItemList =null;
		try {
		  cartItemList=dao.findByUser(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartItemList;
 
	}
	/**
	 * 2.0购物 添加商品实现
	 */
	public void addCartItem(CartItem cartitem){
		 System.out.println("添加购买图书");
		try {
			CartItem _cartitem=dao.findBookInCart(cartitem.getUser().getUid(),
					cartitem.getBook().getBid());
			if (_cartitem != null) {
				/*存在该条目 那么 update,
				 * 更新条目需要 传入新的数量=原数量+新数量
				 * 并且id参数 也是 查询出来的 旧id
				 * */
				System.out.println("--service update cartitem--");
				int quantity=_cartitem.getQuantity()+cartitem.getQuantity();
				dao.updateBookInCart(_cartitem.getCartItemId(), quantity);
				
			}else{
				System.out.println("--service insert cartitem");
				//添加新的条目 需要生成条目id：cartItemId
				cartitem.setCartItemId(CommonUtils.uuid());
				dao.insertInCart(cartitem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 3.0删除cartitem
	 */
	public void deleteCartItem(String cartitems){
		
		try {
			dao.deleteCartItem(cartitems);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 /**
	  * 4.0
	  * 	增加减少订单数量的实现
	  * 先更新数量，然后再次查找并返回CartItem 
	  * @param cartitemid
	  * @return
	  */
	public CartItem updateQuantity(String cartItemId,int quantity){
		CartItem cartitem=null;
		try {
			dao.updateBookInCart(cartItemId, quantity);
			
			cartitem = dao.findByCartItemId(cartItemId);
			return cartitem;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cartitem;
	}
	/**
	 * 5.0 结算 提交选中的 多个 cartitem
	 * @param cartItemId
	 * @return
	 */
	public List<CartItem> findCheckedItems(String cartItemId){
		List<CartItem> list=null;
		try {
			return list=dao.findCheckedCartItem(cartItemId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

















