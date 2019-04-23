package mat.client.cqlworkspace;

import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.cqlworkspace.codes.CQLCodesView;
import mat.client.cqlworkspace.definitions.CQLDefinitionsView;
import mat.client.cqlworkspace.functions.CQLFunctionsView;
import mat.client.cqlworkspace.generalinformation.CQLGeneralInformationView;
import mat.client.cqlworkspace.includedlibrary.CQLIncludeLibraryView;
import mat.client.cqlworkspace.parameters.CQLParametersView;
import mat.client.cqlworkspace.valuesets.CQLAppliedValueSetView;
import mat.client.shared.MessagePanel;

public interface CQLWorkspaceView {
	public void hideAceEditorAutoCompletePopUp();
	public void hideInformationDropDown();
	public void resetMessageDisplay();
	CQLLeftNavBarPanelView getCQLLeftNavBarPanelView();
	public VerticalPanel getMainPanel();
	public CQLDefinitionsView getCQLDefinitionsView();
	public CQLView getViewCQLView();
	public CQLFunctionsView getCQLFunctionsView();
	public AceEditor getViewCQLEditor();
	public CQLIncludeLibraryView getIncludeView();
	public CQLAppliedValueSetView getValueSetView();
	public CQLCodesView getCodesView();
	public CQLParametersView getCQLParametersView();
	public CQLGeneralInformationView getCqlGeneralInformationView();
	public void buildIncludesView();
	public Widget asWidget();
	public void buildView(MessagePanel messagePanel, HelpBlock helpBlock);
	public void setGeneralInfoHeading();
	public void buildCodes();
	public void resetAll();
	public void buildGeneralInformation();
	public void buildAppliedQDM();
	public FlowPanel getMainFlowPanel();
	public void buildParameterLibraryView();
	public void buildDefinitionLibraryView();
	public void buildFunctionLibraryView();
	public void buildCQLFileView(boolean isEditable);
	public String getClickedMenu();
}
