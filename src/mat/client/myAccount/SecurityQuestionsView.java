package mat.client.myAccount;

import java.util.List;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SecurityQuestionAnswerWidget;
import mat.client.shared.SecurityQuestionsDisplay;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;

/**
 * The Class SecurityQuestionsView.
 */
public class SecurityQuestionsView implements SecurityQuestionsDisplay {
	
	/** The container. */
	private FlowPanel container = new FlowPanel();
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	
	/** The heading panel. */
	private ContentWithHeadingWidget headingPanel;
	
	/** The security questions widget. */
	protected SecurityQuestionAnswerWidget securityQuestionsWidget = new SecurityQuestionAnswerWidget();
	
	/** The password edit info widget. */
	protected PasswordEditInfoWidget passwordEditInfoWidget = new PasswordEditInfoWidget();
	
	/** The buttons. */
	private SaveContinueCancelButtonBar buttons = new SaveContinueCancelButtonBar("securityQnA");
	
	/**
	 * Instantiates a new security questions view.
	 */
	public SecurityQuestionsView() {
		container.addStyleName("leftAligned");
		container.add(new SpacerWidget());
		container.add(errorMessages);
		container.add(successMessages);
		
		FieldSet fieldSetQnA = new FieldSet();
		fieldSetQnA.add(securityQuestionsWidget.getRulesGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns3FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns3FormGroup());
		
		fieldSetQnA.add(passwordEditInfoWidget.getPasswordExistingGroup());
		
		container.add(fieldSetQnA);
		container.add(new SpacerWidget());
		buttons.getCancelButton().setText("Undo");
		buttons.getCancelButton().setTitle("Undo");
		buttons.getSaveButton().setTitle("Save");
		container.add(buttons);
		container.setStyleName("contentPanel");
		headingPanel = new ContentWithHeadingWidget(container, "Update Security Questions","SecurityInfo");
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#asWidget()
	 */
	public Widget asWidget() {
		return headingPanel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttons.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttons.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getSuccessMessageDisplay()
	 */
	@Override
	public MessageAlert getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getSecurityQuestionsWidget()
	 */
	@Override
	public SecurityQuestionAnswerWidget getSecurityQuestionsWidget() { 
		return securityQuestionsWidget;
	}
	
		
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getPassword()
	 */
	@Override
	public Input getPassword() {
		return passwordEditInfoWidget.getPassword();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswerText1()
	 */
	@Override
	public String getAnswerText1() {
		
		return  securityQuestionsWidget.getAnswer1().getValue();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswerText2()
	 */
	@Override
	public String getAnswerText2() {
		return  securityQuestionsWidget.getAnswer2().getValue();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswerText3()
	 */
	@Override
	public String getAnswerText3() {
		return  securityQuestionsWidget.getAnswer3().getValue();
	}

	@Override
	public HasClickHandlers getSubmit() {
		return null;
	}

	@Override
	public HasClickHandlers getReset() {
		return null;
	}

	@Override
	public HasValue<String> getQuestion1Answer() {
		return securityQuestionsWidget.getAnswer1();
	}

	@Override
	public HasValue<String> getQuestion2Answer() {
		return securityQuestionsWidget.getAnswer2();
	}

	@Override
	public HasValue<String> getQuestion3Answer() {
		return securityQuestionsWidget.getAnswer3();
	}

	@Override
	public HasValue<String> getQuestion1Text() {
		return securityQuestionsWidget.getSecurityQuestion1();
	}

	@Override
	public HasValue<String> getQuestion2Text() {
		return securityQuestionsWidget.getSecurityQuestion2();
	}

	@Override
	public HasValue<String> getQuestion3Text() {
		return securityQuestionsWidget.getSecurityQuestion3();
	}

	@Override
	public Input getConfirmPassword() {
		return null;
	}

	@Override
	public MessageAlert getPasswordErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public MessageAlert getSecurityErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void addSecurityQuestionTexts(List<NameValuePair> texts) {
		securityQuestionsWidget.getSecurityQuestion1().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion2().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion3().setDropdownOptions(texts);
	}
}
