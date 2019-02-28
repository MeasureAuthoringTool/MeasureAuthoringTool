package mat.client.expressionbuilder.modal;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
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
import mat.shared.CQLIdentifierObject;
import mat.shared.cql.model.FunctionSignature;

public class FunctionBuilderModal extends SubExpressionBuilderModal {

	private static final String WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE = "What function would you like to use?";
	private static final String SELECT_FUNCTION = "-- Select function --";
	private ListBoxMVP functionListBox;
	private int selectedFunctionIndex = 0;
	private FunctionModel functionModel;

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
		

		HorizontalPanel functionSelectorPanel = new HorizontalPanel();
		functionSelectorPanel.setWidth("50%");
		functionSelectorPanel.add(buildFunctionListBox());
		functionSelectorPanel.add(buildBuildButton());
		
		functionPanel.add(buildFormLabel());
		functionPanel.add(functionSelectorPanel);
		return functionPanel;
	}

	private FormLabel buildFormLabel() {
		FormLabel functionLabel = new FormLabel();
		functionLabel.setFor("functionListBox");
		functionLabel.setText(WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE);
		functionLabel.setTitle(WHAT_FUNCTION_WOULD_YOU_LIKE_TO_USE);
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

	private ListBoxMVP buildFunctionListBox() {		
		functionListBox = new ListBoxMVP();
		functionListBox.addItem(SELECT_FUNCTION, SELECT_FUNCTION);	
		addFunctionNamesToListBox();
		
		functionListBox.setId("functionListBox");
		functionListBox.setTitle("Select a function");
		
		functionListBox.addChangeHandler(event -> {
			selectedFunctionIndex  = functionListBox.getSelectedIndex();
			
			String name = functionListBox.getSelectedValue();
			if(selectedFunctionIndex == 0) {
				name = "";
			}
			
			functionModel.setName(name);
			this.updateCQLDisplay();
		});
		
		functionListBox.setSelectedIndex(selectedFunctionIndex);
		
		return functionListBox;
	}

	private void addFunctionNamesToListBox() {
		List<CQLIdentifierObject> userDefinedFunctions = new ArrayList<>();
		userDefinedFunctions.addAll(MatContext.get().getFuncs());
		userDefinedFunctions.addAll(MatContext.get().getIncludedFuncNames());
		userDefinedFunctions.forEach(f -> {
			functionListBox.insertItem(f.getDisplay(), f.toString(), f.getDisplay());
		});
		
		List<String> predefinedNames = new ArrayList<>(MatContext.get().getCqlConstantContainer().getFunctionNames());
		predefinedNames.remove("Coalesce");
		predefinedNames.forEach(f -> {
			functionListBox.insertItem(f, f);
		});
	}
}
