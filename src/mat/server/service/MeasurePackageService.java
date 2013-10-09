package mat.server.service;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.model.DataType;
import mat.model.MatValueSet;
import mat.model.QualityDataSet;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
/**MeasurePackageService.java.**/
public interface MeasurePackageService {
	/**
	 * @return long
	 * **/
	long count();
	/**
	 * @param searchText - String.
	 * @return {@link Long#}
	 * **/
	long count(String searchText);
	/**
	 * @param startIndex - {@link Integer}.
	 * @param numResults - {@link Integer}.
	 * @return {@link List} of {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> search(int startIndex, int numResults);
	/**
	 *@param startIndex - {@link Integer}.
	 *@param numResults - {@link Integer}.
	 *@return {@link List} of {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> searchMeasuresForVersion(int startIndex,
			int numResults);
	/**
	 *@return {@link Long}.
	 * **/
	long countMeasuresForVersion();
	/**
	 *@return {@link Long}.
	 * **/
	long countMeasuresForDraft();
	/**
	 *@param startIndex - {@link Integer}.
	 *@param numResults - {@link Integer}.
	 *@return {@link List} {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> searchMeasuresForDraft(int startIndex,
			int numResults);
	/**
	 * @param searchText - {@link String}.
	 * @param startIndex - {@link Integer}.
	 * @param numResults - {@link Integer}.
	  *@return {@link List} {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> search(String searchText, int startIndex,
			int numResults);
	/**
	 * @param  measurePackage - {@link Measure}.
	 *
	 * **/
	void save(Measure measurePackage);
	/**
	 * @param id - {@link String}.
	 * @return {@link Measure}.
	 * **/
	Measure getById(String id);
	/**
	 * @param measureSetId - {@link String}.
	 * @return {@link String}.
	 * **/
	String findOutMaximumVersionNumber(String measureSetId);
	/**
	 * @param measureId - {@link String}.
	 * @param measureSetId - {@link String}.
	 * @return {@link String}.
	 * **/
	String findOutVersionNumber(String measureId, String measureSetId);
	/**
	 * @param measureId - {@link String}.
	 * @param startIndex - {@link Integer}.
	 * @param pageSize - {@link Integer}.
	 * @return {@link List} {@link MeasureShareDTO}.
	 **/
	List<MeasureShareDTO> getUsersForShare(String measureId,
			int startIndex, int pageSize);
	/**
	 *@return {@link Integer}.
	 * **/
	int countUsersForMeasureShare();
	/**
	 * @param model - {@link ManageMeasureShareModel}.
	 * **/
	void updateUsersShare(ManageMeasureShareModel model);
	/**
	 *@param m - {@link Measure}.
	 * **/
	void updateLockedOutDate(Measure m);
	/**
	 *@param key - {@link String}.
	 *@param matValueSetList - {@link ArrayList} of {@link MatValueSet}.
	 *@return {@link ValidateMeasureResult}.
	 *@throws Exception - {@link Exception}.
	 **/
	ValidateMeasureResult validateMeasureForExport(String key,
			ArrayList<MatValueSet> matValueSetList) throws Exception;
	/**
	 *@param id - {@link String}.
	 *@return {@link MeasureSet}.
	 * **/
	MeasureSet findMeasureSet(String id);
	/**
	 *@param measureSet - {@link MeasureSet}.
	 * **/
	void save(MeasureSet measureSet);
	/**
	 *@return {@link String}.
	 * **/
	String getUniqueOid();
	/**
	 *@param dataTypeName - {@link String}.
	 *@param categoryId - {@link String}.
	 *@return {@link DataType}.
	 * **/
	DataType findDataTypeForSupplimentalCodeList(String dataTypeName,
			String categoryId);
	/**
	 *@param measureId - {@link String}.
	 * **/
	void deleteExistingPackages(String measureId);
	/**
	 *@param qds - {@link QualityDataSet}.
	 * **/
	void saveSupplimentalQDM(QualityDataSet qds);
	/**
	 *@param id - {@link String}
	 *@return {@link Boolean}.
	 * **/
	boolean isMeasureLocked(String id);
	/**
	 *@return {@link Integer}.
	 * **/
	int getMaxEMeasureId();
	/**
	 *@param measure - {@link Measure}
	 *@return {@link Integer}.
	 * **/
	int saveAndReturnMaxEMeasureId(Measure measure);
	/**
	 *@param list - {@link List} of {@link String}.
	 *@param toEmail - {@link String}.
	 *
	 * **/
	void transferMeasureOwnerShipToUser(List<String> list, String toEmail);
	/**
	 *@param searchText - {@link String}.
	 *@param startIndex - {@link Integer}.
	 *@param numResults - {@link Integer}.
	 *@param filter - {@link Integer}.
	 *@return {@link List} of {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> searchWithFilter(String searchText, int startIndex,
			int numResults, int filter);
	
	/**
	 *@param searchText - {@link String}.
	 *@param startIndex - {@link Integer}.
	 *@param numResults - {@link Integer}.
	 *@param filter - {@link Integer}.
	 *@return {@link List} of {@link MeasureShareDTO}.
	 * **/
	List<MeasureShareDTO> searchForAdminWithFilter(String searchText, int startIndex,
			int numResults, int filter);
	/**
	 *@param filter - {@link Integer}.
	 *@return {@link Long}
	 * **/
	long count(int filter);
	/**
	 *@param measureId - {@link String}.
	 *@return {@link MeasureXmlModel}.
	 * **/
	MeasureXmlModel getMeasureXmlForMeasure(String measureId);
	/**
	 *@param measureXmlModel - {@link MeasureXmlModel}.
	 * **/
	void saveMeasureXml(MeasureXmlModel measureXmlModel);
	/**
	 *@param stewardName - {@link String}.
	 *@return {@link String}.
	 * **/
	String retrieveStewardOID(String stewardName);
	/**
	 *@param measureId - {@link String}.
	 *@param isPrivate - {@link Boolean}.
	 * **/
	void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);
}
