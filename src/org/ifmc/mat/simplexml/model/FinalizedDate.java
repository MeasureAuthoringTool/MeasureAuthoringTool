package org.ifmc.mat.simplexml.model;

import java.sql.Timestamp;

public class FinalizedDate {
	private String  ttext;
	private String value;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	
	public void setValue(Timestamp ts){
		setValue(convertTimestampToString(ts));
	}
	
	private String convertTimestampToString(Timestamp ts){
		String hours = getTwoDigitString(ts.getHours());
		String mins = getTwoDigitString(ts.getMinutes());
		String month = getTwoDigitString(ts.getMonth()+1);
		String day = getTwoDigitString(ts.getDate());
		
		String tsStr = month+day+ts.getYear()+1900+hours+mins;
		return tsStr;
	}
	private String getTwoDigitString(int i){
		String ret = i+"";
		if(ret.length()==1)
			ret = "0"+ret;
		return ret;
	}
	
}
