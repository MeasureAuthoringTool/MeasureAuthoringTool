package org.ifmc.mat.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.ifmc.mat.client.codelist.ManageCodeListDetailModel;
import org.ifmc.mat.client.codelist.ValueSetSearchFilterPanel;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.CodeList;
import org.ifmc.mat.model.CodeListSearchDTO;
import org.ifmc.mat.model.ListObject;
import org.ifmc.mat.model.User;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.server.LoggedInUserUtil;
import org.ifmc.mat.shared.ConstantMessages;
import org.ifmc.mat.shared.DateUtility;

public class ListObjectDAO extends GenericDAO<ListObject, String> 
	implements org.ifmc.mat.dao.ListObjectDAO{

	@Override
	public int countSearchResultsByUser(String searchText, String userId,boolean showdefaultCodeList) {
		Criteria mainCriteria = buildCriteriaForUserCodeLists(searchText, userId, showdefaultCodeList);
		mainCriteria.setProjection(Projections.rowCount());
		return ((Long)mainCriteria.uniqueResult()).intValue();
	}
	
	@Override
	public int countSearchResultsByMeasure(String searchText, Measure measure,
			boolean showdefaultCodeList) {
		Criteria mainCriteria = buildCriteriaForMeasureCodeLists(searchText, measure, showdefaultCodeList);
		mainCriteria.setProjection(Projections.rowCount());
		return ((Long)mainCriteria.uniqueResult()).intValue();
	}

	@Override
	public List<CodeListSearchDTO> searchByUser(String searchText, String measureOwnerUserId, int startIndex,
			int pageSize, String sortColumn, boolean isAsc,boolean showdefaultCodeListAndFilterValues ) {
		
		Criteria mainCriteria = buildCriteriaForUserCodeLists(searchText, measureOwnerUserId, showdefaultCodeListAndFilterValues);
		mainCriteria.setFirstResult(startIndex);
		boolean isSortingbyName = sortColumn.equalsIgnoreCase("Name");
		if(isSortingbyName){
			mainCriteria.addOrder(Order.desc("oid")).addOrder(Order.desc("draft")).addOrder(Order.desc("lastModified"));
		}
		setSorting(mainCriteria, sortColumn, isAsc);
		
		@SuppressWarnings("unchecked")
		List<ListObject> valueSetResultList = mainCriteria.list();
		ArrayList<CodeListSearchDTO> orderedDTOList;
		if(isSortingbyName){
			List<ListObject> valueSetArrayList = sortValueSets(valueSetResultList);
			orderedDTOList = buildResultsDTOList(valueSetArrayList);
		}else{
			orderedDTOList = buildResultsDTOList(valueSetResultList);
		}
		ArrayList<CodeListSearchDTO> orderedFilteredDTOList = new ArrayList<CodeListSearchDTO>();
		
		if(showdefaultCodeListAndFilterValues){
			orderedFilteredDTOList.add(orderedDTOList.get(0));
			CodeListSearchDTO last = orderedDTOList.get(0);
			for(int i=1;i<orderedDTOList.size();i++){
				if(!orderedDTOList.get(i).getOid().equalsIgnoreCase( last.getOid())){
					orderedFilteredDTOList.add(orderedDTOList.get(i));
					last = orderedDTOList.get(i);
				}
			}
		}
		else
			orderedFilteredDTOList=orderedDTOList;
		
		if(pageSize < orderedFilteredDTOList.size())
			return onlyReturnFilteredList(pageSize, orderedFilteredDTOList);//Really RPC doesnot like this orderedDTOList.subList(0,pageSize)??
		else
			return orderedFilteredDTOList;
		
	}
	
	private List<CodeListSearchDTO> onlyReturnFilteredList(int pageSize, ArrayList<CodeListSearchDTO> orderedDTOList){
		List<CodeListSearchDTO> orderedDTOValueSets =  new ArrayList<CodeListSearchDTO>();	
		int counter = 1;
		for(CodeListSearchDTO dto: orderedDTOList){
			if(counter > pageSize){
				break;
			}else{
				counter++;
				orderedDTOValueSets.add(dto);
			}
		}
		return orderedDTOValueSets;
	}
	
	private ArrayList<CodeListSearchDTO> buildResultsDTOList(List<ListObject> queryresults) {
		ArrayList<CodeListSearchDTO> results = new ArrayList<CodeListSearchDTO>();
		for(ListObject listObject : queryresults) {
			CodeListSearchDTO dto = new CodeListSearchDTO();
			setDTOValuesFromModel(listObject, dto);
			if(listObject instanceof CodeList) {
				dto.setGroupedCodeList(false);
			}
			results.add(dto);
		}
		
		return results;
	}
	
	@Override
	public List<ListObject> loadAllCodeListPerUser(String loggedInUser){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("objectOwner.id", loggedInUser));
		return criteria.list();
	}
	
	
	//US 413. Added condition for Steward Other.
	@Override
	public ListObject getListObject(ManageCodeListDetailModel currentDetails, String userid) {
		ListObject lo = find(currentDetails.getID());
		String newOID = currentDetails.getOid();
		//determine family oid to ensure no dups outside of family
		String famOid = lo == null ? newOID :
			lo.getOid();
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("name", currentDetails.getName()));
		criteria.add(Restrictions.eq("steward.id", currentDetails.getSteward()));
		criteria.add(Restrictions.eq("category.id", currentDetails.getCategory()));
		//US 216. This criterion should be codeSystem Version, not codeSystem.
		criteria.add(Restrictions.eq("codeSystemVersion", currentDetails.getCodeSystemVersion()));
		criteria.add(Restrictions.eq("objectOwner.id", userid));
		criteria.add(Restrictions.eq("stewardOther", currentDetails.getStewardOther()));
		criteria.add(Restrictions.ne("oid", famOid));
		//US216, uniqueResult hibernate method was throwing error only in IE. So switched to handle this way.
		if(criteria.list() != null && criteria.list().size() > 0){
			return (ListObject)criteria.list().get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public ListObject getListObject(String name, String steward, String categoryCd, String userid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("steward.id", steward));
		criteria.add(Restrictions.eq("category.id", categoryCd));
		criteria.add(Restrictions.eq("objectOwner.id", userid));
		return (ListObject)criteria.uniqueResult();
	}
	
	public String generateUniqueOid(User user) {
		Session session = getSessionFactory().getCurrentSession();
		List<String> oids = (List<String>)session.createQuery("SELECT oid FROM org.ifmc.mat.model.ListObject").list();
		HashSet<Integer> values = new HashSet<Integer>();
		for(String oid : oids){
			String suffix = oid.substring(oid.lastIndexOf(".")+1);
			if(isInt(suffix))
				values.add(Integer.parseInt(suffix));
		}
		int ret = 1;
		while(values.contains(ret))
			ret++;
		String retStr = user.getRootOID()+"."+ret;
		return retStr;
	}
	private boolean isInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}
	void setDTOValuesFromModel(ListObject cl, CodeListSearchDTO dto) {
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
	
	//US536 value set search refactored to be a simple query.
	private Criteria buildCriteriaForUserCodeLists(String searchText, String mOwneruserId,boolean showshowdefaultCodeList) {
		ListObjectSearchCriteriaBuilder loBuilder = 
			new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showshowdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		criteria = loBuilder.addOwnerCriteria(criteria, mOwneruserId);
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		mainCriteria.add(Subqueries.propertyIn("id", criteria));
		return mainCriteria;
	}
	
	private Criteria buildCriteriaForMeasureOwnerCodeLists(String searchText, String measureOwnerId,boolean showshowdefaultCodeList) {
		ListObjectSearchCriteriaBuilder loBuilder = 
			new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showshowdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		criteria = loBuilder.addOwnerCriteria(criteria, measureOwnerId);
		
		CodeListSearchCriteriaBuilder  cBuilder = new 
			CodeListSearchCriteriaBuilder(getSessionFactory(), CodeList.class, searchText, showshowdefaultCodeList);
		DetachedCriteria criteria2 = cBuilder.buildBaseCriteriaForIDs();
		criteria2 = cBuilder.addOwnerCriteria(criteria2, measureOwnerId);
		
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		mainCriteria.add(Restrictions.or(Subqueries.propertyIn("id", criteria),  
					Subqueries.propertyIn("id", criteria2)));
		return mainCriteria;
	}
	
	private Criteria buildCriteriaForMeasureCodeLists(String searchText, Measure measure, boolean showdefaultCodeList) {
		ListObjectSearchCriteriaBuilder loBuilder = 
			new ListObjectSearchCriteriaBuilder(getSessionFactory(), ListObject.class, searchText, showdefaultCodeList);
		DetachedCriteria criteria = loBuilder.buildBaseCriteriaForIDs();
		criteria = loBuilder.addMeasureCriteria(criteria, measure);
		
		CodeListSearchCriteriaBuilder  cBuilder = new 
			CodeListSearchCriteriaBuilder(getSessionFactory(), CodeList.class, searchText, showdefaultCodeList);
		DetachedCriteria criteria2 = cBuilder.buildBaseCriteriaForIDs();
		criteria2 = cBuilder.addMeasureCriteria(criteria2, measure);
		
		Criteria mainCriteria = getSessionFactory().getCurrentSession().createCriteria(ListObject.class);
		mainCriteria.add(Restrictions.or(Subqueries.propertyIn("id", criteria),  
					Subqueries.propertyIn("id", criteria2)));
		return mainCriteria;
	}

	@Override
	public int countListObjectsByOidAndNotId(String oid, String id) {
		
		ListObject lo = find(id);
		String famOid = lo == null ? null :
			lo.getOid();
		
		/*
		 * only new oids need to be verified
		 * assuming old oids were checked at the time they were set
		 */
		if(oid.equalsIgnoreCase(famOid))
			return 0;
		
		Session session = getSessionFactory().getCurrentSession();
		Long count = (Long) session.createQuery("SELECT COUNT(*) FROM org.ifmc.mat.model.ListObject L WHERE L.oid='"+oid+"' AND NOT L.id='"+id+"'").uniqueResult();
		return count.intValue();
	}
	
	@Override
	public List<ListObject> getListObjectsByMeasure(Measure measure) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("measureId", measure.getId()));
		return criteria.list();
	}
	
	//US547:- This method is used to find the mostRecent Value set  whose lastModifiedDate is less than valueSetPackageDate.
	@Override
	public ListObject findMostRecentValueSet(ListObject loFamily, Timestamp vsPackageDate){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("oid",loFamily.getOid()));//Find all the members of the listObjectFamily.
		criteria.add(Restrictions.le("lastModified", vsPackageDate));
		criteria.addOrder(Order.desc("lastModified"));		
		if(!criteria.list().isEmpty()){
			for(Object obj: criteria.list()){
				if(((ListObject)obj).getLastModified()!=null){
					//Only output valid timestamps
					if(((ListObject)obj).getLastModified().toString().length() >2){
						return (ListObject)obj;
					}
				}
			}
		}
		return null;
	}
	
	
	//US204:- This method is used to find out whether the given listObject family is the mostRecentVS.
	private boolean isMostRecentValueSet(ListObject loFamily){
		if(loFamily.isDraft())
			return true;
		else{
			boolean isDraftExists = false;
			Session session = getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(ListObject.class);
			criteria.add(Restrictions.eq("oid",loFamily.getOid()));//Find all the members of the listObjectFamily.
			criteria.addOrder(Order.desc("lastModified"));
			ListObject mostRecent = (ListObject)criteria.list().get(0);
			for(Object obj: criteria.list()){
				if(((ListObject) obj).isDraft())
					isDraftExists = true; //Draft exist in the family.
			}
			if(!isDraftExists && loFamily.getLastModified().equals(mostRecent.getLastModified()))
				return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.ifmc.mat.dao.ListObjectDAO#searchByMeasureOwner(java.lang.String, java.lang.String, int, int, java.lang.String, boolean, boolean)
	 */
	@Override
	public List<CodeListSearchDTO> searchByMeasureOwner(String searchText,
			String ownerId, int startIndex, int pageSize, String sortColumn,
			boolean isAsc, boolean showdefaultCodeList) {
	   Criteria mainCriteria = buildCriteriaForMeasureOwnerCodeLists(searchText, ownerId, showdefaultCodeList);
		
		setPaging(mainCriteria, startIndex, pageSize);
		setSorting(mainCriteria, sortColumn, isAsc);

		@SuppressWarnings("unchecked")
		List<ListObject> queryresults = mainCriteria.list();
		List<CodeListSearchDTO> results = buildResultsDTOList(queryresults);
		
		return results;
	}
	
	/**
	 * select MAX(based on lastModified) from L_O where OID in (select OID from L_O where there is no draft)
	 */
	@Override
	public List<ListObject> getListObjectsToDraft() {
		
		String userId = LoggedInUserUtil.getLoggedInUser();
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("objectOwner.id", userId));
		criteria.addOrder(Order.desc("oid")).addOrder(Order.desc("draft")).addOrder(Order.desc("lastModified"));
		
		List<ListObject> los = criteria.list();
		
		//what are the OIDs for families who have a draft?
		HashSet<String> draftOids = new HashSet<String>();
		for(ListObject lo: los){
			String oid = lo.getOid();
			if(lo.isDraft())
				draftOids.add(oid);
		}
		
		ArrayList<ListObject> ret = new ArrayList<ListObject>();
		
		String oid = "";
		for(ListObject lo : los){
			String loid = lo.getOid();
			//on new OID encountered
			if(!loid.equalsIgnoreCase(oid)){
				if(!draftOids.contains(lo.getOid()))
						ret.add(lo);
			}
			oid = loid;
		}
		ret = (ArrayList<ListObject>) sortValueSets(ret);
		
		return ret;
	}
	
	/*US552*/
	@Override
	public void updateFamilyOid(String oldOID, String newOID) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("update LIST_OBJECT lo set lo.oid = '"+newOID+"' where lo.oid = '"+oldOID+"';");
		int ret = query.executeUpdate();
	}
	
	
	//US536
	/*
	 * ListObjectComparator :- compares listobject within the same measureSet
	 */
	class ListObjectComparator implements Comparator<ListObject>{
		@Override
		public int compare(ListObject l1, ListObject l2){
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
	//US536
	/*
	 * ListObjectFamilyComparator:- compares ListObjectFamily by the ListObject Name
	 */
	class ListObjectFamilyComparator implements Comparator<List<ListObject>>{
		@Override
		public int compare(List<ListObject> l1, List<ListObject> l2){
			String v1 = l1.get(0).getName();
			String v2 = l2.get(0).getName();
			return v1.compareToIgnoreCase(v2);
		}
	}
	//US536
	private ArrayList<ListObject> sortValueSets(List<ListObject> valueSetResultList){
		List<List<ListObject>> valueSetLists = new ArrayList<List<ListObject>>(); 
		for(ListObject lo : valueSetResultList){
			boolean hasList = false;
			for(List<ListObject> loList : valueSetLists){
				String loFamilyID = loList.get(0).getOid();
				if(lo.getOid().equalsIgnoreCase(loFamilyID)){
					loList.add(lo);
					hasList = true;
					break;
				}
			}
			if(!hasList){
				List<ListObject> lolist = new ArrayList<ListObject>();
				lolist.add(lo);
				valueSetLists.add(lolist);
			}
		}
	  	//sort
		for(List<ListObject> mlist: valueSetLists){
			//This sort is sorting the list within the family 
			Collections.sort(mlist, new ListObjectComparator());
		}
		Collections.sort(valueSetLists, new ListObjectFamilyComparator());//This sort is sorting among families.
		//compile list
		ArrayList<ListObject> retList = new ArrayList<ListObject>();
		for(List<ListObject> lolist: valueSetLists){
			for(ListObject lo : lolist){
				retList.add(lo);
			}
		}
		return retList;
	}

	@Override
	public boolean hasDraft(String oid) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("oid", oid));
		criteria.add(Restrictions.eq("draft", true));
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.uniqueResult()).intValue() > 0;
	}
	
	@Override
	public List<ListObject> getListObject(ManageCodeListDetailModel currentDetails, Timestamp ts) {
		ListObject lo = find(currentDetails.getID());
		String newOID = currentDetails.getOid();
		String famOid = lo == null ? newOID :
			lo.getOid();		
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.ne("id", currentDetails.getID()));
		criteria.add(Restrictions.eq("oid", famOid));
		criteria.add(Restrictions.eq("lastModified", ts));
		List<ListObject> los = criteria.list();
		return los;
	}
	
	@Override
	public List<ListObject> getSupplimentalCodeList(){
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("codeListContext","Supplimental CodeList"));
		List<ListObject> suppElementList = criteria.list();
		return suppElementList;
	}
	
	@Override
	public boolean isMyValueSet(String id, String ownerId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ListObject.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("objectOwner.id", ownerId));
		if(criteria.list() != null && criteria.list().size() > 0)
			return true;
		else 
			return false;
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
	
	private Criteria getCriteriaForSearchWithFilter(String searchText, String loggedInUserid, int startIndex, 
			int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter){
		Criteria mainCriteria = null;
		if(filter == ValueSetSearchFilterPanel.MY_VALUE_SETS)
			mainCriteria = buildCriteriaForUserCodeLists(searchText, loggedInUserid, defaultCodeList);
		else if(filter == ValueSetSearchFilterPanel.ALL_VALUE_SETS)
			mainCriteria = buildCriteriaForAllValueSets(searchText, defaultCodeList);
		else if(filter == ValueSetSearchFilterPanel.APPLIED_VALUE_SETS)
			mainCriteria = buildCriteriaForAppliedByUser(searchText, loggedInUserid, defaultCodeList);
		
		if(mainCriteria == null){
			return null;
		}
		
		mainCriteria.setFirstResult(startIndex);
		boolean isSortingbyName = sortColumn.equalsIgnoreCase("Name");
		if(isSortingbyName){
			mainCriteria.addOrder(Order.desc("oid")).addOrder(Order.desc("draft")).addOrder(Order.desc("lastModified"));
		}
		setSorting(mainCriteria, sortColumn, isAsc);
		return mainCriteria;
	}
	
	@Override
	public List<CodeListSearchDTO> searchWithFilter(String searchText, String loggedInUserid, int startIndex, 
			int pageSize, String sortColumn, boolean isAsc, boolean defaultCodeList, int filter) {
		
		ArrayList<CodeListSearchDTO> orderedFilteredDTOList = new ArrayList<CodeListSearchDTO>();
		Criteria mainCriteria = getCriteriaForSearchWithFilter(searchText, loggedInUserid, startIndex, pageSize, sortColumn, isAsc, defaultCodeList, filter);
		
		if(mainCriteria == null){
			return orderedFilteredDTOList;
		}
		
		boolean isSortingbyName = sortColumn.equalsIgnoreCase("Name");
		@SuppressWarnings("unchecked")
		List<ListObject> valueSetResultList = mainCriteria.list();
		
		//US 204
		if(defaultCodeList && !searchText.isEmpty()){
			Iterator<ListObject> litr = valueSetResultList.iterator(); 
			while(litr.hasNext()) {
				if(!isMostRecentValueSet(litr.next())){
					//then remove it from the search results.
					litr.remove();
				}
			}
		}
		
		
		ArrayList<CodeListSearchDTO> orderedDTOList;
		if(isSortingbyName){
			List<ListObject> valueSetArrayList = sortValueSets(valueSetResultList);
			orderedDTOList = buildResultsDTOList(valueSetArrayList);
		}else{
			orderedDTOList = buildResultsDTOList(valueSetResultList);
		}
		
	
		if(defaultCodeList && !orderedDTOList.isEmpty()){
			orderedFilteredDTOList.add(orderedDTOList.get(0));
			CodeListSearchDTO last = orderedDTOList.get(0);
			for(int i=1;i<orderedDTOList.size();i++){
				if(!orderedDTOList.get(i).getOid().equalsIgnoreCase( last.getOid())){//This is the place where we are holding the draft or the most Recent one in the list
					orderedFilteredDTOList.add(orderedDTOList.get(i));
					last = orderedDTOList.get(i);
				}
			}
		}
		else{
			orderedFilteredDTOList=orderedDTOList;
		}
		
		if(pageSize < orderedFilteredDTOList.size())
			return onlyReturnFilteredList(pageSize, orderedFilteredDTOList);//Really RPC doesnot like this orderedDTOList.subList(0,pageSize)??
		else
			return orderedFilteredDTOList;
	}
	
	@Override
	public String generateUniqueName(String name, User u){
		String suffix = " Clone";
		name = name.trim();
		int idx = name.lastIndexOf(" ");
		if(idx > 0){
			String tempName = name.substring(0, idx);
			String tempNum = name.substring(idx+1);
			
			boolean endsWithInt = false;
			try{
				Integer.parseInt(tempNum);
				endsWithInt = true;
			}catch(NumberFormatException nfe){}
			
			boolean endsWithClone = tempName.endsWith(suffix);
			
			if(endsWithClone && endsWithInt){
				name = tempName.substring(0, tempName.length()-suffix.length());
			}
		}
			
		int i = 1;
		name=name+suffix+" ";
		String newName = name+i;
		
		while(newName.length() <= 255 && !isUniqueName(newName, u)){
			newName = name+i;
			i++;
		}
		if(newName.length() > 255)
			return null;
		else
			return newName;
	}
	
	private boolean isUniqueName(String name, User u){
		Session session = getSessionFactory().openSession();
		String sql = "select count(*) from org.ifmc.mat.model.ListObject where objectOwner.id='"+u.getId()+"' and name='"+name+"'";
		Query query = session.createQuery(sql);
		List<Long> list  = query.list();
		session.close();
		return list.get(0) == 0;
	}
	
}
