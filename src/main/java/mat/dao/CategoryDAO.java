package mat.dao;

import java.util.List;

import mat.dto.CategoryDTO;
import mat.model.Category;

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
