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

import com.alibaba.fastjson.JSON;
import com.wyh.biz.CategoryBiz;
import com.wyh.entity.Category;


@Controller
@Scope("prototype")
public class CategoryAction {
	@Autowired
	private CategoryBiz cbiz;
	
	public String  listCategories() throws IOException {
		//1 通过userId来查询出该对象
		HttpServletRequest request = ServletActionContext.getRequest();
			List<Category> catlist = new ArrayList<Category>();	
		List<Category > clist = cbiz.listAllCategories();
		for (Category cat : clist) {
			if(cat.getPid() !=null){
				cat.setUrl("/ssh_goods/listBookByCategory?cid="+cat.getCid());
				cat.setTarget("body");
			}
			catlist.add(cat);
		}
		//将userlist转换为符合JSON格式的字符串
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		String categorylistring = JSON.toJSONString(catlist);
		//获取输出流
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		//通过out将JSON返回到前端界面
		out.println(categorylistring);
		out.flush();
		out.close();
		return "success";
		
	}
	
}
