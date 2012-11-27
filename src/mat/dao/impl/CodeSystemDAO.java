package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.CodeSystemDTO;
import mat.dao.search.GenericDAO;
import mat.model.CodeSystem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class CodeSystemDAO extends GenericDAO<CodeSystem, String> implements mat.dao.CodeSystemDAO {
private static final Log logger = LogFactory.getLog(CodeSystemDAO.class);
	
	public List<CodeSystemDTO> getAllCodeSystem(){
		List<CodeSystemDTO> codeSystemDTOList = new ArrayList<CodeSystemDTO>();
		logger.info("Getting all the codeSystem from the category table");
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
