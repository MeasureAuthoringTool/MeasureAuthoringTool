package org.ifmc.mat.server.service.impl;


import java.util.UUID;

import org.ifmc.mat.dao.SpringInitializationTest;
import org.ifmc.mat.dao.clause.MeasureSetDAO;
import org.ifmc.mat.model.User;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.MeasureSet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class MeasureCreateTest extends SpringInitializationTest  {	
	
	@Autowired
	protected MeasureSetDAO measureSetDAO;
	
	@Test
	@Rollback(true)
	public void testCreateMeasure() {
		
		Measure measure = new Measure();
		measure.setDescription("Measure Test3");
		measure.setaBBRName("Meas Set3");
		measure.setMeasureScoring("Ratio");
		measure.setVersion("0.0");
		measure.setDraft(true);
		User user = new User();
		user.setId("8a4d8f813166c18301316701d1db00fe");
		measure.setOwner(user);		
		MeasureSet measureSet = new MeasureSet();
		measureSet.setId(UUID.randomUUID().toString());
		getService().getMeasureSetDAO().save(measureSet);
		measure.setMeasureSet(measureSet);		
		getService().getMeasureDAO().save(measure);
		//teardownMeasure(measure.getId(),measureSet.getId());
	}
	
	private void teardownMeasure(String measureId, String measureSetId){
		try{
			if(measureId != null)
				getService().getMeasureDAO().delete(measureId);
			if(measureSetId != null)
				getService().getMeasureSetDAO().delete(measureSetId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
