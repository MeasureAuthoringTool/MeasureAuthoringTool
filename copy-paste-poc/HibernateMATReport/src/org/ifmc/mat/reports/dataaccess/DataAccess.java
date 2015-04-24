package mat.reports.dataaccess;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import mat.report.common.CommonConstants;
import mat.reportmodel.Category;
import mat.reportmodel.Clause;
import mat.reportmodel.CodeListAuditLog;
import mat.reportmodel.DataType;
import mat.reportmodel.Decision;
import mat.reportmodel.ListObject;
import mat.reportmodel.Measure;
import mat.reportmodel.MeasureAuditLog;
import mat.reportmodel.Metadata;
import mat.reportmodel.QDMTerm;
import mat.reportmodel.QDSAttributes;
import mat.reportmodel.QualityDataSet;
import mat.reportmodel.SecurityRole;
import mat.reportmodel.User;

public class DataAccess {
	Session session = null;
	private String dbConnectionURL ="";
	private String userName = "";
	private String password = "";
	
	private  HashMap<String,ArrayList<Decision>> mapofChildren = new HashMap<String,ArrayList<Decision>>();
	
	private ArrayList<Decision> childrenofQDSTerm = new ArrayList<Decision>();
	
	private void  initHibernateConfiguration(){
		 Configuration configuration = new Configuration();
		 configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		 configuration.setProperty("hibernate.connection.url", dbConnectionURL);
		 configuration.setProperty("hibernate.connection.username", userName);
		 configuration.setProperty("hibernate.connection.password",  password);
		 configuration.setProperty("hibernate.connection.pool_size", "10");
		 configuration.setProperty("hibernate.connection.pool_size", "10");
		 configuration.setProperty("hibernate.cache.provider_class","org.hibernate.cache.EhCacheProvider");
		 configuration.setProperty("hibernate.cache.use_query_cache", "true");
		 configuration.setProperty("hibernate.cache.use_second_level_cache", "true");
		 configuration.setProperty("show_sql", "true");
		 configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		 //Adding mapping files
		 configuration.addResource("reporthibernate/DataType.hbm.xml");
		 configuration.addResource("reporthibernate/Category.hbm.xml");
		 configuration.addResource("reporthibernate/Decision.hbm.xml");
		 configuration.addResource("reporthibernate/Clause.hbm.xml");
		 configuration.addResource("reporthibernate/Measure.hbm.xml");
		 configuration.addResource("reporthibernate/MeasureAuditLog.hbm.xml");
		 configuration.addResource("reporthibernate/Metadata.hbm.xml");
		 configuration.addResource("reporthibernate/QDMAttributes.hbm.xml");
		 configuration.addResource("reporthibernate/QDMTerm.hbm.xml");
		 configuration.addResource("reporthibernate/QualityDataModel.hbm.xml");
		 configuration.addResource("reporthibernate/ListObject.hbm.xml");
		 configuration.addResource("reporthibernate/User.hbm.xml");
		 configuration.addResource("reporthibernate/SecurityRole.hbm.xml");
		 configuration.addResource("reporthibernate/CodeListAuditLog.hbm.xml");
		 SessionFactory sessionFactory =  configuration.buildSessionFactory();
		 session = sessionFactory.openSession();
	}
	
	public void setDatabaseCreds(String jdbcURL, String userName, String password){
		this.dbConnectionURL = jdbcURL;
		this.userName = userName;
		this.password = password;
		initHibernateConfiguration();
	}
	
	public DataAccess(){
		
	}
	
	public LinkedHashMap<String, Object> retrieveQDMElements(String startDateStr, String endDateStr){
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		try{
		 LinkedHashMap<String,LinkedHashMap<String,Object>> excelKeys =  loadPossibeCatDatAttrCombination();
		 System.out.println("Done loading excelKeys !");
		 Criteria logCriteria = session.createCriteria(MeasureAuditLog.class);
		 logCriteria.add(Restrictions.eq("activityType", "Measure Package Created"));
		 logCriteria.add(Restrictions.sqlRestriction("DATE(timestamp) between DATE('" + startDateStr + "') and DATE('" + endDateStr + "')"));
		 List<MeasureAuditLog> results = (List<MeasureAuditLog>)logCriteria.list();
		 LinkedHashMap<String, Object> msrAndQDMMatrix = new LinkedHashMap<String, Object>();
		 System.out.println("Measures that are packaged during the given time period are:-" + results.size());
		 for(mat.reportmodel.MeasureAuditLog auditLog: results){
			 ArrayList<String> QDMKeysList = new ArrayList<String>();
			 String measure_desc = auditLog.getMeasure().getDescription();
			 String measure_id = auditLog.getMeasure().getId();
			 
			 
			 Criteria clauseCriteria = session.createCriteria(mat.reportmodel.Clause.class);
			
			 clauseCriteria.add(Restrictions.eq("measureId",measure_id));
			 clauseCriteria.add(Restrictions.ne("contextId", "11"));
			 List<mat.reportmodel.Clause> clauses = clauseCriteria.list();
			
			 for(mat.reportmodel.Clause cl : clauses){
				
				 ArrayList<Decision> listofKidsAttachedwithTopLevelAnds =  getChildrenofAND(cl.getDecision().getId());
				 for(Decision attachedDecision: listofKidsAttachedwithTopLevelAnds){
					 if(attachedDecision != null && attachedDecision.getClauseId()!= null){
						 Clause measurePhraseClause = findOutMeasurePhraseClause(attachedDecision.getClauseId());
						 HashMap<String,ArrayList<Decision>> mapOfChildren  = processMeasurePhraseDecision(measurePhraseClause.getDecision().getId());
						 for(String measurePhraseDecisionRoot: mapOfChildren.keySet()){
							ArrayList<Decision> childrenofADecision = mapOfChildren.get(measurePhraseDecisionRoot);
							for(Decision da: childrenofADecision){
								String key = "";
								if(da.getOperator().equalsIgnoreCase("QDSTERM")){
									key = processQDMInformation(da.getId(),measure_id);
									String qdm_decision_id = da.getId();
									childrenofQDSTerm.clear(); //Clear this list for each qdsTerm
									ArrayList<Decision> QDSAttributeList = getChildrenOfQDSTerm(qdm_decision_id);
									for(Decision qdsAttributeDec : QDSAttributeList){
										//key = key +"/" + qdsAttributeDec.getAttributeId();//Adding the attribute combination to the cat-dat/attr.
										QDMKeysList.add(key + "/" +qdsAttributeDec.getAttributeId());
									}
									if( QDSAttributeList.isEmpty()){
										QDMKeysList.add(key);
									}
									
								}//end of QDSTerm if
							}//End of childrenofADecision
						 }//End of MeasurePhraseDecisionRoot
					 }//End of if attachedDecision != null
				 }//End of attachedDecision
			 }//End of clause 
			 msrAndQDMMatrix.put(measure_desc, QDMKeysList) ;
			 response.put("qdmList", excelKeys);
			 response.put("msrList",msrAndQDMMatrix);
		 }
	}catch(Exception e){
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	return response;
}



private  ArrayList<Decision> getChildrenofAND(String decision_id){
	    ArrayList<Decision> listofChildren = new ArrayList<Decision>();
		do{
			Criteria decisionCriteria = session.createCriteria(mat.reportmodel.Decision.class);
			decisionCriteria.add(Restrictions.eq("parentId", decision_id));
			List<mat.reportmodel.Decision> decisionsList = decisionCriteria.list();
			for(Decision d : decisionsList){
				if(d != null){
					decision_id = d.getId();
			    if(!d.getOperator().equalsIgnoreCase("AND"))
			    	listofChildren.add(d);
				}
			}
			if(decisionsList.isEmpty()){
				decision_id = null;
			}
		}while(decision_id != null && !decision_id.equalsIgnoreCase(""));
	
	return listofChildren;
}

private  HashMap<String, ArrayList<Decision>> getChildrenofMeasurePhrase(String decision_id){
	    ArrayList<Decision> listofChildren  = new ArrayList<Decision>();
		Criteria decisionCriteria = session.createCriteria(mat.reportmodel.Decision.class);
		decisionCriteria.add(Restrictions.eq("parentId", decision_id));
		List<mat.reportmodel.Decision> decisionsList = decisionCriteria.list();
		listofChildren.addAll(decisionsList);
		mapofChildren.put(decision_id, listofChildren);
		for(Decision d : decisionsList){
			if(d != null){
				decision_id = d.getId();
				listofChildren.add(d);
				//don't need to process children of QDS_Term.
				if(!d.getOperator().equalsIgnoreCase("QDSTERM"))
					getChildrenofMeasurePhrase(decision_id);
		    }
		}
		return mapofChildren;
}

private  ArrayList<Decision> getChildrenOfQDSTerm(String qdm_dec_id){
	      	Criteria decisionCriteria = session.createCriteria(mat.reportmodel.Decision.class);
			decisionCriteria.add(Restrictions.eq("parentId", qdm_dec_id));
			List<mat.reportmodel.Decision> decisionsList = decisionCriteria.list();
			for(Decision d : decisionsList){
				if(d != null){
					qdm_dec_id = d.getId();
					if(d.getOperator().equalsIgnoreCase("QDSATTRIBUTE")){
						childrenofQDSTerm.add(d);
					}
					getChildrenOfQDSTerm(qdm_dec_id);
				}
			}
	return childrenofQDSTerm;
}

private Clause findOutMeasurePhraseClause(String measurePhraseClauseId){
	Criteria measurePhraseClauseCriteria = session.createCriteria(mat.reportmodel.Clause.class);
	Clause measurePhraseClause = (Clause) measurePhraseClauseCriteria.add(Restrictions.eq("id", measurePhraseClauseId)).list().get(0);
	return measurePhraseClause;
}

private HashMap<String, ArrayList<Decision>> processMeasurePhraseDecision(String decision_id){
	mapofChildren.clear();//clear this map for each Measure Phrase
	return getChildrenofMeasurePhrase(decision_id);
}



private String processQDMInformation(String decId, String measure_id){
	String key = null;
	Criteria QDMTermCriteria = session.createCriteria(mat.reportmodel.QDMTerm.class);
	List<QDMTerm> listOfQDMTerm = QDMTermCriteria.add(Restrictions.eq("decisionId", decId)).list();
	for(QDMTerm qdmTerm : listOfQDMTerm){
		Criteria QualtiyDataModelCriteria = session.createCriteria(mat.reportmodel.QualityDataSet.class);
		QualtiyDataModelCriteria.add(Restrictions.eq("id", qdmTerm.getqDSRef()));
		QualtiyDataModelCriteria.add(Restrictions.eq("measureId.id",  measure_id));
		List<QualityDataSet> qdsList = QualtiyDataModelCriteria.list();
		for(QualityDataSet qds: qdsList){
			if(qds != null){
				 key = qds.getListObject().getCategory().getId()+ "-" + qds.getDataType().getId();
			}
		}
	}
	return key;
}


private LinkedHashMap<String,LinkedHashMap<String,Object>>  loadPossibeCatDatAttrCombination(){
	LinkedHashMap<String,LinkedHashMap<String,Object>> excelKeys = new LinkedHashMap<String,LinkedHashMap<String,Object>>();
	Criteria categoryCriteria = session.createCriteria(mat.reportmodel.Category.class);
	categoryCriteria.add(Restrictions.ne("description", "Attribute"));
    List<Category> categoryList = categoryCriteria.list();
    String rootkey = null;
    for(Category cat : categoryList){
    	rootkey = cat.getId();
    	System.out.println("Category id:-"+ cat.getId() + "-" + cat.getDescription());
    	Set<DataType> dataTypes = cat.getDataTypes();
    	System.out.println("set of datatypes"+ dataTypes);
    	for(DataType dt : dataTypes){
    		 rootkey = cat.getId()+ "-" + dt.getId();
    		 Criteria QualityDataAttributes = session.createCriteria(mat.reportmodel.QDSAttributes.class);
			 QualityDataAttributes.add(Restrictions.eq("dataTypeId", dt.getId()));
			 List<QDSAttributes> qdsAttributeList = QualityDataAttributes.list();
			 LinkedHashMap<String,Object> qdsAttributeNameMap = new LinkedHashMap<String,Object>();
			 qdsAttributeNameMap.put(CommonConstants.CATEGORY , cat.getDescription());
			 qdsAttributeNameMap.put(CommonConstants.DATATYPE , dt.getDescription());
			 qdsAttributeNameMap.put(CommonConstants.ATTRIBUTE,"No Attribute Association");
			 
			 excelKeys.put(rootkey  , qdsAttributeNameMap);
			
			 for(QDSAttributes qdsAttr: qdsAttributeList){
				 qdsAttributeNameMap = new LinkedHashMap<String,Object>();
				 qdsAttributeNameMap.put(CommonConstants.CATEGORY , cat.getDescription());
				 qdsAttributeNameMap.put(CommonConstants.DATATYPE , dt.getDescription());
				 qdsAttributeNameMap.put(CommonConstants.ATTRIBUTE , qdsAttr.getName());
				 excelKeys.put(rootkey + "/" + qdsAttr.getId(), qdsAttributeNameMap);
			 }
			 Criteria QualityDataFlowAttributes = session.createCriteria(mat.reportmodel.QDSAttributes.class);
			 QualityDataFlowAttributes.add(Restrictions.eq("qDSAttributeType", "Data Flow"));
			 List<QDSAttributes> dataFlowAttributeList = QualityDataFlowAttributes.list();
			 for(QDSAttributes dataFlowAttributes : dataFlowAttributeList){
				 qdsAttributeNameMap = new LinkedHashMap<String,Object>();
				 qdsAttributeNameMap.put(CommonConstants.CATEGORY , cat.getDescription());
				 qdsAttributeNameMap.put(CommonConstants.DATATYPE , dt.getDescription());
				 qdsAttributeNameMap.put(CommonConstants.ATTRIBUTE,dataFlowAttributes.getName());
				 excelKeys.put(rootkey + "/" + dataFlowAttributes.getId(), qdsAttributeNameMap);
			 }
    	}
    }
    return excelKeys;
}

public HashMap<String, Object> runSummaryOfTrendsOverTimeReport(String startDate, String endDate){
	
	HashMap<String, Object> results = new HashMap<String, Object>();
	int measuresComplete =0;
	int newUserAccounts =0;
	Set<String> orgOIDs = new HashSet<String>();
	
	//Set<String> completeMeasures = new HashSet<String>();
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	try{
		Date sDate = df.parse(startDate);
		Date eDate = df.parse(endDate);
		////init();
		
		//String sql = "SELECT ACTIVATION_DATE, ORG_OID FROM  USER"; 
		//Statement st = connect.createStatement();
		//ResultSet mResultSet = st.executeQuery(sql);
				
		Criteria categoryCriteria = session.createCriteria(mat.reportmodel.User.class);
		
		categoryCriteria.add(Restrictions.sqlRestriction("DATE(ACTIVATION_DATE) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
	
		List<User> userList = categoryCriteria.list();
		
		
		//String sql = "SELECT ORG_OID FROM USER us where cast(us.activation_date as Date) between ? and ?";
		//mPreparedStmt = connect.prepareStatement(sql);
		//mPreparedStmt.setString(1, startDate);
		//mPreparedStmt.setString(2, endDate);
		//ResultSet mResultSet = mPreparedStmt.executeQuery();
		
		newUserAccounts = userList.size();
		
		categoryCriteria = session.createCriteria(mat.reportmodel.User.class);
		userList = categoryCriteria.list();
		for( User u: userList){
			if(u.getActivationDate().after(eDate))
				continue;
			orgOIDs.add(u.getOrgOID());
		}
		
		//Count  complete and created during this time period?
		//sql = "SELECT * FROM  MEASURE WHERE MEASURE_STATUS ='Complete'";
		//Statement st = connect.createStatement();
		//ResultSet metaResultSet = st.executeQuery(sql);
		
		categoryCriteria = session.createCriteria(mat.reportmodel.Measure.class);
		categoryCriteria.add(Expression.eq("measureStatus", "Complete"));
		
		
		List<Measure> measureList = categoryCriteria.list();
		
		measuresComplete = measureList.size();
		
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		session.flush();
		session.close();
	}
	//catch(ParseException e){
	//	e.printStackTrace();
	//}
	
	results.put("NEW_USER_ACCOUNTS", Integer.toString(newUserAccounts));
	results.put("MEASURES_COMPLETE", Integer.toString(measuresComplete));
	results.put("ORG_OIDS", Integer.toString(orgOIDs.size()));
	results.put("DATE_STRING", startDate+" to "+ endDate);
	
	return results;
}




public HashMap<String, HashMap<String, Object>> runSummaryOfAccountsReport(String startDate, String endDate){


	HashMap<String, HashMap<String, Object>> accountsList = new HashMap<String, HashMap<String, Object>>();
	String email;
	String userHash;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Date endDateAsDate;
	Date startDateAsDate;
	
	
	
	HashMap<String,String> securityRole = new HashMap<String,String>();
	
	HashMap<String,String> userEmailMap = new HashMap<String,String>();
	

	//HashMap<String,String> measureAuditEmailMap = new HashMap<String,String>();
	//HashMap<String,String> codeListAuditAuditEmailMap = new HashMap<String,String>();
	
	HashMap<String,String> measureOwnerMap = new HashMap<String,String>();
	
	//HashMap<String,String> codeListOwnerMap = new HashMap<String,String>();
	
	String roleID;
	String description;
	
	try{
		
		endDateAsDate =df.parse(endDate);
		startDateAsDate = df.parse(startDate);
		
		
		//init();

		//String sql = "SELECT * FROM  SECURITY_ROLE";
		//Statement st = connect.createStatement();
		//ResultSet mResultSet = st.executeQuery(sql);
		
		Criteria securityCriteria = session.createCriteria(mat.reportmodel.SecurityRole.class);
		
	
		List<SecurityRole> securityList = securityCriteria.list();
		
		
		for(SecurityRole role : securityList){
			roleID= role.getId();
			description = role.getDescription();
			if(description.equalsIgnoreCase("Super user")){
				description = "Top level User";
			}
			securityRole.put(roleID, description);
		}
		
		
		//while(mResultSet.next()){
		//	roleID=  mResultSet.getString("SECURITY_ROLE_ID");
		//	description=  mResultSet.getString("DESCRIPTION");
		//	if(description.equalsIgnoreCase("Super user")){
		//		description = "Top level User";
		//	}
		//	securityRole.put(roleID, description);
		//}
		
		Criteria userCriteria = session.createCriteria(mat.reportmodel.User.class);
		List<User> userList = userCriteria.list();
		
		
		// sql = "SELECT * FROM  USER";
		// st = connect.createStatement();
		//	 mResultSet = st.executeQuery(sql);
		
		for(User u : userList){
			if(u.getActivationDate().after(endDateAsDate)){
				continue;
			}
			
			email = u.getEmailAddress();
			HashMap<String, Object> mRow = new HashMap<String, Object>();
			mRow.put("EMAIL_ADDRESS", u.getEmailAddress());
			mRow.put("FIRST_NAME", u.getFirstName());
			mRow.put("LAST_NAME",u.getLastName());
			mRow.put("ORGANIZATION_NAME", u.getOrganizationName());
			mRow.put("ORG_OID", u.getOrgOID());
			mRow.put("ACTIVATION_DATE", u.getActivationDate());
			mRow.put("SIGN_IN_DATE", u.getSignInDate());
			roleID = u.getSecurityRole().getId();
			mRow.put("SECURITY_ROLE_ID", securityRole.get(roleID)); 
			mRow.put("MEASURES_CREATED", 0);
			mRow.put("VALUE_SETS_CREATED", 0);
			userHash = u.getId();
			accountsList.put(userHash,mRow);
			
			userEmailMap.put(userHash ,email);
		}
		
//		while(mResultSet.next()){
//			email = mResultSet.getString("EMAIL_ADDRESS");
//			HashMap<String, Object> mRow = new HashMap<String, Object>();
//			mRow.put("EMAIL_ADDRESS", mResultSet.getString("EMAIL_ADDRESS"));
//			mRow.put("FIRST_NAME", mResultSet.getString("FIRST_NAME"));
//			mRow.put("LAST_NAME",mResultSet.getString("LAST_NAME"));
//			mRow.put("ORGANIZATION_NAME", mResultSet.getString("ORGANIZATION_NAME"));
//			mRow.put("ORG_OID", mResultSet.getString("ORG_OID"));
//
//			mRow.put("ACTIVATION_DATE", mResultSet.getString("ACTIVATION_DATE"));
//			mRow.put("SIGN_IN_DATE", mResultSet.getString("SIGN_IN_DATE"));
//			roleID = mResultSet.getString("SECURITY_ROLE_ID");
//			mRow.put("SECURITY_ROLE_ID", securityRole.get(roleID)); 
//			mRow.put("MEASURES_CREATED", 0);
//			mRow.put("VALUE_SETS_CREATED", 0);
//			accountsList.put(email,mRow);
//			userHash = mResultSet.getString("USER_ID");
//			userEmailMap.put(userHash ,email);
//			//emailOwnerMap.put(email, userHash);
//		}
		//Measures created

		Criteria measureCriteria = session.createCriteria(mat.reportmodel.Measure.class);
		List<Measure> measureList = measureCriteria.list();
		
		
		
		//sql = "Select m.id, m.measure_owner_id from MEASURE m";
		//mPreparedStmt = connect.prepareStatement(sql);
		//mPreparedStmt.setString(1, startDate);
		//mPreparedStmt.setString(2, endDate);
		//ResultSet metaResultSet = mPreparedStmt.executeQuery();
		
		for(Measure m : measureList){
			measureOwnerMap.put(m.getId(), m.getOwner().getId());
		}
		
	//	while(metaResultSet.next()){
	//		measureOwnerMap.put(metaResultSet.getString("ID"), 
	//				metaResultSet.getString("MEASURE_OWNER_ID"));
	//	}
		
		
		
		
		Criteria auditLogCriteria = session.createCriteria(mat.reportmodel.MeasureAuditLog.class);
		
		auditLogCriteria.add(Restrictions.sqlRestriction("DATE(TIMESTAMP) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
		auditLogCriteria.add(Restrictions.eq("activityType", "Measure Created"));
		
		//DB seems to have entries in the MeasureAuditLog with no corresponding
		//entries in the Measure table.
		auditLogCriteria.add(Restrictions.sqlRestriction("MEASURE_ID IN (SELECT ID FROM   MEASURE)"));
		
		
			
		//sql = "Select m.id, m.scoring, m.measure_owner_id from MEASURE m, MEASURE_AUDIT_LOG ml " +
		//"where cast(ml.timestamp as Date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Created';";

		
		List<MeasureAuditLog> auditLogList = auditLogCriteria.list();
		
		
		//GET CREATED MEASURE CODES
		
		//mPreparedStmt = connect.prepareStatement(sql);
		//mPreparedStmt.setString(1, startDate);
		//mPreparedStmt.setString(2, endDate);
		//metaResultSet = mPreparedStmt.executeQuery();
		
		for(MeasureAuditLog mal : auditLogList){
			Measure m = mal.getMeasure();
			userHash  = m.getOwner().getId();
			if(accountsList.get(userHash)!=null){
				Integer count =(Integer)accountsList.get(userHash).get("MEASURES_CREATED");
				accountsList.get(userHash).put("MEASURES_CREATED", count+1);	
			}
			else{
				User u = m.getOwner();
				if(u.getActivationDate().after(endDateAsDate)){
					continue;
				}
				HashMap<String, Object> mRow = new HashMap<String, Object>();
				mRow.put("EMAIL_ADDRESS", u.getEmailAddress());
				mRow.put("FIRST_NAME", u.getFirstName());
				mRow.put("LAST_NAME",u.getLastName());
				mRow.put("ORGANIZATION_NAME", u.getOrganizationName());
				mRow.put("ORG_OID", u.getOrgOID());
				mRow.put("ACTIVATION_DATE", u.getActivationDate());
				mRow.put("SIGN_IN_DATE", u.getSignInDate());
				roleID = u.getSecurityRole().getId();
				mRow.put("SECURITY_ROLE_ID", securityRole.get(roleID)); 
				mRow.put("MEASURES_CREATED", 1);
				mRow.put("VALUE_SETS_CREATED", 0);
				userHash = u.getId();
				accountsList.put(userHash,mRow);	
			}
		}
		

		
		
		
//		while(metaResultSet.next()){
//				
//				//Do the join... then iterate over measure table user IDs to pull them directly from the audit log
//				//thus disregarding stale email addresses in the measure audit log table
//				//measureAuditEmailMap.put(metaResultSet.getString("MEASURE_ID"), metaResultSet.getString("USER_ID"));
//				String s = metaResultSet.getString("MEASURE_OWNER_ID");
//				//if(s ==null)
//				//	continue;
//				email =  userEmailMap.get(s);
//				//metaResultSet.getString("USER_ID");
//				if(accountsList.get(email)!=null){
//					//String timeStamp = metaResultSet.getString("TIMESTAMP");
//					//if(isInterval(timeStamp, startDate, endDate)){
//						Integer count =(Integer)accountsList.get(email).get("MEASURES_CREATED");
//						accountsList.get(email).put("MEASURES_CREATED", count+1);
//					//}
//				}
//		}

		
		Criteria codeListAuditCriteria = session.createCriteria(mat.reportmodel.CodeListAuditLog.class);
		
		
		//codeListAuditCriteria.add(Restrictions.eq("activityType", "Code List Created"));
		//codeListAuditCriteria.add(Restrictions.eq("activityType","Draft Value Set Created"));
		//codeListAuditCriteria.add(Restrictions.eq("activityType","Grouped Code List Created"));	
		codeListAuditCriteria.add(Restrictions.sqlRestriction("DATE(timestamp) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
		
		
		Criterion cOne = Restrictions.eq("activityType", "Code List Created");
		Criterion cTwo = Restrictions.eq("activityType", "Value Set Created");
		Criterion cThree = Restrictions.eq("activityType", "Draft Value Set Created");
		Criterion cFour = Restrictions.eq("activityType", "Grouped Value Set Created");
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(cOne);
		disjunction.add(cTwo);
		disjunction.add(cThree);
		disjunction.add(cFour);
		codeListAuditCriteria.add(disjunction); 
		
		codeListAuditCriteria.add(Restrictions.sqlRestriction("CODE_LIST_ID IN (SELECT LIST_OBJECT_ID FROM LIST_OBJECT)"));
		
		
		List<CodeListAuditLog> codeListAuditList = codeListAuditCriteria.list();
		
		Set<String> uniqueCodeLists = new HashSet<String>();
		for(CodeListAuditLog cal: codeListAuditList){
			uniqueCodeLists.add(cal.getCodeList().getId());
		}
		
		
		Criteria listObjectCriteria = session.createCriteria(mat.reportmodel.ListObject.class);
		disjunction = Restrictions.disjunction();
		for(String u : uniqueCodeLists){
			disjunction.add(Restrictions.eq("id", u));
		}
		listObjectCriteria.add(disjunction);
		
	//	for(String u : uniqueCodeLists){
	//		listObjectCriteria.add(Restrictions.eq("id", u));
	//	}
		
		
		List<ListObject> loList = listObjectCriteria.list();
		
		//Value sets created
		//sql = "SELECT USER_ID FROM  CODE_LIST_AUDIT_LOG clal where cast(clal.timestamp as Date) between ? and ? AND ACTIVITY_TYPE ='Code List Created'";
		//sql = "Select lo.list_object_id, lo.object_owner from LIST_OBJECT lo, CODE_LIST_AUDIT_LOG cl " +
		//"where cast(cl.timestamp as Date) between ? and ? and lo.list_object_id=cl.code_list_id and cl.activity_type in ('Code List Created','Draft Value Set Created','Grouped Code List Created');";
			
		//mPreparedStmt = connect.prepareStatement(sql);
		//mPreparedStmt.setString(1, startDate);
		//mPreparedStmt.setString(2, endDate);
		//metaResultSet = mPreparedStmt.executeQuery();
		
		for(ListObject lo : loList){
			if(lo ==null)
				continue;
			if(lo.getObjectOwner() == null)
				continue;
			/*if(lo.getObjectOwner().getEmailAddress()== null)
				continue;
				*/
			if(lo.getObjectOwner().getId() == null)
				continue;
			userHash = lo.getObjectOwner().getId();
			//email = lo.getObjectOwner().getEmailAddress();
			if(accountsList.get(userHash)!= null){
				Integer count =(Integer)accountsList.get(userHash).get("VALUE_SETS_CREATED");
				accountsList.get(userHash).put("VALUE_SETS_CREATED", count+1);
			}
			else{
				User u = lo.getObjectOwner();
				if(u.getActivationDate().after(endDateAsDate)){
					continue;
				}
				HashMap<String, Object> mRow = new HashMap<String, Object>();
				mRow.put("EMAIL_ADDRESS", u.getEmailAddress());
				mRow.put("FIRST_NAME", u.getFirstName());
				mRow.put("LAST_NAME",u.getLastName());
				mRow.put("ORGANIZATION_NAME", u.getOrganizationName());
				mRow.put("ORG_OID", u.getOrgOID());
				mRow.put("ACTIVATION_DATE", u.getActivationDate());
				mRow.put("SIGN_IN_DATE", u.getSignInDate());
				roleID = u.getSecurityRole().getId();
				mRow.put("SECURITY_ROLE_ID", securityRole.get(roleID)); 
				mRow.put("MEASURES_CREATED", 0);
				mRow.put("VALUE_SETS_CREATED", 1);
				userHash = u.getId();
				accountsList.put(userHash,mRow);
			}
		}
		
		
//		while(metaResultSet.next()){
//				email =  userEmailMap.get(  metaResultSet.getString("OBJECT_OWNER"));
//				if(accountsList.get(email)!= null){
//					//WANT VALUESET/CODELIST CREATE DATE
//					//String timeStamp = metaResultSet.getString("TIMESTAMP");
//					//if(isInterval(timeStamp, startDate, endDate)){
//						Integer count =(Integer)accountsList.get(email).get("VALUE_SETS_CREATED");
//						accountsList.get(email).put("VALUE_SETS_CREATED", count+1);
//					//}
//				}
//		}
//		close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return accountsList;
}



public LinkedHashMap<String, Object> retrieveSummaryReportData(String startDate, String endDate){
	LinkedHashMap<String, Object> rows = new LinkedHashMap<String, Object>();
	try{
		
		
		ArrayList<LinkedHashMap<String, Object>> measureRows = new ArrayList<LinkedHashMap<String,Object>>();
		
		Criteria auditLogCriteria = session.createCriteria(mat.reportmodel.MeasureAuditLog.class);
		auditLogCriteria.add(Restrictions.sqlRestriction("DATE(TIMESTAMP) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
		auditLogCriteria.add(Restrictions.eq("activityType", "Measure Created"));
		auditLogCriteria.add(Restrictions.sqlRestriction("MEASURE_ID IN (SELECT ID FROM MEASURE)"));
		List<MeasureAuditLog> auditLogList = auditLogCriteria.list();
		
		
		
		//process
		//String sql = "Select m.id, m.scoring, m.measure_owner_id from MEASURE m, MEASURE_AUDIT_LOG ml " +
		//						"where cast(ml.timestamp as Date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Created';";
		//mPreparedStmt = connect.prepareStatement(sql);
		//mPreparedStmt.setString(1, startDate);
		//mPreparedStmt.setString(2, endDate);	
		//mResultSet = mPreparedStmt.executeQuery();
		
		
		//Criteria measureCriteria = session.createCriteria(mat.model.Measure.class);
		//List<Measure> measureList = measureCriteria.list();
		
		for(MeasureAuditLog mal : auditLogList){
			LinkedHashMap<String, Object> mRow = new LinkedHashMap<String, Object>();				
			String id = mal.getMeasure().getId();
			String measureOwner  = mal.getMeasure().getOwner().getId();
			mRow.put("measureId", id);
			mRow.put("scoring", mal.getMeasure().getMeasureScoring());
			
			Criteria metaCriteria = session.createCriteria(mat.reportmodel.Metadata.class);
			//ASK ABOUT THIS
			//metaCriteria.add(Restrictions.eq( "measure.getID()", id));
			metaCriteria.add(Restrictions.sqlRestriction("MEASURE_ID = '" + id + "'"));
			
			List<Metadata> metaList = metaCriteria.list();
			ArrayList<String> measureType = new ArrayList<String>();
			for( Metadata m : metaList){
				measureType.add(m.getValue());
			}
			mRow.put("measureType", measureType);
			
			
			Criteria userCriteria = session.createCriteria(mat.reportmodel.User.class);
			userCriteria.add(Restrictions.eq( "id", measureOwner));
			List<User> userList = userCriteria.list();
			
			for(User u : userList){
				mRow.put("userId", u.getId());
				mRow.put("emailAddress", u.getEmailAddress()); 
				mRow.put("organization", u.getOrganizationName()); 
				mRow.put("orgOid", u.getOrgOID());
			}
			
			measureRows.add(mRow);
			
		}
		
		/*
		while(mResultSet.next()){
			LinkedHashMap<String, Object> mRow = new LinkedHashMap<String, Object>();				
			String id =  mResultSet.getString("ID");				
			String measureOwner = mResultSet.getString("measure_owner_id");
			mRow.put("measureId", id);
			mRow.put("scoring", mResultSet.getString("SCORING"));
			
			sql = "Select meta.value from METADATA meta where meta.measure_id = ? and meta.name in ('MeasureType')";
			metaPreparedStmt = connect.prepareStatement(sql);
			metaPreparedStmt.setString(1, id);
			
			metaResultSet = metaPreparedStmt.executeQuery();
			
			ArrayList<String> measureType = new ArrayList<String>();
			
			while(metaResultSet.next()){
				measureType.add(metaResultSet.getString("VALUE"));
			}
			
			mRow.put("measureType", measureType);
			
			sql = "Select u.user_id, u.email_address, u.organization_name,u.org_oid from USER u where u.user_id = ?";
			userPreparedStmt = connect.prepareStatement(sql);
			userPreparedStmt.setString(1, measureOwner);

			userResultSet = userPreparedStmt.executeQuery();
			while(userResultSet.next()){
				mRow.put("userId", userResultSet.getString("USER_ID"));
				mRow.put("emailAddress", userResultSet.getString("EMAIL_ADDRESS"));
				mRow.put("organization", userResultSet.getString("ORGANIZATION_NAME"));
				mRow.put("orgOid", userResultSet.getString("ORG_OID"));
			}
			
			measureRows.add(mRow);
			
		}
		*/
		
		rows.put("measuresList", measureRows);
		
		ArrayList<LinkedHashMap<String, Object>> valueSetRows = new ArrayList<LinkedHashMap<String,Object>>();
		
	//	Criteria loCriteria = session.createCriteria(mat.model.ListObject.class);
		//metaCriteria.add(Restrictions.eq( "id", measureOwner));
		//List<ListObject> loList = loCriteria.list();
		
		Criteria calCriteria = session.createCriteria(mat.reportmodel.CodeListAuditLog.class);
		//metaCriteria.add(Restrictions.eq( "id", measureOwner));
		
		Criterion cOne = Restrictions.eq("activityType", "Code List Created");
		Criterion cTwo = Restrictions.eq("activityType", "Value Set Created");
		Criterion cThree = Restrictions.eq("activityType", "Draft Value Set Created");
		Criterion cFour = Restrictions.eq("activityType", "Grouped Value Set Created");
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(cOne);
		disjunction.add(cTwo);
		disjunction.add(cThree);
		disjunction.add(cFour);
		calCriteria.add(disjunction); 
		
		
		calCriteria.add(Restrictions.sqlRestriction("DATE(TIMESTAMP) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
		//calCriteria.add(Restrictions.eq("activityType", "Code List Created"));
		//calCriteria.add(Restrictions.eq("activityType", "Value Set Created"));	
		//calCriteria.add(Restrictions.eq("activityType", "Draft Value Set Created"));
		//calCriteria.add(Restrictions.eq("activityType", "Grouped Value Set Created"));
		
		calCriteria.add(Restrictions.sqlRestriction("CODE_LIST_ID IN (SELECT LIST_OBJECT_ID FROM LIST_OBJECT)"));
		
		
		
		List<CodeListAuditLog> calList = calCriteria.list();

		for(CodeListAuditLog cal : calList){
			User owner = cal.getCodeList().getObjectOwner();
			if(owner != null){
				LinkedHashMap<String, Object> vRow = new LinkedHashMap<String, Object>();
				vRow.put("userId", owner.getId());
				vRow.put("emailAddress", owner.getEmailAddress());
				vRow.put("organization", owner.getOrganizationName());
				vRow.put("orgOid", owner.getOrgOID());
				valueSetRows.add(vRow);	
			}
		}
		
		int q;
		q=1;
		
		/*sql = "Select lo.list_object_id, lo.object_owner from LIST_OBJECT lo, CODE_LIST_AUDIT_LOG cl " +
							"where cast(cl.timestamp as Date) between ? and ? and lo.list_object_id=cl.code_list_id and cl.activity_type in 
							('Code List Created','Value Set Created','Draft Value Set Created','Grouped Value Set Created');";

		vSetPreparedStmt = connect.prepareStatement(sql);
		vSetPreparedStmt.setString(1, startDate);
		vSetPreparedStmt.setString(2, endDate);
		
		vSetResultSet = vSetPreparedStmt.executeQuery();
		
		while(vSetResultSet.next()){
			LinkedHashMap<String, Object> vRow = new LinkedHashMap<String, Object>();
			String vSetOwner = vSetResultSet.getString("OBJECT_OWNER");
			sql = "Select u.user_id, u.email_address, u.organization_name,u.org_oid from USER u where u.user_id = ?";
			userPreparedStmt = connect.prepareStatement(sql);
			userPreparedStmt.setString(1, vSetOwner);

			userResultSet = userPreparedStmt.executeQuery();
			while(userResultSet.next()){
				vRow.put("userId", userResultSet.getString("USER_ID"));
				vRow.put("emailAddress", userResultSet.getString("EMAIL_ADDRESS"));
				vRow.put("organization", userResultSet.getString("ORGANIZATION_NAME"));
				vRow.put("orgOid", userResultSet.getString("ORG_OID"));
			}
			valueSetRows.add(vRow);	
		}
	*/
		
		
		rows.put("valueSetList", valueSetRows);
		
		//closeConnection();
	}catch(Exception e){
		e.printStackTrace();
	}
	return rows;
}


public HashMap<String, HashMap<String, Object>> runSummaryOfMeasuresReport(String startDate, String endDate){
	HashMap<String, HashMap<String, Object>> measuresList = new HashMap<String, HashMap<String, Object>>();
	
	try{
		
		Criteria malCriteria = session.createCriteria(mat.reportmodel.MeasureAuditLog.class);
	
		malCriteria.add(Restrictions.sqlRestriction("DATE(TIMESTAMP) between DATE('" + startDate + "') and DATE('" + endDate + "')"));
		malCriteria.add(Restrictions.eq("activityType", "Measure Created"));
		
		//DB seems to have entries in the MeasureAuditLog with no corresponding
		//entries in the Measure table.
		malCriteria.add(Restrictions.sqlRestriction("MEASURE_ID IN (SELECT ID FROM   MEASURE)"));
		
		
		List<MeasureAuditLog> malList = malCriteria.list();

		
		
		
		//process
//		String sql = "Select m.id,m.description, m.scoring,m.export_ts,ml.activity_type, ml.timestamp from MEASURE m, MEASURE_AUDIT_LOG ml " +
//								"where cast(ml.timestamp as date) between ? and ? and m.id=ml.measure_id and ml.activity_type = 'Measure Created';";
//		
//		mPreparedStmt = connect.prepareStatement(sql);
//		mPreparedStmt.setString(1, startDate);
//		mPreparedStmt.setString(2, endDate);
//		
//		mResultSet = mPreparedStmt.executeQuery();
//	    

		for(MeasureAuditLog mal : malList){
			
			
			Measure m = mal.getMeasure();
			String measureId =  m.getId();  
			HashMap<String, Object> mRow = new HashMap<String, Object>();
			mRow.put(CommonConstants.MAT_IDENTIFIER, m.getId());  //CHECK THIS TO SEE IF IT IS RIGHT
			mRow.put(CommonConstants.MEASURE_SCORING, m.getMeasureScoring()); 
			mRow.put(CommonConstants.EMEASURE_NAME, m.getDescription()); 
			mRow.put(CommonConstants.CREATION_DATE_TIME, mal.getTime()); 
			mRow.put(CommonConstants.PACKAGE_DATE_TIME, m.getExportedDate()); 
			
			
			ArrayList<String> measureType = new ArrayList<String>();
		
			Criteria metaCriteria = session.createCriteria(mat.reportmodel.Metadata.class);
			
//			metaCriteria.add(Restrictions.eq("name", "version"));
//			metaCriteria.add(Restrictions.eq("name", "MeasureSteward"));
//			metaCriteria.add(Restrictions.eq("name", "EndorseByNQF"));
//			metaCriteria.add(Restrictions.eq("name", "Description"));
//			metaCriteria.add(Restrictions.eq("name", "MeasureType"));
//			metaCriteria.add(Restrictions.eq("name", "MeasureStatus"));
//			metaCriteria.add(Restrictions.eq("measure.id", measureId));
			Disjunction disjunction = Restrictions.disjunction();
			Criterion cOne = Restrictions.eq("name", "version");
			Criterion cTwo =Restrictions.eq("name", "MeasureSteward");
			Criterion cThree =Restrictions.eq("name", "EndorseByNQF");
			Criterion cFour	= Restrictions.eq("name", "Description");
			Criterion cFive	=Restrictions.eq("name", "MeasureType");
			Criterion cSix	=Restrictions.eq("name", "MeasureStatus");
			Criterion cSeven =	Restrictions.eq("measure.id", measureId);
			disjunction.add(cOne);
			disjunction.add(cTwo);
			disjunction.add(cThree);
			disjunction.add(cFour);
			disjunction.add(cFive);
			disjunction.add(cSix);
			disjunction.add(cSeven);
			metaCriteria.add(disjunction);
			
			
			
			
			List<Metadata> metaList = metaCriteria.list();

				for(Metadata met : metaList){
					if(met.getName().equalsIgnoreCase("MeasureType"))
						measureType.add(met.getValue());
					if(met.getName().equalsIgnoreCase("Version"))
						mRow.put(CommonConstants.VERSION, met.getValue());
					if(met.getName().equalsIgnoreCase("MeasureSteward"))
						mRow.put(CommonConstants.MEASURE_STEWARD, met.getValue());
					if(met.getName().equalsIgnoreCase("EndorseByNQF"))
						mRow.put(CommonConstants.ENDORSED, met.getValue());
					if(met.getName().equalsIgnoreCase("Description"))
						mRow.put(CommonConstants.DESCRIPTION, met.getValue());
					if(met.getName().equalsIgnoreCase("MeasureStatus"))
						mRow.put(CommonConstants.STATUS, met.getValue());
					
				}
				mRow.put(CommonConstants.MEASURE_TYPE, measureType);
				measuresList.put(measureId, mRow);
		}
		
		/*(
		while(mResultSet.next()){
			String measureId = mResultSet.getString("ID");
			HashMap<String, Object> mRow = new HashMap<String, Object>();
			mRow.put(CommonConstants.MAT_IDENTIFIER, mResultSet.getString("ID"));
			mRow.put(CommonConstants.MEASURE_SCORING, mResultSet.getString("SCORING"));
			mRow.put(CommonConstants.EMEASURE_NAME,mResultSet.getString("description"));
			mRow.put(CommonConstants.CREATION_DATE_TIME, mResultSet.getString("timestamp"));
			mRow.put(CommonConstants.PACKAGE_DATE_TIME, mResultSet.getString("export_ts"));
			
			//Finding meta data information for a given measure.
			sql = "Select meta.name, meta.value from METADATA meta where meta.measure_id = ? and meta.name in ('version','MeasureSteward','EndorseByNQF','Description','MeasureType','MeasureStatus')";
			metaPreparedStmt = connect.prepareStatement(sql);
			metaPreparedStmt.setString(1, measureId);
			metaResultSet = metaPreparedStmt.executeQuery();
			
			ArrayList<String> measureType = new ArrayList<String>();
			
			while(metaResultSet.next()){
				if(metaResultSet.getString("name").equalsIgnoreCase("MeasureType")){
					measureType.add(metaResultSet.getString("value"));
				}
				if(metaResultSet.getString("name").equalsIgnoreCase("Version")){
					mRow.put(CommonConstants.VERSION,metaResultSet.getString("value"));
				}
				if(metaResultSet.getString("name").equalsIgnoreCase("MeasureSteward")){
					mRow.put(CommonConstants.MEASURE_STEWARD,metaResultSet.getString("value"));
				}
				if(metaResultSet.getString("name").equalsIgnoreCase("EndorseByNQF")){
					mRow.put(CommonConstants.ENDORSED,metaResultSet.getString("value"));
				}
				if(metaResultSet.getString("name").equalsIgnoreCase("Description")){
					mRow.put(CommonConstants.DESCRIPTION,metaResultSet.getString("value"));
				}
				if(metaResultSet.getString("name").equalsIgnoreCase("MeasureStatus")){
					mRow.put(CommonConstants.STATUS,metaResultSet.getString("value"));
				}
			}
			mRow.put(CommonConstants.MEASURE_TYPE, measureType);

		measuresList.put(measureId, mRow);
		}
		*/
	//close();
	}catch(Exception e){
		e.printStackTrace();
	}
	return measuresList;
}



}
