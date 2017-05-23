package mat.client.myAccount;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.PhoneNumberWidget;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;

/**
 * The Class PersonalInformationView.
 */
public class PersonalInformationView implements PersonalInformationPresenter.Display {
	
	/** The v panel. */
	private FlowPanel vPanel;
	
	/** The heading panel. */
	private ContentWithHeadingWidget headingPanel;
	
	private TextBox firstNameTextBox = new TextBox();
	private TextBox middleNameTextBox = new TextBox();
	private TextBox lastNameTextBox = new TextBox();
	
	/** The title. */
	private TextBox title = new TextBox();
	
	/** The email address. */
	private TextBox emailAddress = new TextBox();
	
	/** The login id. */
	private Label loginId = new Label();
		
	/** The oid. */
	private TextBox oid = new TextBox();
	
	/** The organization. */
	private TextBox organization = new TextBox();
					

	
	/** The phone widget. */
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	
	private Input passwordInput = new Input(InputType.PASSWORD);
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar("personalInfo");
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();

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
	
		vPanel.add(hPanel);
		vPanel.add(new SpacerWidget());
		
		vPanel.add(errorMessages);
		vPanel.add(successMessages);
		Form form = buildView();
		vPanel.add(form);
		vPanel.add(new SpacerWidget());
		
		buttons.getCancelButton().setTitle("Undo");
		buttons.getCancelButton().setText("Undo");
		buttons.getSaveButton().setTitle("Save");
		vPanel.add(buttons);
		
		
		vPanel.setStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(vPanel, "Update Personal Information","PersonalInfo");
		
	}
	
	private Form buildView(){
		
		Form userNameForm = new Form();
		FormGroup firstNameGroup = new FormGroup();
		FormGroup middleNameGroup = new FormGroup();
		FormGroup lastNameGroup = new FormGroup();
		
		FormGroup titleGroup = new FormGroup();
		FormGroup organizationGroup = new FormGroup();
		FormGroup organizationOidGroup = new FormGroup();
		FormGroup emailAddressGroup = new FormGroup();
		FormGroup phoneNumberGroup = new FormGroup();
		
		FormGroup passwordGroup = new FormGroup();
		
		
		firstNameTextBox.setPlaceholder("Enter First Name");
		firstNameTextBox.setTitle("FirstName");
		firstNameTextBox.setId("FirstNameTextBox");
		firstNameTextBox.setMaxLength(100);
		
		FormLabel firstNameLabel = new FormLabel();
		firstNameLabel.setId("firstNameLabel");
		firstNameLabel.setFor("FirstNameTextBox");
		firstNameLabel.setShowRequiredIndicator(true);	
		firstNameLabel.setText("First Name");
		
		firstNameGroup.add(firstNameLabel);
		firstNameGroup.add(firstNameTextBox);
	
		//middleNameTextBox.setPlaceholder("M.I.");
		middleNameTextBox.setTitle("MiddleName");
		middleNameTextBox.setId("MiddleNameTextBox");
		middleNameTextBox.setWidth("32px");
		middleNameTextBox.setMaxLength(1);
		
		FormLabel middleNameLabel = new FormLabel();
		middleNameLabel.setId("MiddleNameLabel");
		middleNameLabel.setFor("MiddleNameTextBox");
		middleNameLabel.setText("M.I.");
		
		
		middleNameGroup.add(middleNameLabel);
		middleNameGroup.add(middleNameTextBox);
		
		lastNameTextBox.setPlaceholder("Enter Last Name");
		lastNameTextBox.setTitle("LastName");
		lastNameTextBox.setId("LastNameTextBox");
		lastNameTextBox.setMaxLength(100);
		
		FormLabel lastNameLabel = new FormLabel();
		lastNameLabel.setId("LastNameLabel");
		lastNameLabel.setFor("LastNameTextBox");
		lastNameLabel.setShowRequiredIndicator(true);	
		lastNameLabel.setText("Last Name");
		
		lastNameGroup.add(lastNameLabel);
		lastNameGroup.add(lastNameTextBox);
		
		Grid nameGrid = new Grid(1,3);
		nameGrid.setWidget(0, 0, firstNameGroup);
		nameGrid.setWidget(0, 1, middleNameGroup);
		nameGrid.setWidget(0, 2, lastNameGroup);
		
		FormLabel titleLabel = new FormLabel();
		titleLabel.setId("titleLabel");
		titleLabel.setFor("titleTextBox");
		titleLabel.setText("Title");
		title.setPlaceholder("Enter Title");
		title.setTitle("Title");
		title.setId("titleTextBox");
		title.setMaxLength(32);
		title.setWidth("200px");
		titleGroup.add(titleLabel);
		titleGroup.add(title);
		
		
		FormLabel organizationLabel = new FormLabel();
		organizationLabel.setText("Organization");
		organizationLabel.setId("OrganizationLabel");
		organizationLabel.setTitle("Organization");
		organizationLabel.setShowRequiredIndicator(true);
		organizationLabel.setFor("OrganizationTextBox");
		organization.setId("OrganizationTextBox");
		organization.setEnabled(false);
		organization.setWidth("200px");
		organization.setTitle("Organization");
		organizationGroup.add(organizationLabel);
		organizationGroup.add(organization);
		
		
		FormLabel organizationOidLabel = new FormLabel();
		organizationOidLabel.setText("Organization OID");
		organizationOidLabel.setId("OrganizationOidLabel");
		organizationOidLabel.setTitle("Organization");
		organizationOidLabel.setShowRequiredIndicator(true);
		organizationOidLabel.setFor("OrganizationOidTextBox");
		oid.setId("OrganizationOidTextBox");
		oid.setEnabled(false);
		oid.setWidth("200px");
		oid.setTitle("Organization OID");
		organizationOidGroup.add(organizationOidLabel);
		organizationOidGroup.add(oid);
		
		
		FormLabel emailAddressLabel = new FormLabel();
		emailAddressLabel.setId("emailAddressLabel");
		emailAddressLabel.setFor("emailTextBox");
		emailAddressLabel.setShowRequiredIndicator(true);	
		emailAddressLabel.setText("E-mail Address");
		emailAddress.setPlaceholder("Enter E-mail Address");
		emailAddress.setTitle("Email Address");
		emailAddress.setId("emailTextBox");
		emailAddress.setWidth("250px");
		emailAddress.setMaxLength(254);
		emailAddressGroup.add(emailAddressLabel);
		emailAddressGroup.add(emailAddress);
		
		
		FormLabel phoneNumberLabel = new FormLabel();
		phoneNumberLabel.setId("phoneNumberLabel");
		phoneNumberLabel.setFor("phoneNumber_TextBox");
		phoneNumberLabel.setShowRequiredIndicator(true);	
		phoneNumberLabel.setText("Phone Number");
		phoneWidget.setTitle("Phone Number");
		
		
		phoneNumberGroup.add(phoneNumberLabel);
		phoneNumberGroup.add(phoneWidget);
		
		
		FormLabel passwordLabel = new FormLabel();
		passwordLabel.setId("passwordLabel");
		passwordLabel.setFor("password_TextBox");
		passwordLabel.setShowRequiredIndicator(true);	
		passwordLabel.setText("Enter existing password to confirm changes");
		passwordInput.setTitle("Enter existing password to confirm changes");
		passwordInput.setId("password_TextBox");
		passwordInput.setWidth("200px");
		passwordGroup.add(passwordLabel);
		passwordGroup.add(passwordInput);
		
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(nameGrid);
		formFieldSet.add(titleGroup);
		formFieldSet.add(organizationGroup);
		formFieldSet.add(organizationOidGroup);
		formFieldSet.add(emailAddressGroup);
		formFieldSet.add(phoneNumberGroup);
		formFieldSet.add(passwordGroup);
		
		
		userNameForm.add(formFieldSet);
		return userNameForm;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
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
		return firstNameTextBox;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getLastName()
	 */
	@Override
	public HasValue<String> getLastName() {
		return lastNameTextBox;
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getMiddleInitial()
	 */
	@Override
	public HasValue<String> getMiddleInitial() {
		return middleNameTextBox;
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
	public MessageAlert getErrorMessageDisplay() {
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

		/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return passwordInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.PersonalInformationPresenter.Display#getLoginId()
	 */
	@Override
	public Label getLoginId() {
		return loginId;
	}
	@Override
	public Input getPasswordInput() {
		return passwordInput;
	}

}
