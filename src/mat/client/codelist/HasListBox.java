package mat.client.codelist;



/**
 * The Interface HasListBox.
 */
public interface HasListBox  {
	
	/**
	 * Gets the sort order.
	 * 
	 * @return the sort order
	 */
	int getSortOrder();
	
	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	String getValue();
	
	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	String getItem();
	
	/**
	 * The Class Comparator.
	 */
	public static class Comparator implements java.util.Comparator<HasListBox>{
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(HasListBox o1, HasListBox o2){
		     return o1.getItem().compareTo(o2.getItem());	
		}
	}

}
