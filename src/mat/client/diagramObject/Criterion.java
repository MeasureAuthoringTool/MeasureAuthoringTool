package mat.client.diagramObject;

import java.util.List;

import mat.client.clause.view.DiagramView;
import mat.client.shared.LabelBuilder;
import mat.shared.diagramObject.InProgress_Complete;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Criterion extends DiagramObject {
	protected static final int DESCRIPTION_PANEL_ROW = 0;
	protected static final int DESCRIPTION_PANEL_COL = 0;
	public static final int SAVE_BUTTON_ROW = 1;
	protected int index = -1;
	protected String description;
	protected InProgress_Complete inProgressComplete;
	protected TextBox descriptionTextBox;
	protected ListBox inProgressCompleteListBox;
	
	public Criterion(String name, String customName, int index){
		super(name, customName);
		this.index = index;
		this.description = "";
		this.inProgressComplete = new InProgress_Complete();
	}
	
	public Criterion(String name, int index) {
		super(name + index);
		this.index = index;
		this.description = "";
		this.inProgressComplete = new InProgress_Complete();
	}
	
	@Override
	public FlexTable start(DiagramView<?> view, List<String> elements, FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.INSERT;
		return init(view, elements, g, okClickHandler, null, cancelClickHandler);
	}

	@Override
	public FlexTable select(DiagramView<?> view, List<String> elements,FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.UPDATE;
		return init(view, elements, g, okClickHandler, deleteClickHandler, cancelClickHandler);
	}
	
	private FlexTable init(DiagramView<?> view, List<String> elements, FlexTable g,
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.okClickHandler = okClickHandler;
		this.cancelClickHandler = cancelClickHandler;
		if (action == ACTION.UPDATE)
			this.deleteClickHandler = deleteClickHandler;
		init(view.getPropertyEditor(), g);
		initTable(elements);
		redraw(view.getPropertyEditor(), grid);

		return grid;
	}
	
	protected void initTable(List<String> elements) {
		if (view.getPropertyEditor().getWidgetCount() > 1)
			view.getPropertyEditor().remove(1);
		grid = new FlexTable();
		addDescriptionPanel();
//		addOkCancel(grid, SAVE_BUTTON_ROW, view.criterionHasOnlyOneChild());
		addOkCancel(grid, SAVE_BUTTON_ROW);
	}
	
	private void addDescriptionPanel() {
		VerticalPanel descriptionPanel = new VerticalPanel();
		
		descriptionTextBox = new TextBox();
		descriptionTextBox.setMaxLength(50);
		descriptionTextBox.setWidth("50em");
		descriptionTextBox.setText(description);
		descriptionTextBox.setTitle("Description for this simple expression" + ((identity == null) ? " is blank" : identity));
		descriptionTextBox.setEnabled(view.isEditable());
		
		Widget descriptionLabel = LabelBuilder.buildLabel(descriptionTextBox,"Description\t\t");
		descriptionLabel.addStyleName("gridLabel");	
		descriptionPanel.add(descriptionLabel);
		descriptionPanel.add(descriptionTextBox);
		
		inProgressCompleteListBox = new ListBox();
		Widget statusLabel = LabelBuilder.buildLabel(inProgressCompleteListBox,"Status");
		statusLabel.setTitle("Status");
		descriptionPanel.add(statusLabel);
		
		addListBox(descriptionPanel, inProgressCompleteListBox, InProgress_Complete.getItems());
		inProgressCompleteListBox.setEnabled(view.isEditable());
		setItemText(inProgressCompleteListBox, inProgressComplete.toString());
		grid.setWidget(DESCRIPTION_PANEL_ROW, DESCRIPTION_PANEL_COL, descriptionPanel);
		grid.getFlexCellFormatter().setColSpan(DESCRIPTION_PANEL_ROW, DESCRIPTION_PANEL_COL, 4);
	}
	
	@Override
	public boolean update() {
		description = descriptionTextBox.getText().trim();
		inProgressComplete.set(getItemText(inProgressCompleteListBox));
		return true;
	}
	
	public static DiagramObject clone(Criterion src) throws IllegalArgumentException {
		Criterion dest = new Criterion(src.identity, src.index);
		
		dest.identity = src.identity;
		dest.description = src.description;
		dest.inProgressComplete = src.inProgressComplete;
		return dest;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public InProgress_Complete getInProgressComplete() {
		return inProgressComplete;
	}
	
	public void setInProgress() {
		inProgressComplete.setInProgress();
	}
	
	public boolean isInProgress() {
		return inProgressComplete.isInProgress();
	}
	
	public void setComplete() {
		inProgressComplete.setComplete();
	}
	
	public boolean isComplete() {
		return inProgressComplete.isComplete();
	}
	
	@Override
	public boolean hasPropertyEditor() {
		return true;
	}		
}
