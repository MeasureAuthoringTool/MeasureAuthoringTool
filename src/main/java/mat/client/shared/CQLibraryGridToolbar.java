package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.constants.ButtonGroupSize;

public class CQLibraryGridToolbar extends ButtonGroup {

    private Button versionButton;
    private Button historyButton;
    private Button editButton;
    private Button shareButton;
    private Button deleteButton;

    public CQLibraryGridToolbar() {
        versionButton = new Button("Create Version/Draft");
        versionButton.setEnabled(false);
        versionButton.addStyleName("fa fa-star fa-lg");
        versionButton.setTitle("Click to create Version");

        historyButton = new Button("History");
        historyButton.setEnabled(false);
        historyButton.addStyleName("fa fa-clock-o fa-lg");
        historyButton.setTitle("Click to view history");

        editButton = new Button("Edit");
        editButton.setEnabled(false);
        editButton.addStyleName("fa fa-pencil fa-lg");

        shareButton = new Button("Share");
        shareButton.setEnabled(false);
        shareButton.addStyleName("fa fa-share-square fa-lg");

        deleteButton = new Button("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addStyleName("fa fa-trash fa-lg");

        setSize(ButtonGroupSize.SMALL);
        add(versionButton);
        add(historyButton);
        add(editButton);
        add(shareButton);
        add(deleteButton);
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
}
