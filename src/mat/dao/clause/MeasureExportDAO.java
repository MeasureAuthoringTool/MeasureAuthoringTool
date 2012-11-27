package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.MeasureExport;

public interface MeasureExportDAO extends IDAO<MeasureExport, String> {
	public MeasureExport findForMeasure(String measureId);
}
