package mat.dao.clause;


import mat.dao.IDAO;
import mat.model.clause.MeasureSet;


public interface MeasureSetDAO extends IDAO<MeasureSet, String> {
	public void saveMeasureSet(MeasureSet measureSet);
	public MeasureSet findMeasureSet(String measureSetId);
	
}
