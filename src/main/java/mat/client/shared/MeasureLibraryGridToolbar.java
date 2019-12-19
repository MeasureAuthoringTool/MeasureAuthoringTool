package mat.client.shared;

import java.util.Collection;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import mat.client.measure.ManageMeasureSearchModel;

public class MeasureLibraryGridToolbar extends HorizontalFlowPanel {

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button cloneButton;
    private Button exportButton;

    public MeasureLibraryGridToolbar() {
        setStyleName("action-button-bar");
        addStyleName("btn-group");
        addStyleName("btn-group-sm");

        versionButton = GWT.create(Button.class);
        historyButton = GWT.create(Button.class);
        editButton = GWT.create(Button.class);
        shareButton = GWT.create(Button.class);
        cloneButton = GWT.create(Button.class);
        exportButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(cloneButton);
        add(exportButton);


        applyDefault();
    }

    private void applyDefault() {
        applyDefaultAllButExport();

        exportButton.setText("Export");
        exportButton.setTitle("Click to export");
        historyButton.setWidth("72px");
        buildActionButton(exportButton, IconType.DOWNLOAD);
    }

    private void applyDefaultAllButExport() {
        versionButton.setText("Create Version/Draft");
        versionButton.setTitle("Click to create version or draft");
        versionButton.setWidth("146px");
        buildActionButton(versionButton, IconType.STAR);

        historyButton.setText("History");
        historyButton.setTitle("Click to view history");
        historyButton.setWidth("73px");
        buildActionButton(historyButton, IconType.CLOCK_O);

        editButton.setText("Edit");
        editButton.setTitle("Click to edit");
        editButton.setWidth("57px");
        buildActionButton(editButton, IconType.EDIT);

        shareButton.setText("Share");
        shareButton.setTitle("Click to share");
        shareButton.setWidth("68px");
        buildActionButton(shareButton, IconType.SHARE_SQUARE);

        cloneButton.setText("Clone");
        cloneButton.setTitle("Click to clone");
        editButton.setWidth("69px");
        buildActionButton(cloneButton, IconType.CLONE);
    }

    private Button buildActionButton(Button actionButton, IconType icon) {
        actionButton.setType(ButtonType.DEFAULT);
        actionButton.setSize(ButtonSize.SMALL);
        actionButton.setIcon(icon);
        actionButton.setEnabled(false);
        return actionButton;
    }

    public void updateOnSelectionChanged(Collection<ManageMeasureSearchModel.Result> selectedItems) {
        applyDefault();
        if (selectedItems.isEmpty()) {
            return;
        }
        if (selectedItems.size() > 1) {
            exportButton.setEnabled(true);
            return;
        }
        ManageMeasureSearchModel.Result selectedItem = selectedItems.iterator().next();

        if (selectedItem.isDraftable()) {
            versionButton.setText("Create Draft");
            versionButton.setTitle("Click to create draft");
            versionButton.setEnabled(true);
        } else if (selectedItem.isVersionable()) {
            versionButton.setText("Create Version");
            versionButton.setTitle("Click to create version");
            versionButton.setEnabled(true);
        }

        historyButton.setEnabled(true);

        if (selectedItem.isEditable()) {
            if (selectedItem.isMeasureLocked()) {
                String emailAddress = selectedItem.getLockedUserInfo().getEmailAddress();
                editButton.setTitle("Measure in use by " + emailAddress);
                editButton.setIcon(IconType.LOCK);
            } else {
                editButton.setTitle("Click to edit");
                editButton.setIcon(IconType.PENCIL);
                editButton.setEnabled(true);
            }
        } else {
            editButton.setTitle("Read-Only");
            editButton.setIcon(IconType.NEWSPAPER_O);
        }

        shareButton.setEnabled(selectedItem.isSharable());

        if (!selectedItem.isClonable()) {
            cloneButton.setTitle(selectedItem.getIsComposite() ? "Composite measure not cloneable" : "Measure not cloneable");
            cloneButton.setEnabled(false);
        } else {
            cloneButton.setEnabled(true);
        }

        exportButton.setEnabled(true);
    }

}
