package org.ifmc.mat.client.diagramObject;

import java.util.List;

import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.shared.LabelBuilder;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class InsertPhrase extends DiagramObject {
	public static final int SAVE_BUTTON_ROW = 1;
	protected ListBox phraseListBox;
	protected String phrase;
	
	@Override
	public FlexTable start(DiagramView<?> view, List<String> elements, FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		this.view = view;
		this.okClickHandler = okClickHandler;
		this.cancelClickHandler = cancelClickHandler;
		init(view.getPropertyEditor(), g);
		action = ACTION.INSERT;
		initTable(elements);
		redraw(view.getPropertyEditor(), grid);

		return grid;
	}

	protected void initTable(List<String> elements) {
		this.view = view;
		if (view.getPropertyEditor().getWidgetCount() > 1)
			view.getPropertyEditor().remove(1);
		grid = new FlexTable();
		phraseListBox = new ListBox();
		Widget phraseLabel = LabelBuilder.buildLabel(phraseListBox,"Phrase");
		phraseLabel.addStyleName("gridLabel");
		grid.setWidget(0, 1, phraseLabel);
		addListBox(grid, phraseListBox, 1, 1, elements);
		phraseListBox.setEnabled(view.isEditable());
		addOkCancel(grid, SAVE_BUTTON_ROW);
	}
	
	@Override
	public boolean update() {
		phrase = phraseListBox.getItemText(phraseListBox.getSelectedIndex());
		return true;
	}
	
	public static DiagramObject clone(InsertPhrase src) throws IllegalArgumentException {
		InsertPhrase dest = new InsertPhrase();

		dest.identity = src.identity;
		dest.description = src.description;
		
		ListBox phraseListBox = src.phraseListBox;
		if (phraseListBox != null && phraseListBox.getSelectedIndex() >= 0)
			dest.phrase = phraseListBox.getItemText(phraseListBox.getSelectedIndex());
		else
			dest.phrase = "";
		return dest;
	}

	public String getPhrase() {
		return phrase;
	}
}
