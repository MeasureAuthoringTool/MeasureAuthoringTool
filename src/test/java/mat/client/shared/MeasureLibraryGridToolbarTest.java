package mat.client.shared;

import java.util.Arrays;
import java.util.Collections;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.measure.ManageMeasureSearchModel;
import mat.model.LockedUserInfo;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureLibraryGridToolbarTest {

    @Mock(name = "versionButton")
    private Button versionButton;
    @Mock(name = "historyButton")
    private Button historyButton;
    @Mock(name = "editButton")
    private Button editButton;
    @Mock(name = "shareButton")
    private Button shareButton;
    @Mock(name = "cloneButton")
    private Button cloneButton;
    @Mock(name = "exportButton")
    private Button exportButton;
    @Mock(name = "fhirValidationButton")
    private Button fhirValidationButton;

    @InjectMocks
    private MeasureLibraryGridToolbar toolbar;

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
    public void testApplyDefaultCloseButton() {
        toolbar.applyDefault();

        Mockito.verify(toolbar.getCloneButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getCloneButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.CLONE));
        Mockito.verify(toolbar.getCloneButton(), Mockito.times(1)).setText(Mockito.eq("Clone"));
        Mockito.verify(toolbar.getCloneButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to clone"));
        Mockito.verify(toolbar.getCloneButton(), Mockito.times(1)).setWidth(Mockito.eq("69px"));
    }

    @Test
    public void testApplyDefaultEditButton() {
        toolbar.applyDefault();

        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.EDIT));
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
    public void testApplyDefaultExportButton() {
        toolbar.applyDefault();

        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.DOWNLOAD));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setText(Mockito.eq("Export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setWidth(Mockito.eq("72px"));
    }

    @Test
    public void testExportOnEmptySelection() {
        toolbar.updateOnSelectionChanged(Collections.emptyList());

        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.DOWNLOAD));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setText(Mockito.eq("Export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.times(1)).setWidth(Mockito.eq("72px"));
    }

    public void testApplyDefaultFhirValidationButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setIcon(Mockito.eq(IconType.FILE_TEXT_O));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Run FHIR Validation"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to Run FHIR Validation"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setWidth(Mockito.eq("146px"));
    }

    @Test
    public void testExportOnSelectionNoExportableItem() {
        toolbar.updateOnSelectionChanged(Arrays.asList(
                new ManageMeasureSearchModel.Result(),
                new ManageMeasureSearchModel.Result()
        ));

        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setIcon(Mockito.eq(IconType.DOWNLOAD));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setWidth(Mockito.eq("72px"));
    }

    @Test
    public void testExportOnSelectionOneExportableItem() {
        toolbar.updateOnSelectionChanged(Arrays.asList(
                new ManageMeasureSearchModel.Result() {{
                    setExportable(true);
                }},
                new ManageMeasureSearchModel.Result()
        ));

        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setIcon(Mockito.eq(IconType.DOWNLOAD));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to export"));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setWidth(Mockito.eq("72px"));
    }

    @Test
    public void testVersionButtonOnSelectionDraftableNotVersionable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setDraftable(true);
        item.setVersionable(false);
        item.setIsComposite(false);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Create Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to create draft"));
    }

    @Test
    public void testVersionButtonOnSelectionVersionableNotDraftable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setVersionable(true);
        item.setDraftable(false);
        item.setIsComposite(false);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Create Version"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to create version"));
    }

    @Test
    public void testVersionButtonOnSelectionNotVersionableNotDraftable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setVersionable(false);
        item.setDraftable(false);
        item.setIsComposite(true);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Create Version or Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to create version or draft"));
    }

    @Test
    public void testVersionButtonOnSelectionVersionableDraftable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setVersionable(true);
        item.setDraftable(true);
        item.setIsComposite(true);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Create Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to create draft"));
    }

    @Test
    public void testHistoryOnSelectedNotEmpty() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setIsComposite(true);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getHistoryButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testEditOnSelectedNotEditable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setIsComposite(true);
        toolbar.updateOnSelectionChanged(Arrays.asList(item));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setIcon(Mockito.eq(IconType.NEWSPAPER_O));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Read-Only"));
    }

    @Test
    public void testEditOnSelectedEditableAndLocked() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setEditable(true);
        item.setMeasureLocked(true);
        item.setLockedUserInfo(new LockedUserInfo());
        item.getLockedUserInfo().setEmailAddress("fake@gmail.com");
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setIcon(Mockito.eq(IconType.LOCK));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Measure in use by fake@gmail.com"));
    }

    @Test
    public void testEditOnSelectedEditableAndNotLocked() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setEditable(true);
        item.setMeasureLocked(false);
        item.setLockedUserInfo(new LockedUserInfo());
        item.getLockedUserInfo().setEmailAddress("fake@gmail.com");
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setIcon(Mockito.eq(IconType.EDIT));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Click to edit"));
    }

    @Test
    public void testShareOnNotSharable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setSharable(false);
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getShareButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.never()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testShareOnSharable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setSharable(true);
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getShareButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testCloneOnClonable() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getCloneButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
    }

    // cloneButton.setTitle(selectedItem.getIsComposite() ? "Composite measure not cloneable" : "Measure not cloneable");
    @Test
    public void testCloneOnNotClonableNotComposite() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(false);
        item.setIsComposite(false);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getCloneButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getCloneButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getCloneButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Measure not cloneable"));
    }

    @Test
    public void testCloneOnNotClonableComposite() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(false);
        item.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getCloneButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getCloneButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getCloneButton(), Mockito.atLeastOnce()).setTitle(Mockito.eq("Composite measure not cloneable"));
    }

    @Test
    public void testRunValidationButtonForDraftFhir() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setDraft(true);
        item.setMeasureModel("FHIR");

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testRunValidationButtonForNotDraftFhir() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setDraft(false);
        item.setMeasureModel("FHIR");

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.never()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testRunValidationButtonForVersionedQdm() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setDraftable(true);
        item.setMeasureModel("QDM");
        item.setVersion("v5.9.00");

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testRunValidationButtonForNotVersionedQdm() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setDraftable(false);
        item.setMeasureModel("QDM");
        item.setVersion("v5.9.00");

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.never()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testRunValidationButtonForModelTypeNull() {
        ManageMeasureSearchModel.Result item = new ManageMeasureSearchModel.Result();
        item.setClonable(true);
        item.setMeasureModel(null);
        item.setVersion("v5.9.00");

        toolbar.updateOnSelectionChanged(Arrays.asList(item));

        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.never()).setEnabled(Mockito.eq(true));
    }

    @Test
    public void testAllDisabledMultipleSelectedNotExportable() {
        ManageMeasureSearchModel.Result item0 = new ManageMeasureSearchModel.Result();
        item0.setIsComposite(true);

        ManageMeasureSearchModel.Result item1 = new ManageMeasureSearchModel.Result();
        item1.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item0, item1));

        Mockito.verify(toolbar.getShareButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getCloneButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getExportButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.never()).setEnabled(Mockito.eq(true));

    }

    @Test
    public void testExportEnabledOndMultipleSelectedExportable() {
        ManageMeasureSearchModel.Result item0 = new ManageMeasureSearchModel.Result();
        item0.setExportable(true);
        item0.setIsComposite(true);

        ManageMeasureSearchModel.Result item1 = new ManageMeasureSearchModel.Result();
        item1.setExportable(true);
        item1.setIsComposite(true);

        toolbar.updateOnSelectionChanged(Arrays.asList(item0, item1));

        Mockito.verify(toolbar.getShareButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getEditButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getVersionButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getCloneButton(), Mockito.never()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getExportButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));
        Mockito.verify(toolbar.getFhirValidationButton(), Mockito.atLeastOnce()).setEnabled(Mockito.eq(true));

    }

}
