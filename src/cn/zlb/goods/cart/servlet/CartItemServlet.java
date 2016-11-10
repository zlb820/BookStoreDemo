package cn.zlb.goods.cart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.cart.domin.CartItem;
import cn.zlb.goods.cart.service.CartItemService;
import cn.zlb.goods.user.domin.User;

public class CartItemServlet extends BaseServlet {
 private CartItemService service=new CartItemService();
 	/**
 	 * 1.0 查询用户的购物侧
 	 * @param req
 	 * @param resp
 	 * @return
 	 * @throws ServletException
 	 * @throws IOException
 	 */
	public String findByUser(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
		//1.从session中取出用户id
			User user= (User) req.getSession().getAttribute("sessionuser");
			String uid=user.getUid();
		//2.查询
			List<CartItem> cartItemList=service.findByUser(uid);
		
			req.setAttribute("cartItemList", cartItemList);
			
			return "f:/jsps/cart/list.jsp";
	}
	/**
	 * 2.0添加购买的物品
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String addCartItem(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
			/**
			 * 需要封装CartItem对象：
			 * 封装 Book和 User对象 在CartItem里
			 */
				Map map=req.getParameterMap();
				CartItem cartitem =CommonUtils.toBean(map, CartItem.class);
				
				Book book=CommonUtils.toBean(map, Book.class);
				User user=(User) req.getSession().getAttribute("sessionuser");
				
				cartitem.setBook(book);
				cartitem.setUser(user);
				
				//调用方法
				service.addCartItem(cartitem);
				// 结尾 调用 查询用户购物车方法
				return  findByUser(req, resp) ;
				}
	/**
	 * 3.0删除cartitem
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteCartItem(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
		//请求参数为 String cartitem 字符串
		String cartitems=req.getParameter("cartitems");
		service.deleteCartItem(cartitems);
		return  findByUser(req, resp);
	}
	
	/**
	 * 4.0 修改购物条目数量
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String modifyQuantity(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
		//获取传来的 数量和id
		String cartitemid=req.getParameter("cartItemId");
		int quantity =Integer.parseInt(req.getParameter("quantity"));
		
		CartItem cartitem=service.updateQuantity(cartitemid, quantity);
		
		//更新的结果  返回json数据,封装改变后的物品数量和 小计
		JsonObject object=new JsonObject();
		object.addProperty("quantity", cartitem.getQuantity());
		object.addProperty("totalPrice", cartitem.getTotalPrice());
		System.out.println("json return :" +object.toString());
		resp.getWriter().print(object.toString());
		return "";
		
	}
	/**
	 * 5.0 结算流程  
	 * 获取 cartitems 字符串 ，并查找
	 * 返回查询结果 
	 * 返回 总计
	 *shouitem.jsp 显示订单
	 */
	public String  jiesuan(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
	String cartItems=req.getParameter("cartItemIds");
	//获得选中 cartitem的总计
	 String totalPrice=req.getParameter("totalPrice");
	 double total=Double.parseDouble(totalPrice);
	 System.out.println(total);
	List<CartItem> cartItemLists=service.findCheckedItems(cartItems);
	
	req.setAttribute("cartItemLists", cartItemLists);
	req.setAttribute("totalPrice", total);
	//把所有的cartitem id字符串 返回到cart/showitem
	req.setAttribute("cartItemIds", cartItems);
		return "f:/jsps/cart/showitem.jsp";
	}

}










