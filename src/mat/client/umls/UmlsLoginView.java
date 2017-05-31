package mat.client.umls;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelFooter;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;

import com.google.gwt.user.client.ui.Anchor;
//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.ImageResources;
import mat.client.Mat;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.MatContext;
//import mat.client.shared.PrimaryButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;


/**
 * The Class UmlsLoginView.
 */
public class UmlsLoginView implements ManageUmlsPresenter.UMLSDisplay  {
	private Input userIdText = new Input(InputType.TEXT);
	private Input passwordInput = new Input(InputType.PASSWORD);
	
	private FormGroup passwordGroup = new FormGroup();
	private FormGroup userIdGroup = new FormGroup();
	
	private FormGroup messageFormGrp = new FormGroup();
	private HelpBlock helpBlock = new HelpBlock();
	
	HTML externalDisclamerText = new HTML("You are leaving the Measure Authoring Tool and entering another Web site.The Measure Authoring Tool cannot attest to the accuracy of information provided by linked sites.You will be subject to the destination site's Privacy Policy when you leave the Measure Authoring Tool.");
	//private Panel successMessagePanel = new org.gwtbootstrap3.client.ui.Panel();
	//private PanelBody successMessageBody = new PanelBody();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	
	/** The info message. */
	private HTML infoMessage = new HTML();
	
	/** The info message panel. */
	private SimplePanel infoMessagePanel;
	
	
	/** The submit button. */
	private Button submitButton = new Button("Sign In");
	
	/** The main panel. */
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/** The simple panel. */
	private SimplePanel simplePanel = new SimplePanel();
	
	/** The success. */
	Label success = new Label();
	
	/** The umls external link. */
	Anchor umlsExternalLink ;//= new Anchor("Need a UMLS license?");
	
	/** The umls trouble logging. */
	Anchor umlsTroubleLogging ;//= new Anchor("Trouble signing in?");
	
	
	
	/** The external link disclaimer. */
	VerticalPanel externalLinkDisclaimer = new VerticalPanel();
	
	/** The button bar. */
	SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("umls");
	
	/**
	 * Instantiates a new umls login view.
	 */
	public UmlsLoginView() {
		/*mainPanel.setStyleName("umlscontentPanel");
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
		
		submitButton = new Button("Sign In");
		submitButton.setType(ButtonType.SUCCESS);
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
		mainPanel.add(new SpacerWidget());*/
		
		buildUmlLoginView();
	}
	
	
	void buildUmlLoginView(){
		
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
		
		
		//Login Panel.
		Panel loginPanel = new Panel();
		loginPanel.setWidth("300px");
		loginPanel.setMarginLeft(300.00);
		//Login Panel Header.
		PanelHeader header = new PanelHeader();
		header.setStyleName("loginNewBlueTitleHolder");
		HTML loginText = new HTML("<strong>Please sign in to UMLS</strong>");
		header.add(loginText);
		//Login Panel Body.
		PanelBody loginPanelBody = new PanelBody();
		
		Form loginForm = new Form();
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		loginForm.add(messageFormGrp);
		
		FormLabel userIdLabel = new FormLabel();
		userIdLabel.setText("User Name");
		userIdLabel.setTitle("User Name");
		userIdLabel.setFor("inputUserId");
		userIdText.setWidth("250px");
		userIdText.setHeight("27px");
		userIdText.setId("inputUserId");
		userIdText.setPlaceholder("Enter User Name");
		userIdText.setTitle("Enter User Name");
		userIdGroup.add(userIdLabel);
		userIdGroup.add(userIdText);
		
		FormLabel passwordLabel = new FormLabel();
		passwordLabel.setText("Password");
		passwordLabel.setTitle("Password");
		passwordLabel.setFor("inputPwd");
		passwordInput.setWidth("250px");
		passwordInput.setHeight("27px");
		passwordInput.setId("inputPwd");
		passwordInput.setPlaceholder("Enter Password");
		passwordInput.setTitle("Enter Password");
		passwordInput.setValidateOnBlur(true);
		passwordGroup.add(passwordLabel);
		passwordGroup.add(passwordInput);
		
		FormGroup buttonFormGroup = new FormGroup();
		submitButton.setType(ButtonType.SUCCESS);
		submitButton.setTitle("Sign In");
		buttonFormGroup.add(submitButton);
		
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(userIdGroup);
		formFieldSet.add(passwordGroup);
		
		formFieldSet.add(buttonFormGroup);
		loginForm.add(formFieldSet);
		loginPanelBody.add(loginForm);
		
		
		PanelFooter loginPanelFooter = new PanelFooter();
		VerticalPanel vPanel = new VerticalPanel();
		umlsExternalLink = new Anchor("Need a UMLS license?");
		umlsTroubleLogging = new Anchor("Trouble signing in?");
		umlsExternalLink.setTitle("Need UMLS license");
		umlsExternalLink.getElement().setAttribute("alt", "Need UMLS license");
		umlsTroubleLogging.setTitle("Trouble Logging in");
		umlsTroubleLogging.getElement().setAttribute("alt", "Trouble signing in");
		
		vPanel.add(umlsExternalLink);
		vPanel.add(umlsTroubleLogging);
		
		loginPanelFooter.add(vPanel);
		
		loginPanel.add(header);
		loginPanel.add(loginPanelBody);
		loginPanel.add(loginPanelFooter);
		
		
		simplePanel.add(loginPanel);
		mainPanel.add(simplePanel);
		
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		
	
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
	public Button getSubmit() {
		return submitButton;
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
	 * @see mat.client.umls.ManageUmlsPresenter.UMLSDisplay#setInitialFocus()
	 */
	@Override
	public void setInitialFocus() {
		userIdText.setFocus(false);
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

	@Override
	public Input getUserIdText() {
		return userIdText;
	}


	public void setUserIdText(Input userIdText) {
		this.userIdText = userIdText;
	}

	@Override
	public Input getPasswordInput() {
		return passwordInput;
	}


	public void setPasswordInput(Input passwordInput) {
		this.passwordInput = passwordInput;
	}

	@Override
	public FormGroup getPasswordGroup() {
		return passwordGroup;
	}


	public void setPasswordGroup(FormGroup passwordGroup) {
		this.passwordGroup = passwordGroup;
	}

	@Override
	public FormGroup getUserIdGroup() {
		return userIdGroup;
	}


	public void setUserIdGroup(FormGroup userIdGroup) {
		this.userIdGroup = userIdGroup;
	}

	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}


	public void setMessageFormGrp(FormGroup messageFormGrp) {
		this.messageFormGrp = messageFormGrp;
	}
	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}


	public void setHelpBlock(HelpBlock helpBlock) {
		this.helpBlock = helpBlock;
	}
	
}
