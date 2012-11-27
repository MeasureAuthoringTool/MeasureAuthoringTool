package mat.client.diagramObject.clickHandler;

import mat.client.clause.view.DiagramView;
import mat.client.diagramObject.DiagramObject;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class RelClickHandler extends DiagramObjectClickHandler {
	public RelClickHandler(final DiagramView<?> view, final DiagramObject diagramObject) {
		super(view, diagramObject);
		handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				view.hilightDiagramObject(diagramObject, event);
			}
		};
	}
}
