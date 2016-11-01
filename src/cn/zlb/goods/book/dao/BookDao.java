package cn.zlb.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.book.pager.PageConstant;
import cn.zlb.goods.pager.PagerBean;

public class BookDao {
	private TxQueryRunner qr=new TxQueryRunner();
	/**
	 * 1.0按分类目录查询
	 * @throws SQLException 
	 * 
	 */
	public PagerBean<Book> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("cid","=",cid));
		return findByCriteria(list, pc);
		
		
	}
	
	/**
	 * 2.0按书名模糊查询
	 * @throws SQLException 
	 */
	public PagerBean<Book> findByBookName(String bname,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("bname","like","%"+bname+"%"));
		
		return findByCriteria(list, pc);
	 
	}
	
	/**
	 * 3.0 按照作者名 模糊查询
	 */
	/**
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PagerBean<Book> findByBookAuthor(String author,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("author","like","%"+author+"%"	));
		return findByCriteria(list, pc);
		
	}
	
	/**
	 * 4.0 按照出版社 模糊查询
	 */
	/**
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PagerBean<Book> findByBookPress(String press,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("press","like","%"+press+"%"	));
		return findByCriteria(list, pc);
		
	}
	/**
	 * 5.0多条件组合查询
	 * @throws SQLException 
	 */
	public PagerBean<Book> findByCombination(Book book,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("bname","like","%"+book.getBname()+"%"));
		list.add(new Expression("author","like","%"+book.getAuthor()+"%"));
		list.add(new Expression("press","like","%"+book.getPress()+"%"));
		return findByCriteria(list, pc);
	}
	/**
	 * sql查询的通用类
	 * 参数：Expression 表达式 、pc当前页码
	 * @throws SQLException 
	 */
	@SuppressWarnings("unused")
	private PagerBean<Book>findByCriteria(List<Expression> explist,int pc) throws SQLException{
		/**
		 * 1.获得每页记录数  ps 常量
		 * 2.获得查询结果总记录数 tr
		 * 3.查询结果 Book bean的list集合
		 * 4.把结果集合 封装到PagerBean里
		 */
		
		//1.0 获取 每页记录
		int ps=PageConstant.Page_Size;
		
		/**
		 * 2.0 拼接sql语句的where条件，
		 */
			//定义whereSql
		StringBuilder whereSql=new StringBuilder(" where 1=1");
		 	//定义sql查询的参数,sql语句中"?"对应的参数
		List<Object> params=new ArrayList<Object>();
			//开始拼接sql语句。遍历Expression表达式list
		 for (Expression exp:explist) {
			//添加and条件
			 whereSql.append(" and ").append(exp.getName()).append(" ").append(exp.getOperator()).append(" ");
			 //判断Expression 中的operator 操作符是否是 is null
			 if (!exp.getOperator().equalsIgnoreCase("is null")) {
				//如果不是is null 那么添加一个 ？ 占位符
				 whereSql.append("?");
				 //把表达式中的，要进行判断的值 取出，添加到 params 中
				 params.add(exp.getValue());
			}
		}
		 
		 /**
		  * 2.0 查询符合条件的总记录数
		  */
		 String sql="select count(*) from t_book"+whereSql;
		 //ScalarHandler 获取结果集某一行数据并转换成返回类型的对象
		 Number num=(Number)qr.query(sql, new ScalarHandler(),params.toArray());
		 int tr=num.intValue();
		 
		 /**
		  * 3.0 查询当前页的结果集
		  * 
		  */
		 //limit 从第一个问号记录的吓一条记录开始，取个数为第二个问号数量的 记录数
		sql="select * from t_book"+whereSql+"order by orderBy limit ?,?";
		System.out.println(sql);
		//根据页码计算查询记录数，把结果存入params，params 是可变参数，底层是数组，直接传入就好，可以同上面的占位符？一起计算
		params.add((pc-1)*ps);
		params.add(ps);
		List<Book> beanlist=qr.query(sql, new BeanListHandler<Book>(Book.class),params.toArray());
		
		
		/**
		 * 4.0把数据封装到PagerBean中
		 */
		PagerBean<Book> pagerbean=new PagerBean<Book>();
		pagerbean.setBeanlist(beanlist);
		pagerbean.setPc(pc);
		pagerbean.setTr(tr);
		pagerbean.setPs(ps);
		
		return pagerbean;
		
		
	}
	/*public static void main(String[] args) throws SQLException {
		List<Expression	> list=new ArrayList<Expression>();
		list.add(new Expression("bid", "=", "2"));
		BookDao dap=new BookDao();
		dap.findByCriteria(list, 1);
	}*/
	
 
 
	
	
	
	
	
	
	
	
	
	
}
