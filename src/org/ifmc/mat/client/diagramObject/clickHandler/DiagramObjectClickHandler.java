package org.ifmc.mat.client.diagramObject.clickHandler;

import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;
import org.ifmc.mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class DiagramObjectClickHandler {
	final DiagramView<?> view;
	final DiagramObject diagramObject;
	protected ClickHandler handler;
	
	public DiagramObjectClickHandler(final DiagramView<?> view, final DiagramObject diagramObject) {
		this.view = view;
		this.diagramObject = diagramObject;
		this.handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
			}
		};		
	}

	public ClickHandler getClickHandler() {
		return handler;
	}
}
