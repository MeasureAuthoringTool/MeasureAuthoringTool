package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.model.MatValueSet;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLModelValidator;

public abstract class AbstractCQLWorkspacePresenter {
	protected static final String CODES_SELECTED_SUCCESSFULLY = "All codes successfully selected.";
	protected static final String VALUE_SETS_SELECTED_SUCCESSFULLY = "All value sets successfully selected.";
	protected MessagePanel messagePanel = new MessagePanel();
	protected SimplePanel panel = new SimplePanel();
	protected String setId = null;
	protected String currentSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected String nextSection = CQLWorkSpaceConstants.CQL_GENERAL_MENU;
	protected boolean isModified = false;
	protected AceEditor curAceEditor;
	protected CQLModelValidator validator = new CQLModelValidator();
	protected CQLCode modifyCQLCode;
	protected boolean isCodeModified = false;
	protected boolean isUserDefined = false;
	protected String currentIncludeLibrarySetId = null;
	protected String currentIncludeLibraryId = null;
	protected boolean previousIsProgramReleaseBoxEnabled = true; 
	protected boolean previousIsProgramListBoxEnabled = true; 
	protected boolean previousIsRetrieveButtonEnabled = true; 
	protected boolean previousIsApplyButtonEnabled = false; 
	protected boolean isProgramReleaseBoxEnabled = true; 
	protected boolean isRetrieveButtonEnabled = true; 
	protected boolean isApplyButtonEnabled = false; 
	protected boolean isProgramListBoxEnabled = true; 
	protected String cqlLibraryComment;
	protected boolean isFormattable = true;
	protected static Boolean isPageDirty = false;
	protected List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
	protected List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	protected CQLQualityDataSetDTO modifyValueSetDTO;
	protected MatValueSet currentMatValueSet= null;
	
	protected final VSACAPIServiceAsync vsacapiService = MatContext.get().getVsacapiServiceAsync();
	protected QDSAttributesServiceAsync attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
	protected DeleteConfirmationDialogBox deleteConfirmationDialogBox = new DeleteConfirmationDialogBox();
	
	protected void showUnsavedChangesWarning() {
		messagePanel.clearAlerts();
		messagePanel.getWarningConfirmationMessageAlert().createAlert();
		messagePanel.getWarningConfirmationYesButton().setFocus(true);
	}
	
	public void setIsPageDirty(Boolean isPageDirty) {
		AbstractCQLWorkspacePresenter.isPageDirty = isPageDirty;
	}
	
	public Boolean getIsPageDirty() {
		return isPageDirty;
	}	
	
	protected boolean isValidExpressionName(String expressionName) {
		final String trimedExpression = expressionName.trim();
		return !trimedExpression.isEmpty() && !trimedExpression.equalsIgnoreCase("Patient") && !trimedExpression.equalsIgnoreCase("Population")
				&& MatContext.get().getCqlConstantContainer() != null 
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList() != null
				&& MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList() != null
				&& !MatContext.get().getCqlConstantContainer().getCqlKeywordList().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimedExpression));
	}
	
	public MessagePanel getMessagePanel() {
		return messagePanel;
	}

	public DeleteConfirmationDialogBox getDeleteConfirmationDialogBox() {
		return deleteConfirmationDialogBox;
	}

	public void setDeleteConfirmationDialogBox(DeleteConfirmationDialogBox deleteConfirmationDialogBox) {
		this.deleteConfirmationDialogBox = deleteConfirmationDialogBox;
	}
	
	/**
	 * Gets the delete confirmation dialog box yes button.
	 *
	 * @return the delete confirmation dialog box yes button
	 */
	public Button getDeleteConfirmationDialogBoxYesButton() {
		return deleteConfirmationDialogBox.getYesButton();
	}

	/**
	 * Gets the delete confirmation dialog box no button.
	 *
	 * @return the delete confirmation dialog box no button
	 */
	public Button getDeleteConfirmationDialogBoxNoButton() {
		return deleteConfirmationDialogBox.getNoButton();
	}
}
