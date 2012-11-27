package org.ifmc.mat.client.measure.service;

import java.util.List;

import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.client.measure.ManageMeasureSearchModel;
import org.ifmc.mat.client.measure.ManageMeasureShareModel;
import org.ifmc.mat.client.shared.MatException;
import org.ifmc.mat.model.Author;
import org.ifmc.mat.model.MeasureType;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("measureLibrary")
public interface MeasureService extends RemoteService {
	public ManageMeasureDetailModel getMeasure(String key);
	public SaveMeasureResult save(ManageMeasureDetailModel model);
	public SaveMeasureResult saveFinalizedVersion(String measureId,boolean isMajor,String version) ;
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model);
	public ManageMeasureSearchModel search(String searchText, int startIndex, int pageSize);
	public ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize);
	public void updateUsersShare(ManageMeasureShareModel model);
	public ManageMeasureDetailModel deleteAuthors(String measureId,List<Author> selectedAuthorsList);
	public ManageMeasureDetailModel deleteMeasureTypes(String measureId,List<MeasureType> selectedMeasureTypeList);
	public ValidateMeasureResult validateMeasureForExport(String key) throws MatException;
	public SaveMeasureResult updateLockedDate(String measureId,String userId);
	SaveMeasureResult resetLockedDate(String measureId,String userId);
	ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,
			int pageSize);
	ManageMeasureSearchModel searchMeasuresForDraft(int startIndex, int pageSize);
	public boolean isMeasureLocked(String id);
	public int getMaxEMeasureId();
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureId);
	
	
}
