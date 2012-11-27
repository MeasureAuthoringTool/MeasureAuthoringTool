package mat.dao;

import java.util.List;

import mat.DTO.UnitDTO;
import mat.model.Unit;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitDAO extends IDAO<Unit, String> {
	public List<UnitDTO> getAllUnits();
}
