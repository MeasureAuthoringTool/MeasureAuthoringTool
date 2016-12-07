package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.UnitDTO;
import mat.dao.search.GenericDAO;
import mat.model.Unit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * DAO implementation for Unit table.
 */

public class UnitDAO extends GenericDAO<Unit, String> implements mat.dao.UnitDAO {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(UnitDAO.class);
	
	/* Retrieve all units from the UNIT table
	 * @see mat.dao.UnitDAO#getAllUnits()
	 */
	/* (non-Javadoc)
	 * @see mat.dao.UnitDAO#getAllUnits()
	 */
	public List<UnitDTO> getAllUnits(){
		
		List<UnitDTO> unitDTOList = new ArrayList<UnitDTO>();
		logger.info("Getting all the rows from the Unit table");
		Session session = getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(Unit.class);
		criteria.addOrder(Order.asc("sortOrder"));
		List<Unit> unitList = criteria.list();
		for(Unit unit: unitList){
			UnitDTO unitDTO =  new UnitDTO();
			unitDTO.setId(unit.getId());
			unitDTO.setUnit(unit.getUnit());
			unitDTO.setCqlunit(unit.getCqlunit());
			unitDTO.setSortOrder(unit.getSortOrder());
			unitDTOList.add(unitDTO);
		}
		return unitDTOList;
	}
}
