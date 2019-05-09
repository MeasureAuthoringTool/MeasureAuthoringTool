package mat.client.cqlworkspace.shared;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.shared.CQLError;

public class CQLEditor extends AceEditor {

	public CQLEditor() {
		super();
		this.startEditor();
		this.setMode(AceEditorMode.CQL);
		this.setTheme(AceEditorTheme.ECLIPSE);
		this.setAutocompleteEnabled(true);
		this.addAutoCompletions();
		this.setUseWrapMode(true);
		this.setReadOnly(true);
		this.clearAnnotations();
		this.getElement().getStyle().setFontSize(14, Unit.PX);
		this.setText("");
	}
	
	

	public CQLEditor(boolean isReadOnly) {
		this();
		this.setReadOnly(isReadOnly);
	}
	
	public void setAnnotations(List<CQLError> cqlErrors, String prefix, AceAnnotationType annotationType) {
		for (CQLError error : cqlErrors) {
			int line = error.getErrorInLine();
			int column = error.getErrorAtOffeset();
			this.addAnnotation(line - 1, column, prefix + ": " + error.getErrorMessage(), annotationType);
		}
	}
	
	public void setErrors(List<CQLError> cqlErrors) {
		setAnnotations(cqlErrors, "Error", AceAnnotationType.ERROR);
	}
	
	public void setWarnings(List<CQLError> cqlErrors) {
		setAnnotations(cqlErrors, "Warning", AceAnnotationType.WARNING);

	}
}
