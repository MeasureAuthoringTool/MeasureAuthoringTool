package mat.client.cqlworkspace;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gwtbootstrap3.client.ui.ListBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.shared.CQLError;
import mat.shared.SaveUpdateCQLResult;

public class SharedCQLWorkspaceUtility {
	public static final String ERROR_PREFIX = "ERROR:";
	public static final String WARNING_PREFIX = "WARNING:";

	public static final String MUST_HAVE_PROGRAM_WITH_RELEASE = "Cannot select a release without selecting a program.";

	public void loadPrograms(ListBox programBox) {
		CQLAppliedValueSetUtility.loadPrograms(programBox);
	}

	public static boolean validateCQLArtifact(SaveUpdateCQLResult result, AceEditor aceEditor) {
		result.getCqlErrors().forEach(error -> SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(error, SharedCQLWorkspaceUtility.ERROR_PREFIX, AceAnnotationType.ERROR, aceEditor));
		result.getCqlWarnings().forEach(error -> SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(error, SharedCQLWorkspaceUtility.WARNING_PREFIX, AceAnnotationType.WARNING, aceEditor));

		return !result.getCqlErrors().isEmpty();
	}
	
	public static AceEditor setCQLWorkspaceExceptionAnnotations(String name, Map<String, List<CQLError>> expressionCQLErrorMap, 
			Map<String, List<CQLError>> expressionCQLWarningsMap, AceEditor aceEditor) {
		aceEditor.clearAnnotations();
		aceEditor.removeAllMarkers();

		SharedCQLWorkspaceUtility.checkForExceptionAndCreateAnnotations(expressionCQLErrorMap, name, ERROR_PREFIX, AceAnnotationType.ERROR, aceEditor);
		SharedCQLWorkspaceUtility.checkForExceptionAndCreateAnnotations(expressionCQLWarningsMap, name, WARNING_PREFIX, AceAnnotationType.WARNING, aceEditor);

		aceEditor.setAnnotations();
		aceEditor.redisplay();
		return aceEditor;
	}

	private static AceEditor checkForExceptionAndCreateAnnotations(Map<String, List<CQLError>> expressionCQLExceptionMap, String name, String prefix, 
			AceAnnotationType aceAnnotationType, AceEditor aceEditor) {
		Optional.ofNullable(expressionCQLExceptionMap).ifPresent(expressionCQLExceptions -> 
		Optional.ofNullable(expressionCQLExceptions.get(name)).ifPresent(errors -> errors.forEach(error -> createCQLWorkspaceAnnotations(error, prefix, aceAnnotationType, aceEditor))));
		return aceEditor;
	}

	public static AceEditor createCQLWorkspaceAnnotations(List<CQLError> errors, String prefix, AceAnnotationType aceAnnotationType, AceEditor aceEditor) {
		if(errors != null) {
			for(final CQLError e : errors) {
				aceEditor.addAnnotation(e.getStartErrorInLine() - 1, e.getStartErrorAtOffset(), prefix + e.getErrorMessage(), aceAnnotationType);
			}
		}

		return aceEditor;
	}

	private static AceEditor createCQLWorkspaceAnnotations(CQLError error, String prefix, AceAnnotationType aceAnnotationType, AceEditor aceEditor) {
		final int startLine = error.getStartErrorInLine();
		final int startColumn = error.getStartErrorAtOffset();
		aceEditor.addAnnotation(startLine, startColumn, prefix + error.getErrorMessage(), aceAnnotationType);
		return aceEditor;
	}
}
