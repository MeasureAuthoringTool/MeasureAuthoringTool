package org.ifmc.mat.dao.clause;

import java.util.List;

import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.dao.MetadataDAO;
import org.ifmc.mat.model.User;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.MeasureShareDTO;
import org.springframework.context.ApplicationContext;

public interface MeasureDAO extends IDAO<Measure, String> {
	public void saveMeasure(Measure measure);
	public List<MeasureShareDTO> getMeasureShareInfoForMeasure(String measureId, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasureShareInfoForUser(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasuresForVersion(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasuresForDraft(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasureShareInfoForUser(String searchText, MetadataDAO metadataDAO, User user, int startIndex, int pageSize);
	public int countMeasureShareInfoForUser(User user);
	public int countMeasureForVersion(User user);
	public int countMeasureForDraft(User user);
	public int countMeasureShareInfoForUser(String searchText, User user);
	public int countUsersForMeasureShare();
	public MeasureShareDTO clone(ManageMeasureDetailModel currentDetails, String loggedinUserId, boolean isDraftCreation, 
			ApplicationContext context);
	public java.util.List<Measure> findByOwnerId(String measureOwnerId);
	public void resetLockDate(Measure m);
	public String findMaxVersion(String measureSetId);
	public String findMaxOfMinVersion(String measureSetId, String version);
	public boolean isMeasureLocked(String id);
	public int getMaxEMeasureId();
	public int saveandReturnMaxEMeasureId(Measure measure);
	public List<Measure> getAllMeasuresInSet(List<Measure> ms);
}
