package mat.sprint22.junit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.dao.SpringInitializationTest;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.server.MeasureLibraryServiceImpl;
import mat.server.service.SimpleEMeasureService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;


public class Sprint22Tests extends SpringInitializationTest{
	
	//Start US166
	private MeasureLibraryServiceImpl measureLibService = new MeasureLibraryServiceImpl();
	@Test
	@Rollback
	public void S22_US166_eMeasureId_Test(){
		MeasureDAO measureDAO = getService().getMeasureDAO();
		List<Measure> ms = measureDAO.find();
		List<Measure> mset = new ArrayList<Measure>();
		ArrayList<Measure> temp = new ArrayList<Measure>();
		String key = null;
		for(Measure m : ms){
			if(m.geteMeasureId() == 0){
				key = m.getId();
				temp.clear();
				temp.add(m);
				mset.clear();
				mset = measureDAO.getAllMeasuresInSet(temp);
				if(mset.size()>1)				
					break;
			}
		}
		if(key != null){
			measureLibService.setContext(applicationContext);
			ManageMeasureDetailModel model = measureLibService.getMeasure(key);
			int eid = measureDAO.getMaxEMeasureId();
			model.seteMeasureId(eid);
			SaveMeasureResult result = measureLibService.saveMeasureDetails(model);
			assertNotNull(result);
			Measure m = measureDAO.find(key);
			assertEquals(eid, m.geteMeasureId());
		}
	}
	//End US166
	
	//Start US160
	@Autowired
	private MeasureExportDAO measureExportDAO;
	@Autowired
	private SimpleEMeasureService eMeasureService;
	
	@Test
	public void S22_US159_US160_eMeasureId_GUID_Test() throws Exception {
		if(measureExportDAO == null)
			measureExportDAO = getService().getMeasureExportDAO();
		
		String string = "March 7, 2012";
		Date threshold = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(string);

		List<MeasureExport> mes = measureExportDAO.find();
		String mid = null;
		for(MeasureExport me : mes){
		
			Measure m = me.getMeasure();
			Date mDate = m.getExportedDate();
			if(mDate.after(threshold))
				mid = m.getId();
		}
		if(mid != null){
			String emeasureHTML = eMeasureService.getEMeasureHTML(mid).export;
			boolean hasEmeasureIdentifier = emeasureHTML.contains("eMeasure Identifier<br>(Measure Authoring Tool)");
			boolean hasGUID = emeasureHTML.contains("GUID");
			assertTrue(hasEmeasureIdentifier);
			assertTrue(hasGUID);
		}
	}
	//Enc US160
}
