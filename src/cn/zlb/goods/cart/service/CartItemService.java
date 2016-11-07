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
	
	 
}

















