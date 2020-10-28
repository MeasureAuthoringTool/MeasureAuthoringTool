package mat.client.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.MeasureSearchView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

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

    @Mock
    private MeasureSearchView.Observer observer;

    @InjectMocks
    private MeasureLibraryGridToolbar gridToolbar;

    private Map<String, Boolean> featureFlagMap = new HashMap<>();


    @Test
    public void testAddColumnToTable() {
        com.google.gwt.user.client.Element tbl = DOM.createTable();
        when(cellTable.getElement()).thenReturn(tbl);
        when(tbl.cast()).thenReturn(GWT.create(TableElement.class));

        featureFlagMap.put("MAT_ON_FHIR", false);
        MatContext.get().setFeatureFlags(featureFlagMap);

        cellTable = measureLibraryResultTable.addColumnToTable(gridToolbar, cellTable, fireEvent);
        assertNotNull(cellTable);
        verify(cellTable).addStyleName("table");
        verify(cellTable).setSelectionModel(ArgumentMatchers.any(MultiSelectionModel.class));
        verify(cellTable).addCellPreviewHandler(ArgumentMatchers.any(CellPreviewEvent.Handler.class));
    }

    @Test
    public void testVersionClickNoInteraction() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onVersionButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.never()).onDraftOrVersionClick(Mockito.eq(selected));
    }

    @Test
    public void testVersionClickIsDraftable() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();
        selected.setDraftable(true);

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onVersionButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onDraftOrVersionClick(Mockito.eq(selected));
    }

    @Test
    public void testVersionClickIsVersionable() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();
        selected.setVersionable(true);

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onVersionButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onDraftOrVersionClick(Mockito.eq(selected));
    }

    @Test
    public void testHistoryClick() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onHistoryButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onHistoryClicked(Mockito.eq(selected));
    }

    @Test
    public void testCloneClickNoInteraction() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onCloneButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.never()).onCloneClicked(Mockito.eq(selected));
    }


    @Test
    public void testOnShareSharable() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();
        selected.setSharable(true);

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onShareButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onShareClicked(Mockito.eq(selected));
    }

    @Test
    public void testOnShareClickedButNotSharable() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();
        selected.setSharable(false);

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onShareButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.never()).onShareClicked(Mockito.eq(selected));
    }

    @Test
    public void testOnFhirValidationButtonClicked() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onFhirValidationButtonClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onFhirValidationClicked(Mockito.eq(selected));
    }

    @Test
    public void testOnConvert() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();
        selected.setFhirConvertible(true);

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onConvertClicked(selectionModel);
        Mockito.verify(observer, Mockito.times(1)).onConvertMeasureFhir(Mockito.eq(selected));
    }

    @Test
    public void testOnConvertButNotConvertible() {
        ManageMeasureSearchModel.Result selected = new ManageMeasureSearchModel.Result();

        MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        measureLibraryResultTable.onConvertClicked(selectionModel);
        Mockito.verify(observer, Mockito.never()).onConvertMeasureFhir(Mockito.eq(selected));
    }

}
