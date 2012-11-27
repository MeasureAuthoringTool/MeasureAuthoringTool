package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.model.clause.Packager;

public interface PackagerDAO extends IDAO<Packager, String> {
	public List<Packager> getForMeasure(String measureId);
	public long getNumberOfPackagesForMeasure(String measureId);
	public void deletePackage(String measureId, String sequence);
	public void deleteAllPackages(String measureId);
}
