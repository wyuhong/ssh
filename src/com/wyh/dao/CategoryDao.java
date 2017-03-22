package com.wyh.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.wyh.entity.Category;
@Repository
public class CategoryDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	public List<Category> listAllCategories() {
		 List<Category> clist = hibernateTemplate.find("from Category");
		return clist;
	}
}
