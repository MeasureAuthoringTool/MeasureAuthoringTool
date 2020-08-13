package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.model.CodeModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.util.IdentifierSortUtil;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLIdentifierObject;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeSelectorModal extends SubExpressionBuilderModal {

	private static final String SELECT_A_CODE = "Select a code";
	private static final String SELECT_CODE_PLACEHOLDER = "-- Select code --";
	private ListBoxMVP codeListBox;

	public CodeSelectorModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Code", parent, parentModel, mainModel);
		this.setHideOtherModals(false);
		this.setClosable(false);
		this.setRemoveOnHide(true);
		this.setWidth("35%");
		this.setCQLPanelVisible(false);
		this.getElement().getStyle().setZIndex(9999);
		this.getApplyButton().addClickHandler(event -> applyButtonClickHandler());
		display();
	}
	
	public void applyButtonClickHandler() {
		if(codeListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A code is required.");
			return;
		}
		
		String codeIdentifier = codeListBox.getSelectedValue();
		
		CodeModel model = new CodeModel(codeIdentifier, this.getParentModel());
		this.getParentModel().appendExpression(model);
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		this.getContentPanel().clear();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		Form form = new Form();
		form.add(buildCodeFormGroup());
		
		panel.add(form);
		
		return panel;
	}
	
	private FormGroup buildCodeFormGroup() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(SELECT_A_CODE);
		label.setTitle(SELECT_A_CODE);
		
		
		codeListBox = new ListBoxMVP();
		codeListBox.insertItem(SELECT_CODE_PLACEHOLDER, SELECT_CODE_PLACEHOLDER, SELECT_CODE_PLACEHOLDER);
		List<CQLIdentifierObject> codes = new ArrayList<>();
		
		List<CQLQualityDataSetDTO> valuesetsAndCodes = MatContext.get().getValueSetCodeQualityDataSetList();
		List<CQLQualityDataSetDTO> codeDataSetDTOs = valuesetsAndCodes.stream().filter(v -> v.getCodeIdentifier() != null).collect(Collectors.toList());
		
		List<CQLIdentifierObject> filteredCodes = new ArrayList<>();
		for(int i = 0; i < codeDataSetDTOs.size(); i++) {
			CQLIdentifierObject o = new CQLIdentifierObject(null, codeDataSetDTOs.get(i).getDisplayName());
			filteredCodes.add(o);
		}
		
		codes.addAll(IdentifierSortUtil.sortIdentifierList(filteredCodes));
		codes.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedCodeNames()));
		
		
		for(CQLIdentifierObject code : codes) {
			codeListBox.insertItem(code.getDisplay().substring(0, 90), code.toString(), code.getDisplay());
		}
		
		group.add(label);
		group.add(codeListBox);
		
		return group;
	}
}
