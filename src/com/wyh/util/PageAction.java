package com.wyh.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.commons.StrKit;
import com.opensymphony.xwork2.ActionSupport;

public class PageAction {
	public static int getPageNumber(HttpServletRequest request) {
		int i = 0;
		try {
			//当前台页面没有传递pageNumber,默认为1
			i = 1;
			String pageNumberStr = request.getParameter("pageNumber");
			if(StrKit.notBlank(pageNumberStr)){
				i = Integer.parseInt(pageNumberStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	
	public static String getUrl(HttpServletRequest request,String flagString) {
		String str = "";
		try {
			String contextPath = request.getContextPath();//项目名http://localhost:8080/firstjdbc
			String servletPath = request.getServletPath();//servlet名称：/stuAction
			String queryString = request.getQueryString();//参数：？后面的所有内容
//			if(queryString == null){
//				queryString = flagString;
//			}
			if(queryString == null){
				queryString = "";
			}
			if(queryString.contains("&pageNumber=")){
				queryString = queryString.substring(0, queryString.lastIndexOf("&pageNumber="));
			}
			str = contextPath + servletPath + "?" + queryString;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return str;
	}
	
}
