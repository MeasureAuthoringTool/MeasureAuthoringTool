package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.ObjectStatusDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.ObjectStatus;

public class ObjectStatusDAO extends GenericDAO<ObjectStatus, String> implements org.ifmc.mat.dao.ObjectStatusDAO {
	private static final Log logger = LogFactory.getLog(CodeSystemDAO.class);
	
	
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
