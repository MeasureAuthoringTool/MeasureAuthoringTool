package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import mat.client.shared.MatContext;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.MessagePanel;
import mat.shared.CQLError;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.StringUtility;

public class SharedCQLWorkspaceUtility {
    private static final Logger log = Logger.getLogger(SharedCQLWorkspaceUtility.class.getSimpleName());

    public static final String ERROR_PREFIX = "ERROR:";
    public static final String WARNING_PREFIX = "WARNING:";

    public static final String MUST_HAVE_PROGRAM_WITH_RELEASE = "Cannot select a release without selecting a program.";

    public void loadPrograms(ListBox programBox) {
        CQLAppliedValueSetUtility.loadPrograms(programBox);
    }

    public static boolean validateCQLArtifact(SaveUpdateCQLResult result, AceEditor aceEditor, MessagePanel messagePanel, String expressionType, String expressionName) {
        displayAnnotations(result, aceEditor);
        SharedCQLWorkspaceUtility.displayMessageBanner(result, messagePanel, expressionType, expressionName);
        return !result.getCqlErrors().isEmpty();
    }

    public static void displayAnnotations(SaveUpdateCQLResult result, AceEditor aceEditor) {
        aceEditor.clearAnnotations();
        if (!result.getCqlErrors().isEmpty()) {
            result.getCqlErrors().forEach(error -> SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(error, SharedCQLWorkspaceUtility.ERROR_PREFIX, AceAnnotationType.ERROR, aceEditor));
        } else if (!result.getCqlWarnings().isEmpty()) {
            result.getCqlWarnings().forEach(error -> SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(error, SharedCQLWorkspaceUtility.WARNING_PREFIX, AceAnnotationType.WARNING, aceEditor));
        }
        aceEditor.setAnnotations();
    }

    public static void displayMessagesForViewCQL(SaveUpdateCQLResult result, AceEditor aceEditor, MessagePanel messagePanel) {
        aceEditor.clearAnnotations();
        displayAnnotationForViewCQL(result, aceEditor);
        SharedCQLWorkspaceUtility.displayMessageBannerForViewCQL(result, messagePanel);
        aceEditor.setAnnotations();
    }

    public static void displayAnnotationForViewCQL(SaveUpdateCQLResult result, AceEditor aceEditor) {
        log.log(Level.INFO,"displayAnnotationForViewCQL libNameToErrorMap:\n" + result.getLibraryNameErrorsMap() + "\nLibWarningMap:\n" +
                result.getLibraryNameWarningsMap() + "\nlinterErrors:\n" +
                result.getLinterErrors());
        aceEditor.clearAnnotations();
        String formattedName = result.getCqlModel().getFormattedName();
        SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(result.getLibraryNameErrorsMap().get(formattedName), ERROR_PREFIX, AceAnnotationType.ERROR, aceEditor);
        SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(result.getLibraryNameWarningsMap().get(formattedName), WARNING_PREFIX, AceAnnotationType.WARNING, aceEditor);
        SharedCQLWorkspaceUtility.createCQLWorkspaceAnnotations(result.getLinterErrors(), ERROR_PREFIX, AceAnnotationType.ERROR, aceEditor);
        aceEditor.setAnnotations();
        log.log(Level.INFO,"leaving displayAnnotationForViewCQL " + result);

    }


    private static void displayMessageBannerForViewCQL(SaveUpdateCQLResult result, MessagePanel messagePanel) {
        messagePanel.clearAlerts();
        List<String> errorMessages = new ArrayList<String>();
        if (!result.isQDMVersionMatching()) {
            errorMessages.add(AbstractCQLWorkspacePresenter.INVALID_QDM_VERSION_IN_INCLUDES);
        } else if (!result.getCqlErrors().isEmpty() || !result.getLinterErrorMessages().isEmpty()) {
            result.getCqlErrors().forEach(e -> log.log(Level.INFO,"CQL Error: " + e.getErrorMessage() + " " + e.getStartErrorInLine() +
                    " " + e.getEndErrorInLine()));
            result.getLinterErrorMessages().forEach(e -> log.log(Level.INFO,"Linter Error: " + e.toString()));

            if (!MatContext.get().isCurrentModelTypeFhir() && result.isMeasureComposite() && result.isDoesMeasureHaveIncludedLibraries()) {
                errorMessages.add(AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE_COMPOSITE_AND_INCLUDED);
            } else if (result.isMeasureComposite()) {
                errorMessages.add(AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE_COMPOSITE);
            } else if (!MatContext.get().isCurrentModelTypeFhir() && result.isDoesMeasureHaveIncludedLibraries()) {
                prepareIncludeErrors(result, errorMessages);
            } else {
                errorMessages.add(AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE);
            }

            result.getLinterErrorMessages().forEach(e -> errorMessages.add(e));
        }
        if (!result.isDatatypeUsedCorrectly()) {
            errorMessages.add(AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE_BAD_VALUESET_DATATYPE);
        }

        if (result.isSevereError()) {
            errorMessages.add("Please correct the syntax errors so the CQL can be validated.");
        }

        if (!errorMessages.isEmpty()) {
            messagePanel.getErrorMessageAlert().createAlert(errorMessages);
        } else if (!result.getCqlWarnings().isEmpty()) {
            messagePanel.getWarningMessageAlert().createAlert(AbstractCQLWorkspacePresenter.VIEW_CQL_WARNING_MESSAGE);
        } else {
            messagePanel.getSuccessMessageAlert().createAlert(AbstractCQLWorkspacePresenter.VIEW_CQL_NO_ERRORS_MESSAGE);
        }
    }

    private static void prepareIncludeErrors(SaveUpdateCQLResult result, List<String> errorMessages) {
        errorMessages.add(AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE_INCLUDED);
        if (result.getIncludeLibrariesWithErrors() != null && !result.getIncludeLibrariesWithErrors().isEmpty()) {
            String listOfLibs = String.join(", ", result.getIncludeLibrariesWithErrors());
            String msg = AbstractCQLWorkspacePresenter.VIEW_CQL_ERROR_MESSAGE_INCLUDE_LIBS_ERRORS + listOfLibs + ".";
            errorMessages.add(msg);
        }
    }

    private static void displayMessageBanner(SaveUpdateCQLResult result, MessagePanel messagePanel, String expressionType, String expressionName) {
        messagePanel.clearAlerts();
        if (!result.getCqlErrors().isEmpty()) {
            messagePanel.getErrorMessageAlert().createAlert(expressionType + " " + StringUtility.trimTextToSixtyChars(expressionName) + " successfully saved with errors.");
        } else if (!result.isDatatypeUsedCorrectly()) {
            messagePanel.getErrorMessageAlert().createAlert(expressionType + " " + StringUtility.trimTextToSixtyChars(expressionName) + "  successfully saved with errors. " + AbstractCQLWorkspacePresenter.INCORRECT_VALUE_SET_CODE_DATATYPE_COMBINATION);
        } else if (!result.getCqlWarnings().isEmpty()) {
            messagePanel.getWarningMessageAlert().createAlert(expressionType + " " + StringUtility.trimTextToSixtyChars(expressionName) + " successfully saved with warnings.");
        } else {
            messagePanel.getSuccessMessageAlert().createAlert(expressionType + " " + StringUtility.trimTextToSixtyChars(expressionName) + " successfully saved.");
        }
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
        if (errors != null) {
            for (final CQLError e : errors) {
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

    public static HorizontalPanel buildHeaderPanel(HTML heading, InAppHelp inAppHelp) {
        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.add(heading);
        headerPanel.add(inAppHelp);
        return headerPanel;
    }

}
