package com.wyh.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.vcode.VerifyCode;

public class VerifyCodeAction extends ActionSupport{

	public String verifyCodes(){
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage();// 获取一次性验证码图片
		// 该方法必须在getImage()方法之后来调用
		// System.out.println(vc.getText());//获取图片上的文本
		try {
			VerifyCode.output(image,  ServletActionContext.getResponse().getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 把图片写到指定流中
		// 把文本保存到session中，为LoginServlet验证做准备
		ServletActionContext.getRequest().getSession().setAttribute("vCode", vc.getText());
		return null;
	}
}
