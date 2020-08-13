package mat.dao;

import mat.dto.CodeSystemDTO;
import mat.model.CodeSystem;

import java.util.List;

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
