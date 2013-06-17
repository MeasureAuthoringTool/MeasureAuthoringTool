package mat.dao.clause;

import java.util.HashMap;

import mat.dao.IDAO;
import mat.model.QualityDataModelWrapper;
import mat.model.clause.MeasureXML;

public interface MeasureXMLDAO extends IDAO<MeasureXML, String> {
	public MeasureXML findForMeasure(String measureId);
	public QualityDataModelWrapper createSupplimentalQDM(String measureId, boolean isClone, HashMap<String,String> uuidMap);
}
