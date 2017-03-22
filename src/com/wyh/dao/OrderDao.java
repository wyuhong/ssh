package com.wyh.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.commons.PageBean;
import com.wyh.entity.Book;
import com.wyh.entity.Order;
import com.wyh.entity.User;
@Repository
public class OrderDao {
	@Autowired
	private HibernateTemplate hibernateTemplate;
	public void addCart(Order order) {
		hibernateTemplate.save(order);
	}
	
	public int countOrdersByUser(User user) {
		int i =0;
		List<Long> list = hibernateTemplate.find("select count(*) from Order o where o.user.uid=?", user.getUid());
		Long long1 = list.get(0);
		i = long1.intValue();
		return i;
	}
	@SuppressWarnings("unchecked")
	public List<Order> listOrdersByUser(final User user, PageBean<Order> pb) {
		final int pn = pb.getPageNumber();
		final int ps = pb.getPageSize();
		final String hql="from Order o where o.user.uid=?";
		List<Order> olist = hibernateTemplate.executeFind(new HibernateCallback<List<Order>>() {

			@Override
			public List<Order> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query =session.createQuery(hql);
				query.setString(0, user.getUid());
				query.setFirstResult((pn-1)*ps);//从第几条开始
				query.setMaxResults(ps);//每页显示几条数据
				return query.list();
			}
		});
	return olist;
	}

	public Order getOrderByOid(Order order) {
		List<Order> olist = hibernateTemplate.find("from Order where oid=?", order.getOid());
		if(olist.size()>0){
			order = olist.get(0);
		}
		return order;
	}

	public int getStatusByOid(String oid) {
		int i =0;
		List<Number> nlist = hibernateTemplate.find("select status from Order where oid =?", oid);
		if(nlist.size()>0){
			Number number = nlist.get(0);
			i = number.intValue();
		}
		return i;
	}

	public void updateStatusByOid(String oid, int i) {
		hibernateTemplate.bulkUpdate("update Order set status=? where oid =?",i,oid);
		
	}

}
