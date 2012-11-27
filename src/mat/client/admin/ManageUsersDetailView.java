package mat.client.admin;

import mat.client.ImageResources;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.LabelBuilder;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageUsersDetailView 
	implements ManageUsersPresenter.DetailDisplay{
	
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();

	private SimplePanel mainPanel = new SimplePanel();
	private UserNameWidget nameWidget = new UserNameWidget();
	
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	
	private HorizontalPanel lockedLabel = new HorizontalPanel();
	private FocusableImageButton lock = new FocusableImageButton(ImageResources.INSTANCE.g_lock(),"Account Locked");
	private String titleLabel = "Title";
	private String emailAddressLabel = "E-mail Address";
	private String roleLabel = "Role";
	private String organizationLabel = "Organization";
	private String statusLabel = "Status";
	private String oidLabel = "OID";
	private String rootOidLabel = "Root OID";
	
	private TextBox title = new TextBox();
	private TextBox emailAddress = new EmailAddressTextBox();
	private TextBox oid = new TextBox();
	private TextBox rootOid = new TextBox();
	private PhoneNumberWidget phoneWidget = new PhoneNumberWidget();
	private RadioButton orgUserRadio = new RadioButton("role", "User");
	private RadioButton orgAdminRadio = new RadioButton("role", "Top Level Administrator");
	private RadioButton orgSuperUserRadio = new RadioButton("role", "Top Level User");
	private TextBox organization = new TextBox();
	private RadioButton activeStatus = new RadioButton("status", "Active");
	private RadioButton revokedStatus = new RadioButton("status", "Revoked");
	private SecondaryButton resetPassword = new SecondaryButton("Reset Password");
	
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	//private SecondaryButton deleteButton = new SecondaryButton("Delete User");
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();

	public ManageUsersDetailView() {
		
		
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");


		FlowPanel fPanel = new FlowPanel();
		fPanel.setHeight("100%");
		fPanel.add(required);
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
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(roleRadioPanel,roleLabel));
		
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
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(organization, organizationLabel));
		rightPanel.add(organization);
		rightPanel.add(new SpacerWidget());
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(oid, oidLabel));
		rightPanel.add(oid);
		rightPanel.add(new SpacerWidget());
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(rootOid, rootOidLabel));
		rightPanel.add(rootOid);
		rightPanel.add(new SpacerWidget());
		
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
		organization.setWidth("196px");
		oid.setWidth("196px");
		rootOid.setWidth("196px");
		
		oid.setMaxLength(50);
		rootOid.setMaxLength(50);
		title.setMaxLength(32);
		organization.setMaxLength(80);
	}

	@Override 
	public void setTitle(String title) {
		containerPanel.setHeading(title,"Manage Users");
	}
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
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
	public HasValue<String> getOrganization() {
		return organization;
	}

	@Override
	public HasValue<Boolean> getIsActive() {
		return activeStatus;
	}

	@Override
	public HasValue<Boolean> getIsOrgUser() {
		return orgUserRadio;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void setUserLocked(boolean b) {
		MatContext.get().setVisible(lockedLabel,b);
	}


	@Override
	public void setShowUnlockOption(boolean b) {
		MatContext.get().setVisible(resetPassword, b);
	}


	@Override
	public HasClickHandlers getResetPasswordButton() {
		return resetPassword;
	}

	@Override
	public void setUserIsActiveEditable(boolean b) {
		activeStatus.setEnabled(b);
	}


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
				if(orgAdminRadio.getValue().booleanValue()) {
					return "1";
				}
				else if(orgSuperUserRadio.getValue().booleanValue()) {
					return "2";
				}
				else {
					return "3";
				}
			}

			@Override
			public void setValue(String value) {
				setValue(value, false);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				if("1".equals(value)) {
					orgAdminRadio.setValue(Boolean.TRUE);
					orgSuperUserRadio.setValue(Boolean.FALSE);
					orgUserRadio.setValue(Boolean.FALSE);
				}
				else if("2".equals(value)) {
					orgSuperUserRadio.setValue(Boolean.TRUE);
					orgAdminRadio.setValue(Boolean.FALSE);
					orgUserRadio.setValue(Boolean.FALSE);
				}
				else {
					orgUserRadio.setValue(Boolean.TRUE);
					orgSuperUserRadio.setValue(Boolean.FALSE);
					orgAdminRadio.setValue(Boolean.FALSE);
				}
			}
			
		};
	}


	@Override
	public HasValue<String> getOid() {
		return oid;
	}


	@Override
	public HasValue<String> getRootOid() {
		return rootOid;
	}

	@Override
	public void setShowRevokedStatus(boolean b) {
		MatContext.get().setVisible(revokedStatus,b);
	}

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

	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	
}
