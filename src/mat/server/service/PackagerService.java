package mat.server.service;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.measurepackage.service.MeasurePackageSaveResult;

/**
 * The Interface PackagerService.
 */
public interface PackagerService {
	
	/**
	 * Builds the overview for measure.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure package overview
	 */
	public MeasurePackageOverview buildOverviewForMeasure(String measureId);
	
	/**
	 * Save.
	 * 
	 * @param detail
	 *            the detail
	 */
	public MeasurePackageSaveResult save(MeasurePackageDetail detail);
	
	/**
	 * Delete.
	 * 
	 * @param detail
	 *            the detail
	 */
	public void delete(MeasurePackageDetail detail);
	
	/**
	 * Save qdm data.
	 * 
	 * @param detail
	 *            the detail
	 */
	public void saveQDMData(MeasurePackageDetail detail);
}
