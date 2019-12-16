package mat.client.shared;


import com.google.gwt.user.client.ui.Button;
import mat.model.cql.CQLLibraryDataSetObject;

public class CQLibraryGridToolbar extends HorizontalFlowPanel {

    public static final String CLICK_TO_CREATE_VERSION_DRAFT_TITLE = "Click to create Version/Draft";
    public static final String CREATE_VERSION_DRAFT_TEXT = " Create Version/Draft";
    public static final String HISTORY_TEXT = " History";
    public static final String EDIT_TEXT = " Edit";
    public static final String SHARE_TEXT = " Share";
    public static final String DELETE_TEXT = " Delete";

    public static final String CLICK_TO_VIEW_HISTORY_TITLE = "Click to view history";
    public static final String CLICK_TO_EDIT_TITLE = "Click to edit";
    public static final String CLICK_TO_SHARE_TITLE = "Click to share";
    public static final String CLICK_TO_DELETE_LIBRARY_TITLE = "Click to delete library";

    public static final String VERSION_STYLE_DISABLED = "btn btn-default disabled fa fa-star fa-lg";
    public static final String HISTORY_STYLE_DISABLED = "btn btn-default disabled fa fa-clock-o fa-lg";
    public static final String EDIT_STYLE_DISABLED = "btn btn-default disabled fa fa-pencil fa-lg";
    public static final String SHARE_STYLE_DISABLED = "btn btn-default disabled fa fa-share-square fa-lg";
    public static final String DELETE_STYLE_DISABLED = "btn btn-default disabled fa fa-trash fa-lg";

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button deleteButton;

    public CQLibraryGridToolbar() {
        versionButton = new Button();
        historyButton = new Button();
        editButton = new Button();
        shareButton = new Button();
        deleteButton = new Button();
        addStyleName("btn-group");
        addStyleName("btn-group-sm");
        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(deleteButton);

        applyDefault();
    }

    public void applyDefault() {
        versionButton.setText(CREATE_VERSION_DRAFT_TEXT);
        versionButton.setEnabled(false);
        versionButton.setStyleName(VERSION_STYLE_DISABLED);
        versionButton.setTitle(CLICK_TO_CREATE_VERSION_DRAFT_TITLE);
        versionButton.setWidth("135px");

        historyButton.setText(HISTORY_TEXT);
        historyButton.setEnabled(false);
        historyButton.setStyleName(HISTORY_STYLE_DISABLED);
        historyButton.setTitle(CLICK_TO_VIEW_HISTORY_TITLE);
        historyButton.setWidth("69px");

        editButton.setText(EDIT_TEXT);
        editButton.setEnabled(false);
        editButton.setStyleName(EDIT_STYLE_DISABLED);
        editButton.setWidth("53px");
        editButton.setTitle(CLICK_TO_EDIT_TITLE);

        shareButton.setText(SHARE_TEXT);
        shareButton.setEnabled(false);
        shareButton.setStyleName(SHARE_STYLE_DISABLED);
        shareButton.setWidth("60px");
        shareButton.setTitle(CLICK_TO_SHARE_TITLE);

        deleteButton.setText(DELETE_TEXT);
        deleteButton.setEnabled(false);
        deleteButton.setStyleName(DELETE_STYLE_DISABLED);
        deleteButton.setWidth("63px");
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
            applyDefault();
            return;
        }

        versionButton.setEnabled(true);
        if (selectedItem.isDraftable()) {
            versionButton.setText(" Create Draft");
            versionButton.setStyleName("btn btn-default fa fa-pencil-square-o fa-lg");
            versionButton.setTitle("Click to create draft");
        } else {
            versionButton.setText(" Create Version");
            versionButton.setStyleName("btn btn-default fa fa-star fa-lg");
            versionButton.setTitle("Click to create version");
        }

        historyButton.setEnabled(true);
        historyButton.setText(HISTORY_TEXT);
        historyButton.setStyleName("btn btn-default fa fa-clock-o fa-lg");
        historyButton.setTitle(CLICK_TO_VIEW_HISTORY_TITLE);

        if (selectedItem.isEditable()) {
            if (selectedItem.isLocked()) {
                editButton.setText(EDIT_TEXT);
                editButton.setEnabled(false);
                editButton.setStyleName("btn btn-default disabled fa fa-lock fa-lg");
                editButton.setTitle("Library in use by " + selectedItem.getLockedUserInfo().getEmailAddress());
            } else {
                editButton.setText(EDIT_TEXT);
                editButton.setEnabled(true);
                editButton.setStyleName("btn btn-default fa fa-pencil fa-lg");
                editButton.setTitle(CLICK_TO_EDIT_TITLE);
            }
        } else {
            editButton.setText(EDIT_TEXT);
            editButton.setEnabled(false);
            editButton.setStyleName("btn btn-default disabled fa fa-newspaper-o fa-lg");
            editButton.setTitle("Read-Only");
        }

        shareButton.setText(SHARE_TEXT);
        shareButton.setEnabled(selectedItem.isSharable());
        if (selectedItem.isSharable()) {
            shareButton.setStyleName("btn btn-default fa fa-share-square fa-lg");
        } else {
            shareButton.setStyleName("btn btn-default disabled fa fa-share-square fa-lg");
        }
        shareButton.setTitle(CLICK_TO_SHARE_TITLE);


        deleteButton.setText(DELETE_TEXT);
        deleteButton.setEnabled(selectedItem.isDeletable());
        if (selectedItem.isDeletable()) {
            deleteButton.setStyleName("btn btn-default fa fa-trash fa-lg");
        } else {
            deleteButton.setStyleName("btn btn-default disabled fa fa-trash fa-lg");
        }
        deleteButton.setTitle(CLICK_TO_DELETE_LIBRARY_TITLE);
    }

}
