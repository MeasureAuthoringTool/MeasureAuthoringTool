package mat.client.shared;

import java.util.Collection;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import mat.client.util.FeatureFlagConstant;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;
import mat.client.measure.ManageMeasureSearchModel;

public class MeasureLibraryGridToolbar extends HorizontalFlowPanel {

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button cloneButton;
    private Button exportButton;
    private Button fhirValidationButton;
    private float version;

    private static final float RUN_FHIR_VALIDATION_VERSION = 5.8F;

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
        fhirValidationButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(cloneButton);
        add(exportButton);

        applyDefault();
    }

    @VisibleForTesting
    void applyDefault() {
        applyDefaultAllButExport();
        buildButton(exportButton, IconType.DOWNLOAD, "Export", "Click to export", "72px");
    }

    public void addFhirValidationButton(){
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.FHIR_CONV_V1)) {
            add(fhirValidationButton);
            buildButton(fhirValidationButton, IconType.FILE_TEXT_O, "Run FHIR Validation", "Click to Run FHIR Validation", "146px");
        }
    }

    private void applyDefaultAllButExport() {
        buildButton(versionButton, IconType.STAR, "Create Version or Draft", "Click to create version or draft", "160px");
        buildButton(historyButton, IconType.CLOCK_O, "History", "Click to view history", "73px");
        buildButton(editButton, IconType.EDIT, "Edit", "Click to edit", "57px");
        buildButton(shareButton, IconType.SHARE_SQUARE, "Share", "Click to share", "68px");
        buildButton(cloneButton, IconType.CLONE, "Clone", "Click to clone", "69px");
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

        //Run FHIR validation Button
        RegExp regExp = RegExp.compile("[0-9]+\\.[0-9]+");
        MatchResult matcher = regExp.exec(selectedItem.getVersion());
        boolean matchFound = matcher != null;
        if (matchFound) {
            version = Float.parseFloat(matcher.getGroup(0));
        }

        String modelType = selectedItem.getMeasureModel();

        if(modelType != null && ((selectedItem.isDraft() && modelType.equals("FHIR")) ||
            (selectedItem.isDraftable() && modelType.equals("QDM") && version > RUN_FHIR_VALIDATION_VERSION)) ){
            fhirValidationButton.setEnabled(true);
        }else{
            fhirValidationButton.setEnabled(false);
        }

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

    public Button getFhirValidationButton(){
        return fhirValidationButton;
    }

}
