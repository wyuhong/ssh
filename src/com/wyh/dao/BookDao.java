package com.wyh.dao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.commons.PageBean;
import com.commons.StrKit;
import com.wyh.entity.Book;
import com.wyh.entity.Category;
@Repository
public class BookDao {
	@Autowired
	private HibernateTemplate hibernateTemplate;
	@SuppressWarnings("unchecked")
	public List<Book> listBooksByCategory(final Category cat, PageBean<Book> pb) {
			final int pn = pb.getPageNumber();
			final int ps = pb.getPageSize();
			final String hql="from Book where category.cid=?";
			List<Book> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Book>>() {

				@Override
				public List<Book> doInHibernate(Session session) throws HibernateException, SQLException {
					Query query =session.createQuery(hql);
					query.setString(0, cat.getCid());
					query.setFirstResult((pn-1)*ps);//从第几条开始
					query.setMaxResults(ps);//每页显示几条数据
					return query.list();
				}
			});
		return blist;
	}
	public int countBooksByCategory(Category cat) {
		int i =0;
		List<Long> list = hibernateTemplate.find("select count(*) from Book where category.cid=?",cat.getCid());
		i = list.get(0).intValue();
		return i;
	}
	public Book getBookByBid(Book book) {
		List<Book> bList = hibernateTemplate.find("from Book where bid=?", book.getBid());
		
		return bList.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<Book> listBooksByAuthor( final Book book, PageBean<Book> pb) {
		final int pn = pb.getPageNumber();
		final int ps = pb.getPageSize();
		final String hql="from Book where author=?";
		List<Book> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Book>>() {

			@Override
			public List<Book> doInHibernate(Session session) {
				Query query =session.createQuery(hql);
				String author = book.getAuthor();
					try {
						author = new String(author.getBytes("iso8859-1"),"utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				
				query.setString(0, author);
				query.setFirstResult((pn-1)*ps);//从第几条开始
				query.setMaxResults(ps);//每页显示几条数据
				return query.list();
			}
		});
	return blist;
	}
	public int countBooksByAuthor(Book book) {
		int i =0;
		List<Long> list = hibernateTemplate.find("select count(*) from Book where author=?",book.getAuthor());
		i = list.get(0).intValue();
		return i;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Book> listBooksByPress(final Book book, PageBean<Book> pb) {
		final int pn = pb.getPageNumber();
		final int ps = pb.getPageSize();
		final String hql="from Book where press=?";
		List<Book> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Book>>() {

			@Override
			public List<Book> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query =session.createQuery(hql);
				String press = book.getPress();
				try {
					press = new String(press.getBytes("iso8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				query.setString(0, press);
				query.setFirstResult((pn-1)*ps);//从第几条开始
				query.setMaxResults(ps);//每页显示几条数据
				return query.list();
			}
		});
	return blist;
	}
	
	
	public int countBooksByPress(Book book) {
		int i =0;
		List<Long> list = hibernateTemplate.find("select count(*) from Book where press=?",book.getPress());
		i = list.get(0).intValue();
		return i;
	}
	public int countBooksByBnameFuzzy(Book book) {
		int i =0;
		List<Long> list = hibernateTemplate.find("select count(*) from Book b where b.bname like ?",book.getBname());
		i = list.get(0).intValue();
		return i;
	}
	@SuppressWarnings("unchecked")
	public List<Book> listBooksByBnameFuzzy(final Book book, PageBean<Book> pb) {
		final int pn = pb.getPageNumber();
		final int ps = pb.getPageSize();
		final String hql="from Book b where b.bname like :bname";
		List<Book> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Book>>() {

			@Override
			public List<Book> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query =session.createQuery(hql);
				String bname = book.getBname();
				try {
					bname = new String(bname.getBytes("iso8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				query.setString("bname", "%"+bname+"%");
				query.setFirstResult((pn-1)*ps);//从第几条开始
				query.setMaxResults(ps);//每页显示几条数据
				return query.list();
			}
		});
	return blist;
	}
	@SuppressWarnings("unchecked")
	public List<Book> listBooksByConditionsFuzzy(final Book book, PageBean<Book> pb) {
			final int pn = pb.getPageNumber();
			final int ps = pb.getPageSize();
			
			//这个是做查询是用的,1=1是为了避免其他查询为空时,这条查询语句报错.
			//如果查询语句为空则显示为select * from book where
			//这条语句就会报错了,加上1=1,有查询条件,语句不会报错.
			final StringBuffer hql = new StringBuffer("from Book b where 1=1 ");
			List<Book> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Book>>() {

				@Override
				public List<Book> doInHibernate(Session session) throws HibernateException, SQLException {
					
					String bname = book.getBname();
					String author = book.getAuthor();
					String press = book.getPress();
					try {
						bname = new String(bname.getBytes("iso8859-1"),"utf-8");
						author = new String(author.getBytes("iso8859-1"),"utf-8");
						press = new String(press.getBytes("iso8859-1"),"utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if(StrKit.notBlank(bname)){
						hql.append(" and b.bname like :bname ");//append(String str)，连接一个字符串到末尾
					}
					if(StrKit.notBlank(author)){
						hql.append(" and b.author like :author ");
					}
					if(StrKit.notBlank(press)){
						hql.append(" and b.press like :press ");
					}
					
					Query query =session.createQuery(hql.toString());
					
					if(StrKit.notBlank(bname)){
						query.setString("bname", "%"+bname+"%");
					}
					if(StrKit.notBlank(author)){
						query.setString("author", "%"+author+"%");
					}
					if(StrKit.notBlank(press)){
						query.setString("press", "%"+press+"%");
					}
					
					query.setFirstResult((pn-1)*ps);//从第几条开始
					query.setMaxResults(ps);//每页显示几条数据
					return query.list();
				}
			});
			
			return blist;
	}
	
	
	@SuppressWarnings("unchecked")
	public int countBooksByConditionsFuzzy(final Book book) {
		int i =0;
		final StringBuffer hql = new StringBuffer(" select count(*) from Book b where 1=1 ");
		List<Long> blist = hibernateTemplate.executeFind(new HibernateCallback<List<Long>>() {

			@Override
			public List<Long> doInHibernate(Session session) throws HibernateException, SQLException {
				
				String bname = book.getBname();
				String author = book.getAuthor();
				String press = book.getPress();
				try {
					bname = new String(bname.getBytes("iso8859-1"),"utf-8");
					author = new String(author.getBytes("iso8859-1"),"utf-8");
					press = new String(press.getBytes("iso8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				if(StrKit.notBlank(bname)){
					hql.append(" and b.bname like :bname ");//append(String str)，连接一个字符串到末尾
				}
				if(StrKit.notBlank(author)){
					hql.append(" and b.author like :author ");
				}
				if(StrKit.notBlank(press)){
					hql.append(" and b.press like :press ");
				}
				
				Query query =session.createQuery(hql.toString());
				
				if(StrKit.notBlank(bname)){
					query.setString("bname", "%"+bname+"%");
				}
				if(StrKit.notBlank(author)){
					query.setString("author", "%"+author+"%");
				}
				if(StrKit.notBlank(press)){
					query.setString("press", "%"+press+"%");
				}
				
				return query.list();
		
				}
		});
		i = blist.get(0).intValue();
		return i;
	}
}
