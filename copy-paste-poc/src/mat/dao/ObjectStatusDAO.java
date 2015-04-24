package mat.dao;

import java.util.List;

import mat.DTO.ObjectStatusDTO;
import mat.model.ObjectStatus;

/**
 * The Interface ObjectStatusDAO.
 */
public interface ObjectStatusDAO extends IDAO<ObjectStatus, String> {
	
	/**
	 * Gets the all object status.
	 * 
	 * @return the all object status
	 */
	public List<ObjectStatusDTO> getAllObjectStatus();
}
