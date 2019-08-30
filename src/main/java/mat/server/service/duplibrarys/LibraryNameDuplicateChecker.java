package mat.server.service.duplibrarys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;


/**
 * 
 * This class is a one time task to check for duplicate librarys
 *
 */
public class LibraryNameDuplicateChecker {
	
	/**
	 * replace these variables with your database credentials
	 */
	private final static String USERNAME = "yourUsername";
	private final static String PASSWORD = "yourPassword";

	public static void main(String[] args) {
		try {
			HashMap<String, String> overallDuplicates = new HashMap<String, String>();
			HashMap<String, String> duplicateLibrarys = new HashMap<String, String>();
			HashMap<String, String> duplicateMeasures = new HashMap<String, String>();
			HashMap<String, String> librarys = new HashMap<String, String>();
			HashMap<String, String> measures = new HashMap<String, String>();

			// create our mysql database connection
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://dmmatdmyq01.telligen.us:3306/MAT5-8_STAGE_08282018";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, USERNAME, PASSWORD);

			// our SQL SELECT query.
			// if you only need a few columns, specify them by name instead of using "*"
			String libQuery = "select SET_ID, CQL_NAME from CQL_LIBRARY";
			String measQuery = "select EXTRACTVALUE(convert(x.MEASURE_XML using utf8), '/measure/cqlLookUp/library') as Name, m.MEASURE_SET_ID as Id"
					+ " from MEASURE_XML x, MEASURE m" + " where m.QDM_VERSION is not null and x.MEASURE_ID = m.ID";

			// create the java statement
			java.sql.Statement st = conn.createStatement();
			java.sql.Statement st2 = conn.createStatement();

			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(libQuery);
			ResultSet rs2 = st2.executeQuery(measQuery);

			// iterate through the java resultset
			// KEY IS ID AND VALUE IS THE NAME
			int count = 0;
			int count1 = 0;
			int count2 = 0;
			while (rs.next()) {
				// populate librarys hasmap
				if (librarys.containsValue(rs.getString("CQL_NAME"))
						&& (!librarys.containsKey(rs.getString("SET_ID")))) {
					duplicateLibrarys.put(rs.getString("SET_ID"), rs.getString("CQL_NAME"));
					count++;
				} else
					librarys.put(rs.getString("SET_ID"), rs.getString("CQL_NAME"));
			}

			while (rs2.next()) {
				// populate measures hashmap
				if (measures.containsValue(rs2.getString("Name")) && (!measures.containsKey(rs2.getString("Id")))) {
					duplicateMeasures.put(rs2.getString("Id"), rs2.getString("Name"));
					count1++;
				} else
					measures.put(rs2.getString("Id"), rs2.getString("Name"));
			}
			st.close();
			st2.close();

			System.out.println(count + " library duplicates " + duplicateLibrarys.values() + "\n");
			System.out.println(count1 + " measure duplicates " + duplicateMeasures.values() + "\n");
			// iterate through librarys and measures to find duplicates

			for (String key : duplicateMeasures.keySet()) {
				if (duplicateLibrarys.containsValue(duplicateMeasures.get(key))) {
					overallDuplicates.put(key, duplicateMeasures.get(key));
					count2++;
				}
			}
			System.out.println(count2 + " overall duplicates " + overallDuplicates.values() + "\n");
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
