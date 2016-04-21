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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.xpath.XPathExpressionException;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.cqlworkspace.CQLWorkSpaceConstants;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLDAO;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.clause.CQLData;
import mat.model.cql.CQLDataModel;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryModel;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.cqlparser.CQLErrorListener;
import mat.server.cqlparser.CQLTemplateXML;
import mat.server.cqlparser.MATCQLListener;
import mat.server.cqlparser.cqlLexer;
import mat.server.cqlparser.cqlParser;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

// TODO: Auto-generated Javadoc
/**
 * The Class CQLServiceImpl.
 */
public class CQLServiceImpl implements CQLService {
	
	/** The cql dao. */
	@Autowired
	private CQLDAO cqlDAO;
	
	/** The measure library service. */
	@Autowired
	private MeasureLibraryService measureLibraryService;
	
	/** The context. */
	@Autowired
	private ApplicationContext context;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLServiceImpl.class);
	
	/** The Constant PATIENT. */
	private static final String PATIENT = "patient";
	
	/** The Constant POPULATION. */
	private static final String POPULATION = "population";
	
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
		
		// CQLValidationResult result = new CQLValidationResult();
		CQLModel cqlModel = new CQLModel();
		cqlLexer lexer = new cqlLexer(new ANTLRInputStream(cqlBuilder));
		System.out.println(cqlBuilder);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		cqlParser parser = new cqlParser(tokens);
		CQLErrorListener cqlErrorListener = new CQLErrorListener();
		MATCQLListener cqlListener = new MATCQLListener();
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
		
		// if(cqlErrorListener.getErrors().size() != 0){
		// result.setValid(false);
		// result.setErrorList(cqlErrorListener.getErrors());
		// } else {
		// result.setValid(true);
		// }
		
		// tree.inspect(parser);
		cqlModel = cqlListener.getCqlModel();
		
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
		boolean isDuplicate = false;
		String XPATH_EXPRESSION_FUNCTIONS = "/measure/cqlLookUp/functions";
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				
				if (!toBeModifiedObj.getFunctionName().equalsIgnoreCase(
						currentObj.getFunctionName())) {
					
					isDuplicate = checkForCQLKeywords(currentObj
							.getFunctionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(
							currentObj.getFunctionName(), measureId);
				}
				
				if (!isDuplicate) {
					
					// validation for argument name to check if it is not a
					// keyword.
					result = checkIfKeywordForFuncArguments(result, currentObj);
					isDuplicate = result.isSuccess();
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
						result.setFunction(currentObj);
						return result;
					}
					
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
				isDuplicate = checkForCQLKeywords(currentObj.getFunctionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_KEYWORD);
					return result;
				}
				isDuplicate = isDuplicateIdentifierName(
						currentObj.getFunctionName(), measureId);
				if (!isDuplicate) {
					
					// validation for argument name to check if it is not a
					// keyword.
					result = checkIfKeywordForFuncArguments(result, currentObj);
					isDuplicate = result.isSuccess();
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
						result.setFunction(currentObj);
						return result;
					}
					
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
		boolean isDuplicate = false;
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				
				if (!toBeModifiedObj.getParameterName().equalsIgnoreCase(
						currentObj.getParameterName())) {
					
					isDuplicate = checkForCQLKeywords(currentObj
							.getParameterName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
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
				isDuplicate = checkForCQLKeywords(currentObj.getParameterName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_KEYWORD);
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
		boolean isDuplicate = false;
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				
				// if the modified Name and current Name are not same
				if (!toBeModifiedObj.getDefinitionName().equalsIgnoreCase(
						currentObj.getDefinitionName())) {
					
					isDuplicate = checkForCQLKeywords(currentObj
							.getDefinitionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(result.NAME_NOT_KEYWORD);
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
							xmlModel.setXml(processor.transform(processor
									.getOriginalDoc()));
							getService().saveMeasureXml(xmlModel);
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
				isDuplicate = checkForCQLKeywords(currentObj
						.getDefinitionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(result.NAME_NOT_KEYWORD);
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.CQLService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLData(String measureId) {
		
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		cqlModel = getCQLGeneratlInfo(measureId);
		result.setCqlModel(cqlModel);
		QualityDataModelWrapper valuesetWrapper = measureLibraryService.getAppliedQDMFromMeasureXml(measureId,true);
		List<CQLQualityDataSetDTO> cqlDataSet = new ArrayList<CQLQualityDataSetDTO>();
		cqlDataSet = convertToCQLQualityDataSetDTO(valuesetWrapper.getQualityDataDTO());
		result.getCqlModel().setValueSetList(cqlDataSet);
		CQLDefinitionsWrapper defineWrapper = getCQLDefinitionsFromMeasureXML(measureId);
		result.getCqlModel().setDefinitionList(
				defineWrapper.getCqlDefinitions());
		CQLParametersWrapper paramWrapper = getCQLParametersFromMeasureXML(measureId);
		result.getCqlModel().setCqlParameters(
				paramWrapper.getCqlParameterList());
		CQLFunctionsWrapper functionWrapper = getCQLFunctionsFromMeasureXML(measureId);
		result.getCqlModel().setCqlFunctions(
				functionWrapper.getCqlFunctionsList());
		return result;
	}
	
	private List<CQLQualityDataSetDTO> convertToCQLQualityDataSetDTO(List<QualityDataSetDTO> qualityDataSetDTO){
		List<CQLQualityDataSetDTO> convertedCQLDataSetList = new ArrayList<CQLQualityDataSetDTO>();
		
		for (QualityDataSetDTO tempDataSet : qualityDataSetDTO) {
			CQLQualityDataSetDTO convertedCQLDataSet = new CQLQualityDataSetDTO();
				convertedCQLDataSet.setCodeListName(tempDataSet.getCodeListName());
				convertedCQLDataSet.setCodeSystemName(tempDataSet.getCodeSystemName());
				convertedCQLDataSet.setDataType(tempDataSet.getDataType());
				convertedCQLDataSet.setId(tempDataSet.getId());
				convertedCQLDataSet.setOid(tempDataSet.getOid());
				convertedCQLDataSet.setSuppDataElement(tempDataSet.isSuppDataElement());
				convertedCQLDataSet.setTaxonomy(tempDataSet.getTaxonomy());
				convertedCQLDataSet.setType(tempDataSet.getType());
				convertedCQLDataSet.setUuid(tempDataSet.getUuid());
				convertedCQLDataSet.setVersion(tempDataSet.getVersion());
				convertedCQLDataSetList.add(convertedCQLDataSet);
			
		}
		return convertedCQLDataSetList;
		
	}
	
	/**
	 * Gets the CQL generatl info.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the CQL generatl info
	 */
	private CQLModel getCQLGeneratlInfo(String measureId) {
		
		String libraryNameStr = "";
		String usingModelStr = "";
		CQLModel cqlModel = new CQLModel();
		CQLLibraryModel libraryModel = new CQLLibraryModel();
		CQLDataModel usingModel = new CQLDataModel();
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		
		if (xmlModel != null) {
			
			XmlProcessor processor = new XmlProcessor(xmlModel.getXml());
			String XPATH_EXPRESSION_CQLLOOKUP_lIBRARY = "/measure/cqlLookUp/library/text()";
			String XPATH_EXPRESSION_CQLLOOKUP_USING = "/measure/cqlLookUp/usingModel/text()";
			
			try {
				
				Node nodeCQLLibrary = processor.findNode(
						processor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_lIBRARY);
				Node nodeCQLUsingModel = processor.findNode(
						processor.getOriginalDoc(),
						XPATH_EXPRESSION_CQLLOOKUP_USING);
				
				if (nodeCQLLibrary != null) {
					libraryNameStr = nodeCQLLibrary.getTextContent();
					libraryModel.setLibraryName(libraryNameStr);
					/* libraryModel.setVersionUsed("2"); */
				}
				
				if (nodeCQLUsingModel != null) {
					usingModelStr = nodeCQLUsingModel.getTextContent();
					usingModel.setName(usingModelStr);
				}
				
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
		}
		
		cqlModel.setLibrary(libraryModel);
		cqlModel.setUsedModel(usingModel);
		return cqlModel;
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
	 * Gets the CQL parameters from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the CQL parameters from measure xml
	 */
	private CQLParametersWrapper getCQLParametersFromMeasureXML(String measureId) {
		
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		CQLParametersWrapper wrapper = null;
		if (measureXmlModel != null) {
			wrapper = convertXmltoCQLParameterModel(measureXmlModel);
			if ((wrapper.getCqlParameterList() != null)
					&& (wrapper.getCqlParameterList().size() > 0)) {
				sortParametersList(wrapper.getCqlParameterList());
			}
		}
		return wrapper;
	}
	
	/**
	 * Gets the CQL parameters from measure xml.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the CQL parameters from measure xml
	 */
	private CQLFunctionsWrapper getCQLFunctionsFromMeasureXML(String measureId) {
		
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(
				measureId);
		CQLFunctionsWrapper wrapper = null;
		if (measureXmlModel != null) {
			wrapper = convertXmltoCQLFunctionModel(measureXmlModel);
			if ((wrapper.getCqlFunctionsList() != null)
					&& (wrapper.getCqlFunctionsList().size() > 0)) {
				sortFunctionssList(wrapper.getCqlFunctionsList());
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
	private StringBuilder getCqlString(CQLModel cqlModel) {
		
		StringBuilder cqlStr = new StringBuilder();
		
		// library Name
		if (cqlModel.getLibrary() != null) {
			cqlStr = cqlStr.append("library "
					+ cqlModel.getLibrary().getLibraryName());
			if (cqlModel.getLibrary().getVersionUsed() != null) {
				cqlStr = cqlStr.append("version "
						+ cqlModel.getLibrary().getVersionUsed());
			}
			cqlStr = cqlStr.append("\n\n");
		}
		
		// Using
		cqlStr = cqlStr.append("using QDM");
		cqlStr = cqlStr.append("\n\n");
		
		
		//Valuesets
		List<CQLQualityDataSetDTO> valueSetList = cqlModel.getValueSetList();
		if (valueSetList != null) {
			for (CQLQualityDataSetDTO valueset : valueSetList) {
				cqlStr = cqlStr.append("valueset "
						+ valueset.getQDMElement() + ":"
						+ valueset.getOid());
						
				cqlStr = cqlStr.append("\n\n");
			}
		}
		
		// parameters
		List<CQLParameter> paramList = cqlModel.getCqlParameters();
		if (paramList != null) {
			for (CQLParameter parameter : paramList) {
				cqlStr = cqlStr.append("parameter "
						+ parameter.getParameterName() + " "
						+ parameter.getParameterLogic());
				cqlStr = cqlStr.append("\n\n");
			}
		}
		
		// Definitions and Functions by Context
		cqlStr = getDefineAndFunctionsByContext(cqlModel.getDefinitionList(),
				cqlModel.getCqlFunctions(), cqlStr);
		
		return cqlStr;
	}
	
	/**
	 * Gets the define and funcs by context.
	 * 
	 * @param defineList
	 *            the define list
	 * @param functionsList
	 *            the functions list
	 * @param cqlStr
	 *            the cql str
	 * @return the define and funcs by context
	 */
	private StringBuilder getDefineAndFunctionsByContext(
			List<CQLDefinition> defineList, List<CQLFunctions> functionsList,
			StringBuilder cqlStr) {
		
		List<CQLDefinition> contextPatDefineList = new ArrayList<CQLDefinition>();
		List<CQLDefinition> contextPopDefineList = new ArrayList<CQLDefinition>();
		List<CQLFunctions> contextPatFuncList = new ArrayList<CQLFunctions>();
		List<CQLFunctions> contextPopFuncList = new ArrayList<CQLFunctions>();
		
		if (defineList != null) {
			for (int i = 0; i < defineList.size(); i++) {
				if (defineList.get(i).getContext().equalsIgnoreCase(PATIENT)) {
					contextPatDefineList.add(defineList.get(i));
				} else {
					contextPopDefineList.add(defineList.get(i));
				}
			}
		}
		if (functionsList != null) {
			for (int i = 0; i < functionsList.size(); i++) {
				if (functionsList.get(i).getContext().equalsIgnoreCase(PATIENT)) {
					contextPatFuncList.add(functionsList.get(i));
				} else {
					contextPopFuncList.add(functionsList.get(i));
				}
			}
		}
		
		if ((contextPatDefineList.size() > 0) || (contextPatFuncList.size() > 0)) {
			
			getDefineAndFunctionsByContext(contextPatDefineList,
					contextPatFuncList, PATIENT, cqlStr);
		}
		
		if ((contextPopDefineList.size() > 0) || (contextPopFuncList.size() > 0)) {
			
			getDefineAndFunctionsByContext(contextPopDefineList,
					contextPopFuncList, POPULATION, cqlStr);
		}
		
		return cqlStr;
		
	}
	
	/**
	 * Gets the define and functions by context.
	 * 
	 * @param definitionList
	 *            the definition list
	 * @param functionsList
	 *            the functions list
	 * @param context
	 *            the context
	 * @param cqlStr
	 *            the cql str
	 * @return the define and functions by context
	 */
	private StringBuilder getDefineAndFunctionsByContext(
			List<CQLDefinition> definitionList,
			List<CQLFunctions> functionsList, String context,
			StringBuilder cqlStr) {
		
		cqlStr = cqlStr.append("context").append(" " + context).append("\n\n");
		for (CQLDefinition definition : definitionList) {
			cqlStr = cqlStr.append("define " + definition.getDefinitionName()
					+ ": ");
			cqlStr = cqlStr.append(definition.getDefinitionLogic());
			cqlStr = cqlStr.append("\n\n");
		}
		
		for (CQLFunctions function : functionsList) {
			cqlStr = cqlStr.append("define function "
					+ function.getFunctionName() + "(");
			if(function.getArgumentList()!=null) {
			for (CQLFunctionArgument argument : function.getArgumentList()) {
				StringBuilder argumentType = new StringBuilder();
				if (argument.getArgumentType().toString()
						.equalsIgnoreCase("QDM Datatype")) {
					argumentType = argumentType.append("\"").append(
							argument.getQdmDataType());
					if (argument.getAttributeName() != null) {
						argumentType = argumentType.append(".")
								.append(argument.getAttributeName());
					}
					argumentType = argumentType.append("\"");
				} else if (argument
						.getArgumentType()
						.toString()
						.equalsIgnoreCase(
								CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
					argumentType = argumentType.append(argument.getOtherType());
				} else {
					argumentType = argumentType.append(argument
							.getArgumentType());
				}
				cqlStr = cqlStr.append(argument.getArgumentName() + " "
						+ argumentType + ", ");
			}
			cqlStr.deleteCharAt(cqlStr.length() - 2);
		}
			
			cqlStr = cqlStr.append("): " + function.getFunctionLogic());
			cqlStr = cqlStr.append("\n\n");
		}
		
		return cqlStr;
	}
	
	/**
	 * Check for cql keywords.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	private boolean checkForCQLKeywords(String name) {
		
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
	}
	
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
	 * @param result
	 *            the result
	 * @param currentObj
	 *            the current obj
	 * @return the save update cql result
	 */
	private SaveUpdateCQLResult checkIfKeywordForFuncArguments(
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
	}
	
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
	
}
