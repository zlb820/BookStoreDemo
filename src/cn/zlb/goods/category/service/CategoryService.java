package cn.zlb.goods.category.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.zlb.goods.category.dao.CategoryDao;
import cn.zlb.goods.category.domin.Category;

public class CategoryService {
	private  CategoryDao dao=new CategoryDao();
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
