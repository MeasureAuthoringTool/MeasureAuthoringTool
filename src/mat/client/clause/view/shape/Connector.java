package mat.client.clause.view.shape;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

public abstract class Connector extends DrawingObject {
	protected DrawingArea canvas;
	protected int x1, y1, x2, y2;
	
	public Connector(DrawingArea canvas, Rectangle r1, Rectangle r2) {
		this.canvas = canvas;
		
		x1 = r1.getX() + r1.getWidth();
		y1 = r1.getY() + r1.getHeight() / 2;
		x2 = r2.getX();
		y2 = r2.getY() + r2.getHeight() / 2;
		if (getUserAgent().contains("msie")) {
			x2 -= x1;
			y2 -= y1;
		}
	}
	
	public void draw() {	
		draw(0);
	}
	public abstract void draw(int top);
}
