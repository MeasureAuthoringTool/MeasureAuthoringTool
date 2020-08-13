package mat.client;


import com.google.gwt.user.client.ui.HTML;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.MatContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.Map;

@RunWith(GwtMockitoTestRunner.class)
public class CqlComposerPresenterTest {

    private Map<String, Boolean> featureFlagMap = new HashMap<>();
    private ContentWithHeadingWidget cqlComposerContent;
    @Mock
    private HTML heading;

    @Before
    public void setup() {
        cqlComposerContent = Whitebox.getInternalState(CqlComposerPresenter.class, "cqlComposerContent");
        Assert.assertNotNull(cqlComposerContent);
        Whitebox.setInternalState(cqlComposerContent, "heading", heading);
    }

    @Test
    public void testHeadingFlagOn() {
        featureFlagMap.put("MAT_ON_FHIR",true);
        MatContext.get().setFeatureFlags(featureFlagMap);

        CQLLibrarySelectedEvent selectedEvent = CQLLibrarySelectedEvent.Builder.newBuilder()
                .withLibraryName("library1")
                .withCqlLibraryVersion("v1")
                .withLibraryType("TYPE1")
                .withDraft(true)
                .build();
        MatContext.get().setCurrentLibraryInfo(selectedEvent);
        CqlComposerPresenter.setContentHeading();
        Mockito.verify(heading, Mockito.times(1)).setHTML(Mockito.eq("<a class='invisible' name='CqlComposer'></a><h1>library1 v1 (QDM / QDM)</h1>"));
    }

    @Test
    public void testHeadingFlagOff() {
        featureFlagMap.put("MAT_ON_FHIR",false);
        MatContext.get().setFeatureFlags(featureFlagMap);

        CQLLibrarySelectedEvent selectedEvent = CQLLibrarySelectedEvent.Builder.newBuilder()
                .withLibraryName("library1")
                .withCqlLibraryVersion("v1")
                .withLibraryType("TYPE1")
                .withDraft(true)
                .build();
        MatContext.get().setCurrentLibraryInfo(selectedEvent);
        CqlComposerPresenter.setContentHeading();
        Mockito.verify(heading, Mockito.times(1)).setHTML(Mockito.eq("<a class='invisible' name='CqlComposer'></a><h1>library1 v1</h1>"));
    }
}
