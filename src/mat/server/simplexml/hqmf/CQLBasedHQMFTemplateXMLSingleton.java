package mat.server.simplexml.hqmf;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

public class CQLBasedHQMFTemplateXMLSingleton {
	
	private final static XmlProcessor templateXMLProcessor;
	
	static {
		String fileName = "CQLHQMFTemplates.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File templateFile = null;
		try {
			templateFile = new File(templateFileUrl.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		templateXMLProcessor = new XmlProcessor(templateFile);
	}
	
	public static XmlProcessor getTemplateXmlProcessor(){
		return templateXMLProcessor;
	}
	
}
