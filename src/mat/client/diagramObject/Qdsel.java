package mat.client.diagramObject;

import java.util.List;

import mat.client.clause.view.DiagramView;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class Qdsel extends DiagramObject {
	public static final int SAVE_BUTTON_ROW = 1;
	private ListBoxMVP qdselListBox;
	
	public Qdsel() {
		super();
	}

	public Qdsel(String identity) {
		super(identity);
	}

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

	@Override
	public FlexTable select(DiagramView<?> view, List<String> elements,FlexTable g, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandle, ClickHandler cancelClickHandler) {
		this.view = view;
		action = ACTION.UPDATE;
		init(view, elements, g, okClickHandler, deleteClickHandle, cancelClickHandler);
		return grid;
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
		
		qdselListBox = new ListBoxMVP();
		Widget qdselLabel = LabelBuilder.buildLabel(qdselListBox,"Phrase Element:");
		qdselLabel.addStyleName("gridLabel");
		grid.setWidget(0, 0, qdselLabel);
		addListBox(grid, qdselListBox, 1, 1, elements);
		qdselListBox.setEnabled(view.isEditable());
		if (identity != null)
			setItemText(qdselListBox, identity);
		addOkCancel(grid, SAVE_BUTTON_ROW);
	}
	
	@Override
	public boolean update() {
		identity = qdselListBox.getValue(qdselListBox.getSelectedIndex());
		return true;
	}
	
	public static DiagramObject clone(Qdsel src) {
		Qdsel dest = new Qdsel();
		dest.identity = src.identity;
		dest.description = src.description;
		return dest;
	}
	
	@Override
	public boolean hasPropertyEditor() {
		return true;
	}
	
	@Override
	public boolean canHaveChildren() {
		return false;
	}
}