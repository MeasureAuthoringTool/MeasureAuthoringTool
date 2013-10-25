package mat.client.shared;

/**
 * this class can be used in lieu of VerticalPanel and so avoid using a
 * <table>
 * element.
 * 
 * @author aschmidt
 */
public class VerticalFlowPanel extends CustomFlowPanel {
	
	/* (non-Javadoc)
	 * @see mat.client.shared.CustomFlowPanel#getFlowStyle()
	 */
	@Override
	protected String getFlowStyle() {
		return "block";
	}
}
