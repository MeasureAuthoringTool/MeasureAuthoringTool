package mat.client.shared;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.model.LockedUserInfo;
import mat.model.cql.CQLLibraryDataSetObject;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

@RunWith(GwtMockitoTestRunner.class)
@Ignore
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
    @Mock(name = "convertButton")
    private Button convertButton;

    @InjectMocks
    private CQLibraryGridToolbar toolbar;

    @Test
    public void testApplyDefaultVersionButton() {
        toolbar.applyDefault();
        verifyDefaultVersionButton();
    }

    private void verifyDefaultVersionButton() {
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.STAR));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Version or Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create version or draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setWidth(Mockito.eq("160px"));
    }

    @Test
    public void testApplyDefaultHistoryButton() {
        toolbar.applyDefault();
        verifyDefaultHistoryButton();
    }

    private void verifyDefaultHistoryButton() {
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.CLOCK_O));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq("History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setWidth(Mockito.eq("73px"));
    }

    @Test
    public void testApplyDefaultDeleteButton() {
        toolbar.applyDefault();
        vetifyDefaultDeleteButton();
    }

    private void vetifyDefaultDeleteButton() {
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.TRASH));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq("Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setWidth(Mockito.eq("70px"));
    }

    @Test
    public void testApplyDefaultEditButton() {
        toolbar.applyDefault();
        verifyDefaultEditButton();
    }

    private void verifyDefaultEditButton() {
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setWidth(Mockito.eq("64px"));
    }

    @Test
    public void testApplyDefaultShareButton() {
        toolbar.applyDefault();
        verifyDefaultShareButton();
    }

    private void verifyDefaultShareButton() {
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.SHARE_SQUARE));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq("Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setWidth(Mockito.eq("68px"));
    }

    @Test
    public void testApplyDefaultOnNotSelected() {
        toolbar.updateOnSelectionChanged(null);
        verifyDefaultEditButton();
        verifyDefaultHistoryButton();
        verifyDefaultShareButton();
        verifyDefaultVersionButton();
    }

    @Test
    public void testApplyDefaultConvertButton() {
        toolbar.applyDefault();
        verifyDefaultConvertButton();
    }

    private void verifyDefaultConvertButton() {
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.RANDOM));
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setText(Mockito.eq("Convert to FHIR"));
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to convert"));
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setWidth(Mockito.eq("124px"));
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setVisible(false);
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
    public void testViewNotEditable() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();
        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.EYE));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setText(Mockito.eq("View"));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setTitle(Mockito.eq("Read-Only"));
    }

    @Test
    public void testEditEditableLocked() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        Map<String, Boolean> featureFlagMap = new HashMap<>();
        featureFlagMap.put("FhirEdit", true);

        selectedItem.setEditable(true);
        selectedItem.setLockedUserInfo(new LockedUserInfo());
        selectedItem.getLockedUserInfo().setEmailAddress("lockerby@gmail.com");
        selectedItem.setLocked(true);
        MatContext.get().setFeatureFlags(featureFlagMap);

        toolbar.updateOnSelectionChanged(selectedItem);

        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.LOCK));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setTitle(Mockito.eq("Library in use by lockerby@gmail.com"));
    }

    @Test
    public void testEditEditableNotLocked() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        Map<String, Boolean> featureFlagMap = new HashMap<>();
        featureFlagMap.put("FhirEdit", true);

        MatContext.get().setFeatureFlags(featureFlagMap);
        selectedItem.setEditable(true);

        toolbar.updateOnSelectionChanged(selectedItem);
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.PENCIL));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditOrViewButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
    }

    @Test
    public void testApplyOptions() {
        CQLibraryGridToolbar.Options opts = new CQLibraryGridToolbar.Options();
        opts.setConvertButtonVisible(true);
        toolbar.setOptions(opts);
        toolbar.applyOptions();
        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setVisible(true);
    }

    @Test
    public void testConvertEnabledOnSelection() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        selectedItem.setFhirConvertible(true);

        toolbar.updateOnSelectionChanged(selectedItem);

        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setEnabled(true);
    }

    @Test
    public void testConvertDisabledOnSelection() {
        CQLLibraryDataSetObject selectedItem = new CQLLibraryDataSetObject();

        selectedItem.setFhirConvertible(false);

        toolbar.updateOnSelectionChanged(selectedItem);

        Mockito.verify(toolbar.getConvertButton(), Mockito.times(1)).setEnabled(false);
    }


}
