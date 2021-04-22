package mat.server.hqmf;

import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class QDMTemplateProcessorFactory {
	
	public static XmlProcessor getTemplateProcessor(double qdmVersion) {
		String fileName;

		if(qdmVersion == 5.6) {
			fileName = "templates/hqmf/qdm_v5_6_datatype_templates.xml";
		} else {
			throw new IllegalArgumentException("Unsupported QDM version: " + qdmVersion);
		}
		
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File templateFile = null;
		try {
			templateFile = new File(templateFileUrl.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return new XmlProcessor(templateFile);
	}
}
