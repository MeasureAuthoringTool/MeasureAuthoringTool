package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.MeasureTypeDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.MeasureType;


public class MeasureTypeDAO extends GenericDAO<MeasureType, String> implements org.ifmc.mat.dao.MeasureTypeDAO {
	
	private static final Log logger = LogFactory.getLog(MeasureTypeDAO.class);
	
	public List<MeasureTypeDTO> getAllMeasureTypes(){
		
		List<MeasureTypeDTO> measureTypeList = new ArrayList<MeasureTypeDTO>();
		logger.info("Getting all the rows from the Steward table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<MeasureType> mTypeList = session.createCriteria(MeasureType.class).list();
		for(MeasureType mType: mTypeList){
			MeasureTypeDTO mTypeDTO =  new MeasureTypeDTO();
			mTypeDTO.setName(mType.getDescription());
			mTypeDTO.setId(mType.getId());
			measureTypeList.add(mTypeDTO);
		}
		return measureTypeList;
	}
}
