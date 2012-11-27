/**
 * 
 */
package mat.util;

import java.math.BigDecimal;

/**
 * @author vandavar
 *
 */
public class BigDecimalArithmetic {
	public static void main(String [] args){
		 System.out.println("Incrementing Minor Version");  
		 BigDecimal bg = new BigDecimal("0.789");
		 bg =  bg.add(new BigDecimal("0.001"));
		 System.out.println(bg);
		 
		 System.out.println("Incrementing Major Version");  
		 BigDecimal mg = new BigDecimal("123.789");
		 mg =  mg.add(new BigDecimal("1"));
		 System.out.println(mg);
		 
		 String majorVersion = "0020";
		 String minorVersion = "004050";
		 
		 System.out.println("Trimmed MajorVersion"+majorVersion.replaceFirst("^0+(?!$)", ""));
		 System.out.println("Trimmed MinorVersion"+minorVersion.replaceFirst("^0+(?!$)", ""));
		    
	}

}
