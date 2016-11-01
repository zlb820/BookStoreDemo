package cn.zlb.goods.category.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.service.CategoryService;

public class CategoryServlet extends BaseServlet {
	private CategoryService service=new CategoryService();
		/*
		 * 1.0查询分类目录
		 * 
		 */
	public String findCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("目录查询");
		List<Category> categoryList=service.findCategory();
		req.setAttribute("categorys", categoryList);
		return  "f:/jsps/left.jsp"; 
		
	}

}
