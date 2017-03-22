package com.wyh.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.wyh.entity.Order;
import com.wyh.entity.OrderItem;
@Component
public class OrderItemDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	public List<OrderItem> listOrderItemsByOrder(Order order) {
		List<OrderItem> oilist = hibernateTemplate.find("from OrderItem o where o.order.oid=?",order.getOid());
		return oilist;
	}
	public void addCart(Order order) {
		List<OrderItem> oList = order.getOrderItemList();
		for (OrderItem orderItem : oList) {
			hibernateTemplate.save(orderItem);
		}
	}

}
