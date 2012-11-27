package mat.client.clause.view.shape;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public class SquareConnector extends Connector {
	public SquareConnector(DrawingArea canvas, Rectangle rect1, Rectangle rect2) {
		super(canvas, rect1, rect2);
	}

	public void draw(int top) {
		if (getUserAgent().contains("msie"))
			drawIE(top);
		else
			drawFirefox(top);
	}

	private void drawFirefox(int top) {
		if (y1 == y2)
			canvas.add(new Line(x1, top + y1, x2, top + y2));
		else {
			int midX = x1 + ((x2 - x1) / 2);
			canvas.add(new Line(x1, top + y1, midX, top + y1));
			canvas.add(new Line(midX, top + y1, midX, top + y2));
			canvas.add(new Line(midX, top + y2, x2, top + y2));
		}
	}

	private void drawIE(int top) {
		if (y2 == 0) {
			canvas.add(new Line(x1, top + y1, x2, y2));
		}
		else {
			int midX = x2 / 2;
			canvas.add(new Line(x1, top + y1, midX, 0));
			canvas.add(new Line(x1 + midX, top + y1, 0, y2));
			canvas.add(new Line(x1 + midX, top + y1 + y2, x2 - midX, 0));
		}
	}	
}
