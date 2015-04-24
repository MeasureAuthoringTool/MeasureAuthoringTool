package mat.dao.clause;


import mat.dao.IDAO;
import mat.model.clause.MeasureSet;


/**
 * The Interface MeasureSetDAO.
 */
public interface MeasureSetDAO extends IDAO<MeasureSet, String> {
	
	/**
	 * Save measure set.
	 * 
	 * @param measureSet
	 *            the measure set
	 */
	public void saveMeasureSet(MeasureSet measureSet);
	
	/**
	 * Find measure set.
	 * 
	 * @param measureSetId
	 *            the measure set id
	 * @return the measure set
	 */
	public MeasureSet findMeasureSet(String measureSetId);
	
}
