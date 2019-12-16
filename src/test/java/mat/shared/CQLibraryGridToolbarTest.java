package mat.shared;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwt.user.client.ui.Button;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.shared.CQLibraryGridToolbar;

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
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setText(Mockito.eq("Create Version/Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to create Version/Draft"));
        Mockito.verify(toolbar.getVersionButton(), Mockito.times(1)).setWidth(Mockito.eq("135px"));
    }

    @Test
    public void testApplyDefaultHistoryButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-clock-o fa-lg"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setText(Mockito.eq("History"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to view history"));
        Mockito.verify(toolbar.getHistoryButton(), Mockito.times(1)).setWidth(Mockito.eq("69px"));
    }

    @Test
    public void testApplyDefaultDeleteButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-trash fa-lg"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setText(Mockito.eq("Delete"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to delete library"));
        Mockito.verify(toolbar.getDeleteButton(), Mockito.times(1)).setWidth(Mockito.eq("63px"));
    }

    @Test
    public void testApplyDefaultEditButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-pencil fa-lg"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setText(Mockito.eq("Edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to edit"));
        Mockito.verify(toolbar.getEditButton(), Mockito.times(1)).setWidth(Mockito.eq("53px"));
    }

    @Test
    public void testApplyDefaultShareButton() {
        toolbar.applyDefault();
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setEnabled(Mockito.eq(false));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setStyleName(Mockito.eq("btn btn-default disabled fa fa-share-square fa-lg"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setText(Mockito.eq("Share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setTitle(Mockito.eq("Click to share"));
        Mockito.verify(toolbar.getShareButton(), Mockito.times(1)).setWidth(Mockito.eq("60px"));
    }


}
