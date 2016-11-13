package cn.zlb.goods.order.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import cn.zlb.goods.order.tools.PaymentUtil;
import cn.zlb.goods.pager.PagerBean;
import cn.zlb.goods.user.domin.User;

public class OrderServlet extends BaseServlet {
 private OrderService service =new OrderService();
 //引入CartService
 private CartItemService cService=new CartItemService();
 /**
  * 5.0 支付模块
  * 
  * 支付借助第三方：易宝支付 使用易宝测试接口
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 //跳转支付页面 ，支付按钮请求
 /**
  * 当点击  ‘支付’按钮，跳转到支付界面，选择支付通道
  * 需要传递订单编号用于基本信息显示
  *   */
 public String  payMentPre(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	 String oidStr=req.getParameter("oid");
	 Order ord =new Order();
	 ord.setOid(oidStr);
	 //查询订单
	 Order order=service.viewOrder(ord);
	 req.setAttribute("order", order);
	 return  "f:/jsps/order/pay.jsp";
	 }
 //支付方法
 public String  pay(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	 Properties props =new Properties();
	 props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
	 
	 /*
		 * 1. 准备13个参数
		 */
		String p0_Cmd = "Buy";//业务类型，固定值Buy
		String p1_MerId = props.getProperty("p1_MerId");//商号编码，在易宝的唯一标识
		String p2_Order = req.getParameter("oid");//订单编码
		String p3_Amt = "0.01";//支付金额
		String p4_Cur = "CNY";//交易币种，固定值CNY
		String p5_Pid = "";//商品名称
		String p6_Pcat = "";//商品种类
		String p7_Pdesc = "";//商品描述
		String p8_Url = props.getProperty("p8_Url");//在支付成功后，易宝会访问这个地址。
		String p9_SAF = "";//送货地址
		String pa_MP = "";//扩展信息
		String pd_FrpId = req.getParameter("yh");//支付通道
		String pr_NeedResponse = "1";//应答机制，固定值1
		
		
		/*
		 * 2. 计算hmac
		 * 需要13个参数
		 * 需要keyValue
		 * 需要加密算法
		 * PaymentUtil 明文转密文算法由易宝提供
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
		
		/*
		 * 3. 重定向到易宝的支付网关
		 */
		StringBuilder sb = new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
		sb.append("?").append("p0_Cmd=").append(p0_Cmd);
		sb.append("&").append("p1_MerId=").append(p1_MerId);
		sb.append("&").append("p2_Order=").append(p2_Order);
		sb.append("&").append("p3_Amt=").append(p3_Amt);
		sb.append("&").append("p4_Cur=").append(p4_Cur);
		sb.append("&").append("p5_Pid=").append(p5_Pid);
		sb.append("&").append("p6_Pcat=").append(p6_Pcat);
		sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
		sb.append("&").append("p8_Url=").append(p8_Url);
		sb.append("&").append("p9_SAF=").append(p9_SAF);
		sb.append("&").append("pa_MP=").append(pa_MP);
		sb.append("&").append("pd_FrpId=").append(pd_FrpId);
		sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
		sb.append("&").append("hmac=").append(hmac);
		
		resp.sendRedirect(sb.toString());
				return null;}
 /**
  * 支付回调方法，当支付后易宝会调用本方法，
  * 在方法内，对易宝返回数据进行判断比较，是否支付成功，
  * 进行订单状态更改或者返回错误！
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 public String  payBack(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	 /*
		 * 1. 获取12个参数
		 */
		String p1_MerId = req.getParameter("p1_MerId");
		String r0_Cmd = req.getParameter("r0_Cmd");
		String r1_Code = req.getParameter("r1_Code");
		String r2_TrxId = req.getParameter("r2_TrxId");
		String r3_Amt = req.getParameter("r3_Amt");
		String r4_Cur = req.getParameter("r4_Cur");
		String r5_Pid = req.getParameter("r5_Pid");
		String r6_Order = req.getParameter("r6_Order");
		String r7_Uid = req.getParameter("r7_Uid");
		String r8_MP = req.getParameter("r8_MP");
		String r9_BType = req.getParameter("r9_BType");
		String hmac = req.getParameter("hmac");
		/*
		 * 2. 获取keyValue
		 */
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
		String keyValue = props.getProperty("keyValue");
		/*
		 * 3. 调用PaymentUtil的校验方法来校验调用者的身份
		 *   >如果校验失败：保存错误信息，转发到msg.jsp
		 *   >如果校验通过：
		 *     * 判断访问的方法是重定向还是点对点，如果要是重定向
		 *     修改订单状态，保存成功信息，转发到msg.jsp
		 *     * 如果是点对点：修改订单状态，返回success
		 */
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
				r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
				keyValue);
		if(!bool) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无效的签名，支付失败！ ");
			return "f:/jsps/msg.jsp";
		}
		if(r1_Code.equals("1")) {
			Order ord=new Order();
			ord.setStatus(2);
			service.changeOrderStatus(ord); 
			if(r9_BType.equals("1")) {
				req.setAttribute("code", "success");
				req.setAttribute("msg", "恭喜，支付成功！");
				return "f:/jsps/msg.jsp";				
			} else if(r9_BType.equals("2")) {
				resp.getWriter().print("success");
			}
		}
				return null;}

 /**
  * 4.0 取消订单
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 public String  cancelOrder(HttpServletRequest req, HttpServletResponse resp )
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
		 return "f:/jsps/order/msg.jsp";
	}
	 
	 ord.setStatus(5);
	 service.changeOrderStatus(ord);
	 req.setAttribute("code", "success");
	 req.setAttribute("msg", "订单取消成功!!");
				return "f:/jsps/order/msg.jsp";
				}
 /**
  * 5.0 确认订单
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 public String  confirmOrder(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	//获取订单id
		 String oid=req.getParameter("oid");
		 Order ord=new Order();
		 ord.setOid(oid);
		 //查询订单状态并比较
		 int status=service.viewOrderStatus(ord);
		 if (status!=3) {
			//订单不是未付款状态，不能确认订单
			 req.setAttribute("code", "error");
			 req.setAttribute("msg", "订单不能确认!!");
			 return "f:/jsps/order/msg.jsp";
		}
		 
		 ord.setStatus(4);
		 service.changeOrderStatus(ord);
		 req.setAttribute("code", "success");
		 req.setAttribute("msg", "交易成功!!!");
					return "f:/jsps/order/msg.jsp";
					}
 /**
  * 3.0 查询订单详情
  * @param req
  * @param resp
  * @return
  * @throws ServletException
  * @throws IOException
  */
 public String  viewOrder(HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
	 //获取oid
	 String oid=req.getParameter("oid");
	 Order ord =new Order();
	 ord.setOid(oid);
	 
	 // 调用方法
	 Order order=service.viewOrder(ord);
	 
	 //返回信息
	 req.setAttribute("order", order);
	 //获取 查看 取消  确认 按钮的信息 并返回desc.jsp用于显示
	 String btn=req.getParameter("btn");
	 req.setAttribute("btn", btn);
	  return "f:/jsps/order/desc.jsp";}
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
