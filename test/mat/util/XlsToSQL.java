package mat.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.gwt.dev.util.collect.HashSet;

//TODO  Use StringBuilder to avoid O(n^2) behavior from string copies.

public class XlsToSQL {

	/**
	 * @param args
	 */
	
	public static String createRandomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length) {
			sb.append(Integer.toHexString(random.nextInt()));
		}
		return sb.subSequence(0,length-1).toString();
	}
	
	public static void main(String[] args) {
		try{
		
		// Modify these two lines to change the input and output files.
		FileInputStream fIn = new FileInputStream("war/SupplementalDataElementCodeLists.xls");
		BufferedWriter fOut = new BufferedWriter(new FileWriter("chadout.sql"));
		HSSFWorkbook wkbk = new HSSFWorkbook(fIn);
		Sheet sheet = wkbk.getSheetAt(0);
		
		String oStr ="";
		oStr += "\nLOCK TABLES `LIST_OBJECT` WRITE;\n";
		oStr += "INSERT INTO `LIST_OBJECT` VALUES ";
		
		
		Set<String> listObjects = new HashSet<String>();
		
		for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
		      Row row = rit.next();
		     
		      List<String> rowElts = new ArrayList<String>();    
		      //System.out.print("(");
		      for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ) {
		    	  Cell cell = cit.next();
		    	  rowElts.add(cell.toString());
		    	//  System.out.print(cell.toString());
		    	//  if(cit.hasNext())
		    	//	  System.out.print(",");
		      }
		    //  System.out.print(")");
		     // if(rit.hasNext())
		    //	  System.out.print(",\n");
		    //  else
		    //	  System.out.print("\n");
		      oStr += "('"+ createRandomString(32)+"',";
		     // System.out.print("version, ");
		      oStr += "'"+rowElts.get(2)+"',";
		     // System.out.print("name, ");
		      oStr += "'"+rowElts.get(3)+"',";
		      listObjects.add( rowElts.get(3) );
		     // System.out.print("steward, ");
		      oStr +="'"+rowElts.get(0)+"',";
		     // System.out.print("oid, ");
		      oStr += "'"+rowElts.get(1)+"',";
		      oStr += "'rationale',";
		      //System.out.print("comment,");
		      oStr += "'',";
		      //System.out.print("object_status_id, ");
		      oStr += "'1',";
		     // System.out.print("object owner, ");
		      oStr += "NULL,";
		      //oStr += "'category_id', ";
		     // oStr += "'"+rowElts.get(4)+"',";
		      // This maps to data code 9
		      oStr += "'"+9+"',";
		      
		      //System.out.print("code_system_version, ");
		      oStr += "'"+rowElts.get(6)+"',";
		      oStr += "'code_system_id', ";
		    //  System.out.print("measure_id, ");
		      oStr += "NULL,";
		      //System.out.print("steward_other)");
		      oStr += "'')";
		      if(rit.hasNext())
		    	  oStr += ",";
		      else
		    	  oStr+= ";";
		      oStr += "\n";
		    //  System.out.print("\n");
		}
		oStr += "UNLOCK TABLES;\n";

		String preStr ="\nLOCK TABLES `CODE` WRITE;\n"
			  + "INSERT INTO `CODE` VALUES ";
		
		
		for(Iterator lIt =listObjects.iterator(); lIt.hasNext();){		
			preStr += "('"+createRandomString(32)+"','" + lIt.next().toString().trim() + "', 'descriptor', 'list_object_id')";
			if(lIt.hasNext())
				preStr += ",";
		}
		preStr += ";\nUNLOCK TABLES;\n";
		
		String midStr ="\nINSERT INTO CODE_LIST VALUES ('"+createRandomString(32)+"');\n\n";
		
		
		String sqlOutput ="USE MAT_APP;\n";
		
		sqlOutput += oStr+midStr+preStr;
		sqlOutput += "\nCOMMIT;";
		
		System.out.print(sqlOutput);
		
		
		System.out.print(listObjects.toString());
		
		
		//
		
		fOut.write(sqlOutput);
		fOut.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(createRandomString(32));
	}
}
