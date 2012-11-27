package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.AttributeDetails;

public interface AttributeDetailsDAO extends IDAO<AttributeDetails, String> {
	public AttributeDetails findByName(String attrName);
}
