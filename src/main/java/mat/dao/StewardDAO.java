package mat.dao;

import java.util.List;

import mat.dto.StewardDTO;
import mat.model.MeasureSteward;

/**
 * The Interface StewardDAO.
 */
public interface StewardDAO extends IDAO<MeasureSteward, String> {
	
	/**
	 * Gets the all steward org.
	 * 
	 * @return the all steward org
	 */
	public List<StewardDTO> getAllStewardOrg();
}
