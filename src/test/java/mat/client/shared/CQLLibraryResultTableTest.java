package mat.client.shared;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.cql.CQLLibrarySearchView;
import mat.model.cql.CQLLibraryDataSetObject;

@RunWith(GwtMockitoTestRunner.class)
public class CQLLibraryResultTableTest {

    @Mock
    private CQLLibrarySearchView.Observer observer;

    @Mock
    private HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent;

    @InjectMocks
    private CQLLibraryResultTable resultTable;


    @Test
    public void testHistoryClick() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onHistory(selectionModel);

        Mockito.verify(observer, Mockito.times(1)).onHistoryClicked(Mockito.eq(selected));
    }

    @Test
    public void testDeleteClickNoInteraction() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onDelete(selectionModel);

        Mockito.verify(observer, Mockito.never()).onDeleteClicked(Mockito.eq(selected));
    }

    @Test
    public void testDeleteClick() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        selected.setDeletable(true);

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onDelete(selectionModel);

        Mockito.verify(observer, Mockito.times(1)).onDeleteClicked(Mockito.eq(selected));
    }


    @Test
    public void testDraftOrVersionNoInteraction() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onDraftOrVersion(selectionModel);

        Mockito.verify(observer, Mockito.never()).onDraftOrVersionClick(Mockito.eq(selected));
    }

    @Test
    public void testDraftOrVersionClick() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        selected.setDraftable(true);

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onDraftOrVersion(selectionModel);

        Mockito.verify(observer, Mockito.times(1)).onDraftOrVersionClick(Mockito.eq(selected));
    }

    @Test
    public void testShareClick() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        selected.setSharable(true);

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onShare(selectionModel);

        Mockito.verify(observer, Mockito.times(1)).onShareClicked(Mockito.eq(selected));
    }

    @Test
    public void testShareClickNoInteraction() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onShare(selectionModel);

        Mockito.verify(observer, Mockito.never()).onShareClicked(Mockito.eq(selected));
    }

    @Test
    public void testEditClickNoInteraction() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onEdit(selectionModel);

        Mockito.verify(observer, Mockito.never()).onEditClicked(Mockito.eq(selected));
    }

    @Test
    public void testEditClickEditableButLockedNoInteraction() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        selected.setEditable(true);
        selected.setLocked(true);

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onEdit(selectionModel);

        Mockito.verify(observer, Mockito.never()).onEditClicked(Mockito.eq(selected));
    }

    @Test
    public void testEditClick() {
        CQLLibraryDataSetObject selected = new CQLLibraryDataSetObject();
        selected.setEditable(true);

        MultiSelectionModel<CQLLibraryDataSetObject> selectionModel = new MultiSelectionModel<>();
        selectionModel.setSelected(selected, true);

        resultTable.onEdit(selectionModel);

        Mockito.verify(observer, Mockito.times(1)).onEditClicked(Mockito.eq(selected));
    }
}
