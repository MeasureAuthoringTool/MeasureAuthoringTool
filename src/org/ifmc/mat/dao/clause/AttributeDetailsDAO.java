package org.ifmc.mat.dao.clause;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.AttributeDetails;

public interface AttributeDetailsDAO extends IDAO<AttributeDetails, String> {
	public AttributeDetails findByName(String attrName);
}
