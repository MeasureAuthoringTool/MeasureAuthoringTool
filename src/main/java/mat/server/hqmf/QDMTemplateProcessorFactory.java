package mat.server.hqmf;

import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class QDMTemplateProcessorFactory {
	
	public static XmlProcessor getTemplateProcessor(double qdmVersion) {
		String fileName = ""; 

		if(qdmVersion == 5.3) {
			fileName = "templates/hqmf/qdm_v5_3_datatype_templates.xml";
		}
		
		else if(qdmVersion == 5.4) {
			fileName = "templates/hqmf/qdm_v5_4_datatype_templates.xml";
		} 
		
		else if(qdmVersion == 5.5) {
			fileName = "templates/hqmf/qdm_v5_5_datatype_templates.xml";
		} 
		
		else {
			fileName = "templates/hqmf/qdm_v4_x_datatype_templates.xml";
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
