package mat.client.login;


import java.util.List;

import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class FirstLoginPresenter {
	public static interface Display {
		public HasClickHandlers getSubmit();
		public HasClickHandlers getReset();
		public HasValue<String> getQuestion1Answer();
		public HasValue<String> getQuestion2Answer();
		public HasValue<String> getQuestion3Answer();
		public HasValue<String> getQuestion1Text();
		public HasValue<String> getQuestion2Text();
		public HasValue<String> getQuestion3Text();
		public HasValue<String> getPassword();
		public HasValue<String> getConfirmPassword();
		public ErrorMessageDisplayInterface getPasswordErrorMessageDisplay();
		public ErrorMessageDisplayInterface getSecurityErrorMessageDisplay();
		public void addSecurityQuestionTexts(List<NameValuePair> texts);
		public Widget asWidget();
	}

	private final Display display;

	public FirstLoginPresenter(Display displayArg) {
		this.display = displayArg;

		MatContext.get().getSecurityQuestions(new AsyncCallback<List<NameValuePair>>() {
			public void onSuccess(List<NameValuePair> values) {
				display.addSecurityQuestionTexts(values);
			}
			public void onFailure(Throwable t) {
				display.getSecurityErrorMessageDisplay().setMessage(t.getMessage());
			}
		});

		display.getReset().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
			}
		});

		display.getSubmit().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
					PasswordVerifier verifier = null;
					
						verifier = new PasswordVerifier(
								MatContext.get().getLoggedInUserEmail(), 
								display.getPassword().getValue(), 
								display.getConfirmPassword().getValue());
					

					if(!verifier.isValid()) {
						display.getPasswordErrorMessageDisplay().setMessages(verifier.getMessages());
					}else{
						display.getPasswordErrorMessageDisplay().clear();
					}

					SecurityQuestionVerifier sverifier = 
													new SecurityQuestionVerifier(display.getQuestion1Text().getValue(),
																display.getQuestion1Answer().getValue(),
																display.getQuestion2Text().getValue(),
																display.getQuestion2Answer().getValue(),
																display.getQuestion3Text().getValue(),
																display.getQuestion3Answer().getValue());
					if(!sverifier.isValid()) {
						display.getSecurityErrorMessageDisplay().setMessages(sverifier.getMessages());
					}else{
						display.getSecurityErrorMessageDisplay().clear();
					}

					if(verifier.isValid() && sverifier.isValid()) {
						
						MatContext.get().changePasswordSecurityQuestions(getValues(), new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								display.getSecurityErrorMessageDisplay().setMessage(caught.getMessage());
								
							}
							@Override
							public void onSuccess(Boolean result) {
								if(result){
									MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
								}
								else{
									display.getPasswordErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getMustNotContainDictionaryWordMessage());
								}

							}
						});
					
				}

			}
		});

		display.getReset().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());	
			}
		});
	}

	private void reset() {
		display.getPasswordErrorMessageDisplay().clear();
		display.getSecurityErrorMessageDisplay().clear();
		display.getPassword().setValue("");
		display.getConfirmPassword().setValue("");
		display.getQuestion1Answer().setValue("");
		display.getQuestion2Answer().setValue("");
		display.getQuestion3Answer().setValue("");
		display.getPasswordErrorMessageDisplay().clear();
		display.getSecurityErrorMessageDisplay().clear();
	}

	private LoginModel getValues() {
		LoginModel model = new LoginModel();
		model.setUserId(MatContext.get().getLoggedinUserId());
		model.setEmail(MatContext.get().getLoggedInUserEmail());
		model.setPassword(display.getPassword().getValue());
		model.setQuestion1(display.getQuestion1Text().getValue());
		model.setQuestion1Answer(display.getQuestion1Answer().getValue());
		model.setQuestion2(display.getQuestion2Text().getValue());
		model.setQuestion2Answer(display.getQuestion2Answer().getValue());
		model.setQuestion3(display.getQuestion3Text().getValue());
		model.setQuestion3Answer(display.getQuestion3Answer().getValue());
		return model;
	}
	public void go(HasWidgets container) {
		reset();
		container.add(display.asWidget());
	}
}
