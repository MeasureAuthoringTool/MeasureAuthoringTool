package mat.client.cqlworkspace.attributes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.FhirAttribute;
import mat.client.shared.FhirDataType;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LightBoxLeftPanelView implements LightBoxLeftPanelDisplay {

    private final Panel rootLeftPanel;
    private final Map<String, DataTypeSelectSectionDisplay> dataTypeSections = new LinkedHashMap<>();
    private final HandlerManager eventBus = new HandlerManager(this);

    public LightBoxLeftPanelView(String id, InsertFhirAttributesDialogModel model, String width, String height) {
        PanelGroup selectionPanelGroup = new PanelGroup();
        selectionPanelGroup.setId(id);
        selectionPanelGroup.setWidth("100%");
        selectionPanelGroup.setHeight("480px");

        for (FhirDataType dataType : model.getDataTypes().values()) {
            DataTypeSelectSelectSectionView dataTypeSection = new DataTypeSelectSelectSectionView(selectionPanelGroup.getId(), dataType, eventBus);
            dataTypeSections.put(dataTypeSection.getId(), dataTypeSection);
            selectionPanelGroup.add(dataTypeSection.asWidget());
        }

        PanelHeader leftPanelLabel = GWT.create(PanelHeader.class);
        leftPanelLabel.setText("Attributes:");
        leftPanelLabel.setTitle("Attributes:");

        rootLeftPanel = GWT.create(Panel.class);
        rootLeftPanel.setType(PanelType.PRIMARY);
        rootLeftPanel.setWidth(width);
        rootLeftPanel.setHeight("100%");
        rootLeftPanel.add(leftPanelLabel);

        ScrollPanel scroller = GWT.create(ScrollPanel.class);
        scroller.setWidget(selectionPanelGroup);
        scroller.setSize("100%", height);
        rootLeftPanel.add(scroller);
    }

    @Override
    public Widget asWidget() {
        return rootLeftPanel;
    }

    @Override
    public HandlerRegistration addSelectionHandler(FhirAttributeSelectionEvent.Handler handler) {
        return eventBus.addHandler(FhirAttributeSelectionEvent.TYPE, handler);
    }

    @Override
    public Collection<DataTypeSelectSectionDisplay> getSections() {
        return dataTypeSections.values();
    }

    public static class DataTypeSelectSelectSectionView implements DataTypeSelectSectionDisplay {

        private final String id;
        private final String name;
        private final PanelBody panelBody;
        private final Panel dataTypeSectionPanel;
        private final HandlerManager eventBus;

        public DataTypeSelectSelectSectionView(String parentViewId, FhirDataType dataType, HandlerManager eventBus) {
            this.id = dataType.getId();
            this.name = dataType.getFhirResource();
            this.eventBus = eventBus;
            dataTypeSectionPanel = GWT.create(Panel.class);
            dataTypeSectionPanel.setType(PanelType.DEFAULT);
            dataTypeSectionPanel.setId("DataTypeSelectSelectSectionView_dataType_" + id);

            PanelCollapse panelCollapse = GWT.create(PanelCollapse.class);
            panelCollapse.setId("DataTypeSelectSelectSectionView_collapse_" + id);

            panelBody = GWT.create(PanelBody.class);
            panelCollapse.add(panelBody);

            PanelHeader header = createDataTypeSectionHeader(dataType);
            header.setDataParent(parentViewId);
            header.setDataTargetWidget(panelCollapse);
            header.setDataToggle(Toggle.COLLAPSE);

            dataTypeSectionPanel.add(header);
            dataTypeSectionPanel.add(panelCollapse);

            addAttributes(dataType);
        }

        private PanelHeader createDataTypeSectionHeader(FhirDataType dataType) {
            PanelHeader panelHeader = GWT.create(PanelHeader.class);
            Heading sectionHeading = new Heading(HeadingSize.H4, SafeHtmlUtils.htmlEscape(dataType.getFhirResource()));
            panelHeader.add(sectionHeading);
            return panelHeader;
        }

        private void addAttributes(FhirDataType dataType) {
            for (FhirAttribute attribute : dataType.getAttributes().values()) {
                CheckBox checkBox = GWT.create(CheckBox.class);
                checkBox.setId("DataTypeSelectSelectSectionView_checkBox_" + attribute.getId());
                checkBox.setText(SafeHtmlUtils.htmlEscape(attribute.getFhirElement()));
                checkBox.addValueChangeHandler(event -> {
                    FhirAttributeSelectionEvent fhirAttributeSelectionEvent = new FhirAttributeSelectionEvent();
                    fhirAttributeSelectionEvent.setSelected(event.getValue());
                    fhirAttributeSelectionEvent.setAttribute(attribute);
                    eventBus.fireEvent(fhirAttributeSelectionEvent);
                });
                panelBody.add(checkBox);
            }
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Widget asWidget() {
            return dataTypeSectionPanel;
        }
    }
}
