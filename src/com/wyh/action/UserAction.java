package com.wyh.action;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.commons.BeanKit;
import com.commons.StrKit;
import com.opensymphony.xwork2.ActionContext;
import com.vcode.VerifyCode;
import com.wyh.biz.UserBiz;
import com.wyh.entity.User;


@Controller
@Scope("prototype")
public class UserAction {
	@Autowired
	private UserBiz ubiz;
	private String loginname;
	private String email;
	private JSONObject Result;
	private String loginpass;
	
	
	public String getLoginpass() {
		return loginpass;
	}

	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JSONObject getResult() {
		return Result;
	}

	public void setResult(JSONObject result) {
		Result = result;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String existLoginname(){
		Result = new JSONObject();
		boolean bl = ubiz.existLoginname(this.getLoginname());
		if (bl) {
			// 数据库中存在该用户名
			Result.put("status", "exist");
		} else {
			// 数据库中不存在该用户名
			Result.put("status", "noexist");
		}
		return "success";
	}
	
	public String existEmail(){
		
		Result = new JSONObject();
		boolean bl = ubiz.existEmail(this.getEmail());
		if (bl) {
			// 数据库中存在该用户名
			Result.put("status", "exist");
		} else {
			// 数据库中不存在该用户名
			Result.put("status", "noexist");
		}
		return "success";
	}
	
@SuppressWarnings("unchecked")
public String validateVerifyCode(){
		Result = new JSONObject();
		HttpServletRequest request = ServletActionContext.getRequest();
		String verifyCode = request.getParameter("verifyCode");
		boolean bl = verifyCode.equalsIgnoreCase(request.getSession().getAttribute("vCode").toString());
		if (bl) {
			// 数据库中存在该邮箱
			Result.put("status", "pass");
		} else {
			// 数据库中不存在该邮箱
			Result.put("status", "nopass");
		}
		return "success";
	}


public String regist() {
	HttpServletRequest request = ServletActionContext.getRequest();
	// 将请求流中的数据封装到User对象中
	User user = BeanKit.toBean(request.getParameterMap(), User.class);
	// 后台校验
	Map<String, String> errors = validateRegist(user, request.getSession());
	if (errors != null && errors.size() > 0) {
		request.setAttribute("user", user);
		request.setAttribute("errors", errors);
		return "error";
	}
	// 注册用户
	ubiz.regist(user);
	// 保存成功信息，转发到下一个页面
	request.setAttribute("code", "success");
	request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱完成激活！");
	return "success";

}

private Map<String, String> validateRegist(User formUser, HttpSession session) {
	Map<String, String> errors = new HashMap<String, String>();
	String loginname = formUser.getLoginname();
	if (StrKit.isBlank(loginname)) {
		errors.put("loginname", "用户名不能为空");
	} else if (loginname.length() < 6 || loginname.length() > 20) {
		errors.put("loginname", "用户名长度必须在3~20之间！");
	} else if (ubiz.existLoginname(loginname)) {
		errors.put("loginname", "登录名已被占用");
	}

	String loginpass = formUser.getLoginpass();
	if (StrKit.isBlank(loginpass)) {
		errors.put("loginpass", "密码不能为空！");
	} else if (loginpass.length() < 3 || loginpass.length() > 20) {
		errors.put("loginpass", "密码长度必须在3~20之间！");
	}

	String reloginpass = formUser.getReloginpass();
	if (StrKit.isBlank(reloginpass)) {
		errors.put("reloginpass", "确认密码不能为空！");
	} else if (!reloginpass.equals(loginpass)) {
		errors.put("reloginpass", "两次密码输入不一致");
	}

	String email = formUser.getEmail();
	if (StrKit.isBlank(email)) {
		errors.put("email", "Email不能为空！");
	} else if (!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
		errors.put("email", "Email格式错误！");
	} else if (ubiz.existEmail(email)) {
		errors.put("email", "Email已被注册！");
	}

	String verifyCode = formUser.getVerifyCode();
	String vcode = (String) session.getAttribute("vCode");
	if (StrKit.isBlank(verifyCode)) {
		errors.put("verifyCode", "验证码不能为空！");
	} else if (!verifyCode.equalsIgnoreCase(vcode)) {
		errors.put("verifyCode", "验证码错误！");
	}
	return errors;
}

public String activation(){

	HttpServletRequest request = ServletActionContext.getRequest();
	String code = request.getParameter("activationCode");
	try {
		ubiz.activatioin(code);
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜，激活成功，请马上登录！");
	} catch (Exception e) {
		request.setAttribute("code", "error");
		request.setAttribute("msg", e.getMessage());
	}
	return "success";
}

public String login(){
	HttpServletRequest request = ServletActionContext.getRequest();
	// 将请求流中的数据封装到User对象中
	User formUser = BeanKit.toBean(request.getParameterMap(), User.class);
	// 后台校验
	Map<String, String> errors = validateLogin(formUser, request.getSession());
	if (errors != null && errors.size() > 0) {
		request.setAttribute("user", formUser);
		request.setAttribute("errors", errors);
		return "error";
	}
	// 注册用户
	User user = ubiz.login(formUser);
	if (user == null) {
		request.setAttribute("user", formUser);
		request.setAttribute("msg", "用户名或密码错误");
		return "error";
	} else if (!user.isStatus()) {
		request.setAttribute("user", formUser);
		request.setAttribute("msg", "该用户未激活");
		return "error";
	} else {
		//将正确的user信息存在session中
		request.getSession().setAttribute("sessionUser", user);
		//获取登录名
		String loginname = user.getLoginname();
		//通过UTF-8的字符集将loginname进行编码，便于网络传输，不会乱码
		try {
			loginname = URLEncoder.encode(loginname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//设置一个cookie对象
		Cookie cookie = new Cookie("loginname", loginname);
		//设置cookie的最大存在时间，单位是秒，10天
		cookie.setMaxAge(60 * 60 * 24 * 10);
		//通过响应流吧cookie带回浏览器
		ServletActionContext.getResponse().addCookie(cookie);
		//重定向
		return "success";
	}

}

private Map<String, String> validateLogin(User formUser, HttpSession session) {
	Map<String, String> errors = new HashMap<String, String>();
	String loginname = formUser.getLoginname();
	if (StrKit.isBlank(loginname)) {
		errors.put("loginname", "用户名不能为空");
	} else if (loginname.length() < 6 || loginname.length() > 20) {
		errors.put("loginname", "用户名长度必须在6~20之间！");
	}

	String loginpass = formUser.getLoginpass();
	if (StrKit.isBlank(loginpass)) {
		errors.put("loginpass", "密码不能为空");
	} else if (loginpass.length() < 3 || loginpass.length() > 20) {
		errors.put("loginpass", "密码长度必须在3~20之间！");
	}

	String verifyCode = formUser.getVerifyCode();
	String vcode = (String) session.getAttribute("vCode");
	if (StrKit.isBlank(verifyCode)) {
		errors.put("verifyCode", "验证码不能为空！");
	} else if (!verifyCode.equalsIgnoreCase(vcode)) {
		errors.put("verifyCode", "验证码错误！");
	}

	return errors;
}
public String existLoginpass(){
	Result = new JSONObject();
	HttpServletRequest request = ServletActionContext.getRequest();
	String loginpass = request.getParameter("loginpass");
	User uu = (User)request.getSession().getAttribute("sessionUser");
	if (loginpass.equals(uu.getLoginpass())) {
		Result.put("status", "exist");
	} else {
		Result.put("status", "noexist");
	}
	return "success";
}
public String updatepass() {
	HttpServletRequest request = ServletActionContext.getRequest();
	// 将请求流中的数据封装到User对象中
	User user = BeanKit.toBean(request.getParameterMap(), User.class);
	// 后台校验
	Map<String, String> errors = validateUpdatepass(user, request.getSession());
	if (errors != null && errors.size() > 0) {
		request.setAttribute("user", user);
		request.setAttribute("errors", errors);
		return "error";
	}
	// 更改密码
	User sessionUser = (User) request.getSession().getAttribute("sessionUser");
	user.setLoginname(sessionUser.getLoginname());
	int i = ubiz.updatepass(user);
	// 保存成功信息，转发到下一个页面
	if(i>0){
	sessionUser.setLoginpass(sessionUser.getNewpass());
	request.getSession().setAttribute("sessionUser", sessionUser);
	request.setAttribute("code", "success");
	request.setAttribute("msg", "恭喜，密码修改成功,请重新登录");
	return "success";
	}else{
		request.setAttribute("code", "error");
		request.setAttribute("msg", "修改密码失败");
		return "success";
	}

}

private Map<String, String> validateUpdatepass(User formUser, HttpSession session) {
	Map<String, String> errors = new HashMap<String, String>();
	User uu = (User)session.getAttribute("sessionUser");
	
	String loginpass = formUser.getLoginpass();
	if (StrKit.isBlank(loginpass)) {
		errors.put("loginpass", "原密码不能为空！");
	} else if (loginpass.length() < 3 || loginpass.length() > 20) {
		errors.put("loginpass", "原密码长度必须在3~20之间！");
	}else if (!(loginpass.equals(uu.getLoginpass()))){
		errors.put("loginpass", "原密码错误");
	}
	
	String newpass = formUser.getNewpass();
	if (StrKit.isBlank(newpass)) {
		errors.put("newpass", "新密码不能为空！");
	} else if (newpass.length() < 3 || newpass.length() > 20) {
		errors.put("newpass", "新密码长度必须在3~20之间！");
	}
		
	String reloginpass = formUser.getReloginpass();
	if (StrKit.isBlank(reloginpass)) {
		errors.put("reloginpass", "确认密码不能为空！");
	} else if (!reloginpass.equals(newpass)) {
		errors.put("reloginpass", "两次密码输入不一致");
	}

	String verifyCode = formUser.getVerifyCode();
	String vcode = (String) session.getAttribute("vCode");
	if (StrKit.isBlank(verifyCode)) {
		errors.put("verifyCode", "验证码不能为空！");
	} else if (!verifyCode.equalsIgnoreCase(vcode)) {
		errors.put("verifyCode", "验证码错误！");
	}
	return errors;
}

public String quit(){
	ServletActionContext.getRequest().getSession().invalidate();
	return "success";
}
}
