package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.dto.CategoryDTO;
import mat.model.Category;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("categoryDAO")
public class CategoryDAOImpl extends GenericDAO<Category, String> implements mat.dao.CategoryDAO {
	
	
	public CategoryDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CategoryDAOImpl.class);
	
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
