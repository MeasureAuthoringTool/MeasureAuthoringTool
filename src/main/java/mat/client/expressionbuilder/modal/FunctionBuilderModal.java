package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpandCollapseCQLExpressionPanel;
import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.client.expressionbuilder.observer.FunctionBuildButtonObserver;
import mat.client.expressionbuilder.util.ExpressionTypeUtil;
import mat.client.expressionbuilder.util.IdentifierSortUtil;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.shared.CQLIdentifierObject;
import mat.shared.cql.model.FunctionArgument;
import mat.shared.cql.model.FunctionSignature;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionBuilderModal extends SubExpressionBuilderModal {

	private static final String CHOOSE_SIGNATURE_LABEL = "This function has multiple signatures available. Please choose a signature.";
	private static final String WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE = "What function would you like to use?";
	private static final String SELECT_FUNCTION = "-- Select a Function --";
	private static final String SELECT_FUNCTION_SIGNATURE = "-- Select Function Signature --";
	private ListBoxMVP functionNameListBox;
	private int selectedFunctionIndex = 0;
	private int selectedFunctionSignatureIndex = 0;
	private FunctionModel functionModel;
	private ListBoxMVP functionSignatureListBox;
	private VerticalPanel functionSignaturePanel;
	private Button functionNameBuildButton;
	private VerticalPanel functionPanel;
	private VerticalPanel functionNamePanel;

	public FunctionBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel,
			ExpressionBuilderModel mainModel, boolean isFirstSelection) {
		super("Function", parent, parentModel, mainModel);
		functionModel = new FunctionModel(parentModel);
		this.getParentModel().appendExpression(functionModel);
		this.getApplyButton().addClickHandler(event -> onApplyButtonClick());
		this.setFirstSelection(isFirstSelection);
		display();
		functionSignaturePanel.setVisible(false);
	}

	private void onApplyButtonClick() {
		// the apply button on this screen will always have only one argument, so we will check the arguments built vs. the number of
		// arguments there should be
		if(functionNameListBox.getSelectedIndex() == 0 || getSignaturesForForFunctionName(functionModel.getName()).get(0).getArguments().size() != functionModel.getArguments().size() ||
				!areAllArgumentsFilledOut()) {
			this.getErrorAlert().createAlert("All field are required.");
			return;
		}
		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	private boolean areAllArgumentsFilledOut() {
		for(ExpressionBuilderModel argument : functionModel.getArguments()) {
			if(argument.getChildModels().isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		functionPanel = new VerticalPanel();
		functionPanel.setStyleName("selectorsPanel");
		
		functionNamePanel = new VerticalPanel();
		functionNamePanel.setWidth("50%");
		functionNamePanel.add(buildFunctionNameFormLabel());
		functionNamePanel.add(buildFunctionNameSelectionPanel());

		functionSignaturePanel = new VerticalPanel();
		functionSignaturePanel.setWidth("50%");
		functionSignaturePanel.add(buildFunctionSignatureFormLabel());
		functionSignaturePanel.add(buildFunctionSignatureSelectionPanel());
		
		if(selectedFunctionIndex > 0) {
			functionSignaturePanel.setVisible(false);
			List<FunctionSignature> signatures = getSignaturesForForFunctionName(functionNameListBox.getSelectedValue());
			
			if(signatures.size() > 1) {
				addFunctionSignaturesToListBox(getSignaturesForForFunctionName(functionNameListBox.getSelectedValue()));
				functionSignatureListBox.setSelectedIndex(selectedFunctionSignatureIndex);
				functionSignaturePanel.setVisible(true);
				functionNameBuildButton.setVisible(false);
			}
		}

		functionPanel.add(functionNamePanel);
		functionPanel.add(new SpacerWidget());
		functionPanel.add(new SpacerWidget());
		functionPanel.add(functionSignaturePanel);		
		return functionPanel;
	}
	
	private HorizontalPanel buildFunctionNameSelectionPanel() {
		HorizontalPanel functionNameSelectionPanel = new HorizontalPanel();
		functionNameSelectionPanel.add(buildFunctionNameListBox());
		
		functionNameBuildButton = buildBuildButton();
		functionNameBuildButton.addClickHandler(event -> onFunctionNameBuildButtonClick());
		functionNameSelectionPanel.add(functionNameBuildButton);
		return functionNameSelectionPanel;
	}

	private Widget buildFunctionSignatureSelectionPanel() {
		HorizontalPanel functionSignatureListBoxPanel = new HorizontalPanel();
		functionSignatureListBoxPanel.add(buildFunctionSignatureListBox());
		
		Button functionSignatureBuildButton = buildBuildButton();
		functionSignatureBuildButton.addClickHandler(event -> onFunctionSignatureBuildButtonClick());
		functionSignatureListBoxPanel.add(functionSignatureBuildButton);
		return functionSignatureListBoxPanel;
	}
	
	private void onFunctionNameBuildButtonClick() {
		if(functionNameListBox.getSelectedIndex() != 0) {
			FunctionSignature signatureToUse = getSignaturesForForFunctionName(this.functionModel.getName()).get(0);
			if(!signatureToUse.getArguments().isEmpty()) {
				FunctionBuildButtonObserver observer = new FunctionBuildButtonObserver(this, this.functionModel, this.getMainModel(), signatureToUse);
				observer.onBuildButtonClick();
			} else {
				// when there is not function arguments, put the contents into a collapsable panel
				functionNamePanel.setVisible(false);
				functionPanel.add(buildFunctionNameFormLabel());
				functionPanel.add(new ExpandCollapseCQLExpressionPanel(signatureToUse.getName(), this.functionModel.getCQL("")));
			}
		} else {
			this.getErrorAlert().createAlert("A function is required.");
		}
	}
	
	private void onFunctionSignatureBuildButtonClick() {
		if(functionSignatureListBox.getSelectedIndex() != 0) {
			FunctionSignature signatureToUse = getFunctionSignatureBySignatureString(functionSignatureListBox.getSelectedValue());
			FunctionBuildButtonObserver observer = new FunctionBuildButtonObserver(this, this.functionModel, this.getMainModel(), signatureToUse);
			observer.onBuildButtonClick();
		} else {
			this.getErrorAlert().createAlert("A function and signature are required.");
		}
	}

	private FunctionSignature getFunctionSignatureBySignatureString(String selectedSignature) {
		List<FunctionSignature> signatures = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionSignatures());

		FunctionSignature signatureToUse = null;
		for(FunctionSignature signature : signatures) {
			if(signature.getSignature().equals(selectedSignature)) {
				signatureToUse = signature;
				break;
			}
		}
		return signatureToUse;
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
		functionSignatureListBox.addChangeHandler(event -> onFunctionSignatureListBoxChange());
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
		this.getErrorAlert().clearAlert();
		selectedFunctionIndex  = functionNameListBox.getSelectedIndex();
		functionSignatureListBox.clear();
		
		String name = functionNameListBox.getSelectedValue();
		if(selectedFunctionIndex == 0) {
			name = "";
		}
		
		final String finalName = name; // replace quotes in case of user defined function
		List<FunctionSignature> signatures = getSignaturesForForFunctionName(finalName);
		
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
	
	private void onFunctionSignatureListBoxChange() {
		this.getErrorAlert().clearAlert();
		selectedFunctionSignatureIndex = functionSignatureListBox.getSelectedIndex();
	}

	private List<FunctionSignature> getSignaturesForForFunctionName(String finalName) {
		final String name  = finalName.replaceAll("\"", "");
		List<FunctionSignature> signatures = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionSignatures());
		signatures.addAll(convertUserFunctionToFunctionSignatures());		
		signatures = signatures.stream().filter(f -> f.getName().equals(name)).collect(Collectors.toList());
		return signatures;
	}

	private void addFunctionNamesToListBox() {
		functionNameListBox.clear();
		functionNameListBox.addItem(SELECT_FUNCTION, SELECT_FUNCTION);			
		List<CQLIdentifierObject> userDefinedFunctions = new ArrayList<>();
		
		CQLType firstReturnType = null;
		
		if (this.isFirstSelection()) {
			userDefinedFunctions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getFuncs()));
			userDefinedFunctions.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedFuncNames()));
		} else {
			firstReturnType = this.getParentModel().getChildModels().get(0).getType();
			userDefinedFunctions.addAll(ExpressionTypeUtil.getFunctionsBasedOnReturnType(firstReturnType));
		}
		
		userDefinedFunctions.forEach(f -> {
			functionNameListBox.insertItem(f.toString(), f.getDisplay());
		});

		List<String> predefinedNames = new ArrayList<>(ExpressionTypeUtil.getPreDefinedFunctionsBasedOnReturnType(firstReturnType));
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
	
	private List<FunctionSignature> convertUserFunctionToFunctionSignatures() {
		List<FunctionSignature> signatures = new ArrayList<>();
		List<CQLFunctions> functions = new ArrayList<>();
		functions.addAll(MatContext.get().getCQLModel().getCqlFunctions());
		functions.addAll(MatContext.get().getCQLModel().getIncludedFunc());
	
		for(CQLFunctions function : functions) {
			FunctionSignature signature = new FunctionSignature();
			
			String name = function.getFunctionName();
			
			if(function.getAliasName() != null) {
				name = function.getAliasName() + "." + name;
			}
			
			signature.setName(name);
			signature.setReturnType(function.getReturnType());
			
			List<FunctionArgument> arguments = new ArrayList<>();
			if(function.getArgumentList() != null) {
				for(CQLFunctionArgument argument : function.getArgumentList()) {
					FunctionArgument functionArgument = new FunctionArgument();
					functionArgument.setName(argument.getArgumentName());
					functionArgument.setReturnType(argument.getReturnType().trim());
					arguments.add(functionArgument);
				}
			}
			
			signature.setArguments(arguments);
			signatures.add(signature);
		}
		
				
		return signatures;
	}
	
}
