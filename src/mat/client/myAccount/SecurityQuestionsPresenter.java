package mat.client.myAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Input;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.login.service.LoginServiceAsync;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionAnswerWidget;
import mat.client.util.ClientConstants;
import mat.model.SecurityQuestions;
import mat.shared.SecurityQuestionVerifier;

/**
 * The Class SecurityQuestionsPresenter.
 */
public class SecurityQuestionsPresenter implements MatPresenter {
	
	/** The login service. */
	LoginServiceAsync loginService = MatContext.get().getLoginService();
	
	/**
	 * The Interface Display.
	 */
	public static interface Display {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Adds the question texts.
		 * 
		 * @param texts
		 *            the texts
		 */
		void addQuestionTexts(List<NameValuePair> texts);
		
		/**
		 * Gets the question1.
		 * 
		 * @return the question1
		 */
		HasValue<String> getQuestion1();
		
		/**
		 * Gets the answer1.
		 * 
		 * @return the answer1
		 */
		Input getAnswer1();
		
		/**
		 * Gets the question2.
		 * 
		 * @return the question2
		 */
		HasValue<String> getQuestion2();
		
		/**
		 * Gets the answer2.
		 * 
		 * @return the answer2
		 */
		Input getAnswer2();
		
		/**
		 * Gets the question3.
		 * 
		 * @return the question3
		 */
		HasValue<String> getQuestion3();
		
		/**
		 * Gets the answer3.
		 * 
		 * @return the answer3
		 */
		Input getAnswer3();
		
		/**
		 * Gets the password.
		 * 
		 * @return the password
		 */
		Input getPassword();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public MessageAlert getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public MessageAlert getSuccessMessageDisplay();
		
		/**
		 * Gets the security questions widget.
		 * 
		 * @return the security questions widget
		 */
		SecurityQuestionAnswerWidget getSecurityQuestionsWidget();
		
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
		
	}
	
	/** The submit on enter handler. */
	private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				saveSecurityQuestions(display.getPassword().getValue());
			}
		}
	};
	
	/** The display. */
	private Display display;
	
	/** The current values. */
	private SecurityQuestionsModel currentValues;
	
	
	/**
	 * Instantiates a new security questions presenter.
	 * 
	 * @param displayArg
	 *            the display arg
	 */
	public SecurityQuestionsPresenter(Display displayArg) {
		display = displayArg;
		
		loginService.getSecurityQuestions(new AsyncCallback<List<SecurityQuestions>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			
			@Override
			public void onSuccess(List<SecurityQuestions> result) {
				// TODO Auto-generated method stub
				List<NameValuePair> retList = new ArrayList<NameValuePair>();
				for(int i=0; i < result.size();i++){
					SecurityQuestions securityQues = result.get(i);
					NameValuePair nvp = new NameValuePair();
					nvp.setName(securityQues.getQuestion());
					nvp.setValue(securityQues.getQuestion());
					retList.add(nvp);
				}
				
				if(retList!=null){
					display.addQuestionTexts(retList);
				}
			}
		});
		
		
		
		/*display.getSecurityQuestionsWidget().getAnswer1().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer1().setText("");
				
			}
		});*/
		/*display.getSecurityQuestionsWidget().getAnswer1().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer1().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText1(display.getSecurityQuestionsWidget().getAnswer1().getText());
				}
				//display.getSecurityQuestionsWidget().getAnswer1().setText(display.getSecurityQuestionsWidget().maskAnswers(display.getSecurityQuestionsWidget().getAnswerText1()));
				display.getSecurityQuestionsWidget().getAnswer1().setText(display.getSecurityQuestionsWidget().getAnswerText1());
			}
		});*/
		
		/*display.getSecurityQuestionsWidget().getAnswer2().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer2().setText("");
			}
		});*/
		/*display.getSecurityQuestionsWidget().getAnswer2().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				
				if(!(display.getSecurityQuestionsWidget().getAnswer2().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText2(display.getSecurityQuestionsWidget().getAnswer2().getText());
				}
				
				display.getSecurityQuestionsWidget().getAnswer2().setText(display.getSecurityQuestionsWidget().getAnswerText2());
			}
		});*/
		/*display.getSecurityQuestionsWidget().getAnswer3().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer3().setText("");
			}
		});*/
		/*display.getSecurityQuestionsWidget().getAnswer3().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!(display.getSecurityQuestionsWidget().getAnswer3().getText()).isEmpty()) {
					display.getSecurityQuestionsWidget().setAnswerText3(display.getSecurityQuestionsWidget().getAnswer3().getText());
				}
				display.getSecurityQuestionsWidget().getAnswer3().setText(display.getSecurityQuestionsWidget().getAnswerText3());
			}
		});*/
		
		
		display.getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getErrorMessageDisplay().clearAlert();
				display.getSuccessMessageDisplay().clearAlert();
				if(currentValues != null) {
					setValues(currentValues);
				}
			}
		});
		
		display.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				display.getErrorMessageDisplay().clearAlert();
				SecurityQuestionsModel securityQuestionsModel = new SecurityQuestionsModel(display.getQuestion1().getValue(),
						display.getAnswer1().getValue(),
						display.getQuestion2().getValue(),
						display.getAnswer2().getValue(),
						display.getQuestion3().getValue(),
						display.getAnswer3().getValue());
				securityQuestionsModel.scrubForMarkUp();
				SecurityQuestionVerifier sverifier =
						new SecurityQuestionVerifier(securityQuestionsModel.getQuestion1(),securityQuestionsModel.getQuestion1Answer(),
								securityQuestionsModel.getQuestion2(), securityQuestionsModel.getQuestion2Answer(),
								securityQuestionsModel.getQuestion3(), securityQuestionsModel.getQuestion3Answer());
				if(!sverifier.isValid()) {
					display.getSuccessMessageDisplay().clearAlert();
					display.getErrorMessageDisplay().createAlert(sverifier.getMessages());
				}
				else {
					saveSecurityQuestions(display.getPassword().getValue());
				}
				display.getPassword().setValue("");
			}
		});
		
		// if the user enters the "Enter" key on the password field, do a save.
		//display.getPasswordEditInfoWidget().getPassword().addKeyDownHandler(submitOnEnterHandler);
		Input passwordTextBox = display.getPassword();
		passwordTextBox.addKeyDownHandler(submitOnEnterHandler);
	}
	
	/**
	 * Save security questions.
	 * 
	 * @param password
	 *            the password
	 */
	private void saveSecurityQuestions(String password){
		loginService.validatePassword(MatContext.get().getLoggedinLoginId(), password, new AsyncCallback<HashMap<String,String>>(){
			@Override
			public void onSuccess(HashMap<String,String> resultMap) {
				String result = resultMap.get("result");
				if(result.equals("SUCCESS")){
					currentValues = getValues();
					MatContext.get().getMyAccountService().saveSecurityQuestions(currentValues, new AsyncCallback<SaveMyAccountResult>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						@Override
						public void onSuccess(SaveMyAccountResult result) {
							if(result.isSuccess()){
								display.getErrorMessageDisplay().clearAlert();
								display.getSuccessMessageDisplay().clearAlert();
								display.getSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getSecurityQuestionsUpdatedMessage());
							}else{
								List<String> messages = new ArrayList<String>();
								switch(result.getFailureReason()) {
									case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
										messages = result.getMessages();
										break;
									default:
										messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
								}
								display.getSuccessMessageDisplay().clearAlert();
								display.getErrorMessageDisplay().createAlert(messages);
							}
						}
					});
				}else{
					display.getErrorMessageDisplay().clearAlert();
					display.getSuccessMessageDisplay().clearAlert();
					String displayErrorMsg= resultMap.get("message");
					if(displayErrorMsg.equals("REDIRECT")){
						MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
					}else{
						display.getErrorMessageDisplay().createAlert(displayErrorMsg);
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		
		MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
			
			@Override
			public void onSuccess(SecurityQuestionsModel result) {
				
				display.getErrorMessageDisplay().clearAlert();
				display.getSuccessMessageDisplay().clearAlert();
				setValues(result);
				currentValues = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				display.getSuccessMessageDisplay().clearAlert();
				display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return display.asWidget();
	}
	
	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	private SecurityQuestionsModel getValues() {
		SecurityQuestionsModel model = new SecurityQuestionsModel();
		model.setQuestion1(display.getQuestion1().getValue());
		model.setQuestion1Answer(display.getAnswerText1());
		model.setQuestion2(display.getQuestion2().getValue());
		model.setQuestion2Answer(display.getAnswerText2());
		model.setQuestion3(display.getQuestion3().getValue());
		model.setQuestion3Answer(display.getAnswerText3());
		model.scrubForMarkUp();
		return model;
	}
	
	/**
	 * Sets the values.
	 * 
	 * @param result
	 *            the new values
	 */
	private void setValues(SecurityQuestionsModel result) {
		
		display.getAnswer1().setText(result.getQuestion1Answer());
		
		display.getQuestion1().setValue(result.getQuestion1());
		
		display.getAnswer2().setText(result.getQuestion2Answer());
		display.getQuestion2().setValue(result.getQuestion2());
		
		display.getAnswer3().setText(result.getQuestion3Answer());
		display.getQuestion3().setValue(result.getQuestion3());
		display.getPassword().setValue("");
	}
	
}
