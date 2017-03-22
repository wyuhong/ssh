package com.wyh.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wyh.dao.CategoryDao;
import com.wyh.entity.Category;
@Service
public class CategoryBiz {
	@Autowired
	private CategoryDao cdao;
	public List<Category> listAllCategories() {
		List<Category> clist = new ArrayList<Category>();
			clist = cdao.listAllCategories();
			return clist;
	}

}
