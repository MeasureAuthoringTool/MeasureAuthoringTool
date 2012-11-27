package mat.client.clause.view.shape;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public class DiagonalConnector extends Connector {
	public DiagonalConnector(DrawingArea canvas, Rectangle rect1, Rectangle rect2) {
		super(canvas, rect1, rect2);
	}
	
	public void draw(int top) {
		canvas.add(new Line(x1, top + y1, x2, y2));
	}
}
