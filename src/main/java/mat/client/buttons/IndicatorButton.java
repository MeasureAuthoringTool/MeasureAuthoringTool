package mat.client.buttons;


import com.google.gwt.user.client.ui.HTML;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;


public class IndicatorButton{

	private String inactiveText;
	private String activeText;
	private HTML hideLink;
	private HTML showLink;
	private FlowPanel panel = new FlowPanel();
	
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
		hideLink.getElement().setTabIndex(-1);
		panel.add(hideLink);
		panel.getElement().focus();
		panel.setStyleName("loginSpacer");
	}
	
	private void createShowLink() {
		showLink = new HTML("<button type=\"button\" id=\"disco\" title='"
				+ activeText + "' tabindex=\"0\" class=\"btn btn-link\" > "
				+ "<i class=\"fa fa-circle\" style=\"color: DarkGreen;\"></i><span>"+
				"  "+ activeText + "</span></button>");
		showLink.getElement().setTabIndex(-1);
		panel.add(showLink);
		panel.getElement().focus();
		panel.setStyleName("loginSpacer");
	}
	
	public void hideActive(boolean hide) {
		hideLink.setVisible(hide);
		showLink.setVisible(!hide);
	}
	
	public FlowPanel getPanel() {
		return panel;
	}
	
	public HTML getHideLink() {
		return hideLink;
	}
	
	public HTML getshowLink() {
		return showLink;
	}
}
