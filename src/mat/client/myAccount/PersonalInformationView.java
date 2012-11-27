package mat.client.myAccount;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.PhoneNumberWidget;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.UserNameWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PersonalInformationView implements PersonalInformationPresenter.Display {
	private FlowPanel vPanel;
	private ContentWithHeadingWidget headingPanel;
	private UserNameWidget nameWidget = new UserNameWidget();
	
	private TextBox title = new TextBox();
	private TextBox emailAddress = new EmailAddressTextBox();
	private TextBox oid = new TextBox();
	private TextBox rootOid = new TextBox();
	private TextBox organization = new TextBox();
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();

	public PersonalInformationView() {
		
		
		vPanel = new FlowPanel();

		vPanel.addStyleName("leftAligned");
		
		HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
		vPanel.add(required);
		vPanel.add(new SpacerWidget());
		
		vPanel.add(errorMessages);
		vPanel.add(successMessages);
		vPanel.add(nameWidget);
		vPanel.add(new SpacerWidget());
		
		vPanel.add(buildCell("Title", title, false));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(buildCell("Organization", organization, true));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(buildCell("OID", oid, true));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(buildCell("Root OID", rootOid, true));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(buildCell("E-mail Address", emailAddress, true));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(phoneWidget);
		vPanel.add(new SpacerWidget());
		
		buttons.getCancelButton().setTitle("Undo");
		buttons.getCancelButton().setText("Undo");
		buttons.getSaveButton().setTitle("Save");
		vPanel.add(buttons);
		
		title.setMaxLength(32);
		vPanel.setStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(vPanel, "Update Personal Information","PersonalInfo");
		
	}
	
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	private Panel buildCell(String labelStr, TextBox inputField, boolean required) {
		VerticalPanel panel = new VerticalPanel();
		Widget label;
		if(required) {
			label = LabelBuilder.buildRequiredLabel(inputField, labelStr);
		}
		else {
			label = LabelBuilder.buildLabel(inputField, labelStr);
		}
//		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.add(label);
		panel.add(inputField);
		return panel;
	}

	@Override
	public Widget asWidget() {
		return headingPanel;
	}

	@Override
	public HasValue<String> getFirstName() {
		return nameWidget.getFirstName();
	}

	@Override
	public HasValue<String> getLastName() {
		return nameWidget.getLastName();
	}

	@Override
	public HasValue<String> getMiddleInitial() {
		return nameWidget.getMiddleInitial();
	}


	@Override
	public HasValue<String> getTitle() {
		return title;
	}

	@Override
	public HasValue<String> getEmailAddress() {
		return emailAddress;
	}

	@Override
	public HasValue<String> getPhoneNumber() {
		return phoneWidget.getPhoneNumber();
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttons.getCancelButton();
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public HasValue<String> getOrganisation() {
		return organization;
	}

	@Override
	public HasValue<String> getOID() {
		return oid;
	}

	@Override
	public HasValue<String> getRootOID() {
		return rootOid;
	}


}
