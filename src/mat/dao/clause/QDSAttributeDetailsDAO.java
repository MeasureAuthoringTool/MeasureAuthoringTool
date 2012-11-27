package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.QDSAttributeDetails;

public interface QDSAttributeDetailsDAO extends IDAO<QDSAttributeDetails, String> {
	public List<QDSAttributeDetails> findByDecisionId(String decisionId);
}
