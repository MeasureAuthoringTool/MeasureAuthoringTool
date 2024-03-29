package mat.dao;

import mat.dto.MeasureTypeDTO;
import mat.model.MeasureType;

import java.util.List;

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
	
	public void deleteAllMeasureTypeAssociationsByMeasureId(String measureId);

	MeasureType getMeasureTypeByName(String name);
}
