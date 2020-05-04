package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.UnitTypeDTO;
import mat.dao.UnitTypeDAO;
import mat.dao.search.GenericDAO;
import mat.model.UnitType;

@Repository("unitTypeDAO")
public class UnitTypeDAOImpl extends GenericDAO<UnitType, String> implements UnitTypeDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UnitTypeDAOImpl.class);
	
	public UnitTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<UnitTypeDTO> getAllUnitTypes(){
		
		List<UnitTypeDTO> unitTypeDTOList = new ArrayList<UnitTypeDTO>();
		logger.info("Getting all the rows from the Unit Type table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<UnitType> unitTypeList = session.createCriteria(UnitType.class).list();
		for(UnitType unitType: unitTypeList){
			UnitTypeDTO unitTypeDTO =  new UnitTypeDTO();
			unitTypeDTO.setId(unitType.getId());
			unitTypeDTO.setUnitType(unitType.getName());
			unitTypeDTOList.add(unitTypeDTO);
		}
		return unitTypeDTOList;
	}
}
