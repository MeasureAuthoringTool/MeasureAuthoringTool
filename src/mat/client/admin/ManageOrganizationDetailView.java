package mat.client.admin;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/** The Class ManageUsersDetailView. */
public class ManageOrganizationDetailView
implements ManageOrganizationPresenter.DetailDisplay {
	
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("Organization");
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	
	// private SecondaryButton deleteButton = new SecondaryButton("Delete User");
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The login id. */
	private Label loginId = new Label();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	
	/** The oid. */
	private TextBox oid = new TextBox();
	
	/** The oid label. */
	private String oidLabel = "Organization OID";
	// private String rootOidLabel = "Root OID";
	
	/** The organization. */
	private TextBox organization = new TextBox();
	
	/** The organization label. */
	private String organizationLabel = "Organization";
	
	/** The required. */
	private HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	/** The title. */
	private TextBox title = new TextBox();
	
	
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
		rightPanel.add(LabelBuilder.buildRequiredLabel(organization, organizationLabel));
		rightPanel.add(organization);
		organization.setTitle("Organization");
		rightPanel.add(new SpacerWidget());
		
		rightPanel.add(LabelBuilder.buildRequiredLabel(oid, oidLabel));
		rightPanel.add(oid);
		oid.setTitle("Organization OID");
		rightPanel.add(new SpacerWidget());
		rightPanel.add(new SpacerWidget());
		
		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		fPanel.add(buttonPanel);
		fPanel.add(new SpacerWidget());
		mainPanel.add(fPanel);
		containerPanel.setContent(mainPanel);
		
		title.setWidth("196px");
		organization.setWidth("196px");
		oid.setWidth("196px");
		// rootOid.setWidth("196px");
		
		oid.setMaxLength(50);
		// rootOid.setMaxLength(50);
		title.setMaxLength(32);
		organization.setMaxLength(150);
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
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
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
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
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
