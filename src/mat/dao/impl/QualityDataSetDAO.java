package mat.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.QualityDataSet;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.model.QDSTerm;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class QualityDataSetDAO extends GenericDAO<QualityDataSet, String> implements mat.dao.QualityDataSetDAO {
	
	
	private String getQDSQueryString(boolean showSDEs, String measureId){
		String query = "select q.id from org.ifmc.mat.model.QualityDataSet q, org.ifmc.mat.model.ListObject l " +
				"where q.measureId.id = '"+measureId+"' " + "and l.id = q.listObject.id ";
		if(!showSDEs){
				query +="and  l.oid not in (" +
						"'"+ConstantMessages.GENDER_OID+"',"+
						"'"+ConstantMessages.ETHNICITY_OID+"',"+
						"'"+ConstantMessages.RACE_OID+"',"+
						"'"+ConstantMessages.PAYER_OID+"')";
		}
		return query;
	}
	@Override
	public java.util.List<QualityDataSetDTO> getQDSElements(boolean showSDEs, String measureId){
		
		List<QualityDataSetDTO> dtos = new ArrayList<QualityDataSetDTO>();
		
		Session session = getSessionFactory().getCurrentSession();
		String query = getQDSQueryString(showSDEs, measureId);
		List<String> qids = session.createQuery(query).list();
		
		if(qids.isEmpty())
			return dtos;
		
		Criteria criteria = session.createCriteria(QualityDataSet.class);
//		criteria.add(Restrictions.not(Restrictions.in("listObject.oid", ConstantMessages.SUPPLEMENTAL_DATA_ELEMENT_OID_ARR)));
		criteria.add(Restrictions.in("id", qids));
		
		List<QualityDataSet> qds = criteria.list();
		
		for (QualityDataSet aqds: qds) {
			QualityDataSetDTO dto = new QualityDataSetDTO();
			dto.setId(aqds.getId());
			dto.setDataType(aqds.getDataType().getDescription());
			dto.setCodeListName(aqds.getListObject().getName());
			dto.setOccurrenceText(aqds.getOccurrence());
			dto.setSuppDataElement(aqds.isSuppDataElement());
			dto.setOid(aqds.getListObject().getOid());
			dtos.add(dto);
		}
		return dtos;
	}
	
	public java.util.List<QualityDataSetDTO> getQDSElementsFor(String measureId, String listObjectId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QualityDataSet.class);
		criteria.add(Restrictions.eq("measureId.id", measureId));
		criteria.add(Restrictions.eq("listObject.id", listObjectId));
		List<QualityDataSet> qds = criteria.list();
		List<QualityDataSetDTO> dtos =new ArrayList<QualityDataSetDTO>();
		for (QualityDataSet aqds: qds) {
			QualityDataSetDTO dto = new QualityDataSetDTO();
			dto.setId(aqds.getId());
			dto.setDataType(aqds.getDataType().getDescription());
			dto.setCodeListName(aqds.getListObject().getName());
			dto.setOccurrenceText(aqds.getOccurrence());
			dtos.add(dto);
		}
		return dtos;
	}
	
	public void cloneQDSElements(String measureId, mat.model.clause.Measure clonedMeasure) {

		java.util.List<QualityDataSet> clonedQDSs = getForMeasure(clonedMeasure.getId());
		java.util.List<QualityDataSet> origQDSs = getForMeasure(measureId);
		List<QualityDataSet> persistQDSs = removeduplicate(origQDSs, clonedQDSs);
		//check to see if qdm elements for new measure is already cloned
		if (!persistQDSs.isEmpty()) {
			for (QualityDataSet aqds: persistQDSs) {
				QualityDataSet cloneDqds = new QualityDataSet();
				if (aqds.getDataType()!=null) cloneDqds.setDataType(aqds.getDataType());
				if (aqds.getListObject()!=null) cloneDqds.setListObject(aqds.getListObject());
				if (aqds.getMeasureId()!=null) cloneDqds.setMeasureId(clonedMeasure);
				if (aqds.getVersion()!=null) cloneDqds.setVersion(aqds.getVersion());
				if (aqds.getOid()!=null) cloneDqds.setOid(aqds.getOid());
				if (aqds.getOccurrence()!=null) cloneDqds.setOccurrence(aqds.getOccurrence());
				cloneDqds.setSuppDataElement(aqds.isSuppDataElement());//US 594 adding supplimental data value while cloning
				this.save(cloneDqds);
			}
		}
	}
	
	public void cloneSelectedQDSElements(String measureId, mat.model.clause.Measure clonedMeasure, List<QDSTerm> allQDSTermsInClause) {
		
		java.util.List<QualityDataSet> clonedQDSs = getForMeasure(clonedMeasure.getId());
		java.util.List<QualityDataSet> origQDSs = getForMeasure(measureId);
		List<QualityDataSet> persistQDSs = removeduplicate(origQDSs, clonedQDSs);
		//check to see if qdm elements for new measure is already cloned
		if (!persistQDSs.isEmpty()) {
			for (QualityDataSet aqds: persistQDSs) {
				for (QDSTerm qt : allQDSTermsInClause) {
					QualityDataSet qds = this.find(qt.getqDSRef());
					String listObjectId = qds.getListObject().getId();
					String dataTypeId = qds.getDataType().getId();
					String occurrence = qds.getOccurrence();
					String aListObjectId = aqds.getListObject().getId();
					String aDataTypeId = aqds.getDataType().getId();
					String aoccurrence = aqds.getOccurrence(); 
					/*
					 * clone only QDSTerms found in the clause and match QualityDataSets on:
					 * (1) list object (2) data type (3) occurrence NOTE: null null is a match
					 */
					boolean listObjectsMatch = aListObjectId.equalsIgnoreCase(listObjectId);
					boolean dataTypesMatch = aDataTypeId.equalsIgnoreCase(dataTypeId);
					boolean occurrencesMatch = (occurrence == null && aoccurrence == null) ? true :
						(occurrence == null && aoccurrence != null) ? false :
							(occurrence != null && aoccurrence == null) ? false : 
								occurrence.equalsIgnoreCase(aoccurrence);
					if (listObjectsMatch && dataTypesMatch && occurrencesMatch) {
						QualityDataSet cloneDqds = new QualityDataSet();
						if (aqds.getDataType()!=null) cloneDqds.setDataType(aqds.getDataType());
						if (aqds.getListObject()!=null) cloneDqds.setListObject(aqds.getListObject());
						if (aqds.getMeasureId()!=null) cloneDqds.setMeasureId(clonedMeasure);
						if (aqds.getVersion()!=null) cloneDqds.setVersion(aqds.getVersion());
						if (aqds.getOid()!=null) cloneDqds.setOid(aqds.getOid());
						if (aqds.getOccurrence()!=null) cloneDqds.setOccurrence(aqds.getOccurrence());
						cloneDqds.setSuppDataElement(aqds.isSuppDataElement());//US 594 adding supplimental data value while cloning
						this.save(cloneDqds);
					}
				}
			}
		}
	}	
	
	private List<QualityDataSet> removeduplicate(List<QualityDataSet> origQDSs,
			List<QualityDataSet> clonedQDSs) {

		
		List<QualityDataSet> temp = new ArrayList<QualityDataSet>();
		
		for (QualityDataSet origQDS: origQDSs) {
			boolean foundQDS = false;

			for (QualityDataSet clonedQDS: clonedQDSs) {

				if(clonedQDS.getListObject().getId().equals(origQDS.getListObject().getId()) &&
						clonedQDS.getDataType().equals(origQDS.getDataType())) {
					foundQDS = true;
					break;
				}
			}
			//no qds with this definition exists. So, go ahead and add another.
			if (!foundQDS) {
				temp.add(origQDS);
			}
			
		}
		return temp;
	}

	@Override
	public String generateUniqueOid() {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("insert into QUALITY_DATA_MODEL_OID_GEN values ();");
		query.executeUpdate();
		query = session.createSQLQuery("select LAST_INSERT_ID();");
		String retStr = query.uniqueResult().toString();
		return retStr;
	}

	@Override
	public List<QualityDataSet> getForMeasure(String measureId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QualityDataSet.class);
		criteria.add(Restrictions.eq("measureId.id", measureId));
		List<QualityDataSet> qds = criteria.list();
		return qds;
	}

	@Override
	public List<QualityDataSet> getQDSElementsFor(String measureId,
			String codeListId, String dataTypeId, String occurrence) {
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QualityDataSet.class);
		criteria.add(Restrictions.eq("measureId.id", measureId));
		criteria.add(Restrictions.eq("listObject.id", codeListId));
		criteria.add(Restrictions.eq("dataType.id", dataTypeId));
		if(occurrence != null)
			criteria.add(Restrictions.eq("occurrence", occurrence));
		return criteria.list();
		
	}
	
	@Override
	public List<QualityDataSet> getQDMsById(List<String> qdmids) {
		if(qdmids.isEmpty())
			return new ArrayList<QualityDataSet>(); 
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QualityDataSet.class);
		criteria.add(Restrictions.in("id", qdmids));
		List<QualityDataSet> qds = criteria.list();
		return qds;
	}
	
	@Override
	public void updateListObjectId(String oldLOID, String newLOID) {
		Session session = getSessionFactory().getCurrentSession();
		//update quality data model references from the list object being drafted (oldLOID) to the draft (newLOID)
		SQLQuery query = session.createSQLQuery("update QUALITY_DATA_MODEL q set q.LIST_OBJECT_ID = '"+newLOID+"' where q.LIST_OBJECT_ID in (select LIST_OBJECT_ID from LIST_OBJECT where OID in (select OID from LIST_OBJECT where LIST_OBJECT_ID= '"+oldLOID+"'));");
		int ret = query.executeUpdate();
		//update any qdm's that referenced the list object being drafted
		//and delete any duplicate qdm's
		updateQDMTerms(newLOID);
	}
	
	public List<QualityDataSet> getByListObject(String listObjectId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(QualityDataSet.class);
		criteria.add(Restrictions.eq("listObject.id", listObjectId));
		criteria.addOrder(Order.desc("measureId.id")).addOrder(Order.desc("dataType.id")).addOrder(Order.desc("occurrence"));
		List<QualityDataSet> qds = criteria.list();
		return qds;
	}
	
	public void updateQDMTerms(String newLOID) {
		List<QualityDataSet> qdss = getByListObject(newLOID);
		
		//QualityDataSet's to delete
		HashMap<String, String> dqdss = new HashMap<String, String>();
		
		for(int i = 0; i < qdss.size()-1; i++){
			QualityDataSet qds1 = qdss.get(i);
			QualityDataSet qds2 = qdss.get(i+1);
			String mid1 = qds1.getMeasureId().getId();
			String mid2 = qds2.getMeasureId().getId();
			if(mid1.equalsIgnoreCase(mid2)){
				//check dataType, could be multiple dataTypes with the same category
				String dt1 = qds1.getDataType().getId();
				String dt2 = qds2.getDataType().getId();
				if(dt1.equalsIgnoreCase(dt2)){
					String occ1 = qds1.getOccurrence();
					String occ2 = qds2.getOccurrence();
					if(occ1 == null && occ2 == null){
						//delete: same measure, same dataType, not occurrences
						dqdss.put(qds2.getId(), qds1.getId());
					}else if(occ1 != null && occ2 != null){
						if(occ1.equalsIgnoreCase(occ2)){
							//delete: same measure, same category, same occurrence
							dqdss.put(qds2.getId(), qds1.getId());
						}
					}
					
				}
			}
		}
		for(String key : dqdss.keySet()){
			String newID = key;
			String oldID = dqdss.get(key);
			//update the QDMTerm to use the latest QDM
			updateQDMTerm(newID, oldID);
			//delete the duplicate QDM
			deleteOldQDM(oldID);
		}
	}
	
	public void updateQDMTerm(String newID, String oldID){
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("update QDM_TERM t set t.QDM_ELEMENT_ID = '"+newID+"' where t.QDM_ELEMENT_ID = '"+oldID+"';");
		int ret = query.executeUpdate();
	}
	
	public void deleteOldQDM(String oldID){
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("delete from QUALITY_DATA_MODEL where QUALITY_DATA_MODEL_ID = '"+oldID+"';");
		int ret = query.executeUpdate();
	}
}
