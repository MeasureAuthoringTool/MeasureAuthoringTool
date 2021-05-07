package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.MeasureExport;

/**
 * The Interface MeasureExportDAO.
 */
public interface MeasureExportDAO extends IDAO<MeasureExport, String> {
	
	/**
	 * Find for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure export
	 */
	MeasureExport findByMeasureId(String measureId);

	void saveAndFlush(MeasureExport me);
}
