package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.QDSAttributeDetails;

public interface QDSAttributeDetailsDAO extends IDAO<QDSAttributeDetails, String> {
	public List<QDSAttributeDetails> findByDecisionId(String decisionId);
}
