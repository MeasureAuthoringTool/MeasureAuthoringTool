package org.ifmc.mat.dao.impl.clause;

import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.clause.ShareLevel;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class ShareLevelDAO extends GenericDAO<ShareLevel, String> 
	implements org.ifmc.mat.dao.clause.ShareLevelDAO {

}
