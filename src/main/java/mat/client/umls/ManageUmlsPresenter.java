package mat.client.umls;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.event.UmlsActivatedEvent;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.ClientConstants;

public class ManageUmlsPresenter implements MatPresenter {

    private final Logger logger = Logger.getLogger("MAT");

    private boolean showWelcomeMessage = false;

    public interface UMLSDisplay {

        Widget asWidget();

        SaveContinueCancelButtonBar getButtonBar();

        ErrorMessageDisplayInterface getErrorMessageDisplay();

        VerticalPanel getExternalLinkDisclaimer();

        Button getSubmit();

        Button getCancel();

        Button getContinue();

        Anchor getUmlsExternalLink();

        Anchor getUmlsTroubleLogging();

        void setInitialFocus();

        Input getUserIdText();

        Input getPasswordInput();

        FormGroup getPasswordGroup();

        HelpBlock getHelpBlock();

        FormGroup getMessageFormGrp();

        FormGroup getUserIdGroup();

        MessageAlert getSuccessMessageAlert();

        Modal getModel();

        ModalFooter getFooter();
    }

    private UMLSDisplay display;

    private String userFirstName;

    private KeyDownHandler submitOnEnterHandler = new KeyDownHandler() {
        @Override
        public void onKeyDown(final KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                submit();
            }
        }
    };

    private VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();

    /**
     * Constructor.
     *
     * @param displayArg        - {@link UMLSDisplay}.
     *                          *
     * @param firstName         the first name
     * @param isAlreadySignedIn the is already signed in
     */
    public ManageUmlsPresenter(final UMLSDisplay displayArg, String firstName, boolean isAlreadySignedIn) {
        display = displayArg;
        userFirstName = firstName;
        showWelcomeMessage = !isAlreadySignedIn;
        resetWidget();
        display.getSubmit().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                submit();
            }


        });
        display.getUserIdText().addKeyDownHandler(submitOnEnterHandler);
        display.getPasswordInput().addKeyDownHandler(submitOnEnterHandler);
        display.getUmlsExternalLink().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                display.getExternalLinkDisclaimer().setVisible(true);
                display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
                display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
                display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
                display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
            }
        });
        display.getUmlsTroubleLogging().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                display.getExternalLinkDisclaimer().setVisible(true);
                display.getExternalLinkDisclaimer().getElement().removeAttribute("id");
                display.getExternalLinkDisclaimer().getElement().removeAttribute("role");
                display.getExternalLinkDisclaimer().getElement().setAttribute("id", "ExternalLinkDisclaimer");
                display.getExternalLinkDisclaimer().getElement().setAttribute("role", "alert");
            }
        });
        display.getButtonBar().getSaveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                display.getExternalLinkDisclaimer().setVisible(false);
                Window.open(ClientConstants.EXT_LINK_UMLS, "_blank", "");
            }
        });

        display.getButtonBar().getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                display.getExternalLinkDisclaimer().setVisible(false);
            }
        });
    }


    @Override
    public void beforeClosingDisplay() {
        resetWidget();
    }


    @Override
    public void beforeDisplay() {

        resetWidget();
        if (showWelcomeMessage) {
            showWelcomeMessage = false;
            display.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getWelcomeMessage(userFirstName));
        } else {
            display.getSuccessMessageAlert().clearAlert();
        }

        Mat.focusSkipLists("UMLSAccountLogin");

    }


    @Override
    public Widget getWidget() {
        return display.asWidget();
    }

    /**
     * private method to invalidate UMLS's session by clearing UMLSSession Map for current HTTP session ID.
     **/
    private void invalidateVsacSession() {
        vsacapiService.inValidateVsacUser(new AsyncCallback<Void>() {

            @Override
            public void onFailure(final Throwable caught) {
                display.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
            }

            @Override
            public void onSuccess(final Void result) {
            }
        });
    }

    /**
     * Method to clear all messages and un set user id and password input box's.
     **/
    private void resetWidget() {
        display.getErrorMessageDisplay().clear();
        display.getSuccessMessageAlert().clear();
        display.getExternalLinkDisclaimer().setVisible(false);
        display.getPasswordInput().setText("");
        display.getUserIdText().setText("");
        display.getUserIdGroup().setValidationState(ValidationState.NONE);
        display.getPasswordGroup().setValidationState(ValidationState.NONE);
        display.getMessageFormGrp().setValidationState(ValidationState.NONE);
        display.getHelpBlock().setText("");
    }

    /**
     * Private submit method - Calls to VSAC service.
     **/
    private void submit() {
        display.getErrorMessageDisplay().clear();
        display.getSuccessMessageAlert().clearAlert();
        display.getExternalLinkDisclaimer().setVisible(false);

        if (display.getUserIdText().getText().isEmpty()) {
            display.getUserIdGroup().setValidationState(ValidationState.ERROR);
            display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
            display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getLoginUserRequiredMessage());
            display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
            display.getSubmit().setEnabled(true);
        } else if (display.getPasswordInput().getText().isEmpty()) {
            display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
            display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getPasswordRequiredMessage());
            display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
            display.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
            display.getPasswordGroup().setValidationState(ValidationState.ERROR);
            display.getSubmit().setEnabled(true);
        } else {
            display.getUserIdGroup().setValidationState(ValidationState.SUCCESS);
            display.getPasswordGroup().setValidationState(ValidationState.SUCCESS);
            vsacapiService.validateVsacUser(display.getUserIdText().getText(),
                    display.getPasswordInput().getText(), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(final Throwable caught) {
                            logger.log(Level.SEVERE, "VSACAPIServiceAsync#validateVsacUser onFailure: Error message: " + caught.getMessage(), caught);
                            display.getErrorMessageDisplay().setMessage(
                                    MatContext.get().getMessageDelegate().getUML_LOGIN_UNAVAILABLE());
                            Mat.hideUMLSActive(true);
                        }

                        @Override
                        public void onSuccess(final Boolean result) {
                            display.getUserIdGroup().setValidationState(ValidationState.NONE);
                            display.getPasswordGroup().setValidationState(ValidationState.NONE);
                            if (result) {
                                display.getMessageFormGrp().setValidationState(ValidationState.NONE);
                                display.getHelpBlock().setText("");
                                Mat.hideUMLSActive(false);
                                display.getSuccessMessageAlert().createAlert(MatContext.get().getMessageDelegate().getUMLS_SUCCESSFULL_LOGIN());
                                display.getPasswordInput().setText("");
                                display.getUserIdText().setText("");
                                display.getUserIdGroup().setVisible(false);
                                display.getPasswordGroup().setVisible(false);
                                display.getSubmit().setVisible(false);
                                display.getCancel().setVisible(false);
                                display.getFooter().setVisible(false);
                                display.getContinue().setVisible(true);
                                MatContext.get().restartUMLSSignout();
                                MatContext.get().setUMLSLoggedIn(true);
                                MatContext.get().getEventBus().fireEvent(new UmlsActivatedEvent());
                            } else { //incorrect UMLS credential - no ticket is assigned.
                                display.getHelpBlock().setIconType(IconType.EXCLAMATION_CIRCLE);
                                display.getMessageFormGrp().setValidationState(ValidationState.ERROR);
                                display.getHelpBlock().setText(MatContext.get().getMessageDelegate().getUML_LOGIN_FAILED());
                                Mat.hideUMLSActive(true);
                                MatContext.get().setUMLSLoggedIn(false);
                                invalidateVsacSession();
                            }
                        }
                    });
        }
    }

}
