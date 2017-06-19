package mat.client.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Input;

import mat.client.Mat;
import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SuccessfulLoginEvent;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.SecurityQuestionsModel;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.model.SecurityQuestions;
import mat.model.UserSecurityQuestion;
import mat.shared.PasswordVerifier;
import mat.shared.SecurityQuestionVerifier;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class TempPwdLoginPresenter.
 */
public class TempPwdLoginPresenter {
	
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
		 * Sets the answer text1.
		 * 
		 * @param answerText1
		 *            the new answer text1
		 */
		public void setAnswerText1(String answerText1);
		
		/**
		 * Sets the answer text2.
		 * 
		 * @param answerText2
		 *            the new answer text2
		 */
		public void setAnswerText2(String answerText2);
		
		/**
		 * Sets the answer text3.
		 * 
		 * @param answerText3
		 *            the new answer text3
		 */
		public void setAnswerText3(String answerText3);
		
		/**
		 * Gets the security questions widget.
		 * 
		 * @return the security questions widget
		 */
		SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget();
		
	}
	
	
	/** The display. */
	private final Display display;
	
	/**
	 * Instantiates a new temp pwd login presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public TempPwdLoginPresenter(Display displayArg) {
		
		display = displayArg;
		
		display.getReset().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				reset();
			}
		});
		
		display.getSecurityQuestionsWidget().getAnswer1().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer1().setText("");
				
			}
		});
		display.getSecurityQuestionsWidget().getAnswer1().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer1().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText1(display.getSecurityQuestionsWidget().getAnswer1().getText());
				}
				display.getSecurityQuestionsWidget().getAnswer1().setText(display.getSecurityQuestionsWidget().maskAnswers(
						display.getSecurityQuestionsWidget().getAnswerText1()));
			}
		});
		
		display.getSecurityQuestionsWidget().getAnswer2().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer2().setText("");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer2().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				
				if(!(display.getSecurityQuestionsWidget().getAnswer2().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText2(display.getSecurityQuestionsWidget().getAnswer2().getText());
				}
				
				display.getSecurityQuestionsWidget().getAnswer2().setText(display.getSecurityQuestionsWidget().maskAnswers(
						display.getSecurityQuestionsWidget().getAnswerText2()));
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer3().setText("");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer3().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText3(display.getSecurityQuestionsWidget().getAnswer3().getText());
				}
				display.getSecurityQuestionsWidget().getAnswer3().setText(display.getSecurityQuestionsWidget().maskAnswers(
						display.getSecurityQuestionsWidget().getAnswerText3()));
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
				SecurityQuestionsModel model = new SecurityQuestionsModel(display.getQuestion1Text().getValue(),
						display.getAnswerText1(),
						display.getQuestion2Text().getValue(),
						display.getAnswerText2(),
						display.getQuestion3Text().getValue(),
						display.getAnswerText3());
				model.scrubForMarkUp();
				SecurityQuestionVerifier sverifier =
						new SecurityQuestionVerifier(model.getQuestion1(),
								model.getQuestion1Answer(),
								model.getQuestion2(),
								model.getQuestion2Answer(),
								model.getQuestion3(),
								model.getQuestion3Answer());
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
				}
				else{
					onSuccessTempPwdLogin(verifier, sverifier);
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
	public void onSuccessTempPwdLogin(PasswordVerifier verifier,SecurityQuestionVerifier sverifier){
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
								display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate()
										.getUnknownErrorMessage(result.getFailureReason()));
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
				// TODO Auto-generated method stub
				Window.alert("Error fetching security questions :: " + caught.getMessage());
			}
			
			@Override
			public void onSuccess(List<SecurityQuestions> result) {
				// TODO Auto-generated method stub
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
						//display.addQuestionTexts(retList);
						display.addSecurityQuestionTexts(retList);
					}
				}
				beforeDisplay();
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
		model.setQuestion1Answer(display.getSecurityQuestionsWidget().getAnswerText1());
		model.setQuestion2(display.getSecurityQuestionsWidget().getSecurityQuestion2().getValue());
		model.setQuestion2Answer(display.getSecurityQuestionsWidget().getAnswerText2());
		model.setQuestion3(display.getSecurityQuestionsWidget().getSecurityQuestion3().getValue());
		model.setQuestion3Answer(display.getSecurityQuestionsWidget().getAnswerText3());
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
	
	/**
	 * Before display.
	 */
	private void beforeDisplay() {
		
		MatContext.get().getLoginService().getSecurityQuestionsAnswers(MatContext.get().getLoggedinUserId(),
				new AsyncCallback<List<UserSecurityQuestion>>(){
			
			@Override
			public void onFailure(Throwable caught) {
				display.getSecurityErrorMessageDisplay().clearAlert();
				display.getSecurityErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(List<UserSecurityQuestion> result) {
				if((result !=null) && (result.size()>0)){
					display.setAnswerText1(result.get(0).getSecurityAnswer());
					display.getQuestion1Answer().setValue(display.getSecurityQuestionsWidget()
							.maskAnswers(result.get(0).getSecurityAnswer()));
					display.getQuestion1Text().setValue(result.get(0).getSecurityQuestions().getQuestion());
					
					
					display.setAnswerText2(result.get(1).getSecurityAnswer());
					display.getQuestion2Answer().setValue(display.getSecurityQuestionsWidget()
							.maskAnswers(result.get(1).getSecurityAnswer()));
					display.getQuestion2Text().setValue(result.get(1).getSecurityQuestions().getQuestion());
					
					
					display.setAnswerText3(result.get(2).getSecurityAnswer());
					display.getQuestion3Answer().setValue(display.getSecurityQuestionsWidget()
							.maskAnswers(result.get(2).getSecurityAnswer()));
					display.getQuestion3Text().setValue(result.get(2).getSecurityQuestions().getQuestion());
					
					
				}
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
}
