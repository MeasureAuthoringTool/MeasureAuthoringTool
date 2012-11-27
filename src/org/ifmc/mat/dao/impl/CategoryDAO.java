package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.CategoryDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.Category;

public class CategoryDAO extends GenericDAO<Category, String> implements org.ifmc.mat.dao.CategoryDAO {
	
	private static final Log logger = LogFactory.getLog(CategoryDAO.class);
	
	public List<CategoryDTO> getAllCategories(){
		
		List<CategoryDTO> categoryDTOList = new ArrayList<CategoryDTO>();
		logger.info("Getting all the categories from the category table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<Category> categoryList = session.createCriteria(Category.class).list();
		for(Category category: categoryList){
			if(!category.getDescription().equalsIgnoreCase("Measure Timing")){
				CategoryDTO categoryDTO =  new CategoryDTO();
				categoryDTO.setDescription(category.getDescription());
				categoryDTO.setId(category.getId());
				categoryDTOList.add(categoryDTO);
			}
		}
		return categoryDTOList;
	}
}
