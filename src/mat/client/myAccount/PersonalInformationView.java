package mat.client.myAccount;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.PasswordEditInfoWidget;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PersonalInformationView.
 */
public class PersonalInformationView implements PersonalInformationPresenter.Display {
	
	/** The v panel. */
	private FlowPanel vPanel;
	
	/** The heading panel. */
	private ContentWithHeadingWidget headingPanel;
	
	/** The name widget. */
	private UserNameWidget nameWidget = new UserNameWidget();
	
	/** The title. */
	private TextBox title = new TextBox();
	
	/** The email address. */
	private TextBox emailAddress = new EmailAddressTextBox();
	
	/** The login id. */
	private Label loginId = new Label();
		
	/** The oid. */
	private TextBox oid = new TextBox();
	
	//private TextBox rootOid = new TextBox();
	
	/** The organization. */
	private TextBox organization = new TextBox();
					
	/** The password. */
	private TextBox password = new TextBox();
	
	/** The phone widget. */
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	
	/** The password edit info widget. */
	private PasswordEditInfoWidget passwordEditInfoWidget = new PasswordEditInfoWidget();
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar("personalInfo");
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();

	/**
	 * Instantiates a new personal information view.
	 */
	public PersonalInformationView() {
		
		
		vPanel = new FlowPanel();

		vPanel.addStyleName("leftAligned");
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
		vPanel.add(required);
		vPanel.add(new SpacerWidget());
		
		//HTML userId = new HTML("&nbsp; User ID :&nbsp; ");
		//hPanel.add(userId);
		//hPanel.add(loginId);
		vPanel.add(hPanel);
		vPanel.add(new SpacerWidget());
		
		vPanel.add(errorMessages);
		vPanel.add(successMessages);
		vPanel.add(nameWidget);
		vPanel.add(new SpacerWidget());
		
		//loginId.setEnabled(false);
		//vPanel.add(buildCell("User ID", loginId, false));
		//vPanel.add(new SpacerWidget());
		
		title.setWidth("200px");
		vPanel.add(buildCell("Title", title, false));
		vPanel.add(new SpacerWidget());
		
		organization.setEnabled(false);
		organization.setWidth("200px");
		vPanel.add(buildCell("Organization", organization, true));		
		vPanel.add(new SpacerWidget());
		
		oid.setEnabled(false);
		oid.setWidth("200px");
		vPanel.add(buildCell("Organization OID", oid, true));		
		vPanel.add(new SpacerWidget());
		
		/*vPanel.add(buildCell("Root OID", rootOid, true));
		vPanel.add(new SpacerWidget());*/
		
		vPanel.add(buildCell("E-mail Address", emailAddress, true));
		vPanel.add(new SpacerWidget());
		
		vPanel.add(phoneWidget);
		vPanel.add(new SpacerWidget());
		
		vPanel.add(passwordEditInfoWidget);
		vPanel.add(new SpacerWidget());
		
		buttons.getCancelButton().setTitle("Undo");
		buttons.getCancelButton().setText("Undo");
		buttons.getSaveButton().setTitle("Save");
		vPanel.add(buttons);
		
		title.setMaxLength(32);
		vPanel.setStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(vPanel, "Update Personal Information","PersonalInfo");
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/**
	 * Builds the cell.
	 * 
	 * @param labelStr
	 *            the label str
	 * @param inputField
	 *            the input field
	 * @param required
	 *            the required
	 * @return the panel
	 */
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

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return headingPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getFirstName()
	 */
	@Override
	public HasValue<String> getFirstName() {
		return nameWidget.getFirstName();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getLastName()
	 */
	@Override
	public HasValue<String> getLastName() {
		return nameWidget.getLastName();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getMiddleInitial()
	 */
	@Override
	public HasValue<String> getMiddleInitial() {
		return nameWidget.getMiddleInitial();
	}


	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getTitle()
	 */
	@Override
	public HasValue<String> getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getEmailAddress()
	 */
	@Override
	public HasValue<String> getEmailAddress() {
		return emailAddress;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getPhoneNumber()
	 */
	@Override
	public HasValue<String> getPhoneNumber() {
		return phoneWidget.getPhoneNumber();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttons.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getOrganisation()
	 */
	@Override
	public TextBox getOrganisation() {
		return organization;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getOID()
	 */
	@Override
	public TextBox getOID() {
		return oid;
	}

	/*@Override
	public HasValue<String> getRootOID() {
		return rootOid;
	}*/

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return passwordEditInfoWidget.getPassword();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getLoginId()
	 */
	@Override
	public Label getLoginId() {
		return loginId;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getPasswordEditInfoWidget()
	 */
	@Override
	public PasswordEditInfoWidget getPasswordEditInfoWidget() {
		return passwordEditInfoWidget;
	}
	

}
