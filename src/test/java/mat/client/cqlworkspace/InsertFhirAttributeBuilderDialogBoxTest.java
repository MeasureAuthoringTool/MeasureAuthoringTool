package mat.client.cqlworkspace;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.cqlworkspace.shared.CQLEditor;

@RunWith(GwtMockitoTestRunner.class)
public class InsertFhirAttributeBuilderDialogBoxTest {
    @Mock
    private CQLEditor cqlEditor;

    @Test
    public void testShowAttributeDialogBoxDoesntFail() {
        InsertFhirAttributeBuilderDialogBox.showAttributesDialogBox(cqlEditor);
    }
}
