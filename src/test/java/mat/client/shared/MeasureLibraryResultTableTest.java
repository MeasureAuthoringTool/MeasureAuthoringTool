package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.measure.ManageMeasureSearchModel;
import mat.model.FeatureFlag;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureLibraryResultTableTest {

    @Mock
    private HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent;

    @InjectMocks
    private MeasureLibraryResultTable measureLibraryResultTable;

    @Mock
    private CellTable<ManageMeasureSearchModel.Result> cellTable;

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

    @InjectMocks
    private MeasureLibraryGridToolbar gridToolbar;

    @Before
    public void setUp() {
    }

    @Test
    public void testAddColumnToTable() {
        com.google.gwt.user.client.Element tbl = DOM.createTable();
        when(cellTable.getElement()).thenReturn(tbl);
        when(tbl.cast()).thenReturn(GWT.create(TableElement.class));

        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setFlagOn(true);

        MatContext.get().setMatOnFHIR(featureFlag);

        cellTable = measureLibraryResultTable.addColumnToTable(gridToolbar, cellTable, fireEvent);
        assertNotNull(cellTable);
        verify(cellTable).addStyleName("table");
        verify(cellTable).setSelectionModel(ArgumentMatchers.any(MultiSelectionModel.class));
        verify(cellTable).addCellPreviewHandler(ArgumentMatchers.any(CellPreviewEvent.Handler.class));
    }

}
