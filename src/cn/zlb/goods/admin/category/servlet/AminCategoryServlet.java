package cn.zlb.goods.admin.category.servlet;
/**
 * 后台管理，目录管理模块
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.book.service.BookService;
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.service.CategoryService;

public class AminCategoryServlet extends BaseServlet {
private CategoryService service=new CategoryService();

private BookService bservice=new BookService();
/**
 * 6.0 删除分类
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
/**删除一级分类之前需要 ，明确 该一级分类没有任何的二级分类存在*/
public String deleteParent(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	String pid=req.getParameter("pid");
	int i=service.findChildCountByCid(pid);
	if (i>0) {
		//如果存在二级分类那么返回错误 ，不能删除
		req.setAttribute("msg", "该分类存在子属分类,不能删除!!!");
		System.out.println("该一级分类不可删！！");
		return "f:/adminjsps/admin/msg.jsp";
	}else{
	 //不存在就直接删除
	service.deleteParent(pid);
	return findCategory(req, resp);
	}
	}
/*删除一个二级分类，需要明确在该二级分类下不存在任何的 物品
 * 在 BookDao添加一个查找图书数量的方法
 */
public String deleteChild(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	String cid=req.getParameter("cid");
	
	int i=bservice.findBookCountByCid(cid);
	if (i>0) {
		//如果存在 商品那么返回错误 ，不能删除
		req.setAttribute("msg", "该分类存在子属分类,不能删除!!!");
		return "f:/adminjsps/admin/msg.jsp";	
	}else{
	 service.deleteParent(cid);
	 return findCategory(req, resp);}
	}

/**
 * 5.0修改二级分类
 * 修改二级分类底层和一级分类 一个函数
 * servlet封装不同的对象罢了
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
	/**先找出二级分类的内容，并获取该二级分类所属的一级分类 共同转发到edit2.jsp*/
public String changeChildPre(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	//找出 当前二级分类的 具体内容
	String cid=req.getParameter("cid");
	Category category=service.findCategory(cid);
	//把二级分类 和 所有一级分类 转发到 edit2.jsp
	req.setAttribute("category", category);
	req.setAttribute("categoryParent", service.findCategory());
	return "f:/adminjsps/admin/category/edit2.jsp";
	}

	/**修改二级分类*/
public String changeChild(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	Category cate=CommonUtils.toBean(req.getParameterMap(), Category.class);
	
	//新建父分类对象 
	String pid=req.getParameter("pid");
	Category parent=new Category();
	parent.setCid(pid);
	
	cate.setParent(parent);
	
	service.changeCategory(cate);
	return findCategory(req, resp);}
/**
 * 4.0 修改一级分类
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */
/*修改一级分类之前需要查询出该分类*/
public String changeParentPre(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	String pid=req.getParameter("pid");
	Category category=service.findCategory(pid);
	
	req.setAttribute("category", category);
	
	
	return "f:/adminjsps/admin/category/edit.jsp"; }
/*修改一级分类*/
public String changeParent(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	Category cate=CommonUtils.toBean(req.getParameterMap(), Category.class);
	service.changeCategory(cate);
	return findCategory(req, resp);
	}

/**
 * 3.0 添加二级目录
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */

/**
 *  3.1 再添加二级目录之前首先 找到所有的一级目录进行回显
 */
public String findCategoryParent(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	//需要获取传递的  一级分类id 以便会显时，动态显示一级分类
	String pid=req.getParameter("pid");
	List<Category> categroyList=service.findcategoryParent();
	req.setAttribute("pid", pid);
	req.setAttribute("categoryParentList", categroyList);
		return "f:/adminjsps/admin/category/add2.jsp";}


//3.2添加二级目录
public String addCategoryChild(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	Category cate=CommonUtils.toBean(req.getParameterMap(), Category.class);
	cate.setCid(CommonUtils.uuid());
	
	//获取 页面传递pid,封装到parent中
	String pid=req.getParameter("pid");
	Category parent=new Category();
	parent.setCid(pid);
	//添加父分类到 二级分类 
	cate.setParent(parent);
	//添加目录
	service.addCategory(cate);
	return findCategory(req, resp);
}
/**
 *  3.2 再添加二级目录之前首先 找到所有的一级目录进行回显
 */
/*public String findCategoryChild(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	//新建一个 子分类的 对象
	Category cate=CommonUtils.toBean(req.getParameterMap(), Category.class);
	
	//生成子分类id
	cate.setCid(CommonUtils.uuid());
	
	//获取pid 
	String pid=req.getParameter("pid");
	Category cateParent=new Category();
	cateParent.setCid(pid);
	
	//添加父分类到 子分类
	cate.setParent(cateParent);
	
	service.addCategory(cate);
	 
	return findCategory(req, resp); 
}
*/

/**
 * 2.0添加目录
 * @param req
 * @param resp
 * @return
 * @throws ServletException
 * @throws IOException
 */

/*添加一级目录，一级目录的pid是空的*/
public String addCategoryParent(HttpServletRequest req , HttpServletResponse resp )
		throws ServletException, IOException {
	//获取页面传来的category
	Category cate=CommonUtils.toBean(req.getParameterMap(), Category.class);
	
	//生成目录的id
	cate.setCid(CommonUtils.uuid());
	
	service.addCategory(cate);
	return  findCategory(req, resp);
}
	/**
	 * 1.0 查询所有分类:该模块前台已有，直接调用即可
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategory(HttpServletRequest req , HttpServletResponse resp )
			throws ServletException, IOException {
		resp.setContentType("text/html");
		//查找到所有的分类，回显
		List<Category> categoryList=service.findCategory();
		req.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	 
	}

}











