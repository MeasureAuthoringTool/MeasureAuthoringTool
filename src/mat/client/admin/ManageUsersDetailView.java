package mat.client.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mat.client.ImageResources;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.InformationMessageDisplayInterface;
import mat.client.shared.InformationMessageDisplay;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.CustomTextAreaWithMaxLength;
import mat.client.shared.PhoneNumberWidget;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.UserNameWidget;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageUsersDetailView.
 */
public class ManageUsersDetailView
implements ManageUsersPresenter.DetailDisplay {
	
	/** The active status. */
	private RadioButton activeStatus = new RadioButton("status", "Active");
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The email address. */
	private TextBox emailAddress = new EmailAddressTextBox();
	
	/** The email address label. */
	private String emailAddressLabel = "E-mail Address";
	
	//private SecondaryButton deleteButton = new SecondaryButton("Delete User");
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The lock. */
	private FocusableImageButton lock = new FocusableImageButton(ImageResources.INSTANCE.g_lock(), "Account Locked");
	
	/** The locked label. */
	private HorizontalPanel lockedLabel = new HorizontalPanel();
	
	/** The login id. */
	private Label loginId = new Label();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The name widget. */
	private UserNameWidget nameWidget = new UserNameWidget();
	
	/** The oid. */
	private TextBox oid = new TextBox();
	
	/** The oid label. */
	private String oidLabel = "Organization OID";
	//private String rootOidLabel = "Root OID";
	
	/** The org admin radio. */
	private RadioButton orgAdminRadio = new RadioButton("role", "Top Level Administrator");
	
	/** The organization label. */
	private String organizationLabel = "Organization";
	
	/** The organization. */
	private ListBoxMVP organizationListBox = new ListBoxMVP();
	/** The organizations map. */
	private Map<String, Result> organizationsMap = new HashMap<String, Result>();
	
	/** The org super user radio. */
	private RadioButton orgSuperUserRadio = new RadioButton("role", "Top Level User");
	
	/** The org user radio. */
	private RadioButton orgUserRadio = new RadioButton("role", "User");
	
	//private TextBox rootOid = new TextBox();
	/** The phone widget. */
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	
	/** The required. */
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	
	/** The reset password. */
	private SecondaryButton resetPassword = new SecondaryButton("Reset Password");
	
	/** The revoked status. */
	private RadioButton revokedStatus = new RadioButton("status", "Revoked");
	
	/* The revoked date, past or future */
	private Label revokeDate = new Label();
	
	/** The role label. */
	private String roleLabel = "Role";
	
	/** The status label. */
	private String statusLabel = "Status";
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The title. */
	private TextBox title = new TextBox();
	
	/** The title label. */
	private String titleLabel = "Title";
	
//	private String addInfoLabel = "Additional Information";
	
	
	private static final int ADD_INFO_MAX_LENGTH = 2000;
	
	private CustomTextAreaWithMaxLength addInfoArea = new CustomTextAreaWithMaxLength(ADD_INFO_MAX_LENGTH);
	
	
	private Label addInfoLabel = new Label("Notes");
	
	//Label expLabel = new Label("Expires");
	
	InformationMessageDisplay informationMessage = new InformationMessageDisplay();


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
		HTML userId = new HTML("&nbsp; User ID :&nbsp; ");
//		hPanel.add(userId);
//		hPanel.add(loginId);
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
		
		leftPanel.add(nameWidget);
		leftPanel.add(new SpacerWidget());
		
		leftPanel.add(LabelBuilder.buildLabel(title, titleLabel));
		title.setTitle("Title");
		leftPanel.add(title);
		leftPanel.add(new SpacerWidget());
		
		emailAddress.setTitle("Email Address");
		leftPanel.add(LabelBuilder.buildRequiredLabel(emailAddress, emailAddressLabel));
		leftPanel.add(emailAddress);
		leftPanel.add(new SpacerWidget());
		
		leftPanel.add(phoneWidget);
		leftPanel.add(new SpacerWidget());
		
		addInfoArea.getElement().setAttribute("maxlength", "250");
		addInfoArea.setText("");
		addInfoArea.setHeight("80px");
		addInfoArea.setWidth("250px");
		addInfoLabel.getElement().setId("Admin_AddtionalInfo_Label");
		leftPanel.add(addInfoLabel);
		addInfoArea.setTitle("Notes");
		leftPanel.add(addInfoArea);
		leftPanel.add(new SpacerWidget());
		
		lockedLabel.addStyleName("floatRight");
		lockedLabel.addStyleName("bold");
		lockedLabel.add(lock);
		lockedLabel.add(new Label("Account Locked"));
		rightPanel.add(lockedLabel);
		FlowPanel roleRadioPanel = new FlowPanel();
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(roleRadioPanel, roleLabel));
		
		SimplePanel radioPanel1 = new SimplePanel();
		SimplePanel radioPanel2 = new SimplePanel();
		SimplePanel radioPanel3 = new SimplePanel();
		radioPanel1.add(orgUserRadio);
		radioPanel2.add(orgSuperUserRadio);
		radioPanel3.add(orgAdminRadio);
		roleRadioPanel.add(radioPanel1);
		roleRadioPanel.add(radioPanel2);
		roleRadioPanel.add(radioPanel3);
		rightPanel.add(roleRadioPanel);
		rightPanel.add(new SpacerWidget());
		
		rightPanel.add(LabelBuilder.buildLabel(organizationListBox, organizationLabel));
		rightPanel.add(organizationListBox);
		rightPanel.add(new SpacerWidget());
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
		
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("OrganizationOidUpdated"),
				"OrganizationOidUpdated");
		rightPanel.add(invisibleLabel);
		rightPanel.add(LabelBuilder.buildLabel(oid, oidLabel));
		rightPanel.add(oid);
		rightPanel.add(new SpacerWidget());
		
		/*rightPanel.add(LabelBuilder.buildRequiredLabel(rootOid, rootOidLabel));
		rightPanel.add(rootOid);
		rightPanel.add(new SpacerWidget());*/
		HorizontalPanel hzPanel = new HorizontalPanel();
		hzPanel.getElement().setId("HorizontalPanel_UserExpiryDate");
		//expLabel.getElement().setId("UserPasswordExpiry_Label");
		rightPanel.add(LabelBuilder.buildLabel(activeStatus, statusLabel));
		activeStatus.addStyleName("userStatus");
		revokedStatus.addStyleName("userStatus");
		revokeDate.addStyleName("revokeDate");
		SimplePanel sPanel = new SimplePanel();
		sPanel.setWidth("5px");
		hzPanel.add(activeStatus);
		hzPanel.add(sPanel);
		//hzPanel.add(expLabel);
		hzPanel.addStyleName("inline");
		rightPanel.add(hzPanel);
		
		HorizontalPanel hzRevokePanel = new HorizontalPanel();
		hzRevokePanel.add(revokedStatus);
		hzRevokePanel.add(sPanel);
		//hzRevokePanel.setWidth("35%");
		//hzPanel.addStyleName("inline");
		hzRevokePanel.add(revokeDate);
		rightPanel.add(hzRevokePanel);
		activeStatus.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()) {
					organizationListBox.setEnabled(true);
					oid.setEnabled(true);
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
		
		rightPanel.add(new SpacerWidget());
		
		rightPanel.add(resetPassword);
		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		//deleteButton.addStyleName("floatRight");
		//fPanel.add(deleteButton);
		fPanel.add(buttonPanel);
		mainPanel.add(fPanel);
		containerPanel.setContent(mainPanel);
		//containerPanel.setEmbeddedLink("Manage Users");
		title.setWidth("196px");
		//organization.setWidth("196px");
		oid.setWidth("196px");
		//rootOid.setWidth("196px");
		oid.setEnabled(false);
		//oid.setMaxLength(50);
		//rootOid.setMaxLength(50);
		title.setMaxLength(32);
		//organization.setMaxLength(80);
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
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getFirstName()
	 */
	@Override
	public HasValue<String> getFirstName() {
		return nameWidget.getFirstName();
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
		return nameWidget.getLastName();
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
		return nameWidget.getMiddleInitial();
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
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	
	@Override
	public InformationMessageDisplayInterface getInformationMessageDisplay(){
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
	
	//@Override
	//public HasClickHandlers getDeleteUserButton() {
	//	return deleteButton;
	//}
	
	//@Override
	//public void setUserIsDeletable(boolean b) {
	//	MatContext.get().setVisible(deleteButton,b);
	//}
	
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
		MatContext.get().setVisible(addInfoLabel, b);
		MatContext.get().setVisible(addInfoArea, b);
	}


//	@Override
//	public Label getExpLabel() {
//		return expLabel;
//	}
}