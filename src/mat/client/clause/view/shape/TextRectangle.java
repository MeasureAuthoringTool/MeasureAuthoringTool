package mat.client.clause.view.shape;

import mat.client.clause.AppController;
import mat.client.clause.diagram.Diagram;
import mat.client.clause.diagram.TraversalTree;
import mat.client.clause.view.DiagramView;
import mat.client.diagramObject.DiagramObject;
import mat.client.diagramObject.DiagramObjectFactory;
import mat.client.diagramObject.PlaceHolder;
import mat.client.diagramObject.Qdsel;
import mat.client.diagramObject.clickHandler.DiagramObjectClickHandlerFactory;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class TextRectangle extends DiagramShape {
	private static final int CANVAS_MARGIN = 10;
	private static final String FONT_NAME = "Arial";
	private static final int FONT_SIZE = 12;
	public static final int DIAGRAM_OBJECT_EXPANDED_ARROW_WIDTH = 12;
	public static final int DIAGRAM_OBJECT_EXPANDED_ARROW_HEIGHT = 12;
	public static final int DIAGRAM_OBJECT_COLLAPSED_ARROW_WIDTH = 12;
	public static final int DIAGRAM_OBJECT_COLLAPSED_ARROW_HEIGHT = 12;	
	protected boolean hasExpandButton = false;
	
	protected Text rectText;
	protected int width;
	protected int maxTextLength;
	protected int topMargin;
	protected int rectBottomMargin;
	private boolean isDrawingSimpleStatement;
	
	private final String EXPAND_TITLE = "Expand";
	private final String COLLAPSE_TITLE = "Collapse";

	protected TextRectangle(DiagramView<?> view, TraversalTree t, DiagramObject diagramObject, String caption, 
			String foreColor, String backColor, boolean hasBorder) {
		super(view, t, diagramObject, caption, foreColor, backColor, hasBorder);
		init(diagramObject);
	}

	protected TextRectangle(DiagramView<?> view, TraversalTree t, DiagramObject diagramObject, String caption, 
			String foreColor, String backColor, int r, boolean hasBorder) {
		super(view, t, diagramObject, caption, foreColor, backColor, r, hasBorder);
		init(diagramObject);
	}
		
	private void init(DiagramObject diagramObject) {
		String className = diagramObject.getClass().getName();
		isDrawingSimpleStatement = className.contains("Qdsel") && view.getCurrentCriterion().equals("Measure Phrase");
		isDrawingSimpleStatement = isDrawingSimpleStatement || (className.contains("PlaceHolder") && diagramObject.isExpanded());
		if (isDrawingSimpleStatement) {
			width = getRectWidthSimpleStatement();	
			maxTextLength = getMaxTextLengthSimpleStatement();
		}			
		else {
			width = getRectWidth();	
//			maxTextLength = MAX_TEXT_LENGTH;
			maxTextLength = getMaxTextLengthSimpleStatement();
		}
		topMargin = Diagram.TOP_MARGIN;	
		rectBottomMargin = DIAGRAM_OBJECT_EXPANDED_ARROW_HEIGHT + 3;
		if (traversalTree == null)
			hasExpandButton = className.contains("PlaceHolder");
		else
			hasExpandButton = (traversalTree.getChildren().size() > 0 || className.contains("PlaceHolder"));
	}
	
	public void calcExtent() {
		rect = getRect(traversalTree);
		
		int x = rect.getLeft();	
		int y = rect.getTop();
		view.calcExtent(x + width + CANVAS_MARGIN, topMargin + y + getRectHeight()  + CANVAS_MARGIN);
	}
	
	public void draw(int top, boolean clickable) {
		DrawingArea canvas = view.getCanvas();
		rect = getRect(traversalTree);
		
		int x = rect.getLeft();	
		int y = topMargin + rect.getTop();
		
		if (hasExpandButton) {
			Image image;
			if (diagramObject.isExpanded()) {
				image = new Image(
						x, 
						top + y - DIAGRAM_OBJECT_EXPANDED_ARROW_HEIGHT, 
						DIAGRAM_OBJECT_EXPANDED_ARROW_WIDTH, 
						DIAGRAM_OBJECT_EXPANDED_ARROW_HEIGHT, 
						"images/expanded.gif");
				setImageTitle(image, COLLAPSE_TITLE);
				canvas.add(image);
			}
			else {
				image = new Image(
						x, 
						top + y - DIAGRAM_OBJECT_COLLAPSED_ARROW_HEIGHT, 
						DIAGRAM_OBJECT_COLLAPSED_ARROW_WIDTH, 
						DIAGRAM_OBJECT_COLLAPSED_ARROW_HEIGHT, 
						"images/collapsed.gif");
				setImageTitle(image, EXPAND_TITLE);
				canvas.add(image);
			}
			image.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					diagramObject.toggleExpanded();
					view.drawDiagram();
				}
			});
		}
		
		rectangle = new Rectangle(x, top + y, width, getRectHeight());

		String myCaption = "";
		String myTooltipCaption = "";
		
		if (isDrawingSimpleStatement && (diagramObject instanceof Qdsel || diagramObject instanceof PlaceHolder)) {
			rectangle.setFillColor((diagramObject.isHilighted() && clickable) ? HILIGHT_COLOR : DiagramObject.DEFAULT_BACKCOLOR);
			if (diagramObject instanceof Qdsel)
				myCaption = myTooltipCaption = ((Qdsel)diagramObject).getIdentity();
			else {
				myCaption = ((PlaceHolder)diagramObject).getSimpleStatementFirstLine();
				myTooltipCaption = ((PlaceHolder)diagramObject).getSimpleStatementFullTooltipText();
			}
		}
		else {
			rectangle.setFillColor((diagramObject.isHilighted() && clickable) ? HILIGHT_COLOR : backColor);
			myCaption = myTooltipCaption = caption;
		}
		rectangle.setStrokeWidth(hasBorder | diagramObject.isHilighted()  ? 1 : 0);
		rectangle.setStrokeColor(BORDER_COLOR);

		canvas.add(rectangle);	
		if (isDrawingSimpleStatement) 
			drawText(canvas, x, top + y, myCaption, myTooltipCaption);
		else
			drawText(canvas, x, top + y, myCaption);
		
		if (clickable) {
			ClickHandler handler = DiagramObjectClickHandlerFactory.getClickHandlerFor(rectangle, view, diagramObject);
			if (handler != null) {
				rectangle.addClickHandler(handler);
				rectText.addClickHandler(handler);	
			}
		}
	}	
	
	public void drawText(DrawingArea canvas, int x, int y, String caption) {
		drawText(canvas, x, y, caption, caption);
	}
	
	public void drawText(DrawingArea canvas, int x, int y, String caption, String tooltipCaption) {
		int len = (isDrawingSimpleStatement) ? getMaxTextLengthSimpleStatement() : maxTextLength;
		String shortCaption = (caption.length() > len) ? chop(caption, len) : caption;
		if (getUserAgent().contains("msie"))
			rectText = new Text(x + width / 2, y + getRectHeight() / 2, shortCaption);
		else
			rectText = new Text(x + 5, y + getRectHeight() / 2, shortCaption);
		rectText.setFontFamily(FONT_NAME);		
		rectText.setFontSize(FONT_SIZE);
		rectText.setStrokeWidth(1);
		rectText.setFillOpacity(0.0);
		if (isDrawingSimpleStatement)
			rectText.setStrokeColor(diagramObject.isHilighted() ? "black" : DiagramObject.DEFAULT_FORECOLOR);
		else
			rectText.setStrokeColor(diagramObject.isHilighted() ? "black" : foreColor);
		canvas.add(rectText);
		rectText.setTitle(tooltipCaption);
	}

	@Override
	public void clear() {
		rectangle.removeFromParent();
		rectangle = null;
		rectText.removeFromParent();
		rectText = null;
	}
	
	public TextRectangle clone(AppController appController) {
		TextRectangle copy = new TextRectangle(
				this.view, null, DiagramObjectFactory.clone(appController, this.diagramObject), this.caption,
				this.foreColor, this.backColor, this.hasBorder);
		
		copy.roundRadius = this.roundRadius;
		copy.rectText = null;
		copy.width = this.width;
		copy.maxTextLength = this.maxTextLength;
		copy.topMargin = this.topMargin;
		copy.rectBottomMargin = this.rectBottomMargin;
		copy.isDrawingSimpleStatement = this.isDrawingSimpleStatement;
		
		return copy;
	}
	
	private void setImageTitle(Image image, String title){
		image.setTitle(title);
		image.getElement().setAttribute("alt", title);
	}
}
