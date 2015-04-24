package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.QDSAttributeDetails;

/**
 * The Interface QDSAttributeDetailsDAO.
 */
public interface QDSAttributeDetailsDAO extends IDAO<QDSAttributeDetails, String> {
	
	/**
	 * Find by decision id.
	 * 
	 * @param decisionId
	 *            the decision id
	 * @return the list
	 */
	public List<QDSAttributeDetails> findByDecisionId(String decisionId);
}
