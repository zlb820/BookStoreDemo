package cn.zlb.goods.book.sservlet;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.book.service.BookService;
import cn.zlb.goods.pager.PagerBean;

public class BookServlet  extends BaseServlet{
	private BookService service =new BookService();
	/**
	 * 1.0 按照bid查询
	 */
	public String findByBid(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException{
		String bid=req.getParameter("bid");
		Book book=service.findById(bid );
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
		
		
	}
	//--------------------------------------------------------------------------
	/**
	 * 按照目录分页查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 */
	public String findCategory(HttpServletRequest req,HttpServletResponse resp) 
	throws ServletException{
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
		 * 3.获取分类信息，查询分类目录图书信息
		 */
		String cid=req.getParameter("cid");
		PagerBean<Book> pagerbean=service.findByCategory(cid, pc);
		//4.把uri 封装到PagerBean
		pagerbean.setUrl(uri);
		req.setAttribute("pagerbean", pagerbean);
		
		
		return "f:/jsps/book/list.jsp";
		
		
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
