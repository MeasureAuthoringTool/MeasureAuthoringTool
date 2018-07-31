package mat.server.hqmf.qdm;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

public class TemplateXMLSingleton {
	
	private final static XmlProcessor templateXMLProcessor;
	
	static {
		String fileName = "templates.xml";
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
