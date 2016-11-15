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
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.service.CategoryService;

public class AminCategoryServlet extends BaseServlet {
private CategoryService service=new CategoryService();

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
/**
 *  3.1 再添加二级目录之前首先 找到所有的一级目录进行回显
 */
public String findCategoryChild(HttpServletRequest req , HttpServletResponse resp )
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
//添加二级目录
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











