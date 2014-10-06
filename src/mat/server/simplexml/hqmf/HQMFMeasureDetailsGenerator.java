package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;
import mat.server.service.impl.XMLUtility;

public class HQMFMeasureDetailsGenerator implements Generator {
	
	/** The Constant conversionFileForHQMF_Header. */
	private static final String conversionFileForHQMF_Header = "xsl/new_measureDetails.xsl";
	
	@Override
	public String generate(MeasureExport me) {
		XMLUtility xmlUtility = new XMLUtility();
		String measureDetailsHQMF_XML = xmlUtility.applyXSL(me.getSimpleXML(),
				xmlUtility.getXMLResource(conversionFileForHQMF_Header));
		return measureDetailsHQMF_XML;
	}

}
