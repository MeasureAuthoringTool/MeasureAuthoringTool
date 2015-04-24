package mat.client.shared;

/**
 * this class can be used in lieu of HorizontalPanel and so avoid using a
 * <table>
 * element.
 * 
 * @author aschmidt
 */
public class HorizontalFlowPanel extends CustomFlowPanel {
	
	/* (non-Javadoc)
	 * @see mat.client.shared.CustomFlowPanel#getFlowStyle()
	 */
	@Override
	protected String getFlowStyle() {
		return "inline";
	}
}
