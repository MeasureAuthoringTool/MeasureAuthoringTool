package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.CancelButton;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.inapphelp.message.InAppHelpMessages;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;

public class InsertFhirAttributeBuilderDialogBox {


    private static InAppHelp inAppHelp;
    private static Modal dialogModal;
    private static ClickHandler handler;

    public static void showAttributesDialogBox(final AceEditor editor) {
        dialogModal = new Modal();
        dialogModal.getElement().setAttribute("role", "dialog");
        dialogModal.setClosable(true);
        dialogModal.setFade(true);
        dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
        dialogModal.setDataKeyboard(true);
        dialogModal.setId("InsertFhirAttrToAceEditor_Modal");
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

        dialogModal.show();
    }

    private static ModalBody createModalBody() {
        ModalBody modalBody = new ModalBody();
        FormGroup messageFormgroup = new FormGroup();
        HelpBlock helpBlock = new HelpBlock();
        messageFormgroup.add(helpBlock);
        messageFormgroup.getElement().setAttribute("role", "alert");

        FormGroup helpMessageFormGroup = new FormGroup();
        HelpBlock messageHelpBlock = new HelpBlock();
        helpMessageFormGroup.add(messageHelpBlock);
        helpMessageFormGroup.getElement().setAttribute("role", "alert");
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

    private static Widget createCentralPanel() {
        Widget leftPanel = createLeftPanel();
        Widget rightPanel = createRightPanel();

        HorizontalPanel centralPanel = new HorizontalPanel();
        centralPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
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

    private static Widget createLeftPanel() {

        PanelGroup attributesSelectLeftPanel = new PanelGroup();
        attributesSelectLeftPanel.setId(dialogModal.getId() + "_lb_leftpanel");
        attributesSelectLeftPanel.setWidth("100%");
        attributesSelectLeftPanel.setHeight("100%");
        attributesSelectLeftPanel.addStyleName("pre-scrollable");

        for (DataType dataType : DATA) {
            Panel dataTypeContainerPanel = new Panel(PanelType.DEFAULT);
            dataTypeContainerPanel.setId(attributesSelectLeftPanel.getId() + "_dataType_" + dataType.getId());
            PanelHeader panelHeader = new PanelHeader();
            Heading sectionHeading = new Heading(HeadingSize.H4, dataType.getName());
            panelHeader.add(sectionHeading);
            dataTypeContainerPanel.add(panelHeader);

            PanelBody panelBody = new PanelBody();
            PanelCollapse panelCollapse = new PanelCollapse();
            panelCollapse.setId(attributesSelectLeftPanel.getId() + "_collapse_" + dataType.getId());
            panelCollapse.add(panelBody);
            dataTypeContainerPanel.add(panelCollapse);

            for (AttributeDto attribute : dataType.getAttributes()) {
                CheckBox checkBox = new CheckBox(attribute.getName());
                checkBox.setId(attributesSelectLeftPanel.getId() + "_checkbox_" + dataType.getId() + "_" + attribute.getId());
                panelBody.add(checkBox);
            }

            panelHeader.setDataParent(attributesSelectLeftPanel.getId());
            panelHeader.setDataTargetWidget(panelCollapse);
            panelHeader.setDataToggle(Toggle.COLLAPSE);

            attributesSelectLeftPanel.add(dataTypeContainerPanel);
        }

//        Widget leftPanelLabel = new Label("Attributes:");
        PanelHeader leftPanelLabel = new PanelHeader();
        leftPanelLabel.setText("Attributes:");
        leftPanelLabel.setTitle("Attributes:");

        Panel panel = new Panel(PanelType.PRIMARY);
        panel.add(leftPanelLabel);

//        VerticalPanel leftPanelContainer = new VerticalPanel();
//        leftPanelContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
//        leftPanelContainer.getElement().setId(dialogModal.getId() + "_lb_leftpanel_container");
//        leftPanelContainer.setWidth("100%");
//        leftPanelContainer.setHeight("100%");

//        leftPanelContainer.add(leftPanelLabel);
//        leftPanelContainer.setCellHeight(leftPanelLabel, "30px");

//        ScrollPanel scroller = new ScrollPanel(attributesSelectLeftPanel);
//        scroller.setSize("100%", "100%");
//
//        leftPanelContainer.add(scroller);

        panel.add(attributesSelectLeftPanel);
//        pg.setHeight("150px");
//        return leftPanelContainer;

        PanelGroup pg = new PanelGroup();
        pg.add(panel);
        pg.setHeight("100%");
        pg.setWidth("100%");

        return pg;
    }

    private static Widget createRightPanel() {
//        attributesFormRightPanel.setHeight("100%");
//        attributesFormRightPanel.setWidth("100%");
//        attributesFormRightPanel.addStyleName("pre-scrollable");
        HTML html = new HTML(
                "<div><h4>Adverse Event</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"adverse_event_code\">code</label></h5>\n" +
                        "<select id=\"adverse_event_code\" name=\"adverse_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"adverse_event_type\">type</label></h5>\n" +
                        "<select id=\"adverse_event_type\" name=\"adverse_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
                        +
                        "<div><h4>Allergy/Intolerance</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"alergy_event_code\">code</label></h5>\n" +
                        "<select id=\"alergy_event_code\" name=\"alergy_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"alergy_event_type\">type</label></h5>\n" +
                        "<select id=\"alergy_event_type\" name=\"alergy_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
                        +
                        "<div><h4>Assessment, Not Ordered</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"assessment_event_code\">code</label></h5>\n" +
                        "<select id=\"assessment_event_code\" name=\"assessment_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"assessment_event_type\">type</label></h5>\n" +
                        "<select id=\"assessment_event_type\" name=\"assessment_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
        );
//        html.addStyleName("pre-scrollable");
//        html.setWidth("100%");
//        html.setHeight("100%");

        ScrollPanel scroller = new ScrollPanel(html);
        scroller.setSize("100%", "100%");

        return scroller;
    }

    private static ModalHeader createModalHeader() {
        ModalHeader dialogHeader = new ModalHeader();
        dialogHeader.setId(dialogModal.getId() + "_header");
        HTML heading = new HTML();
        heading.setHTML("<h4><b>Insert Attributes</b></h4>");
        heading.addStyleName("leftAligned");

        inAppHelp = new InAppHelp(InAppHelpMessages.CQL_LIBRARY_ATTRIBUTE_MODAL);
        dialogHeader.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));

        inAppHelp.getHelpModal().addHideHandler(event -> handleClose());
        inAppHelp.getInAppHelpButton().addClickHandler(event -> showModal());
        return dialogHeader;
    }

    private static ModalFooter createModalFooter(AceEditor editor) {
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

    private static void showModal() {
        removeModalFromParent();
        inAppHelp.getHelpModal().show();
        inAppHelp.getHelpModal().getElement().setTabIndex(-1);
        inAppHelp.getMessageFocusPanel().getElement().focus();
    }

    private static void handleClose() {
        inAppHelp.getHelpModal().removeFromParent();
        removeModalFromParent();
        dialogModal.show();
    }

    private static void removeModalFromParent() {
        dialogModal.removeFromParent();
    }

    private static void clickInsertButton(AceEditor editor) {
        editor.insertAtCursor("Some text");
        editor.focus();
        dialogModal.hide();
    }


    private static final List<DataType> DATA = Arrays.asList(
            new DataType(DOM.createUniqueId(), "Adverse Event",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "type"),
                            new AttributeDto(DOM.createUniqueId(), "severity"),
                            new AttributeDto(DOM.createUniqueId(), "facilityLocaion"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "recorder"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "relevantDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "patientId")
                    )
            ),
            new DataType(DOM.createUniqueId(), "Allergy/Intolerance",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "prevalencePeriod"),
                            new AttributeDto(DOM.createUniqueId(), "type"),
                            new AttributeDto(DOM.createUniqueId(), "severity"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "recorder"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "relevantDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "patientId")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Not Ordered",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "negationRationale"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "code")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Not Performed",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "negationRationale"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "code")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Not Recommended",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "negationRationale"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "code")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Order",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "reason"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "requester"),
                            new AttributeDto(DOM.createUniqueId(), "patientId")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Performed",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "method"),
                            new AttributeDto(DOM.createUniqueId(), "components"),
                            new AttributeDto(DOM.createUniqueId(), "relevantDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "relevantPeriod"),
                            new AttributeDto(DOM.createUniqueId(), "result"),
                            new AttributeDto(DOM.createUniqueId(), "reason"),
                            new AttributeDto(DOM.createUniqueId(), "relatedTo"),
                            new AttributeDto(DOM.createUniqueId(), "performer"),
                            new AttributeDto(DOM.createUniqueId(), "id")
                    )),
            new DataType(DOM.createUniqueId(), "Assessment, Recommended",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "reason"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "requester"),
                            new AttributeDto(DOM.createUniqueId(), "id")
                    )),
            new DataType(DOM.createUniqueId(), "Care Goal",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "targetOutcome"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "relevantPeriod"),
                            new AttributeDto(DOM.createUniqueId(), "statusDate"),
                            new AttributeDto(DOM.createUniqueId(), "relatedTo"),
                            new AttributeDto(DOM.createUniqueId(), "performer"),
                            new AttributeDto(DOM.createUniqueId(), "id")
                    )),
            new DataType(DOM.createUniqueId(), "Communication, Not Performed",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "category"),
                            new AttributeDto(DOM.createUniqueId(), "negationRationale"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "sender"),
                            new AttributeDto(DOM.createUniqueId(), "recipient"),
                            new AttributeDto(DOM.createUniqueId(), "id")
                    )),
            new DataType(DOM.createUniqueId(), "Communication, Performed",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "patientId"),
                            new AttributeDto(DOM.createUniqueId(), "category"),
                            new AttributeDto(DOM.createUniqueId(), "medium"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "sentDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "receivedDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "authorDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "sender"),
                            new AttributeDto(DOM.createUniqueId(), "recipient"),
                            new AttributeDto(DOM.createUniqueId(), "basedOn")
                    )),
            new DataType(DOM.createUniqueId(), "Device, Applied",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "anatomicalLocationSite"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "reason"),
                            new AttributeDto(DOM.createUniqueId(), "relevantPeriod"),
                            new AttributeDto(DOM.createUniqueId(), "sentDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "relevantDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "performer"),
                            new AttributeDto(DOM.createUniqueId(), "patientId")
                    )),
            new DataType(DOM.createUniqueId(), "Device, Not Applied",
                    Arrays.asList(
                            new AttributeDto(DOM.createUniqueId(), "anatomicalLocationSite"),
                            new AttributeDto(DOM.createUniqueId(), "code"),
                            new AttributeDto(DOM.createUniqueId(), "reason"),
                            new AttributeDto(DOM.createUniqueId(), "relevantPeriod"),
                            new AttributeDto(DOM.createUniqueId(), "sentDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "id"),
                            new AttributeDto(DOM.createUniqueId(), "relevantDatetime"),
                            new AttributeDto(DOM.createUniqueId(), "performer"),
                            new AttributeDto(DOM.createUniqueId(), "patientId")
                    ))
    );

    public static class AttributeDto {
        private String id;
        private String name;

        public AttributeDto() {
        }

        public AttributeDto(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class DataType {
        private String id;
        private String name;
        private List<AttributeDto> attributes = new ArrayList<>();


        public DataType(String id, String name, List<AttributeDto> attributes) {
            this.id = id;
            this.name = name;
            this.attributes = attributes;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<AttributeDto> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<AttributeDto> attributes) {
            this.attributes = attributes;
        }
    }

}
