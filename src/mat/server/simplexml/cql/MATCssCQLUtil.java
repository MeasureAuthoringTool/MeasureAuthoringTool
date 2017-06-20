package mat.server.simplexml.cql;

public class MATCssCQLUtil {
	
	private final static String MAT_CSS = 

			"<style type=\"text/css\">\r\n" + 
			"body {\r\n" + 
			"	color: rgb(26, 26, 26);\r\n" + 
			"	background-color: rgb(255,255,255);\r\n" + 
			"	font-family: Verdana, Tahoma, sans-serif;\r\n" + 
			"	font-size: 11px;\r\n" + 
			"    overflow-y: auto;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"h1 {\r\n" + 
			"	font-size: 12pt;\r\n" + 
			"	font-weight: bold;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"h2 {\r\n" + 
			"	font-size: 11pt;\r\n" + 
			"	font-weight: bold;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"h3 {\r\n" + 
			"	font-size: 10pt;\r\n" + 
			"	font-weight: bold;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"h4 {\r\n" + 
			"	font-size: 8pt;\r\n" + 
			"	font-weight: bold;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"tr {\r\n" + 
			"	background-color: rgb(224,224,224);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"td {\r\n" + 
			"	padding: 0.1cm 0.2cm;\r\n" + 
			"	vertical-align: top;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"a {\r\n" + 
			"	/*color: rgb(0, 0, 255);\r\n" + 
			"	background-color: rgb(255,255,255);*/\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"table {"+ " \n " + 
			"line-height: 10pt;"+ " \n " +
			"width: 80%;"+ " \n " +
			"font-size: 11px;"+ " \n " +
			"}\r\n" + 
			"\r\n" + 
			"/*div {\r\n" + 
			"	width: 80%;\r\n" + 
			"}*/\r\n" + 
			"\r\n" + 
			".list-unstyled {\r\n" + 
			"    list-style-type: none;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".list-unstyled li {\r\n" + 
			"    padding-top: 10px;\r\n" + 
			"    padding-bottom: 10px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".list-header {\r\n" + 
			"    font-size: 12px;\r\n" + 
			"    color: rgb(0, 0, 238);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".list-header:hover {\r\n" + 
			"    text-decoration: underline;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".code {\r\n" + 
			"    font-family: Consolas,Menlo,Monaco,Lucida Console,Liberation Mono,DejaVu Sans Mono,Bitstream Vera Sans Mono,Courier New,monospace,sans-serif;\r\n" + 
			"    color:black;\r\n" + 
			"    background-color:rgb(250,250,250);\r\n" + 
			//"    margin-top: 1em;\r\n" + 
			//"    margin-bottom: 1em;\r\n" + 
			"    padding: 10px;\r\n" + 
			//"    line-height: 1.5em;\r\n" + 
			"    font-size: 12px;\r\n" + 
			//"    overflow: auto;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".p-l-10 {\r\n" + 
			"    padding-left: 10px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".section {\r\n" + 
			"    font-size: 10pt;\r\n" + 
			"    margin-top: 1em;\r\n" + 
			"    margin-bottom: 1em;\r\n" + 
			"    margin-right: 0.5em;\r\n" + 
			"    font-weight: 700;\r\n" + 
			"    display: inline-block;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".cql-keyword-new {\r\n" + 
			"    color: rgb(0, 0, 255);\r\n" + 
			"    font-weight: 700;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".cql-class, .cql-object {\r\n" + 
			"    color: rgb(25, 127, 157);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".cql_keyword {\r\n" + 
			"    color: rgb(127, 0, 85);\r\n" + 
			"    font-weight: 700;\r\n" + 
			"}\r\n" +
			".cql_function {\r\n" + 
			"    color: rgb(127, 0, 85);\r\n" + 
			"    font-weight: 700;\r\n" + 
			"}\r\n" +
			".cql_attribute {\r\n" + 
			"    color: rgb(127, 0, 85);\r\n" + 
			"    font-weight: 700;\r\n" + 
			"}\r\n" +
			".cql_string {\r\n" + 
			"    color: rgb(42, 0, 255);\r\n" + 
			"}\r\n" +
			".cql_identifier .cql_function {\r\n" + 
			"    color: rgb(60, 76, 114);\r\n" + 
			"}\r\n" + 
			"hr.style13 {\r\n" + 
			"	height: 10px;\r\n" + 
			"	border: 0;\r\n" + 
			"	box-shadow: 0 10px 10px -10px #8c8b8b inset;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
//			".treeview {\r\n" + 
//			"  -webkit-user-select: none;\r\n" + 
//			"  -moz-user-select: none;\r\n" + 
//			"  -ms-user-select: none;\r\n" + 
//			"  -o-user-select: none;\r\n" + 
//			"  user-select: none;\r\n" + 
//			"}\r\n" + 
			"\r\n" + 
			".treeview:hover input ~ label:before,\r\n" + 
			".treeview.hover input ~ label:before {\r\n" + 
			"  opacity: 1.0;\r\n" + 
			"  -webkit-transition-duration: 0.5s;\r\n" + 
			"  -moz-transition-duration: 0.5s;\r\n" + 
			"  -ms-transition-duration: 0.5s;\r\n" + 
			"  -o-transition-duration: 0.5s;\r\n" + 
			"  transition-duration: 0.5s;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".treeview ul {\r\n" + 
			"  -webkit-transition-duration: 1s;\r\n" + 
			"  -moz-transition-duration: 1s;\r\n" + 
			"  -ms-transition-duration: 1s;\r\n" + 
			"  -o-transition-duration: 1s;\r\n" + 
			"  transition-duration: 1s;\r\n" + 
			"  -webkit-transition-timing-function: ease;\r\n" + 
			"  -moz-transition-timing-function: ease;\r\n" + 
			"  -ms-transition-timing-function: ease;\r\n" + 
			"  -o-transition-timing-function: ease;\r\n" + 
			"  transition-timing-function: ease;\r\n" + 
			"  list-style: none;\r\n" + 
			"  padding-left: 1em;\r\n" + 
			"}\r\n" + 
			".treeview ul li span {\r\n" + 
			"  -webkit-transition-property: color;\r\n" + 
			"  -moz-transition-property: color;\r\n" + 
			"  -ms-transition-property: color;\r\n" + 
			"  -o-transition-property: color;\r\n" + 
			"  transition-property: color;\r\n" + 
			"  -webkit-transition-duration: 1s;\r\n" + 
			"  -moz-transition-duration: 1s;\r\n" + 
			"  -ms-transition-duration: 1s;\r\n" + 
			"  -o-transition-duration: 1s;\r\n" + 
			"  transition-duration: 1s;\r\n" + 
			"  \r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".treeview input {\r\n" + 
			"  display: none;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".treeview input[type=checkbox]:checked ~ ul {\r\n" + 
			"  display: none;  \r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".treeview input[type=checkbox] ~ ul {\r\n" + 
			"  max-height: 100%;\r\n" + 
			"  max-width: 90%;\r\n" + 
			"  opacity: 1;  \r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".treeview input ~ label {\r\n" + 
			"  cursor: pointer;\r\n" + 
			"}\r\n" + 
			".treeview input ~ label:before {\r\n" + 
			"  content: '';\r\n" + 
			"  width: 0;\r\n" + 
			"  height: 0;\r\n" + 
			"  position: absolute;\r\n" + 
			"  margin-left: -1em;\r\n" + 
			"  margin-top: 0.25em;\r\n" + 
			"  border-width: 4px;\r\n" + 
			"  border-style: solid;\r\n" + 
			"  border-top-color: transparent;\r\n" + 
			"  border-right-color: rgb(0, 0, 238);\r\n" + 
			"  border-bottom-color: rgb(0, 0, 238);\r\n" + 
			"  border-left-color: transparent;\r\n" + 
			"  opacity: 0.0;\r\n" + 
			"  -webkit-transition-property: opacity;\r\n" + 
			"  -moz-transition-property: opacity;\r\n" + 
			"  -ms-transition-property: opacity;\r\n" + 
			"  -o-transition-property: opacity;\r\n" + 
			"  transition-property: opacity;\r\n" + 
			"  -webkit-transition-duration: 1s;\r\n" + 
			"  -moz-transition-duration: 1s;\r\n" + 
			"  -ms-transition-duration: 1s;\r\n" + 
			"  -o-transition-duration: 1s;\r\n" + 
			"  transition-duration: 1s;\r\n" + 
			"}\r\n" + 
			".treeview input:checked ~ label:before {\r\n" + 
			"  margin-left: -0.8em;\r\n" + 
			"  border-width: 5px;\r\n" + 
			"  border-top-color: transparent;\r\n" + 
			"  border-right-color: transparent;\r\n" + 
			"  border-bottom-color: transparent;\r\n" + 
			"  border-left-color: rgb(0, 0, 238);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			".h1center {\r\n" + 
			"	font-size: 12pt;\r\n" + 
			"	font-weight: bold;\r\n" + 
			"	text-align: center;\r\n" + 
			"	width: 80%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".header_table{\r\n" + 
			"	border: 1pt inset rgb(0,0,0);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".narr_table {\r\n" + 
			"	width: 100%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".narr_tr {\r\n" + 
			"	background-color: rgb(225,225,225);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"pre {\r\n" + 
			" overflow: auto; /* Use horizontal scroller if needed; for Firefox 2, not needed in Firefox 3 */\r\n" + 
			" white-space: pre-wrap; /* css-3 */\r\n" + 
			" white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */\r\n" + 
			" white-space: -pre-wrap; /* Opera 4-6 */\r\n" + 
			" white-space: -o-pre-wrap; /* Opera 7 */\r\n" + 
			" word-wrap: break-word; /* Internet Explorer 5.5+ */\r\n" + 
			" font-family: Verdana, Tahoma, sans-serif;\r\n" + 
			" font-size: 11px;\r\n" + 
			" text-align:left;\r\n" + 
			" margin: 0px 0px 0px 0px;\r\n" + 
			" padding:0px 0px 0px 0px;\r\n" + 
			"}\r\n" + 
			".narr_th {\r\n" + 
			"	background-color: rgb(201,201,201);\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".td_label{\r\n" + 
			"	font-weight: bold;\r\n" + 
			"	color: white;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".hll { background-color: #ffffcc }\r\n" + 
			".c { color: #408080; font-style: italic } /* Comment */\r\n" + 
			".err { border: 1px solid #FF0000 } /* Error */\r\n" + 
			".k { color: #008000; font-weight: bold } /* Keyword */\r\n" + 
			".o { color: #666666 } /* Operator */\r\n" + 
			".cm { color: #408080; font-style: italic } /* Comment.Multiline */\r\n" + 
			".cp { color: #BC7A00 } /* Comment.Preproc */\r\n" + 
			".c1 { color: #408080; font-style: italic } /* Comment.Single */\r\n" + 
			".cs { color: #408080; font-style: italic } /* Comment.Special */\r\n" + 
			".gd { color: #A00000 } /* Generic.Deleted */\r\n" + 
			".ge { font-style: italic } /* Generic.Emph */\r\n" + 
			".gr { color: #FF0000 } /* Generic.Error */\r\n" + 
			".gh { color: #000080; font-weight: bold } /* Generic.Heading */\r\n" + 
			".gi { color: #00A000 } /* Generic.Inserted */\r\n" + 
			".go { color: #888888 } /* Generic.Output */\r\n" + 
			".gp { color: #000080; font-weight: bold } /* Generic.Prompt */\r\n" + 
			".gs { font-weight: bold } /* Generic.Strong */\r\n" + 
			".gu { color: #800080; font-weight: bold } /* Generic.Subheading */\r\n" + 
			".gt { color: #0044DD } /* Generic.Traceback */\r\n" + 
			".kc { color: #008000; font-weight: bold } /* Keyword.Constant */\r\n" + 
			".kd { color: #008000; font-weight: bold } /* Keyword.Declaration */\r\n" + 
			".kn { color: #008000; font-weight: bold } /* Keyword.Namespace */\r\n" + 
			".kp { color: #008000 } /* Keyword.Pseudo */\r\n" + 
			".kr { color: #008000; font-weight: bold } /* Keyword.Reserved */\r\n" + 
			".kt { color: #B00040 } /* Keyword.Type */\r\n" + 
			".m { color: #666666 } /* Literal.Number */\r\n" + 
			".s { color: #BA2121 } /* Literal.String */\r\n" + 
			".na { color: #7D9029 } /* Name.Attribute */\r\n" + 
			".nb { color: #008000 } /* Name.Builtin */\r\n" + 
			".nc { color: #0000FF; font-weight: bold } /* Name.Class */\r\n" + 
			".no { color: #880000 } /* Name.Constant */\r\n" + 
			".nd { color: #AA22FF } /* Name.Decorator */\r\n" + 
			".ni { color: #999999; font-weight: bold } /* Name.Entity */\r\n" + 
			".ne { color: #D2413A; font-weight: bold } /* Name.Exception */\r\n" + 
			".nf { color: #0000FF } /* Name.Function */\r\n" + 
			".nl { color: #A0A000 } /* Name.Label */\r\n" + 
			".nn { color: #0000FF; font-weight: bold } /* Name.Namespace */\r\n" + 
			".nt { color: #008000; font-weight: bold } /* Name.Tag */\r\n" + 
			".nv { color: #19177C } /* Name.Variable */\r\n" + 
			".ow { color: #AA22FF; font-weight: bold } /* Operator.Word */\r\n" + 
			".w { color: #bbbbbb } /* Text.Whitespace */\r\n" + 
			".mf { color: #666666 } /* Literal.Number.Float */\r\n" + 
			".mh { color: #666666 } /* Literal.Number.Hex */\r\n" + 
			".mi { color: #666666 } /* Literal.Number.Integer */\r\n" + 
			".mo { color: #666666 } /* Literal.Number.Oct */\r\n" + 
			".sb { color: #BA2121 } /* Literal.String.Backtick */\r\n" + 
			".sc { color: #BA2121 } /* Literal.String.Char */\r\n" + 
			".sd { color: #BA2121; font-style: italic } /* Literal.String.Doc */\r\n" + 
			".s2 { color: #BA2121 } /* Literal.String.Double */\r\n" + 
			".se { color: #BB6622; font-weight: bold } /* Literal.String.Escape */\r\n" + 
			".sh { color: #BA2121 } /* Literal.String.Heredoc */\r\n" + 
			".si { color: #BB6688; font-weight: bold } /* Literal.String.Interpol */\r\n" + 
			".sx { color: #008000 } /* Literal.String.Other */\r\n" + 
			".sr { color: #BB6688 } /* Literal.String.Regex */\r\n" + 
			".s1 { color: #BA2121 } /* Literal.String.Single */\r\n" + 
			".ss { color: #19177C } /* Literal.String.Symbol */\r\n" + 
			".bp { color: #008000 } /* Name.Builtin.Pseudo */\r\n" + 
			".vc { color: #19177C } /* Name.Variable.Class */\r\n" + 
			".vg { color: #19177C } /* Name.Variable.Global */\r\n" + 
			".vi { color: #19177C } /* Name.Variable.Instance */\r\n" + 
			".il { color: #666666 } /* Literal.Number.Integer.Long */\r\n" + 
			"\r\n" + 
			"#nn-text\r\n" + 
			"{\r\n" + 
			"    text-align: center;\r\n" + 
			"    text-align: center;\r\n" + 
			"    color:lightgrey;\r\n" + 
			"    font-size:40px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			".cql-definition-body {\r\n" + 
			"	width:950px;\r\n" + 
			"	display: block;\r\n" + 
			"	word-wrap: break-word;\r\n" + 
			//"	text-indent: 20px;\r\n" + 
			"}" + 
			"li {"
			+ "  padding-left: 15px;"
			+ "}" +
			"	</style>";

		public static String getCSS() {
			return MAT_CSS;		
		}

	}

