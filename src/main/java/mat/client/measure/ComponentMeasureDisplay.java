package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.BackSaveCancelButtonBar;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SuccessMessageAlert;

public class ComponentMeasureDisplay implements BaseDisplay {
	
	private SimplePanel mainPanel = new SimplePanel();
	FlowPanel flowPanel = new FlowPanel();
	
	private MessageAlert errorMessages = new ErrorMessageAlert();
	private MessageAlert successMessage = new SuccessMessageAlert();

	private BackSaveCancelButtonBar buttonBar = new BackSaveCancelButtonBar("componentMeasures");
	
	private ComponentMeasureSearch componentMeasureSearch = new ComponentMeasureSearch(errorMessages, successMessage, 15);
		
	public ComponentMeasureDisplay() {
		buildMainPanel();
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	public MessageAlert getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(MessageAlert successMessage) {
		this.successMessage = successMessage;
	}

	private void buildMainPanel() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_SimplePanel");
				
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		successMessage.getElement().setId("successMessages_SuccessMessageDisplay");
		flowPanel.add(errorMessages);
		flowPanel.add(successMessage);
		flowPanel.add(componentMeasureSearch.asWidget());
		flowPanel.add(buttonBar);
		
		mainPanel.add(flowPanel);
	}
	
	public Button getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	public Button getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	public Button getBackButton() {
		return buttonBar.getBackButton();
	}

	public ComponentMeasureSearch getComponentMeasureSearch() {
		return componentMeasureSearch;
	}

	public void setComponentMeasureSearch(ComponentMeasureSearch componentMeasureSearch) {
		this.componentMeasureSearch = componentMeasureSearch;
	}
	
	public void setComponentBusy(boolean busy) {
		componentMeasureSearch.setBusy(busy);
		buttonBar.getSaveButton().setEnabled(!busy);
		buttonBar.getBackButton().setEnabled(!busy);
		buttonBar.getCancelButton().setEnabled(!busy);
	}
}