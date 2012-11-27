package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.ifmc.mat.DTO.UnitMatrixDTO;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.UnitMatrix;

/**
 * DAO implementation for UnitTypeMatrix table
 *
 */

public class UnitTypeMatrixDAO extends GenericDAO<UnitMatrix, String> implements org.ifmc.mat.dao.UnitTypeMatrixDAO{
	
	private static final Log logger = LogFactory.getLog(UnitTypeMatrixDAO.class);
	
	/* Retrieves all the unit and type matrix
	 * @see org.ifmc.mat.dao.UnitTypeMatrixDAO#getAllUnitMatrix()
	 */
	public List<UnitMatrixDTO> getAllUnitMatrix(){
		
		List<UnitMatrixDTO> unitTypeMatrixDTOList = new ArrayList<UnitMatrixDTO>();
		logger.info("Getting all the rows from the Unit Type Matrix table");
		Session session = getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<UnitMatrix> unitTypeMatrixList = session.createCriteria(UnitMatrix.class).list();
		for(UnitMatrix unitMatrix: unitTypeMatrixList){
			UnitMatrixDTO matrixDTO =  new UnitMatrixDTO();
			matrixDTO.setId(unitMatrix.getUnitTypeId());
			matrixDTO.setItem(unitMatrix.getUnitId());
			unitTypeMatrixDTOList.add(matrixDTO);
		}
		return unitTypeMatrixDTOList;
	}
}
