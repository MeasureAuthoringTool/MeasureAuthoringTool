package org.ifmc.mat.dao.clause;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.shared.model.QDSTerm;

public interface QDSTermDAO extends IDAO<QDSTerm, String> {
	public QDSTerm findByDecisionId(String decisionId);
}
