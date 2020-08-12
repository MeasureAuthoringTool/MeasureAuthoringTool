package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.model.QualityDataSet;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("qualityDataSetDAO")
public class QualityDataSetDAOImpl extends GenericDAO<QualityDataSet, String> implements mat.dao.QualityDataSetDAO {

	private static final String DATA_TYPE = "dataType";
	private static final String MEASURE_ID = "measureId";
	private static final String LIST_OBJECT = "listObject";
	
	public QualityDataSetDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	private String getQDSQueryString(boolean showSDEs){
		String query = "select q.id from mat.model.QualityDataSet q, mat.model.ListObject l " +
		"where q.measureId.id = :measureId and l.id = q.listObject.id ";
		if(!showSDEs){
				query +="and  l.oid not in (" +
						"'"+ConstantMessages.GENDER_OID+"',"+
						"'"+ConstantMessages.ETHNICITY_OID+"',"+
						"'"+ConstantMessages.RACE_OID+"',"+
						"'"+ConstantMessages.PAYER_OID+"')";
				
		}
		return query;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public java.util.List<QualityDataSetDTO> getQDSElements(boolean showSDEs, String measureId){
		
		final Session session = getSessionFactory().getCurrentSession();
		final String sql = getQDSQueryString(showSDEs);
		
		final NativeQuery<String> query = session.createNativeQuery(sql);
		query.setParameter(MEASURE_ID, measureId);
		
		final List<String> qids = query.list();
		
		if(qids.isEmpty())
			return new ArrayList<>();
		
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QualityDataSetDTO> cq = cb.createQuery(QualityDataSetDTO.class);
		final Root<QualityDataSet> root = cq.from(QualityDataSet.class);
		
		cq.select(cb.construct(
					QualityDataSetDTO.class,
					root.get("id"),
					root.get(DATA_TYPE).get("description"),
					root.get(LIST_OBJECT).get("name"),
					root.get("occurrence"),
					root.get(LIST_OBJECT).get("oid"),
					root.get("suppDataElement")));
		
		cq.where(root.get("id").in(qids));		
		
		return session.createQuery(cq).getResultList();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#getQDSElementsFor(java.lang.String, java.lang.String)
	 */
	@Override
	public java.util.List<QualityDataSetDTO> getQDSElementsFor(String measureId, String listObjectId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QualityDataSetDTO> cq = cb.createQuery(QualityDataSetDTO.class);
		final Root<QualityDataSet> root = cq.from(QualityDataSet.class);
		
		cq.select(cb.construct(
				QualityDataSetDTO.class,
				root.get("id"),
				root.get(DATA_TYPE).get("description"),
				root.get(LIST_OBJECT).get("name"),
				root.get("occurrence")));

		cq.where(cb.and(cb.equal(root.get(MEASURE_ID).get("id"), measureId), cb.equal(root.get(LIST_OBJECT).get("id"), listObjectId)));		

		return session.createQuery(cq).getResultList();
	}
	
	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#cloneQDSElements(java.lang.String, mat.model.clause.Measure)
	 */
	@Override
	public void cloneQDSElements(String measureId, mat.model.clause.Measure clonedMeasure) {

		final java.util.List<QualityDataSet> clonedQDSs = getForMeasure(clonedMeasure.getId());
		final java.util.List<QualityDataSet> origQDSs = getForMeasure(measureId);
		final List<QualityDataSet> persistQDSs = removeduplicate(origQDSs, clonedQDSs);
		//check to see if qdm elements for new measure is already cloned
		if (!persistQDSs.isEmpty()) {
			for (final QualityDataSet aqds: persistQDSs) {
				final QualityDataSet cloneDqds = new QualityDataSet();
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
	
	
	/**
	 * Removeduplicate.
	 * 
	 * @param origQDSs
	 *            the orig qd ss
	 * @param clonedQDSs
	 *            the cloned qd ss
	 * @return the list
	 */
	private List<QualityDataSet> removeduplicate(List<QualityDataSet> origQDSs,
			List<QualityDataSet> clonedQDSs) {

		
		final List<QualityDataSet> temp = new ArrayList<>();
		
		for (final QualityDataSet origQDS: origQDSs) {
			boolean foundQDS = false;

			for (final QualityDataSet clonedQDS: clonedQDSs) {

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

	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#generateUniqueOid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String generateUniqueOid() {
		final Session session = getSessionFactory().getCurrentSession();
		final NativeQuery nativeQuery = session.createNativeQuery("insert into QUALITY_DATA_MODEL_OID_GEN values ();");
		nativeQuery.executeUpdate();
		final NativeQuery query = session.createNativeQuery("select LAST_INSERT_ID();");

		return query.uniqueResult().toString();
	}

	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#getForMeasure(java.lang.String)
	 */
	@Override
	public List<QualityDataSet> getForMeasure(String measureId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QualityDataSet> query = cb.createQuery(QualityDataSet.class);
		final Root<QualityDataSet> root = query.from(QualityDataSet.class);
		
		query.select(root).where(cb.equal(root.get(MEASURE_ID).get("id"), measureId));

		return session.createQuery(query).getResultList();
	}

	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#getQDSElementsFor(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<QualityDataSet> getQDSElementsFor(String measureId,
			String codeListId, String dataTypeId, String occurrence) {
		
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QualityDataSet> query = cb.createQuery(QualityDataSet.class);
		final Root<QualityDataSet> root = query.from(QualityDataSet.class);
		
		final Predicate p1 = cb.and(cb.equal(root.get(MEASURE_ID).get("id"), measureId), 
				cb.equal(root.get(LIST_OBJECT).get("id"), codeListId), 
				cb.equal(root.get(DATA_TYPE).get("id"), dataTypeId));
		
		Predicate p2 = null;
		if (occurrence != null) {
			p2 = cb.equal(root.get("occurrence"), occurrence);
		}
		
		final Predicate predicate = (p2 != null) ? cb.and(p1, p2) : p1;
		
		query.select(root).where(predicate);

		return session.createQuery(query).getResultList();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.dao.QualityDataSetDAO#updateListObjectId(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void updateListObjectId(String oldLOID, String newLOID) {
		final Session session = getSessionFactory().getCurrentSession();
		//update quality data model references from the list object being drafted (oldLOID) to the draft (newLOID)
		final String sql = "update QUALITY_DATA_MODEL q set q.LIST_OBJECT_ID = :newLOID where q.LIST_OBJECT_ID in (select LIST_OBJECT_ID from LIST_OBJECT where OID in (select OID from LIST_OBJECT where LIST_OBJECT_ID= :oldLOID";
		final NativeQuery nativeQuery = session.createNativeQuery(sql);
		nativeQuery.setParameter("newLOID", newLOID);
		nativeQuery.setParameter("oldLOID", oldLOID);
		nativeQuery.executeUpdate();
		//update any qdm's that referenced the list object being drafted
		//and delete any duplicate qdm's
		updateQDMTerms(newLOID);
	}
	
	/**
	 * Gets the by list object.
	 * 
	 * @param listObjectId
	 *            the list object id
	 * @return the by list object
	 */
	public List<QualityDataSet> getByListObject(String listObjectId) {
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<QualityDataSet> query = cb.createQuery(QualityDataSet.class);
		final Root<QualityDataSet> root = query.from(QualityDataSet.class);
		
		query.select(root).where(cb.equal(root.get(LIST_OBJECT).get("id"), listObjectId));
		query.orderBy(cb.desc(root.get(MEASURE_ID).get("id")), cb.desc(root.get(DATA_TYPE).get("id")), cb.desc(root.get("occurrence")));
		
		return session.createQuery(query).getResultList();
	}
	
	/**
	 * Update qdm terms.
	 * 
	 * @param newLOID
	 *            the new loid
	 */
	public void updateQDMTerms(String newLOID) {
		final List<QualityDataSet> qdss = getByListObject(newLOID);
		
		//QualityDataSet's to delete
		final HashMap<String, String> dqdss = new HashMap<>();
		
		for(int i = 0; i < qdss.size()-1; i++){
			final QualityDataSet qds1 = qdss.get(i);
			final QualityDataSet qds2 = qdss.get(i+1);
			final String mid1 = qds1.getMeasureId().getId();
			final String mid2 = qds2.getMeasureId().getId();
			if(mid1.equalsIgnoreCase(mid2)){
				//check dataType, could be multiple dataTypes with the same category
				final String dt1 = qds1.getDataType().getId();
				final String dt2 = qds2.getDataType().getId();
				if(dt1.equalsIgnoreCase(dt2)){
					final String occ1 = qds1.getOccurrence();
					final String occ2 = qds2.getOccurrence();
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
		for(final String key : dqdss.keySet()){
			final String newID = key;
			final String oldID = dqdss.get(key);
			//update the QDMTerm to use the latest QDM
			updateQDMTerm(newID, oldID);
			//delete the duplicate QDM
			deleteOldQDM(oldID);
		}
	}
	
	/**
	 * Update qdm term.
	 * 
	 * @param newID
	 *            the new id
	 * @param oldID
	 *            the old id
	 */
	@SuppressWarnings("rawtypes")
	public void updateQDMTerm(String newID, String oldID){
		final Session session = getSessionFactory().getCurrentSession();
		final String sql = "update QDM_TERM t set t.QDM_ELEMENT_ID = :newID where t.QDM_ELEMENT_ID = :oldID";
		final NativeQuery nativeQuery = session.createNativeQuery(sql);
		nativeQuery.setParameter("newID",newID);
		nativeQuery.setParameter("oldID", oldID);
		nativeQuery.executeUpdate();
	}
	
	/**
	 * Delete old qdm.
	 * 
	 * @param oldID
	 *            the old id
	 */
	@SuppressWarnings("rawtypes")
	public void deleteOldQDM(String oldID){
		final Session session = getSessionFactory().getCurrentSession();
		final String sql = "delete from QUALITY_DATA_MODEL where QUALITY_DATA_MODEL_ID = :oldID";
		final NativeQuery nativeQuery = session.createNativeQuery(sql);
		nativeQuery.setParameter("oldID", oldID);
		nativeQuery.executeUpdate();
	}
	
}
