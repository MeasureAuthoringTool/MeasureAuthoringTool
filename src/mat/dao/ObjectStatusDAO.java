package mat.dao;

import java.util.List;

import mat.DTO.ObjectStatusDTO;
import mat.model.ObjectStatus;

public interface ObjectStatusDAO extends IDAO<ObjectStatus, String> {
	public List<ObjectStatusDTO> getAllObjectStatus();
}
