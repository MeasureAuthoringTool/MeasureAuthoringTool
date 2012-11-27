package org.ifmc.mat.shared;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidationUtility {

	private final String goodChars = "`~1!2@3#4$5%6^7&8*9(0)-_=+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
	private Set<Character> allowedChars = new HashSet<Character>();


	public ValidationUtility(){
		for(int i=0;i<goodChars.length();i++){
			allowedChars.add(goodChars.charAt(i));
		}

	}
	private boolean hasIllegalChars(String s){
		if(s == null)
			return false;
		for(int i=0;i<s.length();i++){
			if(!allowedChars.contains(s.charAt(i))){
				if(!Character.isWhitespace(s.charAt(i)))
					return true;
			}
		}
		return false;
	}
	public boolean isIllegalValidation(String s){
		if(hasIllegalChars(s))
			return true;
		return false;
	}

	public void validate(Map<String, String> metadata,String key){
		String s = metadata.get(key);
		if(this.isIllegalValidation(s)){
			int len = 60;
			if(key.length()<len)
				len = key.length();
			log("Validation found illegal text in "+ key.substring(0, len) +".");
		}
	}
	
	private void log(String logstr){
		//TODO get this output to the server logs
		System.out.println(logstr);
	}
	

}
