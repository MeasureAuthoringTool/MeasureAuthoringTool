package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;

public interface MeasureDAO extends IDAO<Measure, String> {
	public void saveMeasure(Measure measure);
	public List<MeasureShareDTO> getMeasureShareInfoForMeasure(String measureId, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasureShareInfoForUser(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasuresForVersion(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasuresForDraft(User user, int startIndex, int pageSize);
	public List<MeasureShareDTO> getMeasureShareInfoForUser(String searchText, User user, int startIndex, int pageSize);
	public int countMeasureShareInfoForUser(User user);
	public int countMeasureForVersion(User user);
	public int countMeasureForDraft(User user);
	public int countMeasureShareInfoForUser(String searchText, User user);
	public int countUsersForMeasureShare();
	public java.util.List<Measure> findByOwnerId(String measureOwnerId);
	public void resetLockDate(Measure m);
	public String findMaxVersion(String measureSetId);
	public String findMaxOfMinVersion(String measureSetId, String version);
	public boolean isMeasureLocked(String id);
	public int getMaxEMeasureId();
	public int saveandReturnMaxEMeasureId(Measure measure);
	public List<Measure> getAllMeasuresInSet(List<Measure> ms);
	List<MeasureShare> getMeasureShareForMeasure(String measureId);
	List<MeasureShareDTO> getMeasureShareInfoForUserWithFilter(String searchText, User user,
			int startIndex, int pageSize, int filter);
	int countMeasureShareInfoForUser(int filter, User user);
}
