package org.ifmc.mat.client.clause.view.shape;

import org.ifmc.mat.client.clause.diagram.TraversalTree;
import org.ifmc.mat.client.clause.view.DiagramView;
import org.ifmc.mat.client.clause.view.Rect;
import org.ifmc.mat.client.diagramObject.DiagramObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public abstract class DiagramShape extends DrawingObject {
	public static final int ELLIPSE_RADIUS_X = 40;
	public static final int ELLIPSE_RADIUS_Y = 10;
	public static final int MAX_TEXT_LENGTH = 12;
	//public static final int MAX_TEXT_LENGTH_SIMPLE_STATEMENT = MAX_TEXT_LENGTH * SIMPLE_STATEMENT_WIDTH_MULTIPLIER;
	
	
	protected DiagramView<?> view;
	protected TraversalTree traversalTree;
	protected DiagramObject diagramObject;
	protected Rectangle rectangle;
	protected String caption;

	protected String foreColor;
	protected String backColor;
	protected boolean hasBorder;
	protected int roundRadius = 0;
	
	protected DiagramShape(DiagramView<?> view, TraversalTree traversalTree, DiagramObject diagramObject, 
			String caption, String foreColor, String backColor, boolean hasBorder) {
		this.view = view;
		this.diagramObject = diagramObject;
		this.traversalTree = traversalTree;
		this.rect = getRect(traversalTree);
		this.caption = caption;
		this.foreColor = foreColor;
		this.backColor = backColor;
		this.hasBorder = hasBorder;
	}
	
	DiagramShape(DiagramView<?> view, TraversalTree traversalTree, DiagramObject diagramObject,
			String caption, String foreColor, String backColor, int radius, boolean hasBorder) {
		this.view = view;
		this.traversalTree = traversalTree;
		this.rect = getRect(traversalTree);
		this.diagramObject = diagramObject;
		this.caption = caption;
		this.foreColor = foreColor;
		this.hasBorder = hasBorder;
		this.roundRadius = radius;
	}
	
	protected Rect getRect(TraversalTree t) {
		int width;
		int verticalSpacing;
		
		if (diagramObject.getClass().getName().contains("SimpleStatement")) {
			width = getRectWidthSimpleStatement();
			verticalSpacing = getRectVerticalSpacingSimpleStatement();
		}
		else {
			width = getRectWidth();
			verticalSpacing = getRectWidthSimpleStatement();
		}
		int left = 0;
		int top = 0;
		if (t != null) { // t can be null when cloning, i,e, TextRectangle.clone()
			left = (int)(LEFT_MARGIN + t.getX() * (double)verticalSpacing + 0.5);
			top = (int)(TOP_MARGIN + t.getY() * getRectHorizontalSpacing() + 0.5);
		}
		return new Rect(left, top, left + getRectWidthSimpleStatement(), top + getRectHeight());
	}
	
	protected String chop(String text, int maxTextLength) {
		final char ellipsis = 0x2026;
		//while (text.length() > MAX_TEXT_LENGTH)
		//	text = text.substring(0, text.length() - 1);	
		return text.substring(0, maxTextLength) + ellipsis;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public DiagramObject getDiagramObject() {
		return diagramObject;
	}

	public void setDiagramObject(DiagramObject diagramObject) {
		this.diagramObject = diagramObject;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	abstract public void calcExtent();
	
	public void draw() {
		draw(0, true);
	}
	abstract public void draw(int top, boolean clickable);
	
	abstract public void clear();
	
	public int getMaxTextLengthSimpleStatement(){
		return (int)(getRectWidth()/8);
	}
}
