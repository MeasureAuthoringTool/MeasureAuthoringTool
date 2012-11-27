package org.ifmc.mat.client.diagramObject;

import java.util.Arrays;
import java.util.List;

import org.ifmc.mat.client.clause.MeasurePhrases;
import org.ifmc.mat.client.clause.diagram.TraversalTree;
import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SecondaryButton;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DiagramObject {
	public static final int PROPERTY_GRID_COLUMN = 0;
	protected static final int BUTTON_GRID_COL = 0;
	protected static final String EMPTY = "(empty)";
	public static final String DEFAULT_FORECOLOR = "black";
	public static final String DEFAULT_BACKCOLOR = "white";
	public static final String DEFAULT_HILIGHTCOLOR = "yellow";
	protected static int currentID = 1;
	
	protected String identity;
	protected String description;
	
	protected String mPhraseUniqueIdentity;
	protected String changedName;
	protected Rect rect;
	protected String foreColor;
	protected String backColor;
	protected boolean hasBorder;
	protected MeasurePhrases measurePhrases;
	protected boolean isHilighted;
	protected boolean expanded = true;
	protected String customName;
	protected static DiagramView<?> view;
	protected ClickHandler okClickHandler;
	//protected ClickHandler saveAsClickHandler;
	protected ClickHandler deleteClickHandler;
	protected ClickHandler cancelClickHandler;
	protected FlexTable grid;
	enum ACTION {INSERT, UPDATE};
	protected ACTION action = ACTION.INSERT;
	public enum ZOOM {COLLAPSED, EXPANDED};
	protected ZOOM zoom = ZOOM.COLLAPSED;
	
	public DiagramObject(String identity, String foreColor, String backColor, boolean hasBorder) {
		this.identity = identity;
		this.foreColor = foreColor;
		this.backColor = backColor;
		this.hasBorder = hasBorder;
		setHilighted(false);
	}
	
	
	public DiagramObject() {
		this(EMPTY);
	}
	
	public DiagramObject(String identity) {
		this(identity, DEFAULT_FORECOLOR, DEFAULT_BACKCOLOR, true);
	}
	
	public DiagramObject(String identity,String customName) {
		this(identity, DEFAULT_FORECOLOR, DEFAULT_BACKCOLOR, true);
		this.setCustomName(customName);
	}
	
	public DiagramObject(String identity, String foreColor, String backColor) {
		this(identity, foreColor, backColor, true);
	}
	
	public String getIdentity() {
		return identity;
	}
	
	public void setIdentity(String name) {
		this.identity = name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Rect getRect() {
		return rect;
	}
	
	public void setRect(int left, int top, int right, int bottom) {
		rect = new Rect(left, top, right, bottom);
	}
	
	public void setRect(Rect rect) {
		this.rect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
	}

	public String getForeColor() {
		return foreColor;
	}
	
	public String getBackColor() {
		return backColor;
	}

	public boolean hasBorder() {
		return hasBorder;
	}
	
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public void toggleExpanded() {
		expanded = !expanded;
	}
	
	public void init(HorizontalPanel propertyEditor, FlexTable g) {
		this.grid = g;
		if (grid != null)
			grid.clear();	
	}
	
	public FlexTable start(DiagramView<?> view, List<String> elements, FlexTable grid, String top, 
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.INSERT;
		Label label = new Label();
		label.setText("Don't call start() on DiagramObject parent.");
		grid.add(label);
		return grid;
	}
	
	public FlexTable select(DiagramView<?> view, List<String> identities, FlexTable grid, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.UPDATE;
		grid = new FlexTable();
		Label label = new Label();
		label.setText("Don't call setup() on DiagramObject parent.");
		grid.add(label);
		return grid;
	}
	
	protected void initTable() {
	}
	
	public boolean update() {
		return false;
	}
	
	protected void redraw(HorizontalPanel propertyEditor, FlexTable grid) {
		// remove the flex table from the property editor's scroll panel
		ScrollPanel scrollPanel = (ScrollPanel)(propertyEditor.getWidget(0));
		Widget widget = scrollPanel.getWidget();
		if (widget != null)
			scrollPanel.remove(widget);
		scrollPanel.add(grid);
	}
	
	/*
	protected void addSaveSaveAsCancel2(FlexTable grid, int row) {
		HorizontalPanel buttonFlexTable = new HorizontalPanel();
		PrimaryButton saveButton = new PrimaryButton();
		saveButton.setText("Save");
		//saveButton.setTitle(action == ACTION.INSERT ? "Insert simple expression" : "Update simple expression");
		saveButton.setTitle("Save");
		saveButton.addClickHandler(okClickHandler);
		saveButton.setEnabled(view.isEditable());
		saveButton.setEnabled(view.isEditable());
		buttonFlexTable.add(saveButton);
		
		Label space1 = new Label();
		space1.setText("\t\t\t");
		buttonFlexTable.add(space1);
		
		if (action == ACTION.UPDATE) {
			
			Label space2 = new Label();
			space2.setText("\t\t\t");
			buttonFlexTable.add(space2);
		}
		
		SecondaryButton cancelButton = new SecondaryButton();
		cancelButton.setText("Cancel");			
		cancelButton.setTitle("Cancel");
		cancelButton.addClickHandler(cancelClickHandler);
		buttonFlexTable.add(cancelButton);
		cancelButton.setEnabled(view.isEditable());
		
		grid.setWidget(row, BUTTON_GRID_COL, buttonFlexTable);
	}
	*/

	protected void addSaveCancelPhrase(FlexTable grid, int row) {
		addSomethingCancel(grid, row,  "Save Phrase");
	}
	
	protected void addSaveCancel(FlexTable grid, int row) {
		addSomethingCancel(grid, row,  "Save");
	}
	
	private void addSomethingCancel(FlexTable grid, int row, String saveText) {
		HorizontalPanel buttonFlexTable = new HorizontalPanel();
		PrimaryButton saveButton = new PrimaryButton();
		saveButton.setText(saveText);
		//saveButton.setTitle(action == ACTION.INSERT ? "Insert simple expression" : "Update simple expression");
		saveButton.setTitle(saveText);
		saveButton.addClickHandler(okClickHandler);
		saveButton.setEnabled(view.isEditable());
		saveButton.setEnabled(view.isEditable());
		buttonFlexTable.add(saveButton);
		
		Label space1 = new Label();
		space1.setText("\t\t\t");
		buttonFlexTable.add(space1);
		
		if (action == ACTION.UPDATE) {
			
			Label space2 = new Label();
			space2.setText("\t\t\t");
			buttonFlexTable.add(space2);
		}
		
		SecondaryButton cancelButton = new SecondaryButton();
		cancelButton.setText("Cancel");			
		cancelButton.setTitle("Cancel");
		cancelButton.addClickHandler(cancelClickHandler);
		buttonFlexTable.add(cancelButton);
		cancelButton.setEnabled(view.isEditable());
		
		grid.setWidget(row, BUTTON_GRID_COL, buttonFlexTable);
	}

	/*
	 * public void setSaveAsClickHandler(ClickHandler saveAsClickHandler) {
		this.saveAsClickHandler = saveAsClickHandler;
	}
	*/
	
	protected void addOkCancel(FlexTable grid, int row) {
		HorizontalPanel buttonFlexTable = new HorizontalPanel();
		PrimaryButton saveButton = new PrimaryButton();
		saveButton.setText("Ok");
		saveButton.setTitle("Ok");
		saveButton.addClickHandler(okClickHandler);
		saveButton.setEnabled(view.isEditable());
		saveButton.setEnabled(view.isEditable());
		buttonFlexTable.add(saveButton);
		
		Label space1 = new Label();
		space1.setText("\t\t\t");
		buttonFlexTable.add(space1);
		/*
		if (action == ACTION.UPDATE && hasDeleteButton) {
			SecondaryButton deleteButton = new SecondaryButton();
			deleteButton.setText("Delete");
			deleteButton.setTitle("Delete");
			deleteButton.addClickHandler(deleteClickHandler);
			deleteButton.setEnabled(view.isEditable());
			deleteButton.setEnabled(view.isEditable());
			buttonFlexTable.add(deleteButton);		
			Label space3 = new Label();
			space3.setText("\t\t\t");
			buttonFlexTable.add(space3);			
		}
		*/
		SecondaryButton cancelButton = new SecondaryButton();
		cancelButton.setText("Cancel");			
		cancelButton.setTitle("Cancel");
		cancelButton.addClickHandler(cancelClickHandler);
		buttonFlexTable.add(cancelButton);
		
		grid.setWidget(row, BUTTON_GRID_COL, buttonFlexTable);
	}
	
	protected Label addLabel(FlexTable grid, String caption, int row, int col) {
		Label label = new Label();	
		label.setText(caption);
		label.addStyleName("gridLabel");
		try {
			grid.setWidget(row, col, label);
			MatContext.get().setAriaHidden(grid.getWidget(row,col),"false");
			return label;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	protected Widget addLabel(FlexTable grid, String caption, int row, int col, String forVal) {
		Widget label = LabelBuilder.buildLabel(caption, forVal);
		label.addStyleName("gridLabel");
		try {
			grid.setWidget(row, col, label);
			return label;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	protected void addListBox(FlexTable grid, ListBox listBox, int row, int col, List<String> elements) {
		addElementsToListBox(listBox, elements.toArray(new String[elements.size()]));
		grid.setWidget(row, col, listBox);
	}
	
	protected void addListBox(FlexTable grid, ListBox listBox, int row, int col, String[] elements) {
		addElementsToListBox(listBox, elements);
		grid.setWidget(row, col, listBox);
	}

	protected void addListBox(Panel panel, ListBox listBox, List<String> elements) {
		addElementsToListBox(listBox, elements.toArray(new String[elements.size()]));
		panel.add(listBox);
	}
	
	protected void addListBox(Panel panel, ListBox listBox, int phraseListRow, int col, List<String> elements) {
		listBox.clear();
		addElementsToListBox(listBox, elements.toArray(new String[elements.size()]));
		panel.add(listBox);
	}
	
	protected void addAndSortListToListBox(Panel panel, ListBox listBox, int phraseListRow, int col, List<String> elements){
		listBox.clear();
		addAndSortElementsToListBox(listBox, elements.toArray(new String[elements.size()]));
		panel.add(listBox);
	}
	
	protected String getItemText(ListBox listBox, int i) {
		if (listBox == null)
			return null;
		return listBox.getValue(i);
	}
	
	protected String getItemText(ListBox listBox) {
		if (listBox == null)
			return "";
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex < 0)	
			if (listBox.getItemCount() > 0) {
				listBox.setSelectedIndex(0);
				selectedIndex = 0;
			}
			else
				return "";
		if (getItemText(listBox, selectedIndex).startsWith("-"))
			listBox.setSelectedIndex(--selectedIndex);		
		return listBox.getValue(selectedIndex);
	}
	
	protected void setItemText(ListBox listBox, String string) {
		if (string == null)
			return;
		for (int i = 0; i < listBox.getItemCount(); ++i)
			if (getItemText(listBox, i).equals(string)) {
				listBox.setSelectedIndex(i);
				return;
			}
	}

	public boolean isHilighted() {
		return isHilighted;
	}

	public void setHilighted(boolean isHilighted) {
		this.isHilighted = isHilighted;
	}
	
	public void draw(TraversalTree tree, MeasurePhrases measurePhrases, ZOOM zoom) {
	}
	
	public void setView(DiagramView<?> view) {
		this.view = view;
	}
	
	public DiagramView<?> getView() {
		return view;
	}

	public boolean hasPropertyEditor() {
		return false;
	}
	
	public boolean canHaveChildren() {
		return true;
	}
	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}
	
	public String getmPhraseUniqueIdentity() {
		return mPhraseUniqueIdentity;
	}


	public void setmPhraseUniqueIdentity(String mPhraseUniqueIdentity) {
		this.mPhraseUniqueIdentity = mPhraseUniqueIdentity;
	}
	
	public String getChangedName() {
		return changedName;
	}


	public void setChangedName(String changedName) {
		this.changedName = changedName;
	}

	protected void addAndSortElementsToListBox(ListBox listBox, String[] elements){
		Arrays.sort(elements, String.CASE_INSENSITIVE_ORDER);
		for (String element : elements)
			listBox.addItem(element);	
		listBox.setSelectedIndex(0);
	} 
	
	protected void addElementsToListBox(ListBox listBox, String[] elements){
		for (String element : elements)
			listBox.addItem(element);	
		listBox.setSelectedIndex(0);
	}
}
