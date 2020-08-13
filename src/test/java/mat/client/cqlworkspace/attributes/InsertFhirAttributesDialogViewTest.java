package mat.client.cqlworkspace.attributes;

import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.gwtbootstrap3.client.ui.Modal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(GwtMockitoTestRunner.class)
public class InsertFhirAttributesDialogViewTest {

    @GwtMock
    private Modal dialogModal;
    @Mock
    private com.google.gwt.user.client.Element element;

    @Before
    public void setUp() {
        Mockito.when(dialogModal.getElement()).thenReturn(element);
    }

    @Test
    public void testCreateWithSubComponents() {
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel();
        InsertFhirAttributesDialogView view = new InsertFhirAttributesDialogView(model);
        Assert.assertNotNull(view.getCloseButton());
        Assert.assertNotNull(view.getInsertButton());
        Assert.assertNotNull(view.getLeftPanel());
        Assert.assertNotNull(view.getRightPanel());
    }

    @Test
    public void testShow() {
        InsertFhirAttributesDialogView view = new InsertFhirAttributesDialogView(new InsertFhirAttributesDialogModel());
        view.show();
        Mockito.verify(dialogModal, Mockito.times(1)).show();
    }

    @Test
    public void testHide() {
        InsertFhirAttributesDialogView view = new InsertFhirAttributesDialogView(new InsertFhirAttributesDialogModel());
        view.hide();
        Mockito.verify(dialogModal, Mockito.times(1)).hide();
    }

}
