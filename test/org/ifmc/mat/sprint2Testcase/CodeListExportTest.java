package org.ifmc.mat.sprint2Testcase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ifmc.mat.dao.SpringInitializationTest;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.dao.clause.MeasureExportDAO;
import org.ifmc.mat.server.service.impl.CodeListXLSGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CodeListExportTest extends SpringInitializationTest {

	@Autowired
	protected MeasureExportDAO measureExportDAO;

	@Autowired
	protected MeasureDAO measureDAO;

	@Test
	public void testMeasureExport() {
		
	}
	@Test
	public void testGetErrorXLS() {
		CodeListXLSGenerator clgen = new CodeListXLSGenerator();
		HSSFWorkbook wkbk = clgen.getErrorXLS();
		
		wkbk.getSheet(wkbk.getSheetName(0));
	}
}
