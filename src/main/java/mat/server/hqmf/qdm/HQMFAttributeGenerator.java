package mat.server.hqmf.qdm;

import mat.model.clause.MeasureExport;

/**
 * @deprecated this class is deprecated since it is an old version of QDM. It should not be modified. 
 */
public class HQMFAttributeGenerator extends HQMFDataCriteriaElementGenerator {

	private MeasureExport measureExport;	
	
	@Override
	public String generate(MeasureExport me) throws Exception {
		return null;
	}
	
	public MeasureExport getMeasureExport() {
		return measureExport;
	}
	
	public void setMeasureExport(MeasureExport measureExport) {
		this.measureExport = measureExport;
	}
}
