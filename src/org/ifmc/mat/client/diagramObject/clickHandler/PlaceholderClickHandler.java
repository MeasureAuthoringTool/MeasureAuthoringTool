package org.ifmc.mat.client.diagramObject.clickHandler;

import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;
import org.ifmc.mat.client.diagramObject.PlaceHolder;
import org.ifmc.mat.client.shared.MatContext;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PlaceholderClickHandler extends DiagramObjectClickHandler {
	public PlaceholderClickHandler(final DiagramView<?> view, final DiagramObject diagramObject) {
		super(view, diagramObject);
		handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				view.hilightDiagramObject(diagramObject, event);
				NativeEvent nativeEvent = event.getNativeEvent(); 
				if (nativeEvent.getShiftKey())
					view.selectMeasurePhrase(((PlaceHolder)diagramObject).getSimpleStatement().getIdentity());
			}
		};
	}
}
