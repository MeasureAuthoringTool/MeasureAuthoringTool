package mat.client.cqlworkspace;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.attributes.AttributesCQLBuilder;
import mat.client.cqlworkspace.attributes.AttributesCQLBuilderImpl;
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
        InsertFhirAttributesDialogModel model = new InsertFhirAttributesDialogModel(cqlConstantContainer.getFhirDataTypesByResource());
        InsertFhirAttributesDialogDisplay dialogDisplay = new InsertFhirAttributesDialogView(model);
        AttributesCQLBuilder cqlBuilder = new AttributesCQLBuilderImpl();
        InsertFhirAttributesDialogPresenter presenter = new InsertFhirAttributesDialogPresenter(model, editor, dialogDisplay, cqlBuilder);
        presenter.show();
    }

}
