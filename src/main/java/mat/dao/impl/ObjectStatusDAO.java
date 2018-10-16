package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.ObjectStatusDTO;
import mat.dao.search.GenericDAO;
import mat.model.ObjectStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * The Class ObjectStatusDAO.
 */
public class ObjectStatusDAO extends GenericDAO<ObjectStatus, String> implements mat.dao.ObjectStatusDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CodeSystemDAO.class);
	
	
	/* (non-Javadoc)
	 * @see mat.dao.ObjectStatusDAO#getAllObjectStatus()
	 */
	public List<ObjectStatusDTO> getAllObjectStatus(){
		List<ObjectStatusDTO> objectStatusDTOList = new ArrayList<ObjectStatusDTO>();
		logger.info("Getting all the codeSystem from the category table");
		Session session = getSessionFactory().getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<ObjectStatus> objectStatusList = session.createCriteria(ObjectStatus.class).setCacheable(true).setCacheRegion(ObjectStatus.class.getCanonicalName()).list();
		for(ObjectStatus objectstatus: objectStatusList){
			ObjectStatusDTO objectStatusDTO =  new ObjectStatusDTO();
			objectStatusDTO.setDescription(objectstatus.getDescription());
			objectStatusDTO.setId(objectstatus.getId());
			objectStatusDTOList.add(objectStatusDTO);
		}
		return  objectStatusDTOList;
	}
	
}
