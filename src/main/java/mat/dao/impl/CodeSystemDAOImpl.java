package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.dto.CodeSystemDTO;
import mat.model.CodeSystem;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("codeSystemDAO")
public class CodeSystemDAOImpl extends GenericDAO<CodeSystem, String> implements mat.dao.CodeSystemDAO {

	private static final Log logger = LogFactory.getLog(CodeSystemDAOImpl.class);
	
	public CodeSystemDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<CodeSystemDTO> getAllCodeSystem(){
		List<CodeSystemDTO> codeSystemDTOList = new ArrayList<CodeSystemDTO>();
		logger.debug("Getting all the codeSystem from the category table");
		Session session = getSessionFactory().getCurrentSession();
		
		List<CodeSystem> codeSystemList = session.createCriteria(CodeSystem.class).setCacheable(true).setCacheRegion(CodeSystem.class.getCanonicalName()).list();
		for(CodeSystem codeSystem: codeSystemList){
			CodeSystemDTO codeSystemDTO =  new CodeSystemDTO();
			codeSystemDTO.setDescription(codeSystem.getDescription());
			codeSystemDTO.setId(codeSystem.getId());
			codeSystemDTOList.add(codeSystemDTO);
		}
		return codeSystemDTOList;
	}
}
