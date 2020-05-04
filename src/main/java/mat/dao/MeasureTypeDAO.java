package mat.dao;

import java.util.List;

import mat.dto.MeasureTypeDTO;
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
	
	public void deleteAllMeasureTypeAssociationsByMeasureId(String measureId);

	MeasureType getMeasureTypeByName(String name);
}
