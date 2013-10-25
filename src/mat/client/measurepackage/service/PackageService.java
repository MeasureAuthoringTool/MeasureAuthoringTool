package mat.client.measurepackage.service;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.shared.MatException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	 * Save.
	 * 
	 * @param detail
	 *            the detail
	 * @throws MatException
	 *             the mat exception
	 */
	public void save(MeasurePackageDetail detail) throws MatException;
	
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
}
