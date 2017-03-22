package com.wyh.biz;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commons.PropKit;
import com.commons.StrKit;
import com.connection.ConnectionPoolManager;
import com.mail.Mail;
import com.mail.MailKit;
import com.wyh.dao.UserDao;
import com.wyh.entity.User;

@Service
public class UserBiz {
	@Autowired
	private UserDao udao;
	public boolean existLoginname(String loginname) {
		boolean bl = false;
		try {
			bl = udao.existLoginname(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return bl;
	}
	
	public boolean existEmail(String email) {
		boolean bl = false;
		try {
			bl = udao.existEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return bl;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void regist(User user) {
			user.setUid(StrKit.uuid());
			user.setStatus(false);
			user.setActivationCode(StrKit.uuid()+StrKit.uuid());
			int i = udao.save(user);
			if(i > 0){
				PropKit.use("email_template.properties");
				String host = PropKit.get("host");
				String username = PropKit.get("username");
				String password = PropKit.get("password");
				Session session = MailKit.createSession(host, username, password);
				
				
				String from = PropKit.get("from");
				String to = user.getEmail();
				String subject = PropKit.get("subject");
				
				// 
				String content = MessageFormat.format(PropKit.get("content"), user.getActivationCode());
				Mail mail = new Mail(from, to, subject, content);
				
				try {
					MailKit.send(session, mail);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
	

	@Transactional(rollbackFor=Exception.class)
	public void activatioin(String code) {
		try {
			User user = udao.findByCode(code);
			if(user == null) throw new Exception("无效的激活码！");
			if(user.isStatus()) throw new Exception("您已经激活过了，不要二次激活！");
			udao.updateStatus(user.getUid(), true);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

	public User login(User formUser) {
		User user = null;
		try {
			user = udao.findByNamePwd(formUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return user;
	}

	@Transactional(rollbackFor=Exception.class)
	public int updatepass(User user) {
		int i = udao.updatepass(user);
			return i;
	}
}
