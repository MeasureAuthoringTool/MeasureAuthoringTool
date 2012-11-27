package mat.dao;

import java.util.List;

import mat.DTO.MeasureTypeDTO;
import mat.model.MeasureType;

public interface MeasureTypeDAO extends IDAO<MeasureType, String> {
	public List<MeasureTypeDTO> getAllMeasureTypes();
}
