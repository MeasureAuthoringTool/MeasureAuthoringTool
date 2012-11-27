package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.StewardDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.MeasureSteward;


public class StewardDAO extends GenericDAO<MeasureSteward, String> implements org.ifmc.mat.dao.StewardDAO {
	
	private static final Log logger = LogFactory.getLog(StewardDAO.class);
	
	public List<StewardDTO> getAllStewardOrg(){
		
		List<StewardDTO> StewardDTOList = new ArrayList<StewardDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureSteward> StewardList = session.createCriteria(MeasureSteward.class).list();
		for(MeasureSteward Steward: StewardList){
			StewardDTO StewardDTO =  new StewardDTO();
			StewardDTO.setOrgName(Steward.getOrgName());
			StewardDTO.setId(Steward.getId());
			StewardDTOList.add(StewardDTO);
		}
		return StewardDTOList;
	}
}
