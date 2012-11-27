package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.ifmc.mat.DTO.UnitDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.Unit;

/**
 * DAO implementation for Unit table
 *
 */

public class UnitDAO extends GenericDAO<Unit, String> implements org.ifmc.mat.dao.UnitDAO {
	
	private static final Log logger = LogFactory.getLog(UnitDAO.class);
	
	/* Retrieve all units from the UNIT table
	 * @see org.ifmc.mat.dao.UnitDAO#getAllUnits()
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
			unitDTO.setSortOrder(unit.getSortOrder());
			unitDTOList.add(unitDTO);
		}
		return unitDTOList;
	}
}
