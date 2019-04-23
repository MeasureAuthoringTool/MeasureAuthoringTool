package mat.client.clause.clauseworkspace.view;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

public interface Template extends SafeHtmlTemplates {

		@Template("<table tabindex =\"0\"width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}</td></tr></table>")
		SafeHtml menuTable(String name, String shortCut);
		
		@Template("<table tabindex =\"0\"width=\"100%\"><tr><td>{0}</td></tr></table>")
		SafeHtml menuTable(String name);

		@Template("<table tabindex =\"0\"width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}"
				+ "<img src=\"{2}\"/></td></tr></table>")
		SafeHtml menuTableWithIcon(String name, String shortCut, SafeUri imageSource);
}
