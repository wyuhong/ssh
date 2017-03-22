package com.wyh.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wyh.dao.CartItemDao;
import com.wyh.entity.Book;
import com.wyh.entity.CartItem;
import com.wyh.entity.User;
@Service
public class CartItemBiz {

	@Autowired
	private CartItemDao cdao;
	public int isExist(String bid, CartItem cart) {
		int i =0;
		i= cdao.isExist(bid,cart);
		return i;
	}

	@Transactional(rollbackFor=Exception.class)
	public void updateQuantity(String bid, CartItem cart) {
		cdao.upadateQuantity(bid,cart);
		
	}
	@Transactional(rollbackFor=Exception.class)
	public void addCart( CartItem cart,String bid) {
		cdao.addCart(cart,bid);
		
	}

	
	public List<CartItem> listCartItemsByUser(User user) {
		List<CartItem> clist = cdao.listCartItemByUser(user);
		return clist;
	}

	@Transactional(rollbackFor=Exception.class)
	public void addQuantity(String cartItemId) {
		cdao.addQuantity(cartItemId);
		
	}

	public CartItem findBookByCartItemId(String cartItemId) {
		CartItem cartItem= cdao.findBookByCartItemId(cartItemId);
		return cartItem;
	}

	public Book findBookByBid(String bid) {
		Book book = cdao.findBookByBid(bid);
		return book;
	}

	@Transactional(rollbackFor=Exception.class)
	public void reduceQuantity(String cartItemId) {
		cdao.reduceQuantity(cartItemId);
		
	}

	@Transactional(rollbackFor=Exception.class)
	public void deleteCartItemById(String cartItemId) {
		cdao.deleteCartItemById(cartItemId);
		
	}


	public CartItem listCartItemsById(String id) {
		CartItem cartItem = cdao.listCartItemsById(id);
		return cartItem;
	}

}
