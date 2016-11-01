package cn.zlb.goods.category.tools;
/*
 * 辅助目录查询的 转化类
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.itcast.commons.CommonUtils;
import cn.zlb.goods.category.domin.Category;

public class CategoryTools {
	
	/*--------辅助工具类---------------------------------------------
	 * 根据domin类，category pid是 Category类型，
	 * 需要对数据库查询出的pid 进行封装为Category
	 */
	
	public static Category toCategory(Map<String , Object> map){
		//1.0 先把其他的属性对应到 Category中
		Category cate=CommonUtils.toBean(map, Category.class);
		//2.0然后把pid封装进去
		String pid=(String) map.get("pid");
		//把pid转型为 Category然后复制给cate的parent属性
		if (pid!=null) {
			Category parent=new Category();
			parent.setCid(pid);
			cate.setParent(parent);
			
		}
		return cate;
		
	} 
	//批量转换char 类型 pid 到 Category 类型 
	public static  List<Category> toCategoryList(List<Map<String, Object>> list){
		List<Category> categoryList=new ArrayList<Category>();
		//循环遍历 list，进行转换
		for (Map<String, Object> map : list) {
			  Category category=toCategory(map);
			  categoryList.add(category);
		}
		return categoryList;
		
		
	}

}
