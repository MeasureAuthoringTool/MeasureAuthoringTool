package mat.dao;

import java.util.List;

import mat.DTO.CodeSystemDTO;
import mat.model.CodeSystem;

public interface CodeSystemDAO extends IDAO<CodeSystem, String> {
	public List<CodeSystemDTO> getAllCodeSystem();
}
