package mat.dao.impl.clause;

import mat.dao.search.GenericDAO;
import mat.model.clause.ShareLevel;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class ShareLevelDAO extends GenericDAO<ShareLevel, String> 
	implements mat.dao.clause.ShareLevelDAO {

}
