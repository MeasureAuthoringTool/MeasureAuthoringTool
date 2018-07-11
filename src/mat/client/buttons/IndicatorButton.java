package mat.client.buttons;


import com.google.gwt.user.client.ui.HTML;

import mat.client.shared.HorizontalFlowPanel;


public class IndicatorButton{

	private String inactiveText;
	private String activeText;
	private HTML link;
	private HorizontalFlowPanel panel = new HorizontalFlowPanel();
	
	public IndicatorButton(String activeLabel, String inactiveLabel) {
		this.inactiveText = inactiveLabel;
		this.activeText = activeLabel;
		link = new HTML();
	}
	
	
	public void hideActive(){
		panel.clear();
		link = new HTML("<button type=\"button\" title='"
				+ inactiveText + "' tabindex=\"0\" class=\"btn btn-link\" > "
			+ "<i class=\"fa fa-circle\" style=\"color: DarkRed;\"></i><span>"+ 
				"  "  + inactiveText + "</span></button>");
		link.getElement().setTabIndex(0);
		panel.add(link);
	}

	
	public void showActive(){
		panel.clear();
		link = new HTML("<button type=\"button\" title='"
				+ activeText + "' tabindex=\"0\" class=\"btn btn-link\" > "
				+ "<i class=\"fa fa-circle\" style=\"color: DarkGreen;\"></i><span>"+
				"  "+ activeText + "</span></button>");
		link.getElement().setTabIndex(0);
		panel.add(link);
		panel.getElement().focus();
	}
	
	public void setVisible() {
		hideActive();
	}
	
	public HorizontalFlowPanel getPanel() {
		return panel;
	}
	
	public HTML getLink() {
		return link;
	}
}
