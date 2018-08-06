package mat.server.util;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NodeList;

/**
 * The Class CQLUtil.
 */
public class CQLValidationUtil {

	/** The Constant xPath. */
	static final javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLValidationUtil.class);



	
	/**
	 * Checks if is duplicate identifier name.
	 *
	 * @param identifierName
	 *            the identifier name
	 * @param id
	 *            the measure id
	 * @return true, if is duplicate identifier name
	 */
	public static boolean isDuplicateIdentifierName(String identifierName, String xml) {

		if (xml != null) {

			XmlProcessor processor = new XmlProcessor(xml);
			String XPATH_CQLLOOKUP_IDENTIFIER_NAME = "//cqlLookUp//node()[translate(@name, "
					+ "'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + identifierName.toUpperCase()
					+ "']";
			try {
				NodeList nodeList = processor.findNodeList(processor.getOriginalDoc(), XPATH_CQLLOOKUP_IDENTIFIER_NAME);

				if ((nodeList != null) && (nodeList.getLength() > 0)) {
					return true;
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}

		return false;
	}
}

