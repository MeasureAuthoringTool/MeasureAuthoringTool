package mat.client.clause.cqlworkspace;

import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.InlineRadio;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.model.cql.CQLFunctionArgument;

public interface CQLStandaloneWorkspaceViewInterface extends SharedView {
	
	AceEditor getCqlAceEditor();

	CQLIncludeLibraryView getCqlIncludeLibraryView();

	HorizontalPanel getLockedButtonVPanel();

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

	TextBox getAliasNameTxtArea();

	AceEditor getViewCQLEditor();

	TextBox getOwnerNameTextBox();
}

