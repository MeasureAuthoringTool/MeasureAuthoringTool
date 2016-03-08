package mat.client.clause.cqlworkspace;

import mat.client.clause.cqlworkspace.CQLWorkSpacePresenter.ViewDisplay;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class AddParameterToAceEditorDialogBox {
	public static  void showListOfParametersDialogBox(final ViewDisplay searchDisplay){
		final Modal dialogModal = new Modal();
		dialogModal.setTitle("Select Parameter");
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("AddParameter_Modal");
		dialogModal.setSize(ModalSize.SMALL);
		ModalBody modalBody = new ModalBody();
		
		final ListBoxMVP listAllParamNames = new ListBoxMVP();
		listAllParamNames.setWidth("290px");
		listAllParamNames.addItem(MatContext.get().PLEASE_SELECT);
		for (int i = 0; i < searchDisplay.getViewParameterList().size(); i++) {
			listAllParamNames.addItem(searchDisplay.getViewParameterList().get(i).getParameterName());
		}
		
		// main form
		Form bodyForm = new Form();
		// message Form group
		final FormGroup messageFormgroup = new FormGroup();
		final HelpBlock helpBlock = new HelpBlock();
		messageFormgroup.add(helpBlock);
		messageFormgroup.getElement().setAttribute("role", "alert");
		// CQL Data Type Drop down Form group
		final FormGroup paramNameFormGroup = new FormGroup();
		FormLabel availableParamFormLabel = new FormLabel();
		availableParamFormLabel.setText("Available Parameters");
		availableParamFormLabel.setTitle("Available Parameters ID");
		availableParamFormLabel.setFor("listParamType");
		paramNameFormGroup.add(availableParamFormLabel);
		paramNameFormGroup.add(listAllParamNames);
		
		FieldSet formFieldSet = new FieldSet();
		formFieldSet.add(messageFormgroup);
		formFieldSet.add(paramNameFormGroup);
		
		bodyForm.add(formFieldSet);
		modalBody.add(bodyForm);
		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		final Button addButton = new Button();
		addButton.setText("Insert");
		addButton.setTitle("Insert");
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
		listAllParamNames.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				paramNameFormGroup.setValidationState(ValidationState.NONE);
				helpBlock.setText("");
				messageFormgroup.setValidationState(ValidationState.NONE);
				
			}
			
		});
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex = listAllParamNames.getSelectedIndex();
				if (selectedIndex != 0) {
					String paramName = listAllParamNames.getItemText(selectedIndex);
					if(paramName.equalsIgnoreCase(MatContext.get().PLEASE_SELECT)){
						paramNameFormGroup.setValidationState(ValidationState.ERROR);
						helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
						helpBlock.setText("Please Select Valid Parameter name to insert into Editor");
						messageFormgroup.setValidationState(ValidationState.ERROR);
					} else {
						int columnIndex = searchDisplay.getDefineAceEditor().getCursorPosition().getColumn();
						System.out.println(columnIndex);
						searchDisplay.getDefineAceEditor().insertAtCursor(" " + paramName);
						dialogModal.hide();
					}
				} else {
					paramNameFormGroup.setValidationState(ValidationState.ERROR);
					helpBlock.setIconType(IconType.EXCLAMATION_CIRCLE);
					helpBlock.setText("Please Select Parameter name to insert into Editor");
					messageFormgroup.setValidationState(ValidationState.ERROR);
				}
				
			}
			
		});
		dialogModal.show();
	}
}
