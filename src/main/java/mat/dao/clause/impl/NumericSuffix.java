package mat.dao.clause.impl;

/**
 * The Class NumericSuffix.
 */
public class NumericSuffix {

	/**
	 * Decode helper.
	 * 
	 * @param n
	 *            the n
	 * @return the int
	 */
	private int decodeHelper(String n){
		if(n.length()==0)
			return 0;
		try {
			int x = Integer.decode(n);
			return x;
		}
		catch(NumberFormatException e){
			return 0;
		}
	}
	
	/**
	 * Split suffix.
	 * 
	 * @param s
	 *            the s
	 * @return the string[]
	 */
	private String[] splitSuffix(String s){	
		String[] sArr = s.split("[0-9]+$");
		String s1 = "";
		if(sArr != null){
			if(sArr.length>0)
				s1 = sArr[0];
		}
		String s2 = s.substring(s1.length());
		String[] ret = new String[2];
		ret[0]=s1;
		ret[1]=s2;
		return ret;
	}
	
	/**
	 * Compare.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the int
	 */
	public int compare(String a, String b){
		String[] aSplit = splitSuffix(a.trim());
		String[] bSplit = splitSuffix(b.trim());
		int ret = aSplit[0].compareToIgnoreCase(bSplit[0]);
		int retCaseSense = aSplit[0].compareTo(bSplit[0]);
		if(ret != 0)
			return ret;
		else{
			if(retCaseSense!=0){
				if( (decodeHelper(aSplit[1])- decodeHelper(bSplit[1])) !=0)
					return decodeHelper(aSplit[1])- decodeHelper(bSplit[1]);
				else
					return retCaseSense;
			}
			else
				return decodeHelper(aSplit[1])- decodeHelper(bSplit[1]);
		}
	}
}
