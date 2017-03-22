package com.wyh.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="t_cartitem")
public class CartItem {
	@Id
	private String cartItemId;
	private int quantity;
	@OneToOne
	@JoinColumn(name="bid")
	private Book book;
	@ManyToOne
	@JoinColumn(name="uid")
	private User user;
	
	private int orderBy;
	
	
	public int getOrderBy() {
		return orderBy;
	}
	
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}




	/**
	 * 小计功能
	 * 小计=数量（quantity）*当前价格（currPrice）
	 * BigDecimal构造器要使用带String参数的，BigDecimal(String) 创建一个具有参数所指定以字符串表示的数值的对象。
	 * ava在java.math包中提供的API类BigDecimal，用来对超过16位有效位的数进行精确的运算。
	 * 双精度浮点型变量double可以处理16位有效数。在实际应用中，需要对更大或者更小的数进行运算和处理。
	 * float和double只能用来做科学计算或者是工程计算，在商业计算中要用java.math.BigDecimal。
	 * BigDecimal所创建的是对象，我们不能使用传统的+、-、*、/等算术运算符直接对其对象进行数学运算，而必须调用其相对应的方法。
	 * 方法中的参数也必须是BigDecimal的对象。构造器是类的特殊方法，专门用来创建对象，特别是带有参数的对象。
	 * @return
	 */
	
	public double getSubtotal(){
		BigDecimal qt = new BigDecimal(quantity+"");
		BigDecimal cp = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal sub = qt.multiply(cp);//乘法
 		return sub.doubleValue(); 
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

	

	
}
