package mat.client.cqlworkspace.attributes;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class LightBoxRightPanelView implements LightBoxRightPanelDisplay {

    private Widget rootWidget;

    public LightBoxRightPanelView(String width, String height) {
        HTML html = new HTML(
                "<div><h4>Adverse Event</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"adverse_event_code\">code</label></h5>\n" +
                        "<select id=\"adverse_event_code\" name=\"adverse_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"adverse_event_type\">type</label></h5>\n" +
                        "<select id=\"adverse_event_type\" name=\"adverse_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
                        +
                        "<div><h4>Allergy/Intolerance</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"alergy_event_code\">code</label></h5>\n" +
                        "<select id=\"alergy_event_code\" name=\"alergy_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"alergy_event_type\">type</label></h5>\n" +
                        "<select id=\"alergy_event_type\" name=\"alergy_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
                        +
                        "<div><h4>Assessment, Not Ordered</h4>\n" +
                        "<div>\n" +
                        "<h5><label for=\"assessment_event_code\">code</label></h5>\n" +
                        "<select id=\"assessment_event_code\" name=\"assessment_event_code\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "<div>\n" +
                        "<h5><label for=\"assessment_event_type\">type</label></h5>\n" +
                        "<select id=\"assessment_event_type\" name=\"assessment_event_type\">\n" +
                        "<option value=\"First Choice\">First Choice</option>\n" +
                        "<option value=\"Second Choice\">Second Choice</option>\n" +
                        "<option value=\"Third Choice\">Third Choice</option>\n" +
                        "<option value=\"Fourth Choice\">Fourth Choice</option>\n" +
                        "</select>" +
                        "</div>\n" +
                        "\n" +
                        "</div>"
        );
        html.setWidth("100%");
        html.setHeight("100%");

        ScrollPanel scroller = new ScrollPanel(html);
        scroller.setSize(width, height);

        rootWidget = scroller;
    }

    @Override
    public Widget asWidget() {
        return rootWidget;
    }
}
