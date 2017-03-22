package com.wyh.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.PageBean;
import com.wyh.dao.BookDao;
import com.wyh.entity.Book;
import com.wyh.entity.Category;
@Service
public class BookBiz {
	@Autowired
	private BookDao bdao;
	public List<Book> listBooksByCategory(Category cat, PageBean<Book> pb) {
		 List<Book>	blist = bdao.listBooksByCategory(cat,pb);
		return blist;
	}
	
	
	public int countBooksByCategory(Category cat) {
		int i = 0;
			i = bdao.countBooksByCategory(cat);
		return i;
	}


	public Book getBookByBid(Book book) {
		Book books = bdao.getBookByBid(book); 
		return books;
	}


	public List<Book> listBooksByAuthor(Book book, PageBean<Book> pb) {
		 List<Book>	blist = bdao.listBooksByAuthor(book,pb);
		return blist;
	}


	public int countBooksByAuthor(Book book) {
		int i = 0;
		i = bdao.countBooksByAuthor(book);
	return i;
	}


	public List<Book> listBooksByPress(Book book, PageBean<Book> pb) {
		List<Book>	blist = bdao.listBooksByPress(book,pb);
		return blist;
	}


	public int countBooksByPress(Book book) {
		int i = 0;
		i = bdao.countBooksByPress(book);
	return i;
	}


	public List<Book> listBooksByBnameFuzzy(Book book, PageBean<Book> pb) {
		List<Book> blist = bdao.listBooksByBnameFuzzy(book,pb);
		return blist;
	}


	public int countBooksByBnameFuzzy(Book book) {
		int i = 0;
		i = bdao.countBooksByBnameFuzzy(book);
	return i;
	}


	public List<Book> listBooksByConditionsFuzzy(Book book, PageBean<Book> pb) {
		List<Book> blist = bdao.listBooksByConditionsFuzzy(book,pb);
		return blist;
	}


	public int countBooksByConditionsFuzzy(Book book) {
		int i = 0;
		i = bdao.countBooksByConditionsFuzzy(book);
	return i;
	}
}
