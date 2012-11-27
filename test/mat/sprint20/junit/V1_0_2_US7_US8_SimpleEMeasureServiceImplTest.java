package mat.sprint20.junit;

import mat.dao.SpringInitializationTest;
import mat.dao.clause.MeasureExportDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.server.service.impl.SimpleEMeasureServiceImpl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class V1_0_2_US7_US8_SimpleEMeasureServiceImplTest extends SpringInitializationTest{
	@Autowired
	private MeasureExportDAO measureExportDAO;
	@Autowired
	private SimpleEMeasureServiceImpl eMeasureService;
	
	@Test
	public void test_v1_0_2_US7_US8_MeasureExport() throws Exception {
		if(measureExportDAO == null)
			measureExportDAO = getService().getMeasureExportDAO();
		
		MeasureExport me = measureExportDAO.find().get(0);
		Measure m = me.getMeasure();
		String mid = m.getId();

		String emeasureXML = eMeasureService.getEMeasureXML(mid).export;
		System.out.println("XML:");
		System.out.println(emeasureXML);
		
		boolean hasEVNdotCRT = emeasureXML.contains("\"EVN.CRT\"");
		
		assertEquals("\"EVN.CRT\" found in emeasure XML", false, hasEVNdotCRT);
		
		boolean hasCRT = emeasureXML.contains("\"CRT\"");
		
		assertEquals("\"CRT\" found in emeasure XML", false, hasCRT);
	}
}
