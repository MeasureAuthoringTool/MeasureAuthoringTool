package mat.client.buttons;


import com.google.gwt.user.client.ui.HTML;

import mat.client.shared.HorizontalFlowPanel;


public class IndicatorButton{

	private String inactiveText;
	private String activeText;
	private HTML hideLink;
	private HTML showLink;
	private HorizontalFlowPanel panel = new HorizontalFlowPanel();
	
	public IndicatorButton(String activeLabel, String inactiveLabel) {
		this.inactiveText = inactiveLabel;
		this.activeText = activeLabel;
		hideLink = new HTML();
		showLink = new HTML();
	}
	
	public void createAllLinks() {
		createHideLink();
		createShowLink();
	}
	private void createHideLink() {
		hideLink = new HTML("<button type=\"button\" title='"
				+ inactiveText + "' tabindex=\"0\" class=\"btn btn-link\" > "
			+ "<i class=\"fa fa-circle\" style=\"color: DarkRed;\"></i><span>"+ 
				"  "  + inactiveText + "</span></button>");
		hideLink.getElement().setTabIndex(0);
		panel.add(hideLink);
	}
	
	private void createShowLink() {
		showLink = new HTML("<button type=\"button\" title='"
				+ activeText + "' tabindex=\"0\" class=\"btn btn-link\" > "
				+ "<i class=\"fa fa-circle\" style=\"color: DarkGreen;\"></i><span>"+
				"  "+ activeText + "</span></button>");
		showLink.getElement().setTabIndex(0);
		panel.add(showLink);
		panel.getElement().focus();
	}
	
	public void hideActive(boolean hide) {
		hideLink.setVisible(hide);
		showLink.setVisible(!hide);
	}
	
	public HorizontalFlowPanel getPanel() {
		return panel;
	}
	
	public HTML getLink() {
		return hideLink;
	}
}
