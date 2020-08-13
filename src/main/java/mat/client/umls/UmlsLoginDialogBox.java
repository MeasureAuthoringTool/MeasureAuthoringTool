package mat.client.umls;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.CancelButton;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.cqlworkspace.CQLMeasureWorkSpacePresenter;
import mat.client.cqlworkspace.CQLStandaloneWorkSpacePresenter;
import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class UmlsLoginDialogBox  implements ManageUmlsPresenter.UMLSDisplay{
	
	private Input userIdText = new Input(InputType.TEXT);
	private ChangePasswordWidget passwordInput = new ChangePasswordWidget();
	
	private FormGroup userIdGroup = new FormGroup();
	private FormGroup passwordGroup = new FormGroup();
	
	private FormGroup messageFormGrp = new FormGroup();
	private HelpBlock helpBlock = new HelpBlock();
	
	private MessageAlert successMessageAlert = new SuccessMessageAlert();
	
	private  Button submitButton;
	
	private  Button closeButton;
	
	private Button continueButton;

	private  String passwordEntered;
	
	private  Modal panel;
	
	private ModalFooter modalFooter;
	
	FocusPanel focusPanel = new FocusPanel();
	
	Anchor umlsExternalLink ;
	
	Anchor umlsTroubleLogging ;
	
	SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("umls");
	
	HTML externalDisclamerText = new HTML("You are leaving the Measure Authoring Tool and entering another Web site.The Measure Authoring Tool cannot attest to the accuracy of information provided by linked sites.You will be subject to the destination site's Privacy Policy when you leave the Measure Authoring Tool.");
	
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	VerticalPanel externalLinkDisclaimer = new VerticalPanel();

	public void showUMLSLogInDialog(CQLMeasureWorkSpacePresenter cqlMeasureWorkspacePresenter, CQLStandaloneWorkSpacePresenter cqlStandaloneWorkspacePresenter) {
		
		cqlMeasureWorkspacePresenter.getSearchDisplay().resetMessageDisplay();//removes error "not signed in" message above search box
		cqlStandaloneWorkspacePresenter.getSearchDisplay().resetMessageDisplay();
		
		focusPanel.clear();
	    panel = new Modal();
	    panel.setWidth("300px");

		ModalBody modalBody = new ModalBody();
		ErrorMessageAlert messageAlert = new ErrorMessageAlert();

		modalBody.clear();
		messageAlert.clear();
		modalBody.remove(messageAlert);
		panel.remove(modalBody);
		
		panel.setClosable(true);
		panel.setFade(true);
		panel.setDataBackdrop(ModalBackdrop.STATIC);
		panel.setSize(ModalSize.MEDIUM);
		panel.setRemoveOnHide(true);
		panel.getElement().setAttribute("tabindex", "0");
		
		ModalHeader modalHeader = new ModalHeader();
		
		HorizontalPanel header = new HorizontalPanel();
		header.setWidth("250px");
		HTML loginText = new HTML("<strong>Please sign in to UMLS</strong>");
		header.add(loginText);
		header.setHeight("30px");
		modalHeader.setStyleName("loginNewBlueTitleHolder");
		modalHeader.setHeight("40px");
		modalHeader.add(header);
				
		panel.add(modalHeader);
		
		messageFormGrp.add(successMessageAlert);
		messageFormGrp.add(helpBlock);
		messageFormGrp.getElement().setAttribute("role", "alert");
		
		
		FormLabel userIdLabel = new FormLabel();
		userIdLabel.setText("User Name");
		userIdLabel.setTitle("User Name");
		userIdLabel.setFor("inputUserId");
		userIdText.setWidth("250px");
		userIdText.setHeight("27px");
		userIdText.setId("inputUserId");
		userIdText.setPlaceholder("Enter UMLS User Name");
		userIdText.setTitle("Enter UMLS User Name Required");
		userIdGroup.add(userIdLabel);
		userIdGroup.add(userIdText);
		
		FormLabel passwordLabel = new FormLabel();
		passwordLabel.setText("Password");
		passwordLabel.setTitle("Password");
		passwordLabel.setFor("inputPwd");
		passwordInput.getPassword().setWidth("250px");
		passwordInput.getPassword().setHeight("27px");
		passwordInput.getPassword().setId("inputPwd");
		passwordInput.getPassword().setPlaceholder("Enter UMLS Password");
		passwordInput.getPassword().setTitle("Enter UMLS Password Required");
		passwordInput.getPassword().setValidateOnBlur(true);
		passwordGroup.add(passwordLabel);
		passwordGroup.add(passwordInput.getPassword());
		
		messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
		messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);		

		HorizontalPanel hp = new HorizontalPanel();
		

		submitButton = new Button("Sign In");
		submitButton.setType(ButtonType.SUCCESS);
		submitButton.setSize(ButtonSize.DEFAULT);

		closeButton = new CancelButton("UMLSSignIn");
		closeButton.setMarginLeft(10.00);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		
		continueButton = new Button("Continue");
		continueButton.setType(ButtonType.PRIMARY);
		continueButton.setSize(ButtonSize.DEFAULT);
		continueButton.setMarginLeft(10.00);
		continueButton.setDataDismiss(ButtonDismiss.MODAL);
		continueButton.setVisible(false);

		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp.add(submitButton);
		hp.add(closeButton);
		hp.add(continueButton);
		
		VerticalPanel vp = new VerticalPanel();
		
		vp.add(messageFormGrp);
		
		vp.getElement().setAttribute("id", "umlsContent");
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		
		vp.add(messageAlert);
		vp.add(userIdGroup);
		vp.add(passwordGroup);
		vp.add(hp);
		focusPanel.add(vp);
		modalBody.add(focusPanel);
		
		
		
		
		panel.add(modalBody);
		
		modalFooter = new ModalFooter();
		VerticalPanel vPanel = new VerticalPanel();
		umlsExternalLink = new Anchor("Need a UMLS license?");
		umlsTroubleLogging = new Anchor("Trouble signing in?");
		umlsExternalLink.setTitle("Need UMLS license");
		umlsExternalLink.getElement().setAttribute("alt", "Need UMLS license");
		umlsTroubleLogging.setTitle("Trouble Logging in");
		umlsTroubleLogging.getElement().setAttribute("alt", "Trouble signing in");
		
		vPanel.add(umlsExternalLink);
		vPanel.add(umlsTroubleLogging);
		
		modalFooter.add(vPanel);
		
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
		buttonBar.getCancelButton().setMarginLeft(10.00);
		buttonBar.getCancelButton().setTitle("I Disagree");
		buttonBar.getCancelButton().setMarginLeft(10.00);
		externalLinkDisclaimer.add(externalDisclamerText);
		externalLinkDisclaimer.add(new SpacerWidget());
		externalLinkDisclaimer.add(buttonBar);
		externalLinkDisclaimer.setVisible(false);
		externalLinkDisclaimer.addStyleName("widgitDisclaimer");
		
		modalFooter.add(externalLinkDisclaimer);
		
		panel.add(modalFooter);
		panel.getElement().focus();
		
	}

	public void showModal() {
		panel.show();
	}

	public Button getSubmitButton() {
		return submitButton;
	}

	public  String getPasswordEntered() {
		return passwordEntered;
	}

	public  void setPasswordEntered(String passwordEntered) {
		this.passwordEntered = passwordEntered;
	}

	public  Button getsubmitbutton() {
		return submitButton;
	}

	public Input getPassword() {
		return passwordInput.getPassword();
	}

	@Override
	public Widget asWidget() {
		return panel;
	}
	

	@Override
	public SaveContinueCancelButtonBar getButtonBar() {
		return buttonBar;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public VerticalPanel getExternalLinkDisclaimer() {
		return externalLinkDisclaimer;
	}

	@Override
	public Button getSubmit() {
		return submitButton;
	}

	@Override
	public Anchor getUmlsExternalLink() {
		return umlsExternalLink;
	}

	@Override
	public Anchor getUmlsTroubleLogging() {
		return umlsTroubleLogging;
	}

	@Override
	public void setInitialFocus() {
		userIdText.setFocus(false);
		
	}

	@Override
	public Input getUserIdText() {
		return userIdText;
	}

	@Override
	public Input getPasswordInput() {
		return passwordInput.getPassword();
	}

	@Override
	public FormGroup getPasswordGroup() {
		return passwordGroup;
	}

	@Override
	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	@Override
	public FormGroup getMessageFormGrp() {
		return messageFormGrp;
	}

	@Override
	public FormGroup getUserIdGroup() {
		return userIdGroup;
	}

	@Override
	public MessageAlert getSuccessMessageAlert() {
		return successMessageAlert;
	}

	@Override
	public Modal getModel() {
		return panel;
		
	}

	@Override
	public Button getCancel() {
		return closeButton;
	}

	@Override
	public ModalFooter getFooter() {
		return modalFooter;
	}

	@Override
	public Button getContinue() {
		return continueButton;
	}
}
