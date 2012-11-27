package mat.dao.clause;

import mat.dao.IDAO;
import mat.shared.model.QDSTerm;

public interface QDSTermDAO extends IDAO<QDSTerm, String> {
	public QDSTerm findByDecisionId(String decisionId);
}
