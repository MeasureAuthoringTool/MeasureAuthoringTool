package mat.dao;

import java.util.List;

import mat.DTO.UnitTypeDTO;
import mat.model.UnitType;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitTypeDAO extends IDAO<UnitType, String> {
	public List<UnitTypeDTO> getAllUnitTypes();
}
