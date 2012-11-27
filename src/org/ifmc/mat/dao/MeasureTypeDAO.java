package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.MeasureTypeDTO;
import org.ifmc.mat.model.MeasureType;

public interface MeasureTypeDAO extends IDAO<MeasureType, String> {
	public List<MeasureTypeDTO> getAllMeasureTypes();
}
