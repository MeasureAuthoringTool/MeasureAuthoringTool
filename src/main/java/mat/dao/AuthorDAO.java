package mat.dao;

import mat.dto.AuthorDTO;
import mat.model.Author;

import java.util.List;

/**
 * The Interface AuthorDAO.
 */
public interface AuthorDAO extends IDAO<Author, String> {
	
	/**
	 * Gets the all authors.
	 * 
	 * @return the all authors
	 */
	public List<AuthorDTO> getAllAuthors();
}
