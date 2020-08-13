package mat.client.admin;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;

/** The Class ManageUsersDetailView. */
public class ManageOrganizationDetailView
implements ManageOrganizationPresenter.DetailDisplay {
	
	
	/** The button bar. */
	private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("Organization");
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	
	// private SecondaryButton deleteButton = new SecondaryButton("Delete User");
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	
	/** The oid. */
	private TextBox oid = new TextBox();
	
	/** The organization. */
	private TextBox organization = new TextBox();
	
	/** The required. */
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	/** The title. */
	//private TextBox title = new TextBox();
	
	
	/** Instantiates a new manage users detail view. */
	public ManageOrganizationDetailView() {
		
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
		
		FlowPanel leftPanel = new FlowPanel();
		leftPanel.addStyleName("floatLeft");
		
		fPanel.add(leftPanel);
		fPanel.add(rightPanel);
		
		SimplePanel clearPanel = new SimplePanel();
		clearPanel.addStyleName("clearBoth");
		fPanel.add(clearPanel);
		
		
		Form form = new Form();
		
		FormGroup organizationGroup = new FormGroup();
		FormGroup orgOidGroup = new FormGroup();
		
		FormLabel organizationLabel = new FormLabel();
		organizationLabel.setId("organizationLabel");
		organizationLabel.setFor("organizationTextBox");
		organizationLabel.setText("Organization");
		organizationLabel.setShowRequiredIndicator(true);
		
		organization.setId("organizationTextBox");
		organization.setWidth("196px");
		organization.setMaxLength(150);
		organization.setTitle("Organization");
		organization.setPlaceholder("Enter Organization Name");
		
		organizationGroup.add(organizationLabel);
		organizationGroup.add(organization);
		
		FormLabel organizationOidLabel = new FormLabel();
		organizationOidLabel.setId("organizationOidLabel");
		organizationOidLabel.setFor("organizationOidTextBox");
		organizationOidLabel.setText("Organization OID");
		organizationOidLabel.setShowRequiredIndicator(true);
		
		oid.setId("organizationTextBox");
		oid.setTitle("Organization OID");
		oid.setWidth("196px");
		oid.setMaxLength(50);
		oid.setPlaceholder("Enter Organization OID");
		orgOidGroup.add(organizationOidLabel);
		orgOidGroup.add(oid);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.add(organizationGroup);
		fieldSet.add(orgOidGroup);
		form.add(fieldSet);
		
		rightPanel.add(form);
		rightPanel.add(new SpacerWidget());
		rightPanel.add(new SpacerWidget());
		
		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		fPanel.add(buttonPanel);
		fPanel.add(new SpacerWidget());
		mainPanel.add(fPanel);
		containerPanel.setContent(mainPanel);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getOid()
	 */
	@Override
	public HasValue<String> getOid() {
		return oid;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getOrganization()
	 */
	@Override
	public HasValue<String> getOrganization() {
		return organization;
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.admin.ManageUsersPresenter.DetailDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.admin.ManageOrganizationPresenter.DetailDisplay#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		containerPanel.setHeading(title, "Manage Organizations");
	}
	
}
