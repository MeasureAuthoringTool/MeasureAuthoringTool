package mat.client.shared;

import java.util.Collection;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.util.FeatureFlagConstant;

public class MeasureLibraryGridToolbar extends HorizontalFlowPanel {

    private Options options;

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button cloneButton;
    private Button exportButton;
    private Button convertButton;

    public MeasureLibraryGridToolbar() {
        setStyleName("action-button-bar");
        addStyleName("btn-group");
        addStyleName("btn-group-sm");

        this.options = new Options();

        this.versionButton = GWT.create(Button.class);
        this.historyButton = GWT.create(Button.class);
        this.editButton = GWT.create(Button.class);
        this.shareButton = GWT.create(Button.class);
        this.cloneButton = GWT.create(Button.class);
        this.exportButton = GWT.create(Button.class);
        this.convertButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(cloneButton);
        add(exportButton);
        add(convertButton);

        applyDefault();
    }


    @VisibleForTesting
    void applyDefault() {
        applyDefaultAllButExport();
        buildButton(exportButton, IconType.DOWNLOAD, "Export", "Click to export", "72px");
        applyOptions();
    }

    @VisibleForTesting
    void applyOptions() {
        convertButton.setVisible(options.isConvertButtonVisible);
    }

    private void applyDefaultAllButExport() {
        buildButton(versionButton, IconType.STAR, "Create Version or Draft", "Click to create version or draft", "160px");
        buildButton(historyButton, IconType.CLOCK_O, "History", "Click to view history", "73px");
        buildButton(editButton, IconType.EDIT, "Edit", "Click to edit", "57px");
        buildButton(shareButton, IconType.SHARE_SQUARE, "Share", "Click to share", "68px");
        buildButton(cloneButton, IconType.CLONE, "Clone", "Click to clone", "69px");
        buildButton(convertButton, IconType.RANDOM, "Convert to FHIR", "Click to convert", "124px");
    }

    private void buildButton(Button actionButton, IconType icon, String text, String title, String width) {
        actionButton.setText(text);
        actionButton.setTitle(title);
        actionButton.setWidth(width);
        actionButton.setType(ButtonType.DEFAULT);
        actionButton.setSize(ButtonSize.SMALL);
        actionButton.setIcon(icon);
        actionButton.setEnabled(false);
    }

    public void updateOnSelectionChanged(Collection<ManageMeasureSearchModel.Result> selectedItems) {
        applyDefault();
        if (selectedItems.isEmpty()) {
            return;
        }
        exportButton.setEnabled(selectedItems.stream().anyMatch(result -> result.isExportable()));
        if (selectedItems.size() > 1) {
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
            cloneButton.setTitle(Boolean.TRUE.equals(selectedItem.getIsComposite()) ? "Composite measure not cloneable" : "Measure not cloneable");
            cloneButton.setEnabled(false);
        } else {
            cloneButton.setEnabled(true);
        }

        convertButton.setEnabled(selectedItem.isFhirConvertible());
    }

    public Button getVersionButton() {
        return versionButton;
    }

    public Button getHistoryButton() {
        return historyButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getShareButton() {
        return shareButton;
    }

    public Button getCloneButton() {
        return cloneButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public Button getConvertButton() {
        return convertButton;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static MeasureLibraryGridToolbar withOptionsFromFlags() {
        return withOptions(Options.fromFeatureFlags());
    }

    @VisibleForTesting
    static MeasureLibraryGridToolbar withOptions(Options options) {
        MeasureLibraryGridToolbar toolbar = new MeasureLibraryGridToolbar();
        toolbar.setOptions(options);
        toolbar.applyOptions();
        return toolbar;
    }

    public static class Options {

        private boolean isConvertButtonVisible;

        public Options() {
        }

        public boolean isConvertButtonVisible() {
            return isConvertButtonVisible;
        }

        public void setConvertButtonVisible(boolean convertButtonVisible) {
            isConvertButtonVisible = convertButtonVisible;
        }

        public static Options fromFeatureFlags() {
            Options options = new Options();
            options.setConvertButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.FHIR_CONV_V1));
            return options;
        }
    }
}
