package org.ifmc.mat.client.clause.view.shape;

import org.ifmc.mat.client.clause.diagram.TraversalTree;
import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.diagramObject.DiagramObject;

public class TextRoundRect extends TextRectangle {
	TextRoundRect(DiagramView<?> view, TraversalTree t, DiagramObject diagramObject,
			String caption, String foreColor, String backColor, boolean hasBorder) {
		super(view, t, diagramObject, caption, foreColor, backColor, 20, true);
	}
}
	