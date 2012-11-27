package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.AuthorDTO;
import org.ifmc.mat.model.Author;

public interface AuthorDAO extends IDAO<Author, String> {
	public List<AuthorDTO> getAllAuthors();
}
