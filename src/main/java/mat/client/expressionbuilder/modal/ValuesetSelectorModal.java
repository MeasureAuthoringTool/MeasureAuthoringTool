package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.model.ValuesetModel;
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

public class ValuesetSelectorModal extends SubExpressionBuilderModal {

	private static final String SELECT_A_VALUESET = "Select a value set";
	private static final String SELECT_VALUESET_PLACEHOLDER = "-- Select value set --";
	private ListBoxMVP valuesetListBox;

	public ValuesetSelectorModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Value Set", parent, parentModel, mainModel);
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
		if(valuesetListBox.getSelectedIndex() == 0) {
			this.getErrorAlert().createAlert("A valueset is required.");
			return;
		}
		
		String valuesetIdentifier = valuesetListBox.getSelectedValue();
		
		ValuesetModel model = new ValuesetModel(valuesetIdentifier, this.getParentModel());
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
		form.add(buildValuesetFormGroup());
		
		panel.add(form);
		
		return panel;
	}
	
	private FormGroup buildValuesetFormGroup() {
		FormGroup group = new FormGroup();
		FormLabel label = new FormLabel();
		label.setText(SELECT_A_VALUESET);
		label.setTitle(SELECT_A_VALUESET);
		
		
		valuesetListBox = new ListBoxMVP();
		valuesetListBox.insertItem(SELECT_VALUESET_PLACEHOLDER, SELECT_VALUESET_PLACEHOLDER, SELECT_VALUESET_PLACEHOLDER);
		List<CQLIdentifierObject> valuesets = new ArrayList<>();
		
		List<CQLQualityDataSetDTO> valuesetsAndCodes = MatContext.get().getValueSetCodeQualityDataSetList();
		List<CQLQualityDataSetDTO> valuesetDataSetDTO = valuesetsAndCodes.stream().filter(v -> v.getCodeIdentifier() == null).collect(Collectors.toList());
		
		List<CQLIdentifierObject> filteredValuesets = new ArrayList<>();
		for(int i = 0; i < valuesetDataSetDTO.size(); i++) {
			CQLIdentifierObject o = new CQLIdentifierObject(null, valuesetDataSetDTO.get(i).getName());
			filteredValuesets.add(o);
		}
		
		valuesets.addAll(IdentifierSortUtil.sortIdentifierList(filteredValuesets));
		valuesets.addAll(IdentifierSortUtil.sortIdentifierList(MatContext.get().getIncludedValueSetNames()));
		
		
		for(CQLIdentifierObject valueset : valuesets) {
			valuesetListBox.insertItem(valueset.getDisplay().substring(0, 90), valueset.toString(), valueset.getDisplay());
		}
		
		group.add(label);
		group.add(valuesetListBox);
		
		return group;
	}
}
