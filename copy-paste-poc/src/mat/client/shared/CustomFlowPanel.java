package mat.client.shared;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * this family of classes can be used in lieu of VerticalPanel and
 * HorizontalPanel and so avoid using a
 * <table>
 * element.
 * 
 * @author aschmidt
 */
public abstract class CustomFlowPanel extends FlowPanel {
	
	/**
	 * Gets the flow style.
	 * 
	 * @return the flow style
	 */
	protected abstract String getFlowStyle();
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.FlowPanel#add(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void add(Widget w) {
		w.getElement().getStyle().setProperty("display", getFlowStyle());
		super.add(w);
	}
}
