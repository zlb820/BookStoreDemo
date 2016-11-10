package cn.zlb.goods.order.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.cart.domin.CartItem;
import cn.zlb.goods.cart.service.CartItemService;
import cn.zlb.goods.order.domin.Order;
import cn.zlb.goods.order.domin.OrderItem;
import cn.zlb.goods.order.service.OrderService;
import cn.zlb.goods.pager.PagerBean;
import cn.zlb.goods.user.domin.User;

public class OrderServlet extends BaseServlet {
 private OrderService service =new OrderService();
 //引入CartService
 private CartItemService cService=new CartItemService();
 /**
  * 2.0 添加Order，提交订单
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 public String  addOrder(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	 /**
	  * 1.获取所有cartitem信息
	  */
	 String cartItemIds=req.getParameter("cartItemIds");
	 //通过CartItemService查找所有cartitem
	 List<CartItem> cartItemList=cService.findCheckedItems(cartItemIds);
	 //如果 查询结果为空返回错误信息
	 if (cartItemList==null) {
		req.setAttribute("code", "error");
		req.setAttribute("msg", "您没有要购买的商品！！");
		return "f:/jsps/msg.jsp";
	}
	 /**
	  * 2.0创建Order  并添加属性值
	  */
	 Order order =new Order();
	 order.setOid(CommonUtils.uuid());
	 order.setOrdertime(String.format("%tF %<tT", new Date()));
	 order.setStatus(1);
	 order.setAddress(req.getParameter("address"));
	 	User user=(User) req.getSession().getAttribute("sessionuser");
	 order.setOwner(user);
	 		//添加总价到Order,BigDecimal 参数必须为String
	 BigDecimal total=new BigDecimal("0");
	 for (CartItem cartItem : cartItemList) {
		total=total.add(new BigDecimal(cartItem.getTotalPrice()+""));
	}
	 order.setTotal( total.doubleValue());
	 
	 
	 /**
	  * 3.0创建OrderItem
	  * 有多少条cartitem 那就有 多少个OrderItem
	  */
	 
	 List<OrderItem> orderItemList=new ArrayList<OrderItem>();
	 for (CartItem cartItem : cartItemList) {
		OrderItem orderItem=new OrderItem();
		orderItem.setOrderItemId(CommonUtils.uuid());
		orderItem.setQuantity(cartItem.getQuantity());
		orderItem.setSubtotal(cartItem.getTotalPrice());
		orderItem.setBook(cartItem.getBook());
		//添加order到orderItem
		orderItem.setOrder(order);
		//添加每一个orderItem 到orderItemList
	    orderItemList.add(orderItem);
	}
	 
	 /**
	  * 4.0 添加orderItemList到Order
	  */
	 order.setOrderItemList(orderItemList);
	 
	 /**
	  * 5.0 调用service
	  
	  */
	 service.addOrder(order);
	 
	 /**
	  * 6.0 订单提交后需要删除购物车
	  */
	 cService.deleteCartItem(cartItemIds);
	 
	 /**
	  * 7.0保存订单 并返回
	  */
	 req.setAttribute("order", order);
	 return "f:/jsps/order/ordersucc.jsp";}
 
 /**
  * 1.0查找用户的
  * @param request
  * @param response
  * @return
  * @throws ServletException
  * @throws IOException
  */
	public String  findUserOrder(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		/**
		 * 1.获得请求参数中的页码,getPc();
		 */
		int pc=getPc(req);
		/**
		 * 2.获取uri
		 * 需要将请求的URI，封装到PagerBean中，以方便进行分页查询的分页页码URI的拼接
		 */
		String uri=getURI(req);
		/**
		 * 3.获取User信息，查询分类Order
		 */
		 User  user=(User) req.getSession().getAttribute("sessionuser");
		PagerBean<Order> pagerbean=service.findByUser(user.getUid(), pc);
		//4.把uri 封装到PagerBean
		pagerbean.setUrl(uri);
		req.setAttribute("pagerbean", pagerbean);
		
		
		return "f:/jsps/order/list.jsp";
		
 
	}
	
	/**
	 * 获取请求的url
	 * @param req
	 * @return
	 */
	private String getURI(HttpServletRequest req) {
		//getQueryString()：获得查询字符串：“?号以后的字符串不包含问号”---getRequestURI()：获取发出请求字符串的客户端地址
		String uri=req.getRequestURI()+"?"+req.getQueryString();
		//indexOf() :返回指定子字符串在此字符串中第一次出现处的索引。
		int index=uri.indexOf("&pc");
		if (index!=-1) {
			uri=uri.substring(0,index);
			/*uri=uri.substring(1, index);*/
			/**
			 * 在这里出现了分页下一页无法实现的情况，究其原因是因为
			 * 获得URI的 时候从1 开始 ，所以少了一个"/"
			 * 应该是：/goods/BookServlet+method+findByCategory
			 * substring(1, index)=goods/BookServlet+method+findByCategory;
			 */
		}
		return uri;
		/**
		 * 比如: String rui="http://localhost:8080/goods/BookServlet?method=findByCategory&pc=3"
		 * req.getRequestURI()+req.getQueryString()=/goods/BookServlet+method+findByCategory&pc=3;
		 * subString()=/goods/BookServlet+method+findByCategory;
		 */
	}
	/**
	 * 获取请求中的页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		//默认页码为1
		int pc=1;
		//如果有
		String pcStr=req.getParameter("pc");
		if (pcStr!=null && !pcStr.trim().isEmpty()) {
			try {
				pc=Integer.parseInt(req.getParameter("pc"));
				System.out.println("pc="+pc);
			} catch (RuntimeException e) {
				
			}
			
		}
		return pc;
		
		
	}

}
