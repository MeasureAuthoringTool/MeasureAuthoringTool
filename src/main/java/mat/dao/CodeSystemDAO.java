package mat.dao;

import java.util.List;

import mat.dto.CodeSystemDTO;
import mat.model.CodeSystem;

/**
 * The Interface CodeSystemDAO.
 */
public interface CodeSystemDAO extends IDAO<CodeSystem, String> {
	
	/**
	 * Gets the all code system.
	 * 
	 * @return the all code system
	 */
	public List<CodeSystemDTO> getAllCodeSystem();
}
