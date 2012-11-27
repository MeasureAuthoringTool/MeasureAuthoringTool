package org.ifmc.mat.client.diagramObject.clickHandler;

import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;
import org.ifmc.mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ConditionalClickHandler extends DiagramObjectClickHandler {
	public ConditionalClickHandler(final DiagramView<?> view, final DiagramObject diagramObject) {
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
