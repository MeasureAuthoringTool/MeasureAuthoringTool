package mat.sprint2Testcase;

import mat.dao.SpringInitializationTest;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.server.service.impl.CodeListXLSGenerator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
