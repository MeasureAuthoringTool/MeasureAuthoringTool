package mat.dao;

import mat.dto.UnitTypeDTO;
import mat.model.UnitType;

import java.util.List;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation.
 */
public interface UnitTypeDAO extends IDAO<UnitType, String> {
	
	/**
	 * Gets the all unit types.
	 * 
	 * @return the all unit types
	 */
	public List<UnitTypeDTO> getAllUnitTypes();
}
