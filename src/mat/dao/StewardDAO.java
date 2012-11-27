package mat.dao;

import java.util.List;

import mat.DTO.StewardDTO;
import mat.model.MeasureSteward;

public interface StewardDAO extends IDAO<MeasureSteward, String> {
	public List<StewardDTO> getAllStewardOrg();
}
