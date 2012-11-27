package org.ifmc.mat.dao;

import java.util.List;

import org.ifmc.mat.model.QualityDataSet;
import org.ifmc.mat.model.QualityDataSetDTO;
import org.ifmc.mat.shared.model.QDSTerm;

public interface QualityDataSetDAO extends IDAO<QualityDataSet, String> {
	public java.util.List<QualityDataSetDTO> getQDSElements(boolean showSDEs, String measureId);
	public java.util.List<QualityDataSetDTO> getQDSElementsFor(String measureId, String codeListId);
	public java.util.List<QualityDataSet> getQDSElementsFor(String measureId, String codeListId, String dataTypeId, String occurrence);
	
	public java.util.List<QualityDataSet> getForMeasure(String measureId);
	public void cloneQDSElements(String measureId, org.ifmc.mat.model.clause.Measure clonedMeasure);
	public void cloneSelectedQDSElements(String measureId, org.ifmc.mat.model.clause.Measure clonedMeasure, List<QDSTerm> allQDSTermsInClause);
	public String generateUniqueOid();
//	public List<QualityDataSet> getUsedQDMsByMeasure(String measureId);
	public List<QualityDataSet> getQDMsById(List<String> qdmids);
	public void updateListObjectId(String oldLOID, String newLOID);
}
