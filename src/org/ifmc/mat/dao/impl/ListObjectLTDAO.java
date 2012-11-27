package org.ifmc.mat.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.ifmc.mat.client.codelist.ValueSetSearchFilterPanel;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.ListObjectLT;
import org.ifmc.mat.server.LoggedInUserUtil;
import org.ifmc.mat.shared.ConstantMessages;
import org.ifmc.mat.shared.DateUtility;

/**
 * Lightweight (no codes) ListObject data access data structure
 * @author aschmidt
 *
 */
public class ListObjectLTDAO extends GenericDAO<ListObjectLT, String> 
	implements org.ifmc.mat.dao.ListObjectLTDAO{

	void setDTOValuesFromModel(ListObjectLT cl, CodeListSearchDTO dto) {
		dto.setCategoryCode(cl.getCategory().getId());
		dto.setCategoryDisplay(cl.getCategory().getDescription());
		dto.setAbbreviatedCategory(cl.getCategory().getAbbreviation());
		dto.setId(cl.getId());
		dto.setName(cl.getName());
		dto.setOid(cl.getOid());
		dto.setSteward(cl.getSteward().getOrgName());
		dto.setCodeSystem(cl.getCodeSystem().getDescription());
		dto.setAbbreviatedCodeSystem(cl.getCodeSystem().getAbbreviation());
		dto.setGroupedCodeList(true);
		if(cl.isDraft()){
			dto.setLastModified("Draft");
		}else{
		    dto.setLastModified(DateUtility.convertDateToString(cl.getLastModified()));
		}
		
	}
	final void setPaging(Criteria criteria, int startIndex, int pageSize) {
		criteria.setFirstResult(startIndex);
		if(pageSize > 0) {
			criteria.setMaxResults(pageSize);
		}
	}
	
	final void setSorting(Criteria criteria, String sortColumn, boolean isAsc) {
		if(!isEmpty(sortColumn)) {
			
			if(isAsc) {
				criteria.addOrder(Order.asc(sortColumn));
			}
			else {
				criteria.addOrder(Order.desc(sortColumn));
			}
		
		}
		criteria.createAlias("category", "c");
		criteria.createAlias("codeSystem", "cs");
		criteria.createAlias("steward", "st");
	}	
	
	/**
	 * select MAX(based on lastModified) from L_O where OID in (select OID from L_O where there is no draft)
	 */
	@Override
	public List<ListObjectLT> getListObjectsToDraft() {
		
		String userId = LoggedInUserUtil.getLoggedInUser();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObjectLT.class);
		criteria.add(Restrictions.eq("objectOwner.id", userId));
		criteria.addOrder(Order.desc("oid")).addOrder(Order.desc("draft")).addOrder(Order.desc("lastModified"));
		
		List<ListObjectLT> los = criteria.list();
		
		//what are the OIDs for families who have a draft?
		HashSet<String> draftOids = new HashSet<String>();
		for(ListObjectLT lo: los){
			String oid = lo.getOid();
			if(lo.isDraft())
				draftOids.add(oid);
		}
		
		ArrayList<ListObjectLT> ret = new ArrayList<ListObjectLT>();
		
		String oid = "";
		for(ListObjectLT lo : los){
			String loid = lo.getOid();
			//on new OID encountered
			if(!loid.equalsIgnoreCase(oid)){
				if(!draftOids.contains(lo.getOid()))
						ret.add(lo);
			}
			oid = loid;
		}
		ret = (ArrayList<ListObjectLT>) sortValueSets(ret);
		
		return ret;
	}
	
	/*
	 * ListObjectComparator :- compares listobject within the same measureSet
	 */
	class ListObjectLTComparator implements Comparator<ListObjectLT>{
		@Override
		public int compare(ListObjectLT l1, ListObjectLT l2){
			//1 check whether it is draft
			//2 compare last modified.
			int ret = l1.isDraft() ? -1: l2.isDraft() ? 1 :
				      compareTwoTimeStamp(l1.getLastModified() , l2.getLastModified());
			return ret;
		}
		private int compareTwoTimeStamp(Timestamp t1, Timestamp t2){
			int ret = t1.getTime() < t2.getTime() ? 1 :
				      t1.getTime() == t2.getTime()? 0 :
				      -1;
			return ret; 
		}
	}
	
	/*
	 * ListObjectFamilyComparator:- compares ListObjectFamily by the ListObject Name
	 */
	class ListObjectLTFamilyComparator implements Comparator<List<ListObjectLT>>{
		@Override
		public int compare(List<ListObjectLT> l1, List<ListObjectLT> l2){
			String v1 = l1.get(0).getName();
			String v2 = l2.get(0).getName();
			return v1.compareToIgnoreCase(v2);
		}
	}

	private ArrayList<ListObjectLT> sortValueSets(List<ListObjectLT> valueSetResultList){
		List<List<ListObjectLT>> valueSetLists = new ArrayList<List<ListObjectLT>>(); 
		for(ListObjectLT lo : valueSetResultList){
			boolean hasList = false;
			for(List<ListObjectLT> loList : valueSetLists){
				String loFamilyID = loList.get(0).getOid();
				if(lo.getOid().equalsIgnoreCase(loFamilyID)){
					loList.add(lo);
					hasList = true;
					break;
				}
			}
			if(!hasList){
				List<ListObjectLT> lolist = new ArrayList<ListObjectLT>();
				lolist.add(lo);
				valueSetLists.add(lolist);
			}
		}
	  	//sort
		for(List<ListObjectLT> mlist: valueSetLists){
			//This sort is sorting the list within the family 
			Collections.sort(mlist, new ListObjectLTComparator());
		}
		Collections.sort(valueSetLists, new ListObjectLTFamilyComparator());//This sort is sorting among families.
		//compile list
		ArrayList<ListObjectLT> retList = new ArrayList<ListObjectLT>();
		for(List<ListObjectLT> lolist: valueSetLists){
			for(ListObjectLT lo : lolist){
				retList.add(lo);
			}
		}
		return retList;
	}
	
	
	
	
	private Criteria buildCriteriaForAllValueSets(String searchText,boolean showshowdefaultCodeList) {
		ListObjectSearchCriteriaBuilder loBuilder = 
			new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showshowdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		mainCriteria.add(Subqueries.propertyIn("id", criteria));
		return mainCriteria;
	}
	
	private Criteria buildCriteriaForAppliedByUser(String searchText, String loggedInUserid, boolean showdefaultCodeList) {
		Session session = getSessionFactory().getCurrentSession();
		List<String> loids = session.createQuery("select q.listObject.oid from org.ifmc.mat.model.QualityDataSet q where q.measureId.id in " +
				"(select m.id from org.ifmc.mat.model.clause.Measure m where m.owner.id ='"+loggedInUserid+"')").list();

		if(loids.isEmpty() && !showdefaultCodeList){
			return null;
		}
		ListObjectSearchCriteriaBuilder loBuilder = new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		if(showdefaultCodeList){
			if(loids.isEmpty())
				mainCriteria.add(loBuilder.addCommonCodeListCriteria());
			else 
				mainCriteria.add(Restrictions.or(loBuilder.addCommonCodeListCriteria(), Restrictions.in("oid", loids)));
		}
		else {
			mainCriteria.add(Restrictions.in("oid", loids));
		}
		
		mainCriteria.add(Restrictions.not(Restrictions.in("oid", ConstantMessages.SUPPLEMENTAL_DATA_ELEMENT_OID_ARR)));
		mainCriteria.add(Subqueries.propertyIn("id", criteria));
		return mainCriteria;
	}
	
	private Criteria buildCriteriaForUserCodeLists(String searchText, String mOwneruserId,boolean showshowdefaultCodeList) {
		ListObjectSearchCriteriaBuilder loBuilder = 
			new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showshowdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		criteria = loBuilder.addOwnerCriteria(criteria, mOwneruserId);
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		mainCriteria.add(Subqueries.propertyIn("id", criteria));
		return mainCriteria;
	}
	
	private Criteria getCriteriaForSearchWithFilter(String searchText, String userid, boolean defaultCodeList, int filter){
		Criteria mainCriteria = null;
		if(filter == ValueSetSearchFilterPanel.MY_VALUE_SETS)
			mainCriteria = buildCriteriaForUserCodeLists(searchText, userid, defaultCodeList);
		else if(filter == ValueSetSearchFilterPanel.ALL_VALUE_SETS)
			mainCriteria = buildCriteriaForAllValueSets(searchText, defaultCodeList);
		else if(filter == ValueSetSearchFilterPanel.APPLIED_VALUE_SETS)
			mainCriteria = buildCriteriaForAppliedByUser(searchText, userid, defaultCodeList);
		
		return mainCriteria;
	}
	
	@Override
	public int countSearchResultsWithFilter(String searchText, String userid, boolean defaultCodeList, int filter) {
		
		Criteria mainCriteria = getCriteriaForSearchWithFilter(searchText, userid, defaultCodeList, filter);
		if(mainCriteria == null){
			return 0;
		}
		List<ListObjectLT> lolts = mainCriteria.list();
		int count = lolts.size();
		return count;
	}
	
}
