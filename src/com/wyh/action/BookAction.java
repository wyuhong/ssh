package com.wyh.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.commons.BeanKit;
import com.commons.PageBean;
import com.commons.PropKit;
import com.wyh.biz.BookBiz;
import com.wyh.entity.Book;
import com.wyh.entity.Category;
import com.wyh.util.PageAction;

@Controller
@Scope("prototype")
public class BookAction {
	@Autowired
	private BookBiz bbiz;
	private Book book;
		public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

		public String listBookByCategory(){
	         HttpServletRequest request = ServletActionContext.getRequest();
			//获取请求流中的数据封装到对象中
			Category cat = BeanKit.toBean(request.getParameterMap(), Category.class);
			PageBean<Book> pb = new PageBean<Book>();
			//第一步设置pageNumber
			pb.setPageNumber(PageAction.getPageNumber(request));
			//设置pageSize
			pb.setPageSize( PropKit.use("pagesize.properties").getInt("book_page_size"));
			//设置url
			pb.setUrl(PageAction.getUrl(request, null));
			//这指list list中显示页面指定的内容
			pb.setList(bbiz.listBooksByCategory(cat,pb));
			//设置totalRecords
			pb.setTotalRecords(bbiz.countBooksByCategory(cat));
			//将pb存放在request中
			request.setAttribute("pb", pb);
			return "success";
	}
		
		public String getBookByBid(){
			HttpServletRequest request = ServletActionContext.getRequest();
				//将请求流中的bid封装到book对象中
				book = bbiz.getBookByBid(book);
				request.setAttribute("book", book);
				return "success";
		}
		
		public String listBooksByAuthor() throws UnsupportedEncodingException{
			HttpServletRequest request = ServletActionContext.getRequest();
			String author = book.getAuthor();
			author = new String(author.getBytes("iso8859-1"),"utf-8");	
			request.setCharacterEncoding("utf-8");
				//获取请求流中的数据封装到对象中
				PageBean<Book> pb = new PageBean<Book>();
				//第一步设置pageNumber
				pb.setPageNumber(PageAction.getPageNumber(request));
				//设置pageSize
				pb.setPageSize( PropKit.use("pagesize.properties").getInt("book_page_size"));
				//设置url
				pb.setUrl(PageAction.getUrl(request, null));
				//这只list list中显示页面指定的内容
				pb.setList(bbiz.listBooksByAuthor(book,pb));
				//设置totalRecords
				pb.setTotalRecords(bbiz.countBooksByAuthor(book));
				//将pb存放在request中
				request.setAttribute("pb", pb);
				return "success";
			
		}
		
		
		public String listBooksByPress() throws UnsupportedEncodingException{
			HttpServletRequest request = ServletActionContext.getRequest();
				//0 将请求流中的bid封装到book对象中
			String press = book.getPress();
			press = new String(press.getBytes("iso8859-1"),"utf-8");	
			 request.setCharacterEncoding("utf-8");
				PageBean<Book> pb = new PageBean<Book>();
				//第一步：pageNumber
				pb.setPageNumber(PageAction.getPageNumber(request));
				//第二步：pageSize
				pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
				//第三步：设置url
				pb.setUrl(PageAction.getUrl(request, null));
				//第四步：设置list
				pb.setList(bbiz.listBooksByPress(book,pb));
				//第五步：设置totalRecords
				pb.setTotalRecords(bbiz.countBooksByPress(book));
				//第六步：将pb存放到request中
				//为何此处没有计算总页数？因为在封装的PageBean中已经计算出总页数了
				request.setAttribute("pb", pb);
				//第六步：转发到下一个页面
				return "success";
		}
		
		public String listBooksByBnameFuzzy(){
			HttpServletRequest request = ServletActionContext.getRequest();
				//获取请求流中的数据封装到对象中
				PageBean<Book> pb = new PageBean<Book>();
				//第一步设置pageNumber
				pb.setPageNumber(PageAction.getPageNumber(request));
				//设置pageSize
				pb.setPageSize( PropKit.use("pagesize.properties").getInt("book_page_size"));
				//设置url
				pb.setUrl(PageAction.getUrl(request, null));
				//这只list list中显示页面指定的内容
				pb.setList(bbiz.listBooksByBnameFuzzy(book,pb));
				//设置totalRecords
				pb.setTotalRecords(bbiz.countBooksByBnameFuzzy(book));
				//将pb存放在request中
				request.setAttribute("pb", pb);
				return "success";
		}
		
		public String listBooksByConditionsFuzzy(){
			HttpServletRequest request = ServletActionContext.getRequest();
				//获取请求流中的数据封装到对象中
				PageBean<Book> pb = new PageBean<Book>();
				//第一步设置pageNumber
				pb.setPageNumber(PageAction.getPageNumber(request));
				//设置pageSize
				pb.setPageSize( PropKit.use("pagesize.properties").getInt("book_page_size"));
				//设置url
				pb.setUrl(PageAction.getUrl(request, null));
				//这只list list中显示页面指定的内容
				pb.setList(bbiz.listBooksByConditionsFuzzy(book,pb));
				//设置totalRecords
				pb.setTotalRecords(bbiz.countBooksByConditionsFuzzy(book));
				//将pb存放在request中
				request.setAttribute("pb", pb);
				return "success";
		}
}
