package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.STAR));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Version or Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create version or draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setWidth(Mockito.eq("160px"));
    }

    @Test
    public void testApplyDefaultHistoryButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.CLOCK_O));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq("History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setWidth(Mockito.eq("73px"));
    }

    @Test
    public void testApplyDefaultDeleteButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.TRASH));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq("Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setWidth(Mockito.eq("70px"));
    }

    @Test
    public void testApplyDefaultEditButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setWidth(Mockito.eq("57px"));
    }

    @Test
    public void testApplyDefaultShareButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.SHARE_SQUARE));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq("Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setWidth(Mockito.eq("68px"));
    }

    @Test
    public void testVersionButtonNotDraftableVersionable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(false);
        selectedItem.setVersionable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.STAR));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Version"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create version"));
    }

    @Test
    public void testVersionButtonDraftableNotVersionable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(true);
        selectedItem.setVersionable(false);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL_SQUARE_O));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create draft"));
    }

    @Test
    public void testVersionButtonDraftableAndVersionable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(true);
        selectedItem.setVersionable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL_SQUARE_O));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create draft"));
    }

    @Test
    public void testVersionButtonNotDraftableNotVersionable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(false);
        selectedItem.setVersionable(false);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.STAR));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Version or Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create version or draft"));
    }

    @Test
    public void testHistory() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDraftable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.CLOCK_O));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq("History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
    }

    @Test
    public void testShareNotSharable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.SHARE_SQUARE));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq("Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
    }

    @Test
    public void testShareSharable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setSharable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.SHARE_SQUARE));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq("Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
    }

    @Test
    public void testDeleteNotDeletable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.TRASH));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq("Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
    }

    @Test
    public void testDeleteDeletable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setDeletable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.TRASH));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq("Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
    }

    @Test
    public void testEditNotEditable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.NEWSPAPER_O));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
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
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.LOCK));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Library in use by lockerby@gmail.com"));
    }

    @Test
    public void testEditEditableNotLocked() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        selectedItem.setEditable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
    }

}
