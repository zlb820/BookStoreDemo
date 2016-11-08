package cn.zlb.goods.cart.domin;

import java.math.BigDecimal;

import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.user.domin.User;

public class CartItem {
	private String cartItemId;// 主键
	private int quantity;// 数量
	private Book book;// 条目对应的图书
	private User user;// 所属用户
	
	/**
	 * 小计 ，同本书总价 =数量*单价；
	 * @return
	 */
	public double getTotalPrice(){
		//使用BigDecimal 不会产生 精度 缺失
		BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b2=new BigDecimal(quantity+"");
		BigDecimal b3=b1.multiply(b2);
		return  b3.doubleValue() ; 
	}
		
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartItem(String cartItemId, int quantity, Book book, User user) {
		super();
		this.cartItemId = cartItemId;
		this.quantity = quantity;
		this.book = book;
		this.user = user;
	}
	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", quantity=" + quantity
				+ ", book=" + book + ", user=" + user + "]";
	}
	
	
	
	
	
}
