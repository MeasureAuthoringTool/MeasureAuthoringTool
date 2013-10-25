package mat.dao;

import java.util.List;

import mat.DTO.MeasureTypeDTO;
import mat.model.MeasureType;

/**
 * The Interface MeasureTypeDAO.
 */
public interface MeasureTypeDAO extends IDAO<MeasureType, String> {
	
	/**
	 * Gets the all measure types.
	 * 
	 * @return the all measure types
	 */
	public List<MeasureTypeDTO> getAllMeasureTypes();
}
