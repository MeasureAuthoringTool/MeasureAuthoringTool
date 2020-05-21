package mat.client.harp;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import mat.client.event.HarpSupportEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.shared.HarpConstants;

public class HarpUserVerificationPresenter {

    private final Display display;

    public interface Display {

        TextBox getLoginId();

        PasswordTextBox getPassword();

        String getSecurityQuestion();

        String getSecurityAnswer();

        HasClickHandlers getSubmit();

        Anchor getNewUserAnchorTab();

        HasClickHandlers getReset();

        MessageAlert getErrorMessageDisplay();

        void setSecurityQuestionAnswerEnabled(boolean show);

        void addSecurityQuestionOptions(String text);

        void setFocus(boolean focus);

        Widget asWidget();
    }

    public HarpUserVerificationPresenter(Display display) {
        this.display = display;

        display.getReset().addClickHandler(event -> onCancel());

        display.getSubmit().addClickHandler(event -> onGetSecurityQuestion());

        display.getNewUserAnchorTab().addClickHandler(event -> showNewUserInformation());

        display.setSecurityQuestionAnswerEnabled(false);
    }

    private void onGetSecurityQuestion() {
        if (HarpUserVerificationView.isUserIdSubmit) {
            display.getErrorMessageDisplay().clearAlert();
            if (null == display.getLoginId().getValue() || display.getLoginId().getValue().trim().isEmpty()) {
                display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
                return;
            } else if (null == display.getPassword().getValue() || display.getPassword().getValue().trim().isEmpty()) {
                display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
                return;
            }
            display.getLoginId().setEnabled(false);
            display.getPassword().setEnabled(false);
            loadSecurityQuestionForLoginId(display.getLoginId().getText(), display.getPassword().getText());
        } else {
            forgotSecurityAnswer();
        }
    }

    private void loadSecurityQuestionForLoginId(String loginId, String password) {

        MatContext.get().getLoginService().getSecurityQuestionToVerifyHarpUser(loginId, password, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getInvalidUser());
                display.getLoginId().setEnabled(true);
                display.getPassword().setEnabled(true);
            }

            @Override
            public void onSuccess(String securityQuestion) {
                display.setSecurityQuestionAnswerEnabled(true);
                display.addSecurityQuestionOptions(securityQuestion);
            }
        });
    }

    private void forgotSecurityAnswer() {
        String answer = display.getSecurityAnswer();
        if (answer == null || answer.isEmpty()) {
            display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getSecurityNotAnsweredMessage());
            return;
        } else {
            display.getPassword().setEnabled(false);

            MatContext.get().getLoginService().verifyHarpUser(display.getSecurityQuestion(), display.getSecurityAnswer(), display.getLoginId().getValue(), MatContext.get().getHarpUserInfo(),
                    new AsyncCallback<Boolean>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            MatContext.get().getEventBus().fireEvent(new HarpSupportEvent());
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                MatContext.get().getEventBus().fireEvent(new SuccessfulHarpLoginEvent());
                            } else {
                                display.getErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUnableToVerifyHarpUser());
                                display.getLoginId().setEnabled(true);
                                display.getPassword().setEnabled(true);
                            }
                        }
                    });
        }
    }

    private void onCancel() {
        reset();
        MatContext.get().getEventBus().fireEvent(new ReturnToLoginEvent());
    }

    private void reset() {
        display.getLoginId().setEnabled(true);
        display.getLoginId().setValue("");
        display.getPassword().setEnabled(true);
        display.setSecurityQuestionAnswerEnabled(false);
        display.getErrorMessageDisplay().clearAlert();
        display.getLoginId().setFocus(true);
    }

    private void showNewUserInformation() {
        Window.open(HarpConstants.NEW_USER_REGISTRATION_FORM_URL, "_blank", "enabled");
    }

    public void go(HasWidgets container) {
        reset();
        container.add(display.asWidget());
        display.getLoginId().setFocus(true);
    }
}
