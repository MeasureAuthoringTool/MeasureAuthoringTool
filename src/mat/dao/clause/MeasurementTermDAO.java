package mat.dao.clause;

import mat.dao.IDAO;
import mat.shared.model.MeasurementTerm;

public interface MeasurementTermDAO extends IDAO<MeasurementTerm, String> {
	public MeasurementTerm findByDecisionId(String decisionId);
	
}
