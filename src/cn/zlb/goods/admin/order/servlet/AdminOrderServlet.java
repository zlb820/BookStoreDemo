package cn.zlb.goods.admin.order.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.order.domin.Order;
import cn.zlb.goods.order.service.OrderService;
import cn.zlb.goods.pager.PagerBean;
import cn.zlb.goods.user.domin.User;

public class AdminOrderServlet extends BaseServlet {
private OrderService service=new  OrderService();
 


/**
 * 5.0取消订单
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */

public String  adminSendOrder(HttpServletRequest req, HttpServletResponse resp )
		throws ServletException, IOException {
 //获取订单id
 String oid=req.getParameter("oid");
 Order ord=new Order();
 ord.setOid(oid);
 
 
 //查询订单状态并比较
 int status=service.viewOrderStatus(ord);
 if (status!=2) {
	//订单不是未付款状态，不能取消订单
	 req.setAttribute("code", "error");
	 req.setAttribute("msg", "不能发货!!");
	 return "f:/adminjsps/admin/msg.jsp";
}
 
 ord.setStatus(3);
 service.changeOrderStatus(ord);
 req.setAttribute("code", "success");
 req.setAttribute("msg", "已发货!!");
 return "f:/adminjsps/admin/msg.jsp";
			}


/**
 * 4.0取消订单
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */

public String  adminCancelOrder(HttpServletRequest req, HttpServletResponse resp )
		throws ServletException, IOException {
 //获取订单id
 String oid=req.getParameter("oid");
 Order ord=new Order();
 ord.setOid(oid);
 //查询订单状态并比较
 int status=service.viewOrderStatus(ord);
 if (status!=1) {
	//订单不是未付款状态，不能取消订单
	 req.setAttribute("code", "error");
	 req.setAttribute("msg", "订单不能取消!!");
	 return "f:/adminjsps/admin/msg.jsp";
}
 
 ord.setStatus(5);
 service.changeOrderStatus(ord);
 req.setAttribute("code", "success");
 req.setAttribute("msg", "订单取消成功!!");
 return "f:/adminjsps/admin/msg.jsp";
			}

/**
 * 3.0 按照订单的状态查找
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String checkOrderByStatus(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	/**
	 * 1.获得请求参数中的页码,getPc();
	 */
	int pc=getPc(req);
	/**
	 * 2.获取uri
	 * 需要将请求的URI，封装到PagerBean中，以方便进行分页查询的分页页码URI的拼接
	 */
//获取请求订单状态
	String status=req.getParameter("status");
	
	String uri=getURI(req);
    System.out.println("order url:" +uri);
	PagerBean<Order> pagerbean=service.checkOrder(status,pc);
	//4.把uri 封装到PagerBean
	pagerbean.setUrl(uri);
	req.setAttribute("btn", req.getParameter("btn"));
	req.setAttribute("pagerbean", pagerbean);
	
	
	return "f:/adminjsps/admin/order/list.jsp";
	 }
 /**
  * 2.0按照订单的id查询
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
public String checkOrderByOid(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	String oid=req.getParameter("oid");
	Order ord=new Order();
	ord.setOid(oid);
	
	Order order=service.viewOrder(ord);
	
	req.setAttribute("btn", req.getParameter("btn"));
	req.setAttribute("order", order);
	return "f:/adminjsps/admin/order/desc.jsp";}
	



/**
	 * 1.0 查找所有的订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String checkOrder(HttpServletRequest req, HttpServletResponse resp)
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
	/*    System.out.println("order url:" +uri);*/
		PagerBean<Order> pagerbean=service.checkOrder(pc);
		//4.把uri 封装到PagerBean
		pagerbean.setUrl(uri);
		req.setAttribute("pagerbean", pagerbean);
		
		
		return "f:/adminjsps/admin/order/list.jsp";
	}
//------------------------------------------------------------------------------
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
