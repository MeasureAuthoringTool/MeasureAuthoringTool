package mat.shared;


import com.google.gwt.user.client.ui.HTML;

import java.util.HashMap;

/**
 * The Class DynamicTabBarFormatter.
 */
public class DynamicTabBarFormatter {

	//GWT is pretty printing these so you have to use
	//the pretty printed format or else it will not map
	//if you don't store it internally on integer keys.
	
	/** The mock widget. */
	private HTML mockWidget ; 
	
	/** The titles. */
	private HashMap<Integer, String> titles = new HashMap<Integer,String>();
	
	/** The selected titles. */
	private HashMap<Integer, String> selectedTitles = new HashMap<Integer,String>();
	
	
	/**
	 * Gets the title.
	 * 
	 * @param i
	 *            the i
	 * @return the title
	 */
	public String getTitle(int i){	
		return titles.get(i);
	}
	
	/**
	 * Gets the selected title.
	 * 
	 * @param i
	 *            the i
	 * @return the selected title
	 */
	public String getSelectedTitle(int i){
		return selectedTitles.get(i);
	}
	
	/**
	 * Insert title.
	 * 
	 * @param i
	 *            the i
	 * @param title
	 *            the title
	 */
	public void insertTitle(int i, String title){
		String expandedTitle = title;
		
		 if(title.equalsIgnoreCase(ConstantMessages.POP_TAB))
		  expandedTitle = ConstantMessages.POP_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.NUM_TAB))
				 expandedTitle = ConstantMessages.NUM_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.NUM_EX_TAB))
			 expandedTitle = ConstantMessages.NUM_EX_TAB_EXPANDED; 
		 if(title.equalsIgnoreCase(ConstantMessages.DEN_TAB))
				 expandedTitle = ConstantMessages.DEN_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.EXCL_TAB))
				 expandedTitle = ConstantMessages.EXCL_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.EXCEP_TAB))
				 expandedTitle = ConstantMessages.EXCEP_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.MEASURE_POP_TAB))
				 expandedTitle = ConstantMessages.MEASURE_POP_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.MEASURE_OBS_TAB))
				 expandedTitle = ConstantMessages.MEASURE_OBS_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.STRAT_TAB))
			 expandedTitle = ConstantMessages.STRAT_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.USER_DEFINED_TAB))
				 expandedTitle = ConstantMessages.USER_DEFINED_TAB_EXPANDED;
		 if(title.equalsIgnoreCase(ConstantMessages.MEASURE_PHRASE_TAB))
				 expandedTitle = ConstantMessages.MEASURE_PHRASE_TAB_EXPANDED;	
		
		
		String aStr = normalTitle(title);
		String bStr = selectedTitle(expandedTitle);
		
		titles.put(i, aStr);
		selectedTitles.put(i, bStr);
	}
	
	/**
	 * Normal title.
	 * 
	 * @param title
	 *            the title
	 * @return the string
	 */
	public String normalTitle(String title){
		mockWidget = new HTML(title);
		return mockWidget.getHTML();
		
	}
	
	/**
	 * Selected title.
	 * 
	 * @param title
	 *            the title
	 * @return the string
	 */
	public String selectedTitle(String title){
		String html= "<b <label=\"\" title=\"Selected "+title+"\" aria-label=\"Selected "+title+"\">\u25ba"+title+" </b>";
		mockWidget = new HTML(html);
		return mockWidget.getHTML();
	}
}
