package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.AttributeDetails;

/**
 * The Interface AttributeDetailsDAO.
 */
public interface AttributeDetailsDAO extends IDAO<AttributeDetails, String> {
	
	/**
	 * Find by name.
	 * 
	 * @param attrName
	 *            the attr name
	 * @return the attribute details
	 */
	public AttributeDetails findByName(String attrName);
}
