package mat.client.clause.cqlworkspace.leftNavBar.sections;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;

public abstract class GenericView {
	protected SimplePanel containerPanel = new SimplePanel();
	private VerticalPanel messagePanel = new VerticalPanel();
	protected MessageAlert successMessageAlert = new SuccessMessageAlert();
	protected MessageAlert errorMessageAlert = new ErrorMessageAlert();
	protected HTML heading = new HTML();
	

	public GenericView() {
		buildMessagePanel();
	}
	
	private void buildMessagePanel() {
		messagePanel.add(successMessageAlert);
		messagePanel.add(errorMessageAlert);
		messagePanel.setStyleName("marginLeft15px");
	}
	
	public VerticalPanel getMessagePanel() {
		return messagePanel;
	}
	
	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}
	
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}
	
	public Widget asWidget() {
		return containerPanel.asWidget();
	}
	
	public abstract void reset();
	public abstract void setHeading(String text, String linkName);
}