package mat.client.cqlworkspace.attributes;

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

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class LightBoxLeftPanelView implements LightBoxLeftPanelDisplay {

    private Panel rootPanel;

    public LightBoxLeftPanelView(String id, InsertFhirAttributesDialogModel model, String width, String height) {
        PanelGroup attributesSelectLeftPanel = new PanelGroup();
        attributesSelectLeftPanel.setId(id);
        attributesSelectLeftPanel.setWidth("100%");
        attributesSelectLeftPanel.setHeight("480px");

        for (FhirDataType dataType : model.getDataTypes()) {
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

            for (FhirAttribute attribute : dataType.getAttributes()) {
                CheckBox checkBox = new CheckBox(attribute.getName());
                checkBox.setId(attributesSelectLeftPanel.getId() + "_checkbox_" + dataType.getId() + "_" + attribute.getId());
                panelBody.add(checkBox);
            }

            panelHeader.setDataParent(attributesSelectLeftPanel.getId());
            panelHeader.setDataTargetWidget(panelCollapse);
            panelHeader.setDataToggle(Toggle.COLLAPSE);

            attributesSelectLeftPanel.add(dataTypeContainerPanel);
        }

        PanelHeader leftPanelLabel = new PanelHeader();
        leftPanelLabel.setText("Attributes:");
        leftPanelLabel.setTitle("Attributes:");

        rootPanel = new Panel(PanelType.PRIMARY);
        rootPanel.setWidth(width);
        rootPanel.setHeight("100%");
        rootPanel.add(leftPanelLabel);

        ScrollPanel scroller = new ScrollPanel(attributesSelectLeftPanel);
        scroller.setSize("100%", height);
        rootPanel.add(scroller);
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }

}
