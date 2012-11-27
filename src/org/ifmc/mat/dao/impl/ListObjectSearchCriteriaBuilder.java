package org.ifmc.mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.shared.ConstantMessages;

class ListObjectSearchCriteriaBuilder {
	private static final Log logger = LogFactory.getLog(ListObjectSearchCriteriaBuilder.class);
	
	private SessionFactory sessionFactory;
	private Class searchClass;
	private String searchText;
	private boolean showDefaultCodeList;
	
	ListObjectSearchCriteriaBuilder(SessionFactory sessionFactory, Class searchClass, String searchText,boolean defaultCodeList) { 
		this.sessionFactory = sessionFactory;
		this.searchClass = searchClass;
		this.searchText = searchText;
		this.showDefaultCodeList = defaultCodeList;
	}
	
	protected List<Criterion> getSearchFields(String text) {
		List<Criterion> retList = new ArrayList<Criterion>();
		retList.add(Restrictions.ilike("id", text));
		retList.add(Restrictions.ilike("name", text));
		retList.add(Restrictions.ilike("oid", text));
		retList.add(Restrictions.ilike("st.orgName", text));
		retList.add(Restrictions.ilike("cl.oid", text));
		retList.add(Restrictions.ilike("gcl.description", text));
		retList.add(Restrictions.ilike("cs.description", text));
		
		return retList;
	}
	protected void addAliases(DetachedCriteria criteria) {
		criteria.createAlias("codeSystem", "cs");
		criteria.createAlias("steward", "st");
		criteria.createAlias("codesLists", "gcl", Criteria.LEFT_JOIN);
		criteria.createAlias("gcl.codeList", "cl", Criteria.LEFT_JOIN);
	}
	
	final DetachedCriteria buildBaseCriteriaForIDs() {
		DetachedCriteria criteria = DetachedCriteria.forClass(searchClass);
		String text = "%" + searchText + "%";
		
		//
		// get the search fields, and or them all together 
		// until we have a single criterion left
		//
		List<Criterion> searchFields = getSearchFields(text);
		while(searchFields.size() > 1) {
			Criterion c1 = searchFields.remove(0);
			Criterion c2 = searchFields.remove(0);
			Criterion result = Restrictions.or(c1, c2);
			searchFields.add(result);
		}
		criteria.add(searchFields.get(0));
		
		
		if(!showDefaultCodeList){
			criteria.add(Restrictions.ne("category.id","22"));
			criteria.add(Restrictions.not(defaultValueSetCriteria()));	
		}
		criteria.add(Restrictions.not(supplementalValueSetCriteria()));

		addAliases(criteria);
		
		criteria.setProjection(Projections.id());
		
		return criteria;
	}

	/*
	 * check for OID inclusion in the set of ready to use list objects 
	 * the value set oids here are:
	 * birthdate, Administrative Gender Male, Administrative Gender Female, Administrative Gender Undifferentiated, 
	 * Measurement Period, Measurement Period Start 
	 * NOTE: Measurement Period End are taken care of by restricting Category '22'
	 */
	public final Criterion defaultValueSetCriteria(){
		Criterion readyToUseCriterion = Restrictions.or
				(Restrictions.eq("oid",ConstantMessages.BIRTH_DATE_OID),
						Restrictions.or(Restrictions.eq("oid",ConstantMessages.GENDER_FEMALE_OID),
								Restrictions.or(Restrictions.eq("oid",ConstantMessages.GENDER_MALE_OID),
										Restrictions.eq("oid",ConstantMessages.GENDER_UNDIFFERENTIATED_OID)
								)
						)
				);
		return readyToUseCriterion;
	}
	/*
	 * check for OID inclusion in the set of ready to use list objects 
	 * the value set oids here are:
	 * Race, Ethnicity, Gender, Payer
	 */
	public final Criterion supplementalValueSetCriteria(){
		Criterion readyToUseCriterion = Restrictions.or(
				Restrictions.eq("oid",ConstantMessages.RACE_OID),
					Restrictions.or(Restrictions.eq("oid",ConstantMessages.ETHNICITY_OID),
							Restrictions.or(Restrictions.eq("oid",ConstantMessages.GENDER_OID),
									Restrictions.eq("oid",ConstantMessages.PAYER_OID)
							)
					)
				);
		return readyToUseCriterion;
	}
	
	/*
	 * Catergory id = 22 means (Measurement Timing) 
	 *
	 */
	public final Criterion defaultCodeListCriteria(){
		Criterion defaultCodeList = Restrictions.eq("category.id","22");
		return defaultCodeList;
	}
	
	public final Criterion addCommonCodeListCriteria(){
		Criterion commonCodeList = Restrictions.or(defaultCodeListCriteria(), defaultValueSetCriteria());
		return commonCodeList;
	}
	
	//Note:- Supplemental VS should never been shown in the CodeList Search Screen and QDM box.(since object owner for supp VS is null, that is taken
	// care for Free.)
	final DetachedCriteria addOwnerCriteria(DetachedCriteria criteria, String userId) {
		if(showDefaultCodeList){
			return criteria.add(Restrictions.or(addCommonCodeListCriteria(),Restrictions.eq("objectOwner.id", userId)));
		}else{
			return criteria.add(Restrictions.eq("objectOwner.id", userId));
		}
	}

	final DetachedCriteria addMeasureCriteria(DetachedCriteria criteria, Measure measure) {
		criteria.createAlias("qualityDataSets", "qds", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq("objectOwner.id", measure.getOwner().getId()));
		
		criteria.add(Restrictions.or(Restrictions.isNull("measureId"),
				Restrictions.or(Restrictions.eq("measureId", measure.getId()),
				Restrictions.eq("qds.measureId", measure))));
		
		return criteria;
	}

}
