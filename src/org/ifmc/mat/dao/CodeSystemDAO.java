package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.CodeSystemDTO;
import org.ifmc.mat.model.CodeSystem;

public interface CodeSystemDAO extends IDAO<CodeSystem, String> {
	public List<CodeSystemDTO> getAllCodeSystem();
}
