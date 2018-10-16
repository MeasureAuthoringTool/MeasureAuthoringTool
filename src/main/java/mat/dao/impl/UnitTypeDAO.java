package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.UnitTypeDTO;
import mat.dao.search.GenericDAO;
import mat.model.UnitType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 * DAO implementation for UnitType table.
 */

public class UnitTypeDAO extends GenericDAO<UnitType, String> implements mat.dao.UnitTypeDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UnitTypeDAO.class);
	
	/* Retrieve all unit types from the UNIT_TYPE table
	 * @see mat.dao.UnitTypeDAO#getAllUnitTypes()
	 */
	/* (non-Javadoc)
	 * @see mat.dao.UnitTypeDAO#getAllUnitTypes()
	 */
	public List<UnitTypeDTO> getAllUnitTypes(){
		
		List<UnitTypeDTO> unitTypeDTOList = new ArrayList<UnitTypeDTO>();
		logger.info("Getting all the rows from the Unit Type table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<UnitType> unitTypeList = session.createCriteria(UnitType.class).list();
		for(UnitType unitType: unitTypeList){
			UnitTypeDTO unitTypeDTO =  new UnitTypeDTO();
			unitTypeDTO.setId(unitType.getId());
			unitTypeDTO.setUnitType(unitType.getUnitType());
			unitTypeDTOList.add(unitTypeDTO);
		}
		return unitTypeDTOList;
	}
}
