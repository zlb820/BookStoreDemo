package cn.zlb.goods.admin.book.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.book.service.BookService;
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.service.CategoryService;
import cn.zlb.goods.pager.PagerBean;

public class AdminAddBookServlet extends BaseServlet {
private CategoryService cservice=new CategoryService();
private	BookService service=new BookService();


/**
 * 5.0修改图书
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String editBook(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	Map  map=req.getParameterMap();
	Book book=CommonUtils.toBean(map, Book.class);
	Category category=CommonUtils.toBean(map, Category.class);
	
	book.setCategory(category);
	
	 service.editBook(book);
	 req.setAttribute("code", "success");
	 req.setAttribute("msg", "修改图书信息成功！！");
	return "f:/adminjsps/admin/book/msg.jsp";}
/**
 * 4.0删除图书
 * attention：删除图书后，项目里的图书的图片也要删除
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String deleteBook(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	String bid=req.getParameter("bid");
	service.deleBook(bid);
	
	
	//找到图书图片文件文件目录
	Book book=service.findById(bid);
	//删除图片文件
	String savePath=this.getServletContext().getRealPath("/");
	new File(savePath,book.getImage_b()).delete();
	new File(savePath,book.getImage_w()).delete();
	
	req.setAttribute("code", "success");
	 req.setAttribute("msg", "删除图书信息成功！！");
	return "f:/adminjsps/admin/book/msg.jsp" ;}
/**
 * 3.0添加图书时，加载完一级目录，当选中一个一级目录后，需要异步加载二级目录，联动显示
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String checkChild(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	String pid=req.getParameter("pid");
	List<Category> childList=cservice.findChidCategory(pid);
	//把查询的二级目录数组转换成json数组返回
	Gson json=new Gson();
	String jsonstr=json.toJson(childList);
	System.out.println(jsonstr);
	
	resp.getWriter().print(jsonstr);
	return null ;}
	
/**
 * 2.0查询 一级目录，在添加图书时，需要把查询出的一级目录返回并显示
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String checkParent(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	List<Category> categoryList=cservice.findcategoryParent();
	req.setAttribute("categoryList", categoryList);
	
	return "f:/adminjsps/admin/book/add.jsp";
	}


	/**
	 * 1.0图书管理 之 查找目录信息
	 * 可以直接使用前台代码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	/*
	 * 1.0查询分类目录
	 * 
	 */
public String checkCategory(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	System.out.println("目录查询");
	List<Category> categoryList=cservice.findCategory();
	req.setAttribute("categorys", categoryList);
	return  "f:/adminjsps/admin/book/left.jsp"; 
	
}


//-----------------图书分页查询----------------------------------------------------
/**
 * 图书管理之 查找图书信息
 */

/**
 * 1.0 按照bid查询,具体的一本书的信息
 * 后台的查找图书方法
 *后台编辑图书需要得到图书的 所属目录 的父级目录的信息
 * ，因此添加多表查询在dao中方法
 */
public String findByBid(HttpServletRequest req,HttpServletResponse resp)
throws ServletException{
	
	//获取所有一级目录
	List<Category> parentList=cservice.findcategoryParent();
	//返回所有一级目录
	req.setAttribute("parentList", parentList);
	
	//查找图书信息并返回
	String bid=req.getParameter("bid");
	Book book=service.findById(bid );
	req.setAttribute("book", book);
	
	
	//获取所有二级目录并返回
	List<Category> childList=cservice.findChidCategory(book.getCategory().getParent().getCid());
	req.setAttribute("childList", childList);
	
	
	return "f:/adminjsps/admin/book/desc.jsp";
	
	
}
//--------------------------------------------------------------------------
/**
 * I.按照目录分页查询
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
	
	
	return "f:/adminjsps/admin/book/list.jsp";
	
	
}
/**
 * II.按照Author查询
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String  findByBookAuthor(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	 String author=req.getParameter("author");
	 
	 int pc=getPc(req);
	 
	 PagerBean<Book> pagerbean=service.findByBookAuthor(author, pc);
	 
	 String url=getURI(req);
	 
	 pagerbean.setUrl(url);
	 
	 req.setAttribute("pagerbean", pagerbean);
	 
		return "f:/adminjsps/admin/book/list.jsp";
}
/**
 * III.按照press查询
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
public String  findByBookPress(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	 String press=req.getParameter("press");
	 
	 int pc=getPc(req);
	 
	 PagerBean<Book> pagerbean=service.findByBookPress(press, pc);
	 
	 String url=getURI(req);
	 
	 pagerbean.setUrl(url);
	 req.setAttribute("pagerbean", pagerbean);
	  return "f:/adminjsps/admin/book/list.jsp";
}

/**
 * IV.组合查询 高级查询
 */
public String combinationSerach(HttpServletRequest req,HttpServletResponse resp)
throws ServletException{
	int pc=getPc(req);
	Map map=req.getParameterMap();
	Book book =CommonUtils.toBean(map, Book.class);
	PagerBean<Book> pagerbean=service.findByCombination(book, pc);
	String url=getURI(req);
	pagerbean.setUrl(url);
	req.setAttribute("pagerbean", pagerbean);
	return "f:/adminjsps/admin/book/list.jsp";
	
	
}

public String  findByBookName(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	 String bname=req.getParameter("bname");
	 
	 int pc=getPc(req);
	 
	 PagerBean<Book> pagerbean=service.findByBookName(bname, pc);
	 
	 String url=getURI(req);
	 
	 pagerbean.setUrl(url);
	 req.setAttribute("pagerbean", pagerbean);
	  return "f:/adminjsps/admin/book/list.jsp";
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
