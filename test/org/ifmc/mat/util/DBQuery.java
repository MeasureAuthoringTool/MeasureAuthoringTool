package org.ifmc.mat.util;


import java.sql.*;
public class DBQuery {

	public static void main(String[] args) {
		System.out.println("Database Query Utility.");
		Connection conn = null;
		String driver = "com.mysql.jdbc.Driver";

		//Replace these with the desired values in mat-persistance.xml
		String url = "jdbc:mysql://ifdevdb01:3306/MAT_APP";
		String userName = "mat-dev"; 
		String password = "***REMOVED***";

		
//		String url = "jdbc:mysql://iftstdb01:3306/MAT_APP";
//		String userName = "mat-dev"; 
//		String password = "***REMOVED***";


		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url,userName,password);
			System.out.println("Connected to the database");

			//Do queries here
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("SELECT * FROM  MEASURE_VALIDATION_LOG");
		
			ResultSetMetaData md = res.getMetaData();
			System.out.println("Number of cols is:" );
			//System.out.println(res.  .   toString());
			
			//System.out.println(res.);
			//System.out.println("Emp_code: " + "\t" + "Emp_name: ");
			while (res.next()) {
				Blob bob = res.getBlob("INTERIM_BLOB");
				byte[] bdata = bob.getBytes(1, (int) bob.length());
				String text = new String(bdata);
				System.out.println(text);
			}
			conn.close();
			System.out.println("Disconnected from database");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}


