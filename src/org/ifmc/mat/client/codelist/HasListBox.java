package org.ifmc.mat.client.codelist;



public interface HasListBox  {
	int getSortOrder();
	String getValue();
	String getItem();
	
	public static class Comparator implements java.util.Comparator<HasListBox>{
		
		public int compare(HasListBox o1, HasListBox o2){
		     return o1.getItem().compareTo(o2.getItem());	
		}
	}

}
