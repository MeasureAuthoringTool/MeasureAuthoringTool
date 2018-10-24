package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import mat.DTO.MeasureScoreDTO;
import mat.dao.search.GenericDAO;
import mat.model.MeasureScore;


/**
 * DAO implementation for Measure Score table operations.
 *
 */
public class MeasureScoreDAO extends GenericDAO<MeasureScore, String> implements mat.dao.MeasureScoreDAO{
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureScoreDAO.class);
	
	/* Retrieves all the measure scores configured in the table
	 * @see mat.dao.MeasureScoreDAO#getAllMeasureScores()
	 */
	/* (non-Javadoc)
	 * @see mat.dao.MeasureScoreDAO#getAllMeasureScores()
	 */
	public List<MeasureScoreDTO> getAllMeasureScores(){
		
		List<MeasureScoreDTO> scoresList = new ArrayList<MeasureScoreDTO>();
		logger.info("Getting all the rows from the Measure Score table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureScore> msrScoreList = session.createCriteria(MeasureScore.class).list();
		for(MeasureScore msrScore: msrScoreList){
			MeasureScoreDTO scoreDTO =  new MeasureScoreDTO();			
			scoreDTO.setScore(msrScore.getScore());
			scoreDTO.setId(msrScore.getId());
			scoresList.add(scoreDTO);
		}
		return scoresList;
	}
}
