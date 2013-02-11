package mat.client.measure;

import mat.client.shared.ListBoxMVP;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * 
 * @author aschmidt
 *
 */
public class MeasureSearchFilterPanel {
	
	/*US566*/
	
	private Panel panel = new FlowPanel();
	
	public static final int MY_MEASURES = 0;
	public static final int ALL_MEASURES = 1;
	
	
	ListBoxMVP listBox = new ListBoxMVP();
	private final String title = "Measure Search Filter";
	private final String MY_MEASURES_STR = "My Measures";
	private final String ALL_MEASURES_STR = "All Measures";
	
	/*US606*/
	private final String MY_MEASURES_TITLE = "Filter By My Measures";
	private final String ALL_MEASURES_TITLE = "Filter By All Measures";
	
	public MeasureSearchFilterPanel(){
		DOM.setElementAttribute(listBox.getElement(), "id", title);
		listBox.insertItem(MY_MEASURES_STR, MY_MEASURES_TITLE,
				"Returns Measures that you have created." );
		listBox.insertItem(ALL_MEASURES_STR, ALL_MEASURES_TITLE,
				"Returns all Measures created by all users.");
		
		resetFilter();
		panel.add(listBox);
	}

	public Panel getPanel() {
		return panel;
	}
	
	public int getSelectedIndex(){
		int ret = listBox.getSelectedIndex();
		return ret;
	}
	
	public int getDefaultFilter(){
		return MY_MEASURES;
	}
	
	public void resetFilter(){
		listBox.setSelectedIndex(getDefaultFilter());
	}
	
	public void setEnabled(boolean enabled){
		listBox.setEnabled(enabled);
	}
}
