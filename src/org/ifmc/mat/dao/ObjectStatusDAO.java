package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.ObjectStatusDTO;
import org.ifmc.mat.model.ObjectStatus;

public interface ObjectStatusDAO extends IDAO<ObjectStatus, String> {
	public List<ObjectStatusDTO> getAllObjectStatus();
}
