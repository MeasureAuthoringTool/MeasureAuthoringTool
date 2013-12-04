package mat.client.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.ImageResources;
import mat.client.admin.ManageOrganizationSearchModel.Result;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageUsersDetailView.
 */
public class ManageUsersDetailView
	implements ManageUsersPresenter.DetailDisplay {

	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();

	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The name widget. */
	private UserNameWidget nameWidget = new UserNameWidget();

	/** The required. */
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");

	/** The locked label. */
	private HorizontalPanel lockedLabel = new HorizontalPanel();
	
	/** The lock. */
	private FocusableImageButton lock = new FocusableImageButton(ImageResources.INSTANCE.g_lock(), "Account Locked");

	/** The title label. */
	private String titleLabel = "Title";
	
	/** The email address label. */
	private String emailAddressLabel = "E-mail Address";
	
	/** The role label. */
	private String roleLabel = "Role";
	
	/** The organization label. */
	private String organizationLabel = "Organization";
	
	/** The status label. */
	private String statusLabel = "Status";
	
	/** The oid label. */
	private String oidLabel = "Organization OID";
	//private String rootOidLabel = "Root OID";

	/** The login id. */
	private Label loginId = new Label();

	/** The title. */
	private TextBox title = new TextBox();
	
	/** The email address. */
	private TextBox emailAddress = new EmailAddressTextBox();
	
	/** The oid. */
	private TextBox oid = new TextBox();
	//private TextBox rootOid = new TextBox();
	/** The phone widget. */
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	
	/** The org user radio. */
	private RadioButton orgUserRadio = new RadioButton("role", "User");
	
	/** The org admin radio. */
	private RadioButton orgAdminRadio = new RadioButton("role", "Top Level Administrator");
	
	/** The org super user radio. */
	private RadioButton orgSuperUserRadio = new RadioButton("role", "Top Level User");
	
	/** The organization. */
	private ListBoxMVP organizationListBox = new ListBoxMVP();
	
	/** The active status. */
	private RadioButton activeStatus = new RadioButton("status", "Active");
	
	/** The revoked status. */
	private RadioButton revokedStatus = new RadioButton("status", "Revoked");
	
	/** The reset password. */
	private SecondaryButton resetPassword = new SecondaryButton("Reset Password");

	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	//private SecondaryButton deleteButton = new SecondaryButton("Delete User");
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/** The organizations map. */
	private Map<String, Result> organizationsMap = new HashMap<String, Result>();

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
		hPanel.add(userId);
		hPanel.add(loginId);
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
		leftPanel.add(title);
		leftPanel.add(new SpacerWidget());

		leftPanel.add(LabelBuilder.buildRequiredLabel(emailAddress, emailAddressLabel));
		leftPanel.add(emailAddress);
		leftPanel.add(new SpacerWidget());

		leftPanel.add(phoneWidget);
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

		rightPanel.add(LabelBuilder.buildRequiredLabel(organizationListBox, organizationLabel));
		rightPanel.add(organizationListBox);
		organizationListBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				Result organization =  organizationsMap.get(organizationListBox.getValue());
				if (organization != null) {
					oid.setValue(organization.getOid());
					oid.setTitle(organization.getOid());
				} else {
					oid.setValue("");
					oid.setTitle("");
				}
			}
		});
		rightPanel.add(new SpacerWidget());

		rightPanel.add(LabelBuilder.buildRequiredLabel(oid, oidLabel));
		rightPanel.add(oid);
		rightPanel.add(new SpacerWidget());

		/*rightPanel.add(LabelBuilder.buildRequiredLabel(rootOid, rootOidLabel));
		rightPanel.add(rootOid);
		rightPanel.add(new SpacerWidget());*/

		rightPanel.add(LabelBuilder.buildLabel(activeStatus, statusLabel));
		activeStatus.addStyleName("block");
		revokedStatus.addStyleName("block");
		rightPanel.add(activeStatus);
		rightPanel.add(revokedStatus);
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
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setTitle(java.lang.String)
	 */
	@Override 
	public void setTitle(String title) {
		containerPanel.setHeading(title, "Manage Users");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}


	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getFirstName()
	 */
	@Override
	public HasValue<String> getFirstName() {
		return nameWidget.getFirstName();
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getLastName()
	 */
	@Override
	public HasValue<String> getLastName() {
		return nameWidget.getLastName();
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getMiddleInitial()
	 */
	@Override
	public HasValue<String> getMiddleInitial() {
		return nameWidget.getMiddleInitial();
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getTitle()
	 */
	@Override
	public HasValue<String> getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getEmailAddress()
	 */
	@Override
	public HasValue<String> getEmailAddress() {
		return emailAddress;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getPhoneNumber()
	 */
	@Override
	public HasValue<String> getPhoneNumber() {
		return phoneWidget.getPhoneNumber();
	}

	@Override
	public ListBoxMVP getOrganizationListBox() {
		return organizationListBox;
	}
	
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
	public void setListBoxItems(ListBox listBox, List<Result> organizations, String defaultOption) {
		listBox.clear();
		listBox.addItem(defaultOption, "");
		if (organizations != null) {
			for (Result organization : organizations) {
				listBox.addItem(organization.getOrgName(), "" + organization.getId());
			}
		}
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
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setUserLocked(boolean)
	 */
	@Override
	public void setUserLocked(boolean b) {
		MatContext.get().setVisible(lockedLabel, b);
	}


	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setShowUnlockOption(boolean)
	 */
	@Override
	public void setShowUnlockOption(boolean b) {
		MatContext.get().setVisible(resetPassword, b);
	}


	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getResetPasswordButton()
	 */
	@Override
	public HasClickHandlers getResetPasswordButton() {
		return resetPassword;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setUserIsActiveEditable(boolean)
	 */
	@Override
	public void setUserIsActiveEditable(boolean b) {
		activeStatus.setEnabled(b);
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
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getOid()
	 */
	@Override
	public TextBox getOid() {
		return oid;
	}


	/*@Override
	public HasValue<String> getRootOid() {
		return rootOid;
	}*/

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#setShowRevokedStatus(boolean)
	 */
	@Override
	public void setShowRevokedStatus(boolean b) {
		MatContext.get().setVisible(revokedStatus, b);
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getIsRevoked()
	 */
	@Override
	public HasValue<Boolean> getIsRevoked() {
		return revokedStatus;
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
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getLoginId()
	 */
	@Override
	public Label getLoginId() {
		return loginId;
	}

	/**
	 * @return the organizationsMap
	 */
	@Override
	public Map<String, Result> getOrganizationsMap() {
		return organizationsMap;
	}

	/**
	 * @param organizationsMap the organizationsMap to set
	 */
	@Override
	public void setOrganizationsMap(Map<String, Result> organizationsMap) {
		this.organizationsMap = organizationsMap;
	}
}
