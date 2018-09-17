package mat.client.login;

import mat.client.event.ForgotLoginIDEmailSentEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.MatContext;
import mat.shared.ForgottenLoginIDResult;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ForgottenLoginIdPresenter.
 */
public class ForgottenLoginIdPresenter {
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		Input getEmailAddressText();
		
		void setEmailAddressText(Input emailAddressText);
		
		FormGroup getEmailAddressGroup();
		
		FormGroup getMessageFormGrp();
		
		HelpBlock getHelpBlock();
		
		Button getSubmitButton();
		
		Button getResetButton();
	}
	
	/** The display. */
	private final Display display;
	Boolean emailBoxValidationState = true;
	
	
	/**
	 * Instantiates a new forgotten login id presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public ForgottenLoginIdPresenter(Display displayArg) {
		display = displayArg;
		
		display.getResetButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());
			}
		});
		
		display.getSubmitButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.getHelpBlock().setText("");
				display.getEmailAddressGroup().setValidationState(ValidationState.NONE);
				requestForgottenLoginID();
			}
		});
	}
	
	/**
	 * Reset.
	 */
	private void reset() {
		display.getEmailAddressText().setText("");
		display.getHelpBlock().setText("");
		emailBoxValidationState = true;
		if (emailBoxValidationState) {
			display.getEmailAddressGroup().setValidationState(ValidationState.NONE);
		}
	}
	
	/**
	 * Request forgotten login id.
	 */
	private void requestForgottenLoginID() {
		MatContext.get().getLoginService().forgotLoginID(display.getEmailAddressText().getText(), new AsyncCallback<ForgottenLoginIDResult>(){
			@Override
			public void onFailure(Throwable caught) {
				display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
				display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				
			}
			
			@Override
			public void onSuccess(ForgottenLoginIDResult result) {
				if(result.isEmailSent()) {
					MatContext.get().getEventBus().fireEvent(new ForgotLoginIDEmailSentEvent());
				}
				else {
					String message = convertMessage(result.getFailureReason());
					display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
					display.getHelpBlock().setText(message);
					display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
					if (!emailBoxValidationState) {
						display.getEmailAddressGroup().setValidationState(ValidationState.ERROR);
					}
				}
				
			}});
	}
	
	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	private String convertMessage(int id) {
		String message;
		switch(id) {
			case ForgottenLoginIDResult.SECURITY_QUESTION_MISMATCH:
				message = MatContext.get().getMessageDelegate().getSecurityQMismatchMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_NOT_FOUND_MSG:
				message = MatContext.get().getMessageDelegate().getEmailNotFoundMessage();
				break;
			case ForgottenLoginIDResult.USER_ALREADY_LOGGED_IN:
				message = MatContext.get().getMessageDelegate().getLoginFailedAlreadyLoggedInMessage();
				break;
			case ForgottenLoginIDResult.EMAIL_INVALID:
				emailBoxValidationState = false;
				message = "Invalid Email Address.";
				break;
			default: message = MatContext.get().getMessageDelegate().getUnknownFailMessage();
		}
		return message;
	}
	
	
	
	/**
	 * Go.
	 * 
	 * @param container
	 *            the container
	 */
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
	}
}
