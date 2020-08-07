package mat.client.shared;

import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.GWT;
import mat.client.util.FeatureFlagConstant;
import mat.model.cql.CQLLibraryDataSetObject;

public class CQLibraryGridToolbar extends HorizontalFlowPanel {

    public static final String CREATE_VERSION_DRAFT_TEXT = "Create Version or Draft";
    public static final String HISTORY_TEXT = "History";
    public static final String EDIT_TEXT = "Edit";
    public static final String VIEW_TEXT = "View";
    public static final String SHARE_TEXT = "Share";
    public static final String DELETE_TEXT = "Delete";

    public static final String CLICK_TO_CREATE_VERSION_DRAFT_TITLE = "Click to create version or draft";
    public static final String CLICK_TO_VIEW_HISTORY_TITLE = "Click to view history";
    public static final String CLICK_TO_EDIT_TITLE = "Click to edit";
    public static final String CLICK_TO_SHARE_TITLE = "Click to share";
    public static final String CLICK_TO_DELETE_LIBRARY_TITLE = "Click to delete library";
    public static final String CONVERT_TO_FHIR_TEXT = "Convert to FHIR";
    public static final String CONVERT_TO_FHIR_TITLE = "Click to convert";

    private Button versionButton;
    private Button historyButton;
    private Button editOrViewButton;
    private Button shareButton;
    private Button deleteButton;
    private Button convertButton;
    private Options options;

    public CQLibraryGridToolbar() {
        setStyleName("action-button-bar");
        addStyleName("btn-group");
        addStyleName("btn-group-sm");

        options = new Options();

        versionButton = GWT.create(Button.class);
        historyButton = GWT.create(Button.class);
        editOrViewButton = GWT.create(Button.class);
        shareButton = GWT.create(Button.class);
        deleteButton = GWT.create(Button.class);
        convertButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editOrViewButton);
        add(shareButton);
        add(deleteButton);
        add(convertButton);

        applyDefault();
    }

    @VisibleForTesting
    public void applyDefault() {
        buildButton(versionButton, IconType.STAR, CREATE_VERSION_DRAFT_TEXT, CLICK_TO_CREATE_VERSION_DRAFT_TITLE, "160px");
        buildButton(historyButton, IconType.CLOCK_O, HISTORY_TEXT, CLICK_TO_VIEW_HISTORY_TITLE, "73px");
        buildButton(editOrViewButton, IconType.PENCIL, EDIT_TEXT, CLICK_TO_EDIT_TITLE, "64px");
        buildButton(shareButton, IconType.SHARE_SQUARE, SHARE_TEXT, CLICK_TO_SHARE_TITLE, "68px");
        buildButton(deleteButton, IconType.TRASH, DELETE_TEXT, CLICK_TO_DELETE_LIBRARY_TITLE, "70px");
        buildButton(convertButton, IconType.RANDOM, CONVERT_TO_FHIR_TEXT, CONVERT_TO_FHIR_TITLE, "124px");
        applyOptions();
    }

    @VisibleForTesting
    public void applyOptions() {
        convertButton.setVisible(options.isConvertButtonVisible());
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

    public void updateOnSelectionChanged(CQLLibraryDataSetObject selectedItem) {
        if (null == selectedItem) {
            applyDefault();
            return;
        }

        if (selectedItem.isDraftable()) {
            versionButton.setText("Create Draft");
            versionButton.setIcon(IconType.PENCIL_SQUARE_O);
            versionButton.setTitle("Click to create draft");
            versionButton.setEnabled(true);
        } else if (selectedItem.isVersionable()) {
            versionButton.setText("Create Version");
            versionButton.setIcon(IconType.STAR);
            versionButton.setTitle("Click to create version");
            versionButton.setEnabled(true);
        } else {
            versionButton.setText(CREATE_VERSION_DRAFT_TEXT);
            versionButton.setType(ButtonType.DEFAULT);
            versionButton.setEnabled(false);
            versionButton.setIcon(IconType.STAR);
            versionButton.setTitle(CLICK_TO_CREATE_VERSION_DRAFT_TITLE);
        }

        historyButton.setEnabled(true);
        historyButton.setText(HISTORY_TEXT);
        historyButton.setIcon(IconType.CLOCK_O);
        historyButton.setTitle(CLICK_TO_VIEW_HISTORY_TITLE);

        if (selectedItem.isEditable() && selectedItem.isFhirEditOrViewable()) {
            if (selectedItem.isLocked()) {
                editOrViewButton.setText(EDIT_TEXT);
                editOrViewButton.setEnabled(false);
                editOrViewButton.setIcon(IconType.LOCK);
                editOrViewButton.setTitle("Library in use by " + selectedItem.getLockedUserInfo().getEmailAddress());
            } else {
                editOrViewButton.setText(EDIT_TEXT);
                editOrViewButton.setEnabled(true);
                editOrViewButton.setIcon(IconType.PENCIL);
                editOrViewButton.setTitle(CLICK_TO_EDIT_TITLE);
            }
        } else if (!selectedItem.isEditable() && selectedItem.isFhirEditOrViewable()) {
            editOrViewButton.setText(VIEW_TEXT);
            editOrViewButton.setEnabled(true);
            editOrViewButton.setIcon(IconType.EYE);
            editOrViewButton.setTitle("Read-Only");
        } else {
            editOrViewButton.setEnabled(false);
        }


        shareButton.setText(SHARE_TEXT);
        shareButton.setIcon(IconType.SHARE_SQUARE);
        shareButton.setTitle(CLICK_TO_SHARE_TITLE);
        if (ModelTypeHelper.isFhir(selectedItem.getLibraryModelType())) {
            shareButton.setEnabled(selectedItem.isSharable() && MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR));
        } else {
            shareButton.setEnabled(selectedItem.isSharable());
        }

        deleteButton.setText(DELETE_TEXT);
        deleteButton.setEnabled(selectedItem.isDeletable());
        deleteButton.setIcon(IconType.TRASH);
        deleteButton.setTitle(CLICK_TO_DELETE_LIBRARY_TITLE);

        convertButton.setEnabled(selectedItem.isFhirConvertible());
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getEditOrViewButton() {
        return editOrViewButton;
    }

    public Button getHistoryButton() {
        return historyButton;
    }

    public Button getShareButton() {
        return shareButton;
    }

    public Button getVersionButton() {
        return versionButton;
    }

    public Button getConvertButton() {
        return convertButton;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static CQLibraryGridToolbar withOptionsFromFlags() {
        return withOptions(Options.fromFeatureFlags());
    }

    @VisibleForTesting
    static CQLibraryGridToolbar withOptions(Options options) {
        CQLibraryGridToolbar toolbar = new CQLibraryGridToolbar();
        toolbar.setOptions(options);
        toolbar.applyOptions();
        return toolbar;
    }

    public static class Options {

        private boolean isConvertButtonVisible;

        public boolean isConvertButtonVisible() {
            return isConvertButtonVisible;
        }

        public void setConvertButtonVisible(boolean convertButtonVisible) {
            isConvertButtonVisible = convertButtonVisible;
        }

        public static Options fromFeatureFlags() {
            Options options = new Options();
            options.setConvertButtonVisible(MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR));
            return options;
        }

    }
}
