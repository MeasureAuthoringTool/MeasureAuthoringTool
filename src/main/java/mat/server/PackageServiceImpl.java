package mat.server;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.measurepackage.service.MeasurePackageSaveResult;
import mat.client.measurepackage.service.PackageService;
import mat.server.service.PackagerService;
import mat.shared.packager.error.SaveRiskAdjustmentVariableException;
import mat.shared.packager.error.SaveSupplementalDataElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PackageServiceImpl extends SpringRemoteServiceServlet implements PackageService{
	@Autowired
	PackagerService packagerService;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1789210947786753971L;
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#getClausesAndPackagesForMeasure(java.lang.String)
	 */
	@Override
	public MeasurePackageOverview getClausesAndPackagesForMeasure(
			String measureId) {
		MeasurePackageOverview overview = buildOverview(measureId);
		return overview;
	}
	
	public Map<String, MeasurePackageOverview> getClausesAndPackagesForMeasures(List<ManageMeasureSearchModel.Result> measureList) {
		Map<String, MeasurePackageOverview> packageMap = new HashMap<>();
		for(ManageMeasureSearchModel.Result result: measureList) {
			String measureId = result.getId();
			MeasurePackageOverview overview = buildOverview(measureId);
			packageMap.put(measureId, overview);
		}
		
		return packageMap;
	}
	
	/**
	 * Builds the overview.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure package overview
	 */
	private MeasurePackageOverview buildOverview(String measureId) {
		return packagerService.buildOverviewForMeasure(measureId);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#save(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public MeasurePackageSaveResult save(MeasurePackageDetail detail) {
		return packagerService.save(detail);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#delete(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void delete(MeasurePackageDetail detail) {
		packagerService.delete(detail);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#saveQDMData(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void saveQDMData(MeasurePackageDetail detail) throws SaveSupplementalDataElementException {
		packagerService.saveQDMData(detail);
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#saveRiskVariables(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void saveRiskVariables(MeasurePackageDetail currentDetail) throws SaveRiskAdjustmentVariableException {
		packagerService.saveRiskAdjVariables(currentDetail);
		
	}
	
	
}
