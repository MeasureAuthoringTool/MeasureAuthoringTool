package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import mat.DTO.CategoryDTO;
import mat.dao.search.GenericDAO;
import mat.model.Category;

/**
 * The Class CategoryDAO.
 */
public class CategoryDAO extends GenericDAO<Category, String> implements mat.dao.CategoryDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CategoryDAO.class);
	
	/* (non-Javadoc)
	 * @see mat.dao.CategoryDAO#getAllCategories()
	 */
	public List<CategoryDTO> getAllCategories(){
		
		List<CategoryDTO> categoryDTOList = new ArrayList<CategoryDTO>();
		logger.info("Getting all the categories from the category table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings({ "unchecked", "deprecation" })
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
