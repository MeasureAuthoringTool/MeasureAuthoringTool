package mat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.CQLService;
import mat.client.shared.QDMInputValidator;
import mat.dao.clause.CQLDAO;
import mat.model.CQLValueSetTransferObject;
import mat.model.DataType;
import mat.model.MatValueSet;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.CQLData;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.model.cql.parser.CQLDefinitionModelObject;
import mat.model.cql.parser.CQLFileObject;
import mat.model.cql.parser.CQLFunctionModelObject;
import mat.model.cql.parser.CQLParameterModelObject;
import mat.server.cqlparser.CQLTemplateXML;
import mat.server.cqlparser.MATCQLParser;
import mat.server.service.MeasurePackageService;
import mat.server.util.CQLUtil;
import mat.server.util.CQLUtil.CQLArtifactHolder;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLErrors;
import mat.shared.CQLModelValidator;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLServiceImpl.
 */
public class CQLServiceImpl implements CQLService {
	
	/** The cql dao. */
	@Autowired
	private CQLDAO cqlDAO;
	
	/** The context. */
	@Autowired
	private ApplicationContext context;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLServiceImpl.class);
	
	/** The cql supplemental definition XML string. */
	private String cqlSupplementalDefinitionXMLString =
			
			"<supplementalDefinitions>"
			
			+ "<definition context=\"Patient\" name=\"SDE Ethnicity\" "
				+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\"> "
				+ "<logic>[\"Patient Characteristic Ethnicity\": \"Ethnicity\"]</logic> "
			+ "</definition>"
			
			+ "<definition context=\"Patient\" name=\"SDE Payer\" "
            		+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\">  "
            		+ "<logic>[\"Patient Characteristic Payer\": \"Payer\"]</logic>"
            + "</definition>"
            		
            + "<definition context=\"Patient\" name=\"SDE Race\" "
            		+ " supplDataElement=\"true\" popDefinition=\"false\"  id=\"999\"> "
            		+ "<logic>[\"Patient Characteristic Race\": \"Race\"]</logic>"
            + "</definition>"
            		
            + "<definition context=\"Patient\" name=\"SDE Sex\" "
            		+ " supplDataElement=\"true\"  popDefinition=\"false\" id=\"999\">  "
            		+ "<logic>[\"Patient Characteristic Sex\": \"ONC Administrative Sex\"]</logic>"
            + "</definition>"
            		
            + "</supplementalDefinitions>"		;
	
	
	/** The cql default code system XML string. */
	private String cqlDefaultCodeSystemXMLString=
			
			"<codeSystems>"
					
		 	+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" "
			+ "codeSystemVersion=\"2.46\" id=\"777\" "
		 	+ "/>"
			
			+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMEDCT\" "
			+ "codeSystemVersion=\"2016-03\" id=\"777\" "
			+ "/>"
			
			+ "</codeSystems>"; 
	
	/** The cql default code system XML string. */
	private String cqlDefaultCodesXMLString=
			
			"<codes>"
					
		 	+ "<code codeName=\"Birthdate\" codeOID=\"21112-8\" codeSystemName=\"LOINC\" "
		 	+ "displayName=\"Birth date\" "
			+ "codeSystemVersion=\"2.46\" id=\"777\" "
		 	+ "/>"
			
			+ "<code codeName=\"Dead\" codeOID=\"419099009\" codeSystemName=\"SNOMEDCT\" "
			+ "displayName=\"Dead\" "
			+ "codeSystemVersion=\"2016-03\" id=\"777\" "
			+ "/>"
			
			+ "</codes>"; 
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#saveCQL(mat.model.cql.CQLModel)
	 */
	@Override
	public CQLData getCQL(String measureId) {
		
		CQLData cqlData = cqlDAO.findByID(measureId);
		return cqlData;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.CQLService#parseCQL(java.lang.String)
	 */
	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		
		CQLModel cqlModel = new CQLModel();
		/*cqlLexer lexer = new cqlLexer(new ANTLRInputStream(cqlBuilder));
		System.out.println(cqlBuilder);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		cqlParser parser = new cqlParser(tokens);
		MATCQLParser cqlParser = new MATCQLParser();
		CQLErrorListener cqlErrorListener = new CQLErrorListener();
		MATCQLListener cqlListener = new MATCQLListener(cqlParser);
		cqlListener.setCqlModel(cqlModel);
		cqlListener.setParser(cqlListener);
		cqlListener.setLexer(lexer);
		parser.addParseListener(cqlListener);
		cqlListener.setTokens(tokens);
		parser.setBuildParseTree(true);
		
		ParserRuleContext tree = parser.logic();
		parser.notifyErrorListeners("");
		
		System.out.println(parser.getNumberOfSyntaxErrors());
		System.out.println(cqlErrorListener.getErrors());
				
		cqlModel = cqlListener.getCqlModel();*/
				
		return cqlModel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyCQLGeneralInfo(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(
			String currentMeasureId, String context) {
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				currentMeasureId);
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			String XPATH_EXPRESSION_CQLLOOKUP_CONTEXT = "/measure/cqlLookUp/cqlContext";
			try {
				Node nodeCQLContext = processor.findNode(
						processor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_CONTEXT);
				if (nodeCQLContext != null) {
					nodeCQLContext.setTextContent(context);
					xmlModel.setXml(processor.transform(processor
							.getOriginalDoc()));
					getService().saveMeasureXml(xmlModel);
				}
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			result.setSuccess(true);
			result.getCqlModel().setContext(context);
		} else {
			result.setSuccess(false);
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyFunctions(java.lang
	 * .String, mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions,
	 * java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyFunctions(String measureId,
			CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		CQLModelValidator validator = new CQLModelValidator();
		boolean isDuplicate = false;
		String XPATH_EXPRESSION_FUNCTIONS = "/measure/cqlLookUp/functions";
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				if (!toBeModifiedObj.getFunctionName().equalsIgnoreCase(
						currentObj.getFunctionName())) {
					
					isDuplicate = validator.validateForSpecialChar(currentObj
							.getFunctionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(
							currentObj.getFunctionName(), measureId);
				}
				
				if (!isDuplicate) {
					
					// validation for argument name to check if it is not a
					// keyword.
					/*result = checkIfKeywordForFuncArguments(result, currentObj);
					isDuplicate = result.isSuccess();
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
						result.setFunction(currentObj);
						return result;
					}*/
					
					String cqlString = createFunctionsXML(currentObj);
					
					logger.debug(" CQLServiceImpl: saveAndModifyFunctions Start :  ");
					
					String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "/measure/cqlLookUp//function[@id='"
							+ toBeModifiedObj.getId() + "']";
					try {
						Node nodeFunction = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);
						
						if (nodeFunction != null) {
							processor.removeFromParent(nodeFunction);
							processor.appendNode(cqlString, "function",
									XPATH_EXPRESSION_FUNCTIONS);
							xmlModel.setXml(processor.transform(processor
									.getOriginalDoc()));
							getService().saveMeasureXml(xmlModel);
							String name = "define function" + " \"" + currentObj.getFunctionName() + "\""; 
							parseCQLDefForErrors(result, measureId, name, currentObj.getFunctionLogic());
							wrapper = modfiyCQLFunctionList(toBeModifiedObj,
									currentObj, functionsList);
							result.setSuccess(true);
							result.setFunction(currentObj);
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
						
					} catch (SAXException e) {
						result.setSuccess(false);
						e.printStackTrace();
					} catch (IOException e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
					logger.debug(" CQLServiceImpl: saveAndModifyFunctions End :  ");
					
				} else {
					
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
				
			} else {
				
				currentObj.setId(UUID.randomUUID().toString());
				isDuplicate = validator.validateForSpecialChar(currentObj
						.getFunctionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NO_SPECIAL_CHAR);
					return result;
				}
				isDuplicate = isDuplicateIdentifierName(
						currentObj.getFunctionName(), measureId);
				if (!isDuplicate) {
					
					// validation for argument name to check if it is not a
					// keyword.
					/*result = checkIfKeywordForFuncArguments(result, currentObj);
					isDuplicate = result.isSuccess();
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
						result.setFunction(currentObj);
						return result;
					}*/
					String cqlString = createFunctionsXML(currentObj);
					
					try {
						Node nodeFunctions = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_FUNCTIONS);
						if (nodeFunctions != null) {
							try {
								processor.appendNode(cqlString, "function",
										XPATH_EXPRESSION_FUNCTIONS);
								processor.setOriginalXml(processor
										.transform(processor.getOriginalDoc()));
								xmlModel.setXml(processor.getOriginalXml());
								getService().saveMeasureXml(xmlModel);
								String name = "define function" + " \"" + currentObj.getFunctionName() + "\""; 
								parseCQLDefForErrors(result, measureId, name, currentObj.getFunctionLogic());
								functionsList.add(currentObj);
								wrapper.setCqlFunctionsList(functionsList);
								result.setSuccess(true);
								result.setFunction(currentObj);
							} catch (SAXException e) {
								result.setSuccess(false);
								e.printStackTrace();
							} catch (IOException e) {
								result.setSuccess(false);
								e.printStackTrace();
							}
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
				} else {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlFunctionsList().size() > 0)) {
			result.getCqlModel().setCqlFunctions(
					sortFunctionssList(wrapper.getCqlFunctionsList()));
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyParameters(java.lang
	 * .String, mat.model.cql.CQLParameter, mat.model.cql.CQLParameter,
	 * java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyParameters(String measureId,
			CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		if(currentObj.isReadOnly()){
			return null;
		}
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		CQLModelValidator validtor = new CQLModelValidator();
		boolean isDuplicate = false;
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				if (!toBeModifiedObj.getParameterName().equalsIgnoreCase(
						currentObj.getParameterName())) {
					
					isDuplicate = validtor.validateForSpecialChar(currentObj
							.getParameterName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(
							currentObj.getParameterName(), measureId);
				}
				
				if (!isDuplicate) {
					
					logger.debug(" CQLServiceImpl: saveAndModifyParameters Start :  ");
					
					String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "/measure/cqlLookUp//parameter[@id='"
							+ toBeModifiedObj.getId() + "']";
					try {
						Node nodeParameter = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);
						
						if (nodeParameter != null) {
							nodeParameter
							.getAttributes()
							.getNamedItem("name")
							.setNodeValue(currentObj.getParameterName());
							
							Node logicNode = nodeParameter.getFirstChild();
							logicNode.setTextContent(currentObj
									.getParameterLogic());
							xmlModel.setXml(processor.transform(processor
									.getOriginalDoc()));
							getService().saveMeasureXml(xmlModel);
							String name = "parameter" + " \"" + currentObj.getParameterName() + "\""; 
							parseCQLDefForErrors(result, measureId, name, currentObj.getParameterLogic());
							wrapper = modfiyCQLParameterList(toBeModifiedObj,
									currentObj, parameterList);
							result.setSuccess(true);
							result.setParameter(currentObj);
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
						
					}
					logger.debug(" CQLServiceImpl: saveAndModifyParameters End :  ");
					
				} else {
					
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
				
			} else {
				
				currentObj.setId(UUID.randomUUID().toString());
				
				isDuplicate = validtor.validateForSpecialChar(currentObj
						.getParameterName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NO_SPECIAL_CHAR);
					return result;
				}
				isDuplicate = isDuplicateIdentifierName(
						currentObj.getParameterName(), measureId);
				
				if (!isDuplicate) {
					
					String cqlString = createParametersXML(currentObj);
					String XPATH_EXPRESSION_PARAMETERS = "/measure/cqlLookUp/parameters";
					
					try {
						Node nodeParameters = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_PARAMETERS);
						if (nodeParameters != null) {
							try {
								processor.appendNode(cqlString, "parameter",
										XPATH_EXPRESSION_PARAMETERS);
								processor.setOriginalXml(processor
										.transform(processor.getOriginalDoc()));
								xmlModel.setXml(processor.getOriginalXml());
								getService().saveMeasureXml(xmlModel);
								String name = "parameter" + " \"" + currentObj.getParameterName() + "\""; 
								
								parseCQLDefForErrors(result, measureId, name, currentObj.getParameterLogic());
								parameterList.add(currentObj);
								wrapper.setCqlParameterList(parameterList);
								result.setSuccess(true);
								result.setParameter(currentObj);
							} catch (SAXException e) {
								result.setSuccess(false);
								e.printStackTrace();
							} catch (IOException e) {
								result.setSuccess(false);
								e.printStackTrace();
							}
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
				} else {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlParameterList().size() > 0)) {
			result.getCqlModel().setCqlParameters(
					sortParametersList(wrapper.getCqlParameterList()));
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyDefinitions(java.lang
	 * .String, mat.model.cql.CQLDefinition, mat.model.cql.CQLDefinition,
	 * java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyDefinitions(String measureId,
			CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		CQLModelValidator validator= new CQLModelValidator();
		boolean isDuplicate = false;
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				// if the modified Name and current Name are not same
				if (!toBeModifiedObj.getDefinitionName().equalsIgnoreCase(
						currentObj.getDefinitionName())) {
					
					isDuplicate = validator.validateForSpecialChar(currentObj
							.getDefinitionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(
							currentObj.getDefinitionName(), measureId);
				}
				
				if (!isDuplicate) {
					
					logger.debug(" CQLServiceImpl: updateSubTreeLookUp Start :  ");
					String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "/measure/cqlLookUp//definition[@id='"
							+ toBeModifiedObj.getId() + "']";
					try {
						Node nodeDefinition = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);
						if (nodeDefinition != null) {
							nodeDefinition.getAttributes()
							.getNamedItem("context")
							.setNodeValue(currentObj.getContext());
							nodeDefinition
							.getAttributes()
							.getNamedItem("name")
							.setNodeValue(
									currentObj.getDefinitionName());
							Node logicNode = nodeDefinition.getFirstChild();
							logicNode.setTextContent(currentObj
									.getDefinitionLogic());
							//to update Definition Name on RiskAdjusment Variable Section
							updateRiskAdjustmentVariables(processor, toBeModifiedObj, currentObj);
							xmlModel.setXml(processor.transform(processor
									.getOriginalDoc()));
							getService().saveMeasureXml(xmlModel);
							String name = "define" + " \"" + currentObj.getDefinitionName() + "\""; 
							parseCQLDefForErrors(result, measureId,name, currentObj.getDefinitionLogic());
							wrapper = modfiyCQLDefinitionList(toBeModifiedObj,
									currentObj, definitionList);
							result.setSuccess(true);
							result.setDefinition(currentObj);
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
					logger.debug(" CQLServiceImpl: updateSubTreeLookUp End :  ");
				} else {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
			} else {
				currentObj.setId(UUID.randomUUID().toString());
				String name = "define" + " \"" + currentObj.getDefinitionName() + "\""; 
				parseCQLDefForErrors(result, measureId, name, currentObj.getDefinitionLogic());
				isDuplicate = validator.validateForSpecialChar(currentObj
						.getDefinitionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NO_SPECIAL_CHAR);
					return result;
				}
				
				isDuplicate = isDuplicateIdentifierName(
						currentObj.getDefinitionName(), measureId);
				if (!isDuplicate) {
					String cqlString = createDefinitionsXML(currentObj);
					String XPATH_EXPRESSION_DEFINTIONS = "/measure/cqlLookUp/definitions";
					try {
						Node nodeDefinitions = processor.findNode(
								processor.getOriginalDoc(),
								XPATH_EXPRESSION_DEFINTIONS);
						if (nodeDefinitions != null) {
							try {
								processor.appendNode(cqlString, "definition",
										XPATH_EXPRESSION_DEFINTIONS);
								processor.setOriginalXml(processor
										.transform(processor.getOriginalDoc()));
								xmlModel.setXml(processor.getOriginalXml());
								getService().saveMeasureXml(xmlModel);
								name = "define" + " \"" + currentObj.getDefinitionName() + "\""; 
								parseCQLDefForErrors(result, measureId, name, currentObj.getDefinitionLogic());
								result.setSuccess(true);
								result.setDefinition(currentObj);
								definitionList.add(currentObj);
								wrapper.setCqlDefinitions(definitionList);
								
							} catch (SAXException e) {
								result.setSuccess(false);
								e.printStackTrace();
							} catch (IOException e) {
								result.setSuccess(false);
								e.printStackTrace();
							}
							
						} else {
							result.setSuccess(false);
							result.setFailureReason(result.NODE_NOT_FOUND);
						}
						
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
					
				} else {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_UNIQUE);
				}
			}
		}
		
		if (result.isSuccess() && (wrapper.getCqlDefinitions().size() > 0)) {
			result.getCqlModel().setDefinitionList(
					sortDefinitionsList(wrapper.getCqlDefinitions()));
		}
		
		return result;
	}
	 
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#deleteDefinition(java.lang.String, mat.model.cql.CQLDefinition, mat.model.cql.CQLDefinition, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteDefinition(String measureId, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();	
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		System.out.println("DELETE DEFINITION CLICK " + currentObj.getDefinitionName());
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());

		if(xmlModel != null) {
			String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "/measure/cqlLookUp//definition[@id='"+ toBeDeletedObj.getId() + "']";
			System.out.println("XPATH: " + XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);
			try {
				Node definitionNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);
				
				if(definitionNode != null) {
					System.out.println("FOUND NODE");
					
					// remove from xml
					Node deletedNode = definitionNode.getParentNode().removeChild(definitionNode);
					System.out.println(deletedNode.getAttributes().getNamedItem("name").toString());
					processor.setOriginalXml(processor
							.transform(processor.getOriginalDoc()));
					xmlModel.setXml(processor.getOriginalXml());
					getService().saveMeasureXml(xmlModel);		
					
					
					// remove from definition list
					definitionList.remove(toBeDeletedObj);
					wrapper.setCqlDefinitions(definitionList);
					result.setSuccess(true);
					result.setDefinition(toBeDeletedObj);
				}
				
				else {
					System.out.println("NOT FOUND NODE");
					result.setSuccess(false);
					result.setFailureReason(result.NODE_NOT_FOUND);
				}
				
			} catch (XPathExpressionException e) {
				result.setSuccess(false);
				e.printStackTrace();
			}
			
		}
		
		if (result.isSuccess() && (wrapper.getCqlDefinitions().size() > 0)) {
			result.getCqlModel().setDefinitionList(
					sortDefinitionsList(wrapper.getCqlDefinitions()));
			System.out.println(xmlModel.getXml());
			System.out.println(result.isSuccess());
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#deleteFunctions(java.lang.String, mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteFunctions(String measureId, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();	
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		System.out.println("DELETE FUNCTION CLICK " + currentObj.getFunctionName());
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());

		if(xmlModel != null) {
			String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "/measure/cqlLookUp//function[@id='"+ toBeDeletedObj.getId() + "']";
			System.out.println("XPATH: " + XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);
			try {
				Node functionNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);
				
				if(functionNode != null) {
					System.out.println("FOUND NODE");
					
					// remove from xml
					Node deletedNode = functionNode.getParentNode().removeChild(functionNode);
					System.out.println(deletedNode.getAttributes().getNamedItem("name").toString());
					processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
					xmlModel.setXml(processor.getOriginalXml());
					getService().saveMeasureXml(xmlModel);		
					
					
					// remove from function list
					functionsList.remove(toBeDeletedObj);
					wrapper.setCqlFunctionsList(functionsList);
					result.setSuccess(true);
					result.setFunction(toBeDeletedObj);
				}
				
				else {
					System.out.println("NOT FOUND NODE");
					result.setSuccess(false);
					result.setFailureReason(result.NODE_NOT_FOUND);
				}
				
			} catch (XPathExpressionException e) {
				result.setSuccess(false);
				e.printStackTrace();
			}
			
		}
		
		if (result.isSuccess() && (wrapper.getCqlFunctionsList().size() > 0)) {
			result.getCqlModel().setCqlFunctions(sortFunctionssList(wrapper.getCqlFunctionsList()));
			System.out.println(xmlModel.getXml());
			System.out.println(result.isSuccess());
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#deleteParameter(java.lang.String, mat.model.cql.CQLParameter, mat.model.cql.CQLParameter, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteParameter(String measureId, CQLParameter toBeDeletedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();	
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		System.out.println("DELETE PARAMETER CLICK " + currentObj.getParameterName());
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureId);
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		XmlProcessor processor = new XmlProcessor(xmlModel.getXml());

		if(xmlModel != null) {
			String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "/measure/cqlLookUp//parameter[@id='"+ toBeDeletedObj.getId() + "']";
			System.out.println("XPATH: " + XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);
			try {
				Node parameterNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);
				
				if(parameterNode != null) {
					System.out.println("FOUND NODE");
					
					// remove from xml
					Node deletedNode = parameterNode.getParentNode().removeChild(parameterNode);
					System.out.println(deletedNode.getAttributes().getNamedItem("name").toString());
					processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
					xmlModel.setXml(processor.getOriginalXml());
					getService().saveMeasureXml(xmlModel);		
					
					
					// remove from parameter list
					parameterList.remove(toBeDeletedObj); 
					wrapper.setCqlParameterList(parameterList);
					result.setSuccess(true);
					result.setParameter(toBeDeletedObj);
				}
				
				else {
					System.out.println("NOT FOUND NODE");
					result.setSuccess(false);
					result.setFailureReason(result.NODE_NOT_FOUND);
				}
				
			} catch (XPathExpressionException e) {
				result.setSuccess(false);
				e.printStackTrace();
			}
			
		}
		
		if (result.isSuccess() && (wrapper.getCqlParameterList().size() > 0)) {
			result.getCqlModel().setCqlParameters(sortParametersList(wrapper.getCqlParameterList()));
			System.out.println(xmlModel.getXml());
			System.out.println(result.isSuccess());
		}
		
		return result;
	}
	
	
	/**
	 * Update risk adjustment variables.
	 *
	 * @param processor the processor
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj the current obj
	 */
	private void updateRiskAdjustmentVariables(XmlProcessor processor,
			CQLDefinition toBeModifiedObj, CQLDefinition currentObj) {
		
		logger.debug(" CQLServiceImpl: updateRiskAdjustmentVariables Start :  ");
		// XPath to find All cqlDefinitions in riskAdjustmentVariables to be modified Definitions.
		String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure//cqldefinition[@uuid='"
				+ toBeModifiedObj.getId() + "']";
		try {
			NodeList nodesSDE = processor.findNodeList(processor.getOriginalDoc(), XPATH_EXPRESSION_SDE_ELEMENTREF);
			for (int i = 0; i < nodesSDE.getLength(); i++) {
				Node newNode = nodesSDE.item(i);
				newNode.getAttributes().getNamedItem("displayName").setNodeValue(currentObj.getDefinitionName());
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" CQLServiceImpl: updateRiskAdjustmentVariables End :  ");
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.CQLService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String measureId) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLStringFromMeasureXML(xmlModel.getXml(),measureId);
		
		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel,"").toString();
		SaveUpdateCQLResult parsedCQL = parseCQLStringForError(cqlFileString);
		if (!parsedCQL.getCqlErrors().isEmpty()) {
			modifyQDMStatus(cqlModel);	
		}else{
			findUsedValuesets(cqlFileString, cqlModel);		
		}
		result.setCqlModel(cqlModel);
		return result;
	}
	
	/**
	 * Modify QDM status.
	 *
	 * @param cqlModel the cql model
	 */
	private static void modifyQDMStatus(CQLModel cqlModel) {
		for (int i = 0; i < cqlModel.getValueSetList().size(); i++) {
			cqlModel.getValueSetList().get(i).setUsed(true);
		}
	}
	
	/**
	 * Gets the CQL definitions from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the CQL definitions from measure xml
	 */
	public CQLDefinitionsWrapper getCQLDefinitionsFromMeasureXML(
			String measureId) {
		
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		CQLDefinitionsWrapper wrapper = null;
		if (measureXmlModel != null) {
			wrapper = convertXmltoCQLDefinitionModel(measureXmlModel);
			if ((wrapper.getCqlDefinitions() != null)
					&& (wrapper.getCqlDefinitions().size() > 0)) {
				sortDefinitionsList(wrapper.getCqlDefinitions());
			}
		}
		return wrapper;
	}
	
	/**
	 * Convert xmlto cql definition model.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the CQL definitions wrapper
	 */
	private CQLDefinitionsWrapper convertXmltoCQLDefinitionModel(
			final MeasureXmlModel xmlModel) {
		logger.info("In CQLServiceImpl.convertXmltoCQLDefinitionModel()");
		CQLDefinitionsWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml())
			.getXmlByTagName("cqlLookUp");
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain cqlLookUp tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader()
				.getResourceAsURL("CQLDefinitionModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLDefinitionsWrapper.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml..cqlLookUp " + xml);
				details = (CQLDefinitionsWrapper) unmar
						.unmarshal(new InputSource(new StringReader(xml)));
				// logger.info("unmarshalling complete..cqlLookUp" +
				// details.getQualityDataDTO().get(0).getCodeListName());
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLDefinitionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/**
	 * Convert xmlto cql definition model.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the CQL definitions wrapper
	 */
	private CQLParametersWrapper convertXmltoCQLParameterModel(
			final MeasureXmlModel xmlModel) {
		logger.info("In CQLServiceImpl.convertXmltoCQLParameterModel()");
		CQLParametersWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml())
			.getXmlByTagName("cqlLookUp");
			// logger.info("xml by tag name cqlLookUp" + xml);
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain cqlLookUp tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader()
				.getResourceAsURL("CQLParameterModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLParametersWrapper.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml..cqlLookUp " + xml);
				details = (CQLParametersWrapper) unmar
						.unmarshal(new InputSource(new StringReader(xml)));
				// logger.info("unmarshalling complete..cqlLookUp" +
				// details.getQualityDataDTO().get(0).getCodeListName());
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLDefinitionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/**
	 * Convert xml to cql Functions model.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the CQL Function wrapper
	 */
	private CQLFunctionsWrapper convertXmltoCQLFunctionModel(
			final MeasureXmlModel xmlModel) {
		logger.info("In CQLServiceImpl.convertXmltoCQLParameterModel()");
		CQLFunctionsWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml())
			.getXmlByTagName("cqlLookUp");
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain cqlLookUp tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader()
				.getResourceAsURL("CQLFunctionModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLFunctionsWrapper.class);
				unmar.setWhitespacePreserve(true);
				details = (CQLFunctionsWrapper) unmar
						.unmarshal(new InputSource(new StringReader(xml)));
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLFunctionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/**
	 * Creates the Parameters xml.
	 * 
	 * @param parameter
	 *            the CQLParameter
	 * @return the string
	 */
	@Override
	public String createParametersXML(CQLParameter parameter) {
		
		logger.info("In CQLServiceImpl.createParametersXML");
		Mapping mapping = new Mapping();
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		List<CQLParameter> paramList = new ArrayList<CQLParameter>();
		
		paramList.add(parameter);
		wrapper.setCqlParameterList(paramList);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
			.getResourceAsURL("CQLParameterModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLParameter is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLParameterModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createParametersXML()");
		return stream.toString();
		
	}
	
	/**
	 * Creates the Function xml.
	 * 
	 * @param function
	 *            the CQLFunctions
	 * @return the string
	 */
	private String createFunctionsXML(CQLFunctions function) {
		
		logger.info("In CQLServiceImpl.createFunctionsXML");
		Mapping mapping = new Mapping();
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		List<CQLFunctions> funcList = new ArrayList<CQLFunctions>();
		
		funcList.add(function);
		wrapper.setCqlFunctionsList(funcList);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
			.getResourceAsURL("CQLFunctionModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLFunctions is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLFunctionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createFunctionsXML()");
		return stream.toString();
		
	}
	
	/**
	 * Creates the definitions xml.
	 * 
	 * @param definition
	 *            the definition
	 * @return the string
	 */
	@Override
	public String createDefinitionsXML(CQLDefinition definition) {
		
		logger.info("In CQLServiceImpl.createDefinitionsXML");
		Mapping mapping = new Mapping();
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		List<CQLDefinition> definitionList = new ArrayList<CQLDefinition>();
		definitionList.add(definition);
		wrapper.setCqlDefinitions(definitionList);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
			.getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLDefinition is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLDefinitionModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				// logger.info(e.printStackTrace());
				e.printStackTrace();
			}
		}
		logger.info("Exiting ManageCodeLiseServiceImpl.createDefinitionsXML()");
		return stream.toString();
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.CQLService#getCQLFileData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLFileData(String measureId) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		SaveUpdateCQLResult allCqlData = getCQLData(measureId);
		StringBuilder cqlString = getCqlString(allCqlData.getCqlModel());
		if (cqlString != null) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		result.setCqlString(cqlString.toString());
		return result;
	}
	
	/**
	 * Gets the cql string.
	 * 
	 * @param cqlModel
	 *            - CQLModel
	 * @return the cql string
	 */
	@Override
	public StringBuilder getCqlString(CQLModel cqlModel) {
		
		return CQLUtilityClass.getCqlString(cqlModel, "");
	}
	
	/**
	 * Check for cql keywords.
	 *
	 * @return true, if successful
	 */
	/*private boolean checkForCQLKeywords(String name) {
		
		XmlProcessor cqlXMLProcessor = CQLTemplateXML
				.getCQLTemplateXmlProcessor();
		String XPATH_CQL_KEYWORDS = "/cqlTemplate/keywords/keyword[translate(text(),'abcdefghijklmnopqrstuvwxyz',"
				+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + name.toUpperCase() + "']";
		try {
			Node templateNode = cqlXMLProcessor.findNode(
					cqlXMLProcessor.getOriginalDoc(), XPATH_CQL_KEYWORDS);
			if (templateNode != null) {
				return true;
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return false;
	}*/
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.CQLService#getCQLKeyWords()
	 */
	@Override
	public CQLKeywords getCQLKeyWords() {
		
		CQLKeywords cqlKeywords = new CQLKeywords();
		XmlProcessor cqlXMLProcessor = CQLTemplateXML
				.getCQLTemplateXmlProcessor();
		String XPATH_DATATYPES = "/cqlTemplate/datatypes/datatype";
		String XPATH_TIMINGS = "/cqlTemplate/timings/timing";
		String XPATH_FUNCTIONS = "/cqlTemplate/functions/function";
		List<String> cqlDataTypeList = new ArrayList<String>();
		List<String> cqlTimingList = new ArrayList<String>();
		List<String> cqlFunctionList = new ArrayList<String>();
		try {
			NodeList dataTypeNodeList = cqlXMLProcessor.findNodeList(
					cqlXMLProcessor.getOriginalDoc(), XPATH_DATATYPES);
			NodeList timingNodeList = cqlXMLProcessor.findNodeList(
					cqlXMLProcessor.getOriginalDoc(), XPATH_TIMINGS);
			NodeList functionNodeList = cqlXMLProcessor.findNodeList(
					cqlXMLProcessor.getOriginalDoc(), XPATH_FUNCTIONS);
			if (dataTypeNodeList != null) {
				for (int i = 0; i < dataTypeNodeList.getLength(); i++) {
					Node node = dataTypeNodeList.item(i);
					cqlDataTypeList.add(node.getTextContent());
				}
			}
			if (timingNodeList != null) {
				for (int i = 0; i < timingNodeList.getLength(); i++) {
					Node node = timingNodeList.item(i);
					cqlTimingList.add(node.getTextContent());
				}
			}
			if (functionNodeList != null) {
				for (int i = 0; i < functionNodeList.getLength(); i++) {
					Node node = functionNodeList.item(i);
					cqlFunctionList.add(node.getTextContent());
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		cqlKeywords.setCqlDataTypeList(cqlDataTypeList);
		cqlKeywords.setCqlTimingList(cqlTimingList);
		cqlKeywords.setCqlFunctionsList(cqlFunctionList);
		return cqlKeywords;
	}
	
	/**
	 * Checks if is duplicate identifier name.
	 * 
	 * @param identifierName
	 *            the identifier name
	 * @param measureId
	 *            the measure id
	 * @return true, if is duplicate identifier name
	 */
	private boolean isDuplicateIdentifierName(String identifierName,
			String measureId) {
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		// boolean isInvalid = false;
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			String XPATH_CQLLOOKUP_IDENTIFIER_NAME = "/measure/cqlLookUp//node()[translate(@name, "
					+ "'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
					+ identifierName.toUpperCase() + "']";
			try {
				NodeList nodeList = processor.findNodeList(
						processor.getOriginalDoc(),
						XPATH_CQLLOOKUP_IDENTIFIER_NAME);
				
				if ((nodeList != null) && (nodeList.getLength() > 0)) {
					return true;
				}
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
		}
		
		return false;
	}
	
	/**
	 * Check if keyword for func arguments.
	 *
	 * @param toBeModifiedObj the to be modified obj
	 * @param currentObj            the current obj
	 * @param parameterList the parameter list
	 * @return the save update cql result
	 */
	/*private SaveUpdateCQLResult checkIfKeywordForFuncArguments(
			SaveUpdateCQLResult result, CQLFunctions currentObj) {
		
		List<CQLFunctionArgument> argList = currentObj.getArgumentList();
		for (int i = 0; i < argList.size(); i++) {
			if (checkForCQLKeywords(argList.get(i).getArgumentName())) {
				argList.get(i).setValid(true);
				result.setSuccess(true);
			} else {
				argList.get(i).setValid(false);
			}
		}
		
		if (argList.size() > 0) {
			currentObj.setArgumentList(argList);
		} else {
			currentObj.setArgumentList(new ArrayList<CQLFunctionArgument>());
		}
		result.setFunction(currentObj);
		return result;
	}*/
	
	/**
	 * Modfiy cql Parameter List list.
	 * 
	 * @param toBeModifiedObj
	 *            the to be modified obj
	 * @param currentObj
	 *            the current obj
	 * @param parameterList
	 *            the parameter list
	 * @return the CQL parameters wrapper
	 */
	private CQLParametersWrapper modfiyCQLParameterList(
			CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		Iterator<CQLParameter> iterator = parameterList.iterator();
		while (iterator.hasNext()) {
			CQLParameter cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModifiedObj.getId())) {
				// CQLDefinition definition = cqlDefinition;
				
				iterator.remove();
				break;
			}
		}
		parameterList.add(currentObj);
		wrapper.setCqlParameterList(parameterList);
		return wrapper;
	}
	
	/**
	 * Modfiy cql Function List list.
	 * 
	 * @param toBeModifiedObj
	 *            the to be modified obj
	 * @param currentObj
	 *            the current obj
	 * @param functionList
	 *            the function list
	 * @return the CQL Function wrapper
	 */
	private CQLFunctionsWrapper modfiyCQLFunctionList(
			CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionList) {
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		Iterator<CQLFunctions> iterator = functionList.iterator();
		while (iterator.hasNext()) {
			CQLFunctions cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModifiedObj.getId())) {
				// CQLDefinition definition = cqlDefinition;
				
				iterator.remove();
				break;
			}
		}
		functionList.add(currentObj);
		wrapper.setCqlFunctionsList(functionList);
		return wrapper;
	}
	
	/**
	 * Modfiy cql definition list.
	 * 
	 * @param toBeModifiedObj
	 *            the to be modified obj
	 * @param currentObj
	 *            the current obj
	 * @param definitionList
	 *            the definition list
	 * @return the CQL definitions wrapper
	 */
	private CQLDefinitionsWrapper modfiyCQLDefinitionList(
			CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		Iterator<CQLDefinition> iterator = definitionList.iterator();
		while (iterator.hasNext()) {
			CQLDefinition cqlDefinition = iterator.next();
			if (cqlDefinition.getId().equals(toBeModifiedObj.getId())) {
				// CQLDefinition definition = cqlDefinition;
				
				iterator.remove();
				break;
			}
		}
		definitionList.add(currentObj);
		wrapper.setCqlDefinitions(definitionList);
		return wrapper;
	}
	
	/**
	 * Sort definitions list.
	 * 
	 * @param defineList
	 *            the define list
	 * @return the list
	 */
	private List<CQLDefinition> sortDefinitionsList(
			List<CQLDefinition> defineList) {
		
		Collections.sort(defineList, new Comparator<CQLDefinition>() {
			@Override
			public int compare(final CQLDefinition o1, final CQLDefinition o2) {
				return o1.getDefinitionName().compareToIgnoreCase(
						o2.getDefinitionName());
			}
		});
		
		return defineList;
	}
	
	/**
	 * Sort parameters list.
	 * 
	 * @param paramList
	 *            the param list
	 * @return the list
	 */
	private List<CQLParameter> sortParametersList(List<CQLParameter> paramList) {
		
		Collections.sort(paramList, new Comparator<CQLParameter>() {
			@Override
			public int compare(final CQLParameter o1, final CQLParameter o2) {
				return o1.getParameterName().compareToIgnoreCase(
						o2.getParameterName());
			}
		});
		
		return paramList;
	}
	
	/**
	 * Sort Functions list.
	 * 
	 * @param funcList
	 *            the Function list
	 * @return the list
	 */
	private List<CQLFunctions> sortFunctionssList(List<CQLFunctions> funcList) {
		
		Collections.sort(funcList, new Comparator<CQLFunctions>() {
			@Override
			public int compare(final CQLFunctions o1, final CQLFunctions o2) {
				return o1.getFunctionName().compareToIgnoreCase(
						o2.getFunctionName());
			}
		});
		
		return funcList;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getJSONObjectFromXML()
	 */
	@Override
	public String getJSONObjectFromXML() {
		String result = null;
		try {
			result = convertXmlToString();
			XMLSerializer xmlSerializer = new XMLSerializer();
			xmlSerializer.setForceTopLevelObject(true);
			xmlSerializer.setTypeHintsEnabled(false);
			JSON json = xmlSerializer.read(result);
			JSONObject jsonObject = JSONObject.fromObject(json.toString());
			result = jsonObject.toString(4);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	
	/**
	 * Convert xml to string.
	 *
	 * @return the string
	 */
	@SuppressWarnings("resource")
	private String convertXmlToString() {
		String fileName = "CQLTimingExpressions.xml";
		URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
		File xmlFile = null;
		FileReader fr;
		String line = "";
		StringBuilder sb = new StringBuilder();
		try {
			try {
				xmlFile = new File(templateFileUrl.toURI());
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			fr = new FileReader(xmlFile);
			BufferedReader br = new BufferedReader(fr);
			
			try {
				while ((line = br.readLine()) != null) {
					sb.append(line.trim());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getSupplementalDefinitions()
	 */
	@Override
	public String getSupplementalDefinitions() {
		
		return cqlSupplementalDefinitionXMLString;
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getDefaultCodeSystems()
	 */
	@Override
	public String getDefaultCodeSystems() {
		
		return cqlDefaultCodeSystemXMLString;
		
	}
	
	
	/**
	 * Parses the CQL def for errors.
	 *
	 * @param result the result
	 * @param measureId the measure id
	 * @param name the name
	 * @param logic the logic
	 * @return the save update CQL result
	 */
	public SaveUpdateCQLResult parseCQLDefForErrors(SaveUpdateCQLResult result, String measureId, String name, String logic) {
		
		List<String> Errors = new ArrayList<String>();
		MeasureXmlModel measureXML = getService().getMeasureXmlForMeasure(measureId);
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML.getXml(),measureId), name).toString();
	
		result.getCqlModel().setLines(countLines(cqlFileString));
		
		
		
		
		
		
		String wholeDef = name + " " + logic; 
		System.out.println("Whole Definition: " + wholeDef);

		int endLine = CQLUtilityClass.getSize(); 
		int size = countLines(wholeDef); 
		int startLine = endLine - size + 1;
		
		System.out.println("Start Line: " + startLine);
		System.out.println("End Line: " + endLine);
		
		result.setStartLine(startLine);
		result.setEndLine(endLine);
		
	
		List<CqlTranslatorException> cqlErrorsList = new ArrayList<CqlTranslatorException>();
		//int lines = countLines(cqlFileString);
		List<CQLErrors> errors = new ArrayList<CQLErrors>();
		if(!StringUtils.isBlank(cqlFileString)){
			CQLtoELM cqlToElm = new CQLtoELM(cqlFileString); 
			cqlToElm.doTranslation(true, false, false);
			
			if(cqlToElm.getErrors() != null) {
				cqlErrorsList.addAll(cqlToElm.getErrors()); 
			}
			
		}
		
		for(CqlTranslatorException cte : cqlErrorsList){
			CQLErrors cqlErrors = new CQLErrors();
			
			int errorStartLine = cte.getLocator().getStartLine(); 
			
			cqlErrors.setStartErrorInLine(cte.getLocator().getStartLine() - startLine);
			cqlErrors.setStartErrorAtOffset(cte.getLocator().getStartChar());
			
			cqlErrors.setEndErrorInLine(cte.getLocator().getEndLine() - startLine);
			cqlErrors.setEndErrorAtOffset(cte.getLocator().getEndChar());

			cqlErrors.setErrorMessage(cte.getMessage());
			
			if((errorStartLine >= startLine && errorStartLine <= endLine)) {
				errors.add(cqlErrors);

			}
		}
				
		result.setCqlErrors(errors);
		
	return result;
	}
		
	/**
	 * Gets the start line.
	 *
	 * @param toFind the to find
	 * @param cqlString the cql string
	 * @return the start line
	 */
	private static int getStartLine(String toFind, String cqlString) {
		System.out.println("TO FIND: " + toFind);
		Scanner scanner = new Scanner(cqlString);
		
		int startLine = 1; 
		while(scanner.hasNextLine()) {
			String currentLine = scanner.nextLine();
			if(currentLine.startsWith(toFind)) {
				break; 
			} else {
				startLine++;
			}
		}
		
		
		return startLine; 
	}
	

	
	/**
	 * Count lines.
	 *
	 * @param str the str
	 * @return the int
	 */
	public static int countLines(String str) {
	    if(str == null || str.isEmpty())
	    {
	        return 0;
	    }
	    int lines = 1;
	    int pos = 0;
	    while ((pos = str.indexOf("\n", pos) + 1) != 0) {
	        lines = lines + 1;
	    }
	    return lines;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getDefaultCodes()
	 */
	@Override
	public String getDefaultCodes() {
		return cqlDefaultCodesXMLString;
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getUsedCQlArtifacts(java.lang.String)
	 */
	@Override
	public GetUsedCQLArtifactsResult getUsedCQlArtifacts(String measureId) {
		GetUsedCQLArtifactsResult result = new GetUsedCQLArtifactsResult(); 
		
		System.out.println("GETTING CQL ARTIFACTS");
		MeasureXmlModel measureXML = getService().getMeasureXmlForMeasure(measureId);
		XmlProcessor processor = new XmlProcessor(measureXML.getXml());
		String cqlFileString = CQLUtilityClass.getCqlString(CQLUtilityClass.getCQLStringFromMeasureXML(measureXML.getXml() ,measureId), "").toString();
		System.out.println(cqlFileString);
		
		
		
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
		
		List<CQLDefinitionModelObject> cqlDefinitionObjects = new ArrayList<CQLDefinitionModelObject>(cqlFileObject.getDefinitionsMap().values());
		List<CQLFunctionModelObject> cqlFunctionObjects = new ArrayList<CQLFunctionModelObject>(cqlFileObject.getFunctionsMap().values()); 
		List<CQLParameterModelObject> cqlParameterObjects = new ArrayList<CQLParameterModelObject>(cqlFileObject.getParametersMap().values()); 
		
		Set<CQLDefinitionModelObject> usedCQLDefinitionObjects = new HashSet<CQLDefinitionModelObject>();
		Set<CQLFunctionModelObject> usedCQLFunctionObjects = new HashSet<CQLFunctionModelObject>(); 
		Set<CQLParameterModelObject> usedCQLParameterObjects = new HashSet<CQLParameterModelObject>(); 
		
		for(CQLDefinitionModelObject cqlDefinitionModelObject: cqlDefinitionObjects) {
			usedCQLDefinitionObjects.addAll(cqlDefinitionModelObject.getReferredToDefinitions()); 
			usedCQLFunctionObjects.addAll(cqlDefinitionModelObject.getReferredToFunctions()); 
			usedCQLParameterObjects.addAll(cqlDefinitionModelObject.getReferredToParameters());
		}
		
		for(CQLFunctionModelObject cqlFunctionModelObject : cqlFunctionObjects) {
			usedCQLDefinitionObjects.addAll(cqlFunctionModelObject.getReferredToDefinitions()); 
			usedCQLFunctionObjects.addAll(cqlFunctionModelObject.getReferredToFunctions());
			usedCQLParameterObjects.addAll(cqlFunctionModelObject.getReferredToParameters()); 
		}	
		
		Set<String> usedCQLDefinition = new HashSet<String>(); 
		Set<String> usedCQLFunction = new HashSet<String>(); 
		Set<String> usedCQLParameter = new HashSet<String>(0); 

		for(CQLDefinitionModelObject c : usedCQLDefinitionObjects) {
			usedCQLDefinition.add(c.getIdentifier().replaceAll("\"", ""));
		}
		
		for(CQLFunctionModelObject c : usedCQLFunctionObjects) {
			usedCQLFunction.add(c.getIdentifier().replaceAll("\"", ""));
		}
		
		for(CQLParameterModelObject c : usedCQLParameterObjects) {
			usedCQLParameter.add(c.getIdentifier().replaceAll("\"", ""));
		}
		
		CQLArtifactHolder cqlArtifactHolder = CQLUtil.getCQLArtifactsReferredByPoplns(processor.getOriginalDoc(), cqlFileObject);
		usedCQLDefinition.addAll(cqlArtifactHolder.getCqlDefFromPopSet());		
		usedCQLFunction.addAll(cqlArtifactHolder.getCqlFuncFromPopSet()); 
		
		List<String> parameterList = new ArrayList<String>(); 
		parameterList.addAll(cqlArtifactHolder.getCqlParameterIdentifierSet());
		System.out.println("DEF LIST: " + usedCQLDefinition);
		System.out.println("FUNCT LIST: " + usedCQLFunction);
		System.out.println("PARAM LIST: " + usedCQLParameter); 
		
		result.setUsedCQLDefinitions(new ArrayList<String>(usedCQLDefinition));
		result.setUsedCQLFunctionss(new ArrayList<String>(usedCQLFunction));
		result.setUsedCQLParameters(new ArrayList<String>(usedCQLParameter));
		
		
		return result; 
	}
	
	/**
	 * Find used valuesets.
	 *
	 * @param cqlFileString the cql file string
	 * @param cqlModel the cql model
	 */
	private void findUsedValuesets(String cqlFileString, CQLModel cqlModel){
		MATCQLParser matcqlParser = new MATCQLParser();
		CQLFileObject cqlFileObject = matcqlParser.parseCQL(cqlFileString);
		try {
			CQLArtifactHolder cqlArtifactHolder = CQLUtil.getUsedCQLValuesets(cqlFileObject);
			List<String> usedValuesets = new ArrayList<String>();
			
			usedValuesets.addAll(new ArrayList<String>(cqlArtifactHolder.getCqlValuesetIdentifierSet()));
			cqlModel.getValueSetList();
			
			
			for(int i=0; i<cqlModel.getValueSetList().size(); i++){
				CQLQualityDataSetDTO cqlDataset = cqlModel.getValueSetList().get(i);
				
				if(usedValuesets.contains(cqlDataset.getCodeListName())){
					cqlDataset.setUsed(true);
				}
			}
			
			
		} catch (XPathExpressionException e) {
			logger.info("Error while trying to find used value sets : "+e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#parseCQLStringForError(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult parseCQLStringForError(String cqlFileString) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		List<CqlTranslatorException> cqlErrorsList = new ArrayList<CqlTranslatorException>();
		List<CQLErrors> errors = new ArrayList<CQLErrors>();
		if(!StringUtils.isBlank(cqlFileString)){
			
			CQLtoELM cqlToElm = new CQLtoELM(cqlFileString); 
			cqlToElm.doTranslation(true, false, false);
			
			if(cqlToElm.getErrors() != null) {
				cqlErrorsList.addAll(cqlToElm.getErrors());
			}
		}
		
		for(CqlTranslatorException cte : cqlErrorsList){
			
			CQLErrors cqlErrors = new CQLErrors();
			cqlErrors.setErrorInLine(cte.getLocator().getStartLine());
			cqlErrors.setErrorAtOffeset(cte.getLocator().getStartChar());
			cqlErrors.setErrorMessage(cte.getMessage());
			errors.add(cqlErrors);
		}

		result.setCqlErrors(errors);

		return result;
	}
	
	
	/**
	 * Save QD sto measure.
	 *
	 * @param valueSetTransferObject the value set transfer object
	 * @return the save update code list result
	 */
	@Override
	public final SaveUpdateCQLResult saveCQLValuesettoMeasure(
			CQLValueSetTransferObject valueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
	    valueSetTransferObject.scrubForMarkUp();
		CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
		MatValueSet matValueSet = valueSetTransferObject.getMatValueSet();
		qds.setOid(matValueSet.getID());
		qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		qds.setCodeListName(matValueSet.getDisplayName());
		if (matValueSet.isGrouping()) {
			qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
		} else {
			qds.setTaxonomy(matValueSet.getCodeSystemName());
		}
		qds.setUuid(UUID.randomUUID().toString());
		if (valueSetTransferObject.isVersion()) {
			qds.setVersion(valueSetTransferObject.getMatValueSet().getVersion());
		} else {
			qds.setVersion("1.0");
		}
		if (valueSetTransferObject.isExpansionProfile()) {
			qds.setExpansionIdentifier(valueSetTransferObject.getMatValueSet()
					.getExpansionProfile());
		}

		ArrayList<CQLQualityDataSetDTO> qualityDataSetDTOs = (ArrayList<CQLQualityDataSetDTO>) valueSetTransferObject
				.getAppliedQDMList();

		 // Treat as regular QDM
		
			if (!isDuplicate(valueSetTransferObject, true)) {
				wrapper.getQualityDataDTO().add(qds);
				String xmlString = addCQLAppliedQDMInMeasureXML(wrapper);
				result.setSuccess(true);
				qualityDataSetDTOs.add(qds);
				result.setCqlAppliedQDMList(sortQualityDataSetList(qualityDataSetDTOs));
				result.setCqlString(xmlString);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		
		return result;
	}
	
	/**
	 * Sort quality data set list.
	 *
	 * @param finalList the final list
	 * @return the list
	 */
	private List<CQLQualityDataSetDTO> sortQualityDataSetList(
			final List<CQLQualityDataSetDTO> finalList) {

		Collections.sort(finalList, new Comparator<CQLQualityDataSetDTO>() {
			@Override
			public int compare(final CQLQualityDataSetDTO o1,
					final CQLQualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(
						o2.getCodeListName());
			}
		});

		return finalList;

	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#saveUserDefinedQDStoMeasure(mat.model.CQLValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValuesettoMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		matValueSetTransferObject.scrubForMarkUp();
		QDMInputValidator validator = new QDMInputValidator();
		String errorMessage = validator.validate(matValueSetTransferObject);
		if (errorMessage.isEmpty()) {
			ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
			List<CQLQualityDataSetDTO> existingQDSList = matValueSetTransferObject
					.getAppliedQDMList();
			boolean isQDSExist = false;
			for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {
				if (dataSetDTO.getOid().equalsIgnoreCase(
						ConstantMessages.USER_DEFINED_QDM_OID) && dataSetDTO.getCodeListName()
						.equalsIgnoreCase(matValueSetTransferObject
								.getUserDefinedText())) {
						isQDSExist = true;
						break;
				}
			}
			if (!isQDSExist) {
				wrapper.setQualityDataDTO(qdsList);
				CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
				qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
				qds.setId(UUID.randomUUID().toString());
				qds.setCodeListName(matValueSetTransferObject
						.getUserDefinedText());
				qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
				qds.setUuid(UUID.randomUUID().toString());
				qds.setVersion("1.0");
				wrapper.getQualityDataDTO().add(qds);
				String qdmXMLString = addCQLAppliedQDMInMeasureXML(wrapper);
				result.setSuccess(true);
				result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper
						.getQualityDataDTO()));
				result.setCqlString(qdmXMLString);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}
	
	
	
	/**
	 * Checks if is duplicate.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @param isVSACValueSet the is VSAC value set
	 * @return true, if is duplicate
	 */
	private boolean isDuplicate(CQLValueSetTransferObject matValueSetTransferObject, boolean isVSACValueSet) {
		logger.info(" checkForDuplicates Method Call Start.");
		boolean isQDSExist = false;
		String qdmCompareName = "";
		
		if (isVSACValueSet) {
			qdmCompareName = matValueSetTransferObject.getMatValueSet()
					.getDisplayName();
		} else {
			qdmCompareName = matValueSetTransferObject.getCodeListSearchDTO().getName();
		}
		
		List<CQLQualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {

			String codeListName = "";
			codeListName = dataSetDTO.getCodeListName();

			if (codeListName.equalsIgnoreCase(qdmCompareName)) {
				isQDSExist = true;
				break;
			}
		}
		logger.info("checkForDuplicates Method Call End.Check resulted in :" + (isQDSExist));
		return isQDSExist;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#updateQDStoMeasure(mat.model.CQLValueSetTransferObject)
	 */
	@Override
	public final SaveUpdateCQLResult updateCQLValueSetstoMeasure(
			CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = null;
		matValueSetTransferObject.scrubForMarkUp();
		if (matValueSetTransferObject.getMatValueSet() != null) {
			result = updateVSACValueSetInCQLLookUp(matValueSetTransferObject);
		} else if (matValueSetTransferObject.getCodeListSearchDTO() != null) {
			result = updateUserDefineValuesetInCQLLookUp(matValueSetTransferObject);
		}
		return result;
	}
	
	/**
	 * Update VSAC value set in element look up.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult updateVSACValueSetInCQLLookUp(
			CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataSetDTO oldQdm = new CQLQualityDataSetDTO();
		populatedOldQDM(oldQdm,
				matValueSetTransferObject.getCqlQualityDataSetDTO());
		  // Treat as regular QDM
			List<CQLQualityDataSetDTO> origAppliedQDMList = matValueSetTransferObject
					.getAppliedQDMList();
			List<CQLQualityDataSetDTO> tempAppliedQDMList = new ArrayList<CQLQualityDataSetDTO>();
			tempAppliedQDMList.addAll(matValueSetTransferObject
					.getAppliedQDMList());
			// Removing the QDS that is being modified from the
			// tempAppliedQDMList.
			Iterator<CQLQualityDataSetDTO> iterator = tempAppliedQDMList
					.iterator();
			while (iterator.hasNext()) {
				CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
				if (qualityDataSetDTO.getUuid().equals(
						matValueSetTransferObject.getCqlQualityDataSetDTO()
								.getUuid())) {
					iterator.remove();
					break;
				}
			}
			matValueSetTransferObject.setAppliedQDMList(tempAppliedQDMList);

			if (!isDuplicate(matValueSetTransferObject, true)) {
				matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
				CQLQualityDataSetDTO qds = matValueSetTransferObject
						.getCqlQualityDataSetDTO();
				MatValueSet matValueSet = matValueSetTransferObject
						.getMatValueSet();
				qds.setOid(matValueSet.getID());
				qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				qds.setCodeListName(matValueSet.getDisplayName());
				if (matValueSet.isGrouping()) {
					qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
				} else {
					qds.setTaxonomy(matValueSet.getCodeSystemName());
				}
				if (matValueSetTransferObject.isVersion()) {
					qds.setVersion(matValueSetTransferObject.getMatValueSet()
							.getVersion());
				} else {
					qds.setVersion("1.0");
				}
				if (matValueSetTransferObject.isExpansionProfile()) {
					qds.setExpansionIdentifier(matValueSetTransferObject
							.getMatValueSet().getExpansionProfile());
				} else {
					qds.setExpansionIdentifier(null);
				}
				CQLQualityDataModelWrapper wrapper = modifyAppliedElementList(
						qds,
						(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject
								.getAppliedQDMList());
				
				result.setSuccess(true);
				result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper
						.getQualityDataDTO()));
				result.setCqlQualityDataSetDTO(qds);
			} else {
				matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		return result;
	}
	
	/**
	 * Update user define QDM in element look up.
	 *
	 * @param matValueSetTransferObject the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult updateUserDefineValuesetInCQLLookUp(
			CQLValueSetTransferObject matValueSetTransferObject) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		QDMInputValidator validator = new QDMInputValidator();
		List<String> messageList = new ArrayList<String>();
		validator.validate(matValueSetTransferObject);
		if (messageList.size() == 0) {
			if (!isDuplicate(matValueSetTransferObject, false)) {
				ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
				wrapper.setQualityDataDTO(qdsList);
				CQLQualityDataSetDTO qds = matValueSetTransferObject
						.getCqlQualityDataSetDTO();
				qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
				qds.setId(UUID.randomUUID().toString());
				qds.setCodeListName(matValueSetTransferObject
						.getCodeListSearchDTO().getName());
				qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
				qds.setVersion("1.0");
				wrapper = modifyAppliedElementList(
						qds,
						(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject
								.getAppliedQDMList());
				String qdmXMLString = addCQLAppliedQDMInMeasureXML(wrapper);
				result.setSuccess(true);
				result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper
						.getQualityDataDTO()));
				result.setCqlString(qdmXMLString);
				result.setCqlQualityDataSetDTO(qds);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}



	
	/**
	 * Adds the new applied QDM in measure XML.
	 *
	 * @param qualityDataSetDTOWrapper the quality data set DTO wrapper
	 * @return the string
	 */
	private String addCQLAppliedQDMInMeasureXML(
			final CQLQualityDataModelWrapper qualityDataSetDTOWrapper) {
		logger.info("addNewAppliedQDMInMeasureXML Method Call Start.");
		ByteArrayOutputStream stream = createNewXML(qualityDataSetDTOWrapper);
		int startIndex = stream.toString().indexOf("<valueset ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex,
				lastIndex + 2);
		logger.debug("addNewAppliedQDMInMeasureXML Method Call xmlString :: "
				+ xmlString);
		return xmlString;
	}
	
	/**
	 * Creates the new XML.
	 *
	 * @param qualityDataSetDTO the quality data set DTO
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream createNewXML(
			final CQLQualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In ManageCodeLiseServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
					.getResourceAsURL("ValueSetsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.debug("Marshalling of QualityDataSetDTO is successful.."
					+ stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load ValueSetsMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting ManageCodeLiseServiceImpl.createXml()");
		return stream;
	}
	
	/**
	 * Modify applied element list.
	 *
	 * @param dataSetDTO the data set DTO
	 * @param appliedQDM the applied QDM
	 * @return the CQL quality data model wrapper
	 */
	private CQLQualityDataModelWrapper modifyAppliedElementList(
			final CQLQualityDataSetDTO dataSetDTO,
			final ArrayList<CQLQualityDataSetDTO> appliedQDM) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		Iterator<CQLQualityDataSetDTO> iterator = appliedQDM.iterator();
		while (iterator.hasNext()) {
			CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(dataSetDTO.getUuid())) {
				CQLQualityDataSetDTO qdm = qualityDataSetDTO;
				iterator.remove();
				break;
			}
		}
		appliedQDM.add(dataSetDTO);
		wrapper.setQualityDataDTO(appliedQDM);
		return wrapper;
	}
	
	
	/**
	 * Populated old QDM.
	 *
	 * @param oldQdm the old qdm
	 * @param qualityDataSetDTO the quality data set DTO
	 */
	private void populatedOldQDM(CQLQualityDataSetDTO oldQdm,
		CQLQualityDataSetDTO qualityDataSetDTO) {
		oldQdm.setCodeListName(qualityDataSetDTO.getCodeListName());
		oldQdm.setOid(qualityDataSetDTO.getOid());
		oldQdm.setUuid(qualityDataSetDTO.getUuid());
		oldQdm.setVersion(qualityDataSetDTO.getVersion());
		oldQdm.setExpansionIdentifier(qualityDataSetDTO
				.getExpansionIdentifier());
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.service.CQLService#getCQLValusets(java.lang.String)
	 */
	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureId, CQLQualityDataModelWrapper cqlQualityDataModelWrapper) {
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		CQLModel cqlModel = new CQLModel();
		XmlProcessor measureXMLProcessor = new XmlProcessor(xmlModel.getXml());
		String cqlLookUpXMLString = measureXMLProcessor.getXmlByTagName("cqlLookUp");
		if(StringUtils.isNotBlank(cqlLookUpXMLString)){
			CQLUtilityClass.getValueSet(cqlModel, cqlLookUpXMLString);
		}
		
		List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs = CQLUtilityClass.sortCQLQualityDataSetDto(getCQLData(measureId).getCqlModel().getAllValueSetList());
		cqlQualityDataModelWrapper.setQualityDataDTO(cqlQualityDataSetDTOs);
		
		return cqlQualityDataModelWrapper;
	}
}
