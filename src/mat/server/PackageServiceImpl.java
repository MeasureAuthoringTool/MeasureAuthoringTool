package mat.server;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.measurepackage.service.PackageService;
import mat.server.service.PackagerService;

/**
 * The Class PackageServiceImpl.
 */
public class PackageServiceImpl extends SpringRemoteServiceServlet implements PackageService{
	
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
	
	/**
	 * Builds the overview.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the measure package overview
	 */
	private MeasurePackageOverview buildOverview(String measureId) {
		return getPackagerService().buildOverviewForMeasure(measureId);
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#save(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void save(MeasurePackageDetail detail) {
		getPackagerService().save(detail);
	}

	/**
	 * Gets the packager service.
	 * 
	 * @return the packager service
	 */
	private PackagerService getPackagerService() {
		return (PackagerService)context.getBean("packagerService");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#delete(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void delete(MeasurePackageDetail detail) {
		getPackagerService().delete(detail);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.service.PackageService#saveQDMData(mat.client.measurepackage.MeasurePackageDetail)
	 */
	@Override
	public void saveQDMData(MeasurePackageDetail detail) {
		getPackagerService().saveQDMData(detail);
	}

	
}
