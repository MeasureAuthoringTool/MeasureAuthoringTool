package org.ifmc.mat.server.service.impl;

import mat.dao.clause.MeasureExportDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.server.service.impl.SimpleEMeasureServiceImpl;

import org.ifmc.mat.dao.SpringInitializationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleEMeasureServiceImplTest  extends SpringInitializationTest{
	@Autowired
	private MeasureExportDAO measureExportDAO;
	@Autowired
	private SimpleEMeasureServiceImpl eMeasureService;
	
	@Test
	public void testMeasureExport() throws Exception {
		if(measureExportDAO == null)
			measureExportDAO = getService().getMeasureExportDAO();
		
		MeasureExport me = measureExportDAO.find().get(0);
		Measure m = me.getMeasure();
		String mid = m.getId();
		
		String simpleXML = eMeasureService.exportMeasureIntoSimpleXML(mid).export;
		System.out.println("XML:");
		System.out.println(simpleXML);
		
		String emeasureHTML = eMeasureService.getEMeasureHTML(mid).export;
		System.out.println("HTML:");
		System.out.println(emeasureHTML);
		
	}
}
