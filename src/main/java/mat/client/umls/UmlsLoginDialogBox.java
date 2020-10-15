package mat.client.umls;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.CancelButton;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.cqlworkspace.CQLMeasureWorkSpacePresenter;
import mat.client.cqlworkspace.CQLStandaloneWorkSpacePresenter;
import mat.client.shared.ChangePasswordWidget;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalSpacerWidget;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import java.util.Arrays;
import java.util.logging.Logger;

public class UmlsLoginDialogBox implements ManageUmlsPresenter.UMLSDisplay {
    private final Logger logger = Logger.getLogger("UmlsLoginDialogBox");
    private static final String[] UNICODE_NUMBERS = {
            "➊", "➋", "➌", "➍", "➎", "➏"
    };
    private static final String NUMBER_FONT_STYLE = "color: blue";
    private static final String BOLD_STYLE = "font-weight: bold";
    private static final String UNICODE_POINTER_RIGHT = "▶";
    private static final String UNICODE_POINTER_DOWN = "▼";

    private HTML stepOne = new HTML(buildSpan(UNICODE_NUMBERS[0], NUMBER_FONT_STYLE) + " Sign in to your ");
    private final Widget[] INSTRUCTION_TEXT = {
            new HTML(buildSpan(UNICODE_NUMBERS[1], NUMBER_FONT_STYLE) + " Click on \"My Profile\""),
            new HTML(buildSpan(UNICODE_NUMBERS[2], NUMBER_FONT_STYLE) + " Copy your API key, and paste in the field above"),
            new HTML(buildSpan("If you don't have a key in your UTS profile", BOLD_STYLE + "margin-top:5px; margin-bottom:5px")),
            new HTML(buildSpan(UNICODE_NUMBERS[3], NUMBER_FONT_STYLE) + " Click \"Edit Profile\""),
            new HTML(buildSpan(UNICODE_NUMBERS[4], NUMBER_FONT_STYLE) + " Check the box labled: \"Generate new API key\""),
            new HTML(buildSpan(UNICODE_NUMBERS[5], NUMBER_FONT_STYLE) + " Click \"Save Profile\", and return to step 3"),
    };

    private final ChangePasswordWidget apiKey = new ChangePasswordWidget();
    private final FormGroup apiKeyGroup = new FormGroup();
    private final FormGroup messageFormGrp = new FormGroup();
    private final HelpBlock helpBlock = new HelpBlock();

    private final MessageAlert successMessageAlert = new SuccessMessageAlert();

    private Button submitButton;

    private Button closeButton;

    private Button continueButton;

    private Modal panel;

    private ModalFooter modalFooter;

    private String apiKeyEntered;

    private HTML trianglePointer = new HTML();

    private VerticalPanel instructionsPanel = new VerticalPanel();

    private FocusPanel focusPanel = new FocusPanel();

    private Anchor showInstructions;

    private Anchor umlsExternalLink;

    private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("umls");

    private HTML externalDisclamerText = new HTML("You are leaving the Measure Authoring Tool and entering another Web site." +
            "The Measure Authoring Tool cannot attest to the accuracy of information provided by linked sites." +
            "You will be subject to the destination site's Privacy Policy when you leave the Measure Authoring Tool.");

    private final ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
    VerticalPanel externalLinkDisclaimer = new VerticalPanel();

    private HorizontalPanel instructionLinksPanel = new HorizontalPanel();

    public void showUMLSLogInDialog(CQLMeasureWorkSpacePresenter cqlMeasureWorkspacePresenter,
                                    CQLStandaloneWorkSpacePresenter cqlStandaloneWorkspacePresenter) {
        logger.info("Entering showUMLSLogInDialog");
        cqlMeasureWorkspacePresenter.getSearchDisplay().resetMessageDisplay();//removes error "not signed in" message above search box
        cqlStandaloneWorkspacePresenter.getSearchDisplay().resetMessageDisplay();

        focusPanel.clear();
        panel = new Modal();
        panel.setWidth("350px");

        ModalBody modalBody = new ModalBody();
        ErrorMessageAlert messageAlert = new ErrorMessageAlert();

        modalBody.clear();
        messageAlert.clear();
        modalBody.remove(messageAlert);
        panel.remove(modalBody);

        panel.setClosable(true);
        panel.setFade(true);
        panel.setDataBackdrop(ModalBackdrop.STATIC);
        panel.setSize(ModalSize.MEDIUM);
        panel.setRemoveOnHide(true);
        panel.getElement().setAttribute("tabindex", "0");

        panel.add(buildHeaderWidgets());

        umlsExternalLink = new Anchor("UMLS account");
        umlsExternalLink.setTitle("UTS");
        umlsExternalLink.getElement().setAttribute("alt", "UTS");

        buildInstructionsWidgets();

        messageFormGrp.add(successMessageAlert);
        messageFormGrp.add(helpBlock);
        messageFormGrp.getElement().setAttribute("role", "alert");

        buildApiKeyWidgets();

        messageAlert.getElement().getStyle().setMarginTop(0.0, Style.Unit.PX);
        messageAlert.getElement().getStyle().setMarginBottom(0.0, Style.Unit.PX);



        buildButtons();

        logger.info("After buttons");
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        buttonPanel.add(submitButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(continueButton);

        VerticalPanel vp = new VerticalPanel();
        vp.getElement().setAttribute("id", "umlsContent");
        vp.add(messageFormGrp);
        vp.add(new SpacerWidget());
        vp.add(messageAlert);
        vp.add(apiKeyGroup);
        vp.add(instructionLinksPanel);
        vp.add(new SpacerWidget());
        vp.add(instructionsPanel);
        vp.add(new HTML("<hr/>"));
        vp.add(buttonPanel);
        focusPanel.add(vp);
        modalBody.add(focusPanel);

        panel.add(modalBody);

        logger.info("Before footer");
        modalFooter = new ModalFooter();
        buildDisclaimer();
        modalFooter.add(externalLinkDisclaimer);

        panel.add(modalFooter);
        panel.getElement().focus();

        logger.info("Finished showUMLSLogInDialog");
    }

    public void showModal() {
        panel.show();
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public String getApiKeyEntered() {
        return apiKeyEntered;
    }

    public void setApiKeyEntered(String apiKeyEntered) {
        this.apiKeyEntered = apiKeyEntered;
    }

    public Button getsubmitbutton() {
        return submitButton;
    }

    @Override
    public Input getApiKeyInput() {
        return apiKey.getPassword();
    }

    @Override
    public Widget asWidget() {
        return panel;
    }


    @Override
    public SaveContinueCancelButtonBar getButtonBar() {
        return buttonBar;
    }

    @Override
    public ErrorMessageDisplayInterface getErrorMessageDisplay() {
        return errorMessages;
    }

    @Override
    public VerticalPanel getExternalLinkDisclaimer() {
        return externalLinkDisclaimer;
    }

    @Override
    public Button getSubmit() {
        return submitButton;
    }

    @Override
    public Anchor getUmlsExternalLink() {
        return umlsExternalLink;
    }

    @Override
    public void setInitialFocus() {
        apiKey.getPassword().setFocus(false);
    }

    @Override
    public HelpBlock getHelpBlock() {
        return helpBlock;
    }

    @Override
    public FormGroup getMessageFormGrp() {
        return messageFormGrp;
    }


    @Override
    public MessageAlert getSuccessMessageAlert() {
        return successMessageAlert;
    }

    @Override
    public Modal getModel() {
        return panel;
    }

    @Override
    public Button getCancel() {
        return closeButton;
    }

    @Override
    public ModalFooter getFooter() {
        return modalFooter;
    }

    @Override
    public Button getContinue() {
        return continueButton;
    }

    @Override
    public FormGroup getApiKeyGroup() {
        return apiKeyGroup;
    }

    public boolean isShowingInstructions() {
        boolean result = "none" != instructionsPanel.getElement().getStyle().getDisplay();
        logger.info("isShowingInstructions " + result);
        return result;
    }

    public void setShowingInstructions(boolean isShowing) {
        logger.info("setShowingInstructions(" + isShowing + ")");
        if (isShowing) {
            trianglePointer.setHTML(buildSpan(UNICODE_POINTER_DOWN, "font-size: medium"));
            instructionsPanel.getElement().getStyle().setProperty("display", "");
        } else {
            trianglePointer.setHTML(buildSpan(UNICODE_POINTER_RIGHT, "font-size: medium"));
            instructionsPanel.getElement().getStyle().setProperty("display", "none");
        }
    }

    public Anchor getShowInstructions() {
        return showInstructions;
    }

    private String buildSpan(String text, String style) {
        return "<span style='" + style + "'>" + text + "</span>";
    }

    private void buildInstructionsWidgets() {
        showInstructions = new Anchor("Show Instructions");
        umlsExternalLink.setTitle("UTS");
        umlsExternalLink.getElement().setAttribute("alt", "UTS");
        umlsExternalLink.getElement().getStyle().setMarginTop(5.0, Style.Unit.PX);

        instructionsPanel.setBorderWidth(1);
        instructionsPanel.setWidth("100%");
        instructionsPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        VerticalPanel innerInstructions = new VerticalPanel();
        innerInstructions.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        innerInstructions.setWidth("100%");
        innerInstructions.getElement().getStyle().setMargin(5.0, Style.Unit.PX);

        HorizontalPanel step1Panel = new HorizontalPanel();
        step1Panel.add(stepOne);
        step1Panel.add(umlsExternalLink);
        stepOne.getElement().getStyle().setMarginRight(5.0, Style.Unit.PX);
        innerInstructions.add(step1Panel);
        Arrays.stream(INSTRUCTION_TEXT).forEach(w -> innerInstructions.add(w));
        instructionsPanel.add(innerInstructions);

        instructionLinksPanel.add(new HTML(buildSpan("Where is my key?", BOLD_STYLE )));
        instructionLinksPanel.add(new HTML("&nbsp;&nbsp;"));
        instructionLinksPanel.add(showInstructions);
        instructionLinksPanel.add(new HTML("&nbsp;&nbsp;"));
        instructionLinksPanel.add(trianglePointer);
        setShowingInstructions(false);
    }

    private void buildApiKeyWidgets() {
        FormLabel apiKeyLbl = new FormLabel();
        apiKeyLbl.setText("API key");
        apiKeyLbl.setTitle("API key");
        apiKeyLbl.setFor("inputPwd");
        apiKey.getPassword().setWidth("250px");
        apiKey.getPassword().setHeight("27px");
        apiKey.getPassword().setId("inputPwd");
        apiKey.getPassword().setPlaceholder("Enter UMLS API key");
        apiKey.getPassword().setTitle("Enter UMLS UMLS API key Required");
        apiKey.getPassword().setValidateOnBlur(true);
        apiKeyGroup.add(apiKeyLbl);
        apiKeyGroup.add(apiKey.getPassword());
    }

    private Widget buildHeaderWidgets() {
        ModalHeader result = new ModalHeader();
        HorizontalPanel header = new HorizontalPanel();
        header.setWidth("250px");
        HTML loginText = new HTML("<strong>Please sign in to UMLS</strong>");
        header.add(loginText);
        header.setHeight("30px");
        result.setStyleName("loginNewBlueTitleHolder");
        result.setHeight("40px");
        result.add(header);
        logger.info("Updating panel");
        return result;
    }

    private void buildButtons() {
        logger.info("Before buttons");
        submitButton = new Button("Connect to UMLS");
        submitButton.setType(ButtonType.SUCCESS);
        submitButton.setSize(ButtonSize.DEFAULT);
        submitButton.setId("umlsSubmitButton");

        closeButton = new CancelButton("UMLSSignIn");
        closeButton.setId("umlsCancelButton");
        closeButton.setMarginLeft(10.00);
        closeButton.setDataDismiss(ButtonDismiss.MODAL);

        continueButton = new Button("Continue");
        continueButton.setType(ButtonType.PRIMARY);
        continueButton.setSize(ButtonSize.DEFAULT);
        continueButton.setMarginLeft(10.00);
        continueButton.setDataDismiss(ButtonDismiss.MODAL);
        continueButton.setVisible(false);
        continueButton.setId("umlsContinueButton");

    }

    private void buildDisclaimer() {
        logger.info("Before externalDisclamerText");
        externalDisclamerText.getElement().setAttribute("id", "externalDisclamerText");
        externalLinkDisclaimer.getElement().setAttribute("id", "ExternalLinkDisclaimer");
        externalLinkDisclaimer.getElement().setAttribute("aria-role", "panel");
        externalLinkDisclaimer.getElement().setAttribute("aria-labelledby", "externalDisclamerText");
        externalLinkDisclaimer.getElement().setAttribute("aria-live", "assertive");
        externalLinkDisclaimer.getElement().setAttribute("aria-atomic", "true");
        externalLinkDisclaimer.getElement().setAttribute("aria-relevant", "all");
        externalLinkDisclaimer.getElement().setAttribute("role", "alert");
        buttonBar.getSaveButton().setText("I Agree");
        buttonBar.getSaveButton().setTitle("I Agree");
        buttonBar.getCancelButton().setText("I Disagree");
        buttonBar.getCancelButton().setMarginLeft(10.00);
        buttonBar.getCancelButton().setTitle("I Disagree");
        buttonBar.getCancelButton().setMarginLeft(10.00);
        externalLinkDisclaimer.add(externalDisclamerText);
        externalLinkDisclaimer.add(new SpacerWidget());
        externalLinkDisclaimer.add(buttonBar);
        externalLinkDisclaimer.setVisible(false);
        externalLinkDisclaimer.addStyleName("widgitDisclaimer");
    }
}
