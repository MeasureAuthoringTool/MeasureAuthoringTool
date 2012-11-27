package mat.shared;



import java.util.HashMap;

import com.google.gwt.user.client.ui.HTML;

public class DynamicTabBarFormatter {

	//GWT is pretty printing these so you have to use
	//the pretty printed format or else it will not map
	//if you don't store it internally on integer keys.
	
	private HTML mockWidget ; 
	
	private HashMap<Integer, String> titles = new HashMap<Integer,String>();
	private HashMap<Integer, String> selectedTitles = new HashMap<Integer,String>();
	
	
	public String getTitle(int i){	
		return titles.get(i);
	}
	
	public String getSelectedTitle(int i){
		return selectedTitles.get(i);
	}
	
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
	
	public String normalTitle(String title){
		mockWidget = new HTML(title);
		return mockWidget.getHTML();
		
	}
	
	public String selectedTitle(String title){
		String html= "<b <label=\"\" title=\"Selected "+title+"\" aria-label=\"Selected "+title+"\">\u25ba"+title+" </b>";
		mockWidget = new HTML(html);
		return mockWidget.getHTML();
	}
}
