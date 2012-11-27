package mat.dao;

import java.util.List;

import mat.DTO.MeasureScoreDTO;
import mat.model.MeasureScore;

/**
 * DAO interface for Measure Score table operation
 *
 */
public interface MeasureScoreDAO extends IDAO<MeasureScore, String> {
	public List<MeasureScoreDTO> getAllMeasureScores();
}
