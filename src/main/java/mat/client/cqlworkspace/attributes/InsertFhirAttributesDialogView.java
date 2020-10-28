package mat.client.cqlworkspace.attributes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.CancelButton;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.inapphelp.message.InAppHelpMessages;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

public class InsertFhirAttributesDialogView implements InsertFhirAttributesDialogDisplay {

    private static final String ROLE_ATTR = "role";
    private static final String SCROLL_PANEL_HEIGHT_PX = "470px";
    private static final String ALERT = "alert";
    private static ClickHandler handler;
    private InAppHelp inAppHelp;
    private Modal dialogModal;
    private InsertFhirAttributesDialogModel model;
    private LightBoxLeftPanelDisplay leftPanel;
    private LightBoxRightPanelDisplay rightPanel;
    private Button insertButton;
    private CancelButton closeButton;

    public InsertFhirAttributesDialogView(InsertFhirAttributesDialogModel model) {
        if (model == null) {
            throw new IllegalArgumentException("Model must be provided");
        }
        this.model = model;
        dialogModal = GWT.create(Modal.class);
        dialogModal.setId("InsertFhirAttrToAceEditor_Modal");
        dialogModal.getElement().setAttribute("role", "dialog");
        dialogModal.setClosable(true);
        dialogModal.setFade(true);
        dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
        dialogModal.setDataKeyboard(true);
        dialogModal.setSize(ModalSize.MEDIUM);
        dialogModal.setWidth("80%");
        dialogModal.setRemoveOnHide(true);

        final ModalHeader dialogHeader = createModalHeader();
        dialogModal.add(dialogHeader);

        if (handler == null) {
            handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
        }

        dialogModal.addDomHandler(handler, ClickEvent.getType());

        final ModalBody modalBody = createModalBody();

        final ModalFooter modalFooter = createModalFooter();
        dialogModal.add(modalBody);
        dialogModal.add(modalFooter);
    }

    private ModalHeader createModalHeader() {
        ModalHeader dialogHeader = GWT.create(ModalHeader.class);
        dialogHeader.setId(dialogModal.getId() + "_header");
        HTML heading = GWT.create(HTML.class);
        heading.setHTML("<h4><b>Insert Attributes</b></h4>");
        heading.addStyleName("leftAligned");

        createInAppHelp(dialogHeader, heading);
        return dialogHeader;
    }

    private void createInAppHelp(ModalHeader dialogHeader, HTML heading) {
        inAppHelp = new InAppHelp(InAppHelpMessages.CQL_LIBRARY_ATTRIBUTE_MODAL_FHIR);
        dialogHeader.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));

        inAppHelp.getHelpModal().addHideHandler(event -> closeHelpDialog());
        inAppHelp.getInAppHelpButton().addClickHandler(event -> showHelpDialog());
    }

    private ModalBody createModalBody() {
        ModalBody modalBody = GWT.create(ModalBody.class);
        FormGroup messageFormGroup = GWT.create(FormGroup.class);
        HelpBlock helpBlock = GWT.create(HelpBlock.class);
        messageFormGroup.add(helpBlock);
        messageFormGroup.getElement().setAttribute(ROLE_ATTR, ALERT);

        FormGroup helpMessageFormGroup = GWT.create(FormGroup.class);
        HelpBlock messageHelpBlock = GWT.create(HelpBlock.class);
        helpMessageFormGroup.add(messageHelpBlock);
        helpMessageFormGroup.getElement().setAttribute(ROLE_ATTR, ALERT);
        messageHelpBlock.setColor("transparent");
        messageHelpBlock.setHeight("0px");
        helpMessageFormGroup.setHeight("0px");

        Widget leftAndRightPanelsContainer = createCentralPanel();

        modalBody.add(messageFormGroup);
        modalBody.add(helpMessageFormGroup);

        modalBody.add(leftAndRightPanelsContainer);
        modalBody.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        return modalBody;
    }

    private Widget createCentralPanel() {
        Widget leftPanelWidget = createLeftPanel();
        Widget rightPanelWidget = createRightPanel();

        HorizontalPanel centralPanel = GWT.create(HorizontalPanel.class);
        centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        centralPanel.addStyleName("insert-fhir-central-panel");
        centralPanel.setHeight("500px");
        centralPanel.setWidth("100%");
        centralPanel.add(leftPanelWidget);
        centralPanel.setCellWidth(leftPanelWidget, "320px");
        SpacerWidget separator = GWT.create(SpacerWidget.class);
        centralPanel.add(separator);
        centralPanel.setCellWidth(separator, "20px");
        centralPanel.add(rightPanelWidget);
        return centralPanel;
    }

    private Widget createLeftPanel() {
        leftPanel = new LightBoxLeftPanelView(dialogModal.getId() + "_lb_leftpanel", model, "100%", SCROLL_PANEL_HEIGHT_PX);
        return leftPanel.asWidget();
    }

    private Widget createRightPanel() {
        rightPanel = new LightBoxRightPanelView("100%", SCROLL_PANEL_HEIGHT_PX);
        return rightPanel.asWidget();
    }

    private ModalFooter createModalFooter() {
        final ModalFooter modalFooter = GWT.create(ModalFooter.class);
        final ButtonToolBar buttonToolBar = GWT.create(ButtonToolBar.class);

        insertButton = GWT.create(Button.class);
        insertButton.setText("Insert");
        insertButton.setTitle("Insert");
        insertButton.setType(ButtonType.PRIMARY);
        insertButton.setSize(ButtonSize.SMALL);
        insertButton.setId("addButton_Button");

        closeButton = GWT.create(CancelButton.class);
        closeButton.setAsCancel("InsertAttributeBox");
        closeButton.setSize(ButtonSize.SMALL);
        closeButton.setDataDismiss(ButtonDismiss.MODAL);
        closeButton.setId("Cancel_button");
        buttonToolBar.add(insertButton);
        buttonToolBar.add(closeButton);
        modalFooter.add(buttonToolBar);
        return modalFooter;
    }

    private void closeHelpDialog() {
        inAppHelp.getHelpModal().removeFromParent();
        dialogModal.removeFromParent();
        dialogModal.show();
    }

    private void showHelpDialog() {
        dialogModal.removeFromParent();
        inAppHelp.getHelpModal().show();
        inAppHelp.getHelpModal().getElement().setTabIndex(-1);
        inAppHelp.getMessageFocusPanel().getElement().focus();
    }

    @Override
    public LightBoxLeftPanelDisplay getLeftPanel() {
        return leftPanel;
    }

    @Override
    public LightBoxRightPanelDisplay getRightPanel() {
        return rightPanel;
    }

    @Override
    public void show() {
        dialogModal.show();
    }

    @Override
    public void hide() {
        dialogModal.hide();
    }


    @Override
    public HasClickHandlers getInsertButton() {
        return insertButton;
    }

    @Override
    public HasClickHandlers getCloseButton() {
        return closeButton;
    }


}
