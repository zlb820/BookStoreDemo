package cn.zlb.goods.category.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.tools.CategoryTools;
public class CategoryDao {
	private TxQueryRunner qr=new TxQueryRunner();
	//one:查找一级目录结构
	public List<Category> findCategory() throws SQLException{
		//1.0 查询出所有一级分类
		String sql="select * from t_category where pid is null";
		List<Map<String, Object>> categoryMap=qr.query(sql, new MapListHandler());
		//把所有一级分类  pid 转化
		List<Category> categoryList=CategoryTools.toCategoryList(categoryMap);
		 
		//遍历每一个一级分类，查询出所有的子分类 ，并把子分类添加到一级分类中
		for (Category category : categoryList) {
			System.out.println("一级分类："+category.getCname());
			List<Category> childs=findChidCategory(category.getCid());
			System.out.println("二级分类：");
			for (Category child: childs) {
				System.out.println("--:"+child.getCname());
			}
			
			category.setChild(childs);
			
		}
		System.out.println("目录查询完毕:" );
		/*for (Category category : categoryList) {
				System.out.println(category.getCname());
			 for (Category child : category.getChild()) {
				System.out.print("-"+child.getCname());
			}
		}*/
		return categoryList;
	}
	//根据pid查找子目录
	
	public List<Category> findChidCategory(String cid) throws SQLException{
		//查询 所有分类中 ： pid 等于 cid的记录 
		String sql="select * from t_category where pid=?";
		List<Map<String,Object>> categoryMap=qr.query(sql, new MapListHandler(), cid);
		return  CategoryTools.toCategoryList(categoryMap);
	}
	
	
	
	
}









