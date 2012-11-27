package org.ifmc.mat.server.export;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ifmc.mat.client.shared.MetaDataConstants;
import org.ifmc.mat.dao.MetadataDAO;
import org.ifmc.mat.dao.PropertyOperator;
import org.ifmc.mat.dao.StewardDAO;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.dao.search.CriteriaQuery;
import org.ifmc.mat.dao.search.SearchCriteria;
import org.ifmc.mat.model.MeasureSteward;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.model.clause.Metadata;
import org.ifmc.mat.server.util.UuidUtility;
import org.ifmc.mat.shared.ConstantMessages;
import org.ifmc.mat.simplexml.model.Author;
import org.ifmc.mat.simplexml.model.Authors;
import org.ifmc.mat.simplexml.model.Copyright;
import org.ifmc.mat.simplexml.model.Custodian;
import org.ifmc.mat.simplexml.model.Definition;
import org.ifmc.mat.simplexml.model.Definitions;
import org.ifmc.mat.simplexml.model.DenominatorDescription;
import org.ifmc.mat.simplexml.model.DenominatorExceptionsDescription;
import org.ifmc.mat.simplexml.model.DenominatorExclusionsDescription;
import org.ifmc.mat.simplexml.model.Description;
import org.ifmc.mat.simplexml.model.Disclaimer;
import org.ifmc.mat.simplexml.model.Emeasureid;
import org.ifmc.mat.simplexml.model.FinalizedDate;
import org.ifmc.mat.simplexml.model.Guidance;
import org.ifmc.mat.simplexml.model.Headers;
import org.ifmc.mat.simplexml.model.ImprovementNotations;
import org.ifmc.mat.simplexml.model.InitialPatientPopDescription;
import org.ifmc.mat.simplexml.model.MeasureObservationsDescription;
import org.ifmc.mat.simplexml.model.MeasurePopulationDescription;
import org.ifmc.mat.simplexml.model.NqfId;
import org.ifmc.mat.simplexml.model.NumeratorDescription;
import org.ifmc.mat.simplexml.model.NumeratorExclusionsDescription;
import org.ifmc.mat.simplexml.model.Period;
import org.ifmc.mat.simplexml.model.QualityMeasureSet;
import org.ifmc.mat.simplexml.model.RateAggregation;
import org.ifmc.mat.simplexml.model.Rationales;
import org.ifmc.mat.simplexml.model.Recommendation;
import org.ifmc.mat.simplexml.model.Recommendations;
import org.ifmc.mat.simplexml.model.Reference;
import org.ifmc.mat.simplexml.model.RiskAdjustment;
import org.ifmc.mat.simplexml.model.Score;
import org.ifmc.mat.simplexml.model.Scores;
import org.ifmc.mat.simplexml.model.Setid;
import org.ifmc.mat.simplexml.model.Startdate;
import org.ifmc.mat.simplexml.model.Status;
import org.ifmc.mat.simplexml.model.Stopdate;
import org.ifmc.mat.simplexml.model.StratificationDescription;
import org.ifmc.mat.simplexml.model.SupplementalData;
import org.ifmc.mat.simplexml.model.Title;
import org.ifmc.mat.simplexml.model.TransmissionFormat;
import org.ifmc.mat.simplexml.model.Type;
import org.ifmc.mat.simplexml.model.Types;
import org.ifmc.mat.simplexml.model.Uuid;
import org.ifmc.mat.simplexml.model.Verifier;
import org.ifmc.mat.simplexml.model.Verifiers;
import org.springframework.beans.factory.annotation.Autowired;

public class HeaderInfoGenerator {
	
	@Autowired 
	MetadataDAO metaDataDAO;
	
	@Autowired 
	StewardDAO stewardDAO;

	@Autowired
	MeasureDAO measureDAO;
	
	@SuppressWarnings("unused")
	public Headers getAllHeaders(String measureId) {
		Headers header = new Headers();
	
		/*US501*/
		Uuid uuid = new Uuid();
		String uuidStr = UuidUtility.idToUuid(measureId);
		uuid.setTtext(uuidStr);
		header.setUuid(uuid);
		
		Title title = new Title();
		Emeasureid emeasureid = new Emeasureid();
		
		Period period = new Period();
		period.setUuid(generateRandomUuid());
		header.setPeriod(period);

		
		List<Metadata> metadataList = metaDataDAO.getMeasureDetails(measureId);
		List<Author> authorList = new ArrayList<Author>();
		List<Type> measureTypeList  = new ArrayList<Type>(); 
		List<Reference> referenceList = new ArrayList<Reference>();
		List<Verifier> verifierList = new ArrayList<Verifier>();
		List<Score> scoreList = new ArrayList<Score>();
		
		for(Metadata mt: metadataList){
		
			/*if(mt.getName().equalsIgnoreCase("Name")){
				title.setTtext(mt.getValue());
			}*/
			
			//US 413 and clean up. Handle Steward Other and Use an single object reference.
			String key = mt.getName();
			String value = mt.getValue();
			
			if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_DEVELOPER)){
				Author a = new Author();
				a.setTtext(value);

				if (value != null) {
			    	//Find the OID for this steward. If there is one, use it as its id on the simple XML.
					//If there is none, use a randomly generated value.
					String oid = retrieveStewardOID(value);

					if (oid != null) {
						a.setId(oid);
					} else {
						a.setId(generateRandomUuid());						
					}
				} else {
					a.setId(generateRandomUuid());
				}
				
				authorList.add(a);
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_TYPE)){
				Type measureType = new Type();
				if(mt.getValue().equalsIgnoreCase("Composite")){
					measureType.setTtext(value);
					measureType.setId("COMPOSITE");
				}else if(mt.getValue().equalsIgnoreCase("Cost/Resource Use")){
					measureType.setTtext(value);
					measureType.setId("COSTRESOURCEUSE");
				}else if(mt.getValue().equalsIgnoreCase("Efficiency")){
					measureType.setTtext(value);
					measureType.setId("EFFICIENCY");
				}else if(mt.getValue().equalsIgnoreCase("Outcome")){
					measureType.setTtext(value);
					measureType.setId("OUTCOME");
				}else if(mt.getValue().equalsIgnoreCase("Structure")){
					measureType.setTtext(value);
					measureType.setId("STRUCTURE");
				}else if(mt.getValue().equalsIgnoreCase("Patient Engagement/Experience")){
					measureType.setTtext(value);
					measureType.setId("PATENGEXP");
				}else if(mt.getValue().equalsIgnoreCase("Process")){
					measureType.setTtext(value);
					measureType.setId("PROCESS");
				}
				measureTypeList.add(measureType);
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_SET)){
				QualityMeasureSet qualityMeasureSet = new QualityMeasureSet();
				qualityMeasureSet.setUuid(generateRandomUuid());
				qualityMeasureSet.setTtext(value);
				header.setQualityMeasureSet(qualityMeasureSet);
			
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASUREMENT_FROM_PERIOD)){
			    Startdate startDate = new Startdate();
			    startDate.setUuid(generateRandomUuid());
				header.getPeriod().setStartdate(startDate);
				String replacedandFormattedString  = replaceALLXWithZeros(value);
				header.getPeriod().getStartdate().setTtext(replacedandFormattedString);
				
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASUREMENT_TO_PERIOD)){
				Stopdate stopdate = new Stopdate();
				stopdate.setUuid(generateRandomUuid());
				header.getPeriod().setStopdate(stopdate);
				String replacedandFormattedString  = replaceALLXWithZeros(value);
				header.getPeriod().getStopdate().setTtext(replacedandFormattedString);
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_STEWARD) && !value.equalsIgnoreCase("Other")){ //US 413. Handle Measure Steward Other.
			    header.setCustodian(new Custodian());
			    header.getCustodian().setTtext(value);

			    if(value != null) {
			    
			    	//Find the oid for this steward. If there is one, use it as its id on the simple XML.
					//If there is none, use a randomly generated value.
			    	String oid = retrieveStewardOID(value);

			    	if ( oid != null && !oid.trim().isEmpty()) {
			    		header.getCustodian().setId(oid);
			    	} else {
				    	header.getCustodian().setId(generateRandomUuid());				    		
			    	}
				} else {
			    	header.getCustodian().setId(generateRandomUuid());
			    }
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_STEWARD_OTHER)){ //US 413 and code clean up. Handle Measure Steward Other. Use an single object reference.
			    header.setCustodian(new Custodian());
			    header.getCustodian().setTtext(value);

			    if(value != null) {
			    
			    	//Find the oid for this steward. If there is one, use it as its id on the simple XML.
					//If there is none, use a randomly generated value.
			    	String oid = retrieveStewardOID(value);

			    	if ( oid != null && !oid.trim().isEmpty()) {
			    		header.getCustodian().setId(oid);
			    	} else {
				    	header.getCustodian().setId(generateRandomUuid());				    		
			    	}
				} else {
			    	header.getCustodian().setId(generateRandomUuid());
			    }
			}else if(key.equalsIgnoreCase(MetaDataConstants.ENDORSE_BY_NQF)){
				header.setVerifiers(new Verifiers());
				Verifier verifier = new Verifier();
			    if(mt.getValue().equalsIgnoreCase("true")){
			    	verifier.setTtext("National Quality Forum");
			    	verifier.setId("2.16.840.1.113883.3.560");
				    verifierList.add(verifier);
				}else{
			    	verifier.setTtext("None");
			    	verifierList.add(verifier);
			    }
			    header.getVerifiers().setVerifiers(verifierList);
			}else if(key.equalsIgnoreCase(MetaDataConstants.DESCRIPTION)){
				header.setDescription(new Description());
				header.getDescription().setTtext(value);
				
				
			}else if(key.equalsIgnoreCase(MetaDataConstants.DISCLAIMER)){
				header.setDisclaimer(new Disclaimer());
				header.getDisclaimer().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.RISK_ADJUSTMENT)){
				header.setRiskAdjustment(new RiskAdjustment());
				header.getRiskAdjustment().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.RATE_AGGREGATION)){
				header.setRateAggregation(new RateAggregation());
				header.getRateAggregation().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.INITIAL_PATIENT_POP)){
				header.setInitialPatientPopDescription(new InitialPatientPopDescription());
				header.getInitialPatientPopDescription().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.DENOM)){
				header.setDenominatorDescription(new DenominatorDescription());
				header.getDenominatorDescription().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.DENOM_EXCL)){
				header.setDenominatorExclusionsDescription(new DenominatorExclusionsDescription());
				header.getDenominatorExclusionsDescription().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.NUM)){
				header.setNumeratorDescription(new NumeratorDescription());
				header.getNumeratorDescription().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.NUM_EXCL)){
				header.setNumeratorExclusionsDescription(new NumeratorExclusionsDescription());
				header.getNumeratorExclusionsDescription().setTtext(value);
			} else if(key.equalsIgnoreCase(MetaDataConstants.DENOM_EXEP)){
				header.setDenominatorExceptionsDescription(new DenominatorExceptionsDescription());
				header.getDenominatorExceptionsDescription().setTtext(value);
			   
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_POP)){
				header.setMeasurePopulationDescription(new MeasurePopulationDescription());
				header.getMeasurePopulationDescription().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_OBS)){
				header.setMeasureObservationsDescription(new MeasureObservationsDescription());
				header.getMeasureObservationsDescription().setTtext(value);
				
				
			}else if(key.equalsIgnoreCase(MetaDataConstants.COPYRIGHT)){
				header.setCopyright(new Copyright());
				header.getCopyright().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.CLINICAL_RECOM_STATE)){
				header.setRecommendations(new Recommendations());
				Recommendation recommendation = new Recommendation();
				recommendation.setTtext(value);
				header.getRecommendations().setRecommendation(recommendation);
			}else if(key.equalsIgnoreCase(MetaDataConstants.DEFENITION)){
				header.setDefinitions(new Definitions());
				Definition definition = new Definition();
				definition.setTtext(value);
				header.getDefinitions().setDefinition(definition);
			}else if(key.equalsIgnoreCase(MetaDataConstants.GUIDANCE)){
				header.setGuidance(new Guidance());
				header.getGuidance().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.TRANSMISSION_FORMAT)){
				header.setTransmissionFormat(new TransmissionFormat());
				header.getTransmissionFormat().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.RATIONALE)){
				header.setRationales(new Rationales());
				header.getRationales().getRationale().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.IMPROVEMENT_NOTATION)){
				header.setImprovementNotation(new ImprovementNotations());
				header.getImprovementNotation().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.STRATIFICATION)){
				header.setStratification(new StratificationDescription());
				header.getStratification().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS)){
				header.setSupplementalData(new SupplementalData());
				header.getSupplementalData().setTtext(value);
			}else if(key.equalsIgnoreCase(MetaDataConstants.REFERENCES)){
				Reference reference = new Reference();
				reference.setTtext(value);
				referenceList.add(reference);
				header.getReferences().setReferences(referenceList);
			}else if(key.equalsIgnoreCase(MetaDataConstants.MEASURE_STATUS)){
				Status status = new Status();
				status.setTtext(value);
				header.setStatus(status);
			}else if(key.equalsIgnoreCase(MetaDataConstants.NQF_NUMBER)){
				header.setNqfid(new NqfId());
				header.getNqfid().setExtension(value);
			}
		}
		
		
		//add measure details to headers
		Measure measure = measureDAO.find(measureId);
		if(measure != null){
			
			title.setTtext(measure.getDescription());
			emeasureid.setTtext(measure.geteMeasureId()+"");
			
			String scoring = measure.getMeasureScoring();
			if(scoring != null){
				Score score = new Score();
				if(scoring.equalsIgnoreCase(ConstantMessages.CONTINUOUS_VARIABLE_SCORING)){
					score.setTtext(scoring);
					score.setId("CONTVAR");
				}else if(scoring.equalsIgnoreCase(ConstantMessages.PROPORTION_SCORING)){
					score.setTtext(scoring);
					score.setId("PROPOR");
				}else if(scoring.equalsIgnoreCase(ConstantMessages.RATIO_SCORING)){
					score.setTtext(scoring);
					score.setId("RATIO");
				}
				scoreList.add(score);
			}
			/*US501*/
			/*if(measure.getVersion() != null){
				String versionStr = measure.getVersion();
				Version version = new Version();
				version.setTtext(versionStr);
				header.setVersion(version);
			}*/
			/*US524*/
			if(measure.getMeasureSet().getId() != null){
				String eMeasureIdentifier = measure.getMeasureSet().getId();
				Setid setId = new Setid();
				setId.setTtext(eMeasureIdentifier);
				header.setSetid(setId);
			}
			/*US528*/
			if(measure.getFinalizedDate() != null){
				FinalizedDate finalizedDate = new FinalizedDate();
				finalizedDate.setValue(measure.getFinalizedDate());
				header.setFinalizedDate(finalizedDate);
			}
		}		
		
		header.setAuthors(new Authors());
		header.getAuthors().setAuthors(authorList);
		header.setTypes(new Types());
		header.getTypes().setTypes(measureTypeList);

		header.setTitle(title);
		header.setScores(new Scores());
		header.getScores().setScores(scoreList);
		
		header.setEmeasureid(emeasureid);
		return header;
	}

	/* Find the oid for this steward. */
	private String retrieveStewardOID(String stewardName) {
		String oid = null;
		SearchCriteria criteria = new SearchCriteria("orgName", 
				stewardName.trim(), PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<MeasureSteward> stewards = stewardDAO.find(query);

	    if (stewards != null && !stewards.isEmpty()) {
	    	MeasureSteward stw = stewards.get(0);
	    	 oid = stw.getOrgOid();
	    }

	    return oid;
	}

	private String generateRandomUuid(){
		return  UUID.randomUUID().toString();
	}
	
	private String replaceALLXWithZeros(String dateString){
		String formattedDate = "";
		if(dateString.length() > 6){
			String yearString = dateString.substring(6);
			if(yearString.contains("X")){
				yearString = "0000";
				formattedDate = customDateFormatter(dateString, yearString);
			}else if(yearString.contains("x")){
				yearString = "0000";
				formattedDate = customDateFormatter(dateString, yearString);
			}else{
				return dateFormatter(dateString);
			}
		}
		return formattedDate;
		
	}
	
	private String customDateFormatter(String dateString, String yearString){
		 String monthText = dateString.substring(0, 2);
	     String dayText  = dateString.substring(3, 5);
	     //String yearText  = dateString.substring(6);
	     String formattedDate = yearString+monthText+dayText;
	     return formattedDate;
	}
	
	private String dateFormatter(String dateString){
		String formattedDate  ="";
		try{
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
				Date dateToBeConverted = dateFormatter.parse(dateString);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //please notice the capital M
				formattedDate = formatter.format(dateToBeConverted);
		}catch(Exception e){}
		return formattedDate;
	}
	
}
