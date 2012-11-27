package org.ifmc.mat.server.service.impl;

import java.util.List;

import mat.dao.clause.QDSAttributesDAO;
import mat.model.clause.QDSAttributes;
import mat.server.clause.QDSAttributesServiceImpl;

import org.ifmc.mat.dao.SpringInitializationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QDSAttributesServiceImplTest extends SpringInitializationTest {
	@Autowired
	protected  QDSAttributesServiceImpl  qDSAttributesServiceImpl;
	
	@Test
	public void testLoadClause() {
		String dataTypeName = "procedure, result";
		qDSAttributesServiceImpl.setContext(applicationContext);
		List<QDSAttributes> listOfAttrib = qDSAttributesServiceImpl.getAllDataTypeAttributes(dataTypeName);
		List<QDSAttributes> listOfAttrib1 = qDSAttributesServiceImpl.getAllDataFlowAttributeName();
		
		assertTrue(listOfAttrib!=null);
		assertTrue(listOfAttrib1!=null);
	}

	@Test
	public void test_v1_0_2_US142_FindByDataType() {
		String[] qdmnamesPass = {
				"Test:QDM: Name: procedure, result - 11371240.5295.4418.9174.713345441468",
				"Test-QDM-Name: procedure, result - 11371240.5295.4418.9174.713345441468",
				"Test-QDM-Name: Communication: From Patient to Provider - 11371240.5295.4418.9174.713345441468",
				"Test:QDM: Name: Communication: From Patient to Provider - 11371240.5295.4418.9174.713345441468"};
		
		QDSAttributesDAO qdsAttributesDAO = qDSAttributesServiceImpl.getDAO();
		
		for(String qdmname : qdmnamesPass){
			List<QDSAttributes> qdmAttrs = qdsAttributesDAO.findByDataType(qdmname, applicationContext);
			assertFalse(qdmAttrs.isEmpty());
		}
	}
}
