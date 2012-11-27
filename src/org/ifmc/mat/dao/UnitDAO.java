package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.UnitDTO;
import org.ifmc.mat.model.Unit;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitDAO extends IDAO<Unit, String> {
	public List<UnitDTO> getAllUnits();
}
