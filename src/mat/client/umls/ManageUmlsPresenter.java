package mat.client.umls;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessageDelegate;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.ClientConstants;

/**
 * The Class ManageUmlsPresenter.
 */
public class ManageUmlsPresenter implements MatPresenter{
	
	
	/** The show welcome message. */
	private boolean showWelcomeMessage = false;
	
	/**
	 * The Interface UMLSDisplay.
	 */
	public interface UMLSDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Gets the button bar.
		 * 
		 * @return the button bar
		 */
		SaveCancelButtonBar getButtonBar() ;
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the external link disclaimer.
		 * 
		 * @return the external link disclaimer
		 */
		VerticalPanel getExternalLinkDisclaimer();
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		Button getSubmit();
		
		/**
		 * Gets the umls external link.
		 * 
		 * @return the umls external link
		 */
		Anchor getUmlsExternalLink();
		
		/**
		 * Gets the umls trouble logging.
		 * 
		 * @return the umls trouble logging
		 */
		Anchor getUmlsTroubleLogging();
		
		/**
		 * Sets the initial focus.
		 */
		void setInitialFocus();

		Input getUserIdText();

		Input getPassword();

		FormGroup getPasswordGroup();

		HelpBlock getHelpBlock();

		FormGroup getMessageFormGrp();

		FormGroup getUserIdGroup();

		MessageAlert getSuccessMessageAlert();

		Modal getModel();
		
		ChangePasswordWidget getPasswordInput();
	}
	
	/** The display. */
	private  UMLSDisplay display;
	
	/** The welcome message. */
	private String userFirstName;

	/**Key down handler to trap enter key.**/
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(final KeyDownEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				submit();
			}
		}
	};
	
	/** The vsacapi service. */
	private VSACAPIServiceAsync vsacapiService  = MatContext.get().getVsacapiServiceAsync();
	
	/**
	 * Constructor.
	 *
	 * @param displayArg - {@link UMLSDisplay}.
	 * *
	 * @param firstName the first name
	 * @param isAlreadySignedIn the is already signed in
	 */
	public ManageUmlsPresenter(final UMLSDisplay displayArg, String firstName, boolean isAlreadySignedIn) {
		display = displayArg;	
		userFirstName = firstName;
		showWelcomeMessage=!isAlreadySignedIn;		
		resetWidget();
		display.getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(final ClickEvent event) {
				submit();				
			}
			
			
		});
		display.getUserIdText().addKeyDownHandler(submitOnEnterHandler);
		display.getPassword().addKeyDownHandler(submitOnEnterHandler);
		display.getUmlsExternalLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
				display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
				display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
				display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
				display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
			}
		});
		display.getUmlsTroubleLogging().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(true);
				display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
				display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
				display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
				display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
			}
		});
		display.getButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(false);
				Window.open(ClientConstants.EXT_LINK_UMLS, "_blank", "");
			}
		});
		
		display.getButtonBar().getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				display.getExternalLinkDisclaimer().setVisible(false);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		resetWidget();
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
	
		resetWidget();		
		if(showWelcomeMessage){
			showWelcomeMessage = false;
			display.getSuccessMessageAlert().createAlert(MessageDelegate.getWelcomeMessage(userFirstName));
		} else {
			display.getSuccessMessageAlert().clearAlert();
		}
		
		Mat.focusSkipLists("UMLSAccountLogin");
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {		
		return display.asWidget();
	}
	
	/**private method to invalidate UMLS's session by clearing UMLSSession Map for current HTTP session ID.**/
	private void invalidateVsacSession() {
		vsacapiService.inValidateVsacUser(new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(final Void result) {
			}
		});
	}
	
	/**Method to clear all messages and un set user id and password input box's.**/
	private void resetWidget() {
		display.getErrorMessageDisplay().clear();
		display.getSuccessMessageAlert().clear();
		display.getExternalLinkDisclaimer().setVisible(false);
		display.getPasswordInput().getPasswords().put("password", "");
		display.getUserIdText().setText("");
		display.getUserIdGroup().setValidationState(ValidationState.NONE);
		display.getPasswordGroup().setValidationState(ValidationState.NONE);
		display.getMessageFormGrp().setValidationState(ValidationState.NONE);
		display.getHelpBlock().setText("");
	}
	
	/** Private submit method - Calls to VSAC service.**/
	private void submit() {
		display.getErrorMessageDisplay().clear();
		display.getSuccessMessageAlert().clearAlert();
		display.getExternalLinkDisclaimer().setVisible(false);
		
		if (display.getUserIdText().getText().isEmpty()) {
			display.getUserIdGroup().setValidationState(ValidationState.ERROR);
			display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
			display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getLoginUserRequiredMessage());
			display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
			display.getSubmit().setEnabled(true);
		} else if (display.getPasswordInput().getPasswords().get("password").isEmpty()) {
			display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
			display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
			display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
			display.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
			display.getPasswordGroup().setValidationState(ValidationState.ERROR);
			display.getSubmit().setEnabled(true);
		}	else {
			display.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
			display.getPasswordGroup().setValidationState(ValidationState.SUCCESS);
			vsacapiService.validateVsacUser(display.getUserIdText().getText(),
					display.getPasswordInput().getPasswords().get("password"), new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(final Throwable caught) {
					display.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate().getUML_LOGIN_UNAVAILABLE());
					caught.printStackTrace();
					Mat.hideUMLSActive();
				}
				@Override
				public void onSuccess(final Boolean result) {
					display.getUserIdGroup().setValidationState(ValidationState.NONE);
					display.getPasswordGroup().setValidationState(ValidationState.NONE);
					if (result) {
						display.getMessageFormGrp().setValidationState(ValidationState.NONE);
						display.getHelpBlock().setText("");
						Mat.showUMLSActive();
						display.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_SUCCESSFULL_LOGIN());
						display.getPasswordInput().getPasswords().put("password", "");
						display.getUserIdText().setText("");
						MatContext.get().restartUMLSSignout();
						MatContext.get().setUMLSLoggedIn(true);
						display.getModel().hide();
					} else { //incorrect UMLS credential - no ticket is assigned.
						display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
						display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
						display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getUML_LOGIN_FAILED());
						Mat.hideUMLSActive();
						MatContext.get().setUMLSLoggedIn(false);
						invalidateVsacSession();
					}
				}
			});
		}
	}
	
}
