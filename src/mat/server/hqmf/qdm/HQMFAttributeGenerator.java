package mat.server.hqmf.qdm;

import mat.model.clause.MeasureExport;

/**
 * @deprecated this class is depcreated since it is an old version of QDM. It should not be modified. 
 * @author jmeyer
 *
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
