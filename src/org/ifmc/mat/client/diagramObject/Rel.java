package org.ifmc.mat.client.diagramObject;

import java.util.HashMap;
import java.util.List;

import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.shared.model.QDSMeasurementTerm.QDSOperator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class Rel extends DiagramObject {
	public static final int SAVE_BUTTON_ROW = 1;
	protected static ListBox timingListBox;
	
	protected String timing;
	
	public Rel() {
		super("DURING");
	}
	
	public Rel(String identity) {
		super(identity);
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
			ClickHandler okClickHandler, ClickHandler deleteClickHandle, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.UPDATE;
		return init(view, elements, g, okClickHandler, deleteClickHandle, cancelClickHandler);
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
		timingListBox = new ListBox();
		Widget timingLabel = LabelBuilder.buildLabel(timingListBox,"Timing");
		timingLabel.addStyleName("gridLabel");
		grid.setWidget(0, 0, timingLabel);
		addListBox(grid, timingListBox, 1, 1, elements);
		timingListBox.setEnabled(view.isEditable());
		if (timing != null)
			setItemText(timingListBox, timing);
		addOkCancel(grid, SAVE_BUTTON_ROW);
	}
	
	@Override
	public boolean update() {
		timing = timingListBox.getItemText(timingListBox.getSelectedIndex());
		return true;
	}
	
	public static DiagramObject clone(Rel src) throws IllegalArgumentException {
		Rel dest = new Rel();

		dest.identity = src.identity;
		dest.description = src.description;
		ListBox phraseListBox = src.timingListBox;
		if (phraseListBox != null && phraseListBox.getSelectedIndex() >= 0)
			dest.timing = timingListBox.getItemText(timingListBox.getSelectedIndex());
		else
			dest.timing = "";
		return dest;
	}

	public String getTiming() {
		return timing;
	}
	
	public static String longToShortIdentity(QDSOperator operator) {
		return longToShortIdentity(operator.toString());
	}	
	
	public static String longToShortIdentity(String operator) {
		return getRelMap().get(operator);
	}	
	
	public static String shortToLongIdentity(QDSOperator operator) {
		for(String key : getRelMap().keySet()){
			if(getRelMap().get(key).equalsIgnoreCase(operator.toString())){
					return key;
			}
		}
		return null;
	}
	
	
	@Override
	public boolean hasPropertyEditor() {
		return true;
	}
	 //US 171, Getting rid of Rel class static hard coded values.
	private static HashMap<String,String> getRelMap(){
		return MatContext.get().getListBoxCodeProvider().getTimingConditionsMap();
	}
}
