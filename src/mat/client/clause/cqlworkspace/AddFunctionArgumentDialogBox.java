package mat.client.clause.cqlworkspace;

import java.util.List;
import mat.client.shared.ListBoxMVP;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLGrammarDataType;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;



public class AddFunctionArgumentDialogBox {
	private static List<String> argumentDataTypeList = CQLGrammarDataType.getDataTypeName();
	
	public static  void showArgumentDialogBox(final CQLModel currentModel, final CQLFunctionArgument functionArg, boolean isEdit){
		String saveButtonText = "Add";
		String modalText = "Add Argument";
		if (isEdit) {
			saveButtonText = "Edit";
			modalText = "Edit Argument " + functionArg.getArgumentName();
		}
		Modal dialogModal = new Modal();
		dialogModal.setTitle(modalText);
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("AddEditArgument_Modal");
		dialogModal.setSize(ModalSize.SMALL);
		
		ModalBody modalBody = new ModalBody();
		Container modalContainer = new Container();
		Row rowOne = new Row();
		Row rowTwo = new Row();
		Row rowThird = new Row();
		Row rowFourth = new Row();
		Row rowFifth = new Row();
		Row rowSixth = new Row();
		Column rowOneColOne = new Column(ColumnSize.SM_5);
		Column rowTwoColOne = new Column(ColumnSize.SM_5);
		Column rowThirdColOne = new Column(ColumnSize.SM_5);
		Column rowFourthColOne = new Column(ColumnSize.SM_5);
		Column rowFifthColOne = new Column(ColumnSize.SM_5);
		Column rowSixthColOne = new Column(ColumnSize.SM_5);
		
		Label labelDataTypeLabel = new Label(LabelType.INFO,"Available Datatypes");
		labelDataTypeLabel.setId("Available_Datatypes");
		labelDataTypeLabel.setText("Available Datatypes");
		labelDataTypeLabel.getElement().setAttribute("style", "font-size:75%;");
		
		final ListBoxMVP listAllDataTypes = new ListBoxMVP();
		listAllDataTypes.setWidth("150px");
		listAllDataTypes.addItem("-- Select--");
		for (int i = 0; i < argumentDataTypeList.size(); i++) {
			listAllDataTypes.addItem(argumentDataTypeList.get(i));
		}
		
		
		Label labelArgumentName = new Label(LabelType.INFO,"Argument Name");
		labelArgumentName.setText("Argument Name");
		labelArgumentName.getElement().setAttribute("style", "font-size:75%;");
		
		final TextArea argumentNameTextArea = new TextArea();
		argumentNameTextArea.setPlaceholder("Enter Argument Name");
		argumentNameTextArea.setWidth("250px");
		argumentNameTextArea.setHeight("25px");
		
		
		Label labelSelectItem = new Label(LabelType.INFO, "Select Item");
		labelSelectItem.setText("Select Item");
		labelSelectItem.getElement().setAttribute("style", "font-size:75%;");
		
		final ListBoxMVP listSelectItem = new ListBoxMVP();
		listSelectItem.setWidth("150px");
		listSelectItem.setEnabled(false);
		
		if(isEdit){
			String selectedDataType = null;
			for(int i=0;i<listAllDataTypes.getItemCount();i++){
				if(listAllDataTypes.getItemText(i).equalsIgnoreCase(functionArg.getArgumentType())){
					listAllDataTypes.setSelectedIndex(i);
					selectedDataType = functionArg.getArgumentType();
					break;
				}
			}
			if(selectedDataType.equalsIgnoreCase("definition")
					|| functionArg.getArgumentType().equalsIgnoreCase("parameter")){
				argumentNameTextArea.setEnabled(false);
				argumentNameTextArea.clear();
				if(selectedDataType.equalsIgnoreCase("definition")){
					addDefinitionInList(listSelectItem, currentModel.getDefinitionList());
					for(int i=0;i<listSelectItem.getItemCount();i++){
						if(listSelectItem.getItemText(i).equalsIgnoreCase(functionArg.getArgumentName())){
							listSelectItem.setSelectedIndex(i);
							break;
						}
					}
				} else if(selectedDataType.equalsIgnoreCase("parameter")){
					addParamInList(listSelectItem, currentModel.getCqlParameters());
					for(int i=0;i<listSelectItem.getItemCount();i++){
						if(listSelectItem.getItemText(i).equalsIgnoreCase(functionArg.getArgumentName())){
							listSelectItem.setSelectedIndex(i);
							break;
						}
					}
				}
			} else {
				argumentNameTextArea.setEnabled(true);
				argumentNameTextArea.clear();
				argumentNameTextArea.setText(functionArg.getArgumentName());
				listSelectItem.clear();
				listSelectItem.setEnabled(false);
			}
			
		}
		
		
		rowOneColOne.add(labelDataTypeLabel);
		rowTwoColOne.add(listAllDataTypes);
		rowThirdColOne.add(labelArgumentName);
		rowFourthColOne.add(argumentNameTextArea);
		rowFifthColOne.add(labelSelectItem);
		rowSixthColOne.add(listSelectItem);
		
		rowOne.add(rowOneColOne);
		rowTwo.add(rowTwoColOne);
		rowTwo.setMarginTop(15.00);
		rowThird.add(rowThirdColOne);
		rowThird.setMarginTop(15.00);
		rowFourth.add(rowFourthColOne);
		rowFourth.setMarginTop(15.00);
		rowFifth.add(rowFifthColOne);
		rowFifth.setMarginTop(15.00);
		rowSixth.add(rowSixthColOne);
		rowSixth.setMarginTop(15.00);
		rowSixth.setMarginBottom(15.00);
		
		modalContainer.add(rowOne);
		modalContainer.add(rowTwo);
		modalContainer.add(rowThird);
		modalContainer.add(rowFourth);
		modalContainer.add(rowFifth);
		modalContainer.add(rowSixth);
		
		modalBody.add(modalContainer);
		ModalFooter modalFooter = new ModalFooter();
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		
		addButton.setText(saveButtonText);
		addButton.setTitle(saveButtonText);
		addButton.setType(ButtonType.PRIMARY);
		addButton.setSize(ButtonSize.SMALL);
		
		Button closeButton = new Button();
		closeButton.setText("Close");
		closeButton.setTitle("Close");
		closeButton.setType(ButtonType.DANGER);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(addButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		
		listAllDataTypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (listAllDataTypes.getValue().contains("Select")) {
					addButton.setEnabled(false);
					argumentNameTextArea.setEnabled(true);
				} else if (listAllDataTypes.getValue().equalsIgnoreCase("definition")) {
					addDefinitionInList(listSelectItem, currentModel.getDefinitionList());
					listSelectItem.setEnabled(true);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(false);
				} else if (listAllDataTypes.getValue().equalsIgnoreCase("Parameter")) {
					addParamInList(listSelectItem, currentModel.getCqlParameters());
					listSelectItem.setEnabled(true);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(false);
				} else {
					listSelectItem.clear();
					listSelectItem.setEnabled(false);
					addButton.setEnabled(true);
					argumentNameTextArea.setEnabled(true);
				}
				
			}
			
		});
		
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		dialogModal.show();
		
	}
	/**
	 * Method to add CQLParameter content in listSelectItem
	 * @param listSelectItem - ListMVP
	 * @param cqlParameters - List
	 */
	protected static void addParamInList(ListBoxMVP listSelectItem, List<CQLParameter> cqlParameters) {
		listSelectItem.clear();
		listSelectItem.addItem("-- Select--");
		for (CQLParameter param : cqlParameters) {
			listSelectItem.addItem(param.getParameterName());
		}
		
	}
	
	/**
	 * Method to add CQLDefinition content in listSelectItem
	 * @param listSelectItem - ListMVP
	 * @param definitionList - List
	 */
	protected static void addDefinitionInList(ListBoxMVP listSelectItem, List<CQLDefinition> definitionList) {
		listSelectItem.clear();
		listSelectItem.addItem("-- Select--");
		for (CQLDefinition define : definitionList) {
			listSelectItem.addItem(define.getDefinitionName());
		}
		
	}
	
}
