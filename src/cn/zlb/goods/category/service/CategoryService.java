package cn.zlb.goods.category.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.zlb.goods.category.dao.CategoryDao;
import cn.zlb.goods.category.domin.Category;


public class CategoryService {
	private  CategoryDao dao=new CategoryDao();
	
	/**
	 * 7.0查询该一级目录下的所有二级目录
	 * @param cid
	 * @return
	 */
	public List<Category> findChidCategory(String cid){
		List<Category> categoryChildList=null;
		try {
			return categoryChildList=dao.findChidCategory(cid);
		} catch (SQLException e) {
			 e.printStackTrace();
		}
		return categoryChildList;
	}
	
	/**
	 * 6.0删除分类 ，
	 * 
	 * @param cid
	 * @throws SQLException 
	 */
	
	public void deleteParent(String cid)  {
		try {
			dao.deleteParent(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**6.1查找该 一级分类是否存在二级分类
	  
	 */
	public int findChildCountByCid(String cid)  {
		int i=0;
		try {
			  i=dao.findChildCountByCid(cid);
			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
		 
	}
	
	/**
	 * 5.0修改分类
	 * @param cate
	 */
	public void changeCategory(Category cate){
		
		try {
			dao.changeCategory(cate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * 4.0 根据cid查找分类
	 * @param cid
	 * @return
	 */
	public Category findCategory(String cid){
		Category category=null;
		try {
			category=dao.findCategory(cid);
			return category;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return category;
	}
	
	/**
	 * 3.0 查找所有的一级分类
	 * @return
	 */
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
