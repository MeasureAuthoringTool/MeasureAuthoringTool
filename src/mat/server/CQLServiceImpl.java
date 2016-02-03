package mat.server;

import org.springframework.beans.factory.annotation.Autowired;

import mat.client.measure.service.CQLService;
import mat.dao.clause.CQLDAO;
import mat.model.clause.CQLData;
import mat.model.cql.CQLModel;

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
	public Boolean saveCQL(CQLModel cqlDataModel) {

		CQLData cqlData = cqlDAO.findForCQL(cqlDataModel.getMeasureId());
		if(cqlData!=null){
			cqlData.setMeasureXMLAsByteArray(cqlDataModel.getCqlBuilder());

		} else {

			cqlData = new CQLData();
			cqlData.setMeasure_id(cqlDataModel.getMeasureId());
			cqlData.setMeasureXMLAsByteArray(cqlDataModel.getCqlBuilder());
		}

		cqlDAO.save(cqlData);

		return true;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#saveCQL(mat.model.cql.CQLModel)
	 */
	@Override
	public CQLData getCQL(String measureId) {
	
		CQLData cqlData = cqlDAO.findForCQL(measureId);
		return cqlData;
	}
	
}
