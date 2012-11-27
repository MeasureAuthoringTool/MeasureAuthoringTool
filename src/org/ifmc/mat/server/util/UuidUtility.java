package org.ifmc.mat.server.util;

public class UuidUtility {

	public static String idToUuid(String id){
		StringBuffer sb = new StringBuffer(id);
		sb.insert(8,'-');
		sb.insert(13,'-');
		sb.insert(18,'-');
		sb.insert(23,'-');
		return sb.toString();
	}
	
}
