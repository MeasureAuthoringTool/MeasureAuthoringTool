package mat.client.cqlworkspace.attributes;

import com.google.gwt.core.client.GWT;
import mat.client.cqlworkspace.shared.EditorDisplay;

public class InsertFhirAttributesDialogPresenter {
    private EditorDisplay editor;
    private InsertFhirAttributesDialogDisplay display;

    public InsertFhirAttributesDialogPresenter(EditorDisplay editor, InsertFhirAttributesDialogDisplay display) {
        this.editor = editor;
        this.display = display;
        bind();
    }

    private void bind() {
        display.getInsertButton().addClickHandler(event -> {
            editor.insertAtCursor("TODO: This should insert meaningful CQL code");
            editor.focus();
            display.hide();
        });
        display.getLeftPanel().addSelectionHandler(this::onSelected);
    }

    private void onSelected(FhirAttributeSelectionEvent event) {
        GWT.log(event.toString());
    }

    public void show() {
        display.show();
    }

}
