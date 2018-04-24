package mat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cqframework.cql.cql2elm.CQLtoELM;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.tools.formatter.CQLFormatter;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.CQLService;
import mat.client.shared.MatException;
import mat.client.shared.ValueSetNameInputValidator;
import mat.dao.clause.CQLDAO;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.CQLValueSetTransferObject;
import mat.model.MatCodeTransferObject;
import mat.model.MatValueSet;
import mat.model.clause.CQLData;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.model.cql.CQLCodeSystemWrapper;
import mat.model.cql.CQLCodeWrapper;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLDefinitionsWrapper;
import mat.model.cql.CQLFunctionArgument;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLFunctionsWrapper;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLIncludeLibraryWrapper;
import mat.model.cql.CQLKeywords;
import mat.model.cql.CQLLibraryAssociation;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLParametersWrapper;
import mat.model.cql.CQLQualityDataModelWrapper;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.server.cqlparser.CQLTemplateXML;
import mat.server.service.MeasurePackageService;
import mat.server.service.impl.MatContextServiceUtil;
import mat.server.util.CQLUtil;
import mat.server.util.CQLUtil.CQLArtifactHolder;
import mat.server.util.ResourceLoader;
import mat.server.util.XmlProcessor;
import mat.shared.CQLErrors;
import mat.shared.CQLExpressionObject;
import mat.shared.CQLModelValidator;
import mat.shared.CQLObject;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.LibHolderObject;
import mat.shared.SaveUpdateCQLResult;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * The Class CQLServiceImpl.
 */
public class CQLServiceImpl implements CQLService {
	
	/** The cql dao. */
	@Autowired
	private CQLDAO cqlDAO;

	/** The cql library dao. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;

	/** The context. */
	@Autowired
	private ApplicationContext context;

	/** The cql library association DAO. */
	@Autowired
	private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CQLServiceImpl.class);

	/** The cql supplemental definition XML string. */
	private String cqlSupplementalDefinitionXMLString =

			"<supplementalDefinitions>"

					+ "<definition context=\"Patient\" name=\"SDE Ethnicity\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\"> "
					+ "<logic>[\"Patient Characteristic Ethnicity\": \"Ethnicity\"]</logic> " + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Payer\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\" id=\"999\">  "
					+ "<logic>[\"Patient Characteristic Payer\": \"Payer\"]</logic>" + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Race\" "
					+ " supplDataElement=\"true\" popDefinition=\"false\"  id=\"999\"> "
					+ "<logic>[\"Patient Characteristic Race\": \"Race\"]</logic>" + "</definition>"

					+ "<definition context=\"Patient\" name=\"SDE Sex\" "
					+ " supplDataElement=\"true\"  popDefinition=\"false\" id=\"999\">  "
					+ "<logic>[\"Patient Characteristic Sex\": \"ONC Administrative Sex\"]</logic>" + "</definition>"

					+ "</supplementalDefinitions>";

	/** The cql default code system XML string. */
	private String cqlDefaultCodeSystemXMLString =

			"<codeSystems>"

					+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" "
					+ "codeSystemVersion=\"2.46\" id=\"777\" " + "/>"

					+ "<codeSystem codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMEDCT\" "
					+ "codeSystemVersion=\"2016-03\" id=\"777\" " + "/>"

					+ "</codeSystems>";

	/** The cql default code system XML string. */
	private String cqlDefaultCodesXMLString =

			"<codes>"

					+ "<code codeName=\"Birthdate\" codeOID=\"21112-8\" codeSystemName=\"LOINC\" "
					+ "displayName=\"Birth date\" " + "codeSystemVersion=\"2.46\" id=\"777\" isCodeSystemVersionIncluded =\"false\"" + "/>"

					+ "<code codeName=\"Dead\" codeOID=\"419099009\" codeSystemName=\"SNOMEDCT\" "
					+ "displayName=\"Dead\" " + "codeSystemVersion=\"2016-03\" id=\"777\" isCodeSystemVersionIncluded =\"false\" " + "/>"

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
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyCQLGeneralInfo(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String xml, String libraryName) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if (xml != null) {

			XmlProcessor processor = new XmlProcessor(xml);
			String XPATH_EXPRESSION_CQLLOOKUP_LIBRARY = "//cqlLookUp/library";
			try {
				Node libraryNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_LIBRARY);
				if (libraryNode != null) {
					libraryNode.setTextContent(libraryName);

					result.setXml(processor.transform(processor.getOriginalDoc()));
					result.setSuccess(true);
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			result.setSuccess(true);
			result.getCqlModel().setLibraryName(libraryName);
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	private List<String> getExpressionListFromCqlModel(CQLModel cqlModel) {
		List<String> expressionList = new ArrayList<>();

		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			expressionList.add(cqlDefinition.getDefinitionName());
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			expressionList.add(cqlFunction.getFunctionName());
		}

		return expressionList;

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
	public SaveUpdateCQLResult saveAndModifyFunctions(String xml, CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionsList, boolean isFormatable) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		CQLModelValidator validator = new CQLModelValidator();
		boolean isDuplicate = false;
		boolean isCommentInvalid = false;

		String XPATH_EXPRESSION_FUNCTIONS = "//cqlLookUp/functions";
		if (xml != null && !xml.isEmpty()) {

			XmlProcessor processor = new XmlProcessor(xml);
			if (toBeModifiedObj != null) {
				currentObj.setId(toBeModifiedObj.getId());
				if (!toBeModifiedObj.getFunctionName().equalsIgnoreCase(currentObj.getFunctionName())) {

					isDuplicate = validator.validateForSpecialChar(currentObj.getFunctionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(currentObj.getFunctionName(), xml);
				}

				//validating function comment string
				isCommentInvalid = validator.validateForCommentTextArea(toBeModifiedObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}

				if (!isDuplicate) {
					boolean isValidArgumentName = true;
					if (currentObj.getArgumentList() != null && currentObj.getArgumentList().size() > 0) {
						for (CQLFunctionArgument argument : currentObj.getArgumentList()) {
							isValidArgumentName = validator.validateForAliasNameSpecialChar(argument.getArgumentName());
							if (!isValidArgumentName) {
								break;
							}
						}
					}
					if (isValidArgumentName) {
						logger.debug(" CQLServiceImpl: saveAndModifyFunctions Start :  ");

						try {
							String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='"
									+ toBeModifiedObj.getId() + "']";
							Node nodeFunction = processor.findNode(processor.getOriginalDoc(),
									XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

							if (nodeFunction != null) {
								// Server Side check to see if Function is used
								// and context is changed from developer's tool
								// since if function is used we disable the
								// context radio buttons then only allow to
								// modify
								// if either cql is invalid or function is not
								// used.
								String oldExpressionName = nodeFunction.getAttributes().getNamedItem("name")
										.getNodeValue();
								String oldExpressionLogic = nodeFunction.getChildNodes().item(0).getTextContent();
								String oldContextValue = nodeFunction.getAttributes().getNamedItem("context")
										.getNodeValue();
								if (MatContextServiceUtil.get().isMeasure()
										&& !oldContextValue.equalsIgnoreCase(currentObj.getContext())) {
									parseCQLExpressionForErrors(result, xml,
											"define function" + " \"" + oldExpressionName + "\"", oldExpressionLogic,
											oldExpressionName, "Function");
									if (result.getUsedCQLArtifacts().getUsedCQLFunctions()
											.contains(oldExpressionName)) {
										currentObj.setContext(oldContextValue);
									}

								}

								processor.removeFromParent(nodeFunction);
								String cqlString = createFunctionsXML(currentObj);
								processor.appendNode(cqlString, "function", XPATH_EXPRESSION_FUNCTIONS);

								String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
								result.setXml(finalUpdatedXmlString);
								processor.setOriginalXml(finalUpdatedXmlString);

								String cqlExpressionName = "define function" + " \"" + currentObj.getFunctionName() + "\"";
								parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
										currentObj.getFunctionLogic(), currentObj.getFunctionName(), "Function");

								// if the function has no errors, get the result type and format it
								if(result.getCqlErrors().isEmpty()) {

									CQLExpressionObject obj = findExpressionObject(currentObj.getFunctionName(), result.getCqlObject().getCqlFunctionObjectList());
									if(obj != null){
										currentObj.setReturnType(obj.getReturnType());
									}

									// format function
									if(isFormatable) {
										currentObj.setFunctionLogic(formatFunction(currentObj));

										XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='"
												+ currentObj.getId() + "']";
										nodeFunction = processor.findNode(processor.getOriginalDoc(),
												XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

										if(nodeFunction != null) {
											NodeList functionChildNodes = nodeFunction.getChildNodes();
											for(int i = 0; i < functionChildNodes.getLength(); i++) {
												Node currentChildNode = functionChildNodes.item(i);

												if(currentChildNode.getNodeName().equalsIgnoreCase("logic")) {
													currentChildNode.setTextContent(currentObj.getFunctionLogic());
													break;
												}
											}
										}


										finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
										result.setXml(finalUpdatedXmlString);
									}

								}
								result.setCqlObject(null);
								wrapper = modfiyCQLFunctionList(toBeModifiedObj, currentObj, functionsList);

								result.setSuccess(true);
								result.setFunction(currentObj);
							} else {
								result.setSuccess(false);
								result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
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
						result.setFailureReason(SaveUpdateCQLResult.FUNCTION_ARGUMENT_INVALID);
					}

				} else {

					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}

			} else {

				currentObj.setId(UUID.randomUUID().toString());
				isDuplicate = validator.validateForSpecialChar(currentObj.getFunctionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}
				isDuplicate = isDuplicateIdentifierName(currentObj.getFunctionName(), xml);

				//validating function comment string
				isCommentInvalid = validator.validateForCommentTextArea(currentObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}

				if (!isDuplicate) {

					boolean isValidArgumentName = true;
					if (currentObj.getArgumentList() != null && currentObj.getArgumentList().size() > 0) {
						for (CQLFunctionArgument argument : currentObj.getArgumentList()) {
							isValidArgumentName = validator.validateForAliasNameSpecialChar(argument.getArgumentName());
							if (!isValidArgumentName) {
								break;
							}
						}
					}
					if (isValidArgumentName) {

						try {
							String cqlString = createFunctionsXML(currentObj);
							Node nodeFunctions = processor.findNode(processor.getOriginalDoc(),
									XPATH_EXPRESSION_FUNCTIONS);

							if (nodeFunctions != null) {
								try {

									processor.appendNode(cqlString, "function", XPATH_EXPRESSION_FUNCTIONS);
									processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));

									String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
									result.setXml(finalUpdatedXmlString);
									processor.setOriginalXml(finalUpdatedXmlString);

									String cqlExpressionName = "define function" + " \"" + currentObj.getFunctionName()
									+ "\"";
									parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
											currentObj.getFunctionLogic(), currentObj.getFunctionName(), "Function");

									// if the function has no errors, get the result type and format it
									if(result.getCqlErrors().isEmpty()) {
										CQLExpressionObject obj = findExpressionObject(currentObj.getFunctionName(), result.getCqlObject().getCqlFunctionObjectList());
										if(obj != null){
											currentObj.setReturnType(obj.getReturnType());
										}

										// format function
										if(isFormatable) {
											currentObj.setFunctionLogic(formatFunction(currentObj));

											String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='"
													+ currentObj.getId() + "']";
											Node nodeFunction = processor.findNode(processor.getOriginalDoc(),
													XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

											if(nodeFunction != null) {
												NodeList functionChildNodes = nodeFunction.getChildNodes();
												for(int i = 0; i < functionChildNodes.getLength(); i++) {
													Node currentChildNode = functionChildNodes.item(i);

													if(currentChildNode.getNodeName().equalsIgnoreCase("logic")) {
														currentChildNode.setTextContent(currentObj.getFunctionLogic());
														break;
													}
												}
											}

											finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
											result.setXml(finalUpdatedXmlString);
										}
									}
									result.setCqlObject(null);
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
								result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
							}
						} catch (Exception e) {
							result.setSuccess(false);
							e.printStackTrace();
						}
					} else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.FUNCTION_ARGUMENT_INVALID);
					}

				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlFunctionsList().size() > 0)) {
			result.getCqlModel().setCqlFunctions(sortFunctionssList(wrapper.getCqlFunctionsList()));
		}

		return result;
	}

	/**
	 * Builds a temporary function exprsesion string by taking the name, arguments, and logic, then formats the logic, and returns the formatted logic.
	 * @param function the function to format
	 * @return the formatted function logic
	 * @throws IOException
	 */
	private String formatFunction(CQLFunctions function) throws IOException {
		// create argument string and then format the cql expression

		StringBuilder argumentBuilder = new StringBuilder();

		argumentBuilder.append("(");
		for(int i = 0; i < function.getArgumentList().size(); i++) {
			CQLFunctionArgument argument = function.getArgumentList().get(i);

			argumentBuilder.append(argument.getArgumentName() + " " + argument.getArgumentType() + ", ");
		}
		argumentBuilder.append(")");

		String definitionStatement = "define" + " \"" + function.getFunctionName() + argumentBuilder.toString()  + "\":";
		String tempFunctionString =  "define" + " \"" + function.getFunctionName() + argumentBuilder.toString()  + "\":\n" + function.getFunctionLogic();

		String functionLogic = "";
		if(function.getFunctionLogic() != null && !function.getFunctionLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter();
			String formattedFunction = formatter.format(tempFunctionString);
			functionLogic = parseOutBody(formattedFunction, definitionStatement);
		}

		return functionLogic;


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
	public SaveUpdateCQLResult saveAndModifyParameters(String xml, CQLParameter toBeModifiedObj,
			CQLParameter currentObj, List<CQLParameter> parameterList, boolean isFormatable) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		CQLModelValidator validtor = new CQLModelValidator();
		boolean isDuplicate = false;
		boolean isCommentInvalid = false;
		String XPATH_EXPRESSION_PARAMETERS = "//cqlLookUp/parameters";
		if (xml != null && !xml.isEmpty()) {

			XmlProcessor processor = new XmlProcessor(xml);
			if (toBeModifiedObj != null) {

				if (toBeModifiedObj.isReadOnly()) {
					return null;
				}
				currentObj.setId(toBeModifiedObj.getId());
				if (!toBeModifiedObj.getParameterName().equalsIgnoreCase(currentObj.getParameterName())) {

					isDuplicate = validtor.validateForSpecialChar(currentObj.getParameterName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(currentObj.getParameterName(), xml);
				}

				//validating parameter comment
				isCommentInvalid = validtor.validateForCommentTextArea(toBeModifiedObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}

				if (!isDuplicate) {
					logger.debug(" CQLServiceImpl: saveAndModifyParameters Start :  ");

					try {
						String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='"
								+ toBeModifiedObj.getId() + "']";
						Node nodeParameter = processor.findNode(processor.getOriginalDoc(),
								XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

						if (nodeParameter != null) {


							String cqlString = createParametersXML(currentObj);
							processor.removeFromParent(nodeParameter);
							processor.appendNode(cqlString, "parameter", XPATH_EXPRESSION_PARAMETERS);

							String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
							result.setXml(finalUpdatedXmlString);
							processor.setOriginalXml(finalUpdatedXmlString);

							String cqlExpressionName = "parameter" + " \"" + currentObj.getParameterName() + "\"";
							parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
									currentObj.getParameterLogic(), currentObj.getParameterName(), "Parameter");

							// if the parameter has no errors, format it
							if(result.getCqlErrors().isEmpty()) {
								if(isFormatable) {
									currentObj.setParameterLogic(formatParameter(currentObj));

									XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='"
											+ currentObj.getId() + "']";
									nodeParameter = processor.findNode(processor.getOriginalDoc(),
											XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

									if(nodeParameter != null) {
										NodeList parameterChildNodelist = nodeParameter.getChildNodes();

										for(int i = 0 ; i < parameterChildNodelist.getLength(); i++) {
											Node currentChildNode = parameterChildNodelist.item(i);

											if(currentChildNode.getNodeName().equalsIgnoreCase("logic")) {
												currentChildNode.setTextContent(currentObj.getParameterLogic());
												break;
											}
										}
									}

									finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
									result.setXml(finalUpdatedXmlString);
								}
							}

							result.setCqlObject(null);
							wrapper = modfiyCQLParameterList(toBeModifiedObj, currentObj, parameterList);
							result.setSuccess(true);
							result.setParameter(currentObj);
						} else {
							result.setSuccess(false);
							result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();

					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.debug(" CQLServiceImpl: saveAndModifyParameters End :  ");

				} else {

					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}

			} else {

				currentObj.setId(UUID.randomUUID().toString());

				isDuplicate = validtor.validateForSpecialChar(currentObj.getParameterName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}
				isDuplicate = isDuplicateIdentifierName(currentObj.getParameterName(), xml);

				//validating parameter comment String
				isCommentInvalid = validtor.validateForCommentTextArea(currentObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}

				if (!isDuplicate) {
					try {
						String cqlString = createParametersXML(currentObj);

						Node nodeParameters = processor.findNode(processor.getOriginalDoc(),
								XPATH_EXPRESSION_PARAMETERS);
						if (nodeParameters != null) {
							try {
								processor.appendNode(cqlString, "parameter", XPATH_EXPRESSION_PARAMETERS);
								processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));

								String finalUpdatedString = processor.transform(processor.getOriginalDoc());
								result.setXml(finalUpdatedString);
								processor.setOriginalXml(finalUpdatedString);

								String cqlExpressionName = "parameter" + " \"" + currentObj.getParameterName() + "\"";
								parseCQLExpressionForErrors(result, finalUpdatedString, cqlExpressionName,
										currentObj.getParameterLogic(), currentObj.getParameterName(), "Parameter");

								// if the parameter has no errors, format it
								if(result.getCqlErrors().isEmpty()) {
									if(isFormatable) {
										currentObj.setParameterLogic(formatParameter(currentObj));

										String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='"
												+ currentObj.getId() + "']";
										Node nodeParameter = processor.findNode(processor.getOriginalDoc(),
												XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

										if(nodeParameter != null) {
											NodeList parameterChildNodelist = nodeParameter.getChildNodes();

											for(int i = 0 ; i < parameterChildNodelist.getLength(); i++) {
												Node currentChildNode = parameterChildNodelist.item(i);

												if(currentChildNode.getNodeName().equalsIgnoreCase("logic")) {
													currentChildNode.setTextContent(currentObj.getParameterLogic());
													break;
												}
											}
										}

										finalUpdatedString = processor.transform(processor.getOriginalDoc());
										result.setXml(finalUpdatedString);
									}
								}

								result.setCqlObject(null);
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
							result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
						}
					} catch (Exception e) {
						result.setSuccess(false);
						e.printStackTrace();
					}
				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlParameterList().size() > 0)) {
			result.getCqlModel().setCqlParameters(sortParametersList(wrapper.getCqlParameterList()));
		}

		return result;
	}

	/**
	 * Builds a temporary parameter expressions string, formats the parameter, and returns the formatted logic
	 * @param parameterExpressionString the
	 * @return the formatted parameter logic
	 * @throws IOException
	 */
	private String formatParameter(CQLParameter parameter) throws IOException {

		// format the cql parameter
		String tempParameterString = "parameter" + " \"" + parameter.getParameterName() + "\" " + parameter.getParameterLogic();

		String parameterLogic = "";
		if(parameter.getParameterLogic() != null && !parameter.getParameterLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter(); 
			String formattedParameter = formatter.format(tempParameterString);
			parameterLogic = parseOutParameterBody(formattedParameter, parameter.getParameterName());
		}


		return parameterLogic;
	}

	/**
	 * Parses the body from the parameter expression. Removes the parameter definition statement from the logic and formats nicely for ace editor.
	 * @param cqlExpressionString the parameter expression string in the format of `parameter "ParamName" paramlogic`
	 * @param parameterName the parameter name
	 * @return the parameter body
	 */
	private String parseOutParameterBody(String cqlExpressionString, String parameterName) {

		String parameterDefinitionStatement = "parameter \"" + parameterName + "\"";

		// remove the parameter definition statement
		String expressionBodyString = cqlExpressionString.replace(parameterDefinitionStatement, "");
		expressionBodyString = expressionBodyString.trim();

		return expressionBodyString;

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
	public SaveUpdateCQLResult saveAndModifyDefinitions(String xml, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj, List<CQLDefinition> definitionList, boolean isFormatable) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		CQLModelValidator validator = new CQLModelValidator();
		boolean isDuplicate = false;
		boolean isCommentInvalid = false;

		String XPATH_EXPRESSION_DEFINTIONS = "//cqlLookUp/definitions";
		if (xml != null && !xml.isEmpty()) {

			XmlProcessor processor = new XmlProcessor(xml);
			if (toBeModifiedObj != null) {

				if (toBeModifiedObj.isSupplDataElement()) {
					return null;
				}

				currentObj.setId(toBeModifiedObj.getId());
				// if the modified Name and current Name are not same
				if (!toBeModifiedObj.getDefinitionName().equalsIgnoreCase(currentObj.getDefinitionName())) {

					isDuplicate = validator.validateForSpecialChar(currentObj.getDefinitionName());
					if (isDuplicate) {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
						return result;
					}
					isDuplicate = isDuplicateIdentifierName(currentObj.getDefinitionName(), xml);
				}

				//validate definition comment string
				isCommentInvalid = validator.validateForCommentTextArea(toBeModifiedObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}

				if (!isDuplicate) {

					logger.debug(" CQLServiceImpl: saveAndModifyDefinitions Start :  ");
					String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='"
							+ toBeModifiedObj.getId() + "']";
					try {
						Node nodeDefinition = processor.findNode(processor.getOriginalDoc(),
								XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);
						if (nodeDefinition != null) {

							// Server Side check to see if Function is used and
							// context is changed from developer's tool
							// since if function is used we disable the context
							// radio buttons then only allow to modify
							// if either cql is invalid or function is not used.
							String oldExpressionName = nodeDefinition.getAttributes().getNamedItem("name")
									.getNodeValue();
							String oldExpressionLogic = nodeDefinition.getChildNodes().item(0).getTextContent();
							String oldContextValue = nodeDefinition.getAttributes().getNamedItem("context")
									.getNodeValue();
							if (MatContextServiceUtil.get().isMeasure()
									&& !oldContextValue.equalsIgnoreCase(currentObj.getContext())) {
								parseCQLExpressionForErrors(result, xml,
										"define" + " \"" + currentObj.getDefinitionName() + "\"", oldExpressionLogic,
										oldExpressionName, "Definition");
								if (result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(oldExpressionName)) {
									currentObj.setContext(oldContextValue);
								}

							}

							// append the definition node and parse for errors
							String cqlString = createDefinitionsXML(currentObj);
							processor.removeFromParent(nodeDefinition);
							processor.appendNode(cqlString, "definition", XPATH_EXPRESSION_DEFINTIONS);
							updateRiskAdjustmentVariables(processor, toBeModifiedObj, currentObj);

							String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
							result.setXml(finalUpdatedXmlString);
							processor.setOriginalXml(finalUpdatedXmlString);

							String cqlExpressionName = "define" + " \"" + currentObj.getDefinitionName() + "\"";
							parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
									currentObj.getDefinitionLogic(), currentObj.getDefinitionName(), "Definition");

							if(result.getCqlErrors().isEmpty()) {
								CQLExpressionObject obj = findExpressionObject(currentObj.getDefinitionName(), result.getCqlObject().getCqlDefinitionObjectList());
								if(obj != null){
									currentObj.setReturnType(obj.getReturnType());
								}

								// format the definition
								if(isFormatable) {
									currentObj.setDefinitionLogic(formatDefinition(currentObj));

									XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='"
											+ currentObj.getId() + "']";
									nodeDefinition = processor.findNode(processor.getOriginalDoc(),
											XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);

									if(nodeDefinition != null) {
										NodeList definitionChildNodes = nodeDefinition.getChildNodes();
										for(int i = 0; i < definitionChildNodes.getLength(); i++) {
											Node currentChildNode = definitionChildNodes.item(i);
											if(currentChildNode.getNodeName().equals("logic")) {
												currentChildNode.setTextContent(currentObj.getDefinitionLogic());
												break;
											}
										}
									}

									finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
									result.setXml(finalUpdatedXmlString);
								}
							}
							
							result.setCqlObject(null);
							wrapper = modfiyCQLDefinitionList(toBeModifiedObj, currentObj, definitionList);

							result.setSuccess(true);
							result.setDefinition(currentObj);
						} else {
							result.setSuccess(false);
							result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
						}
					} catch (XPathExpressionException e) {
						result.setSuccess(false);
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					logger.debug(" CQLServiceImpl: saveAndModifyDefinitions End :  ");
				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}
			} else {
				currentObj.setId(UUID.randomUUID().toString());
				String cqlExpressionName = "define" + " \"" + currentObj.getDefinitionName() + "\"";

				isDuplicate = validator.validateForSpecialChar(currentObj.getDefinitionName());
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}

				isDuplicate = isDuplicateIdentifierName(currentObj.getDefinitionName(), xml);

				//validating definition Comment
				isCommentInvalid = validator.validateForCommentTextArea(currentObj.getCommentString());

				if (isCommentInvalid) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.COMMEENT_INVALID);
					return result;
				}
				if (!isDuplicate) {
					try {
						Node nodeDefinitions = processor.findNode(processor.getOriginalDoc(),
								XPATH_EXPRESSION_DEFINTIONS);
						if (nodeDefinitions != null) {

							String cqlString = createDefinitionsXML(currentObj);

							try {
								processor.appendNode(cqlString, "definition", XPATH_EXPRESSION_DEFINTIONS);
								processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));

								String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
								result.setXml(finalUpdatedXmlString);
								processor.setOriginalXml(finalUpdatedXmlString);

								cqlExpressionName = "define" + " \"" + currentObj.getDefinitionName() + "\"";
								parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
										currentObj.getDefinitionLogic(), currentObj.getDefinitionName(), "Definition");

								if(result.getCqlErrors().isEmpty()) {
									CQLExpressionObject obj = findExpressionObject(currentObj.getDefinitionName(), result.getCqlObject().getCqlDefinitionObjectList());
									if(obj != null){
										currentObj.setReturnType(obj.getReturnType());
									}

									// format the definition
									if(isFormatable) {
										currentObj.setDefinitionLogic(formatDefinition(currentObj));

										String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='"
												+ currentObj.getId() + "']";
										Node nodeDefinition = processor.findNode(processor.getOriginalDoc(),
												XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);

										if(nodeDefinition != null) {
											NodeList definitionChildNodes = nodeDefinition.getChildNodes();
											for(int i = 0; i < definitionChildNodes.getLength(); i++) {
												Node currentChildNode = definitionChildNodes.item(i);
												if(currentChildNode.getNodeName().equals("logic")) {
													currentChildNode.setTextContent(currentObj.getDefinitionLogic());
													break;
												}
											}
										}

										finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
										result.setXml(finalUpdatedXmlString);
									}
								}

								result.setCqlObject(null);
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
							result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
						}

					} catch (Exception e) {
						result.setSuccess(false);
						e.printStackTrace();
					}

				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}
			}
		}

		if (result.isSuccess() && (wrapper.getCqlDefinitions().size() > 0)) {
			result.getCqlModel().setDefinitionList(sortDefinitionsList(wrapper.getCqlDefinitions()));
		}

		return result;
	}

	/**
	 * Creates a temporary string for the definition, formats it, and returns the formatted definition logic.
	 * @param definition the definition to format
	 * @return the formatted definition logic
	 * @throws IOException
	 */
	private String formatDefinition(CQLDefinition definition) throws IOException {

		String definitionStatement = "define" + " \"" + definition.getDefinitionName() + "\":";
		String tempDefinitionString = definitionStatement + "\n\t" + definition.getDefinitionLogic();
		
		System.out.println(tempDefinitionString);

		String definitionLogic = "";
		if(definition.getDefinitionLogic() != null && !definition.getDefinitionLogic().isEmpty()) {
			CQLFormatter formatter = new CQLFormatter(); 
			String formattedDefinition = formatter.format(tempDefinitionString);
			definitionLogic = parseOutBody(formattedDefinition, definitionStatement);
		}


		return definitionLogic;
	}

	/**
	 * Parses the body from the cql definition or function. Remove the define statement for the expression and formats it nicely for ace editor by
	 * removing the first tab on each line.
	 * @param cqlExpressionString the cql expression string
	 * @param expressionDefinitionString the definition string in the format of define "ExpressionName": logic or
	 * define function "FunctionName"(arg1 Boolean, arg2 Boolean...): logic
	 * @return the body of the cql expression
	 */
	private String parseOutBody(String cqlExpressionString, String expressionDefinitionString) {

		// remove the definition statement from the expressions string to make the epxerssion body and then trim whitespace
		String expressionBodyString = cqlExpressionString.replace(expressionDefinitionString, "").trim();


		Scanner scanner = new Scanner(expressionBodyString);
		StringBuilder builder = new StringBuilder();

		// go through and rebuild the the format
		// this will remove the first tab in a line so
		// it properly displays in the ace editor.
		// without doing this, the the ace editor display
		// would be indented one too many
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();

			if(!line.isEmpty() && line.charAt(0) == '\t') {
				line = line.replaceFirst("\t", "");
			}

			builder.append(line + "\n");
		}
		
		System.out.println(builder.toString());

		scanner.close();
		return builder.toString();
	}

	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#saveAndModifyIncludeLibray(java.
	 * lang.String, mat.model.cql.CQLIncludeLibrary,
	 * mat.model.cql.CQLIncludeLibrary, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult saveAndModifyIncludeLibrayInCQLLookUp(String xml, CQLIncludeLibrary toBeModifiedObj,
			CQLIncludeLibrary currentObj, List<CQLIncludeLibrary> incLibraryList) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		CQLModelValidator validator = new CQLModelValidator();
		boolean isDuplicate = false;
		if (xml != null && !xml.isEmpty()) {

			XmlProcessor processor = new XmlProcessor(xml);
			// before adding includeLibrary Node we need need to add
			// <includeLibrarys> tag

			checkAndAppendIncludeLibraryParentNode(processor);

			if (toBeModifiedObj != null) { // this is a part of Modify
				currentObj.setId(toBeModifiedObj.getId());
				currentObj.setAliasName(toBeModifiedObj.getAliasName());
				String XPATH_EXPRESSION_INCLUDES = "//includeLibrary[@cqlLibRefId='" + toBeModifiedObj.getCqlLibraryId()
						+ "']";
				try {
					Node nodeIncludes = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_INCLUDES);

					if (nodeIncludes != null) {
						currentObj.setId(toBeModifiedObj.getId());
						String cqlString = createIncludeLibraryXML(currentObj);
						String XPATH_EXPRESSION_INCLUDELIBRARYS = "//cqlLookUp/includeLibrarys";
						processor.removeFromParent(nodeIncludes);
						processor.appendNode(cqlString, "includeLibrary", XPATH_EXPRESSION_INCLUDELIBRARYS);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));

						String finalUpdatedXml = processor.transform(processor.getOriginalDoc());
						result.setXml(finalUpdatedXml);
						result.setSuccess(true);
						result.setIncludeLibrary(currentObj);
						wrapper.setCqlIncludeLibrary(modifyIncludesList(toBeModifiedObj, currentObj, incLibraryList));
					} else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}
				} catch (XPathExpressionException | SAXException | IOException e) {
					e.printStackTrace();
				} 
				
			} else { // this is part of save functionality
				currentObj.setId(UUID.randomUUID().toString());
				isDuplicate = validator.validateForAliasNameSpecialChar(currentObj.getAliasName());
				if (!isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
					return result;
				}

				isDuplicate = isDuplicateIdentifierName(currentObj.getAliasName(), xml);
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
					return result;
				}

				isDuplicate = isDupParentCQLLibraryName(currentObj.getAliasName(), xml);
				if (isDuplicate) {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
					return result;
				}

				if (!isDuplicate) {
					String cqlString = createIncludeLibraryXML(currentObj);
					String XPATH_EXPRESSION_INCLUDES = "//cqlLookUp/includeLibrarys";
					try {
						Node nodeIncludes = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_INCLUDES);

						if (nodeIncludes != null) {

							processor.appendNode(cqlString, "includeLibrary", XPATH_EXPRESSION_INCLUDES);
							processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));

							String finalUpdatedXml = processor.transform(processor.getOriginalDoc());
							result.setXml(finalUpdatedXml);
							result.setSuccess(true);
							result.setIncludeLibrary(currentObj);
							incLibraryList.add(currentObj);
							wrapper.setCqlIncludeLibrary(incLibraryList);
						} else {
							result.setSuccess(false);
							result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
						}

					} catch (XPathExpressionException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NAME_NOT_UNIQUE);
				}
			}
		}

		if (result.isSuccess() && (wrapper.getCqlIncludeLibrary().size() > 0)) {
			result.getCqlModel().setCqlIncludeLibrarys(sortIncludeLibList(wrapper.getCqlIncludeLibrary()));
			CQLUtil.getIncludedCQLExpressions(cqlModel, cqlLibraryDAO);
		}

		return result;
	}

	/**
	 * Save CQL association.
	 *
	 * @param currentObj
	 *            the current obj
	 * @param measureId
	 *            the measure id
	 */
	@Override
	public void saveCQLAssociation(CQLIncludeLibrary currentObj, String measureId) {
		CQLLibraryAssociation cqlLibraryAssociation = new CQLLibraryAssociation();
		cqlLibraryAssociation.setCqlLibraryId(currentObj.getCqlLibraryId());
		cqlLibraryAssociation.setAssociationId(measureId);
		cqlLibraryAssociationDAO.save(cqlLibraryAssociation);
	}

	@Override
	public int countNumberOfAssociation(String associatedWithId) {
		return cqlLibraryAssociationDAO.findAssociationCount(associatedWithId);
	}

	@Override
	public List<CQLLibraryAssociation> getAssociations(String associatedWithId) {
		return cqlLibraryAssociationDAO.getAssociations(associatedWithId);
	}

	/**
	 * Check and append include library parent node.
	 *
	 * @param processor
	 *            the processor
	 */
	private void checkAndAppendIncludeLibraryParentNode(XmlProcessor processor) {

		try {
			Node cqlNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp");
			if (cqlNode != null) {

				Node cqlIncludeLibNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/includeLibrarys");
				if (cqlIncludeLibNode == null) {
					Element includesChildElem = processor.getOriginalDoc().createElement("includeLibrarys");
					cqlNode.appendChild(includesChildElem);
				}

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the include library XML.
	 *
	 * @param includeLibrary
	 *            the include library
	 * @return the string
	 */
	private String createIncludeLibraryXML(CQLIncludeLibrary includeLibrary) {
		logger.info("In CQLServiceImpl.createIncludeLibraryXML");
		Mapping mapping = new Mapping();
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		List<CQLIncludeLibrary> includeLibraryList = new ArrayList<CQLIncludeLibrary>();
		includeLibraryList.add(includeLibrary);
		wrapper.setCqlIncludeLibrary(includeLibraryList);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLIncludeLibrayMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.info("Marshalling of CQLIncludeLibrary is successful..");
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLIncludeLibrayMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createIncludeLibraryXML()");
		return stream.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#deleteDefinition(java.lang.String,
	 * mat.model.cql.CQLDefinition, mat.model.cql.CQLDefinition, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteDefinition(String xml, CQLDefinition toBeDeletedObj, 
			List<CQLDefinition> definitionList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if(toBeDeletedObj.isSupplDataElement()){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}

		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLDefinitions().contains(toBeDeletedObj.getDefinitionName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {
			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null && !xml.isEmpty()) {
				String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='" + toBeDeletedObj.getId()
						+ "']";
				try {
					Node definitionNode = processor.findNode(processor.getOriginalDoc(),
							XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);

					if (definitionNode != null) {
						// remove from xml
						definitionNode.getParentNode().removeChild(definitionNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());

						// remove from definition list
						definitionList.remove(toBeDeletedObj);
						wrapper.setCqlDefinitions(definitionList);
						result.setSuccess(true);
						result.setDefinition(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}

			}
		}
		if (result.isSuccess() && (wrapper.getCqlDefinitions().size() > 0)) {
			result.getCqlModel().setDefinitionList(sortDefinitionsList(wrapper.getCqlDefinitions()));
		}

		return result;
	}

	@Override
	public SaveUpdateCQLResult deleteValueSet(String xml, String toBeDelValueSetId) {
		logger.info("Start deleteValueSet : ");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		try {
			String xpathforValueSet = "//cqlLookUp//valueset[@id='" + toBeDelValueSetId + "']";
			Node valueSetElements = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforValueSet);

			if (valueSetElements != null) {
				String valueSetName = valueSetElements.getAttributes().getNamedItem("name").getNodeValue();
				CQLQualityDataSetDTO valueSet = new CQLQualityDataSetDTO();
				valueSet.setCodeListName(valueSetName);
				Node parentNode = valueSetElements.getParentNode();
				parentNode.removeChild(valueSetElements);
				result.setSuccess(true);
				result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
				result.setCqlQualityDataSetDTO(valueSet);

			} else {
				logger.info("Unable to find the selected valueset element with id in deleteValueSet : "
						+ toBeDelValueSetId);
				result.setSuccess(false);
			}
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteValueSet: " + e.getMessage());
		}

		logger.info("END deleteValueSet : ");
		return result;

	}

	@Override
	public SaveUpdateCQLResult deleteCode(String xml, String toBeDeletedCodeId) {
		logger.info("Start deleteCode : ");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);
		try {
			String xpathforCodeNode = "//cqlLookUp//code[@id='" + toBeDeletedCodeId + "']";
			Node codeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforCodeNode);

            if (codeNode != null) {
                String cqlOID = codeNode.getAttributes().getNamedItem("codeOID").getNodeValue();
                String cqlCodeName = codeNode.getAttributes().getNamedItem("codeName").getNodeValue();
                String codeSystemOID = codeNode.getAttributes().getNamedItem("codeSystemOID").getNodeValue();
                String codeSystemVersion = codeNode.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
                if(!isCodeSystemUsedByAnotherCode(codeSystemOID, codeSystemVersion, toBeDeletedCodeId, xmlProcessor)) {
                	deleteCodeSystem(xmlProcessor, codeSystemOID, codeSystemVersion);
                }
                
                CQLCode cqlCode = new CQLCode();
                cqlCode.setCodeName(cqlCodeName);
                cqlCode.setCodeOID(cqlOID);

    			Node parentNode = codeNode.getParentNode();
                parentNode.removeChild(codeNode);
                result.setSuccess(true);
                result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
                result.setCqlCodeList(getCQLCodes(result.getXml()).getCqlCodeList());
                result.setCqlCode(cqlCode);
			} else {
				logger.info("Unable to find the selected Code element with id in deleteCode : " + toBeDeletedCodeId);
				result.setSuccess(false);
			}
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteCode: " + e.getMessage());
		} catch (MatException e) {
			result.setSuccess(false);
			logger.info("Error in method deleteCode: " + e.getMessage());
		}

		logger.info("END deleteCode : ");

		return result;
	}

	private void deleteCodeSystem(XmlProcessor xmlProcessor, String codeSystemOID, String codeSystemVersion) throws XPathExpressionException, MatException {
		String xpathforCodeSystemNode = "//cqlLookUp//codeSystem[@codeSystem='" + codeSystemOID +  "' and @codeSystemVersion='" + codeSystemVersion + "']";
		Node codeSystemNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforCodeSystemNode);
		if(codeSystemNode != null) {
			Node codeSystemParentNode = codeSystemNode.getParentNode();
			codeSystemParentNode.removeChild(codeSystemNode);
		} else {
			throw new MatException("Unable to find the selected CodeSystem element with id:" + codeSystemOID + " and version: " + codeSystemVersion);
		}
	}

	private boolean isCodeSystemUsedByAnotherCode(String codeSystemOID, String codeSystemVersion, String toBeDeletedCodeId, XmlProcessor xmlProcessor) throws XPathExpressionException {
		boolean isCodeSystemUsed = false;
		String xpathforCodeNodes = "//cqlLookUp//code[@id!='" + toBeDeletedCodeId  + "']";
		NodeList nodeList = xmlProcessor.findNodeList(xmlProcessor.getOriginalDoc(), xpathforCodeNodes);
		for(int i = 0; i<nodeList.getLength(); i++) {
			Node curCodeNode = nodeList.item(i);
			if(curCodeNode != null) {
				String curCodeSystemOID = curCodeNode.getAttributes().getNamedItem("codeSystemOID").getNodeValue();
				String curCodeSystemVersion = curCodeNode.getAttributes().getNamedItem("codeSystemVersion").getNodeValue();
				if(codeSystemOID.equalsIgnoreCase(curCodeSystemOID) && curCodeSystemVersion.equals(codeSystemVersion)) {
					isCodeSystemUsed = true;
					break;
				}
			}	
		}
		
		return isCodeSystemUsed;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#deleteFunctions(java.lang.String,
	 * mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteFunctions(String xml, CQLFunctions toBeDeletedObj, 
			List<CQLFunctions> functionsList) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);


		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLFunctions().contains(toBeDeletedObj.getFunctionName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {

			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null && !xml.isEmpty()) {
				String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='" + toBeDeletedObj.getId() + "']";
				try {
					Node functionNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

					if (functionNode != null) {

						// remove from xml
						functionNode.getParentNode().removeChild(functionNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());

						// remove from function list
						functionsList.remove(toBeDeletedObj);
						wrapper.setCqlFunctionsList(functionsList);
						result.setSuccess(true);
						result.setFunction(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}
			}
		}
		if (result.isSuccess() && (wrapper.getCqlFunctionsList().size() > 0)) {
			result.getCqlModel().setCqlFunctions(sortFunctionssList(wrapper.getCqlFunctionsList()));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#deleteParameter(java.lang.String,
	 * mat.model.cql.CQLParameter, mat.model.cql.CQLParameter, java.util.List)
	 */
	@Override
	public SaveUpdateCQLResult deleteParameter(String xml, CQLParameter toBeDeletedObj, 
			List<CQLParameter> parameterList) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLParametersWrapper wrapper = new CQLParametersWrapper();

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);

		if(toBeDeletedObj.isReadOnly()){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}


		GetUsedCQLArtifactsResult artifactsResult = getUsedCQlArtifacts(xml);
		if (artifactsResult.getCqlErrors().isEmpty()
				&& artifactsResult.getUsedCQLParameters().contains(toBeDeletedObj.getParameterName())) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
		} else {
			XmlProcessor processor = new XmlProcessor(xml);

			if (xml != null) {
				String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='" + toBeDeletedObj.getId()
						+ "']";
				try {
					Node parameterNode = processor.findNode(processor.getOriginalDoc(),
							XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

					if (parameterNode != null) {

						// remove from xml
						parameterNode.getParentNode().removeChild(parameterNode);
						processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
						result.setXml(processor.getOriginalXml());


						// remove from parameter list
						parameterList.remove(toBeDeletedObj);
						wrapper.setCqlParameterList(parameterList);
						result.setSuccess(true);
						result.setParameter(toBeDeletedObj);
					}

					else {
						result.setSuccess(false);
						result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
					}

				} catch (XPathExpressionException e) {
					result.setSuccess(false);
					e.printStackTrace();
				}

			}
		}
		if (result.isSuccess() && (wrapper.getCqlParameterList().size() > 0)) {
			result.getCqlModel().setCqlParameters(sortParametersList(wrapper.getCqlParameterList()));
		}

		return result;
	}

	/**
	 * Update risk adjustment variables.
	 *
	 * @param processor
	 *            the processor
	 * @param toBeModifiedObj
	 *            the to be modified obj
	 * @param currentObj
	 *            the current obj
	 */
	private void updateRiskAdjustmentVariables(XmlProcessor processor, CQLDefinition toBeModifiedObj,
			CQLDefinition currentObj) {

		logger.debug(" CQLServiceImpl: updateRiskAdjustmentVariables Start :  ");
		// XPath to find All cqlDefinitions in riskAdjustmentVariables to be
		// modified Definitions.
		String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure//cqldefinition[@uuid='" + toBeModifiedObj.getId() + "']";
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
	public SaveUpdateCQLResult getCQLData(String xmlString) {
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLStringFromXML(xmlString);

		SaveUpdateCQLResult parsedCQL = null;


		 parsedCQL = parseCQLLibraryForErrors(cqlModel);

		if (parsedCQL.getCqlErrors().isEmpty()) {
			parsedCQL.setUsedCQLArtifacts(getUsedCQlArtifacts(xmlString));
			setUsedValuesets(parsedCQL, cqlModel);
			setUsedCodes(parsedCQL, cqlModel);
			boolean isValid = CQLUtil.isValidDataTypeUsed(parsedCQL.getUsedCQLArtifacts().getValueSetDataTypeMap(),
					parsedCQL.getUsedCQLArtifacts().getCodeDataTypeMap());
			parsedCQL.setDatatypeUsedCorrectly(isValid);

		}

		parsedCQL.setCqlModel(cqlModel);

		return parsedCQL;
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getCQLData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLDataForLoad(String xmlString) {
		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLStringFromXML(xmlString);

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		Map<String, LibHolderObject> cqlLibNameMap = new HashMap<String, LibHolderObject>();
		CQLUtil.getCQLIncludeLibMap(cqlModel, cqlLibNameMap, cqlLibraryDAO);
		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);
		CQLUtil.setIncludedCQLExpressions(cqlModel);
		result.setCqlModel(cqlModel);

		return result;
	}
	
	@Override
	public SaveUpdateCQLResult getCQLLibraryData(String xmlString) {

		CQLModel cqlModel = new CQLModel();
		cqlModel = CQLUtilityClass.getCQLStringFromXML(xmlString);
		HashMap<String, LibHolderObject> cqlLibNameMap =  new HashMap<>();
		String parentLibraryName = cqlModel.getLibraryName();
		CQLUtil.getCQLIncludeLibMap(cqlModel, cqlLibNameMap, getCqlLibraryDAO());
		cqlModel.setIncludedCQLLibXMLMap(cqlLibNameMap);

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		List<CqlTranslatorException> cqlTranslatorExceptions = new ArrayList<CqlTranslatorException>();
		Map<String, String> libraryMap = new HashMap<>();

		// get the strings for parsing
		String parentCQLString = CQLUtilityClass.getCqlString(cqlModel, "").toString();

		libraryMap.put(cqlModel.getName() + "-" + cqlModel.getVersionUsed(), parentCQLString);
		for (String cqlLibName : cqlLibNameMap.keySet()) {
			CQLModel includedCQLModel = CQLUtilityClass.getCQLStringFromXML(cqlLibNameMap.get(cqlLibName).getMeasureXML());
			LibHolderObject libHolderObject = cqlLibNameMap.get(cqlLibName);
			String includedCQLString = CQLUtilityClass.getCqlString(includedCQLModel, "").toString();
			libraryMap.put(libHolderObject.getCqlLibrary().getCqlLibraryName() + "-" + libHolderObject.getCqlLibrary().getVersion(), includedCQLString);
		}

		// do the parsing
		CQLtoELM cqlToELM = new CQLtoELM(parentCQLString, libraryMap);
		cqlToELM.doTranslation(true);
		List<CQLErrors> errors = new ArrayList<CQLErrors>();
		cqlTranslatorExceptions.addAll(cqlToELM.getErrors());

		List<String> exprList = getExpressionListFromCqlModel(cqlModel);

		// do the filtering
		if(exprList != null){
			CQLUtil.filterCQLArtifacts(cqlModel, result, cqlToELM, exprList);
		}

		// add in the errors, if any
		for (CqlTranslatorException cte : cqlTranslatorExceptions) {
			CQLErrors cqlErrors = new CQLErrors();

			cqlErrors.setStartErrorInLine(cte.getLocator().getStartLine());

			cqlErrors.setErrorInLine(cte.getLocator().getStartLine());
			cqlErrors.setErrorAtOffeset(cte.getLocator().getStartChar());

			cqlErrors.setEndErrorInLine(cte.getLocator().getEndLine());
			cqlErrors.setEndErrorAtOffset(cte.getLocator().getEndChar());

			cqlErrors.setErrorMessage(cte.getMessage());
			errors.add(cqlErrors);
		}

		setUsedValuesets(result, cqlModel);
		setUsedCodes(result, cqlModel);
		boolean isValid = CQLUtil.isValidDataTypeUsed(result.getUsedCQLArtifacts().getValueSetDataTypeMap(),
				result.getUsedCQLArtifacts().getCodeDataTypeMap());
		result.setDatatypeUsedCorrectly(isValid);

		if(errors.isEmpty()) {
			try {
				CQLFormatter formatter = new CQLFormatter();
				result.setCqlString(formatter.format(parentCQLString));
			} catch (IOException e) {
				result.setCqlString(parentCQLString);
			}
		}

		else {
			result.setCqlString(parentCQLString);
		}
		result.setCqlString(parentCQLString);
		result.setCqlErrors(errors);
		result.setLibraryName(parentLibraryName);
		return result;
	}

	private void setReturnTypes(SaveUpdateCQLResult result , CQLModel cqlModel) {
		CQLObject cqlObject = result.getCqlObject();
		if (cqlObject == null || cqlModel == null) {
			return;

		}
		if (cqlModel.getDefinitionList() != null) {
			for (CQLDefinition definition : cqlModel.getDefinitionList()) {
				CQLExpressionObject obj = findExpressionObject(definition.getDefinitionName(),
						cqlObject.getCqlDefinitionObjectList());
				if (obj != null) {
					result.getUsedCQLArtifacts().getExpressionReturnTypeMap().put(obj.getName(), obj.getReturnType());
				}

			}

		}
		if (cqlModel.getCqlFunctions() != null) {
			for (CQLFunctions functions : cqlModel.getCqlFunctions()) {
				CQLExpressionObject obj = findExpressionObject(functions.getFunctionName(),
						cqlObject.getCqlFunctionObjectList());
				if (obj != null) {
					result.getUsedCQLArtifacts().getExpressionReturnTypeMap().put(obj.getName(), obj.getReturnType());
				}

			}
		}

}

	private void setUsedCodes(SaveUpdateCQLResult parsedCQL, CQLModel cqlModel) {

		List<String> usedCodes = parsedCQL.getUsedCQLArtifacts().getUsedCQLcodes();
		System.out.println("used codes:" + usedCodes);
		for (CQLCode cqlCode : cqlModel.getCodeList()) {
			boolean isUsed = usedCodes.contains(cqlCode.getDisplayName());
			cqlCode.setUsed(isUsed);
		}

	}

	private void setUsedValuesets(SaveUpdateCQLResult parsedCQL, CQLModel cqlModel) {

		List<String> usedValuesets = parsedCQL.getUsedCQLArtifacts().getUsedCQLValueSets();

		for (CQLQualityDataSetDTO valueset : cqlModel.getAllValueSetList()) {
			boolean isUsed = usedValuesets.contains(valueset.getCodeListName());
			valueset.setUsed(isUsed);
		}

	}

	@Override
	public SaveUpdateCQLResult parseCQLLibraryForErrors(CQLModel cqlModel) {
		return CQLUtil.parseCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), null);
	}


	/**
	 * Gets the CQL definitions from measure xml.
	 *
	 * @param measureId
	 *            the measure id
	 * @return the CQL definitions from measure xml
	 */
	public CQLDefinitionsWrapper getCQLDefinitionsFromMeasureXML(String measureId) {

		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(measureId);
		CQLDefinitionsWrapper wrapper = null;
		if (measureXmlModel != null) {
			wrapper = convertXmltoCQLDefinitionModel(measureXmlModel);
			if ((wrapper.getCqlDefinitions() != null) && (wrapper.getCqlDefinitions().size() > 0)) {
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
	private CQLDefinitionsWrapper convertXmltoCQLDefinitionModel(final MeasureXmlModel xmlModel) {
		logger.info("In CQLServiceImpl.convertXmltoCQLDefinitionModel()");
		CQLDefinitionsWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("cqlLookUp");
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain cqlLookUp tag");

			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(CQLDefinitionsWrapper.class);
				unmar.setWhitespacePreserve(true);
				details = (CQLDefinitionsWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));

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
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLParameterModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
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
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLFunctionModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
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
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CQLDefinitionModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
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
				e.printStackTrace();
			}
		}
		logger.info("Exiting CQLServiceImpl.createDefinitionsXML()");
		return stream.toString();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#getCQLFileData(java.lang.String)
	 */
	@Override
	public SaveUpdateCQLResult getCQLFileData(String xmlString) {

		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		result = getCQLData(xmlString);
		String cqlString = getCqlString(result.getCqlModel());
		if (cqlString != null) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		result.setCqlString(cqlString);
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
	public String getCqlString(CQLModel cqlModel) {

		return CQLUtilityClass.getCqlString(cqlModel, "");
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getCQLKeyWords()
	 */
	@Override
	public CQLKeywords getCQLKeyWords() {

		CQLKeywords cqlKeywords = new CQLKeywords();
		XmlProcessor cqlXMLProcessor = CQLTemplateXML.getCQLTemplateXmlProcessor();
		String XPATH_DATATYPES = "/cqlTemplate/datatypes/datatype";
		String XPATH_TIMINGS = "/cqlTemplate/timings/timing";
		String XPATH_FUNCTIONS = "/cqlTemplate/functions/function";
		String XPATH_KEYWORDS = "/cqlTemplate/keywords/keyword";
		List<String> cqlDataTypeList = new ArrayList<String>();
		List<String> cqlTimingList = new ArrayList<String>();
		List<String> cqlFunctionList = new ArrayList<String>();
		List<String> cqlKeywordsList = new ArrayList<String>();
		try {
			NodeList dataTypeNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_DATATYPES);
			NodeList timingNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_TIMINGS);
			NodeList functionNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_FUNCTIONS);
			NodeList keywordNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_KEYWORDS);
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
			if (keywordNodeList != null) {
				for (int i = 0; i < keywordNodeList.getLength(); i++) {
					Node node = keywordNodeList.item(i);
					cqlKeywordsList.add(node.getTextContent());
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		cqlKeywords.setCqlDataTypeList(cqlDataTypeList);
		cqlKeywords.setCqlTimingList(cqlTimingList);
		cqlKeywords.setCqlFunctionsList(cqlFunctionList);
		cqlKeywords.setCqlKeywordsList(cqlKeywordsList);
		return cqlKeywords;
	}

	/**
	 * Checks if is duplicate identifier name.
	 *
	 * @param identifierName
	 *            the identifier name
	 * @param id
	 *            the measure id
	 * @return true, if is duplicate identifier name
	 */
	private boolean isDuplicateIdentifierName(String identifierName, String xml) {

		if (xml != null) {

			XmlProcessor processor = new XmlProcessor(xml);
			String XPATH_CQLLOOKUP_IDENTIFIER_NAME = "//cqlLookUp//node()[translate(@name, "
					+ "'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + identifierName.toUpperCase()
					+ "']";
			try {
				NodeList nodeList = processor.findNodeList(processor.getOriginalDoc(), XPATH_CQLLOOKUP_IDENTIFIER_NAME);

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
	 * Checks if is dupidn name with msr name.
	 *
	 * @param identifierName
	 *            the identifier name
	 * @param id
	 *            the measure id
	 * @return true, if is dupidn name with msr name
	 */
	private boolean isDupParentCQLLibraryName(String identifierName, String xml) {

		XmlProcessor processor = new XmlProcessor(xml);
		String XPATH_MEASURE_NAME = "//cqlLookUp/library";
		try {
			Node node = processor.findNode(processor.getOriginalDoc(), XPATH_MEASURE_NAME);

			if (node != null) {
				String msrName = node.getTextContent();
				if (identifierName.equalsIgnoreCase(msrName)) {
					return true;
				}

			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return false;
	}

	private List<CQLIncludeLibrary> modifyIncludesList(CQLIncludeLibrary toBeModified , CQLIncludeLibrary currentObj , List<CQLIncludeLibrary> incLibraryList ){
		Iterator<CQLIncludeLibrary> iterator = incLibraryList.iterator();
		while (iterator.hasNext()) {
			CQLIncludeLibrary cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModified.getId())) {

				iterator.remove();
				break;
			}
		}
		incLibraryList.add(currentObj);
		return incLibraryList;
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
	private CQLParametersWrapper modfiyCQLParameterList(CQLParameter toBeModifiedObj, CQLParameter currentObj,
			List<CQLParameter> parameterList) {
		CQLParametersWrapper wrapper = new CQLParametersWrapper();
		Iterator<CQLParameter> iterator = parameterList.iterator();
		while (iterator.hasNext()) {
			CQLParameter cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModifiedObj.getId())) {

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
	private CQLFunctionsWrapper modfiyCQLFunctionList(CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
			List<CQLFunctions> functionList) {
		CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
		Iterator<CQLFunctions> iterator = functionList.iterator();
		while (iterator.hasNext()) {
			CQLFunctions cqlParam = iterator.next();
			if (cqlParam.getId().equals(toBeModifiedObj.getId())) {

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
	private CQLDefinitionsWrapper modfiyCQLDefinitionList(CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
			List<CQLDefinition> definitionList) {
		CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
		Iterator<CQLDefinition> iterator = definitionList.iterator();
		while (iterator.hasNext()) {
			CQLDefinition cqlDefinition = iterator.next();
			if (cqlDefinition.getId().equals(toBeModifiedObj.getId())) {

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
	private List<CQLDefinition> sortDefinitionsList(List<CQLDefinition> defineList) {

		Collections.sort(defineList, new Comparator<CQLDefinition>() {
			@Override
			public int compare(final CQLDefinition o1, final CQLDefinition o2) {
				return o1.getDefinitionName().compareToIgnoreCase(o2.getDefinitionName());
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
				return o1.getParameterName().compareToIgnoreCase(o2.getParameterName());
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
				return o1.getFunctionName().compareToIgnoreCase(o2.getFunctionName());
			}
		});

		return funcList;
	}

	/**
	 * Sort include lib list.
	 *
	 * @param IncLibList
	 *            the inc lib list
	 * @return the list
	 */
	private List<CQLIncludeLibrary> sortIncludeLibList(List<CQLIncludeLibrary> IncLibList) {

		Collections.sort(IncLibList, new Comparator<CQLIncludeLibrary>() {
			@Override
			public int compare(final CQLIncludeLibrary o1, final CQLIncludeLibrary o2) {
				return o1.getAliasName().compareToIgnoreCase(o2.getAliasName());
			}
		});

		return IncLibList;
	}

	/*
	 * (non-Javadoc)
	 *
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

	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getSupplementalDefinitions()
	 */
	@Override
	public String getSupplementalDefinitions() {

		return cqlSupplementalDefinitionXMLString;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getDefaultCodeSystems()
	 */
	@Override
	public String getDefaultCodeSystems() {

		return cqlDefaultCodeSystemXMLString;

	}

		public SaveUpdateCQLResult parseCQLExpressionForErrors(SaveUpdateCQLResult result, String xml,
			String cqlExpressionName, String logic, String expressionName, String expressionType) {

		CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(xml);
		String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, cqlExpressionName).toString();

		cqlModel.setLines(countLines(cqlFileString));

		String wholeDef = "";
		int size = 0;
		int startLine = 0;
		int endLine = CQLUtilityClass.getSize();
		
				
		if(expressionType.equalsIgnoreCase("parameter")) {
			endLine = endLine + 1; // for parameters, the size is actually 1 more than reporetd.
			wholeDef = cqlExpressionName + " " + logic;
			size = countLines(wholeDef);
			
			if(size > 1) {
				startLine = endLine - size;
			}else {
				startLine = endLine;
			}
		}

		else {
			wholeDef = cqlExpressionName + " :\n" + logic;
			size = countLines(wholeDef);
			startLine = endLine - size + 1; // the start line is really 1 more than endLine - size because
											// the definition logic starts on the next line.
		}

		result.setStartLine(startLine);
		result.setEndLine(endLine);

		List<String> expressionList = getExpressionListFromCqlModel(cqlModel);
		SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
		parsedCQL = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);

		if(!parsedCQL.getCqlErrors().isEmpty()){
			result.setValidCQLWhileSavingExpression(false);
		}
		List<CQLErrors> errors = new ArrayList<CQLErrors>();
		for (CQLErrors cqlError : parsedCQL.getCqlErrors()) {
			int errorStartLine = cqlError.getStartErrorInLine();
			
			if ((errorStartLine >= startLine && errorStartLine <= endLine)) {
				
				if(cqlError.getStartErrorInLine() == startLine) {
					cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() - startLine);
				}else {
					cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() - startLine - 1);
				}
				
				if(cqlError.getEndErrorInLine() == startLine) {
					cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - startLine);
				}else {
					cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - startLine - 1);
				}
				
				cqlError.setErrorMessage(cqlError.getErrorMessage());
				errors.add(cqlError);
			}
		}

		if (errors.isEmpty()) {
			result.setCqlObject(parsedCQL.getCqlObject());

			boolean isValid = findValidDataTypeUsage(expressionName, expressionType, parsedCQL);
			result.setDatatypeUsedCorrectly(isValid);
			if (isValid) {
				XmlProcessor xmlProcessor = new XmlProcessor(xml);
				CQLArtifactHolder cqlArtifactHolder = CQLUtil
						.getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
				parsedCQL.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
				parsedCQL.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
			}
		}

		result.setCqlModel(cqlModel);
		result.setCqlErrors(errors);
		result.setUsedCQLArtifacts(parsedCQL.getUsedCQLArtifacts());
		return result;
	}

	private boolean findValidDataTypeUsage(String expressionName, String expressionType,
			SaveUpdateCQLResult parsedCQL) {
		List<CQLExpressionObject> expressionList = null;
		switch (expressionType) {
		case "Function":
			expressionList = parsedCQL.getCqlObject().getCqlFunctionObjectList();
			break;
		case "Definition":
			expressionList = parsedCQL.getCqlObject().getCqlDefinitionObjectList();
			break;
		case "Parameter":
			expressionList = parsedCQL.getCqlObject().getCqlParameterObjectList();
			break;

		}
		CQLExpressionObject cqlExpressionObject = findExpressionObject(expressionName, expressionList);
		boolean isValid = true;
		if (cqlExpressionObject != null) {
			isValid = CQLUtil.isValidDataTypeUsed(cqlExpressionObject.getValueSetDataTypeMap(),
					cqlExpressionObject.getCodeDataTypeMap());
		}
		return isValid;
	}


	private CQLExpressionObject findExpressionObject(String expressionName, List<CQLExpressionObject> expressionList) {
		CQLExpressionObject cqlExpressionObject = null;
		if (expressionList != null) {
			for (CQLExpressionObject expressionObject : expressionList) {
				if (expressionName.equalsIgnoreCase(expressionObject.getName())) {
					cqlExpressionObject = expressionObject;
					break;
				}
			}
		}
		return cqlExpressionObject;
	}

	/**
	 * Count lines.
	 *
	 * @param str
	 *            the str
	 * @return the int
	 */
	public static int countLines(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}
		int lines = 1;
		int pos = 0;
		while ((pos = str.indexOf("\n", pos) + 1) != 0) {
			lines = lines + 1;
		}
		return lines;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getDefaultCodes()
	 */
	@Override
	public String getDefaultCodes() {
		return cqlDefaultCodesXMLString;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#getUsedCQlArtifacts(java.lang.
	 * String)
	 */
	@Override
	public GetUsedCQLArtifactsResult getUsedCQlArtifacts(String xml) {
		logger.info("GETTING CQL ARTIFACTS");
		CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(xml);

		List<String> exprList = new ArrayList<String>();

		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			System.out.println("name:" + cqlDefinition.getDefinitionName());
			exprList.add(cqlDefinition.getDefinitionName());
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			System.out.println("name:" + cqlFunction.getFunctionName());
			exprList.add(cqlFunction.getFunctionName());
		}
		
		
		for (CQLParameter cqlParameter : cqlModel.getCqlParameters()) {
			System.out.println("name:" + cqlParameter.getParameterName());
			exprList.add(cqlParameter.getParameterName());
		}

		SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), exprList);
		
		Map<String , List<CQLErrors>> expressionMapWithError = getCQLErrorsPerExpressions(cqlModel, cqlResult);
		
		cqlResult.getUsedCQLArtifacts().setCqlErrorsPerExpression(expressionMapWithError);

		// if there are no errors in the cql file, get the used cql artifacts
		if (cqlResult.getCqlErrors().isEmpty()) {

			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			CQLArtifactHolder cqlArtifactHolder = CQLUtil
					.getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
			cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
			cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
			System.out.println("USED LIBRARY: " + cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries());

			setReturnTypes(cqlResult, cqlModel);

		} else {
			cqlResult.getUsedCQLArtifacts().setCqlErrors(cqlResult.getCqlErrors());
		}

		return cqlResult.getUsedCQLArtifacts();
	}

		/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#parseCQLStringForError(java.lang.
	 * String)
	 */
	@Override
	public SaveUpdateCQLResult parseCQLStringForError(String cqlFileString) {
		return null;
	}

	/**
	 * Save QD sto measure.
	 *
	 * @param valueSetTransferObject
	 *            the value set transfer object
	 * @return the save update code list result
	 */
	@SuppressWarnings("static-access")
	@Override
	public final SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
		valueSetTransferObject.scrubForMarkUp();
		
		if(!valueSetTransferObject.validateModel()){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
			return result;
		}
		
		
		CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
		MatValueSet matValueSet = valueSetTransferObject.getMatValueSet();
		qds.setOid(matValueSet.getID());
		qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		qds.setCodeListName(valueSetTransferObject.getCqlQualityDataSetDTO().getCodeListName());
		qds.setSuffix(valueSetTransferObject.getCqlQualityDataSetDTO().getSuffix());
		qds.setOriginalCodeListName(valueSetTransferObject.getCqlQualityDataSetDTO().getOriginalCodeListName());
		qds.setRelease(valueSetTransferObject.getCqlQualityDataSetDTO().getRelease());
		qds.setProgram(valueSetTransferObject.getCqlQualityDataSetDTO().getProgram());

		if (matValueSet.isGrouping()) {
			qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
		} else {
			qds.setTaxonomy(matValueSet.getCodeSystemName());
		}
		qds.setUuid(UUID.randomUUID().toString());
		
		qds.setVersion(getValueSetVersion(valueSetTransferObject.isVersion(), 
				valueSetTransferObject.getCqlQualityDataSetDTO().getRelease(), valueSetTransferObject.getMatValueSet().getVersion()));
		
		ArrayList<CQLQualityDataSetDTO> qualityDataSetDTOs = (ArrayList<CQLQualityDataSetDTO>) valueSetTransferObject
				.getAppliedQDMList();

		// Treat as regular QDM

		if (!isDuplicate(valueSetTransferObject, true)) {
			wrapper.getQualityDataDTO().add(qds);
			String xmlString = generateXmlForAppliedValueset(wrapper);
			result.setSuccess(true);
			qualityDataSetDTOs.add(qds);
			result.setCqlAppliedQDMList(sortQualityDataSetList(qualityDataSetDTOs));
			result.setXml(xmlString);
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}

		return result;
	}
	
	private String getValueSetVersion(boolean hasVersion, String release, String version) {	
				
		if (StringUtils.isNotBlank(release)){
			return "" ;
		} else {
			return hasVersion ? version : "1.0" ;			
		}
		
	}

	/**
	 * Sort quality data set list.
	 *
	 * @param finalList
	 *            the final list
	 * @return the list
	 */
	private List<CQLQualityDataSetDTO> sortQualityDataSetList(final List<CQLQualityDataSetDTO> finalList) {

		Collections.sort(finalList, new Comparator<CQLQualityDataSetDTO>() {
			@Override
			public int compare(final CQLQualityDataSetDTO o1, final CQLQualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
			}
		});

		return finalList;

	}

		/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#saveUserDefinedQDStoMeasure(mat.
	 * model.CQLValueSetTransferObject)
	 */
	@SuppressWarnings("static-access")
	@Override
	public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		matValueSetTransferObject.scrubForMarkUp();
		
		if(!matValueSetTransferObject.validateModel()){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCQLResult.SERVER_SIDE_VALIDATION);
			return result;
		}
		
		
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator();
		String errorMessage = validator.validate(matValueSetTransferObject);
		if (errorMessage.isEmpty()) {
			ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
			
			wrapper.setQualityDataDTO(qdsList);
			CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
			qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
			qds.setId(UUID.randomUUID().toString());
			qds.setCodeListName(matValueSetTransferObject.getCqlQualityDataSetDTO().getCodeListName());
			qds.setSuffix(matValueSetTransferObject.getCqlQualityDataSetDTO().getSuffix());
			qds.setOriginalCodeListName(matValueSetTransferObject.getCqlQualityDataSetDTO().getOriginalCodeListName());
			qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
			qds.setUuid(UUID.randomUUID().toString());
			qds.setVersion("1.0");
			qds.setRelease("");
			qds.setProgram("");
			wrapper.getQualityDataDTO().add(qds);
			
			String qdmXMLString = generateXmlForAppliedValueset(wrapper);
			result.setSuccess(true);
			result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
			result.setXml(qdmXMLString);
			
		} else {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodes(String xml, MatCodeTransferObject codeTransferObject) {
		logger.info("::: CQLServiceImpl saveCQLCodes Start :::");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		codeTransferObject.scrubForMarkUp();
		if (codeTransferObject.isValidModel() ) {

			XmlProcessor xmlProcessor = new XmlProcessor(xml);
			CQLCode appliedCode = codeTransferObject.getCqlCode();
			appliedCode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			
			
			try {
				Node existingCodeList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
						"//cqlLookUp/codes/code[@displayName=\"" + appliedCode.getDisplayName() + "\" ]");
				if (existingCodeList != null) {
					logger.info("::: Duplicate Code :::");
					result.setSuccess(false);
					result.setFailureReason(result.getDuplicateCode());
				} else {
					CQLCodeWrapper wrapper = new CQLCodeWrapper();
					ArrayList<CQLCode> codeList = new ArrayList<CQLCode>();
					wrapper.setCqlCodeList(codeList);
					wrapper.getCqlCodeList().add(codeTransferObject.getCqlCode());
					String codeXMLString = generateXmlForAppliedCode(wrapper);
					result.setSuccess(true);
					result.setXml(codeXMLString);
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		logger.info("::: CQLServiceImpl saveCQLCodes End :::");
		return result;
	}

	@Override
	public SaveUpdateCQLResult saveCQLCodeSystem(String xml, CQLCodeSystem codeSystem) {
		logger.info("::: CQLServiceImpl saveCQLCodeSystem Start :::");
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor xmlProcessor = new XmlProcessor(xml);

		codeSystem.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			Node existingCodeSystemList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),
					"//cqlLookUp/codeSystems/codeSystem[@codeSystemName='" + codeSystem.getCodeSystemName()
							+ "' and @codeSystemVersion='" + codeSystem.getCodeSystemVersion() + "' ]");

			if (existingCodeSystemList != null) {
				logger.info("::: CodeSystem Already added :::");
				result.setSuccess(false);
			} else {
				CQLCodeSystemWrapper wrapper = new CQLCodeSystemWrapper();
				ArrayList<CQLCodeSystem> codeSystemList = new ArrayList<CQLCodeSystem>();
				wrapper.setCqlCodeSystemList(codeSystemList);
				wrapper.getCqlCodeSystemList().add(codeSystem);
				String codeSystemXMLString = generateXmlForAppliedCodeSystem(wrapper);
				result.setSuccess(true);
				result.setXml(codeSystemXMLString);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.info("::: CQLServiceImpl saveCQLCodeSystem End :::");
		return result;
	}

	/**
	 * Checks if is duplicate.
	 *
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @param isVSACValueSet
	 *            the is VSAC value set
	 * @return true, if is duplicate
	 */
	private boolean isDuplicate(CQLValueSetTransferObject matValueSetTransferObject, boolean isVSACValueSet) {
		logger.info(" checkForDuplicates Method Call Start.");
		boolean isQDSExist = false;
		
		String qdmCompareName = matValueSetTransferObject.getCqlQualityDataSetDTO().getCodeListName();

		List<CQLQualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {

			String codeListName =  dataSetDTO.getCodeListName();

			if (codeListName.equalsIgnoreCase(qdmCompareName)) {
				isQDSExist = true;
				break;
			}
		}
		logger.info("checkForDuplicates Method Call End.Check resulted in :" + (isQDSExist));
		return isQDSExist;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see mat.client.measure.service.CQLService#updateQDStoMeasure(mat.model.
	 * CQLValueSetTransferObject)
	 */
	@Override
	public final SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		matValueSetTransferObject.scrubForMarkUp();
		if(!matValueSetTransferObject.validateModel()){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
			return result;
		}
		if (matValueSetTransferObject.getMatValueSet() != null) {
			result = modifyVSACValueSetInCQLLookUp(matValueSetTransferObject);
		} else if (matValueSetTransferObject.getCodeListSearchDTO() != null) {
			result = modifyUserDefineValuesetInCQLLookUp(matValueSetTransferObject);
		}
		return result;
	}

	/**
	 * Update VSAC value set in element look up.
	 *
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult modifyVSACValueSetInCQLLookUp(CQLValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLQualityDataSetDTO oldQdm = new CQLQualityDataSetDTO();
		populatedOldQDM(oldQdm, matValueSetTransferObject.getCqlQualityDataSetDTO());
		// Treat as regular QDM
		List<CQLQualityDataSetDTO> origAppliedQDMList = matValueSetTransferObject.getAppliedQDMList();
		List<CQLQualityDataSetDTO> tempAppliedQDMList = new ArrayList<CQLQualityDataSetDTO>();
		tempAppliedQDMList.addAll(matValueSetTransferObject.getAppliedQDMList());
		// Removing the QDS that is being modified from the
		// tempAppliedQDMList.
		Iterator<CQLQualityDataSetDTO> iterator = tempAppliedQDMList.iterator();
		while (iterator.hasNext()) {
			CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(matValueSetTransferObject.getCqlQualityDataSetDTO().getUuid())) {
				iterator.remove();
				break;
			}
		}
		matValueSetTransferObject.setAppliedQDMList(tempAppliedQDMList);

		if (!isDuplicate(matValueSetTransferObject, true)) {
			matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
			CQLQualityDataSetDTO qds = matValueSetTransferObject.getCqlQualityDataSetDTO();
			MatValueSet matValueSet = matValueSetTransferObject.getMatValueSet();
			qds.setOid(matValueSet.getID());
			qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			qds.setCodeListName(matValueSet.getDisplayName());
			if (matValueSet.isGrouping()) {
				qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
			} else {
				qds.setTaxonomy(matValueSet.getCodeSystemName());
			}
			
			qds.setVersion(getValueSetVersion(matValueSetTransferObject.isVersion(), 
					matValueSetTransferObject.getCqlQualityDataSetDTO().getRelease(), matValueSetTransferObject.getMatValueSet().getVersion()));
			
			CQLQualityDataModelWrapper wrapper = modifyAppliedElementList(qds,
					(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());

			result.setSuccess(true);
			result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
			result.setCqlQualityDataSetDTO(qds);
		} else {
			matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}
		return result;
	}

	@Override
	public SaveUpdateCQLResult updateCQLLookUpTag(String xml, CQLQualityDataSetDTO modifyWithDTO,
			final CQLQualityDataSetDTO modifyDTO) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		XmlProcessor processor = new XmlProcessor(xml);

		// XPath Expression to find all elementRefs in elementLookUp for to
		// be modified QDM.
		String XPATH_EXPRESSION_VALUESETS = "//cqlLookUp/valuesets/valueset[@uuid='" + modifyDTO.getUuid() + "']";
		try {
			NodeList nodesValuesets = (NodeList) processor.findNodeList(processor.getOriginalDoc(),
					XPATH_EXPRESSION_VALUESETS);
			for (int i = 0; i < nodesValuesets.getLength(); i++) {
				Node newNode = nodesValuesets.item(i);
				newNode.getAttributes().getNamedItem("originalName").setNodeValue(modifyWithDTO.getOriginalCodeListName());
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getOriginalCodeListName());
				newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem("codeSystemName")
							.setNodeValue(modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem("version").setNodeValue(getValueSetVersion(true, modifyWithDTO.getRelease(), modifyWithDTO.getVersion()));
				if (newNode.getAttributes().getNamedItem("release") != null) {
					newNode.getAttributes().getNamedItem("release").setNodeValue(modifyWithDTO.getRelease());
					newNode.getAttributes().getNamedItem("program").setNodeValue(modifyWithDTO.getProgram());
				}	
				
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
				}
				if ((newNode.getAttributes().getNamedItem("suffix") == null)){
					if(modifyDTO.getSuffix() != null && !modifyDTO.getSuffix().isEmpty()){
						Attr attrNode = processor.getOriginalDoc().createAttribute("suffix");
						attrNode.setNodeValue(modifyWithDTO.getSuffix());
						newNode.getAttributes().setNamedItem(attrNode);
						newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getOriginalCodeListName()+" ("+modifyWithDTO.getSuffix()+")");
					}
					
				} else {
					if(modifyDTO.getSuffix() != null && !modifyDTO.getSuffix().isEmpty()){
						newNode.getAttributes().getNamedItem("suffix").setNodeValue(modifyDTO.getSuffix());
						newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getOriginalCodeListName()+" ("+modifyWithDTO.getSuffix()+")");
					} else  {
						if(newNode.getAttributes().getNamedItem("suffix") != null && modifyDTO.getSuffix()== null) {
							newNode.getAttributes().removeNamedItem("suffix");
						}
					}
				}
			}
			result.setSuccess(true);
			result.setXml(processor.transform(processor.getOriginalDoc()));
		} catch (XPathExpressionException e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Update user define QDM in element look up.
	 *
	 * @param matValueSetTransferObject
	 *            the mat value set transfer object
	 * @return the save update code list result
	 */
	private SaveUpdateCQLResult modifyUserDefineValuesetInCQLLookUp(
			CQLValueSetTransferObject matValueSetTransferObject) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator();
		List<String> messageList = new ArrayList<String>();
		validator.validate(matValueSetTransferObject);
		if (messageList.size() == 0) {
			if (!isDuplicate(matValueSetTransferObject, false)) {
				ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
				wrapper.setQualityDataDTO(qdsList);
				CQLQualityDataSetDTO qds = matValueSetTransferObject.getCqlQualityDataSetDTO();
				qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
				qds.setId(UUID.randomUUID().toString());
				qds.setCodeListName(matValueSetTransferObject.getCodeListSearchDTO().getName());
				qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
				qds.setVersion("1.0");
				wrapper = modifyAppliedElementList(qds,
						(ArrayList<CQLQualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());
				String qdmXMLString = generateXmlForAppliedValueset(wrapper);
				result.setSuccess(true);
				result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
				result.setXml(qdmXMLString);
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
	 * @param qualityDataSetDTOWrapper
	 *            the quality data set DTO wrapper
	 * @return the string
	 */
	private String generateXmlForAppliedValueset(final CQLQualityDataModelWrapper qualityDataSetDTOWrapper) {
		logger.info("addNewAppliedQDMInMeasureXML Method Call Start.");
		ByteArrayOutputStream stream = createNewXML(qualityDataSetDTOWrapper);
		int startIndex = stream.toString().indexOf("<valueset ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("addNewAppliedQDMInMeasureXML Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	private String generateXmlForAppliedCode(CQLCodeWrapper wrapper) {
		logger.info("generateXmlForAppliedCode Method Call Start.");
		ByteArrayOutputStream stream = createNewCodeXML(wrapper);
		int startIndex = stream.toString().indexOf("<code ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("generateXmlForAppliedCode Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	private String generateXmlForAppliedCodeSystem(CQLCodeSystemWrapper wrapper) {
		logger.info("generateXmlForAppliedCodeSystem Method Call Start.");
		ByteArrayOutputStream stream = createNewCodeSystemXML(wrapper);
		int startIndex = stream.toString().indexOf("<codeSystem ", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex + 2);
		logger.debug("generateXmlForAppliedCodeSystem Method Call xmlString :: " + xmlString);
		return xmlString;
	}

	/**
	 * Creates the new XML.
	 *
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream createNewXML(final CQLQualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In CQLServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("ValueSetsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.debug("Marshalling of QualityDataSetDTO is successful.." + stream.toString());
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
		logger.info("Exiting CQLServiceImpl.createXml()");
		return stream;
	}

	private ByteArrayOutputStream createNewCodeXML(final CQLCodeWrapper wrapper) {
		logger.info("In CQLServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.debug("Marshalling of CQLCode is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLMapping.xml" + e);
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
		logger.info("Exiting CQLServiceImpl.createNewCodeXml()");
		return stream;
	}

	private ByteArrayOutputStream createNewCodeSystemXML(final CQLCodeSystemWrapper wrapper) {
		logger.info("In CQLServiceImpl.createNewCodeSystemXML()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("CodeSystemsMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(wrapper);
			logger.debug("Marshalling of CQLCode is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load CQLMapping.xml" + e);
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
		logger.info("Exiting CQLServiceImpl.createNewCodeSystemXML()");
		return stream;
	}

	/**
	 * Modify applied element list.
	 *
	 * @param dataSetDTO
	 *            the data set DTO
	 * @param appliedQDM
	 *            the applied QDM
	 * @return the CQL quality data model wrapper
	 */
	private CQLQualityDataModelWrapper modifyAppliedElementList(final CQLQualityDataSetDTO dataSetDTO,
			final ArrayList<CQLQualityDataSetDTO> appliedQDM) {
		CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
		Iterator<CQLQualityDataSetDTO> iterator = appliedQDM.iterator();
		while (iterator.hasNext()) {
			CQLQualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(dataSetDTO.getUuid())) {
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
	 * @param oldQdm
	 *            the old qdm
	 * @param qualityDataSetDTO
	 *            the quality data set DTO
	 */
	private void populatedOldQDM(CQLQualityDataSetDTO oldQdm, CQLQualityDataSetDTO qualityDataSetDTO) {
		oldQdm.setCodeListName(qualityDataSetDTO.getCodeListName());
		oldQdm.setSuffix(qualityDataSetDTO.getSuffix());
		oldQdm.setOriginalCodeListName(qualityDataSetDTO.getOriginalCodeListName());
		oldQdm.setOid(qualityDataSetDTO.getOid());
		oldQdm.setUuid(qualityDataSetDTO.getUuid());
		oldQdm.setVersion(qualityDataSetDTO.getVersion());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * mat.client.measure.service.CQLService#getCQLValusets(java.lang.String)
	 */
	@Override
	public CQLQualityDataModelWrapper getCQLValusets(String measureId,
			CQLQualityDataModelWrapper cqlQualityDataModelWrapper) {
		MeasureXmlModel model = getService().getMeasureXmlForMeasure(measureId);
		String xmlString = model.getXml();
		List<CQLQualityDataSetDTO> cqlQualityDataSetDTOs = CQLUtilityClass
				.sortCQLQualityDataSetDto(getCQLData(xmlString).getCqlModel().getAllValueSetList());
		cqlQualityDataModelWrapper.setQualityDataDTO(cqlQualityDataSetDTOs);

		return cqlQualityDataModelWrapper;
	}

	@Override
	public CQLCodeWrapper getCQLCodes(String xmlString) {
		CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
		if (xmlString != null && !xmlString.isEmpty()) {
			SaveUpdateCQLResult parsedResult = getCQLData(xmlString);
			CQLModel cqlModel = parsedResult.getCqlModel();
			List<CQLCode> allCodes = cqlModel.getCodeList();
			if (parsedResult.getCqlErrors().isEmpty()) {
				GetUsedCQLArtifactsResult artifactsResult = parsedResult.getUsedCQLArtifacts();
				List<String> usedCodes = artifactsResult.getUsedCQLcodes();
				for (CQLCode code : allCodes) {
					if (usedCodes.contains(code.getDisplayName())) {
						code.setUsed(true);
					}
				}
			} else {
				for (CQLCode code : allCodes) {
					code.setUsed(false);
				}
			}
			List<CQLCode> cqlCodeDtos = CQLUtilityClass.sortCQLCodeDTO(allCodes);
			cqlCodeWrapper.setCqlCodeList(cqlCodeDtos);
		}

		return cqlCodeWrapper;
	}

	public CQLLibraryDAO getCqlLibraryDAO() {
		return cqlLibraryDAO;
	}

	public void setCqlLibraryDAO(CQLLibraryDAO cqlLibraryDAO) {
		this.cqlLibraryDAO = cqlLibraryDAO;
	}

	@Override
	public SaveUpdateCQLResult deleteInclude(String xml, CQLIncludeLibrary toBeModifiedIncludeObj,
			List<CQLIncludeLibrary> viewIncludeLibrarys) {
		SaveUpdateCQLResult result = new SaveUpdateCQLResult();
		CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
		System.out.println("DELETE Include CLICK " + toBeModifiedIncludeObj.getAliasName());

		CQLModel cqlModel = new CQLModel();
		result.setCqlModel(cqlModel);
		XmlProcessor processor = new XmlProcessor(xml);

		if (xml != null) {
			String XPATH_EXPRESSION_CQLLOOKUP_INCLUDE = "//cqlLookUp//includeLibrary[@id='"
					+ toBeModifiedIncludeObj.getId() + "']";
			System.out.println("XPATH: " + XPATH_EXPRESSION_CQLLOOKUP_INCLUDE);
			try {
				Node includeNode = processor.findNode(processor.getOriginalDoc(), XPATH_EXPRESSION_CQLLOOKUP_INCLUDE);

				if (includeNode != null) {
					System.out.println("FOUND NODE");

					// remove from xml
					Node deletedNode = includeNode.getParentNode().removeChild(includeNode);
					System.out.println(deletedNode.getAttributes().getNamedItem("name").toString());
					processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
					result.setXml(processor.getOriginalXml());


					// remove from library list
					viewIncludeLibrarys.remove(toBeModifiedIncludeObj);
					wrapper.setCqlIncludeLibrary(viewIncludeLibrarys);
					result.setSuccess(true);
					result.setIncludeLibrary(toBeModifiedIncludeObj);
				}

				else {
					System.out.println("NOT FOUND NODE");
					result.setSuccess(false);
					result.setFailureReason(SaveUpdateCQLResult.NODE_NOT_FOUND);
				}

			} catch (XPathExpressionException e) {
				result.setSuccess(false);
				e.printStackTrace();
			}
		}

		if (result.isSuccess() && (wrapper.getCqlIncludeLibrary().size() > 0)) {
			result.getCqlModel().setCqlIncludeLibrarys(sortIncludeLibList(wrapper.getCqlIncludeLibrary()));
			CQLUtil.getIncludedCQLExpressions(cqlModel, cqlLibraryDAO);
			System.out.println(result.getXml());
			System.out.println(result.isSuccess());
		}

		return result;
	}

	/**
	 * Delete CQL Association.
	 *
	 * @param currentObj
	 *            the current obj
	 * @param associatedWithId
	 *            the measure id
	 */
	@Override
	public void deleteCQLAssociation(CQLIncludeLibrary currentObj, String associatedWithId) {
		CQLLibraryAssociation cqlLibraryAssociation = new CQLLibraryAssociation();
		cqlLibraryAssociation.setCqlLibraryId(currentObj.getCqlLibraryId());
		cqlLibraryAssociation.setAssociationId(associatedWithId);
		cqlLibraryAssociationDAO.deleteAssociation(cqlLibraryAssociation);
	}
	
	
	/**
	 * Find CQL Parsing Errors per CQL Expression.
	 * 
	 * @param cqlModel
	 * @param parsedCQL
	 * @return Map.
	 */
	private Map<String , List<CQLErrors>> getCQLErrorsPerExpressions(CQLModel cqlModel , SaveUpdateCQLResult parsedCQL ){
		
		Map<String , List<CQLErrors>> expressionMapWithError = new HashMap<String,List<CQLErrors>>();
		List<CQLExpressionObject> cqlExpressionObjects = getCQLExpressionObjectListFromCQLModel(cqlModel);
		 
		for(CQLExpressionObject expressionObject : cqlExpressionObjects){
			int fileStartLine = -1;
			int fileEndLine = -1;
			int size = 0;
			String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, expressionObject.getName()).toString();
			
			String wholeDef = ""; 
			String expressionToFind = null;
			
			switch (expressionObject.getType()) {
			case "Parameter":
				expressionToFind = "parameter \"" + expressionObject.getName() + "\"";
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				wholeDef = expressionObject.getName() + " " + expressionObject.getLogic();
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;
			case "Definition":
				expressionToFind = "define \"" + expressionObject.getName() + "\"";
				wholeDef = expressionObject.getName() + " :\n" + expressionObject.getLogic();
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;
			case "Function":
				expressionToFind = "define function \"" + expressionObject.getName() + "\"";
				wholeDef = expressionObject.getName() + " :\n" + expressionObject.getLogic();
				fileStartLine = findStartLineForCQLExpressionInCQLFile(cqlFileString, expressionToFind);
				size = countLines(wholeDef);
				fileEndLine = fileStartLine + size - 1;
				break;

			default:
				break;
			}
			
			
			System.out.println("fileStartLine of expression ===== "+ fileStartLine);
			System.out.println("fileEndLine of expression ===== "+ fileEndLine);
		
			List<CQLErrors> errors = new ArrayList<CQLErrors>();
			for (CQLErrors cqlError : parsedCQL.getCqlErrors()) {
				int errorStartLine = cqlError.getStartErrorInLine();
				String errorMsg = (cqlError.getErrorMessage() == null) ? "" : cqlError.getErrorMessage();

				if ((errorStartLine >= fileStartLine && errorStartLine <= fileEndLine)) {
					cqlError.setStartErrorInLine(errorStartLine - fileStartLine - 1);
					cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - fileStartLine - 1);
					cqlError.setErrorMessage(errorMsg);
					if(cqlError.getStartErrorInLine() ==-1){
						cqlError.setStartErrorInLine(0);
					}
					errors.add(cqlError);
				}
			}
			expressionMapWithError.put(expressionObject.getName(), errors);
			
		}

		return expressionMapWithError;
		
		
	}

	/**
	 * This method finds the start line of each expression in CQL File.
	 * @param fileStartLine
	 * @param cqlFileString
	 * @param expressionToFind
	 * @return integer value.
	 */
	private int findStartLineForCQLExpressionInCQLFile( String cqlFileString,
			String expressionToFind) {
		int fileStartLine =-1;
		try {
			LineNumberReader rdr = new LineNumberReader(new StringReader(cqlFileString));
			String line = null;
			System.out.println("Expression to Find :: " + expressionToFind);
			while((line = rdr.readLine()) != null) {
				if (line.indexOf(expressionToFind) >= 0) {
		        	fileStartLine =rdr.getLineNumber();
		        	break;
		        }
			}
			rdr.close();
		   // cqlFile.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileStartLine;
	}
	
	
	/**
	 * This method iterates through CQLModel object and generates CQLExpressionObject with type, Name and Logic.
	 * @param cqlModel
	 * @return List<CQLExpressionObject>.
	 */
	private List<CQLExpressionObject> getCQLExpressionObjectListFromCQLModel(CQLModel cqlModel) {
		
		

		List<CQLExpressionObject> cqlExpressionObjects = new ArrayList<CQLExpressionObject>();
		
		
		for (CQLParameter cqlParameter : cqlModel.getCqlParameters()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Parameter",
					cqlParameter.getParameterName(), cqlParameter.getParameterLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}
		for (CQLDefinition cqlDefinition : cqlModel.getDefinitionList()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Definition",
					cqlDefinition.getDefinitionName(), cqlDefinition.getDefinitionLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}

		for (CQLFunctions cqlFunction : cqlModel.getCqlFunctions()) {
			CQLExpressionObject cqlExpressionObject = new CQLExpressionObject("Function", cqlFunction.getFunctionName(),
					cqlFunction.getFunctionLogic());
			cqlExpressionObjects.add(cqlExpressionObject);
		}
		
		return cqlExpressionObjects;
		
	}

	@Override
	public CQLModel parseCQL(String cqlBuilder) {
		CQLModel cqlModel = new CQLModel();
		return cqlModel;
	}	
}
