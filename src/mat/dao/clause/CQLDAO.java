package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.CQLData;

public interface CQLDAO extends IDAO<CQLData, String>{

	CQLData findByID(String measureId);

}
