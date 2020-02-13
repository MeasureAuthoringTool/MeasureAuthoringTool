package mat.client.cqlworkspace.attributes;

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

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.CancelButton;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.inapphelp.message.InAppHelpMessages;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

public class InsertFrhiAttributesDialogView implements InsertFrhiAttributesDialogDisplay {

    private static final String ROLE_ATTR = "role";
    private static final String SCROLL_PANEL_HEIGHT_PX = "470px";
    private static final String ALERT = "alert";
    private static ClickHandler handler;
    private InAppHelp inAppHelp;
    private Modal dialogModal;
    private InsertFhirAttributesDialogModel model;

    public InsertFrhiAttributesDialogView(AceEditor editor, InsertFhirAttributesDialogModel model) {
        this.model = model;
        dialogModal = new Modal();
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

        final ModalFooter modalFooter = createModalFooter(editor);
        dialogModal.add(modalBody);
        dialogModal.add(modalFooter);
    }

    private ModalHeader createModalHeader() {
        ModalHeader dialogHeader = new ModalHeader();
        dialogHeader.setId(dialogModal.getId() + "_header");
        HTML heading = new HTML();
        heading.setHTML("<h4><b>Insert Attributes</b></h4>");
        heading.addStyleName("leftAligned");

        inAppHelp = new InAppHelp(InAppHelpMessages.CQL_LIBRARY_ATTRIBUTE_MODAL_FHIR);
        dialogHeader.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));

        inAppHelp.getHelpModal().addHideHandler(event -> handleClose());
        inAppHelp.getInAppHelpButton().addClickHandler(event -> showModal());
        return dialogHeader;
    }

    private ModalBody createModalBody() {
        ModalBody modalBody = new ModalBody();
        FormGroup messageFormgroup = new FormGroup();
        HelpBlock helpBlock = new HelpBlock();
        messageFormgroup.add(helpBlock);
        messageFormgroup.getElement().setAttribute(ROLE_ATTR, ALERT);

        FormGroup helpMessageFormGroup = new FormGroup();
        HelpBlock messageHelpBlock = new HelpBlock();
        helpMessageFormGroup.add(messageHelpBlock);
        helpMessageFormGroup.getElement().setAttribute(ROLE_ATTR, ALERT);
        messageHelpBlock.setColor("transparent");
        messageHelpBlock.setHeight("0px");
        helpMessageFormGroup.setHeight("0px");


        Widget leftAndRightPanelsContainer = createCentralPanel();

        modalBody.add(messageFormgroup);
        modalBody.add(helpMessageFormGroup);

        modalBody.add(leftAndRightPanelsContainer);
        modalBody.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        return modalBody;
    }

    private Widget createCentralPanel() {
        Widget leftPanel = createLeftPanel();
        Widget rightPanel = createRightPanel();

        HorizontalPanel centralPanel = new HorizontalPanel();
        centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        centralPanel.addStyleName("insert-fhir-central-panel");
        centralPanel.setHeight("500px");
        centralPanel.setWidth("100%");
        centralPanel.add(leftPanel);
        centralPanel.setCellWidth(leftPanel, "320px");
        SpacerWidget separator = new SpacerWidget();
        centralPanel.add(separator);
        centralPanel.setCellWidth(separator, "20px");
        centralPanel.add(rightPanel);
        return centralPanel;
    }

    private Widget createLeftPanel() {
        LightBoxLeftPanelView leftPanel = new LightBoxLeftPanelView(dialogModal.getId() + "_lb_leftpanel", model, "100%", SCROLL_PANEL_HEIGHT_PX);
        return leftPanel.asWidget();
    }

    private Widget createRightPanel() {
        return new LightBoxRightPanelView("100%", SCROLL_PANEL_HEIGHT_PX).asWidget();
    }

    private ModalFooter createModalFooter(AceEditor editor) {
        final ModalFooter modalFooter = new ModalFooter();
        final ButtonToolBar buttonToolBar = new ButtonToolBar();
        final Button addButton = new Button();
        addButton.setText("Insert");
        addButton.setTitle("Insert");
        addButton.setType(ButtonType.PRIMARY);
        addButton.setSize(ButtonSize.SMALL);
        addButton.setId("addButton_Button");
        addButton.addClickHandler(event -> clickInsertButton(editor));
        final Button closeButton = new CancelButton("InsertAttributeBox");
        closeButton.setSize(ButtonSize.SMALL);
        closeButton.setDataDismiss(ButtonDismiss.MODAL);
        closeButton.setId("Cancel_button");
        buttonToolBar.add(addButton);
        buttonToolBar.add(closeButton);
        modalFooter.add(buttonToolBar);
        return modalFooter;
    }

    private void clickInsertButton(AceEditor editor) {
        editor.insertAtCursor("TODO: This should insert meaningful CQL code");
        editor.focus();
        dialogModal.hide();
    }

    private void handleClose() {
        inAppHelp.getHelpModal().removeFromParent();
        dialogModal.removeFromParent();
        dialogModal.show();
    }

    private void showModal() {
        dialogModal.removeFromParent();
        inAppHelp.getHelpModal().show();
        inAppHelp.getHelpModal().getElement().setTabIndex(-1);
        inAppHelp.getMessageFocusPanel().getElement().focus();
    }

    @Override
    public void show() {
        dialogModal.show();
    }

}
