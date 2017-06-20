package mat.client.myAccount;

import java.util.List;

import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MessageAlert;
import mat.client.shared.NameValuePair;
import mat.client.shared.PasswordEditInfoWidget;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SecurityQuestionWithMaskedAnswerWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SecurityQuestionsView.
 */
public class SecurityQuestionsView implements SecurityQuestionsPresenter.Display {
	
	/** The container. */
	private FlowPanel container = new FlowPanel();
	
	/** The password. */
	//private PasswordTextBox password = new PasswordTextBox();
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The success messages. */
	private MessageAlert successMessages = new SuccessMessageAlert();
	
	/** The heading panel. */
	private ContentWithHeadingWidget headingPanel;
	
	/** The security questions widget. */
	protected SecurityQuestionWithMaskedAnswerWidget securityQuestionsWidget = new SecurityQuestionWithMaskedAnswerWidget();
	
	/** The password edit info widget. */
	protected PasswordEditInfoWidget passwordEditInfoWidget = new PasswordEditInfoWidget();
	
	/** The buttons. */
	private SaveCancelButtonBar buttons = new SaveCancelButtonBar("securityQnA");
	
	/**
	 * Instantiates a new security questions view.
	 */
	public SecurityQuestionsView() {
		Label required = new Label("All fields are required");
		container.addStyleName("leftAligned");
		container.add(required);
		container.add(new SpacerWidget());
		container.add(errorMessages);
		container.add(successMessages);
		
		Form formSecurityQuestionAnswer = new Form();
		FieldSet fieldSetQnA = new FieldSet();
		fieldSetQnA.add(securityQuestionsWidget.getRulesGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns1FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns2FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getQuestionAns3FormGroup());
		fieldSetQnA.add(securityQuestionsWidget.getAns3FormGroup());
		
		fieldSetQnA.add(passwordEditInfoWidget.getPasswordExistingGroup());
		
		formSecurityQuestionAnswer.add(fieldSetQnA);
		
		
		container.add(formSecurityQuestionAnswer);
		container.add(new SpacerWidget());
		/*password.getElement().setId("password_ExistingPasswordTextBox");
		container.add(LabelBuilder.buildRequiredLabel(password, "Enter existing password to confirm changes"));
		container.add(new SpacerWidget());
		container.add (password);
		container.add(new SpacerWidget());*/
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
	
	/**
	 * Gets the password text box.
	 * 
	 * @return the password text box
	 */
/*	public PasswordTextBox getPasswordTextBox() {
		return password;
	}*/
	
	/**
	 * Gets the password text box.
	 * 
	 * @param password
	 *            the password
	 * @return the password text box
	 */
	/*public void getPasswordTextBox(PasswordTextBox password) {
		this.password = password;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#addQuestionTexts(java.util.List)
	 */
	public void addQuestionTexts(List<NameValuePair> texts) {
		securityQuestionsWidget.getSecurityQuestion1().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion2().setDropdownOptions(texts);
		securityQuestionsWidget.getSecurityQuestion3().setDropdownOptions(texts);
	}

	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getQuestion1()
	 */
	@Override
	public HasValue<String> getQuestion1() {
		return securityQuestionsWidget.getSecurityQuestion1();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswer1()
	 */
	@Override
	public TextBox getAnswer1() {
		return securityQuestionsWidget.getAnswer1();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getQuestion2()
	 */
	@Override
	public HasValue<String> getQuestion2() {
		return securityQuestionsWidget.getSecurityQuestion2();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswer2()
	 */
	@Override
	public TextBox getAnswer2() {
		return securityQuestionsWidget.getAnswer2();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getQuestion3()
	 */
	@Override
	public HasValue<String> getQuestion3() {
		return securityQuestionsWidget.getSecurityQuestion3();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswer3()
	 */
	@Override
	public TextBox getAnswer3() {
		return securityQuestionsWidget.getAnswer3();
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
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
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
	public SecurityQuestionWithMaskedAnswerWidget getSecurityQuestionsWidget() {
		return securityQuestionsWidget;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getPasswordEditInfoWidget()
	 */
	/*@Override
	public PasswordEditInfoWidget getPasswordEditInfoWidget() {
		return passwordEditInfoWidget;
	}*/
		
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
		
		return  securityQuestionsWidget.getAnswerText1();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswerText2()
	 */
	@Override
	public String getAnswerText2() {
		return  securityQuestionsWidget.getAnswerText2();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#getAnswerText3()
	 */
	@Override
	public String getAnswerText3() {
		return  securityQuestionsWidget.getAnswerText3();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#setAnswerText1(java.lang.String)
	 */
	@Override
	public void setAnswerText1(String answerText1) {
		 securityQuestionsWidget.setAnswerText1(answerText1);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#setAnswerText2(java.lang.String)
	 */
	@Override
	public void setAnswerText2(String answerText2) {
		 securityQuestionsWidget.setAnswerText2(answerText2);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.myAccount.SecurityQuestionsPresenter.Display#setAnswerText3(java.lang.String)
	 */
	@Override
	public void setAnswerText3(String answerText3) {
		 securityQuestionsWidget.setAnswerText3(answerText3);
		
	}
	
}
