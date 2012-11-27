package org.ifmc.mat.dao.clause;


import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.MeasureSet;


public interface MeasureSetDAO extends IDAO<MeasureSet, String> {
	public void saveMeasureSet(MeasureSet measureSet);
	public MeasureSet findMeasureSet(String measureSetId);
	
}
