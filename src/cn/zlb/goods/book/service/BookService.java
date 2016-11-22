package cn.zlb.goods.book.service;

import java.sql.SQLException;

import cn.zlb.goods.book.dao.BookDao;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.book.exception.SqlNullException;
import cn.zlb.goods.pager.PagerBean;
import cn.zlb.goods.book.exception.*;
public class BookService {
	private BookDao dao=new BookDao();
	/**
	 * 一。按照bid查询
	 
	 */
	public Book findById(String bid )   {
		Book book=null;
		try {
			book = dao.findById(bid);
		} catch (SQLException e) {
		   e.printStackTrace();
		}
		 
		return book;
		
	}
	//--------------------------------------------------------------------------
	/**
	 * 1.0按照分类查询
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PagerBean<Book> findByCategory(String cid,int pc)  {
		PagerBean<Book> pagerbean=null;
		try {
			  pagerbean=dao.findByCategory(cid, pc);
		} catch (SQLException e) {
			System.out.println("pagerbean no found!~!!");
		}
		 
		return pagerbean;
		
	}
	 /**
	  * 2.0按照书名查询
	  * @param bname
	  * @param pc
	  * @return
	  */
	public PagerBean<Book> findByBookName(String bname,int pc)  {
		PagerBean<Book> pagerbean=null;
		try {
			  pagerbean=dao.findByBookName(bname, pc);
		} catch (SQLException e) {
			System.out.println("pagerbean no found!~!!");
		}
		 
		return pagerbean;
		
	}
	/**
	  * 3.0按照作者查询
	  * @param bname
	  * @param pc
	  * @return
	  */
	public PagerBean<Book> findByBookAuthor(String author,int pc)  {
		PagerBean<Book> pagerbean=null;
		try {
			  pagerbean=dao.findByBookAuthor(author, pc);
		} catch (SQLException e) {
			System.out.println("pagerbean no found!~!!");
		}
		 
		return pagerbean;
		
	}
	
	/**
	  * 4.0按照出版社查询
	  * @param bname
	  * @param pc
	  * @return
	  */
	public PagerBean<Book> findByBookPress(String press,int pc)  {
		PagerBean<Book> pagerbean=null;
		try {
			  pagerbean=dao.findByBookPress(press, pc);
		} catch (SQLException e) {
			System.out.println("pagerbean no found!~!!");
		}
		 
		return pagerbean;
		
	}
	/**
	  * 4.0按照出版社查询
	  * @param bname
	  * @param pc
	  * @return
	  */
	public PagerBean<Book> findByCombination(Book book,int pc)  {
		PagerBean<Book> pagerbean=null;
		try {
			  pagerbean=dao.findByCombination(book, pc);
		} catch (SQLException e) {
			System.out.println("pagerbean no found!~!!");
		}
		 
		return pagerbean;
		
	}
	//-------------------------------后台方法-------------------------------------
	/**
	 * 5.0查找商品数量
	 */
	public int findBookCountByCid(String cid){
	int i=0;
	try {
		i=dao.findBookCountByCid(cid);
		return i;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return i;
	}
	
	/**
	 * 6.0删除商品
	 */
	public void deleBook(String bid)
	{
		try {
			dao.deleteBook(bid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 7.0 修改图书
	 */
	public void editBook(Book book ){
		
		try {
			dao.editBook(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 8.0添加图书
	 * @param book
	 */
	public void add(Book book) {
		
		try {
			dao.add(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
