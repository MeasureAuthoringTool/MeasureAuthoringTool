package org.ifmc.mat.dao;

import org.ifmc.mat.model.MeasureValidationLog;
import org.ifmc.mat.model.clause.Measure;

/**
 * Validation Log Interface requires an implementation save op
 * @author aschmidt
 *
 */
public interface MeasureValidationLogDAO extends IDAO<MeasureValidationLog, String> {

	/**
	 * perform a save op using the non-derivable fields of a validation event
	 * @param measure
	 * @param event
	 * @param interimBarr
	 * @return true if save operation is successful
	 */
	public boolean recordMeasureValidationEvent(Measure measure, String event, byte[] interimBarr);
}
