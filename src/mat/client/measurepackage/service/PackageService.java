package mat.client.measurepackage.service;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.shared.MatException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("package")
public interface PackageService extends RemoteService {
	public MeasurePackageOverview getClausesAndPackagesForMeasure(String measureId);
	public void save(MeasurePackageDetail detail) throws MatException;
	public void delete(MeasurePackageDetail pkg);
	public void saveQDMData(MeasurePackageDetail detail) throws MatException;
}
