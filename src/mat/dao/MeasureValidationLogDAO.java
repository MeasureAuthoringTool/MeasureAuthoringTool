package mat.dao;

import mat.model.MeasureValidationLog;
import mat.model.clause.Measure;

/**
 * Validation Log Interface requires an implementation save op.
 * 
 * @author aschmidt
 */
public interface MeasureValidationLogDAO extends IDAO<MeasureValidationLog, String> {

	/**
	 * perform a save op using the non-derivable fields of a validation event.
	 * 
	 * @param measure
	 *            the measure
	 * @param event
	 *            the event
	 * @param interimBarr
	 *            the interim barr
	 * @return true if save operation is successful
	 */
	public boolean recordMeasureValidationEvent(Measure measure, String event, byte[] interimBarr);
}
