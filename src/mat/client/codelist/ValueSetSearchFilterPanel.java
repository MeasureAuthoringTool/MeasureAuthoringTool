package mat.client.codelist;

import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * The Class ValueSetSearchFilterPanel.
 * 
 * @author aschmidt
 */
public class ValueSetSearchFilterPanel {
	
	/*US566*/
	
	/** The panel. */
	private Panel panel = new FlowPanel();
	
	/** The Constant MY_VALUE_SETS. */
	public static final int MY_VALUE_SETS = 0;
	
	/** The Constant ALL_VALUE_SETS. */
	public static final int ALL_VALUE_SETS = 1;
	
	/** The list box. */
	ListBoxMVP listBox = new ListBoxMVP();
	
	/** The title. */
	private final String title = "Value Set Search Filter";
	
	/** The my value sets str. */
	private final String MY_VALUE_SETS_STR = "My Value Sets";
	
	/** The all value sets str. */
	private final String ALL_VALUE_SETS_STR = "All Value Sets";
	
	/*US606*/
	/** The my value sets title. */
	private final String MY_VALUE_SETS_TITLE = "Filter By My Value Sets";
	
	/** The all value sets title. */
	private final String ALL_VALUE_SETS_TITLE = "Filter By All Value Sets";
	
	/**
	 * Instantiates a new value set search filter panel.
	 */
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

	/**
	 * Gets the panel.
	 * 
	 * @return the panel
	 */
	public Panel getPanel() {
		return panel;
	}
	
	/**
	 * Gets the selected index.
	 * 
	 * @return the selected index
	 */
	public int getSelectedIndex(){
		int ret = listBox.getSelectedIndex();
		return ret;
	}
	
	/**
	 * Gets the default filter.
	 * 
	 * @return the default filter
	 */
	public int getDefaultFilter(){
		return MY_VALUE_SETS;
	}
	
	/**
	 * Reset filter.
	 */
	public void resetFilter(){
		listBox.setSelectedIndex(getDefaultFilter());
	}
	
	/**
	 * Sets the enabled.
	 * 
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled){
		listBox.setEnabled(enabled);
	}
}
