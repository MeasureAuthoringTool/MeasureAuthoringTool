package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLIdentifierObject;
import mat.shared.cql.model.FunctionSignature;

public class FunctionBuilderModal extends SubExpressionBuilderModal {

	private static final String CHOOSE_SIGNATURE_LABEL = "This function has multiple signatures available. Please choose a signature.";
	private static final String WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE = "What function would you like to use?";
	private static final String SELECT_FUNCTION = "-- Select a Function --";
	private static final String SELECT_FUNCTION_SIGNATURE = "-- Select Function Signature --";
	private ListBoxMVP functionNameListBox;
	private int selectedFunctionIndex = 0;
	private FunctionModel functionModel;
	private ListBoxMVP functionSignatureListBox;
	private VerticalPanel functionSignaturePanel;
	private VerticalPanel functionNamePanel;
	private Button functionNameBuildButton;
	private Button functionSignatureBuildButton;

	public FunctionBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel) {
		super("Function", parent, parentModel, mainModel);
		functionModel = new FunctionModel(parentModel);
		this.getParentModel().appendExpression(functionModel);
		display();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		VerticalPanel functionPanel = new VerticalPanel();
		functionPanel.setStyleName("selectorsPanel");
		
		functionNamePanel = new VerticalPanel();
		functionNamePanel.setWidth("50%");
		functionNamePanel.add(buildFunctionNameFormLabel());
		functionNamePanel.add(buildFunctionNameSelectionPanel());

		functionSignaturePanel = new VerticalPanel();
		functionSignaturePanel.setWidth("50%");
		functionSignaturePanel.add(buildFunctionSignatureFormLabel());
		functionSignaturePanel.add(buildFunctionSignatureSelectionPanel());

		functionPanel.add(functionNamePanel);
		functionPanel.add(new SpacerWidget());
		functionPanel.add(new SpacerWidget());
		functionPanel.add(functionSignaturePanel);		
		functionSignaturePanel.setVisible(false);
		return functionPanel;
	}
	
	private HorizontalPanel buildFunctionNameSelectionPanel() {
		HorizontalPanel functionNamePanel = new HorizontalPanel();
		functionNamePanel.add(buildFunctionNameListBox());
		
		functionNameBuildButton = buildBuildButton();
		functionNamePanel.add(functionNameBuildButton);
		return functionNamePanel;
	}

	private Widget buildFunctionSignatureSelectionPanel() {
		HorizontalPanel functionSignatureListBoxPanel = new HorizontalPanel();
		functionSignatureListBoxPanel.add(buildFunctionSignatureListBox());
		
		functionSignatureBuildButton = buildBuildButton();
		functionSignatureListBoxPanel.add(functionSignatureBuildButton);
		return functionSignatureListBoxPanel;
	}
	
	private ListBoxMVP buildFunctionNameListBox() {		
		functionNameListBox = new ListBoxMVP();
		functionNameListBox.setWidth("500px");
		addFunctionNamesToListBox();
		functionNameListBox.setId("functionNameListBox");
		functionNameListBox.setTitle("Select a function");
		functionNameListBox.addChangeHandler(event -> onFunctionNameListBoxChange());
		functionNameListBox.setSelectedIndex(selectedFunctionIndex);
		
		return functionNameListBox;
	}
	
	private Widget buildFunctionSignatureListBox() {
		functionSignatureListBox = new ListBoxMVP();
		functionSignatureListBox.setWidth("500px");
		functionSignatureListBox.setId("functionSignatureListBox");
		functionSignatureListBox.setTitle("Select a function signature");		
		return functionSignatureListBox;
	}

	private FormLabel buildFunctionNameFormLabel() {
		return buildFormLabel("functionNameListBox", WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE);
	}
	
	private FormLabel buildFunctionSignatureFormLabel() {
		return buildFormLabel("functionSignatureListBox", CHOOSE_SIGNATURE_LABEL);
	}

	private FormLabel buildFormLabel(String forField, String label) {
		FormLabel functionLabel = new FormLabel();
		functionLabel.setFor(forField);
		functionLabel.setText(label);
		functionLabel.setTitle(label);
		return functionLabel;
	}

	private Button buildBuildButton() {
		Button buildButton = new Button();
		buildButton.setText("Build");
		buildButton.setTitle("Build");
		buildButton.setType(ButtonType.PRIMARY);
		buildButton.setMarginLeft(5.0);
		buildButton.setIcon(IconType.WRENCH);
		return buildButton;
	}

	private void onFunctionNameListBoxChange() {
		selectedFunctionIndex  = functionNameListBox.getSelectedIndex();
		functionSignatureListBox.clear();
		
		String name = functionNameListBox.getSelectedValue();
		if(selectedFunctionIndex == 0) {
			name = "";
		}
		
		final String finalName = name;
		List<FunctionSignature> signatures = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionSignatures());
		signatures = signatures.stream().filter(f -> f.getName().equals(finalName)).collect(Collectors.toList());
		
		if(signatures.size() > 1) {
			functionNameBuildButton.setVisible(false);
			functionSignaturePanel.setVisible(true);
			addFunctionSignaturesToListBox(signatures);
		} else {
			functionNameBuildButton.setVisible(true);
			functionSignaturePanel.setVisible(false);
		}
		
		functionModel.setName(name);
		this.updateCQLDisplay();
	}

	private void addFunctionNamesToListBox() {
		functionNameListBox.clear();
		functionNameListBox.addItem(SELECT_FUNCTION, SELECT_FUNCTION);			
		List<CQLIdentifierObject> userDefinedFunctions = new ArrayList<>();
		userDefinedFunctions.addAll(MatContext.get().getFuncs());
		userDefinedFunctions.addAll(MatContext.get().getIncludedFuncNames());
		userDefinedFunctions.forEach(f -> {
			functionNameListBox.insertItem(f.getDisplay(), f.toString(), f.getDisplay());
		});
		
		List<String> predefinedNames = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionNames());
		predefinedNames.remove("Coalesce");
		predefinedNames.forEach(f -> {
			functionNameListBox.insertItem(f, f);
		});
	}
	
	private void addFunctionSignaturesToListBox(List<FunctionSignature> signatures) {
		functionSignatureListBox.clear();
		functionSignatureListBox.addItem(SELECT_FUNCTION_SIGNATURE, SELECT_FUNCTION_SIGNATURE);			
		signatures.forEach(s -> {
			functionSignatureListBox.insertItem(s.getSignature(), s.getSignature());
		});
	}
	
}
