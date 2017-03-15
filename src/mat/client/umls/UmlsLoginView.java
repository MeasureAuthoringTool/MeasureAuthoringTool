package mat.client.umls;

import mat.client.ImageResources;
import mat.client.Mat;
import mat.client.shared.EmailAddressTextBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class UmlsLoginView.
 */
public class UmlsLoginView implements ManageUmlsPresenter.UMLSDisplay  {
	
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The info message. */
	private HTML infoMessage = new HTML();
	
	/** The info message panel. */
	private Panel infoMessagePanel;
	
	/** The userid. */
	private TextBox userid;
	
	/** The password. */
	private TextBox password;
	
	/** The submit button. */
	private Button submitButton;
	
	/** The main panel. */
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/** The simple panel. */
	private SimplePanel simplePanel = new SimplePanel();
	
	/** The success. */
	Label success = new Label();
	
	/** The umls external link. */
	Anchor umlsExternalLink;
	
	/** The umls trouble logging. */
	Anchor umlsTroubleLogging;
	
	/** The external link disclaimer. */
	VerticalPanel externalLinkDisclaimer = new VerticalPanel();
	
	/** The button bar. */
	SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("umls");
	
	/**
	 * Instantiates a new umls login view.
	 */
	public UmlsLoginView() {
		mainPanel.setStyleName("umlscontentPanel");
		mainPanel.getElement().setAttribute("id", "umlsContent");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		Grid infoGrid = new Grid(2,2);
		FocusableImageButton focusableImageButton = new FocusableImageButton(ImageResources.INSTANCE.icon_success_sm(),"Success");
		infoGrid.setWidget(0, 0, focusableImageButton);
		Mat.removeInputBoxFromFocusPanel(focusableImageButton.getElement());
		success.setStyleName("loginInfoMessageHeader");
		infoGrid.setWidget(0, 1, success);
		infoGrid.setWidget(1, 1, infoMessage);
		SimplePanel infoMessage = new SimplePanel();
		infoMessage.add(infoGrid);
		infoMessage.setStyleName("loginInfoMessageContainer");
		infoMessagePanel = wrapInSpacer(infoMessage);
		mainPanel.add(infoMessagePanel);
		
		SimplePanel loginTitleHolder = new SimplePanel();
		Label loginTitlePanel = new Label("Please sign in to UMLS");
		loginTitleHolder.add(loginTitlePanel);
		loginTitleHolder.setStylePrimaryName("loginBlueTitleHolder");
		loginTitlePanel.setStylePrimaryName("loginBlueTitle");
		mainPanel.add(loginTitleHolder);
		
		VerticalPanel loginPanel = new VerticalPanel();
		loginPanel.add(errorMessages);
		
		userid = new EmailAddressTextBox();
		userid.getElement().setAttribute("id", "User Name");
		loginPanel.add(LabelBuilder.buildLabel(userid, "User Name"));
		loginPanel.add(userid);
		loginPanel.add(new SpacerWidget());
		
		password = new PasswordTextBox();
		loginPanel.add(LabelBuilder.buildLabel(password, "Password"));
		loginPanel.add(password);
		loginPanel.add(new SpacerWidget());
		
		submitButton = new PrimaryButton("Sign In","primaryButton");
		loginPanel.add(submitButton);
		
		loginPanel.setStylePrimaryName("loginContentPanel");
		
		VerticalPanel vPanel = new VerticalPanel();
		umlsExternalLink = new Anchor("Need a UMLS license?");
		umlsTroubleLogging = new Anchor("Trouble signing in?");
		umlsExternalLink.setTitle("Need UMLS license");
		umlsExternalLink.getElement().setAttribute("alt", "Need UMLS license");
		umlsTroubleLogging.setTitle("Trouble Logging in");
		umlsTroubleLogging.getElement().setAttribute("alt", "Trouble signing in");
		
		vPanel.add(umlsExternalLink);
		vPanel.add(umlsTroubleLogging);
		loginPanel.add(new SpacerWidget());
		loginPanel.add(vPanel);
		
		password.setWidth("200px");
		simplePanel.add(loginPanel);
		
		mainPanel.add(simplePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		
		HTML externalDisclamerText = new HTML("You are leaving the Measure Authoring Tool and entering another Web site.The Measure Authoring Tool cannot attest to the accuracy of information provided by linked sites.You will be subject to the destination site's Privacy Policy when you leave the Measure Authoring Tool.");
		externalDisclamerText.getElement().setAttribute("id", "externalDisclamerText");
		externalLinkDisclaimer.getElement().setAttribute("id", "ExternalLinkDisclaimer");
		externalLinkDisclaimer.getElement().setAttribute("aria-role", "panel");
		externalLinkDisclaimer.getElement().setAttribute("aria-labelledby", "externalDisclamerText");
		externalLinkDisclaimer.getElement().setAttribute("aria-live", "assertive");
		externalLinkDisclaimer.getElement().setAttribute("aria-atomic", "true");
		externalLinkDisclaimer.getElement().setAttribute("aria-relevant", "all");
		externalLinkDisclaimer.getElement().setAttribute("role", "alert");
		buttonBar.getSaveButton().setText("I Agree");
		buttonBar.getSaveButton().setTitle("I Agree");
		buttonBar.getCancelButton().setText("I Disagree");
		buttonBar.getCancelButton().setTitle("I Disagree");
		externalLinkDisclaimer.add(externalDisclamerText);
		externalLinkDisclaimer.add(new SpacerWidget());
		externalLinkDisclaimer.add(buttonBar);
		externalLinkDisclaimer.setVisible(false);
		externalLinkDisclaimer.addStyleName("disclaimer");
		mainPanel.add(externalLinkDisclaimer);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
	}
	
	/**
	 * Wrap in spacer.
	 * 
	 * @param w
	 *            the w
	 * @return the simple panel
	 */
	private SimplePanel wrapInSpacer(Widget w) {
		SimplePanel spacer = new SimplePanel();
		spacer.setStylePrimaryName("loginSpacer");
		spacer.add(w);
		return spacer;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getSubmit()
	 */
	@Override
	public HasClickHandlers getSubmit() {
		return submitButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getUserid()
	 */
	@Override
	public HasValue<String> getUserid() {
		return userid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getPassword()
	 */
	@Override
	public HasValue<String> getPassword() {
		return password;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getInfoMessage()
	 */
	@Override
	public HasHTML getInfoMessage() {
		return infoMessage;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#setInfoMessageVisible(boolean)
	 */
	@Override
	public void setInfoMessageVisible(boolean value) {
		if(value){
			success.setText("Success");
		}else{
			success.setText("");
		}
		MatContext.get().setVisible(infoMessagePanel,value);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getUseridField()
	 */
	@Override
	public HasKeyDownHandlers getUseridField() {
		return userid;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getPasswordField()
	 */
	@Override
	public HasKeyDownHandlers getPasswordField() {
		return password;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#setInitialFocus()
	 */
	@Override
	public void setInitialFocus() {
		userid.setFocus(false);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getUmlsExternalLink()
	 */
	@Override
	public Anchor getUmlsExternalLink() {
		return umlsExternalLink;
	}
	
	/**
	 * Sets the umls external link.
	 * 
	 * @param umlsExternalLink
	 *            the new umls external link
	 */
	public void setUmlsExternalLink(Anchor umlsExternalLink) {
		this.umlsExternalLink = umlsExternalLink;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getExternalLinkDisclaimer()
	 */
	@Override
	public VerticalPanel getExternalLinkDisclaimer() {
		return externalLinkDisclaimer;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getUmlsTroubleLogging()
	 */
	@Override
	public Anchor getUmlsTroubleLogging() {
		return umlsTroubleLogging;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#getButtonBar()
	 */
	@Override
	public SaveCancelButtonBar getButtonBar() {
		return buttonBar;
	}
	
}
