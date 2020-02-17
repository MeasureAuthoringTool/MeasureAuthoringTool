package mat.client.cqlworkspace.attributes;

import com.google.gwt.user.client.Window;
import mat.client.cqlworkspace.shared.EditorDisplay;

public class InsertFhirAttributesDialogPresenter {
    private EditorDisplay editor;
    private InsertFhirAttributesDialogDisplay display;
    private InsertFhirAttributesDialogModel model;

    public InsertFhirAttributesDialogPresenter(EditorDisplay editor, InsertFhirAttributesDialogDisplay display, InsertFhirAttributesDialogModel model) {
        this.editor = editor;
        this.display = display;
        this.model = model;
        bind();
    }


    private void bind() {
        display.getInsertButton().addClickHandler(event -> {
            editor.insertAtCursor("TODO: This should insert meaningful CQL code");
            editor.focus();
            display.hide();
        });
        display.getLeftPanel().addSelectionHandler(event -> {
            Window.alert(event.toString());
        });
    }

    public void show() {
        display.show();
    }

}
