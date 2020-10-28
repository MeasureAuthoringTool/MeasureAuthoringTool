package mat.server.humanreadable;

public class MATCssUtil {

	private final static String MAT_CSS = 

		"<style type=\"text/css\">" + " \n " +

		"body {" + " \n " +
		"color: rgb(0,0,0);"+ " \n " +
		"background-color: rgb(255,255,255);"+ " \n " +
		"font-family: Verdana, Tahoma, sans-serif;"+ " \n " +
		"font-size: 11px;"+ " \n " +
		"}"+ " \n " +

		"a {"+ " \n " +
		"color: rgb(0, 0, 255);"+ " \n " +
		"background-color: rgb(255,255,255);"+ " \n " +
		"}"+ " \n " +

		"h1 {"+ " \n " +
		"font-size: 12pt;"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"}"+ " \n " +

		"h2 {"+ " \n " +
		"font-size: 11pt;"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"}"+ " \n " +

		"h3 {"+ " \n " +
		"font-size: 10pt;"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"}"+ " \n " +

		"h4 {"+ " \n " +
		"font-size: 8pt;"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"}"+ " \n " +

		"div {"+ " \n " +
		"width: 80%;"+ " \n " +
		"}"+ " \n " +

		"table {"+ " \n " +
		"line-height: 10pt;"+ " \n " +
		"width: 80%;"+ " \n " +
		"font-size: 11px;"+ " \n " +
		"}"+ " \n " +

		".inner_table {"+ " \n " +
		"width: 100%;"+ " \n " +
		"}"+ " \n " +
		
		".inner_table td {" + "\n" +
		"border-color:#ffffff;" + "\n" +
		"border-style:solid;" + "\n" +
		"border-width:1px;" + "\n" +
		"border-top: 0;" + "\n" +
		"border-left: 0;" + "\n" +
		"border-right: 0;" + "\n" +
		"}" + "\n" +
		
		".inner_table tbody > tr:last-child > td  {" + "\n" +
		"border-bottom-style:none;" + "\n" +
		"}" + "\n" +
		
		".ver {"+ " \n " +
		"padding: 0px 5px" +
		"}"+ " \n " +
		
		"tr {"+ " \n " +
		"background-color: rgb(224,224,224);"+ " \n " +
		"}"+ " \n " +

		"td {"+ " \n " +
		"padding: 0.1cm 0.2cm;"+ " \n " +
		"vertical-align: top;"+ " \n " +
		"}"+ " \n " +

		".h1center {"+ " \n " +
		"font-size: 12pt;"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"text-align: center;"+ " \n " +
		"width: 80%;}"+ " \n " +

		".header_table{"+ " \n " +
		"border: 1pt inset rgb(0,0,0);}"+ " \n " +

		".narr_table {"+ " \n " +
		"width: 100%;"+ " \n " +
		"}"+ " \n " +

		".narr_tr {"+ " \n " +
		"background-color: rgb(225,225,225);"+ " \n " +
		"}"+ " \n " +
		"pre {"+ " \n " +
		"overflow: auto; /* Use horizontal scroller if needed; for Firefox 2, not needed in Firefox 3 */"+ " \n " +
		"white-space: pre-wrap; /* css-3 */"+ " \n " +
		"white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */"+ " \n " +
		"white-space: -pre-wrap; /* Opera 4-6 */"+ " \n " +
		"white-space: -o-pre-wrap; /* Opera 7 */"+ " \n " +
		"word-wrap: break-word; /* Internet Explorer 5.5+ */"+ " \n " +
		"font-family: Verdana, Tahoma, sans-serif;"+ " \n " +
		"font-size: 11px;"+ " \n " +
		"text-align:left;"+ " \n " +
		"margin: 0px 0px 0px 0px;"+ " \n " +
		"padding:0px 0px 0px 0px;"+ " \n " +
		"}"+ " \n " +
		".narr_th {"+ " \n " +
		"background-color: rgb(201,201,201);"+ " \n " +
		"}"+ " \n " +

		".td_label{"+ " \n " +
		"font-weight: bold;"+ " \n " +
		"color: white;"+ " \n " +
		"}</style>";


	public static String getCSS() {
		return MAT_CSS;		
	}

}
