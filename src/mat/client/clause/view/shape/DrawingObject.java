package mat.client.clause.view.shape;

import mat.client.clause.view.Rect;
import mat.client.shared.MatContext;

public class DrawingObject {
	
	private int rectWidth = 4;
	private int rectHeight = 1;
	public static final double SIMPLE_STATEMENT_WIDTH_MULTIPLIER = 1.25;
	
	protected static final int RECT_BOTTOM_MARGIN = 20;
	protected static final int LEFT_MARGIN = 5;
	protected static final int TOP_MARGIN = 0;
	protected static final String BORDER_COLOR = "#A0A0A0";
	protected static final String HILIGHT_COLOR = "#FFFFCC";
	
	protected mat.client.clause.view.Rect rect;
	
	public Rect getRect() {
		return rect;
	}
	
	// this is a JavaScript native method. Do not remove the special characters!
	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
}-*/;
	
	public int getRectWidth(){
		return getFactor()*rectWidth;
	}
	
	public int getRectHeight(){
		return getFactor()*rectHeight;
	}
	public void incrementFactor(){
		MatContext.get().getZoomFactorService().incrementFactor();
	}
	public void decrementFactor(){
		MatContext.get().getZoomFactorService().decrementFactor();
	}
	
	protected int getRectWidthSimpleStatement(){
		return (int)(getRectWidth() * SIMPLE_STATEMENT_WIDTH_MULTIPLIER);
	}
	protected int getRectVerticalSpacing(){
		return (int)(getRectWidth() * 1.25 + 0.5);
	}
	protected int getRectVerticalSpacingSimpleStatement(){
		return (int)(getRectWidthSimpleStatement() * 1.25 + 0.5);
	}
	protected int getRectHorizontalSpacing(){
		return (int)(getRectHeight() * 1.0 + 0.5) + RECT_BOTTOM_MARGIN;
	}
	private int getFactor(){
		return MatContext.get().getZoomFactorService().getFactor();
	}
}
