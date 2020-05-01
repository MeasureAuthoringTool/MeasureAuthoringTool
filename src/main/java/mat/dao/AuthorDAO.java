package mat.dao;

import java.util.List;

import mat.dto.AuthorDTO;
import mat.model.Author;

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
