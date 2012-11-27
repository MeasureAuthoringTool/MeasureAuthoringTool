package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.CategoryDTO;
import org.ifmc.mat.model.Category;

public interface CategoryDAO extends IDAO<Category, String> {
	public List<CategoryDTO> getAllCategories();
}
