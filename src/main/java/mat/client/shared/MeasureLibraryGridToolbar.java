package mat.client.shared;

import java.util.Collection;

import com.google.common.annotations.VisibleForTesting;
import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.util.FeatureFlagConstant;

public class MeasureLibraryGridToolbar extends HorizontalFlowPanel {

    private static final String VIEW_TEXT = "View";

    private Options options;

    private Button versionButton;
    private Button historyButton;
    private Button editOrViewButton;
    private Button shareButton;
    private Button cloneButton;
    private Button exportButton;
    private Button fhirValidationButton;
    private Button convertButton;

    public MeasureLibraryGridToolbar() {
        setStyleName("action-button-bar");
        addStyleName("btn-group");
        addStyleName("btn-group-sm");

        options = new Options();

        versionButton = GWT.create(Button.class);
        historyButton = GWT.create(Button.class);
        editOrViewButton = GWT.create(Button.class);
        shareButton = GWT.create(Button.class);
        cloneButton = GWT.create(Button.class);
        exportButton = GWT.create(Button.class);
        fhirValidationButton = GWT.create(Button.class);
        convertButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editOrViewButton);
        add(shareButton);
        add(cloneButton);
        add(exportButton);
        add(fhirValidationButton);
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
        convertButton.setVisible(options.isConvertButtonVisible());
        fhirValidationButton.setVisible(options.isFhirValidationButtonVisible());
    }

    private void applyDefaultAllButExport() {
        buildButton(versionButton, IconType.STAR, "Create Version or Draft", "Click to create version or draft", "160px");
        buildButton(historyButton, IconType.CLOCK_O, "History", "Click to view history", "73px");
        buildButton(editOrViewButton, IconType.EDIT, "Edit", "Click to edit", "64px");
        buildButton(shareButton, IconType.SHARE_SQUARE, "Share", "Click to share", "68px");
        buildButton(cloneButton, IconType.CLONE, "Clone", "Click to clone", "69px");
        buildButton(fhirValidationButton, IconType.FILE_TEXT_O, "Run FHIR Validation", "Click to Run FHIR Validation", "146px");
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

        ManageMeasureSearchModel.Result selectedItem = selectedItems.iterator().next();
        final boolean[] isExportable = {true};
        selectedItems.forEach(item -> {
            if (options.isFhirExportEnabled()) {
                isExportable[0] = isExportable[0] && item.isExportable();
                exportButton.setEnabled(isExportable[0]);
                exportButton.setTitle("Click to Export MAT " + item.getHqmfReleaseVersion());
            }
        });

        if (selectedItems.size() > 1) {
            exportButton.setTitle("Click to Export");
            return;
        }

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

        fhirValidationButton.setEnabled(selectedItem.isValidatable());

        if (selectedItem.isEditable() && selectedItem.isFhirEditOrViewable()) {
            if (selectedItem.isMeasureLocked()) {
                String emailAddress = selectedItem.getLockedUserInfo().getEmailAddress();
                editOrViewButton.setTitle("Measure in use by " + emailAddress);
                editOrViewButton.setIcon(IconType.LOCK);
            } else {
                editOrViewButton.setTitle("Click to edit");
                editOrViewButton.setIcon(IconType.PENCIL);
                editOrViewButton.setEnabled(true);
            }
        } else if (!selectedItem.isEditable() && selectedItem.isFhirEditOrViewable()) {
            editOrViewButton.setText(VIEW_TEXT);
            editOrViewButton.setEnabled(true);
            editOrViewButton.setTitle("Read-Only");
            editOrViewButton.setIcon(IconType.EYE);
        } else {
            editOrViewButton.setEnabled(false);
        }

        if (selectedItems.size() == 1) {
            if (ModelTypeHelper.isFhir(selectedItem.getMeasureModel())) {
                if (MatContext.get().getFeatureFlagStatus("FhirShare")) {
                    shareButton.setEnabled(selectedItem.isSharable());
                }
            } else {
                shareButton.setEnabled(selectedItem.isSharable());
            }
        }

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

    public Button getEditOrViewButton() {
        return editOrViewButton;
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

    public Button getFhirValidationButton() {
        return fhirValidationButton;
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
        private boolean isFhirValidationButtonVisible;
        private boolean isFhirExportEnabled;

        public Options() {
        }

        public boolean isConvertButtonVisible() {
            return isConvertButtonVisible;
        }

        public void setConvertButtonVisible(boolean convertButtonVisible) {
            isConvertButtonVisible = convertButtonVisible;
        }

        public boolean isFhirValidationButtonVisible() {
            return isFhirValidationButtonVisible;
        }

        public void setFhirValidationButtonVisible(boolean isFhirValidationButtonVisible) {
            this.isFhirValidationButtonVisible = isFhirValidationButtonVisible;
        }

        public void setFhirExportEnabled(boolean fhirExportEnabled) {
            isFhirExportEnabled = fhirExportEnabled;
        }

        public boolean isFhirExportEnabled() {
            return isFhirExportEnabled;
        }

        public static Options fromFeatureFlags() {
            Options options = new Options();
            options.setConvertButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.FHIR_CONV_V1));
            options.setFhirValidationButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.FHIR_CONV_V1));
            options.setFhirExportEnabled(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.EXPORT_V1));
            return options;
        }
    }

}
