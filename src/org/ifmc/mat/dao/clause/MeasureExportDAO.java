package org.ifmc.mat.dao.clause;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.MeasureExport;

public interface MeasureExportDAO extends IDAO<MeasureExport, String> {
	public MeasureExport findForMeasure(String measureId);
}
