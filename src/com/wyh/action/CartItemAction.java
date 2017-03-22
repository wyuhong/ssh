package com.wyh.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.commons.StrKit;
import com.wyh.biz.CartItemBiz;
import com.wyh.entity.CartItem;
import com.wyh.entity.User;
@Controller
@Scope("prototype")
public class CartItemAction {

	@Autowired
	private CartItemBiz cbiz;
	private JSONObject Result;
	
	
	public JSONObject getResult() {
		return Result;
	}

	public void setResult(JSONObject result) {
		Result = result;
	}

	public String mycart(){
		CartItem cart =new CartItem();
		HttpServletRequest request = ServletActionContext.getRequest();
		//获得session中的user对象
		User user = (User) request.getSession().getAttribute("sessionUser");
		String bid = request.getParameter("bid");
		
		cart.setUser(user);
		cart.setCartItemId(StrKit.uuid().substring(0,5));
		int quantity = Integer.valueOf(request.getParameter("quantity"));
		cart.setQuantity(quantity);
		//判断书是否存在
		if(cbiz.isExist(bid,cart)>0){
			//存在的话，quantity加一
			cbiz.updateQuantity(bid,cart);
		}else{
			cbiz.addCart(cart,bid);
		}
		List<CartItem> clist = cbiz.listCartItemsByUser(user);
		request.setAttribute("clist", clist);
		
		return "success";
	}	
	
	//购物车展示
		public String listCartItems(){
			
			HttpServletRequest request = ServletActionContext.getRequest();
			//获得session中的user对象
			User user = (User) request.getSession().getAttribute("sessionUser");
			List<CartItem> clist = cbiz.listCartItemsByUser(user);
			request.setAttribute("clist", clist);
			
			return "success";

		}
		
		
		
		public String addQuantity() {
			
			Result = new JSONObject();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String cartItemId = request.getParameter("cartItemId");
			cbiz.addQuantity(cartItemId);
			CartItem cat = cbiz.findBookByCartItemId(cartItemId);
			int quantity = cat.getQuantity();
		    cat.setBook(cbiz.findBookByBid(cat.getBook().getBid()));
			double subtotal = cat.getSubtotal();
			Result.put("quantity", quantity);
			Result.put("subtotal", subtotal);
			// 返回值为字符串，如果字符串中包含"f:"，这就是转发；如果字符串中包含"r:"，这就是重定向；如果return
			// null，既不转发也不重定向
			
			return "success";
		}
		
		public String reduceQuantity(){
			Result = new JSONObject();
			HttpServletRequest request = ServletActionContext.getRequest();
			String cartItemId = request.getParameter("cartItemId");
			//如果数量大于一，就减少数量
			
			cbiz.reduceQuantity(cartItemId);
			CartItem cat = cbiz.findBookByCartItemId(cartItemId);
			int quantity = cat.getQuantity();
		    cat.setBook(cbiz.findBookByBid(cat.getBook().getBid()));
			double subtotal = cat.getSubtotal();
			Result.put("quantity", quantity);
			Result.put("subtotal", subtotal);
		
			// 返回值为字符串，如果字符串中包含"f:"，这就是转发；如果字符串中包含"r:"，这就是重定向；如果return
			// null，既不转发也不重定向
			
			return "success";
		}
		
		public String deleteCartItemById(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String cartItemId = request.getParameter("cartItemId");
			//如果数量等于一，则直接删除该数据
			cbiz.deleteCartItemById(cartItemId);
			return "success";
		}
		
		//订单展示
		public String listCartItemsById(){
			HttpServletRequest request = ServletActionContext.getRequest();
			User user = (User) request.getSession().getAttribute("sessionUser");
			String cartItemIds = request.getParameter("cartItemIds");//获得字符串
			String cartItemIds1 = cartItemIds.substring(0,cartItemIds.length()-1);//切掉最后一个逗号
			List<CartItem> olist = new ArrayList<CartItem>();
			String[] cartItems = cartItemIds1.split(",");//在逗号除切开，存入数组
			for (String id : cartItems) {//对数组进行遍历
				CartItem cartItem = cbiz.listCartItemsById(id);
				cartItem.setUser(user);
				olist.add(cartItem);
			}
			
			request.setAttribute("olist", olist);
			request.setAttribute("cartItemIds", cartItemIds);
			return "success";
		}
		
		
		public String DeleteCart(){
			HttpServletRequest request = ServletActionContext.getRequest();
			String cartItemIds = request.getParameter("cartItemIds");//获得字符串
			String cartItemIds1 = cartItemIds.substring(0,cartItemIds.length()-1);//切掉最后一个逗号
			
			String[] cartItems = cartItemIds1.split(",");//在逗号除切开，存入数组
			for (String id : cartItems) {//对数组进行遍历
				cbiz.deleteCartItemById(id);//逐条删除
			}
				
		
			
			return "success";
		}
}
