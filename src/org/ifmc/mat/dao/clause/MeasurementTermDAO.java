package org.ifmc.mat.dao.clause;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.shared.model.MeasurementTerm;

public interface MeasurementTermDAO extends IDAO<MeasurementTerm, String> {
	public MeasurementTerm findByDecisionId(String decisionId);
	
}
