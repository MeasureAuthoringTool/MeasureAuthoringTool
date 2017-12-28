package mat.server;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.clause.cqlworkspace.model.StrataDataModel;
import mat.client.clause.cqlworkspace.model.StratificationsObject;
import mat.client.population.service.PopulationService;
import mat.model.populations.CQLAggFunction;
import mat.model.populations.CQLDefinition;
import mat.model.populations.CQLFunction;
import mat.model.populations.Clause;
import mat.model.populations.Population;
import mat.server.service.MeasurePackageService;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;

public class PopulationServiceImpl extends SpringRemoteServiceServlet implements PopulationService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}

	@Override
	public SaveUpdateCQLResult savePopulations(String measureId, PopulationsObject populationsObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = getService().getMeasureXmlForMeasure(measureId);
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());

		String parentNode = "";
		if(isMeasureObservations(populationsObject.getDisplayName())) {
			parentNode = "measureObservations";
		}else {
			parentNode = "populations";
		}
		String m = marshall(populationsObject);
		//System.out.println("POP: " + m);

		String newXml = xmlProcessor.replaceNode(m, populationsObject.getPopulationName(), parentNode);
		newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());

		model.setXml(newXml);

		//System.out.println("NEW XML: " + newXml);
		//TODO:uncomment the below line after unit testing
		getService().saveMeasureXml(model);

		if (StringUtils.isNotBlank(model.getXml())) {
			result.setXml(model.getXml());
			result.setSetId(measureId);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}


	private String marshall(PopulationsObject populationsObject) {

		StringWriter sw = new StringWriter();
		
		try {	

			Population pop = new Population(populationsObject.getDisplayName());
			
			for(PopulationClauseObject p : populationsObject.getPopulationClauseObjectList()) {

				Clause clause = new Clause(p.getType(), p.getUuid(), p.getDisplayName());
				
				if(StringUtils.isNotBlank(p.getCqlExpressionUUID())) {
					
					boolean isMeasureObservation =  isMeasureObservations(populationsObject.getDisplayName());			
					
					if(isMeasureObservation) {
						
						if(StringUtils.isNotBlank(p.getAggFunctionName())){
							
							CQLAggFunction cqlAggFunction = new CQLAggFunction(p.getAggFunctionName());
							cqlAggFunction.setCqlFunction(new CQLFunction(p.getCqlExpressionDisplayName(), p.getCqlExpressionUUID()));							
							
							clause.setCqlAggFunction(cqlAggFunction);
							
						} else {							
							clause.setCqlFunction(new CQLFunction(p.getCqlExpressionDisplayName(), p.getCqlExpressionUUID()));	
						}
						
					} else {
						//Other Populations					
						clause.setCqldefinition(new CQLDefinition(p.getCqlExpressionDisplayName(), p.getCqlExpressionUUID()));	
					}	
				}
				
				
				pop.add(clause);
			}

			JAXBContext jaxbContext = JAXBContext.newInstance(Population.class);			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();			
			QName qName = new QName(populationsObject.getPopulationName());

			JAXBElement<Population> root = new JAXBElement<>(qName, Population.class, pop);

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//jaxbMarshaller.marshal(root, System.out);
			jaxbMarshaller.marshal(root, sw);

		}catch (Exception e) {
			System.out.println("Error:" +e.getMessage());
		}
		return sw.toString();
	}


	private String marshalStrata(List<StratificationsObject> stratificationObjectList) {
		StringWriter sw = new StringWriter();

		try {
			
			for(StratificationsObject s : stratificationObjectList) {
				//Stratification stratification = new Stratification(s.getDisplayName(), s.ge);
				System.out.println("Do Something here");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return sw.toString();

	}

	@Override
	public SaveUpdateCQLResult saveStratifications(String measureId, StrataDataModel strataDataModel) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel model = getService().getMeasureXmlForMeasure(measureId);
		XmlProcessor xmlProcessor = new XmlProcessor(model.getXml());

		String parentNode = "populations";
		String m = marshalStrata(strataDataModel.getStratificationObjectList());
		System.out.println("POP: " + m);

		String newXml = xmlProcessor.replaceNode(m, "strata", parentNode);
		newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());

		model.setXml(newXml);

		System.out.println("NEW XML: " + newXml);
		//TODO:uncomment the below line after unit testing
		//getService().saveMeasureXml(model);

		if (StringUtils.isNotBlank(model.getXml())) {
			result.setXml(model.getXml());
			result.setSetId(measureId);
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	private boolean isMeasureObservations(String name) {
		return name.equals("Measure Observations");
	}

}
