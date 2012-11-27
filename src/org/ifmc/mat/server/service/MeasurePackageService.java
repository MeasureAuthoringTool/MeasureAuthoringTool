package org.ifmc.mat.server.service;

import java.util.List;

import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.client.measure.ManageMeasureShareModel;
import org.ifmc.mat.client.measure.service.ValidateMeasureResult;
import org.ifmc.mat.model.Author;
import org.ifmc.mat.model.DataType;
import org.ifmc.mat.model.MeasureType;
import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.MeasureSet;
import org.ifmc.mat.model.clause.MeasureShareDTO;
import org.ifmc.mat.model.clause.Metadata;

public interface MeasurePackageService {
	public long count();
	public long count(String searchText);
	public List<MeasureShareDTO> search(int startIndex, int numResults);
	public List<MeasureShareDTO> searchMeasuresForVersion(int startIndex, int numResults);
	public long countMeasuresForVersion();
	public long countMeasuresForDraft();
	public List<MeasureShareDTO> searchMeasuresForDraft(int startIndex, int numResults);
	public List<MeasureShareDTO> search(String searchText, int startIndex, int numResults);
	public void save(Measure measurePackage);
	public void saveMeasureDetails(List<Metadata> measureDetails);
	public Measure getById(String id);
	public Metadata getMetadata(String id);
	public List<Metadata> getMeasureDetailsById(String id);
	public String findOutMaximumVersionNumber(String measureSetId);
	public String findOutVersionNumber(String measureId, String measureSetId);
	public List<MeasureShareDTO> getUsersForShare(String measureId, int startIndex, int pageSize);
	public int countUsersForMeasureShare();
	public void updateUsersShare(ManageMeasureShareModel model);
	public void updateLockedOutDate(Measure m);
//	public void clone(Measure measurePackage, String newName);
	public ManageMeasureDetailModel deleteAuthors(List<Author> authorList,String id);
	public ManageMeasureDetailModel deleteMeasureTypes(List<MeasureType> measureTypeList,String id);
	public void deleteALLDetailsForMeasureId(List<Metadata> metaDetails);
	public void update(String metadataId,String value);
	public ValidateMeasureResult validateMeasureForExport(String key) throws Exception;
	public MeasureSet findMeasureSet(String id);
	public void save(MeasureSet measureSet);
	public String getUniqueOid();
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId);
	public void deleteExistingPackages(String measureId);
	public void saveSupplimentalQDM(QualityDataSet qds);
	public boolean isMeasureLocked(String id);
	public int getMaxEMeasureId();
	public int saveAndReturnMaxEMeasureId(Measure measure);
}
