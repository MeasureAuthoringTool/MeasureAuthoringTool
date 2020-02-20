package mat.client.cqlworkspace;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogDisplay;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogModel;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogPresenter;
import mat.client.cqlworkspace.attributes.InsertFhirAttributesDialogView;
import mat.client.cqlworkspace.shared.EditorDisplay;
import mat.client.shared.CQLConstantContainer;
import mat.client.shared.MatContext;

public class InsertFhirAttributeBuilderDialogBox {

    private InsertFhirAttributeBuilderDialogBox() {
    }

    public static void showAttributesDialogBox(final AceEditor aceEditor) {
        EditorDisplay editor = (EditorDisplay) aceEditor;
        CQLConstantContainer cqlConstantContainer = MatContext.get().getCqlConstantContainer();
        InsertFhirAttributesDialogModel insertFhirAttributesDialogModel = new InsertFhirAttributesDialogModel(cqlConstantContainer.getFhirDataTypes());
        InsertFhirAttributesDialogDisplay dialogDisplay = new InsertFhirAttributesDialogView(insertFhirAttributesDialogModel);
        InsertFhirAttributesDialogPresenter presenter = new InsertFhirAttributesDialogPresenter(editor, dialogDisplay);
        presenter.show();
    }

}
