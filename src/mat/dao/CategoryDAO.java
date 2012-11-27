package mat.dao;

import java.util.List;

import mat.DTO.CategoryDTO;
import mat.model.Category;

public interface CategoryDAO extends IDAO<Category, String> {
	public List<CategoryDTO> getAllCategories();
}
