package mat.server;

import mat.client.measure.service.CQLService;
import mat.dao.clause.CQLDAO;
import mat.model.clause.CQLData;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLServiceImpl.
 */
public class CQLServiceImpl  implements CQLService{
	
	/** The cql dao. */
	@Autowired
	private CQLDAO cqlDAO;
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#saveCQL(mat.model.cql.CQLModel)
	 */
	@Override
	public CQLData getCQL(String measureId) {
		
		CQLData cqlData = cqlDAO.findByID(measureId);
		return cqlData;
	}
	
	
	
}
