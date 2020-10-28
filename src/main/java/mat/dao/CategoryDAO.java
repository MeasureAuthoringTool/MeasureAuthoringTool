package mat.dao;

import mat.dto.CategoryDTO;
import mat.model.Category;

import java.util.List;

/**
 * The Interface CategoryDAO.
 */
public interface CategoryDAO extends IDAO<Category, String> {
	
	/**
	 * Gets the all categories.
	 * 
	 * @return the all categories
	 */
	public List<CategoryDTO> getAllCategories();
}
