package mat.client.myAccount;

import java.util.ArrayList;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.myAccount.service.SaveMyAccountResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.NameValuePair;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.client.shared.SuccessMessageDisplayInterface;
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
import com.google.gwt.user.client.ui.Widget;

public class SecurityQuestionsPresenter implements MatPresenter {
	public static interface Display {
		Widget asWidget();
		void addQuestionTexts(List<NameValuePair> texts);
		
		HasValue<String> getQuestion1();
		HasValue<String> getAnswer1();
		HasValue<String> getQuestion2();
		HasValue<String> getAnswer2();
		HasValue<String> getQuestion3();
		HasValue<String> getAnswer3();
		HasClickHandlers getSaveButton();
		HasClickHandlers getCancelButton();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget();
	}
	
	private Display display;
	private SecurityQuestionsModel currentValues;
	
	public SecurityQuestionsPresenter(Display displayArg) {
		this.display = displayArg;
		
		MatContext.get().getSecurityQuestions(new AsyncCallback<List<NameValuePair>>() {
			public void onSuccess(List<NameValuePair> values) {
				display.addQuestionTexts(values);
			}
			public void onFailure(Throwable t) {
				//Window.alert(t.getMessage());
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
		
		display.getSecurityQuestionsWidget().getAnswer1().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer1().setText("");
				display.getSecurityQuestionsWidget().getAnswer1().getElement().setAttribute("type", "text");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer1().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				display.getSecurityQuestionsWidget().getAnswer1().getElement().setAttribute("type", "password");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer2().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer2().setText("");
				display.getSecurityQuestionsWidget().getAnswer2().getElement().setAttribute("type", "text");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer2().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				display.getSecurityQuestionsWidget().getAnswer2().getElement().setAttribute("type", "password");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				display.getSecurityQuestionsWidget().getAnswer3().setText("");
				display.getSecurityQuestionsWidget().getAnswer3().getElement().setAttribute("type", "text");
			}
		});
		display.getSecurityQuestionsWidget().getAnswer3().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				display.getSecurityQuestionsWidget().getAnswer3().getElement().setAttribute("type", "password");
			}
		});
		
		display.getCancelButton().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				display.getErrorMessageDisplay().clear();
				display.getSuccessMessageDisplay().clear();
				if(currentValues != null) {
					setValues(currentValues);
				}
			}
		});
		
		display.getSaveButton().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				display.getErrorMessageDisplay().clear();
				SecurityQuestionVerifier sverifier = 
					new SecurityQuestionVerifier(display.getQuestion1().getValue(),
							display.getAnswer1().getValue(),
							display.getQuestion2().getValue(),
							display.getAnswer2().getValue(),
							display.getQuestion3().getValue(),
							display.getAnswer3().getValue());
				if(!sverifier.isValid()) {
					display.getSuccessMessageDisplay().clear();
					display.getErrorMessageDisplay().setMessages(sverifier.getMessages());
				}
				else {
					currentValues = getValues();
					MatContext.get().getMyAccountService().saveSecurityQuestions(currentValues, new AsyncCallback<SaveMyAccountResult>() {
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
						@Override
						public void onSuccess(SaveMyAccountResult result) {
							if(result.isSuccess()){
								display.getErrorMessageDisplay().clear();
								display.getSuccessMessageDisplay().clear();
								display.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getSecurityQuestionsUpdatedMessage());
							}else{
								List<String> messages = new ArrayList<String>();
								switch(result.getFailureReason()) {
									case SaveMyAccountResult.SERVER_SIDE_VALIDATION:
										messages = result.getMessages();
										break;
									default:
										messages.add(MatContext.get().getMessageDelegate().getUnknownErrorMessage(result.getFailureReason()));
								}
								display.getSuccessMessageDisplay().clear();
								display.getErrorMessageDisplay().setMessages(messages);
							}
						}
					});
				}
			}
		});
	}

	@Override
	public void beforeDisplay() {
		
		MatContext.get().getMyAccountService().getSecurityQuestions(new AsyncCallback<SecurityQuestionsModel>() {
			
			public void onSuccess(SecurityQuestionsModel result) {
				
				display.getErrorMessageDisplay().clear();
				display.getSuccessMessageDisplay().clear();
				setValues(result);
				currentValues = result;
			}
			
			public void onFailure(Throwable caught) {
				display.getSuccessMessageDisplay().clear();
				display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}
		});
		Mat.focusSkipLists("SecurityInfo");
	}
	@Override 
	public void beforeClosingDisplay() {	
	}
	
	public Widget getWidget() {
		return display.asWidget();
	}
	
	private SecurityQuestionsModel getValues() {
		SecurityQuestionsModel model = new SecurityQuestionsModel();
		model.setQuestion1(display.getQuestion1().getValue());
		model.setQuestion1Answer(display.getAnswer1().getValue());
		model.setQuestion2(display.getQuestion2().getValue());
		model.setQuestion2Answer(display.getAnswer2().getValue());
		model.setQuestion3(display.getQuestion3().getValue());
		model.setQuestion3Answer(display.getAnswer3().getValue());
		return model;
	}
	private void setValues(SecurityQuestionsModel result) {
		display.getAnswer1().setValue(result.getQuestion1Answer());
		display.getQuestion1().setValue(result.getQuestion1());

		display.getAnswer2().setValue(result.getQuestion2Answer());
		display.getQuestion2().setValue(result.getQuestion2());
		
		display.getAnswer3().setValue(result.getQuestion3Answer());
		display.getQuestion3().setValue(result.getQuestion3());
	}
}
