package com.wyh.action;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.commons.PageBean;
import com.commons.PropKit;
import com.commons.StrKit;
import com.wyh.biz.CartItemBiz;
import com.wyh.biz.OrderBiz;
import com.wyh.entity.CartItem;
import com.wyh.entity.Order;
import com.wyh.entity.OrderItem;
import com.wyh.entity.User;
import com.wyh.util.PageAction;

@Controller
@Scope("prototype")
public class OrderAction {
	@Autowired
	private OrderBiz obiz;
	@Autowired
	private CartItemBiz cbiz;
	
	private Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String saveOrder() throws Exception, InvocationTargetException{
		HttpServletRequest request=ServletActionContext.getRequest();
		//获取前台购物车条目ID，通过多个ID获取条目集合
		Order order = new Order();
		 String cartItemIds = request.getParameter("cartItemIds");
			//创建订单
		 //订单编号
		 order.setOid(StrKit.uuid());
		 //下单时间
		 order.setOrdertime(String.format("%tF %<tT", new Date()));
		 //订单地址
		 order.setAddress(request.getParameter("address"));
		 //订单所属用户
		 User user =  (User)request.getSession().getAttribute("sessionUser");	
		 order.setUser(user);
		// 设置状态，1表示未付款
			order.setStatus(1);
			
			String cartItemIds1 = cartItemIds.substring(0,cartItemIds.length()-1);//切掉最后一个逗号
			List<CartItem> olist = new ArrayList<CartItem>();
			String[] cartItems = cartItemIds1.split(",");//在逗号除切开，存入数组
			for (String id : cartItems) {//对数组进行遍历
				CartItem cartItem = cbiz.listCartItemsById(id);
				cartItem.setUser(user);
				olist.add(cartItem);
			}
			
			//order表中的合计字段
			BigDecimal total = new BigDecimal(0 + "");
			for (CartItem cat : olist) {
				total = total.add(new BigDecimal(cat.getSubtotal() + ""));
			}
			order.setTotal(total.doubleValue());
		 
			// 创建订单详细条目对象
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			for (CartItem cat : olist) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderItemId(StrKit.uuid());
				orderItem.setQuantity(cat.getQuantity());
				orderItem.setSubtotal(cat.getSubtotal());
				BeanUtils.copyProperties(orderItem, cat.getBook());
//				orderItem.setBook(cat.getBook());
				// 注意这里的写法，这个是双向的一对多
				orderItem.setOrder(order);
				orderItemList.add(orderItem);
			}
			order.setOrderItemList(orderItemList);
			// 调用业务层保存订单对象
			obiz.saveOrder(order);
			// 删除购物车条目			
			for (String string : cartItems) {//对数组进行遍历
				cbiz.deleteCartItemById(string);//逐条删除
			}
			// 将订单对象存放到request中
			request.setAttribute("order", order);
		return "success";
	}
	
	public String listOrdersByUser(){
		HttpServletRequest request = ServletActionContext.getRequest();
			 User user =  (User)request.getSession().getAttribute("sessionUser");	
			PageBean<Order> pb = new PageBean<Order>();
			//第一步设置pageNumber
			pb.setPageNumber(PageAction.getPageNumber(request));
			//设置pageSize
			pb.setPageSize( PropKit.use("pagesize.properties").getInt("book_page_size"));
			//设置url
			pb.setUrl(PageAction.getUrl(request, null));
			//这只list list中显示页面指定的内容
			pb.setList(obiz.listOrdersByUser(user,pb));
			//设置totalRecords
			pb.setTotalRecords(obiz.countOrdersByUser(user));
			//将pb存放在request中
			request.setAttribute("pb", pb);
			return "success";
	}
	
	public String getOrderByOid(){
		HttpServletRequest request = ServletActionContext.getRequest();
		order = obiz.getOrderByOid(order);
		request.setAttribute("order", order);
		request.setAttribute("action", request.getParameter("action"));
		return "success";
		
	}
	
	public String cancelOrder(){
		HttpServletRequest request=ServletActionContext.getRequest();
		//通过 oid获得订单状态进行判断
		int status = obiz.getStatusByOid(order.getOid());
		if(status != 1){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能取消订单！");
			return "error";
		}
		obiz.updateStatusByOid(order.getOid(), 5);//设置状态为取消！
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您的订单已经取消！");
		return "success";
		
	}
	
	public String confirmOrder(){
		HttpServletRequest request = ServletActionContext.getRequest();
		//通过 oid获得订单状态进行判断
		int status = obiz.getStatusByOid(order.getOid());
		if(status != 3){//等待确认收货
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能确认收货！");
			return "error";
		}
		obiz.updateStatusByOid(order.getOid(), 4);//设置状态为确认收货！
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜您，交易成功！");
		return "success";
	}
	
}
