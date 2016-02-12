package mat.server;

import org.springframework.beans.factory.annotation.Autowired;

import mat.client.measure.service.CQLService;
import mat.dao.clause.CQLDAO;
import mat.model.clause.CQLData;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameterTransferModel;

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
	
	
	public CQLModel saveAndModifyCQLParameter(CQLParameterTransferModel transferModel){
		if(transferModel!=null){
			
		}
		
		return null;
	}
	
}
