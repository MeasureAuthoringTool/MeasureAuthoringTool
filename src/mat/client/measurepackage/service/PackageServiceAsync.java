package mat.client.measurepackage.service;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface PackageServiceAsync.
 */
public interface PackageServiceAsync {
	
	/**
	 * Gets the clauses and packages for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param callback
	 *            the callback
	 * @return the clauses and packages for measure
	 */
	public void getClausesAndPackagesForMeasure(String measureId, AsyncCallback<MeasurePackageOverview> callback);
	
	/**
	 * Save.
	 *
	 * @param detail the detail
	 * @param callback the callback
	 */
	void save(MeasurePackageDetail detail, AsyncCallback<MeasurePackageSaveResult> callback);
	
	/**
	 * Save qdm data.
	 * 
	 * @param detail
	 *            the detail
	 * @param callback
	 *            the callback
	 */
	public void saveQDMData(MeasurePackageDetail detail, AsyncCallback<Void> callback);
	
	/**
	 * Delete.
	 * 
	 * @param pkg
	 *            the pkg
	 * @param callback
	 *            the callback
	 */
	public void delete(MeasurePackageDetail pkg, AsyncCallback<Void> callback);

	/**
	 * Save risk variables.
	 *
	 * @param currentDetail the current detail
	 * @param asyncCallback the async callback
	 */
	public void saveRiskVariables(MeasurePackageDetail currentDetail,
			AsyncCallback<Void> asyncCallback);
}
