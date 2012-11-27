package mat.client.shared;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class LabelBuilder {
	public static Widget buildLabel(Widget widget, String labelStr) {
		widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label for='" + labelStr + "'>" + labelStr + "</label>");
		return label;
	}
	public static Widget buildRequiredLabel(Widget widget, String labelStr) {
		widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label for='" + labelStr + "'>" + labelStr + RequiredIndicator.get() + "</label>" );
		return label;
	}
	public static Widget buildLabel(String labelStr, String forVal) {
		HTML label = new HTML("<label for='" + forVal + "'>" + labelStr + "</label>");
		return label;
	}
	public static Widget buildInvisibleLabel(Widget widget, String labelStr) {
		widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label for='" + labelStr + "'>"+labelStr+"</label>");
		label.setStyleName("invisible");
		return label;
	}
	public static Widget setInvisibleLabel(Widget widget, HTML label, String labelStr) {
		widget.getElement().setAttribute("id", labelStr);
		label.setHTML("<label for='" + labelStr + "'>"+labelStr+"</label>");
		label.setStyleName("invisible");
		return label;
	}
	
	public static Widget buildLabelWithEmbeddedLink(Widget widget, String labelStr,String embeddedString){
		widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<a class='invisible' name='"+embeddedString+"'></a><label for='" + labelStr + "'>" + labelStr + "</label>");
		return label;
	}
	
	public static String buildPlainLabelWithAnchor(String labelStr,String embeddedString){
		String Html = "<a class='invisible' name='"+embeddedString+"'></a><label>"+labelStr+"</label>";
		return Html;
	}

}
