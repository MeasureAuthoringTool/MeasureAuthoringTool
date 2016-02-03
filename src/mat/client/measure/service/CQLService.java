package mat.client.measure.service;

import mat.model.clause.CQLData;
import mat.model.cql.CQLModel;

// TODO: Auto-generated Javadoc
/**
 * The Interface CQLService.
 */
public interface CQLService {

	/**
	 * Save cql.
	 *
	 * @param cqlDataModel the cql data model
	 * @return the boolean
	 */
	Boolean saveCQL(CQLModel cqlDataModel);
	
	/**
	 * Get cql.
	 *
	 * @param boolean
	 * @return cqlDataModel the cql data model
	 */
	CQLData getCQL(String measureId);

}
