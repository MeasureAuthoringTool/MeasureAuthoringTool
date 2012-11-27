package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Packager;

public interface PackagerDAO extends IDAO<Packager, String> {
	public List<Packager> getForMeasure(String measureId);
	public long getNumberOfPackagesForMeasure(String measureId);
	public void deletePackage(String measureId, String sequence);
	public void deleteAllPackages(String measureId);
}
