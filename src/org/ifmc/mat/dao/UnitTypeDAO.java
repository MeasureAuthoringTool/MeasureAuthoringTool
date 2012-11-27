package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.UnitTypeDTO;
import org.ifmc.mat.model.UnitType;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitTypeDAO extends IDAO<UnitType, String> {
	public List<UnitTypeDTO> getAllUnitTypes();
}
