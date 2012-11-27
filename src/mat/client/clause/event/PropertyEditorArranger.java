package mat.client.clause.event;

import java.util.List;

import mat.client.clause.view.DiagramView;
import mat.client.diagramObject.DiagramObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;

public class PropertyEditorArranger {
	public FlexTable create(DiagramView<?> view, List<String> elements, DiagramObject diagramObject, FlexTable grid, String top, 
			ClickHandler okClickHandler, ClickHandler cancelClickHandler) {
		return diagramObject.start(view, elements, grid, top, okClickHandler, cancelClickHandler);
	}
	
	public FlexTable select(DiagramView<?> view, List<String> elements, DiagramObject diagramObject, FlexTable grid, String top, 
			ClickHandler okClickHandler, ClickHandler deleteClickHandler, ClickHandler cancelClickHandler) {
		return diagramObject.select(view, elements, grid, top, okClickHandler, deleteClickHandler, cancelClickHandler);
	}
}
