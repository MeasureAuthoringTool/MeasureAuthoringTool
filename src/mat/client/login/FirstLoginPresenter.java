package mat.client.login;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionAnswerWidget;
import mat.model.SecurityQuestions;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;

/**
 * The Class FirstLoginPresenter.
 */
public class FirstLoginPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * Gets the submit.
		 * 
		 * @return the submit
		 */
		public HasClickHandlers getSubmit();
		
		/**
		 * Gets the reset.
		 * 
		 * @return the reset
		 */
		public HasClickHandlers getReset();
		
		/**
		 * Gets the question1 answer.
		 * 
		 * @return the question1 answer
		 */
		public HasValue<String> getQuestion1Answer();
		
		/**
		 * Gets the question2 answer.
		 * 
		 * @return the question2 answer
		 */
		public HasValue<String> getQuestion2Answer();
		
		/**
		 * Gets the question3 answer.
		 * 
		 * @return the question3 answer
		 */
		public HasValue<String> getQuestion3Answer();
		
		/**
		 * Gets the question1 text.
		 * 
		 * @return the question1 text
		 */
		public HasValue<String> getQuestion1Text();
		
		/**
		 * Gets the question2 text.
		 * 
		 * @return the question2 text
		 */
		public HasValue<String> getQuestion2Text();
		
		/**
		 * Gets the question3 text.
		 * 
		 * @return the question3 text
		 */
		public HasValue<String> getQuestion3Text();
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		public Input getPassword();
		
		/**
		 * Gets the confirm password.
		 * 
		 * @return the confirm password
		 */
		public Input getConfirmPassword();
		
		/**
		 * Gets the password error message display.
		 * 
		 * @return the password error message display
		 */
		public MessageAlert getPasswordErrorMessageDisplay();
		
		/**
		 * Gets the security error message display.
		 * 
		 * @return the security error message display
		 */
		public MessageAlert getSecurityErrorMessageDisplay();
		
		/**
		 * Adds the security question texts.
		 * 
		 * @param texts
		 *            the texts
		 */
		public void addSecurityQuestionTexts(List<NameValuePair> texts);
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the answer text1.
		 * 
		 * @return the answer text1
		 */
		public String getAnswerText1();
		
		/**
		 * Gets the answer text2.
		 * 
		 * @return the answer text2
		 */
		public String getAnswerText2();
		
		/**
		 * Gets the answer text3.
		 * 
		 * @return the answer text3
		 */
		public String getAnswerText3();
		
		/**
		 * Gets the security questions widget.
		 * 
		 * @return the security questions widget
		 */
		SecurityQuestionAnswerWidget getSecurityQuestionsWidget();
	}
	
	/** The display. */
	private final Display display;
	
	/**
	 * Instantiates a new first login presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public FirstLoginPresenter(Display displayArg) {
		display = displayArg;
		
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});
		
		display.getSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				PasswordVerifier verifier = null;
				String markupRegExp = "<[^>]+>";
				String noMarkupTextPwd = display.getPassword().getText().trim().replaceAll(markupRegExp, "");
				display.getPassword().setText(noMarkupTextPwd);
				
				String noMarkupTextConfirm = display.getConfirmPassword().getText().trim().replaceAll(markupRegExp, "");
				display.getConfirmPassword().setText(noMarkupTextConfirm);
				
				verifier = new PasswordVerifier(
						MatContext.get().getLoggedinLoginId(),
						display.getPassword().getText(),
						display.getConfirmPassword().getText());
				
				
				if(!verifier.isValid()) {
					display.getPasswordErrorMessageDisplay().createAlert(verifier.getMessages());
				}else{
					display.getPasswordErrorMessageDisplay().clearAlert();
				}
				SecurityQuestionsModel securityQuestionsModel = new SecurityQuestionsModel(display.getQuestion1Text().getValue(),
						display.getQuestion1Answer().getValue(),
						display.getQuestion2Text().getValue(),
						display.getQuestion2Answer().getValue(),
						display.getQuestion3Text().getValue(),
						display.getQuestion3Answer().getValue());
				securityQuestionsModel.scrubForMarkUp();
				SecurityQuestionVerifier sverifier = new SecurityQuestionVerifier(securityQuestionsModel.getQuestion1(),securityQuestionsModel.getQuestion1Answer(),
						securityQuestionsModel.getQuestion2(),securityQuestionsModel.getQuestion2Answer(),securityQuestionsModel.getQuestion3(),
						securityQuestionsModel.getQuestion3Answer());
				if(!sverifier.isValid()) {
					display.getSecurityErrorMessageDisplay().createAlert(sverifier.getMessages());
				}else{
					display.getSecurityErrorMessageDisplay().clearAlert();
				}
				
				ValidateChangedPassword(verifier,sverifier);
			}
		});
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
				MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());
			}
		});
	}
	
	
	/**
	 * Validate changed password.
	 *
	 * @param verifier the verifier
	 * @param sverifier the sverifier
	 */
	public void ValidateChangedPassword(final PasswordVerifier verifier,final SecurityQuestionVerifier sverifier){
		
		loginService.validateNewPassword(MatContext.get().getLoggedinLoginId(), display.getPassword().getText(),
				new AsyncCallback<HashMap<String,String>>(){
			
			@Override
			public void onFailure(Throwable caught) {
				display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(HashMap<String, String> resultMap) {
				
				String result = resultMap.get("result");
				if(result.equals("SUCCESS")){
					display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate()
							.getIS_NOT_PREVIOUS_PASSWORD());
				}else {
					onSuccessFirstLogin(verifier,sverifier);
				}
			}
			
		});
		
	}
	
	/**
	 * On success temp pwd login.
	 *
	 * @param verifier the verifier
	 * @param sverifier the sverifier
	 */
	public void onSuccessFirstLogin(PasswordVerifier verifier,SecurityQuestionVerifier sverifier){
		if(verifier.isValid() && sverifier.isValid()) {
			MatContext.get().changePasswordSecurityQuestions(getValues(), new AsyncCallback<LoginResult>() {
				@Override
				public void onFailure(Throwable caught) {
					display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				@Override
				public void onSuccess(LoginResult result) {
					if(result.isSuccess()){
						MatContext.get().getEventBus().fireEvent(new SuccessfulLoginEvent());
					}else {
						switch(result.getFailureReason()) {
							case LoginResult.SERVER_SIDE_VALIDATION_SECURITY_QUESTIONS:
								display.getPasswordErrorMessageDisplay().createAlert(result.getMessages());
								break;
							case LoginResult.SERVER_SIDE_VALIDATION_PASSWORD:
								display.getSecurityErrorMessageDisplay().createAlert(result.getMessages());
								break;
							case LoginResult.DICTIONARY_EXCEPTION:
								display.getPasswordErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate()
										.getMustNotContainDictionaryWordMessage());
								break;
							default:
								display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().
										getUnknownErrorMessage(result.getFailureReason()));
						}
						
					}
				}
			});
		}
	}
	
	/**
	 * Load security questions.
	 */
	private void loadSecurityQuestions(){
		MatContext.get().getLoginService().getSecurityQuestions(new AsyncCallback<List<SecurityQuestions>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Error fetching 1 security questions :: " + caught.getMessage());
			}
			
			@Override
			public void onSuccess(List<SecurityQuestions> result) {
				if(result != null){
					List<NameValuePair> retList = new ArrayList<NameValuePair>();
					for(int i=0; i < result.size();i++){
						SecurityQuestions securityQues = result.get(i);
						NameValuePair nvp = new NameValuePair();
						nvp.setName(securityQues.getQuestion());
						nvp.setValue(securityQues.getQuestion());
						retList.add(nvp);
					}
					
					if(retList!=null){
						display.addSecurityQuestionTexts(retList);
					}
				}
			}
			
		});
		
		
	}
	
	
	/**
	 * Reset.
	 */
	private void reset() {
		display.getPasswordErrorMessageDisplay().clearAlert();
		display.getSecurityErrorMessageDisplay().clearAlert();
		display.getPassword().setText("");
		display.getConfirmPassword().setText("");
		display.getQuestion1Answer().setValue("");
		display.getQuestion2Answer().setValue("");
		display.getQuestion3Answer().setValue("");
	}
	
	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	private LoginModel getValues() {
		LoginModel model = new LoginModel();
		model.setUserId(MatContext.get().getLoggedinUserId());
		model.setEmail(MatContext.get().getLoggedInUserEmail());
		model.setLoginId(MatContext.get().getLoggedinLoginId());
		model.setPassword(display.getPassword().getText());
		model.setQuestion1(display.getSecurityQuestionsWidget().getSecurityQuestion1().getValue());
		model.setQuestion1Answer(display.getSecurityQuestionsWidget().getAnswer1().getValue());
		model.setQuestion2(display.getSecurityQuestionsWidget().getSecurityQuestion2().getValue());
		model.setQuestion2Answer(display.getSecurityQuestionsWidget().getAnswer2().getValue());
		model.setQuestion3(display.getSecurityQuestionsWidget().getSecurityQuestion3().getValue());
		model.setQuestion3Answer(display.getSecurityQuestionsWidget().getAnswer3().getValue());
		model.scrubForMarkUp();
		return model;
	}
	
	/**
	 * Go.
	 * 
	 * @param container
	 *            the container
	 */
	public void go(HasWidgets container) {
		reset();
		loadSecurityQuestions();
		container.add(display.asWidget());
	}
	
	
}
