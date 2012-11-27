package mat.client.diagramObject;

import java.util.List;

import mat.client.clause.view.DiagramView;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class Conditional extends DiagramObject {
	public static final int SAVE_BUTTON_ROW = 1;
	protected String[] identities = {"AND", "OR"};
	protected ListBox conditionListBox;
	
	public Conditional() {
		super("AND");
	}
	
	public Conditional(String identity) {
		super(identity, "black", "#D0DBEC", false);
	}

	public String[] getIdentities() {
		return identities;
	}
	
	@Override
	public FlexTable start(DiagramView<?> view, List<String> identities, FlexTable g, String top,
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.INSERT;
		return init(view, g, okClickHandler, null, cancelClickHandler);
	}

	@Override
	public FlexTable select(DiagramView<?> view, List<String> identities, FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.UPDATE;
		return init(view, g, okClickHandler, deleteClickHandler, cancelClickHandler);
	}
	
	private FlexTable init(DiagramView<?> viewr, FlexTable g,
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		this.okClickHandler = okClickHandler;
		this.cancelClickHandler = cancelClickHandler;
		if (action == ACTION.UPDATE)
			this.deleteClickHandler = deleteClickHandler;
		init(view.getPropertyEditor(), g);
		
		initTable();
		redraw(view.getPropertyEditor(), grid);
		
		return grid;
	}
	
	@Override
	protected void initTable() {	
		if (view.getPropertyEditor().getWidgetCount() > 1)
			view.getPropertyEditor().remove(1);
		grid = new FlexTable();
		
		Label conditionalLabel = new Label();	
		conditionalLabel.setText("Conditional");
		conditionalLabel.addStyleName("gridLabel");
		grid.setWidget(0, 0, conditionalLabel);
		addListBox(grid, conditionListBox = new ListBox(), 1, 1, identities);
		conditionListBox.setEnabled(view.isEditable());
		setItemText(conditionListBox, identity);
		addOkCancel(grid, SAVE_BUTTON_ROW);
	}
	
	@Override
	public boolean update() {
		identity = conditionListBox.getItemText(conditionListBox.getSelectedIndex());
		return true;
	}
	
	public static DiagramObject clone(Conditional src) throws IllegalArgumentException {
		Conditional dest = new Conditional();
		dest.identity = src.identity;
		dest.description = src.description;
		return dest;
	}
	
	@Override
	public boolean hasPropertyEditor() {
		return true;
	}	
}
