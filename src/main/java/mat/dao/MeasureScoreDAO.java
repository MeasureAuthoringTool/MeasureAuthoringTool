package mat.dao;

import mat.dto.MeasureScoreDTO;
import mat.model.MeasureScore;

import java.util.List;

/**
 * DAO interface for Measure Score table operation.
 */
public interface MeasureScoreDAO extends IDAO<MeasureScore, String> {
	
	/**
	 * Gets the all measure scores.
	 * 
	 * @return the all measure scores
	 */
	public List<MeasureScoreDTO> getAllMeasureScores();
}
