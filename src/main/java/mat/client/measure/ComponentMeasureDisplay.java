package mat.client.measure;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.BackSaveCancelButtonBar;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessagePanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class ComponentMeasureDisplay implements BaseDisplay {
	
	private SimplePanel mainPanel = new SimplePanel();
	FlowPanel flowPanel = new FlowPanel();
		
	private MessagePanel messagePanel = new MessagePanel();

	private BackSaveCancelButtonBar buttonBar = new BackSaveCancelButtonBar("componentMeasures");
	
	private ComponentMeasureSearch componentMeasureSearch = new ComponentMeasureSearch(messagePanel.getErrorMessageAlert(), messagePanel.getSuccessMessageAlert(), 15);
	HorizontalPanel breadCrumbPanel = new HorizontalPanel();
	Label breadcrumb = new Label("Component Measures > Edit Component Measures");
		
	public ComponentMeasureDisplay() {
		buildMainPanel();
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return messagePanel.getErrorMessageAlert();
	}
	
	public MessageAlert getSuccessMessage() {
		return messagePanel.getSuccessMessageAlert();
	}

	public void setSuccessMessage(MessageAlert successMessage) {
		this.messagePanel.setSuccessMessageAlert(successMessage);
	}

	private void buildMainPanel() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_SimplePanel");
				
		messagePanel.getErrorMessageAlert().getElement().setId("errorMessages_ErrorMessageDisplay");
		messagePanel.getSuccessMessageAlert().getElement().setId("successMessages_SuccessMessageDisplay");
		messagePanel.setWidth("100%");
		
		flowPanel.add(breadCrumbPanel);
		flowPanel.add(messagePanel);
		flowPanel.add(componentMeasureSearch.asWidget());
		flowPanel.add(buttonBar);
		setUpBreadCrump();
		mainPanel.add(flowPanel);
	}
	
	private void setUpBreadCrump() {
		breadcrumb.setStyleName("breadcrum");
		breadCrumbPanel.add(breadcrumb);
		breadCrumbPanel.setStyleName("breadcrumPaddign");
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

	public HorizontalPanel getBreadCrumbPanel() {
		return breadCrumbPanel;
	}

	public void setBreadCrumbPanel(HorizontalPanel breadCrumbPanel) {
		this.breadCrumbPanel = breadCrumbPanel;
	}

	public MessagePanel getMessagePanel() {
		return messagePanel;
	}

	public void setMessagePanel(MessagePanel messagePanel) {
		this.messagePanel = messagePanel;
	}
}