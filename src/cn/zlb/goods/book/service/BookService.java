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
			book = dao.findById(bid );
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
}	
