package mat.client.shared;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.measure.ManageMeasureSearchModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(GwtMockitoTestRunner.class)
public class MeasureLibraryResultTableTest {

    @Mock
    private HasSelectionHandlers<ManageMeasureSearchModel.Result> fireEvent;

    @InjectMocks
    private MeasureLibraryResultTable measureLibraryResultTable;

    @Mock
    private CellTable<ManageMeasureSearchModel.Result> cellTable;

    private List<ManageMeasureSearchModel.Result> results;
    private ClickHandler clickHandler;
    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    @Before
    public void setUp() {
        ManageMeasureSearchModel.Result measure1 = new ManageMeasureSearchModel.Result();
        measure1.setId("MSR001");
        measure1.setName("TestMSR1");
        measure1.setMeasureModel("QDM");
        measure1.setVersion("QDM");
        measure1.setVersion("1.0");
        ManageMeasureSearchModel.Result measure2 = new ManageMeasureSearchModel.Result();
        measure2.setId("MSR002");
        measure2.setName("TestMSR2");
        measure2.setMeasureModel("FHIR");
        measure2.setVersion("1.0.1");
        results = new ArrayList<>();
        results.add(measure1);
        results.add(measure2);
    }

    @Test
    public void testAddColumnToTable() {
        com.google.gwt.user.client.Element tbl = DOM.createTable();
        when(cellTable.getElement()).thenReturn(tbl);
        when(tbl.cast()).thenReturn(GWT.create(TableElement.class));

        featureFlagMap.put("MAT_ON_FHIR", false);
        featureFlagMap.put("FHIR_EDIt", true);
        MatContext.get().setFeatureFlags(featureFlagMap);

        cellTable = measureLibraryResultTable.addColumnToTable("Recent Activity", cellTable, results, false, fireEvent);
        assertNotNull(cellTable);
        verify(cellTable).addStyleName("table");
        verify(cellTable).setSelectionModel(ArgumentMatchers.any(MultiSelectionModel.class));
        verify(cellTable).addCellPreviewHandler(ArgumentMatchers.any(CellPreviewEvent.Handler.class));
    }
}
