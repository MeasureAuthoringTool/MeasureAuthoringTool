package org.ifmc.mat.server.service;

import org.ifmc.mat.client.measurepackage.MeasurePackageDetail;
import org.ifmc.mat.client.measurepackage.MeasurePackageOverview;

public interface PackagerService {
	public MeasurePackageOverview buildOverviewForMeasure(String measureId);
	public void save(MeasurePackageDetail detail);
	public void delete(MeasurePackageDetail detail);
	public void saveQDMData(MeasurePackageDetail detail);
}
