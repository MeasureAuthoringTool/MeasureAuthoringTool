package mat.client.cqlworkspace.attributes;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import mat.client.cqlworkspace.shared.EditorDisplay;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InsertFhirAttributesDialogPresenterTest {

    @Mock
    private EditorDisplay editor;
    @Mock
    private InsertFhirAttributesDialogDisplay dialog;
    @Mock
    private HasClickHandlers insertButton;
    @Mock
    private LightBoxLeftPanelDisplay leftPanel;
    @Mock
    private HandlerRegistration insertClickRegistration;

    @Test
    public void testCreateAndShowAndInsertPresenter() {
        Mockito.when(dialog.getInsertButton()).thenReturn(insertButton);
        Mockito.when(dialog.getLeftPanel()).thenReturn(leftPanel);
        Mockito.when(insertButton.addClickHandler(Mockito.any())).thenReturn(insertClickRegistration);
        InsertFhirAttributesDialogPresenter presenter = new InsertFhirAttributesDialogPresenter(editor, dialog);
        presenter.show();
        Mockito.verify(dialog, Mockito.times(1)).show();

        ArgumentCaptor<ClickHandler> insertClickHandlerCaptor = ArgumentCaptor.forClass(ClickHandler.class);
        Mockito.verify(insertButton, Mockito.times(1)).addClickHandler(insertClickHandlerCaptor.capture());

        ClickHandler onInsertClicked = insertClickHandlerCaptor.getValue();
        onInsertClicked.onClick(new ClickEvent() {
        });

        Mockito.verify(editor, Mockito.times(1)).insertAtCursor(Mockito.eq("TODO: This should insert meaningful CQL code"));
        Mockito.verify(editor, Mockito.times(1)).focus();
        Mockito.verify(dialog, Mockito.times(1)).hide();
    }
}
