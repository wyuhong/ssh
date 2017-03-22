package com.wyh.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.wyh.entity.User;
@Repository
public class UserDao {
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	public boolean existLoginname(String loginname) {
		boolean bl = true;
			List<User> ulist= hibernateTemplate.find("from User where loginname=?", loginname);
			if(ulist.isEmpty()){
				bl=false;
			}
		return bl;
	}

	public boolean existEmail(String email) {
		boolean bl = true;
		List<User> ulist= hibernateTemplate.find("from User where email=?", email);
		if(ulist.isEmpty()){
			bl=false;
		}
		return bl;
	}
	
	public int save(User user) {
	 hibernateTemplate.save(user);
	return 1;
	}
	
	
	public User findByCode(String code) {
		List<User> ulist = hibernateTemplate.find("from User where activationCode=?", code);
		return ulist.get(0);
	}
	
	public void updateStatus(String uid, boolean b) {	
		hibernateTemplate.bulkUpdate("update User set status=? where uid=? ",  b, uid);
	}

	public User findByNamePwd(User formUser) {
		List<User> ulist = hibernateTemplate.find("from User where loginname=? and loginpass=?", formUser.getLoginname(),formUser.getLoginpass());
		return ulist.get(0);
	}

	public int updatepass(User user) {
		int i = 0;
		i = hibernateTemplate.bulkUpdate("update User set loginpass=? where loginname=?", user.getNewpass(),user.getLoginname());
		return i;
	}

}
