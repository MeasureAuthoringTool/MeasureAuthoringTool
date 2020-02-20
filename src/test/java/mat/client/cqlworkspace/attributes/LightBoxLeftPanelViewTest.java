package mat.client.cqlworkspace.attributes;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class LightBoxLeftPanelViewTest {

    @Test
    public void testCreateEmpty() {
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(Collections.emptyMap());
        LightBoxLeftPanelView view = new LightBoxLeftPanelView("id", model, "100", "100");
        Assert.assertNotNull(view.getSections());
        Assert.assertNotNull(view.asWidget());
        Assert.assertEquals(0, view.getSections().size());
    }

}
