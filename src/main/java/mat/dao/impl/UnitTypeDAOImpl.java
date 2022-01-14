package mat.dao.impl;

import mat.dao.UnitTypeDAO;
import mat.dao.search.GenericDAO;
import mat.dto.UnitTypeDTO;
import mat.model.UnitType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("unitTypeDAO")
public class UnitTypeDAOImpl extends GenericDAO<UnitType, String> implements UnitTypeDAO {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(UnitTypeDAOImpl.class);
	
	public UnitTypeDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<UnitTypeDTO> getAllUnitTypes(){
		
		List<UnitTypeDTO> unitTypeDTOList = new ArrayList<UnitTypeDTO>();
		logger.debug("Getting all the rows from the Unit Type table");
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
