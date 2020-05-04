package mat.dao;

import java.util.List;

import mat.dto.UnitTypeDTO;
import mat.model.UnitType;

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
