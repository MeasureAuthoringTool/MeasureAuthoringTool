package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.MeasureExport;

public interface MeasureExportDAO extends IDAO<MeasureExport, String> {

	MeasureExport findByMeasureId(String measureId);
}
