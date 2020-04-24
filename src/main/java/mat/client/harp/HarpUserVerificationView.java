package mat.client.harp;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MessageAlert;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputType;

public class HarpUserVerificationView implements HarpUserVerificationPresenter.Display {
    private Panel mainPanel;

    private TextBox loginId = new TextBox();

    private PasswordTextBox password = new PasswordTextBox();

    private Label securityQuestion;

    private Input securityAnswer;

    private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("Verify User");

    private MessageAlert errorMessages = new ErrorMessageAlert();

    private VerticalPanel securityQuestionAnsPanel = new  VerticalPanel();

    public static boolean isUserIdSubmit = true;

    public HarpUserVerificationView() {
        mainPanel = new VerticalPanel();
        mainPanel.addStyleName("centered");

        SimplePanel titleHolder = new SimplePanel();
        Label titlePanel = new Label("Link your Harp and Mat accounts");
        titleHolder.add(titlePanel);
        titleHolder.setStylePrimaryName("loginBlueTitleHolder");
        titleHolder.setWidth("100%");
        titlePanel.setStylePrimaryName("loginBlueTitle");
        mainPanel.add(titleHolder);

        VerticalPanel bluePanel = new VerticalPanel();
        bluePanel.setStylePrimaryName("loginContentPanel");

        Label instructions = new Label("Please enter the following information and verify your existing MAT account");
        instructions.setStylePrimaryName("loginForgotInstructions");
        bluePanel.add(instructions);
        bluePanel.add(new SpacerWidget());

        bluePanel.add(errorMessages);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
        Label userIdLabel = (Label) LabelBuilder.buildLabel(loginId, "Mat ID");
        horizontalPanel.add(userIdLabel);
        HTML required = new HTML(RequiredIndicator.get());
        horizontalPanel.add(required);
        bluePanel.add(horizontalPanel);
        bluePanel.add(new SpacerWidget());

        loginId.setTitle("Enter Mat ID Required");
        loginId.setPlaceholder("Enter Mat ID");
        loginId.setEnabled(true);
        loginId.setWidth("170px");

        password.setTitle("Enter Mat password Required");
        password.setText("Enter Mat password");
        password.setEnabled(true);
        password.setWidth("170px");

        bluePanel.add(loginId);
        bluePanel.add(password);
        bluePanel.add(new SpacerWidget());

        bluePanel.add(securityQuestionAnsPanel);
        bluePanel.add(new SpacerWidget());

        buttonBar.getSaveButton().setText("Submit");
        bluePanel.add(buttonBar);

        mainPanel.add(bluePanel);
    }

    @Override
    public TextBox getLoginId() {
        return loginId;
    }

    @Override
    public PasswordTextBox getPassword() {
        return password;
    }

    @Override
    public String getSecurityQuestion() {
        return securityQuestion.getText();
    }

    @Override
    public String getSecurityAnswer() {
        return securityAnswer.getValue();
    }

    @Override
    public HasClickHandlers getSubmit() {
        return buttonBar.getSaveButton();
    }

    @Override
    public HasClickHandlers getReset() {
        return buttonBar.getCancelButton();
    }

    @Override
    public Widget asWidget() {
        return mainPanel;
    }

    @Override
    public void addSecurityQuestionOptions(String text) {
        securityQuestion.setText(text);
    }

    @Override
    public MessageAlert getErrorMessageDisplay() {
        return errorMessages;
    }

    @Override
    public void setFocus(boolean focus) {
        securityAnswer.setFocus(focus);
    }

    @Override
    public void setSecurityQuestionAnswerEnabled(boolean enable) {
        securityQuestionAnsPanel.clear();
        if(enable){
            isUserIdSubmit = false;
            securityQuestion = new Label();
            Label label = (Label)LabelBuilder.buildLabel(securityQuestion, "Security Question:");
            securityQuestionAnsPanel.add(label);
            securityQuestionAnsPanel.add(securityQuestion);
            securityQuestionAnsPanel.add(new SpacerWidget());

            securityAnswer = new Input(InputType.PASSWORD);
            securityAnswer.setTitle("Enter Security Question Answer");
            securityQuestionAnsPanel.add(LabelBuilder.buildLabel(securityAnswer, "Security Question Answer"));
            securityQuestionAnsPanel.add(securityAnswer);
            securityQuestionAnsPanel.add(new SpacerWidget());

            Element element1 = securityAnswer.getElement();
            element1.setAttribute("aria-role", "command");
            element1.setAttribute("aria-labelledby", "LiveRegion");
            element1.setAttribute("aria-live", "assertive");
            element1.setAttribute("aria-atomic", "true");
            element1.setAttribute("aria-relevant", "all");
            element1.setAttribute("role", "alert");

            Element element = securityQuestion.getElement();
            element.setAttribute("aria-role", "command");
            element.setAttribute("aria-labelledby", "LiveRegion");
            element.setAttribute("aria-live", "assertive");
            element.setAttribute("aria-atomic", "true");
            element.setAttribute("aria-relevant", "all");
            element.setAttribute("role", "alert");

            setFocus(true);

            securityAnswer.addFocusHandler(event -> securityAnswer.setText(""));
        }
    }


}
