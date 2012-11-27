package org.ifmc.mat.client.clause.view;

import org.ifmc.mat.client.shared.MatContext;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ZoomCanvasDelegate {
	private final String resetTitle = "Reset diagram size to default.";
	private final String minusTitle = "Decrease diagram size.";
	private final String plusTitle = "Increase diagram size.";
	private DiagramView view;
	
	public ZoomCanvasDelegate(DiagramView view){
		this.view = view;
	}
	
	public void drawReset(int top) {
		draw(top, 15, 5, "images/action_refresh_blue.png", 16, 16, resetTitle, 
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						MatContext.get().getZoomFactorService().resetFactor();
						view.drawDiagram();
					}
				});
	}
	
	public void drawPlus(int top) {
		draw(top, 30, 5, "images/zoom_in.png", 16, 16, plusTitle, 
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						MatContext.get().getZoomFactorService().incrementFactor();
						view.drawDiagram();
					}
				});
	}
	
	public void drawMinus(int top){
		draw(top, 0, 5, "images/zoom_out.png", 16, 16, minusTitle, 
			new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					MatContext.get().getZoomFactorService().decrementFactor();
					view.drawDiagram();
				}
			});
	}
	
	public void draw(int top, int x, int y, String imagePath, int imageX, int imageY, String imageTitle, ClickHandler handler) {
		DrawingArea canvas = view.getCanvas();
		
		Image image;
		image = new Image(
				x, 
				top + y - 5, 
				imageX, 
				imageY, 
				imagePath);
		canvas.add(image);
		image.setTitle(imageTitle);
		image.getElement().setAttribute("alt", imageTitle);
		image.addClickHandler(handler);
	}
}
