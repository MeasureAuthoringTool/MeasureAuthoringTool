package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.DTO.MeasureTypeDTO;
import mat.dao.search.GenericDAO;
import mat.model.MeasureType;


@Repository("measureTypeDAO")
public class MeasureTypeDAOImpl extends GenericDAO<MeasureType, String> implements mat.dao.MeasureTypeDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureTypeDAOImpl.class);
	
	public MeasureTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
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
			mTypeDTO.setAbbrName(mType.getAbbrName());
			measureTypeList.add(mTypeDTO);
		}
		return measureTypeList;
	}
}
