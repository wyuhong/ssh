package com.wyh.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.commons.BeanKit;
import com.wyh.entity.Book;
import com.wyh.entity.CartItem;
import com.wyh.entity.User;
@Repository
public class CartItemDao {
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings("unchecked")
	public int isExist(String bid, CartItem cart) {
		int i =0;
			List<Long> list = hibernateTemplate.find("select count(*) from CartItem c  where c.book.bid=? and c.user.uid=?", bid,cart.getUser().getUid());
			if(list.size()>0){
				Long j = list.get(0);
				i = j.intValue();
			}
		
		return i;
	}

	public void upadateQuantity(String bid, CartItem cart) {
		hibernateTemplate.bulkUpdate("update CartItem c set c.quantity=c.quantity+? where c.book.bid=?",cart.getQuantity(),bid);
	}

	public void addCart(CartItem cart,String bid) {
		List<Book> blist = hibernateTemplate.find("from Book where bid=?" ,bid);
		cart.setBook(blist.get(0));
		hibernateTemplate.save(cart);
		
	}

	@SuppressWarnings("unchecked")
	public List<CartItem> listCartItemByUser(User user) {
		 List<CartItem> clist = hibernateTemplate.find("from CartItem c where c.user.uid=?", user.getUid());
		return clist;
	}

	public void addQuantity(String cartItemId) {
		hibernateTemplate.bulkUpdate("update CartItem c set c.quantity=c.quantity+1 where cartItemId=?",cartItemId);
		
	}

	public CartItem findBookByCartItemId(String cartItemId) {
		CartItem cartItem=null;
		List<CartItem> clist = hibernateTemplate.find("from CartItem where cartItemId=?", cartItemId);
		if(clist.size()>0){
			cartItem=clist.get(0);
		}
		return cartItem;
	}

	public Book findBookByBid(String bid) {
		Book book = null;
		List<Book> blist = hibernateTemplate.find("from Book where bid=?", bid);
		if(blist.size()>0){
			book = blist.get(0);
		}
		return book;
	}

	public void reduceQuantity(String cartItemId) {
		hibernateTemplate.bulkUpdate("update CartItem c set c.quantity=c.quantity-1 where cartItemId=?",cartItemId);
		
	}

	public void deleteCartItemById(String cartItemId) {
		hibernateTemplate.bulkUpdate("delete CartItem where cartItemId=?",cartItemId);
	}

	public CartItem listCartItemsById(String id) {
		CartItem cartItem =null;
		List<CartItem> cList = hibernateTemplate.find("from CartItem where cartItemId=?", id);
		if(cList.size()>0){
			cartItem = cList.get(0);
		}
		return cartItem;
	}

}
