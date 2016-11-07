package cn.zlb.goods.cart.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}










