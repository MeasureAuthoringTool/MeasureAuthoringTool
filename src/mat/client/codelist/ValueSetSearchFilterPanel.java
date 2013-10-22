package mat.client.codelist;

import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * 
 * @author aschmidt
 *
 */
public class ValueSetSearchFilterPanel {
	
	/*US566*/
	
	private Panel panel = new FlowPanel();
	
	public static final int MY_VALUE_SETS = 0;
	public static final int ALL_VALUE_SETS = 1;
	
	ListBoxMVP listBox = new ListBoxMVP();
	private final String title = "Value Set Search Filter";
	private final String MY_VALUE_SETS_STR = "My Value Sets";
	private final String ALL_VALUE_SETS_STR = "All Value Sets";
	
	/*US606*/
	private final String MY_VALUE_SETS_TITLE = "Filter By My Value Sets";
	private final String ALL_VALUE_SETS_TITLE = "Filter By All Value Sets";
	
	public ValueSetSearchFilterPanel(){
		DOM.setElementAttribute(listBox.getElement(), "id", title);
		DOM.setElementAttribute(listBox.getElement(), "name", title);
		listBox.setName(title);
		listBox.insertItem(MY_VALUE_SETS_STR, MY_VALUE_SETS_TITLE,
				"Returns Value Sets that you have created." );
		listBox.insertItem(ALL_VALUE_SETS_STR, ALL_VALUE_SETS_TITLE,
				"Returns all Value Sets created by all users.");		
		resetFilter();
		panel.add(LabelBuilder.buildInvisibleLabel(new Label(), title));
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
		return MY_VALUE_SETS;
	}
	
	public void resetFilter(){
		listBox.setSelectedIndex(getDefaultFilter());
	}
	
	public void setEnabled(boolean enabled){
		listBox.setEnabled(enabled);
	}
}
