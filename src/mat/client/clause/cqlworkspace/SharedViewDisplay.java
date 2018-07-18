package mat.client.clause.cqlworkspace;

import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.model.cql.CQLFunctionArgument;

public interface SharedViewDisplay {

	VerticalPanel getMainPanel();

	Widget asWidget();

	HorizontalPanel getMainHPanel();

	FlowPanel getMainFlowPanel();

	void buildView();

	String getClickedMenu();

	void setClickedMenu(String clickedMenu);

	String getNextClickedMenu();

	void setNextClickedMenu(String nextClickedMenu);

	CQLLeftNavBarPanelView getCqlLeftNavBarPanelView();

	void resetMessageDisplay();

	void hideAceEditorAutoCompletePopUp();

	CQLParametersView getCqlParametersView();

	CQLDefinitionsView getCqlDefinitionsView();

	CQLFunctionsView getCqlFunctionsView();

	CQLIncludeLibraryView getCqlIncludeLibraryView();

	void buildCQLFileView(boolean isEditable);

	AceEditor getCqlAceEditor();

	void buildGeneralInformation();

	CQLGeneralInformationView getCqlGeneralInformationView();

	CQLIncludeLibraryView getIncludeView();

	TextBox getAliasNameTxtArea();

	AceEditor getViewCQLEditor();

	TextBox getOwnerNameTextBox();

	void buildIncludesView();

	void resetAll();

	void buildParameterLibraryView();

	void buildDefinitionLibraryView();

	void buildFunctionLibraryView();

	void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList);

	DefinitionFunctionButtonToolBar getParameterButtonBar();

	DefinitionFunctionButtonToolBar getDefineButtonBar();

	DefinitionFunctionButtonToolBar getFunctionButtonBar();

	TextBox getDefineNameTxtArea();

	AceEditor getDefineAceEditor();

	InlineRadio getContextDefinePATRadioBtn();

	InlineRadio getContextDefinePOPRadioBtn();

	TextBox getFuncNameTxtArea();

	AceEditor getFunctionBodyAceEditor();

	InlineRadio getContextFuncPATRadioBtn();

	InlineRadio getContextFuncPOPRadioBtn();

	List<CQLFunctionArgument> getFunctionArgumentList();

	TextBox getParameterNameTxtArea();

	AceEditor getParameterAceEditor();

	Map<String, CQLFunctionArgument> getFunctionArgNameMap();

	void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable);

	CQLAppliedValueSetView getValueSetView();

	CQLCodesView getCodesView();

	void buildAppliedQDM();

	void buildCodes();

	HorizontalPanel getLockedButtonVPanel();

	void hideInformationDropDown();

	CQLView getViewCQLView();

	void setGeneralInfoHeading();
	
	HelpBlock getHelpBlock();
}
