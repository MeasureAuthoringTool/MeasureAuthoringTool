package mat.client.shared;

import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import mat.shared.StringUtility;


public class MatTextCell extends TextInputCell {
	private String tooltip;
    public MatTextCell() {}
    
	public MatTextCell(String tooltip) {
		super();
		this.tooltip = tooltip;
	}
	
    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        Object key = context.getKey();
        ViewData viewData = getViewData(key);
        if (viewData != null && viewData.getCurrentValue().equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        String s = (viewData != null) ? viewData.getCurrentValue() : value;
        if (!StringUtility.isEmptyOrNull(s)) {
    		sb.appendHtmlConstant("<input type=\"text\" value=\"" + s + "\" title=\"" + s + "\"></input>");
        } else {
        	if(tooltip != null) {
        		sb.appendHtmlConstant("<input type=\"text\" title=\"" + tooltip + "\"></input>");
        	} else {
        		sb.appendHtmlConstant("<input type=\"text\" title=\"\"></input>");
        	}
            
        }
    }


}