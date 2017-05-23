package mat.client.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.ImageResources;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CustomTextAreaWithMaxLength;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.InformationMessageAlert;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.PhoneNumberWidget;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;

/**
 * The Class ManageUsersDetailView.
 */
public class ManageUsersDetailView
implements ManageUsersPresenter.DetailDisplay {
	
	private TextBox firstNameTextBox = new TextBox();
	private TextBox middleNameTextBox = new TextBox();
	private TextBox lastNameTextBox = new TextBox();
	private FormGroup additionalInfoGroup = new FormGroup();
	
	/** The active status. */
	private RadioButton activeStatus = new RadioButton("status", "Active");
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("user");
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The email address. */
	private TextBox emailAddress = new TextBox();
	
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The lock. */
	private FocusableImageButton lock = new FocusableImageButton(ImageResources.INSTANCE.g_lock(), "Account Locked");
	
	/** The locked label. */
	private HorizontalPanel lockedLabel = new HorizontalPanel();
	
	/** The login id. */
	private Label loginId = new Label();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();

	
	/** The oid. */
	private TextBox oid = new TextBox();
	
	
	
	/** The org admin radio. */
	private RadioButton orgAdminRadio = new RadioButton("role", "Top Level Administrator");
	

	
	/** The organization. */
	private ListBoxMVP organizationListBox = new ListBoxMVP();
	/** The organizations map. */
	private Map<String, Result> organizationsMap = new HashMap<String, Result>();
	
	/** The org super user radio. */
	private RadioButton orgSuperUserRadio = new RadioButton("role", "Top Level User");
	
	/** The org user radio. */
	private RadioButton orgUserRadio = new RadioButton("role", "User");
	
	
	/** The phone widget. */
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	
	/** The required. */
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	
	/** The reset password. */
	private Button resetPassword = new Button("Reset Password");
	
	/** The revoked status. */
	private RadioButton revokedStatus = new RadioButton("status", "Revoked");
	
	/* The revoked date, past or future */
	private Label revokeDate = new Label();
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	
	/** The title. */
	private TextBox title = new TextBox();
	
	private static final int ADD_INFO_MAX_LENGTH = 2000;
	
	private CustomTextAreaWithMaxLength addInfoArea = new CustomTextAreaWithMaxLength(ADD_INFO_MAX_LENGTH);
	
	private MessageAlert informationMessage = new InformationMessageAlert();


	/**
	 * Instantiates a new manage users detail view.
	 */
	public ManageUsersDetailView() {
		lockedLabel.getElement().setId("lockedLabel_HorizontalPanel");
		
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		
		FlowPanel fPanel = new FlowPanel();
		fPanel.setHeight("100%");
		fPanel.add(required);
		fPanel.add(new SpacerWidget());
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(informationMessage);
		fPanel.add(hPanel);
		fPanel.add(new SpacerWidget());
		fPanel.add(successMessages);
		fPanel.add(errorMessages);
		
		FlowPanel rightPanel = new FlowPanel();
		rightPanel.addStyleName("floatLeft");
		rightPanel.addStyleName("manageUserRightPanel");
		FlowPanel leftPanel = new FlowPanel();
		leftPanel.addStyleName("floatLeft");
		
		fPanel.add(leftPanel);
		fPanel.add(rightPanel);
		
		SimplePanel clearPanel = new SimplePanel();
		clearPanel.addStyleName("clearBoth");
		fPanel.add(clearPanel);
		
		Form leftSideForm = buildLeftSideForm();
		
		leftPanel.add(leftSideForm);
		leftPanel.add(new SpacerWidget());
		
				
		lockedLabel.addStyleName("floatRight");
		lockedLabel.addStyleName("bold");
		lockedLabel.add(lock);
		lockedLabel.add(new Label("Account Locked"));
		rightPanel.add(lockedLabel);
		
		Form rightSideForm = buildRightSideForm();
		
		rightPanel.add(rightSideForm);
		rightPanel.add(new SpacerWidget());
		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		fPanel.add(buttonPanel);
		mainPanel.add(fPanel);
		containerPanel.setContent(mainPanel);
		
		addEventHandlers();
	}
	
	
	private Form buildRightSideForm(){
		Form rightSideForm = new Form();
		FormGroup roleGroup = new FormGroup();
		FormGroup organizationGroup = new FormGroup();
		FormGroup oidGroup = new FormGroup();
		FormGroup statusGroup = new FormGroup();
		FormGroup resetButtonGroup = new FormGroup();
		
		FormLabel roleLabel = new FormLabel();
		roleLabel.setText("Role");
		roleLabel.setShowRequiredIndicator(true);
		roleLabel.setId("roleLabel");
		roleLabel.setFor("roleRadioPanel");
		FlowPanel roleRadioPanel = new FlowPanel();
		SimplePanel radioPanel1 = new SimplePanel();
		SimplePanel radioPanel2 = new SimplePanel();
		SimplePanel radioPanel3 = new SimplePanel();
		orgUserRadio.setTitle("User");
		radioPanel1.add(orgUserRadio);
		orgSuperUserRadio.setTitle("Top Level User");
		orgAdminRadio.setTitle("Top Level Administrator");
		radioPanel2.add(orgSuperUserRadio);
		radioPanel3.add(orgAdminRadio);
		roleRadioPanel.add(radioPanel1);
		roleRadioPanel.add(radioPanel2);
		roleRadioPanel.add(radioPanel3);
		roleRadioPanel.getElement().setAttribute("id", "roleRadioPanel");
		
		roleGroup.add(roleLabel);
		roleGroup.add(roleRadioPanel);
		
		FormLabel organizationLabel = new FormLabel();
		organizationLabel.setText("Organization");
		organizationLabel.setTitle("Organization");
		organizationLabel.setId("OrganizationLabel");
		organizationLabel.setFor("OrganizationListBox");
		organizationLabel.setShowRequiredIndicator(true);
		organizationListBox.getElement().setAttribute("id", "OrganizationListBox");
		organizationGroup.add(organizationLabel);
		organizationGroup.add(organizationListBox);
		
		FormLabel oidLabel = new FormLabel();
		Label invisibleLabel = new Label();
		invisibleLabel.setText("OrganizationOidUpdated");
		invisibleLabel.getElement().setAttribute("id", "OrganizationOidUpdated");
		invisibleLabel.getElement().setAttribute("for", "OrganizationOidUpdated");
		invisibleLabel.setVisible(false);
		oidLabel.setText("Organization OID");
		oidLabel.setTitle("Organization OID");
		oidLabel.setId("OIDLabel");
		oidLabel.setFor("OIDTextBox");
		oidLabel.setShowRequiredIndicator(true);
		oid.setWidth("196px");
		oid.setEnabled(false);
		oidGroup.add(oidLabel);
		oidGroup.add(invisibleLabel);
		oidGroup.add(oid);
		
		FormLabel statusLabel = new FormLabel();
		statusLabel.setText("Status");
		statusLabel.setTitle("Status");
		statusLabel.setId("statusLabel");
		statusLabel.setFor("StatusGroupPanel");
		
		
		HorizontalPanel hzPanel = new HorizontalPanel();
		hzPanel.getElement().setId("HorizontalPanel_UserExpiryDate");
		
		activeStatus.addStyleName("userStatus");
		revokedStatus.addStyleName("userStatus");
		revokeDate.addStyleName("revokeDate");
		hzPanel.add(activeStatus);
		
		HorizontalPanel hzRevokePanel = new HorizontalPanel();
		hzRevokePanel.add(revokedStatus);
		hzRevokePanel.add(revokeDate);
		
		statusGroup.add(statusLabel);
		statusGroup.add(hzPanel);
		statusGroup.add(hzRevokePanel);
		
		resetPassword.setTitle("Reset Password");
		//resetPassword.setType(ButtonType.INFO);
		resetButtonGroup.add(resetPassword);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.add(roleGroup);
		fieldSet.add(organizationGroup);
		fieldSet.add(oidGroup);
		fieldSet.add(statusGroup);
		fieldSet.add(resetButtonGroup);
		
		rightSideForm.add(fieldSet);
		return rightSideForm;
	}


	/**
	 * 
	 */
	private void addEventHandlers() {
		organizationListBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				Result organization =  organizationsMap.get(organizationListBox.getValue());
				if (organization != null) {
					oid.setValue(organization.getOid());
					oid.setTitle(organization.getOid());
					
					oid.getElement().setAttribute("id", "orgTextBox");
					oid.getElement().setAttribute("aria-role", "Panel");
					oid.getElement().setAttribute("aria-live", "assertive");
					oid.getElement().setAttribute("aria-atomic", "true");
					oid.getElement().setAttribute("aria-relevant", "all");
					oid.getElement().setAttribute("role", "alert");
					oid.getElement().setAttribute("aria-labelledby", "OrganizationOidUpdated");
				} else {
					oid.setValue("");
					oid.setTitle("");
				}
			}
		});
		
		
		revokedStatus.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub
				if(event.getValue()) {
					organizationListBox.setEnabled(false);
					organizationListBox.setValue("");
					oid.setValue("");
					oid.setEnabled(false);
				}
			}
		});
		
		activeStatus.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()) {
					organizationListBox.setEnabled(true);
					oid.setEnabled(true);
				}
			}
		});
	}
	
	
	private Form buildLeftSideForm(){
		
		Form userNameForm = new Form();
		FormGroup firstNameGroup = new FormGroup();
		FormGroup middleNameGroup = new FormGroup();
		FormGroup lastNameGroup = new FormGroup();
		
		FormGroup titleGroup = new FormGroup();
		FormGroup emailAddressGroup = new FormGroup();
		FormGroup phoneNumberGroup = new FormGroup();
		
		
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
		//titleLabel.setShowRequiredIndicator(true);	
		titleLabel.setText("Title");
		title.setPlaceholder("Enter Title");
		title.setTitle("Title");
		title.setId("titleTextBox");
		
		title.setWidth("196px");
		title.setMaxLength(32);
		
		titleGroup.add(titleLabel);
		titleGroup.add(title);
		
		
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
		
		
		FormLabel additionaInfoLabel = new FormLabel();
		additionaInfoLabel.setId("addInfoLabel");
		additionaInfoLabel.setFor("addInfoContent");
		additionaInfoLabel.setText("Notes");
		addInfoArea.getElement().setAttribute("maxlength", "250");
		addInfoArea.getElement().setAttribute("id", "addInfoContent");
		addInfoArea.setText("");
		addInfoArea.setHeight("80px");
		addInfoArea.setWidth("250px");
		
		additionalInfoGroup.add(additionaInfoLabel);
		additionalInfoGroup.add(addInfoArea);
		
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(nameGrid);
		formFieldSet.add(titleGroup);
		formFieldSet.add(emailAddressGroup);
		formFieldSet.add(phoneNumberGroup);
		formFieldSet.add(additionalInfoGroup);
		
		
		userNameForm.add(formFieldSet);
		return userNameForm;
	}
	
	

	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getEmailAddress()
	 */
	@Override
	public HasValue<String> getEmailAddress() {
		return emailAddress;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getFirstName()
	 */
	@Override
	public HasValue<String> getFirstName() {
		return firstNameTextBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getIsActive()
	 */
	@Override
	public HasValue<Boolean> getIsActive() {
		return activeStatus;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getIsOrgUser()
	 */
	@Override
	public HasValue<Boolean> getIsOrgUser() {
		return orgUserRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getIsRevoked()
	 */
	@Override
	public HasValue<Boolean> getIsRevoked() {
		return revokedStatus;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getLastName()
	 */
	@Override
	public HasValue<String> getLastName() {
		return lastNameTextBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getLoginId()
	 */
	@Override
	public Label getLoginId() {
		return loginId;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getMiddleInitial()
	 */
	@Override
	public HasValue<String> getMiddleInitial() {
		return middleNameTextBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getOid()
	 */
	@Override
	public TextBox getOid() {
		return oid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getOrganizationListBox()
	 */
	@Override
	public ListBoxMVP getOrganizationListBox() {
		return organizationListBox;
	}
	
	/** Gets the organizations map.
	 * 
	 * @return the organizationsMap */
	@Override
	public Map<String, Result> getOrganizationsMap() {
		return organizationsMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getPhoneNumber()
	 */
	@Override
	public HasValue<String> getPhoneNumber() {
		return phoneWidget.getPhoneNumber();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getResetPasswordButton()
	 */
	@Override
	public HasClickHandlers getResetPasswordButton() {
		return resetPassword;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getRole()
	 */
	@Override
	public HasValue<String> getRole() {
		return new HasValue<String>() {
			
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<String> handler) {
				return null;
			}
			
			@Override
			public void fireEvent(GwtEvent<?> event) {
			}
			
			@Override
			public String getValue() {
				if (orgAdminRadio.getValue().booleanValue()) {
					return "1";
				} else if (orgSuperUserRadio.getValue().booleanValue()) {
					return "2";
				} else {
					return "3";
				}
			}
			
			@Override
			public void setValue(String value) {
				setValue(value, false);
			}
			
			@Override
			public void setValue(String value, boolean fireEvents) {
				if ("1".equals(value)) {
					orgAdminRadio.setValue(Boolean.TRUE);
					orgSuperUserRadio.setValue(Boolean.FALSE);
					orgUserRadio.setValue(Boolean.FALSE);
				} else if ("2".equals(value)) {
					orgSuperUserRadio.setValue(Boolean.TRUE);
					orgAdminRadio.setValue(Boolean.FALSE);
					orgUserRadio.setValue(Boolean.FALSE);
				} else {
					orgUserRadio.setValue(Boolean.TRUE);
					orgSuperUserRadio.setValue(Boolean.FALSE);
					orgAdminRadio.setValue(Boolean.FALSE);
				}
			}
		};
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}
	
	
	@Override
	public MessageAlert getInformationMessageDisplay(){
		return informationMessage;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getTitle()
	 */
	@Override
	public HasValue<String> getTitle() {
		return title;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#populateOrganizations(java.util.List)
	 */
	@Override
	public void populateOrganizations(List<Result> organizations) {
		setListBoxItems(organizationListBox, organizations, MatContext.PLEASE_SELECT);
	}
	
	
	/**
	 * Sets the list box items.
	 *
	 * @param listBox the list box
	 * @param organizations the organizations
	 * @param defaultOption the default option
	 */
	public void setListBoxItems(ListBoxMVP listBox, List<Result> organizations, String defaultOption) {
		listBox.clear();
		listBox.addItem(defaultOption, "");
		if (organizations != null) {
			for (Result organization : organizations) {
				// truncate the org names to 60 chars, so fields on screen don't wrap
				String orgName = organization.getOrgName();
				if (orgName.length() > 60) {
					orgName = organization.getOrgName().substring(0,60);
				}
				listBox.insertItem(orgName, "" + organization.getId(), organization.getOrgName());
			}
		}
	}
	
	
	/*@Override
	public HasValue<String> getRootOid() {
		return rootOid;
	}*/
	
	/** Sets the organizations map.
	 * 
	 * @param organizationsMap the organizationsMap to set */
	@Override
	public void setOrganizationsMap(Map<String, Result> organizationsMap) {
		this.organizationsMap = organizationsMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setShowRevokedStatus(boolean)
	 */
	@Override
	public void setShowRevokedStatus(boolean b) {
		MatContext.get().setVisible(revokedStatus, b);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setShowUnlockOption(boolean)
	 */
	@Override
	public void setShowUnlockOption(boolean b) {
		MatContext.get().setVisible(resetPassword, b);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		containerPanel.setHeading(title, "Manage Users");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setUserIsActiveEditable(boolean)
	 */
	@Override
	public void setUserIsActiveEditable(boolean b) {
		activeStatus.setEnabled(b);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setUserLocked(boolean)
	 */
	@Override
	public void setUserLocked(boolean b) {
		MatContext.get().setVisible(lockedLabel, b);
	}

	@Override
	public CustomTextAreaWithMaxLength getAddInfoArea() {
		return addInfoArea;
	}

	@Override
	public void setAddInfoArea(CustomTextAreaWithMaxLength addInfoArea) {
		this.addInfoArea = addInfoArea;
	}
	
	@Override
	public Label getRevokeDate() {
		return revokeDate;
	}
	
	@Override
	public void setRevokeDate(Label revokeDate) {
		this.revokeDate = revokeDate;
	}
	
	@Override
	public void setShowAdminNotes(boolean b) {
		additionalInfoGroup.setVisible(b);
	}
}