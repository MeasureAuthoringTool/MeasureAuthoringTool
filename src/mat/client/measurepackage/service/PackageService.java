package mat.client.measurepackage.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.shared.MatException;


/**
 * The Interface PackageService.
 */
@RemoteServiceRelativePath("package")
public interface PackageService extends RemoteService {
	
	/**
	 * Gets the clauses and packages for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the clauses and packages for measure
	 */
	public MeasurePackageOverview getClausesAndPackagesForMeasure(String measureId);
	
	/**
	 * Gets the clauses and packages for measure list.
	 * 
	 * @param measureList
	 *            the List of measures
	 * @return the clauses and packages for measures
	 */
	public Map<String, MeasurePackageOverview> getClausesAndPackagesForMeasures(List<ManageMeasureSearchModel.Result> measureList);
	
	/**
	 * Save.
	 *
	 * @param detail            the detail
	 * @return the measure package save result
	 * @throws MatException             the mat exception
	 */
	public MeasurePackageSaveResult save(MeasurePackageDetail detail) throws MatException;
	
	/**
	 * Delete.
	 * 
	 * @param pkg
	 *            the pkg
	 */
	public void delete(MeasurePackageDetail pkg);
	
	/**
	 * Save qdm data.
	 * 
	 * @param detail
	 *            the detail
	 * @throws MatException
	 *             the mat exception
	 */
	public void saveQDMData(MeasurePackageDetail detail) throws MatException;

	/**
	 * Save risk variables.
	 *
	 * @param currentDetail the current detail
	 */
	void saveRiskVariables(MeasurePackageDetail currentDetail);
}
