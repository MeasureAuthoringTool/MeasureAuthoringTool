package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.ButtonGroupSize;

import mat.model.cql.CQLLibraryDataSetObject;

public class CQLibraryGridToolbar extends ButtonGroup {

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
        setSize(ButtonGroupSize.SMALL);
        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(deleteButton);


        applyDefault();
    }

    public void applyDefault() {
        versionButton.setText("Create Version/Draft");
        versionButton.setEnabled(false);
        versionButton.setStyleName("btn btn-default disabled fa fa-star fa-lg");
        versionButton.setTitle("Click to create Version");
        versionButton.setWidth("135px");

        historyButton.setText("History");
        historyButton.setEnabled(false);
        historyButton.setStyleName("btn btn-default disabled fa fa-clock-o fa-lg");
        historyButton.setTitle("Click to view history");
        historyButton.setWidth("69px");

        editButton.setText("Edit");
        editButton.setEnabled(false);
        editButton.setStyleName("btn btn-default disabled fa fa-pencil fa-lg");
        editButton.setWidth("53px");

        shareButton.setText("Share");
        shareButton.setEnabled(false);
        shareButton.setStyleName("btn btn-default disabled fa fa-share-square fa-lg");
        shareButton.setWidth("60px");

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.setStyleName("btn btn-default disabled fa fa-trash fa-lg");
        deleteButton.setWidth("63px");
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
            versionButton.setText("Create Draft");
            versionButton.setStyleName("btn btn-default fa fa-pencil-square-o fa-lg");
            versionButton.setTitle("Click to create draft");
        } else {
            versionButton.setText("Create Version");
            versionButton.setStyleName("btn btn-default fa fa-star fa-lg");
            versionButton.setTitle("Click to create version");
        }

        historyButton.setEnabled(true);
        historyButton.setText("History");
        historyButton.setStyleName("btn btn-default fa fa-clock-o fa-lg");
        historyButton.setTitle("History");

        if (selectedItem.isEditable()) {
            if (selectedItem.isLocked()) {
                editButton.setText("Edit");
                editButton.setEnabled(false);
                editButton.setStyleName("btn btn-default disabled fa fa-lock fa-lg");
                editButton.setTitle("Library in use by " + selectedItem.getLockedUserInfo().getEmailAddress());
            } else {
                editButton.setText("Edit");
                editButton.setEnabled(true);
                editButton.setStyleName("btn btn-default fa fa-pencil fa-lg");
                editButton.setTitle("Click to edit");
            }
        } else {
            editButton.setText("Edit");
            editButton.setEnabled(false);
            editButton.setStyleName("btn btn-default disabled fa fa-newspaper-o fa-lg");
            editButton.setTitle("Read-Only");
        }

        shareButton.setText("Share");
        shareButton.setEnabled(true);
        shareButton.setEnabled(selectedItem.isSharable());
        shareButton.setStyleName("btn btn-default fa fa-share-square fa-lg");
        shareButton.setTitle("Click to share");


        deleteButton.setText("Delete");
        deleteButton.setEnabled(true);
        deleteButton.setStyleName("btn btn-default fa fa-trash fa-lg");
        deleteButton.setTitle("Click to delete library");
    }

}
