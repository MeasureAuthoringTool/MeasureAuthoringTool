package mat.server.simplexml;

import java.io.IOException;
import java.io.StringReader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import freemarker.template.TemplateException;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.server.humanreadable.cql.HumanReadableModel;
import mat.server.humanreadable.cql.NewHumanReadableGenerator;
import mat.server.simplexml.cql.CQLHumanReadableGenerator;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;

@Component
public class HumanReadableGenerator {
	
	@Autowired NewHumanReadableGenerator humanReadableGenerator; 
	
	public String generateHTMLForPopulationOrSubtree(String measureId,
			String subXML, String measureXML, CQLLibraryDAO cqlLibraryDAO) {
		
		XmlProcessor subXMLProcessor = new XmlProcessor(subXML);
		String html = "";
		
		if(subXMLProcessor.getOriginalDoc().getDocumentElement().hasChildNodes()){			
			String firstNodeName = subXMLProcessor.getOriginalDoc().getDocumentElement().getFirstChild().getNodeName();
			System.out.println("firstNodeName:"+firstNodeName);
			
			if("cqldefinition".equals(firstNodeName) || "cqlfunction".equals(firstNodeName) || "cqlaggfunction".equals(firstNodeName)){
				html = CQLHumanReadableGenerator.generateHTMLForPopulation(measureId, subXMLProcessor, measureXML, cqlLibraryDAO);
			}else{
				html = HQMFHumanReadableGenerator.generateHTMLForPopulationOrSubtree(measureId,subXML,measureXML);
			}
		}else{
			return "<html></html>";
		}
		
		return html;
	}
	
	public String generateHTMLForMeasure(String measureId,String simpleXmlStr, String measureReleaseVersion, CQLLibraryDAO cqlLibraryDAO){
		
		String html = "";
		System.out.println("Generating human readable for ver:"+measureReleaseVersion);
		if(MatContext.get().isCQLMeasure(measureReleaseVersion)){
			try {
				Mapping mapping = new Mapping(); 
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("SimpleXMLHumanReadableModelMapping.xml"));
				Unmarshaller unmarshaller = new Unmarshaller(mapping);
				unmarshaller.setClass(HumanReadableModel.class);
				unmarshaller.setWhitespacePreserve(true);
				HumanReadableModel model = (HumanReadableModel) unmarshaller.unmarshal(new InputSource(new StringReader(simpleXmlStr)));
				html = humanReadableGenerator.generate(model);
			} catch (IOException | TemplateException | MappingException | MarshalException | ValidationException e) {
				e.printStackTrace();
			}
		} else{
			html = HQMFHumanReadableGenerator.generateHTMLForMeasure(measureId,simpleXmlStr);
		}
		
		return html;
	}
	
}
