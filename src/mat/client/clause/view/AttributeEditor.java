package mat.client.clause.view;

import java.util.List;

import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.codelist.HasListBox;
import mat.client.diagramObject.Phrase;
import mat.client.diagramObject.SimpleStatement;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MessageDelegate;
import mat.model.clause.QDSAttributes;
import mat.shared.Attribute;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AttributeEditor {
	private static final String SELECT = "--Select--";
	private static final String CHECK_IF_PRESENT = "Check if Present";
	private static final String COMPARISON = "Comparison";
	private static final String QDS_ELEMENT = "Value Set";
	
	private DiagramViewImpl<?> view;
	private Phrase phrase;
	private List<Attribute>attributes;
	private VerticalPanel attributeTablePanel;
	private Grid attributeTable;
	private ListBox attributeList;
	private Button addAttributeButton;
	private ListBox attributeTypeList;	
	private Button attributeUpdateTypeButton;
	private ListBox attributeComparisonList;
	private TextBox attributeQuantity;
	private ListBox attributeUnit;
	private Button attributeComparisonUpdateButton;
	private ListBoxMVP attributeQDSElementList;
	private Button attributeQDSElementUpdateButton;
	private Button attributeEditorSaveButton;
	private QDSAttributesServiceAsync attributeService;
	private String currentAttribute;
	private String currentType;
	private String currentComparisonOperator;
	private String currentQuantity;
	private String currentUnit;
	private String currentTerm;
	private String text;
	
	public AttributeEditor(
			DiagramViewImpl<?> view,
			Phrase phrase,
			VerticalPanel attributeTablePanel,
			ListBox attributeList, Button addAttributeButton, 
			ListBox attributeTypeList, Button attributeUpdateTypeButton,
			ListBox attributeComparisonList, TextBox attributeQuantity, ListBox attributeUnit, Button attributeComparisonUpdateButton,
			ListBoxMVP attributeQDSElementList, Button attributeQDSElementUpdateButton,
			Button attributeEditorSaveButton) {
		this.view = view;
			
		this.phrase = phrase;
		this.attributes = phrase.cloneAttributes();

		this.attributeTablePanel = attributeTablePanel;
		
		this.attributeList = attributeList;
		this.addAttributeButton = addAttributeButton;
		
		this.attributeTypeList = attributeTypeList;
		this.attributeUpdateTypeButton = attributeUpdateTypeButton;
		
		this.attributeComparisonList = attributeComparisonList;
		this.attributeQuantity = attributeQuantity;
		//US211
		attributeQuantity.setMaxLength(100);
		this.attributeUnit = attributeUnit;
		this.attributeComparisonUpdateButton = attributeComparisonUpdateButton;
		
		this.attributeQDSElementList = attributeQDSElementList;
		this.attributeQDSElementUpdateButton = attributeQDSElementUpdateButton;
		
		this.attributeEditorSaveButton = attributeEditorSaveButton;
		
		text = phrase.getText();
		drawTable();
		if (view.isEditable()) {
			initGridWidgets(false);
			initAttributeList(text);
		}
		else {
			 setVisible(attributeList,false);
			 setVisible(addAttributeButton,false);			
		}
	}

	private void drawTable() {
		if (attributeTablePanel.getWidgetCount() > 0)
			attributeTablePanel.remove(0);
		
		if (attributes.size() == 0) {
			setVisible(attributeTablePanel,true);
			return;
		}
		
		attributeTable = new Grid(Math.max(2, attributes.size()+1), 4);
		attributeTablePanel.add(attributeTable);
		setVisible(attributeTablePanel,true);
		
		String[] title = {"ATTRIBUTE", "TYPE", "TERM"};
		int row = 0;
		
		for (int col= 0; col < title.length; ++col) {
			Label labelAttribute = new Label();
			labelAttribute.setText(title[col]);
			labelAttribute.setTitle(title[col]);
			labelAttribute.addStyleName("gridTitle");
			attributeTable.setWidget(row, col, labelAttribute);
			attributeTable.getCellFormatter().addStyleName(row, col, "tableBorder");
		}
		
		++row;
		for (final Attribute attribute : attributes) {
			attributeTable.setText(row, 0, attribute.getAttribute());
			attributeTable.getCellFormatter().addStyleName(row, 0, "tableBorder");
			attributeTable.setText(row,1, attribute.getType());
			attributeTable.getCellFormatter().addStyleName(row, 1, "tableBorder");
			String termStr = MatContext.get().getTextSansOid(attribute.getTerm());
			attributeTable.setText(row, 2, termStr);
			attributeTable.getCellFormatter().addStyleName(row, 2, "tableBorder");
			
			Button removeButton = new Button();
			removeButton.setText("Remove");
			removeButton.setTitle("Remove " + attribute.getAttribute());
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					remove(attribute.getAttribute());
					view.setDiagramDirty();
					moveFocusToPropertyEditor();
				}
			});
			attributeTable.setWidget(row, 3, removeButton);
			//US 430. Disable the remove button for read only mode
			removeButton.setEnabled(view.isEditable());
			++row;
		}
		moveFocusToPropertyEditor();
	}

	private void remove(String removeAttribute) {
		for (Attribute attribute : attributes)
			if (attribute.getAttribute().equals(removeAttribute)) {
				attributes.remove(attribute);
				drawTable();
				
				setVisible(attributeEditorSaveButton,true);
				initGridWidgets(true);
				initAttributeList(text);
				return;
			}
	}
	
	private void initGridWidgets(boolean showSave) {
		currentAttribute = null;
		currentType = null;
		currentComparisonOperator = null;
		currentQuantity = null;
		currentUnit = null;
		currentTerm = null;

		attributeList.setEnabled(true);
		setVisible(attributeList,true);
		addAttributeButton.setEnabled(true);
		setVisible(addAttributeButton,true);
		
		attributeTypeList.setEnabled(true);
		setVisible(attributeTypeList,false);
		setVisible(attributeUpdateTypeButton,false);
		
		attributeComparisonList.setEnabled(true);
		setVisible(attributeComparisonList,false);
		setVisible(attributeQuantity,false);
		attributeQuantity.setText("");
		setVisible(attributeUnit,false);
		setVisible(attributeComparisonUpdateButton,false);
		
		attributeQDSElementList.setEnabled(true);
		setVisible(attributeQDSElementList,false);
		setVisible(attributeQDSElementUpdateButton,false);
		
		setVisible(attributeEditorSaveButton,showSave);
	}
	
	private void initList(ListBox listBox, String[] items) {
		listBox.clear();
		for (String item : items) 
			listBox.addItem(item);
	}
	
	private void initAttributeQDSElementList(ListBoxMVP listBox, String[] items) {
		listBox.clear();
		for (String item : items){
			((ListBoxMVP)listBox).insertItem(item, item, item, -1);
		}
	}
	
	/**
	 * Initialize the attribute units to the list box.
	 * @param listBox
	 * @param itemList
	 * @param defaultOption
	 */
	private void initList(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.insertItem("", 0);
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}
	
	private boolean isSelect(String s) {
		if (s.equals(SELECT)) {
			view.propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getPleaseSelectItemMessage());
			return true;
		}
		return false;
	}
	
	private void initAttributeList(String qdselName) {
		if (qdselName==null) return;

		/*
		 * qdselName arg could be of the form: 
		 * <<value set name>>:<<data type>>
		 * where <<value set name>> and <<data type>> could contain one or more ':'
		 * the logic to process this content is now in QDSAttributesDAO where DataType searches can more easily be made
		 */
		attrListByDataType(attributeList, qdselName);
	}
	
	
	
	private void attrListByDataType(final ListBox listBox, String dataTypeName) {
		attributeService = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
		attributeService.getAllDataTypeAttributes(dataTypeName, new AsyncCallback<List<QDSAttributes>>() {

			@Override
			public void onFailure(Throwable caught) {
				listBox.clear();
				caught.printStackTrace();
				System.out.println("Error retrieving data type attributes. " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<QDSAttributes> result) {
				listBox.clear();
				for (QDSAttributes q : result) {
					String name = q.getName();
					boolean isDuplicate = false;
					
					for (Attribute attribute : attributes)
						if (attribute.getAttribute().equals(name)) {
							isDuplicate = true;
							break;
						}
						if (!isDuplicate)
							listBox.addItem(name);
				}
				
				if (attributeList.getItemCount() == 0) {
					setVisible(attributeList,false);
					setVisible(addAttributeButton,false);
				}
			}
		});	
	}
	
	public void onClickAddAttributeButton(ClickEvent event) {
		final String[] attributeTypes = {SELECT, CHECK_IF_PRESENT, COMPARISON, QDS_ELEMENT};
		
		String attribute = attributeList.getItemText(attributeList.getSelectedIndex());
		if (!isSelect(attribute)) {
			currentAttribute = attribute;
			attributeList.setEnabled(false);
			initList(attributeTypeList, attributeTypes);
			setVisible(addAttributeButton,false);
			setVisible(attributeTypeList,true);
			setVisible(attributeUpdateTypeButton,true);
			moveFocusToPropertyEditor();
		}
	}

	public void onClickAttributeUpdateTypeButton(ClickEvent event) {
		String type = attributeTypeList.getItemText(attributeTypeList.getSelectedIndex());
		if (!isSelect(type)) {
			currentType = type;
			attributeTypeList.setEnabled(false);
			setVisible(attributeUpdateTypeButton,false);
			
			if (type.equals(CHECK_IF_PRESENT))
				attributeEditorSelectIfPresent();
			else if (type.equals(COMPARISON))
				attributeEditorComparison();
			else if (type.equals(QDS_ELEMENT))
				attributeEditorQDSElement();
		}
	}

	private void attributeEditorSelectIfPresent() {	
		Attribute attribute = new Attribute(currentAttribute, currentType);
		attributes.add(attribute);
		setVisible(attributeEditorSaveButton,true);
		drawTable();
		initAttributeList(text);
		initGridWidgets(true);
	}
	
	private void attributeEditorQDSElement() {
		initAttributeQDSElementList(attributeQDSElementList, phrase.getQDSElements());
		setVisible(attributeQDSElementList,true);
		setVisible(attributeQDSElementUpdateButton,true);
		moveFocusToPropertyEditor();
	}
	
	public void onClickAttributeQDSElementUpdateButton(ClickEvent event) {
		String term = attributeQDSElementList.getItemTitle(attributeQDSElementList.getSelectedIndex());
		if (!isSelect(term)) {
			currentTerm = term;
			Attribute attribute = new Attribute(currentAttribute, currentType, currentTerm);
			attributes.add(attribute);
			setVisible(attributeEditorSaveButton,true);
			initAttributeList(text);
			drawTable();
			initGridWidgets(true);
		}
	}
	
	private void attributeEditorComparison() {
		setVisible(attributeComparisonList,true);
		initList(attributeComparisonList, SimpleStatement.comparisonOperators);
		setVisible(attributeQuantity,true);
		
		//US 62. Retrieve and initialize units to the attributes page.
		MatContext.get().getListBoxCodeProvider().getUnitMatrixListByCategory(ConstantMessages.UNIT_ATTRIBUTE, new AsyncCallback<List<? extends HasListBox>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				view.propEditErrorMessages.setMessage(MessageDelegate.s_ERR_RETRIEVE_UNITS);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {				
				initList(attributeUnit, result, null);
			}
       });

		
		
		setVisible(attributeUnit,true);
		setVisible(attributeComparisonUpdateButton,true);
		moveFocusToPropertyEditor();
	}

	public void onClickAttributeComparisonUpdateButton(ClickEvent event) {
		currentQuantity = attributeQuantity.getText().trim();
		try {
			//US211
			new Double(currentQuantity);
			if(currentQuantity.length()>100)
				throw new Exception();
//			new Integer(currentQuantity);
		}
		catch (Exception e) {
			view.propEditErrorMessages.setMessage(MatContext.get().getMessageDelegate().getQuantityNumMessage());
			return;
		}
		
		currentComparisonOperator = attributeComparisonList.getItemText(attributeComparisonList.getSelectedIndex());
		currentUnit = attributeUnit.getItemText(attributeUnit.getSelectedIndex());
		
		Attribute attribute = new Attribute(currentAttribute, currentType, currentComparisonOperator, currentQuantity, currentUnit, currentTerm);
		attributes.add(attribute);
		drawTable();
		setVisible(attributeEditorSaveButton,true);
		initAttributeList(text);
		initGridWidgets(true);
	}
	
	public void onClickAttributeEditorSaveButton(ClickEvent event) {
		view.setDiagramDirty();
		phrase.setAttributes(attributes);
		moveFocusToPropertyEditor();
	}
	
	
	public void setVisible(Widget widget, Boolean visible){
		MatContext.get().setVisible(widget, visible);
	}
	
	private void moveFocusToPropertyEditor(){
		view.moveFocusToPropertyEditor();
	}
}
