package cn.zlb.goods.category.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.category.domin.Category;
import cn.zlb.goods.category.tools.CategoryTools;
public class CategoryDao {
	private TxQueryRunner qr=new TxQueryRunner();
	
	/**
	 * 5.0删除分类 ，
	 * 
	 * @param cid
	 * @throws SQLException 
	 */
	
	public void deleteParent(String cid) throws SQLException{
		String sqlStr="delete  from t_category where cid =?";
		qr.update(sqlStr,cid);
		
	}
	
	/**5.1查找该 一级分类是否存在二级分类
	  
	 */
	public int findChildCountByCid(String cid) throws SQLException{
		String sqlStr="select count(*) from t_category where pid=?";
		Number num=(Number) qr.query(sqlStr,new ScalarHandler(),cid);
		System.out.println(num.intValue());
		return num==null ? 0 : num.intValue();
	}
	
	/**
	 * 4.0 修改分类
	 * 》需要具备修改一级分类和 二级分类的作用：
	 * 》区别在于：一级分类 pid为 null，二级pid不为空
	 * 》因此pid需要特殊处理
	 * @param cate
	 * @throws SQLException 
	 */
	/*4.1在修改分类之前，首先需要查找出该分类*/
	public Category findCategory(String cid) throws SQLException{
		String sqlStr="select * from t_category where cid = ?";
		 Map<String, Object> map=qr.query(sqlStr, new MapHandler(),cid);
		return CategoryTools.toCategory(map);
	}
	
	/*修改分类*/
	public void changeCategory(Category cate) throws SQLException{
		String sqlStr="update t_category set cname=? , pid =?,`desc`=? where cid=?";
		
		String pid=null;
		if (cate.getParent()!=null) {
			pid=cate.getParent().getCid();
		}
		Object params[]={cate.getCname(),pid,cate.getDesc(),cate.getCid()};
		qr.update(sqlStr,params);
	}
	
	
	
	/**
	 * 3.0查找所有的一级分类 
	 * 在进行二级目录的添加之前，需要找到所有的一级分类
	 * @return
	 * @throws SQLException 
	 */
	public List<Category >findCategoryParent() throws SQLException{
		String sqlStr="select * from t_category where pid is null";
		List<Map<String, Object>> categoryMapList=qr.query(sqlStr, new MapListHandler());
		List<Category> categoryList=CategoryTools.toCategoryList(categoryMapList);
		return categoryList;
	} 
	
	/**
	 * 2.0添加目录
	 * 1》判断Category的pid是否为空
	 * 2》为空是一级目录：pid赋值为null
	 * 3》不为空 是二级目录：赋值响应pid
	 * @param cate
	 * @throws SQLException 
	 */
	public void addCategory(Category cate) throws SQLException{
		//desc需要转移 ，desc是sql中排序的关键字
		String sqlStr="insert into t_category (cid,cname,pid,`desc`) values (?,?,?,?)";
		String pid=null;
		if (cate.getParent()!=null) {
			//如果是二级分类，那么pid不为空，把pid设置为 父分类cid
			pid=cate.getParent().getCid();
		}
		Object params[]={cate.getCid(),cate.getCname(),pid,cate.getDesc()};
		qr.update(sqlStr,params);
		 
	}
	/**
	 * 1.0查找目录
	 * @return
	 * @throws SQLException
	 */
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









