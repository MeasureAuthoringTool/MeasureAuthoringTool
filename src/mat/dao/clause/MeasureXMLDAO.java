package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.MeasureXML;

public interface MeasureXMLDAO extends IDAO<MeasureXML, String> {
	public MeasureXML findForMeasure(String measureId);
}
