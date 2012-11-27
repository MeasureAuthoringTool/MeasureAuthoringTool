package org.ifmc.mat.client.clause.view.shape;

import org.ifmc.mat.client.clause.diagram.TraversalTree;
import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;

public class DiagramShapeFactory {	
	public static DiagramShape getShape(DiagramView<?> view, TraversalTree t, DiagramObject diagramObject) {
		String displayText = diagramObject.getCustomName()!=null ? diagramObject.getCustomName() : 
			diagramObject.getIdentity();
		
		TextRectangle textRectangle = new TextRectangle(
			view, t, diagramObject,
			displayText, 
			diagramObject.getForeColor(), diagramObject.getBackColor(), diagramObject.hasBorder());
		return textRectangle;	
	}
}
