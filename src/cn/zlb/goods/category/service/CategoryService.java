package cn.zlb.goods.category.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.zlb.goods.category.dao.CategoryDao;
import cn.zlb.goods.category.domin.Category;


public class CategoryService {
	private  CategoryDao dao=new CategoryDao();
	
	
	public List<Category> findcategoryParent(){
		List<Category> categoryList=null;
		try {
			return categoryList = dao.findCategoryParent();
		} catch (SQLException e) {
			 System.out.println("findcategoryParent error");
		}
		
		return  categoryList;
	}
	
	/**
	 * 2.0 添加目录
	 * @param cate
	 */
	public void addCategory(Category cate){
		try {
			dao.addCategory(cate);
		} catch (SQLException e) {
		 
		}
		
	}
	/**
	 * 1.0查找所有目录
	 * @author Bingo
	 *
	 */
	public List<Category> findCategory(){
		List<Category> list=null;
		try {
			list= dao.findCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
