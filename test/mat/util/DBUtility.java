package mat.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DBUtility {

	
	String wkbkName;
	String[] headerList;
	
	
	public static void main(String[] args) {
	System.out.println("Database Query Utility.");
	
	
	//Date functionality
	//Hitting enter twice makes it go from a week in the past to today
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
	try{
		System.out.println("Enter begin date(MM/dd/yyyy): ");
		String startDate = reader.readLine();
		
		if(startDate.equalsIgnoreCase("")){
			Calendar inst = Calendar.getInstance();
			inst.add(Calendar.DATE, -7);
			startDate =df.format(inst.getTime());
		}
		System.out.println("Enter ending date(MM/dd/yyyy): ");
		String endDate = reader.readLine();
		if(endDate.equalsIgnoreCase("")){
			endDate =df.format(Calendar.getInstance().getTime());
		}
		Date sd = df.parse(startDate);
		Date ed = df.parse(endDate);
		System.out.println("startDate "+df.format(sd));
		System.out.println("endDate "+df.format(ed));
	}
	catch(Exception e){
		e.printStackTrace();
	}
	
	
	
	Connection conn = null;
	String driver = "com.mysql.jdbc.Driver";

	//Replace these with the desired values in mat-persistance.xml
	String url = "jdbc:mysql://ifdevdb01:3306/MAT_APP";
	String userName = "mat-dev"; 
	String password = "***REMOVED***";

	
	//String url = "jdbc:mysql://iftstdb01:3306/MAT_APP";
	//String userName = "mat-dev"; 
	//String password = "***REMOVED***";

	
	try {
		
		//Stores a hashtable for each row that we populate with data
		HashMap<String, HashMap<String, Object>> userList = new HashMap<String, HashMap<String, Object>>();
		
		
		//US485 "Summary of User Accounts"
		/*
		 ** First Name   USER.FIRST_NAME
		 ** Last Name    USER.LAST_NAME
		 ** Email        USER.EMAIL_ADDRESS
		 ** Organization   USER.ORGANIZATION_NAME
		 ** Organization OID    USER.ORG_OID
		 ** Date Account Created   USER.ACTIVATION_DATE
		 ** Last Date Accessed USER.SIGN_IN_DATE
		 * 
		 ** # of Measures created by user 
		 * -- SELECT * FROM  MEASURE_AUDIT_LOG WHERE ACTIVITY_TYPE ='Measure Created'
		 * 
		 ** # of ValueSets (CodeLists) created by user
		 * -- SELECT * FROM  CODE_LIST_AUDIT_LOG WHERE ACTIVITY_TYPE ='Code List Created'
		 * 
		 ** Account type {Admin,Top level user, user}  USER.SECURITY_ROLE_ID
		 */
		
		
		Class.forName(driver).newInstance();
		conn = DriverManager.getConnection(url,userName,password);
		System.out.println("Connected to the database");

		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery("SELECT * FROM  USER");
		ResultSetMetaData md = res.getMetaData();
		
		while (res.next()) {
			String email = res.getString("EMAIL_ADDRESS");
			userList.put(email, new HashMap<String, Object>());
			
			String uid = res.getString("USER_ID");
			
			String fn =res.getString("FIRST_NAME");
			userList.get(email).put("FIRST_NAME", fn);
			
			
			String ln = res.getString("LAST_NAME");
			userList.get(email).put("LAST_NAME", ln);
			
			String org = res.getString("ORGANIZATION_NAME");
			userList.get(email).put("ORGANIZATION_NAME", org);
			
			String orgOID = res.getString("ORG_OID");
			userList.get(email).put("ORG_OID", orgOID);
			
			
			
			String active = res.getString("ACTIVATION_DATE");
			Date activeDate = res.getDate("ACTIVATION_DATE");
			userList.get(email).put("ACTIVATION_DATE", activeDate);
			
			
			String accessed = res.getString("SIGN_IN_DATE");
			Date signDate = res.getDate("SIGN_IN_DATE");
			userList.get(email).put("SIGN_IN_DATE", signDate);
			
			String userType = res.getString("SECURITY_ROLE_ID");
			String userTypeString = "UNKNOWN";
			if(userType.equalsIgnoreCase("1"))userTypeString = "Admin";
			if(userType.equalsIgnoreCase("2"))userTypeString = "Top Level User";
			if(userType.equalsIgnoreCase("3"))userTypeString = "User";
			userList.get(email).put("SECURITY_ROLE_ID", userTypeString);
			
			userList.get(email).put("MEASURES_CREATED", 0);
			userList.get(email).put("VALUE_SETS_CREATED",0);
			
		}
		
		
		st = conn.createStatement();
		 res = st.executeQuery("SELECT * FROM  MEASURE_AUDIT_LOG WHERE ACTIVITY_TYPE ='Measure Created'");
		 md = res.getMetaData();
		 while (res.next()) {
				String email = res.getString("USER_ID");
				if(userList.get(email)!=null){
					Integer count =(Integer)userList.get(email).get("MEASURES_CREATED");
					userList.get(email).put("MEASURES_CREATED", count+1);
				}
		}

		 st = conn.createStatement();
		 res = st.executeQuery("SELECT * FROM  CODE_LIST_AUDIT_LOG WHERE ACTIVITY_TYPE ='Code List Created'");
		 md = res.getMetaData();
		 while (res.next()) {
			 	String email = res.getString("USER_ID");
			 	if(userList.get(email)!=null){
			 		int count =(Integer)userList.get(email).get("VALUE_SETS_CREATED");
			 		userList.get(email).put("VALUE_SETS_CREATED", count+1);
			 	}
		}	 
		 
		//Extra for US489 
		 st = conn.createStatement();
		 res = st.executeQuery("SELECT * FROM  USER");
		 md = res.getMetaData();
		 int userAccounts =0;
		 while (res.next()) {
				userAccounts++;
				//Date activeDate = res.getDate("ACTIVATION_DATE");
				
				//Log the orgid
				//res.getString("ORG_OID");
		}
		 st = conn.createStatement();
		 res = st.executeQuery("SELECT * FROM  MEASURE");
		 md = res.getMetaData();
		 int measureCount =0;
		 while (res.next()) {
				measureCount++;
		}
		 
		 //ASK LINDSEY, is ORG_OID only a column in the USER table?
		 
		conn.close();
		System.out.println("Disconnected from database");
		System.out.println("Writing workbook");
		//The Excel file to output
		HSSFWorkbook wkbk = new HSSFWorkbook();
		HSSFSheet wkst = wkbk.createSheet("Summary of User Accounts");
		
		int userCount =0;
		int colCount=0;
		for(String user: userList.keySet()){
			HSSFRow row = wkst.createRow(userCount);
			
			if(userCount ==0){
				colCount=0;
				//Autogen the column headers from the keys
				for(String key: userList.get(user).keySet()){
					HSSFCell cell = row.createCell(colCount);
					cell.setCellValue(key);
					colCount++;
				}
				userCount++;
				row = wkst.createRow(userCount);
			}
			
			colCount =0;
			for(String key: userList.get(user).keySet()){
				HSSFCell cell = row.createCell(colCount);
				if(userList.get(user).get(key)!= null)
					cell.setCellValue(userList.get(user).get(key).toString());
				colCount++;
			}
			userCount++;
		}
		
		
		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
		wkbk.write(fileOut);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	
	
	
	
	}
}
