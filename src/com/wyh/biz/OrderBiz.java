package com.wyh.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commons.PageBean;
import com.wyh.dao.OrderDao;
import com.wyh.dao.OrderItemDao;
import com.wyh.entity.Order;
import com.wyh.entity.OrderItem;
import com.wyh.entity.User;
@Service
public class OrderBiz {

	@Autowired
	private OrderDao odao;
	@Autowired
	private OrderItemDao oidao;
	@Transactional(rollbackFor=Exception.class)
	public void saveOrder(Order order) {
		odao.addCart(order);
		oidao.addCart(order);
		
	}
	public List<Order> listOrdersByUser(User user, PageBean<Order> pb) {
		List<Order> olist = new ArrayList<Order>();
		List<Order> list = odao.listOrdersByUser(user, pb);
		if (list != null && !list.isEmpty()) {
			for (Order order : list) {
				List<OrderItem> oilist = oidao.listOrderItemsByOrder(order);
				order.setOrderItemList(oilist);
				olist.add(order);
			}
			}
		return olist;
	}
		
	public int countOrdersByUser(User user) {
		int i =0;
		i = odao.countOrdersByUser(user);
		return i;
	}
	public Order getOrderByOid(Order order) {
		Order order2 = odao.getOrderByOid(order);
		return order2;
	}
	public int getStatusByOid(String oid) {
		int i = 0;
		i = odao.getStatusByOid(oid);
		return i;
	}
	@Transactional
	public void updateStatusByOid(String oid, int i) {
		odao.updateStatusByOid(oid,i);
	}
}
