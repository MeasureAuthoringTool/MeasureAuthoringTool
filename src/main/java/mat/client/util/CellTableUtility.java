package mat.client.util;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import mat.shared.StringUtility;

public class CellTableUtility {
    /**
     * Gets the column tool tip.
     *
     * @param title the title
     * @return the column tool tip
     */
    public static SafeHtml getColumnToolTip(String title) {
        String htmlConstant = "<html>" + "<head> </head> <body><span  title=\" " + title + "\">" +
                title + "</span></body>" + "</html>";
        return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
    }

    /**
     * Gets the column tool tip.
     *
     * @param columnText the column text
     * @param title      the title
     * @return the column tool tip
     */
    public static SafeHtml getColumnToolTip(String columnText, String title) {
        String htmlConstant = "<html>" + "<head> </head> <body><span  title=\" " + StringUtility.escapeHtml(title) + "\">" + StringUtility.escapeHtml(columnText)
                + "</span></body>"
                + "</html>";
        return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
    }


    /**
     * Gets the name column tool tip.
     *
     * @param columnText the column text
     * @param title      the title
     * @return the name column tool tip
     */
    public static SafeHtml getNameColumnToolTip(String columnText, String title) {
        String htmlConstant = "<div id='container'>" +
                "<span id='div2' title=\" " + StringUtility.escapeHtml(title) + "\">" +
                StringUtility.escapeHtml(columnText) + "</span>" + "</div>";
        return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
    }


    /**
     * Gets the name column tool tip.
     *
     * @param columnText the column text
     * @param title      the title
     * @return the name column tool tip
     */
    public static SafeHtml getCodeDescriptorColumnToolTip(String columnText, String title, String suffix) {

        if (columnText.length() > 450) {
            if (suffix != null && !suffix.isEmpty()) {
                columnText = columnText.substring(0, 443);
                columnText.concat(" (" + suffix + ")");
            } else {
                columnText = columnText.substring(0, 449);
            }
        } else if (columnText.length() > 443) {
            if (suffix != null && !suffix.isEmpty()) {
                columnText = columnText.substring(0, 443);
                columnText.concat(" (" + suffix + ")");
            }
        } else {
            if (suffix != null && !suffix.isEmpty()) {
                columnText.concat(" (" + suffix + ")");
            }
        }
        String htmlConstant = "<div id='container'> " +
                "<span id='div3' title=\" " + StringUtility.escapeHtml(title) + "\">" +
                StringUtility.escapeHtml(columnText) + "</span>" + "</div>";


        return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
    }
}
