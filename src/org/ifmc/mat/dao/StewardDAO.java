package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.StewardDTO;
import org.ifmc.mat.model.MeasureSteward;

public interface StewardDAO extends IDAO<MeasureSteward, String> {
	public List<StewardDTO> getAllStewardOrg();
}
