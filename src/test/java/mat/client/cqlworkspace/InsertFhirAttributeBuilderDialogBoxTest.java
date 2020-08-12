package mat.client.cqlworkspace;


import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.cqlworkspace.shared.CQLEditor;
import org.gwtbootstrap3.client.ui.Modal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(GwtMockitoTestRunner.class)
public class InsertFhirAttributeBuilderDialogBoxTest {
    @Mock
    private CQLEditor cqlEditor;
    @GwtMock
    private Modal dialogModal;
    @Mock
    private com.google.gwt.user.client.Element element;

    @Before
    public void setUp() {
        Mockito.when(dialogModal.getElement()).thenReturn(element);
    }

    @Test
    public void testShowAttributeDialogBoxDoesntFail() {
        InsertFhirAttributeBuilderDialogBox.showAttributesDialogBox(cqlEditor);
        Mockito.verify(dialogModal, Mockito.times(1)).show();
    }
}
