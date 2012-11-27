package mat.dao;

import java.util.List;

import mat.DTO.AuthorDTO;
import mat.model.Author;

public interface AuthorDAO extends IDAO<Author, String> {
	public List<AuthorDTO> getAllAuthors();
}
