package mat.client.shared;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwt.user.client.ui.Button;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.model.LockedUserInfo;
import mat.model.cql.CQLLibraryDataSetObject;

@RunWith(GwtMockitoTestRunner.class)
public class CQLibraryGridToolbarTest {

    @Mock(name = "versionButton")
    private Button versionButton;
    @Mock(name = "historyButton")
    private Button historyButton;
    @Mock(name = "editButton")
    private Button editButton;
    @Mock(name = "shareButton")
    private Button shareButton;
    @Mock(name = "deleteButton")
    private Button deleteButton;
    @InjectMocks
    private CQLibraryGridToolbar toolbar;

    @Test
    public void testApplyDefaultVersionButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-star fa-lg"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq(" Create Version/Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create Version/Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setWidth(Mockito.eq("135px"));
    }

    @Test
    public void testApplyDefaultHistoryButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-clock-o fa-lg"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq(" History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setWidth(Mockito.eq("69px"));
    }

    @Test
    public void testApplyDefaultDeleteButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-trash fa-lg"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq(" Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setWidth(Mockito.eq("63px"));
    }

    @Test
    public void testApplyDefaultEditButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-pencil fa-lg"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq(" Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setWidth(Mockito.eq("53px"));
    }

    @Test
    public void testApplyDefaultShareButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-share-square fa-lg"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq(" Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setWidth(Mockito.eq("60px"));
    }

    @Test
    public void testVersionButtonNotDraftable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-star fa-lg"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq(" Create Version"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create version"));
    }

    @Test
    public void testVersionButtonDraftable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-pencil-square-o fa-lg"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq(" Create Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create draft"));
    }

    @Test
    public void testHistory() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-clock-o fa-lg"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq(" History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
    }

    @Test
    public void testShareNotSharable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-share-square fa-lg"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq(" Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
    }

    @Test
    public void testShareSharable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setSharable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-share-square fa-lg"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq(" Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
    }

    @Test
    public void testDeleteNotDeletable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-trash fa-lg"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq(" Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
    }

    @Test
    public void testDeleteDeletable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDeletable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-trash fa-lg"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq(" Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
    }

    @Test
    public void testEditNotEditable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-newspaper-o fa-lg"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq(" Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Read-Only"));
    }

    @Test
    public void testEditEditableLocked() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setEditable(true);
        selectedItem.setLockedUserInfo(new LockedUserInfo());
        selectedItem.getLockedUserInfo().setEmailAddress("lockerby@gmail.com");
        selectedItem.setLocked(true);

        toolbar.updateOnSelectionChanged(selectedItem);

        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-lock fa-lg"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq(" Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Library in use by lockerby@gmail.com"));
    }

    @Test
    public void testEditEditableNotLocked() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setEditable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default fa fa-pencil fa-lg"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq(" Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
    }


}
