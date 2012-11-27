package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.UnitMatrixDTO;
import org.ifmc.mat.model.UnitMatrix;

/**
 * DAO interface for Unit, UnitType and Matrix tables operation
 *
 */
public interface UnitTypeMatrixDAO extends IDAO<UnitMatrix, String> {
	public List<UnitMatrixDTO> getAllUnitMatrix();
}
