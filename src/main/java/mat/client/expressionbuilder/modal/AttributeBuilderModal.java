package mat.client.expressionbuilder.modal;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.expressionbuilder.component.ExpressionTypeSelectorList;
import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.model.AttributeModel;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;
import mat.client.expressionbuilder.observer.BuildButtonObserver;
import mat.client.expressionbuilder.util.QueryFinderHelper;
import mat.client.shared.CQLTypeContainer;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.QDMContainer;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributeBuilderModal extends SubExpressionBuilderModal {

	private static final String AN_ELEMENT_AND_AN_ATTRIBUTE_ARE_REQUIRED = "An Element and an Attribute are required.";
	private static final String HOW_WOULD_YOU_LIKE_TO_CLARIFY_YOUR_ATTRIBUTE = "How would you like to clarify your attribute? (Optional)";
	private static final String WHAT_ATTRIBUTE_WOULD_YOU_LIKE_TO_FIND = "What attribute would you like to find?";
	private static final String SELECT_AN_ATTRIBUTE = "-- Select an Attribute --";
	private AttributeModel attributeModel;
	private BuildButtonObserver buildButtonObserver;
	private boolean isSourceRequired;
	private ListBoxMVP clarifyingAttributeListBox;
	private ListBoxMVP attributeListBox;
	private QDMContainer qdmContainer;
	private CQLTypeContainer cqlTypeContainer;
	private FormGroup clarifyingAttributeFormGroup;
	private int attributeSelectedIndex = 0;
	private int clarifyingAttributeSelectedIndex = 0;
	private String selectedAttribute;

	public AttributeBuilderModal(ExpressionBuilderModal parent, ExpressionBuilderModel parentModel, ExpressionBuilderModel mainModel) {
		super("Attribute", parent, parentModel, mainModel);
		qdmContainer = MatContext.get().getCqlConstantContainer().getQdmContainer();
		cqlTypeContainer = MatContext.get().getCqlConstantContainer().getCqlTypeContainer();
		
		attributeModel = new AttributeModel(parentModel); 
		this.getParentModel().appendExpression(attributeModel);
		
		this.isSourceRequired = !QueryFinderHelper.isPartOfSort(attributeModel);
		
		buildButtonObserver = new BuildButtonObserver(this, attributeModel.getSource(), mainModel);
		display();
		this.getApplyButton().addClickHandler(event -> onApplyButtonClickHandler());
	}

	private void onApplyButtonClickHandler() {

		if(isSourceRequired && attributeModel.getSource().getChildModels().isEmpty()) {
			this.getErrorAlert().createAlert(AN_ELEMENT_AND_AN_ATTRIBUTE_ARE_REQUIRED);			
			return;
		}
		
		if(attributeModel.getAttributes().isEmpty()) {
			if(isSourceRequired) {
				this.getErrorAlert().createAlert(AN_ELEMENT_AND_AN_ATTRIBUTE_ARE_REQUIRED);			
				return;
			} else {
				this.getErrorAlert().createAlert("An Attribute is required.");			
			}
		}
		
		this.getExpressionBuilderParent().showAndDisplay();
	}

	@Override
	public void display() {
		this.getContentPanel().clear();
		this.getErrorAlert().clearAlert();
		this.getContentPanel().add(buildContentPanel());
		this.updateCQLDisplay();
	}

	private Widget buildContentPanel() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("selectorsPanel");
		
	
		if(isSourceRequired) {
			List<ExpressionType> availableExpressionTypes = new ArrayList<>();
			availableExpressionTypes.add(ExpressionType.DEFINITION);
			availableExpressionTypes.add(ExpressionType.FUNCTION);
			String label = "What element are you wanting to use the attribute for?";
			ExpressionTypeSelectorList selectors = new ExpressionTypeSelectorList(
					availableExpressionTypes, new ArrayList<>(), QueryFinderHelper.findAliasNames(attributeModel), 
					buildButtonObserver, this.attributeModel.getSource(), label, this);
			panel.add(selectors);
		}

		panel.add(buildAttributeFormGroup());
		panel.add(buildClarifyingAttributeFormGroup());
		
		
		return panel;
	}
	
	private Widget buildAttributeFormGroup() {
		FormGroup attributeFormGroup = new FormGroup();
		
		FormLabel label = new FormLabel();
		label.setFor("attributeListBox");
		label.setText(WHAT_ATTRIBUTE_WOULD_YOU_LIKE_TO_FIND);
		label.setTitle(WHAT_ATTRIBUTE_WOULD_YOU_LIKE_TO_FIND);
		
		attributeListBox = new ListBoxMVP();
		attributeListBox.setId("attributeListBox");
		attributeListBox.insertItem(SELECT_AN_ATTRIBUTE, SELECT_AN_ATTRIBUTE);
				
		List<String> attributes = qdmContainer.getAttributes();
		for(String attribute : attributes) {
			attributeListBox.insertItem(attribute, attribute);
		}
		
		attributeListBox.setSelectedIndex(attributeSelectedIndex);
		attributeListBox.addChangeHandler(event -> {
			selectedAttribute = attributeListBox.getSelectedValue();			
			addContentToClarifyingAttributeListBox(getClarifyingAttributesForAttribute(selectedAttribute));
			addAttributesToModel();
			attributeSelectedIndex = attributeListBox.getSelectedIndex();
			this.updateCQLDisplay();
		});
		
		attributeFormGroup.add(label);
		attributeFormGroup.add(attributeListBox);
		
		attributeFormGroup.setWidth("36%");
		return attributeFormGroup;
	}
	
	private Widget buildClarifyingAttributeFormGroup() {
		clarifyingAttributeFormGroup = new FormGroup();
		clarifyingAttributeFormGroup.setVisible(false);
		
		FormLabel clarifyingAttributeLabel = new FormLabel();
		clarifyingAttributeLabel.setFor("clarifyingAttributeListBox");
		clarifyingAttributeLabel.setText(HOW_WOULD_YOU_LIKE_TO_CLARIFY_YOUR_ATTRIBUTE);
		clarifyingAttributeLabel.setTitle(HOW_WOULD_YOU_LIKE_TO_CLARIFY_YOUR_ATTRIBUTE);
		
		clarifyingAttributeListBox = new ListBoxMVP();
		clarifyingAttributeListBox.setId("clarifyingAttributeListBox");
		
		if(selectedAttribute == null) {
			addContentToClarifyingAttributeListBox(new ArrayList<>());
		} else {
			if(!getClarifyingAttributesForAttribute(selectedAttribute).isEmpty()) {
				addContentToClarifyingAttributeListBox(getClarifyingAttributesForAttribute(selectedAttribute));
				clarifyingAttributeFormGroup.setVisible(true);
			}
		}
		
		
		clarifyingAttributeFormGroup.add(clarifyingAttributeLabel);
		clarifyingAttributeFormGroup.add(clarifyingAttributeListBox);
		
		
		clarifyingAttributeFormGroup.setWidth("36%");
		
		clarifyingAttributeListBox.addChangeHandler(event -> {
			addAttributesToModel();
			clarifyingAttributeSelectedIndex = clarifyingAttributeListBox.getSelectedIndex();
			this.updateCQLDisplay();
		});
		
		clarifyingAttributeListBox.setSelectedIndex(clarifyingAttributeSelectedIndex);
		return clarifyingAttributeFormGroup;
	}
	
	private void addContentToClarifyingAttributeListBox(List<String> contentList) {
		clarifyingAttributeListBox.clear();
		clarifyingAttributeListBox.insertItem(SELECT_AN_ATTRIBUTE, SELECT_AN_ATTRIBUTE);

		if(contentList != null && !contentList.isEmpty()) {
			contentList.forEach(attribute -> clarifyingAttributeListBox.insertItem(attribute, attribute));
		}

	}
	
	private List<String> getClarifyingAttributesForAttribute(String attribute) {
		boolean visibility = false;
		List<String> types = qdmContainer.getCQLTypeByAttribute(attribute);
		Set<String> clarifyingAttributes = new HashSet<>();
		if(types != null) {
			for(String type : types) {
				List<String> typeAttributes = cqlTypeContainer.getCQLTypeAttributesByType(type);
				if(typeAttributes != null) {
					visibility = true;
					clarifyingAttributes.addAll(cqlTypeContainer.getCQLTypeAttributesByType(type));
				}
			}
		}
		
		clarifyingAttributeFormGroup.setVisible(visibility);
		List<String> clarifyingAttributeList = new ArrayList<>(clarifyingAttributes);
		clarifyingAttributeList.sort(Comparator.naturalOrder());
		return clarifyingAttributeList;
	}
	
	private void addAttributesToModel() {
		attributeModel.getAttributes().clear();
		
		if(attributeListBox.getSelectedIndex() != 0) {
			attributeModel.getAttributes().add(attributeListBox.getSelectedValue());
		}
		
		if(clarifyingAttributeListBox.getSelectedIndex() != 0) {
			attributeModel.getAttributes().add(clarifyingAttributeListBox.getSelectedValue());
		}
	}

}
