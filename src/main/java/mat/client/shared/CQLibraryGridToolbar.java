package mat.client.shared;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import mat.model.cql.CQLLibraryDataSetObject;

public class CQLibraryGridToolbar extends HorizontalFlowPanel {

    public static final String CREATE_VERSION_DRAFT_TEXT = "Create Version/Draft";
    public static final String HISTORY_TEXT = "History";
    public static final String EDIT_TEXT = "Edit";
    public static final String SHARE_TEXT = "Share";
    public static final String DELETE_TEXT = "Delete";

    public static final String CLICK_TO_CREATE_VERSION_DRAFT_TITLE = "Click to create version or draft";
    public static final String CLICK_TO_VIEW_HISTORY_TITLE = "Click to view history";
    public static final String CLICK_TO_EDIT_TITLE = "Click to edit";
    public static final String CLICK_TO_SHARE_TITLE = "Click to share";
    public static final String CLICK_TO_DELETE_LIBRARY_TITLE = "Click to delete library";

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button deleteButton;

    public CQLibraryGridToolbar() {
        setStyleName("action-button-bar");
        addStyleName("btn-group");
        addStyleName("btn-group-sm");

        versionButton = GWT.create(Button.class);
        historyButton = GWT.create(Button.class);
        editButton = GWT.create(Button.class);
        shareButton = GWT.create(Button.class);
        deleteButton = GWT.create(Button.class);

        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(deleteButton);

        applyDefault();
    }

    public void applyDefault() {
        versionButton.setText(CREATE_VERSION_DRAFT_TEXT);
        versionButton.setType(ButtonType.DEFAULT);
        versionButton.setEnabled(false);
        versionButton.setIcon(IconType.STAR);
        versionButton.setTitle(CLICK_TO_CREATE_VERSION_DRAFT_TITLE);
        versionButton.setWidth("146px");

        historyButton.setText(HISTORY_TEXT);
        historyButton.setType(ButtonType.DEFAULT);
        historyButton.setEnabled(false);
        historyButton.setIcon(IconType.CLOCK_O);
        historyButton.setTitle(CLICK_TO_VIEW_HISTORY_TITLE);
        historyButton.setWidth("73px");

        editButton.setText(EDIT_TEXT);
        editButton.setType(ButtonType.DEFAULT);
        editButton.setEnabled(false);
        editButton.setIcon(IconType.PENCIL);
        editButton.setWidth("57px");
        editButton.setTitle(CLICK_TO_EDIT_TITLE);

        shareButton.setText(SHARE_TEXT);
        shareButton.setType(ButtonType.DEFAULT);
        shareButton.setEnabled(false);
        shareButton.setIcon(IconType.SHARE_SQUARE);
        shareButton.setWidth("68px");
        shareButton.setTitle(CLICK_TO_SHARE_TITLE);

        deleteButton.setText(DELETE_TEXT);
        deleteButton.setType(ButtonType.DEFAULT);
        deleteButton.setEnabled(false);
        deleteButton.setIcon(IconType.TRASH);
        deleteButton.setWidth("70px");
        deleteButton.setTitle(CLICK_TO_DELETE_LIBRARY_TITLE);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getEditButton() {
        return editButton;
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

    public void updateOnSelectionChanged(CQLLibraryDataSetObject selectedItem) {

        if (null == selectedItem) {
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

        if (selectedItem.isEditable()) {
            if (selectedItem.isLocked()) {
                editButton.setText(EDIT_TEXT);
                editButton.setEnabled(false);
                editButton.setIcon(IconType.LOCK);
                editButton.setTitle("Library in use by " + selectedItem.getLockedUserInfo().getEmailAddress());
            } else {
                editButton.setText(EDIT_TEXT);
                editButton.setEnabled(true);
                editButton.setIcon(IconType.PENCIL);
                editButton.setTitle(CLICK_TO_EDIT_TITLE);
            }
        } else {
            editButton.setText(EDIT_TEXT);
            editButton.setEnabled(false);
            editButton.setIcon(IconType.NEWSPAPER_O);
            editButton.setTitle("Read-Only");
        }

        shareButton.setText(SHARE_TEXT);
        shareButton.setEnabled(selectedItem.isSharable());
        shareButton.setIcon(IconType.SHARE_SQUARE);
        shareButton.setTitle(CLICK_TO_SHARE_TITLE);


        deleteButton.setText(DELETE_TEXT);
        deleteButton.setEnabled(selectedItem.isDeletable());
        deleteButton.setIcon(IconType.TRASH);
        deleteButton.setTitle(CLICK_TO_DELETE_LIBRARY_TITLE);
    }

}
