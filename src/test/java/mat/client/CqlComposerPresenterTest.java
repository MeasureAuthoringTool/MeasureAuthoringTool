package mat.client;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwtmockito.GwtMockitoTestRunner;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.MatContext;
import mat.model.FeatureFlag;

import java.util.ArrayList;
import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class CqlComposerPresenterTest {

    private List<FeatureFlag> featureFlaglist = new ArrayList<>();
    private ContentWithHeadingWidget cqlComposerContent;
    @Mock
    private HTML heading;

    @Before
    public void setup() {
        featureFlaglist.add(new FeatureFlag(1, "MAT_ON_FHIR", false));
        MatContext.get().setFeatureFlags(featureFlaglist);

        cqlComposerContent = Whitebox.getInternalState(CqlComposerPresenter.class, "cqlComposerContent");
        Assert.assertNotNull(cqlComposerContent);
        Whitebox.setInternalState(cqlComposerContent, "heading", heading);
    }

    @Test
    public void testHeadingFlagOn() {
        featureFlaglist.get(0).setFlagOn(true);

        CQLLibrarySelectedEvent selectedEvent = CQLLibrarySelectedEvent.Builder.newBuilder()
                .withLibraryName("library1")
                .withCqlLibraryVersion("v1")
                .withLibraryType("TYPE1")
                .withDraft(true)
                .build();
        MatContext.get().setCurrentLibraryInfo(selectedEvent);
        CqlComposerPresenter.setContentHeading();
        Mockito.verify(heading, Mockito.times(1)).setHTML(Mockito.eq("<a class='invisible' name='CqlComposer'></a><h1>library1 v1 (TYPE1)</h1>"));
    }

    @Test
    public void testHeadingFlagOff() {
        featureFlaglist.get(0).setFlagOn(false);

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
