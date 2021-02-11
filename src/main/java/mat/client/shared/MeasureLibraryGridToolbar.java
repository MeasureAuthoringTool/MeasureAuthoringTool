package mat.client.shared;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.util.FeatureFlagConstant;
import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Collection;

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
    private String ariaLabelPrefix;

    public MeasureLibraryGridToolbar(String ariaLabelPrefix) {
        this.ariaLabelPrefix = ariaLabelPrefix;
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
        buildButton(versionButton, IconType.STAR, "Create Version or Draft", ariaLabelPrefix + " Create version or draft", "160px");
        buildButton(historyButton, IconType.CLOCK_O, "History", ariaLabelPrefix + " View History", "73px");
        buildButton(editOrViewButton, IconType.EDIT, "Edit", ariaLabelPrefix + " Edit", "64px");
        buildButton(shareButton, IconType.SHARE_SQUARE, "Share", ariaLabelPrefix + " Share", "68px");
        buildButton(cloneButton, IconType.CLONE, "Clone", ariaLabelPrefix + " Clone", "69px");
        buildButton(fhirValidationButton, IconType.FILE_TEXT_O, "Run FHIR Validation", ariaLabelPrefix + " Run FHIR Validation", "146px");
        buildButton(convertButton, IconType.RANDOM, "Convert to FHIR", ariaLabelPrefix + " Convert", "124px");
    }

    private void buildButton(Button actionButton, IconType icon, String text, String title, String width) {
        actionButton.setText(text);
        actionButton.getElement().setAttribute("aria-label",title);
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
        if (ModelTypeHelper.isQdm(selectedItem.getMeasureModel()) ||
                ModelTypeHelper.isFhir(selectedItem.getMeasureModel()) && options.isFhirExportEnabled()) {
            exportButton.setEnabled(selectedItems.iterator().next().isExportable());
            exportButton.setTitle("Click to Export MAT " + selectedItem.getHqmfReleaseVersion());
        }

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

        if (selectedItem.isEditable() && selectedItem.isMeasureEditOrViewable()) {
            if (selectedItem.isMeasureLocked()) {
                String emailAddress = selectedItem.getLockedUserInfo().getEmailAddress();
                editOrViewButton.setTitle("Measure in use by " + emailAddress);
                editOrViewButton.setIcon(IconType.LOCK);
            } else {
                editOrViewButton.setTitle("Click to edit");
                editOrViewButton.setIcon(IconType.PENCIL);
                editOrViewButton.setEnabled(true);
            }
        } else if (!selectedItem.isEditable() && selectedItem.isMeasureEditOrViewable()) {
            editOrViewButton.setText(VIEW_TEXT);
            editOrViewButton.setEnabled(true);
            editOrViewButton.setTitle("Read-Only");
            editOrViewButton.setIcon(IconType.EYE);
        } else {
            editOrViewButton.setEnabled(false);
        }

        if (selectedItems.size() == 1) {
            if (ModelTypeHelper.isFhir(selectedItem.getMeasureModel())) {
                if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
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

    public static MeasureLibraryGridToolbar withOptionsFromFlags(String ariaLabelPrefix) {
        return withOptions(ariaLabelPrefix, Options.fromFeatureFlags());
    }

    @VisibleForTesting
    static MeasureLibraryGridToolbar withOptions(String ariaLabelPrefix, Options options) {
        MeasureLibraryGridToolbar toolbar = new MeasureLibraryGridToolbar(ariaLabelPrefix);
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
            options.setConvertButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR));
            options.setFhirValidationButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR));
            options.setFhirExportEnabled(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR));
            return options;
        }
    }

}
