package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.DTO.MeasureScoreDTO;
import org.ifmc.mat.model.MeasureScore;

/**
 * DAO interface for Measure Score table operation
 *
 */
public interface MeasureScoreDAO extends IDAO<MeasureScore, String> {
	public List<MeasureScoreDTO> getAllMeasureScores();
}
