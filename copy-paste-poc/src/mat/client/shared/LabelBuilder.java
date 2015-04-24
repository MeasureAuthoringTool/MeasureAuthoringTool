package mat.client.shared;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class LabelBuilder.
 */
public class LabelBuilder {
	
	/** Builds the invisible label.
	 * @param id - String id for Label.
	 * @param labelStr - Label Text.
	 * @return Widget. */
	public static Widget buildInvisibleLabel(String id, String labelStr) {
		//widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label id= '" + id + "' for='" + labelStr + "'>" + labelStr + "</label>");
		label.getElement().setAttribute("id", id);
		label.setStyleName("invisible");
		return label;
	}
	/**
	 * Builds the invisible label.
	 * 
	 * @param widget
	 *            the widget
	 * @param labelStr
	 *            the label str
	 * @return the widget
	 */
	public static Widget buildInvisibleLabel(Widget widget, String labelStr) {
		//widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label id= '" +labelStr+ "' for='" + labelStr + "'>"+labelStr+"</label>");
		label.getElement().setAttribute("id", labelStr);
		label.setStyleName("invisible");
		return label;
	}
	
	/**
	 * Builds the invisible label with content.
	 * 
	 * @param widget
	 *            the widget
	 * @param id
	 *            the id
	 * @param content
	 *            the content
	 * @return the widget
	 */
	public static Widget buildInvisibleLabelWithContent(Widget widget, String id, String content) {
		//widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label id= '" +id+ "' for='" + id + "'>"+content+"</label>");
		label.getElement().setAttribute("id", id);
		label.setStyleName("invisible");
		return label;
	}
	
	/**
	 * Builds the label.
	 * 
	 * @param labelStr
	 *            the label str
	 * @param forVal
	 *            the for val
	 * @return the widget
	 */
	public static Widget buildLabel(String labelStr, String forVal) {
		HTML label = new HTML("<label id= '" +labelStr+ "' for='" + forVal + "'>" + labelStr + "</label>");
		label.getElement().setAttribute("id", labelStr);
		return label;
	}
	
	/** Builds the label.
	 * 
	 * @param widget the widget
	 * @param labelStr the label str
	 * @return the widget */
	public static Widget buildLabel(Widget widget, String labelStr) {
		// widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label id= '" + labelStr + "' for='" + labelStr + "'>" + labelStr + "</label>");
		label.getElement().setAttribute("id", labelStr);
		return label;
	}
	/**
	 * Builds the label with embedded link.
	 * 
	 * @param widget
	 *            the widget
	 * @param labelStr
	 *            the label str
	 * @param embeddedString
	 *            the embedded string
	 * @return the widget
	 */
	public static Widget buildLabelWithEmbeddedLink(Widget widget, String labelStr,String embeddedString){
		//widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<a class='invisible' name='"+embeddedString+"'></a><label for='" + labelStr + "'>" + labelStr + "</label>");
		label.getElement().setAttribute("id", labelStr);
		return label;
	}
	
	/**
	 * Builds the plain label with anchor.
	 * 
	 * @param labelStr
	 *            the label str
	 * @param embeddedString
	 *            the embedded string
	 * @return the string
	 */
	public static String buildPlainLabelWithAnchor(String labelStr,String embeddedString){
		String Html = "<a class='invisible' name='"+embeddedString+"'></a><label>"+labelStr+"</label>";
		return Html;
	}
	
	/**
	 * Builds the required label.
	 * 
	 * @param widget
	 *            the widget
	 * @param labelStr
	 *            the label str
	 * @return the widget
	 */
	public static Widget buildRequiredLabel(Widget widget, String labelStr) {
		//	widget.getElement().setAttribute("id", labelStr);
		HTML label = new HTML("<label id= '" +labelStr+ "' for='" + labelStr + "'>" + labelStr + RequiredIndicator.get() + "</label>" );
		label.getElement().setAttribute("id", labelStr);
		return label;
	}
	
	/**
	 * Sets the invisible label.
	 * 
	 * @param widget
	 *            the widget
	 * @param label
	 *            the label
	 * @param labelStr
	 *            the label str
	 * @return the widget
	 */
	public static Widget setInvisibleLabel(Widget widget, HTML label, String labelStr) {
		//	widget.getElement().setAttribute("id", labelStr);
		label.setHTML("<label id= '" +labelStr+ "' for='" + labelStr + "'>"+labelStr+"</label>");
		label.getElement().setAttribute("id", labelStr);
		label.setStyleName("invisible");
		return label;
	}
	
}
