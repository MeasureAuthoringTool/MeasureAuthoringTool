package mat.client.measure;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;

/**
 * The Class MeasureSearchFilterPanel.
 * 
 * @author aschmidt
 */
public class MeasureSearchFilterPanel {
	
	/*US566*/
	
	/** The panel. */
	private Panel panel = new FlowPanel();
	
	/** The Constant MY_MEASURES. */
	public static final int MY_MEASURES = 0;
	
	/** The Constant ALL_MEASURES. */
	public static final int ALL_MEASURES = 1;
	
	
	/** The list box. */
	ListBoxMVP listBox = new ListBoxMVP();
	
	/** The title. */
	private final String title = "Measure Search Filter";
	
	/** The my measures str. */
	private final String MY_MEASURES_STR = "My Measures";
	
	/** The all measures str. */
	private final String ALL_MEASURES_STR = "All Measures";
	
	/*US606*/
	/** The my measures title. */
	private final String MY_MEASURES_TITLE = "Filter By My Measures";
	
	/** The all measures title. */
	private final String ALL_MEASURES_TITLE = "Filter By All Measures";
	
	/**
	 * Instantiates a new measure search filter panel.
	 */
	public MeasureSearchFilterPanel(){
		DOM.setElementAttribute(listBox.getElement(), "id", title);
		DOM.setElementAttribute(listBox.getElement(), "name", title);
		listBox.insertItem(MY_MEASURES_STR, MY_MEASURES_TITLE,
				"Returns Measures that you have created." );
		listBox.insertItem(ALL_MEASURES_STR, ALL_MEASURES_TITLE,
				"Returns all Measures created by all users.");
		
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
		return MY_MEASURES;
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
