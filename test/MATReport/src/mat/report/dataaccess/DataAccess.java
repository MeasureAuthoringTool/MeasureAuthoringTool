package MATReport.src.mat.report.dataaccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import MATReport.src.mat.report.common.CommonConstants;

public class DataAccess {
	private Connection connect = null;
	private PreparedStatement mPreparedStmt= null;
	private PreparedStatement metaPreparedStmt= null;
	private PreparedStatement userPreparedStmt = null;
	private PreparedStatement vSetPreparedStmt = null;
	private PreparedStatement catPreparedStmt = null;
	private PreparedStatement dTypePreparedStmt = null;
	private PreparedStatement attrPreparedStmt = null;
	private PreparedStatement duplicateattrPreparedStmt = null;
	private PreparedStatement qdmPreparedStmt = null;
	private PreparedStatement qTermPreparedStmt = null;
	private PreparedStatement deciPreparedStmt = null;
	private PreparedStatement decisionPreparedStmt = null;
	private PreparedStatement clausePreparedStmt = null;
	

	private ResultSet mResultSet = null;
	private ResultSet metaResultSet = null;
	private ResultSet userResultSet = null;
	private ResultSet vSetResultSet = null;
	private ResultSet catResultSet = null;
	private ResultSet dTypeResultSet = null;
	private ResultSet attrResultSet = null;
	private ResultSet duplicateattrResultSet = null;
	private ResultSet qdmResultSet = null;
	private ResultSet qTermResultSet = null;
	private ResultSet deciResultSet = null;
	private ResultSet decisionResultSet = null;
    private ResultSet clauseResultSet = null;
	
	
	private String dbConnectionURL ="";
	private String userName = "";
	private String password = "";
	
	
	public void init(){
		try {
			
			Properties connectionProps = new Properties();
			connectionProps.put("user", userName);
			connectionProps.put("password", password);
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbConnectionURL , connectionProps);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	public void setDatabaseCreds(String jdbcURL, String userName, String password){
		this.dbConnectionURL = jdbcURL;
		this.userName = userName;
		this.password = password;
	}
	
	
	
	public LinkedHashMap<String, Object> retrieveSummaryReportData(String startDate, String endDate){
		LinkedHashMap<String, Object> rows = new LinkedHashMap<String, Object>();
		try{
			init();
			
			ArrayList<LinkedHashMap<String, Object>> measureRows = new ArrayList<LinkedHashMap<String,Object>>();
			
			//process
			String sql = "Select m.id, m.scoring, m.measure_owner_id from MEASURE m, MEASURE_AUDIT_LOG ml " +
									"where cast(ml.timestamp as Date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Created';";
			
			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			
			mResultSet = mPreparedStmt.executeQuery();
			while(mResultSet.next()){
				LinkedHashMap<String, Object> mRow = new LinkedHashMap<String, Object>();				
				String id = mResultSet.getString("ID");				
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
			
			rows.put("measuresList", measureRows);
			
			ArrayList<LinkedHashMap<String, Object>> valueSetRows = new ArrayList<LinkedHashMap<String,Object>>();
			
			sql = "Select lo.list_object_id, lo.object_owner from LIST_OBJECT lo, CODE_LIST_AUDIT_LOG cl " +
								"where cast(cl.timestamp as Date) between ? and ? and lo.list_object_id=cl.code_list_id and cl.activity_type='Code List Created';";

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
			
			rows.put("valueSetList", valueSetRows);
			
			closeConnection();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}
		return rows;
	}

	public void dbDump(){
		try {
			init();
			DatabaseMetaData dbmd = connect.getMetaData();
			String[] types = {"TABLE"};
			ResultSet rs = dbmd.getTables(null,null,"%",types);
			System.out.println("Table name:");
			List<String> tableList = new ArrayList<String>();
			while (rs.next()){
				String table = rs.getString("TABLE_NAME");
				System.out.println(table);
				tableList.add(table);
			}

			String sql;
			Statement st;
			ResultSet metaResultSet;
			for(int k=0;k<tableList.size();k++){
				sql = "SELECT * FROM " + tableList.get(k);
				st = connect.createStatement();
				metaResultSet = st.executeQuery(sql);

				ResultSetMetaData meta =metaResultSet.getMetaData();
				int colCount = meta.getColumnCount();
				for(int i=0;i<colCount;i++){
					System.out.println(meta.getColumnName(i+1));
				}

			}
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
public LinkedHashMap<String, Object> retrieveQDMElementsData(String startDate, String endDate){
		
		LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
		try{
			
			init();
			
			ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> rows = new ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>>();
			
		    loadCatDatAttr(rows);//This loads the category-datatype-Attribute combination available in the Database and loads has Excels rows.
			
			String sql = "Select distinct m.id, m.description from MEASURE m, MEASURE_AUDIT_LOG ml " +
									"where cast(ml.timestamp as date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Package Created';";

			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			
			mResultSet = mPreparedStmt.executeQuery();
			LinkedHashMap<String, Object> msrList = new LinkedHashMap<String, Object>();
			ArrayList measuresList = resultSetToArrayList(mResultSet); 
			for(int m=0;m<measuresList.size();m++){
				ArrayList<String> msrKeysList = new ArrayList<String>();				
			    HashMap measuresMap =(HashMap) measuresList.get(m);
				String measureId =(String) measuresMap.get("ID");
				String measureDesc = (String)measuresMap.get("DESCRIPTION");
				sql = "Select name, decision_id from  CLAUSE where measure_id = ? and context_id not in ('9');";
				clausePreparedStmt  = connect.prepareStatement(sql);
				clausePreparedStmt.setString(1,measureId);
				clauseResultSet = clausePreparedStmt.executeQuery();
				ArrayList clausesList = resultSetToArrayList(clauseResultSet); 
				for(int c=0; c<clausesList.size();c++){
					String key = null;
					String categoryId = null;
					HashMap clausesMap =(HashMap) clausesList.get(c);
					String decisionId = (String)  clausesMap.get("DECISION_ID");
					//This query finds out any system clauses is using any of the measure phrase.
					sql = "select clause_id from DECISION where id in ( Select id from DECISION where parent_id in (select id from DECISION where parent_id = ?));";
					decisionPreparedStmt = connect.prepareStatement(sql);
					decisionPreparedStmt.setString(1,decisionId);
					decisionResultSet = decisionPreparedStmt.executeQuery();
					ArrayList listofDecisions = resultSetToArrayList(decisionResultSet);
				    for(int dl=0;dl<listofDecisions.size();dl++){
				    	HashMap decisionlListMap =(HashMap) listofDecisions.get(dl);
						String clauseUsedInMeasure = (String)  decisionlListMap.get("CLAUSE_ID");
						sql = "Select name, decision_id from CLAUSE where id = ? ";
						clausePreparedStmt  = connect.prepareStatement(sql);
						clausePreparedStmt.setString(1,clauseUsedInMeasure);
						clauseResultSet = clausePreparedStmt.executeQuery();
						if(clauseResultSet.next()){
					    	String clause_decision_id = clauseResultSet.getString("decision_id");
					    	HashMap<String,ArrayList> mapofChildren = new HashMap<String,ArrayList>();
					    	mapofChildren = processDecision(clause_decision_id);
					    	//System.out.println(mapofChildren);
						    	 for(String decisionkey: mapofChildren.keySet()){
						    		 ArrayList decision_to_children = mapofChildren.get(decisionkey);
						    		 for(int dtc = 0; dtc < decision_to_children.size();dtc++){
						    			 HashMap mapofmap = (HashMap) decision_to_children.get(dtc);
						    			 String operator = (String) mapofmap.get("OPERATOR");
						    			 String qdm_dec_id = (String) mapofmap.get("ID");
						    			 if(operator.equalsIgnoreCase("QDSTERM")){
						    				//Get information about the qdm_term.
										    	sql = "Select qt.qdm_element_id from QDM_TERM qt where qt.decision_id = ?";
										    	qTermPreparedStmt = connect.prepareStatement(sql);
												qTermPreparedStmt.setString(1, qdm_dec_id);
												qTermResultSet = qTermPreparedStmt.executeQuery();
												
												if(qTermResultSet.next()){
													String qdmId = qTermResultSet.getString("qdm_element_id");
													//Get the Quality_Data_Model info
													sql = "Select qdm.data_type_id, qdm.list_object_id from QUALITY_DATA_MODEL qdm where qdm.measure_id=? and qdm.quality_data_model_id =?";
													qdmPreparedStmt = connect.prepareStatement(sql);
													qdmPreparedStmt.setString(1, measureId);
													qdmPreparedStmt.setString(2,qdmId);
													qdmResultSet = qdmPreparedStmt.executeQuery();
													ArrayList qdmArrayList = resultSetToArrayList(qdmResultSet);
													for(int q=0;q<qdmArrayList.size();q++){		
														HashMap qdmMap = (HashMap)qdmArrayList.get(q);
														String qdmDataTypeId = (String)qdmMap.get("DATA_TYPE_ID");
														String qdmListObjectId = (String)qdmMap.get("LIST_OBJECT_ID");
														//find out category id
														sql = "Select lo.category_id from LIST_OBJECT lo where lo.list_object_id = ?;";
														vSetPreparedStmt = connect.prepareStatement(sql);
														vSetPreparedStmt.setString(1, qdmListObjectId);
														
														vSetResultSet = vSetPreparedStmt.executeQuery();
														ArrayList valueSetList = resultSetToArrayList(vSetResultSet);
														for(int v=0;v<valueSetList.size();v++){
															HashMap valueSetMap = (HashMap)valueSetList.get(v);
															categoryId = (String)valueSetMap.get("CATEGORY_ID");
														}
														key = categoryId + "-" + qdmDataTypeId;
													}//end of QDM ArrayList.	
													//Check for Attribute association
											    	sql = "Select d.attribute_id from DECISION d where d.parent_id in (select ds.id from DECISION ds where ds.parent_id = ?) and d.operator = 'QDSATTRIBUTE';";
											    	deciPreparedStmt = connect.prepareStatement(sql);
													deciPreparedStmt.setString(1, qdm_dec_id);						
													deciResultSet = deciPreparedStmt.executeQuery();
													ArrayList decisionList = resultSetToArrayList(deciResultSet);
													if(!decisionList.isEmpty()){
														for(int d=0; d<decisionList.size(); d++){		
														    HashMap decisionMap = (HashMap)decisionList.get(d);
															String attributeId = (String)decisionMap.get("ATTRIBUTE_ID");
															msrKeysList.add(key + "/" + attributeId);
														}	
													}else{
														msrKeysList.add(key);
													}
												}//end of if qTermResultSet
						    			 }//end of QDSTerm
						    		 }//end of For dtc
						    	 }//end of for DecisionKey
					    }//End of if
					}//End of listofDecisions For loop
				}//End of clausesList for Loop
				msrList.put(measureDesc, msrKeysList);
			}//End of MeasuresList.
			response.put("qdmList", rows);
			response.put("msrList", msrList);
			close();
			closeConnection();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}
		return response;
	}
	

	public List<String> getDuplicateAttributeCombinations(String prefix, String attributeId){
		
		ArrayList<String> response = new ArrayList<String>();
		try{
			init();
			
			String sql = "Select attr.id from QDM_ATTRIBUTES attr where attr.name = (select sub.name from QDM_ATTRIBUTES sub where sub.id=?);";
			
			duplicateattrPreparedStmt = connect.prepareStatement(sql);
			duplicateattrPreparedStmt.setString(1, attributeId);
			
			duplicateattrResultSet = duplicateattrPreparedStmt.executeQuery();
			
			ArrayList duplicateattrResultList = resultSetToArrayList(duplicateattrResultSet);
			for(int dup=0;dup<duplicateattrResultList.size();dup++){
				HashMap duplicateAttrMap = (HashMap)duplicateattrResultList.get(dup);
				response.add(prefix + "/" + duplicateAttrMap.get("ID"));
			}
			
			close();
			closeConnection();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}
		return response;
			
		
	}
	
	
	public boolean isIntervalFunc(String x, String start, String end){
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Date xD;
		Date startD;
		Date endD;
		try {
			xD = df.parse(x);
			startD = df.parse(start);
			endD = df.parse(end);
			
			if(xD.before(startD))
				return false;
			if(xD.after(endD))
				return false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	//Usefull when two queries are giving you different results and you need the
	//symmetric diference.
	//Make the big-O on this nlogn  if it gets large...
	private void printDiff(ArrayList<String> setOne, ArrayList<String> setTwo){
		int i;
		for(i=0;i<setOne.size();i++){
			String s = setOne.get(i);
			if(setTwo.indexOf(s)<0){
				System.out.println("**"+s+"**");
			}
		}
		
		for(i=0;i<setTwo.size();i++){
			String s = setTwo.get(i);
			if(setOne.indexOf(s)<0){
				System.out.println("**"+s+"**");
			}
		}
	}
	public HashMap<String, HashMap<String, Object>> runSummaryOfAccountsReport(String startDate, String endDate){


		HashMap<String, HashMap<String, Object>> accountsList = new HashMap<String, HashMap<String, Object>>();
		String email;
		String userHash;

		HashMap<String,String> securityRole = new HashMap<String,String>();
		
		HashMap<String,String> userEmailMap = new HashMap<String,String>();
		
 
		//HashMap<String,String> measureAuditEmailMap = new HashMap<String,String>();
		//HashMap<String,String> codeListAuditAuditEmailMap = new HashMap<String,String>();
		
		HashMap<String,String> measureOwnerMap = new HashMap<String,String>();
		
		//HashMap<String,String> codeListOwnerMap = new HashMap<String,String>();
		
		String roleID;
		String description;
		
		try{
			init();

			String sql = "SELECT * FROM  SECURITY_ROLE";
			Statement st = connect.createStatement();
			ResultSet mResultSet = st.executeQuery(sql);
			while(mResultSet.next()){
				roleID=  mResultSet.getString("SECURITY_ROLE_ID");
				description=  mResultSet.getString("DESCRIPTION");
				if(description.equalsIgnoreCase("Super user")){
					description = "Top level User";
				}
				securityRole.put(roleID, description);
			}
			
			 sql = "SELECT * FROM  USER";
		 st = connect.createStatement();
			 mResultSet = st.executeQuery(sql);
			while(mResultSet.next()){
				email = mResultSet.getString("EMAIL_ADDRESS");
				HashMap<String, Object> mRow = new HashMap<String, Object>();
				mRow.put("EMAIL_ADDRESS", mResultSet.getString("EMAIL_ADDRESS"));
				mRow.put("FIRST_NAME", mResultSet.getString("FIRST_NAME"));
				mRow.put("LAST_NAME",mResultSet.getString("LAST_NAME"));
				mRow.put("ORGANIZATION_NAME", mResultSet.getString("ORGANIZATION_NAME"));
				mRow.put("ORG_OID", mResultSet.getString("ORG_OID"));

				mRow.put("ACTIVATION_DATE", mResultSet.getString("ACTIVATION_DATE"));
				mRow.put("SIGN_IN_DATE", mResultSet.getString("SIGN_IN_DATE"));
				roleID = mResultSet.getString("SECURITY_ROLE_ID");
				mRow.put("SECURITY_ROLE_ID", securityRole.get(roleID)); 
				
				
				mRow.put("MEASURES_CREATED", 0);
				mRow.put("VALUE_SETS_CREATED", 0);
				accountsList.put(email,mRow);
				userHash = mResultSet.getString("USER_ID");
				userEmailMap.put(userHash ,email);
				//emailOwnerMap.put(email, userHash);
			}
			//Measures created

			
			
			
			sql = "Select m.id, m.measure_owner_id from MEASURE m";
			mPreparedStmt = connect.prepareStatement(sql);
			//mPreparedStmt.setString(1, startDate);
			//mPreparedStmt.setString(2, endDate);
			ResultSet metaResultSet = mPreparedStmt.executeQuery();
			while(metaResultSet.next()){
				measureOwnerMap.put(metaResultSet.getString("ID"), 
						metaResultSet.getString("MEASURE_OWNER_ID"));
			}
			
			
			
		//	sql = "SELECT * FROM  MEASURE_AUDIT_LOG ml WHERE cast(ml.timestamp as Date) between ? and ? AND ACTIVITY_TYPE ='Measure Created'";
			
			sql = "Select m.id, m.scoring, m.measure_owner_id from MEASURE m, MEASURE_AUDIT_LOG ml " +
			"where cast(ml.timestamp as Date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Created';";

			//sql = "Select * from MEASURE_AUDIT_LOG ml, MEASURE m,  " +
			//"where cast(ml.timestamp as Date) between ? and ? and m.id=ml.measure_id and ml.activity_type='Measure Created';";

			
			//GET CREATED MEASURE CODES
			
			
			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			metaResultSet = mPreparedStmt.executeQuery();
			
			
			while(metaResultSet.next()){
					
					//Do the join... then iterate over measure table user IDs to pull them directly from the audit log
					//thus disregarding stale email addresses in the measure audit log table
					//measureAuditEmailMap.put(metaResultSet.getString("MEASURE_ID"), metaResultSet.getString("USER_ID"));
					String s = metaResultSet.getString("MEASURE_OWNER_ID");
					//if(s ==null)
					//	continue;
					email =  userEmailMap.get(s);
					//metaResultSet.getString("USER_ID");
					if(accountsList.get(email)!=null){
						//String timeStamp = metaResultSet.getString("TIMESTAMP");
						//if(isInterval(timeStamp, startDate, endDate)){
							Integer count =(Integer)accountsList.get(email).get("MEASURES_CREATED");
							accountsList.get(email).put("MEASURES_CREATED", count+1);
						//}
					}
			}

			//Value sets created
			//sql = "SELECT USER_ID FROM  CODE_LIST_AUDIT_LOG clal where cast(clal.timestamp as Date) between ? and ? AND ACTIVITY_TYPE ='Code List Created'";
			sql = "Select lo.list_object_id, lo.object_owner from LIST_OBJECT lo, CODE_LIST_AUDIT_LOG cl " +
			"where cast(cl.timestamp as Date) between ? and ? and lo.list_object_id=cl.code_list_id and cl.activity_type='Code List Created';";
				
			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			metaResultSet = mPreparedStmt.executeQuery();
			
			
			while(metaResultSet.next()){
					email =  userEmailMap.get(  metaResultSet.getString("OBJECT_OWNER"));
					if(accountsList.get(email)!= null){
						//WANT VALUESET/CODELIST CREATE DATE
						//String timeStamp = metaResultSet.getString("TIMESTAMP");
						//if(isInterval(timeStamp, startDate, endDate)){
							Integer count =(Integer)accountsList.get(email).get("VALUE_SETS_CREATED");
							accountsList.get(email).put("VALUE_SETS_CREATED", count+1);
						//}
					}
			}
			close();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}
		return accountsList;
	}

	public HashMap<String, Object> runSummaryOfTrendsOverTimeReport(String startDate, String endDate){
		
		HashMap<String, Object> results = new HashMap<String, Object>();
		int measuresComplete =0;
		int newUserAccounts =0;
		Set<String> orgOIDs = new HashSet<String>();
		
		
		try{
			init();
			
			String sql = "SELECT ORG_OID FROM USER us where cast(us.activation_date as Date) between ? and ?";
			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			ResultSet mResultSet = mPreparedStmt.executeQuery();
			
			while(mResultSet.next()){
				newUserAccounts++;
			}
			
			sql = "SELECT ORG_OID FROM USER";
			mPreparedStmt = connect.prepareStatement(sql);
			mResultSet = mPreparedStmt.executeQuery();
			while(mResultSet.next()){
				orgOIDs.add(mResultSet.getString("ORG_OID"));
			}
			
			
			
			
			
			//Count  complete and created during this time period?
			sql = "SELECT * FROM  MEASURE WHERE MEASURE_STATUS ='Complete'";
			Statement st = connect.createStatement();
			ResultSet metaResultSet = st.executeQuery(sql);
			
			while(metaResultSet.next()){
				measuresComplete++;
			}
				
		close();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}

		results.put("NEW_USER_ACCOUNTS", Integer.toString(newUserAccounts));
		results.put("MEASURES_COMPLETE", Integer.toString(measuresComplete));
		results.put("ORG_OIDS", Integer.toString(orgOIDs.size()));
		results.put("DATE_STRING", startDate+" to "+ endDate);
		
		return results;
	}


	public HashMap<String, HashMap<String, Object>> runSummaryOfMeasuresReport(String startDate, String endDate){
		HashMap<String, HashMap<String, Object>> measuresList = new HashMap<String, HashMap<String, Object>>();
		
		try{
			init();
			
			//process
			String sql = "Select m.id,m.description, m.scoring,m.export_ts,ml.activity_type, ml.timestamp from MEASURE m, MEASURE_AUDIT_LOG ml " +
									"where cast(ml.timestamp as date) between ? and ? and m.id=ml.measure_id and ml.activity_type = 'Measure Created';";
			
			mPreparedStmt = connect.prepareStatement(sql);
			mPreparedStmt.setString(1, startDate);
			mPreparedStmt.setString(2, endDate);
			
			mResultSet = mPreparedStmt.executeQuery();
		    
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
		close();
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
		}
		return measuresList;
	}


	
	// You need to close the resultSet
	private void close() {
		try {
			if(mPreparedStmt != null){
				mPreparedStmt.close();
			}
			if(mResultSet != null){
				mResultSet.close();
			}
			if(metaPreparedStmt != null){
				metaPreparedStmt.close();
			}
			if(metaResultSet != null){
				metaResultSet.close();
			}			
			if(userPreparedStmt != null){
				userPreparedStmt.close();
			}
			if(userResultSet != null){
				userResultSet.close();
			}
			if(vSetPreparedStmt != null){
				vSetPreparedStmt.close();
			}
			if(vSetResultSet != null){
				vSetResultSet.close();
			}			
			
			if(catPreparedStmt != null){
				catPreparedStmt.close();
			}
			if(catResultSet != null){
				catResultSet.close();
			}
			if(dTypePreparedStmt != null){
				dTypePreparedStmt.close();
			}
			if(dTypeResultSet != null){
				dTypeResultSet.close();
			}
			if(attrPreparedStmt != null){
				attrPreparedStmt.close();
			}
			if(attrResultSet != null){
				attrResultSet.close();
			}
			if(qdmPreparedStmt != null){
				qdmPreparedStmt.close();
			}
			if(qdmResultSet != null){
				qdmResultSet.close();
			}
			if(qTermPreparedStmt != null){
				qTermPreparedStmt.close();
			}
			if(qTermResultSet != null){
				qTermResultSet.close();
			}

			if(deciPreparedStmt != null){
				deciPreparedStmt.close();
			}
			if(deciResultSet != null){
				deciResultSet.close();
			}
			if(decisionResultSet != null){
				decisionResultSet.close();
			}
			if(clauseResultSet != null){
				clauseResultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void closeConnection(){
		try {
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList resultSetToArrayList(ResultSet rs) throws SQLException{

		java.sql.ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList results = new ArrayList();

		while (rs.next()){

			HashMap row = new HashMap();
	
			results.add(row);
	
			for(int i=1; i<=columns; i++){
				row.put(md.getColumnName(i),rs.getObject(i));
			}
	
	    }
		//rs.close();
		return results;
		}
    
	private void loadCatDatAttr(ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> rows){
		try{
		//Start of Category, datatype and Attribute Combination.
		String sql = "Select cat.category_id, cat.description from CATEGORY cat where cat.description not in('Attribute') order by cat.description;";

		catPreparedStmt = connect.prepareStatement(sql);
		
		catResultSet = catPreparedStmt.executeQuery();
		
		ArrayList categoryArrayList = resultSetToArrayList(catResultSet);
		for(int i=0; i<categoryArrayList.size();i++){				
			String key = null;
			LinkedHashMap<String, LinkedHashMap<String, String>> qdmKeys = new LinkedHashMap<String, LinkedHashMap<String,String>>();
			HashMap mapofCategory = (HashMap)categoryArrayList.get(i);
			String catId = (String)mapofCategory.get("CATEGORY_ID");
			String category =  (String)mapofCategory.get("DESCRIPTION");
			
			key = catId;
			
			sql = "Select dt.data_type_id, dt.description from DATA_TYPE dt where dt.category_id = ? order by dt.description;";
			
			dTypePreparedStmt = connect.prepareStatement(sql);
			dTypePreparedStmt.setString(1, catId);
			
			dTypeResultSet = dTypePreparedStmt.executeQuery();
			ArrayList dataTypeList = resultSetToArrayList(dTypeResultSet);
			
			for(int j=0;j<dataTypeList.size();j++){
				HashMap mapofDataType = (HashMap) dataTypeList.get(j);
				String dataTypeId = (String)mapofDataType.get("DATA_TYPE_ID");
				String dataType = (String)mapofDataType.get("DESCRIPTION");
				
				LinkedHashMap<String, String> qdmMap = new LinkedHashMap<String, String>();
				qdmMap.put(CommonConstants.CATEGORY, category);
				qdmMap.put(CommonConstants.DATATYPE, dataType);
				qdmMap.put(CommonConstants.ATTRIBUTE, "No Attribute Association");
				qdmKeys.put(key + "-" + dataTypeId, qdmMap);						

				
				sql = "Select attr.id, attr.name from QDM_ATTRIBUTES attr where attr.data_type_id = ? order by attr.name;";
				attrPreparedStmt = connect.prepareStatement(sql);
				attrPreparedStmt.setString(1, dataTypeId);
				
				attrResultSet = attrPreparedStmt.executeQuery();
				ArrayList attributeList = resultSetToArrayList(attrResultSet);
			    for(int k=0;k<attributeList.size(); k++){
			    	    HashMap mapofAttribute = (HashMap) attributeList.get(k);
						String attrId = (String)mapofAttribute.get("ID");
						String attribute = (String)mapofAttribute.get("NAME");

						qdmMap = new LinkedHashMap<String, String>();
						qdmMap.put(CommonConstants.CATEGORY, category);
						qdmMap.put(CommonConstants.DATATYPE, dataType);
						qdmMap.put(CommonConstants.ATTRIBUTE, attribute);
						qdmKeys.put(key + "-" + dataTypeId + "/" + attrId, qdmMap);							
				
				}
		
			}
			rows.add(qdmKeys);
		}//end of For 
	
	}catch(SQLException e){}
	}
	
	private ArrayList getChildren(String decision_id){
		ArrayList listofChildren = new ArrayList();
		try{
			String clause_decision_id = decision_id;
			do{
		    	String sql = "Select d.id,d.operator from DECISION d where d.parent_id = ?";
		    	deciPreparedStmt = connect.prepareStatement(sql);
				deciPreparedStmt.setString(1, clause_decision_id);						
				deciResultSet = deciPreparedStmt.executeQuery();
				ArrayList decisionList = resultSetToArrayList(deciResultSet);
				for(int a=0;a<decisionList.size();a++){
					HashMap mapofDecisonoperator = (HashMap) decisionList.get(a);
					clause_decision_id = (String)mapofDecisonoperator.get("ID");
					listofChildren.add(mapofDecisonoperator);
				}
				if(decisionList.isEmpty()){
					clause_decision_id = null;
				}
			}while(clause_decision_id != null && !clause_decision_id.equalsIgnoreCase(""));
		}catch(SQLException s){
			
		}
		return listofChildren;
	}
	
	private HashMap<String,ArrayList> processDecision(String decision_id){
		HashMap<String,ArrayList> mapofChildren = new HashMap<String,ArrayList>();
		ArrayList children_of_decision = getChildren(decision_id);
		if(!children_of_decision.isEmpty()){
			mapofChildren.put(decision_id, children_of_decision);
		}
		for(int b=0;b<children_of_decision.size();b++){
			HashMap childofchild = (HashMap) children_of_decision.get(b);
			String childDid = (String) childofchild.get("ID");
			processDecision(childDid);
		}
		return mapofChildren;
	}
}
