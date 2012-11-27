package org.ifmc.mat.shared;

public class StringUtility {
	
	public final String nl = "\r\n";
	/**
	 * for input "blah18", should return 18-1 
	 * naming convention starts at 1, code logic is zero based
	 * @param name
	 * @return
	 */
	public int getPos(String name) {
		int val = 0 ;
		try {
			int offset = 0;
			while(isInt(name.charAt(name.length()-(offset+1))))
				offset++;
			String  numVal = name.substring(name.length()-offset);
			val = (new Integer(numVal)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;//default
		}
		return val-1;
	}
	
	private boolean isInt(char a){
		return Character.isDigit(a);
	}
	
	
	public boolean isEmptyOrNull(String str){
		return str == null || str.isEmpty();
	}
	
	public String stripOffNumber(String s){
		boolean flag = true;
		int strLen = s.length();
		while(flag){
			char c = s.charAt(strLen-1);
			if(Character.isDigit(c)){
				strLen--;
				continue;
			}else{
				flag = false;
			}
		}
		return s.substring(0,strLen);
	}
	
	
	public String trimLeadingZeros(String numberStr){
		return numberStr.replaceFirst("^0+(?!$)", "");
	}
}
