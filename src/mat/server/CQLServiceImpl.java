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
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.client.measure.service.CQLService;
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
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;
import mat.shared.SaveUpdateCQLResult;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
* The Class CQLServiceImpl.
*/
public class CQLServiceImpl implements CQLService {

       public static final String PATIENT_CHARACTERSTICS_EXPIRED = "Patient Characteristic Expired";

       public static final String DEAD = "Dead";

       public static final String PATIENT_CHARACTERISTIC_BIRTHDATE = "Patient Characteristic Birthdate";

       public static final String BIRTHDATE = "Birthdate";

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
                                  + "displayName=\"Birth date\" " + "codeSystemVersion=\"2.46\" id=\"777\" " + "/>"

                                  + "<code codeName=\"Dead\" codeOID=\"419099009\" codeSystemName=\"SNOMEDCT\" "
                                  + "displayName=\"Dead\" " + "codeSystemVersion=\"2016-03\" id=\"777\" " + "/>"

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
              /*
              * cqlLexer lexer = new cqlLexer(new ANTLRInputStream(cqlBuilder));
              * System.out.println(cqlBuilder); CommonTokenStream tokens = new
              * CommonTokenStream(lexer); cqlParser parser = new cqlParser(tokens);
              * MATCQLParser cqlParser = new MATCQLParser(); CQLErrorListener
              * cqlErrorListener = new CQLErrorListener(); MATCQLListener cqlListener
              * = new MATCQLListener(cqlParser); cqlListener.setCqlModel(cqlModel);
              * cqlListener.setParser(cqlListener); cqlListener.setLexer(lexer);
              * parser.addParseListener(cqlListener); cqlListener.setTokens(tokens);
              * parser.setBuildParseTree(true);
              * 
               * ParserRuleContext tree = parser.logic();
              * parser.notifyErrorListeners("");
              * 
               * System.out.println(parser.getNumberOfSyntaxErrors());
              * System.out.println(cqlErrorListener.getErrors());
              * 
               * cqlModel = cqlListener.getCqlModel();
              */

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
       public SaveUpdateCQLResult saveAndModifyCQLGeneralInfo(String xml, String libraryName) {

              /*
              * MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
              * currentMeasureId);
              */
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
                                  /*
                                  * xmlModel.setXml(processor.transform(processor
                                  * .getOriginalDoc()));
                                  * getService().saveMeasureXml(xmlModel);
                                  */
                                  result.setXml(processor.transform(processor.getOriginalDoc()));
                                  result.setSuccess(true);
                           }

                     } catch (XPathExpressionException e) {
                           e.printStackTrace();
                     }

			result.setSuccess(true);
			// CQLLibraryModel cqlLibraryModel = new CQLLibraryModel();
			result.getCqlModel().setLibraryName(libraryName);
			/* result.getCqlModel().setLibrary(cqlLibraryModel); */
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
                     List<CQLFunctions> functionsList) {

              /*
              * if(MatContext.get().getMeasureLockService().checkForEditPermission())
              * { return null; }
              * 
               * MeasureXmlModel measureXMLModel =
              * getService().getMeasureXmlForMeasure( measureId);
              */
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
              CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();
              CQLModelValidator validator = new CQLModelValidator();
              boolean isDuplicate = false;
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

                                         String XPATH_EXPRESSION_CQLLOOKUP_FUNCTION = "//cqlLookUp//function[@id='"
                                                       + toBeModifiedObj.getId() + "']";
                                         try {
                                                Node nodeFunction = processor.findNode(processor.getOriginalDoc(),
                                                              XPATH_EXPRESSION_CQLLOOKUP_FUNCTION);

                                                if (nodeFunction != null) {
                                                       // Server Side check to see if Function is used and context is changed from developer's tool
                                                       // since if function is used we disable the context radio buttons then only allow to modify
                                                       // if either cql is invalid or function is not used.
                                                       String oldExpressionName = nodeFunction.getAttributes().getNamedItem("name").getNodeValue();
                                                       String oldExpressionLogic = nodeFunction.getChildNodes().item(0).getTextContent();
                                                       String oldContextValue = nodeFunction.getAttributes().getNamedItem("context").getNodeValue();
                                                       if(MatContextServiceUtil.get().isMeasure() && !oldContextValue.equalsIgnoreCase(currentObj.getContext())) {
                                                              parseCQLExpressionForErrors(result, xml, "define function" + " \"" + oldExpressionName  + "\"",
                                                                           oldExpressionLogic, oldExpressionName, "Function");
                                                              if(result.getUsedCQLArtifacts().getUsedCQLFunctions().contains(oldExpressionName)){
                                                                     currentObj.setContext(oldContextValue);
                                                              }
                                                              
                                                       } 
                                                       String cqlString = createFunctionsXML(currentObj);
                                                       processor.removeFromParent(nodeFunction);
                                                       processor.appendNode(cqlString, "function", XPATH_EXPRESSION_FUNCTIONS);

                                                       String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());

                                                       String cqlExpressionName = "define function" + " \"" + currentObj.getFunctionName()
                                                                     + "\"";
                                                       result.setXml(finalUpdatedXmlString);

                                                       parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
                                                                     currentObj.getFunctionLogic(), currentObj.getFunctionName(), "Function");

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
                                         String cqlString = createFunctionsXML(currentObj);

                                         try {
                                                Node nodeFunctions = processor.findNode(processor.getOriginalDoc(),
                                                              XPATH_EXPRESSION_FUNCTIONS);
                                                if (nodeFunctions != null) {
                                                       try {
                                                              processor.appendNode(cqlString, "function", XPATH_EXPRESSION_FUNCTIONS);
                                                              processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
                                                              String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
                                                              String cqlExpressionName = "define function" + " \"" + currentObj.getFunctionName()
                                                                           + "\"";

                                                              result.setXml(finalUpdatedXmlString);
                                                              parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
                                                                            currentObj.getFunctionLogic(), currentObj.getFunctionName(), "Function");

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
                                         } catch (XPathExpressionException e) {
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
                     CQLParameter currentObj, List<CQLParameter> parameterList) {

              /*
              * if(MatContext.get().getMeasureLockService().checkForEditPermission())
              * { return null; }
              * 
               * MeasureXmlModel measureXMLModel =
              * getService().getMeasureXmlForMeasure( measureId);
              */
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
              CQLParametersWrapper wrapper = new CQLParametersWrapper();
              CQLModelValidator validtor = new CQLModelValidator();
              boolean isDuplicate = false;
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

                           if (!isDuplicate) {

                                  logger.debug(" CQLServiceImpl: saveAndModifyParameters Start :  ");

                                  String XPATH_EXPRESSION_CQLLOOKUP_PARAMETER = "//cqlLookUp//parameter[@id='"
                                                + toBeModifiedObj.getId() + "']";
                                  try {
                                         Node nodeParameter = processor.findNode(processor.getOriginalDoc(),
                                                       XPATH_EXPRESSION_CQLLOOKUP_PARAMETER);

                                         if (nodeParameter != null) {
                                                /*nodeParameter.getAttributes().getNamedItem("name")
                                                              .setNodeValue(currentObj.getParameterName());

                                                Node logicNode = nodeParameter.getFirstChild();
                                                logicNode.setTextContent(currentObj.getParameterLogic());
                                                Node commentNode = nodeParameter.getLastChild();
                                                commentNode.setTextContent(currentObj.getCommentString());
                                                String finalUpdatedString = processor.transform(processor.getOriginalDoc());
                                                
                                                */
                                        	 
                                        	 String cqlString = createParametersXML(currentObj);
                                             processor.removeFromParent(nodeParameter);
                                             processor.appendNode(cqlString, "parameter", XPATH_EXPRESSION_PARAMETERS);

                                             String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());

                                             
                                                String cqlExpressionName = "parameter" + " \"" + currentObj.getParameterName() + "\"";

                                                result.setXml(finalUpdatedXmlString);
                                                cqlModel = CQLUtilityClass.getCQLStringFromXML(xml);

                                                parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
                                                              currentObj.getParameterLogic(), currentObj.getParameterName(), "Parameter");

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
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
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

                           if (!isDuplicate) {

                                  String cqlString = createParametersXML(currentObj);

                                  try {
                                         Node nodeParameters = processor.findNode(processor.getOriginalDoc(),
                                                       XPATH_EXPRESSION_PARAMETERS);
                                         if (nodeParameters != null) {
                                                try {
                                                       processor.appendNode(cqlString, "parameter", XPATH_EXPRESSION_PARAMETERS);
                                                       processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
                                                       String finalUpdatedString = processor.transform(processor.getOriginalDoc());
                                                       String cqlExpressionName = "parameter" + " \"" + currentObj.getParameterName() + "\"";

                                                       result.setXml(finalUpdatedString);
                                                       parseCQLExpressionForErrors(result, finalUpdatedString, cqlExpressionName,
                                                                     currentObj.getParameterLogic(), currentObj.getParameterName(), "Parameter");

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
                                  } catch (XPathExpressionException e) {
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
                     CQLDefinition currentObj, List<CQLDefinition> definitionList) {
              /*
              * if(MatContext.get().getMeasureLockService().checkForEditPermission())
              * { return null; } MeasureXmlModel measureXMLModel =
              * getService().getMeasureXmlForMeasure( measureId);
              */
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
              CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();
              CQLModelValidator validator = new CQLModelValidator();
              boolean isDuplicate = false;
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

                           if (!isDuplicate) {

                                  logger.debug(" CQLServiceImpl: saveAndModifyDefinitions Start :  ");
                                  String XPATH_EXPRESSION_CQLLOOKUP_DEFINITION = "//cqlLookUp//definition[@id='"
                                                + toBeModifiedObj.getId() + "']";
                                  try {
                                         Node nodeDefinition = processor.findNode(processor.getOriginalDoc(),
                                                       XPATH_EXPRESSION_CQLLOOKUP_DEFINITION);
                                         if (nodeDefinition != null) {
                                                
                                                // Server Side check to see if Function is used and context is changed from developer's tool
                                                // since if function is used we disable the context radio buttons then only allow to modify
                                                // if either cql is invalid or function is not used.
                                                String oldExpressionName = nodeDefinition.getAttributes().getNamedItem("name").getNodeValue();
                                                String oldExpressionLogic = nodeDefinition.getChildNodes().item(0).getTextContent();
                                                String oldContextValue = nodeDefinition.getAttributes().getNamedItem("context").getNodeValue();
                                                if(MatContextServiceUtil.get().isMeasure() && !oldContextValue.equalsIgnoreCase(currentObj.getContext())) {
                                                       parseCQLExpressionForErrors(result, xml, "define" + " \"" + currentObj.getDefinitionName() + "\"",
                                                                     oldExpressionLogic, oldExpressionName, "Definition");
                                                       if(result.getUsedCQLArtifacts().getUsedCQLDefinitions().contains(oldExpressionName)){
                                                              currentObj.setContext(oldContextValue);
                                                       }
                                                       
                                                } 
                                                /*nodeDefinition.getAttributes().getNamedItem("context")
                                                              .setNodeValue(currentObj.getContext());
                                                nodeDefinition.getAttributes().getNamedItem("name")
                                                              .setNodeValue(currentObj.getDefinitionName());
                                                Node logicNode = nodeDefinition.getFirstChild();
                                                logicNode.setTextContent(currentObj.getDefinitionLogic());
                                                Node commentNode = nodeDefinition.getLastChild();
                                                commentNode.setTextContent(currentObj.getCommentString());*/
                                                
                                                
                                                // to update Definition Name on RiskAdjusment
                                                // Variable Section
                                                
                                                String cqlString = createDefinitionsXML(currentObj);
                                                processor.removeFromParent(nodeDefinition);
                                                processor.appendNode(cqlString, "definition", XPATH_EXPRESSION_DEFINTIONS);
                                                
                                                updateRiskAdjustmentVariables(processor, toBeModifiedObj, currentObj);
                                                String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());

                                                String cqlExpressionName = "define" + " \"" + currentObj.getDefinitionName() + "\"";

                                                result.setXml(finalUpdatedXmlString);
                                                parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
                                                              currentObj.getDefinitionLogic(), currentObj.getDefinitionName(), "Definition");

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
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
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

                           parseCQLExpressionForErrors(result, xml, cqlExpressionName, currentObj.getDefinitionLogic(),
                                         currentObj.getDefinitionName(), "Definition");
                           isDuplicate = validator.validateForSpecialChar(currentObj.getDefinitionName());
                           if (isDuplicate) {
                                  result.setSuccess(false);
                                  result.setFailureReason(SaveUpdateCQLResult.NO_SPECIAL_CHAR);
                                  return result;
                           }

                           isDuplicate = isDuplicateIdentifierName(currentObj.getDefinitionName(), xml);
                           if (!isDuplicate) {
                                  String cqlString = createDefinitionsXML(currentObj);
                                  
                                  try {
                                         Node nodeDefinitions = processor.findNode(processor.getOriginalDoc(),
                                                       XPATH_EXPRESSION_DEFINTIONS);
                                         if (nodeDefinitions != null) {
                                                try {
                                                       processor.appendNode(cqlString, "definition", XPATH_EXPRESSION_DEFINTIONS);
                                                       processor.setOriginalXml(processor.transform(processor.getOriginalDoc()));
                                                       String finalUpdatedXmlString = processor.transform(processor.getOriginalDoc());
                                                       cqlExpressionName = "define" + " \"" + currentObj.getDefinitionName() + "\"";

                                                       result.setXml(finalUpdatedXmlString);
                                                       parseCQLExpressionForErrors(result, finalUpdatedXmlString, cqlExpressionName,
                                                                     currentObj.getDefinitionLogic(), currentObj.getDefinitionName(), "Definition");

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

                                  } catch (XPathExpressionException e) {
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

       /*
       * (non-Javadoc)
       * 
        * @see
       * mat.client.measure.service.CQLService#saveAndModifyIncludeLibray(java.
       * lang.String, mat.model.cql.CQLIncludeLibrary,
       * mat.model.cql.CQLIncludeLibrary, java.util.List)
       */
       @Override
       public SaveUpdateCQLResult saveIncludeLibrayInCQLLookUp(String xml, CQLIncludeLibrary toBeModifiedObj,
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
                                                                           // functionality

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
                                         // TODO Auto-generated catch block
                                         e.printStackTrace();
                                  } catch (SAXException e) {
                                         // TODO Auto-generated catch block
                                         e.printStackTrace();
                                  } catch (IOException e) {
                                         // TODO Auto-generated catch block
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
                     CQLUtil.getIncludedCQLExpressions(cqlModel,cqlLibraryDAO);
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
                     // TODO Auto-generated catch block
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
                           // logger.info(e.printStackTrace());
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
       public SaveUpdateCQLResult deleteDefinition(String xml, CQLDefinition toBeDeletedObj, CQLDefinition currentObj,
                     List<CQLDefinition> definitionList) {
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLDefinitionsWrapper wrapper = new CQLDefinitionsWrapper();

              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
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
                           Node parentNode = valueSetElements.getParentNode();
                           parentNode.removeChild(valueSetElements);
                           result.setSuccess(true);
                           result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
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
       public SaveUpdateCQLResult deleteCode(String xml , String toBeDeletedCodeId){
              logger.info("Start deleteCode : ");
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              XmlProcessor xmlProcessor = new XmlProcessor(xml);
              try {
                     String xpathforCodeNode = "//cqlLookUp//code[@id='" + toBeDeletedCodeId + "']";
                     Node codeNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), xpathforCodeNode);
                     if (codeNode != null) {
                           Node parentNode = codeNode.getParentNode();
                           parentNode.removeChild(codeNode);
                           result.setSuccess(true);
                           result.setXml(xmlProcessor.transform(xmlProcessor.getOriginalDoc()));
                           result.setCqlCodeList(getCQLCodes(result.getXml()).getCqlCodeList());
                           /*CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(result.getXml());
                           
                           SaveUpdateCQLResult parseResult = parseCQLLibraryForErrors(cqlModel);*/
                           
                     } else {
                           logger.info("Unable to find the selected Code element with id in deleteCode : "
                                         + toBeDeletedCodeId);
                           result.setSuccess(false);
                     }
              } catch (XPathExpressionException e) {
                     result.setSuccess(false);
                     logger.info("Error in method deleteCode: " + e.getMessage());
              }

              logger.info("END deleteCode : ");
              
              return result;
       }

       /*
       * (non-Javadoc)
       * 
        * @see
       * mat.client.measure.service.CQLService#deleteFunctions(java.lang.String,
       * mat.model.cql.CQLFunctions, mat.model.cql.CQLFunctions, java.util.List)
       */
       @Override
       public SaveUpdateCQLResult deleteFunctions(String xml, CQLFunctions toBeDeletedObj, CQLFunctions currentObj,
                     List<CQLFunctions> functionsList) {

              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLFunctionsWrapper wrapper = new CQLFunctionsWrapper();

              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
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
                                  /*
                                  * xmlModel.setXml(processor.getOriginalXml());
                                  * getService().saveMeasureXml(xmlModel);
                                  */

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
       public SaveUpdateCQLResult deleteParameter(String xml, CQLParameter toBeDeletedObj, CQLParameter currentObj,
                     List<CQLParameter> parameterList) {
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLParametersWrapper wrapper = new CQLParametersWrapper();

              // MeasureXmlModel xmlModel =
              // getService().getMeasureXmlForMeasure(measureId);
              CQLModel cqlModel = new CQLModel();
              result.setCqlModel(cqlModel);
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
                                  /*
                                  * xmlModel.setXml(processor.getOriginalXml());
                                  * getService().saveMeasureXml(xmlModel);
                                  */

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
       @Override
       public String getDefaultExpansionIdentifier(String xml) {
              String defaultExpId = null;
              if (xml != null) {
                     XmlProcessor processor = new XmlProcessor(xml);

                     String XPATH_VSAC_EXPANSION_IDENTIFIER = "//cqlLookUp/valuesets/@vsacExpIdentifier";

                     try {
                           Node vsacExpIdAttr = (Node) XPathFactory.newInstance().newXPath().evaluate(XPATH_VSAC_EXPANSION_IDENTIFIER, processor.getOriginalDoc(),
                                         XPathConstants.NODE);
                           if (vsacExpIdAttr != null) {
                                  defaultExpId = vsacExpIdAttr.getNodeValue();
                           }
                     } catch (XPathExpressionException e) {
                           e.printStackTrace();
                     }
              }
              if(defaultExpId != null){
                     return defaultExpId;
              } else{
                     return "";
              }
       }
       
       /*
       * (non-Javadoc)
       * 
        * @see mat.client.measure.service.CQLService#getCQLData(java.lang.String)
       */
       @Override
       public SaveUpdateCQLResult getCQLData(String xmlString) {

              // SaveUpdateCQLResult result = new SaveUpdateCQLResult();

              CQLModel cqlModel = new CQLModel();
              cqlModel = CQLUtilityClass.getCQLStringFromXML(xmlString);

              SaveUpdateCQLResult parsedCQL = null;
              /*
              * if(cqlModel.getCqlIncludeLibrarys().size() == 0){ parsedCQL =
              * parseCQLStringForError(cqlFileString); }else { parsedCQL =
              * parseCQLLibraryForErrors(cqlModel); }
              */
              parsedCQL = parseCQLLibraryForErrors(cqlModel);

              if (parsedCQL.getCqlErrors().isEmpty()) {
                     parsedCQL.setUsedCQLArtifacts(getUsedCQlArtifacts(xmlString));
            	  	 setUsedValuesets(parsedCQL, cqlModel);
            	  	 setUsedCodes(parsedCQL, cqlModel);
                     boolean isValid = isValidDataTypeUsed(parsedCQL.getUsedCQLArtifacts().getValueSetDataTypeMap(), 
                    		 parsedCQL.getUsedCQLArtifacts().getCodeDataTypeMap());;
                     parsedCQL.setDatatypeUsedCorrectly(isValid);
              }

              parsedCQL.setCqlModel(cqlModel);
              // result.setCqlErrors(parsedCQL.getCqlErrors());
              return parsedCQL;
       }

       private void setUsedCodes(SaveUpdateCQLResult parsedCQL, CQLModel cqlModel) {
		
    	   List<String> usedCodes = parsedCQL.getUsedCQLArtifacts().getUsedCQLcodes();
    	   System.out.println("used codes:"+usedCodes);
    	   for(CQLCode cqlCode : cqlModel.getCodeList()){
    		   boolean isUsed = usedCodes.contains(cqlCode.getCodeName());
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
       * Modify QDM status.
       *
       * @param cqlModel
       *            the cql model
       */
       /*
       * private static void modifyQDMStatus(CQLModel cqlModel) { for (int i = 0;
       * i < cqlModel.getValueSetList().size(); i++) {
       * cqlModel.getValueSetList().get(i).setUsed(true); } }
       */

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
                           // logger.info("unmarshalling xml..cqlLookUp " + xml);
                           details = (CQLDefinitionsWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
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
                           // logger.info(e.printStackTrace());
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
              StringBuilder cqlString = getCqlString(result.getCqlModel());
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
       /*
       * private boolean checkForCQLKeywords(String name) {
       * 
        * XmlProcessor cqlXMLProcessor = CQLTemplateXML
       * .getCQLTemplateXmlProcessor(); String XPATH_CQL_KEYWORDS =
       * "/cqlTemplate/keywords/keyword[translate(text(),'abcdefghijklmnopqrstuvwxyz',"
       * + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + name.toUpperCase() + "']"; try {
       * Node templateNode = cqlXMLProcessor.findNode(
       * cqlXMLProcessor.getOriginalDoc(), XPATH_CQL_KEYWORDS); if (templateNode
       * != null) { return true; } } catch (XPathExpressionException e) {
       * e.printStackTrace(); } return false; }
       */

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
              List<String> cqlDataTypeList = new ArrayList<String>();
              List<String> cqlTimingList = new ArrayList<String>();
              List<String> cqlFunctionList = new ArrayList<String>();
              try {
                     NodeList dataTypeNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_DATATYPES);
                     NodeList timingNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_TIMINGS);
                     NodeList functionNodeList = cqlXMLProcessor.findNodeList(cqlXMLProcessor.getOriginalDoc(), XPATH_FUNCTIONS);
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
       * @param id
       *            the measure id
       * @return true, if is duplicate identifier name
       */
       private boolean isDuplicateIdentifierName(String identifierName, String xml) {

              /*
              * MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(
              * measureId);
              */
              // boolean isInvalid = false;
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

       /**
       * Check if keyword for func arguments.
       *
       * @param toBeModifiedObj
       *            the to be modified obj
       * @param currentObj
       *            the current obj
       * @param parameterList
       *            the parameter list
       * @return the save update cql result
       */
       /*
       * private SaveUpdateCQLResult checkIfKeywordForFuncArguments(
       * SaveUpdateCQLResult result, CQLFunctions currentObj) {
       * 
        * List<CQLFunctionArgument> argList = currentObj.getArgumentList(); for
       * (int i = 0; i < argList.size(); i++) { if
       * (checkForCQLKeywords(argList.get(i).getArgumentName())) {
       * argList.get(i).setValid(true); result.setSuccess(true); } else {
       * argList.get(i).setValid(false); } }
       * 
        * if (argList.size() > 0) { currentObj.setArgumentList(argList); } else {
       * currentObj.setArgumentList(new ArrayList<CQLFunctionArgument>()); }
       * result.setFunction(currentObj); return result; }
       */

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
       private CQLFunctionsWrapper modfiyCQLFunctionList(CQLFunctions toBeModifiedObj, CQLFunctions currentObj,
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
       private CQLDefinitionsWrapper modfiyCQLDefinitionList(CQLDefinition toBeModifiedObj, CQLDefinition currentObj,
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

       /**
       * Parses the CQL def for errors.
       *
       * @param result
       *            the result
       * @param id
       *            the measure id
       * @param name
       *            the name
       * @param logic
       *            the logic
       * @return the save update CQL result
       *//*
              * private SaveUpdateCQLResult
              * parseCQLExpressionForErrors(SaveUpdateCQLResult result,
              * MeasureXmlModel measureXMLModel, String name, String logic) {
              * 
               * CQLModel cqlModel =
              * CQLUtilityClass.getCQLStringFromXML(measureXMLModel.getXml()); String
              * cqlFileString = CQLUtilityClass.getCqlString(cqlModel,
              * name).toString();
              * 
               * cqlModel.setLines(countLines(cqlFileString));
              * result.setCqlModel(cqlModel);
              * 
               * String wholeDef = name + " " + logic;
              * 
               * int endLine = CQLUtilityClass.getSize(); int size =
              * countLines(wholeDef); int startLine = endLine - size + 1;
              * 
               * result.setStartLine(startLine); result.setEndLine(endLine);
              * 
               * List<String> expressionList =
              * getExpressionListFromCqlModel(cqlModel); SaveUpdateCQLResult
              * parsedCQL = new SaveUpdateCQLResult(); parsedCQL =
              * CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO,
              * expressionList);
              * 
               * List<CQLErrors> errors = new ArrayList<CQLErrors>(); for(CQLErrors
              * cqlError : parsedCQL.getCqlErrors()){ int errorStartLine =
              * cqlError.getStartErrorInLine();
              * 
               * if((errorStartLine >= startLine && errorStartLine <= endLine)) {
              * cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() -
              * startLine); cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() -
              * startLine); cqlError.setErrorMessage(cqlError.getErrorMessage());
              * errors.add(cqlError); } }
              * 
               * result.setCqlErrors(errors);
              * result.setUsedCQLArtifacts(parsedCQL.getUsedCQLArtifacts());
              * 
               * 
               * return result; }
              */

       public SaveUpdateCQLResult parseCQLExpressionForErrors(SaveUpdateCQLResult result, String xml,
                     String cqlExpressionName, String logic, String expressionName, String expressionType) {

              CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(xml);
              String cqlFileString = CQLUtilityClass.getCqlString(cqlModel, cqlExpressionName).toString();

              cqlModel.setLines(countLines(cqlFileString));
              result.setCqlModel(cqlModel);

              String wholeDef = cqlExpressionName + " " + logic;

              int endLine = CQLUtilityClass.getSize();
              int size = countLines(wholeDef);
              int startLine = endLine - size + 1;

              result.setStartLine(startLine);
              result.setEndLine(endLine);

              List<String> expressionList = getExpressionListFromCqlModel(cqlModel);
              SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
              parsedCQL = CQLUtil.parseCQLLibraryForErrors(cqlModel, cqlLibraryDAO, expressionList);

              List<CQLErrors> errors = new ArrayList<CQLErrors>();
              for (CQLErrors cqlError : parsedCQL.getCqlErrors()) {
                     int errorStartLine = cqlError.getStartErrorInLine();
                     String errorMsg = (cqlError.getErrorMessage() == null)?"":cqlError.getErrorMessage();
                     
                     if ((errorStartLine >= startLine && errorStartLine <= endLine)) {
         				cqlError.setStartErrorInLine(cqlError.getStartErrorInLine() - startLine);
         				cqlError.setEndErrorInLine(cqlError.getEndErrorInLine() - startLine);
         				cqlError.setErrorMessage(cqlError.getErrorMessage());
         				errors.add(cqlError);
         			}
              }

              if (errors.isEmpty()) {
                     boolean isValid = findValidDataTypeUsage(expressionName, expressionType, parsedCQL);
                     result.setDatatypeUsedCorrectly(isValid);
                     if(isValid) {
                           XmlProcessor xmlProcessor = new XmlProcessor(xml);
                           CQLArtifactHolder cqlArtifactHolder = CQLUtil
                                         .getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
                     parsedCQL.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
                     parsedCQL.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
                     }
              }
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
                     isValid = isValidDataTypeUsed(cqlExpressionObject.getValueSetDataTypeMap(), cqlExpressionObject.getCodeDataTypeMap());
              }
              return isValid;
       }

       // @Override
       public boolean isValidDataTypeUsed(Map<String, List<String>> valueSetDataTypeMap, Map<String, List<String>> codeDataTypeMap) {
              if (valueSetDataTypeMap != null && !valueSetDataTypeMap.isEmpty()) {
                     for (String valueSetName : valueSetDataTypeMap.keySet()) {
                           List<String> dataTypeList = valueSetDataTypeMap.get(valueSetName);
                           String[] valueSetNameArray = valueSetName.split(Pattern.quote("|"));
                           if (valueSetNameArray.length == 3) {
                                  valueSetName = valueSetNameArray[2];
                           }
                           
                           if(!isValidDataTypeCombination(valueSetName, dataTypeList)) {
                                  return false;   
                           }
                     }
              }
              
              if(codeDataTypeMap != null && !codeDataTypeMap.isEmpty()) {
                     for (String codeName : codeDataTypeMap.keySet()) {
                           List<String> dataTypeList = codeDataTypeMap.get(codeName);
                           String[] codeNameArray = codeName.split(Pattern.quote("|"));
                           if (codeNameArray.length == 3) {
                                  codeName = codeNameArray[2];
                           }
                           
                           if(!isValidDataTypeCombination(codeName, dataTypeList)) {
                                  return false;   
                           }
                     }
              }
              
              return true;
       }
       
       /**
       * Checks if the valueset/code has a valid datatype combination. 
        * @return true if it valid, false if it is not.
       */
       private boolean isValidDataTypeCombination(String name, List<String> dataTypeList) {
              boolean isValid = true; 
              
              // check if the birthdate valueset is being used with something other than then Patient Characteristic Birthdate datatype
              if (name.equalsIgnoreCase(BIRTHDATE)) {
                 for (String dataType : dataTypeList) {
                       if (!dataType.equalsIgnoreCase(PATIENT_CHARACTERISTIC_BIRTHDATE)) {
                              return false; 
                       }
                 }
              } 
              
              // check if the dead valueset is being used with something other than the Patient Characteristic Expired datatype
              else if (name.equalsIgnoreCase(DEAD)) {
                 for (String dataType : dataTypeList) {
                       if (!dataType.equalsIgnoreCase(PATIENT_CHARACTERSTICS_EXPIRED)) {
                              return false; 
                       }
                 }
              } 
              
              // if not birthdate or dead, check if the datatype list contains Patient Characteristic Birthdate or Dead, any non Birthdate or Dead code cannot use these datatypes. 
              else if(dataTypeList.contains(PATIENT_CHARACTERISTIC_BIRTHDATE) || dataTypeList.contains(PATIENT_CHARACTERSTICS_EXPIRED)) {
            	  return false; 
              }
              
          return true; 
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

              SaveUpdateCQLResult cqlResult = CQLUtil.parseCQLLibraryForErrors(cqlModel, getCqlLibraryDAO(), exprList);

              // if there are no errors in the cql file, get the used cql artifacts
              if (cqlResult.getCqlErrors().isEmpty()) {

                     XmlProcessor xmlProcessor = new XmlProcessor(xml);
                     CQLArtifactHolder cqlArtifactHolder = CQLUtil
                                  .getCQLArtifactsReferredByPoplns(xmlProcessor.getOriginalDoc());
              cqlResult.getUsedCQLArtifacts().getUsedCQLDefinitions().addAll(cqlArtifactHolder.getCqlDefFromPopSet());
              cqlResult.getUsedCQLArtifacts().getUsedCQLFunctions().addAll(cqlArtifactHolder.getCqlFuncFromPopSet());
                     System.out.println("USED LIBRARY: " + cqlResult.getUsedCQLArtifacts().getUsedCQLLibraries());

              } else {
                     cqlResult.getUsedCQLArtifacts().setCqlErrors(cqlResult.getCqlErrors());
              }

              return cqlResult.getUsedCQLArtifacts();
       }

       /**
       * Find used value-sets.
       *
       * @param cqlFileString
       *            the cql file string
       * @param cqlModel
       *            the cql model
       */
       /*
       * private void findUsedValuesets(String cqlFileString, CQLModel cqlModel){
       * MATCQLParser matcqlParser = new MATCQLParser(); CQLFileObject
       * cqlFileObject = matcqlParser.parseCQL(cqlFileString);
       * 
        * try { CQLArtifactHolder cqlArtifactHolder =
       * CQLUtil.getUsedCQLValuesets(cqlFileObject); List<String> usedValuesets =
       * new ArrayList<String>();
       * 
        * usedValuesets.addAll(new
       * ArrayList<String>(cqlArtifactHolder.getCqlValuesetIdentifierSet()));
       * cqlModel.getValueSetList();
       * 
        * 
        * for(int i=0; i<cqlModel.getValueSetList().size(); i++){
       * CQLQualityDataSetDTO cqlDataset = cqlModel.getValueSetList().get(i);
       * 
        * if(usedValuesets.contains(cqlDataset.getCodeListName())){
       * cqlDataset.setUsed(true); } }
       * 
        * 
        * } catch (XPathExpressionException e) { logger.info(
       * "Error while trying to find used value sets : "+e.getMessage()); } }
       */

       /*
       * (non-Javadoc)
       * 
        * @see
       * mat.client.measure.service.CQLService#parseCQLStringForError(java.lang.
       * String)
       */
       @Override
       public SaveUpdateCQLResult parseCQLStringForError(String cqlFileString) {
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              List<CqlTranslatorException> cqlErrorsList = new ArrayList<CqlTranslatorException>();
              List<CQLErrors> errors = new ArrayList<CQLErrors>();
              if (!StringUtils.isBlank(cqlFileString)) {

                     CQLtoELM cqlToElm = new CQLtoELM(cqlFileString);
                     try {
                           cqlToElm.doTranslation(true, false, false);
                     } catch (IOException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                     }

                     if (cqlToElm.getErrors() != null) {
                           cqlErrorsList.addAll(cqlToElm.getErrors());
                     }
              }

              for (CqlTranslatorException cte : cqlErrorsList) {

                     CQLErrors cqlErrors = new CQLErrors();

                     cqlErrors.setStartErrorInLine(cte.getLocator().getStartLine());

                     cqlErrors.setErrorInLine(cte.getLocator().getStartLine());
                     cqlErrors.setErrorAtOffeset(cte.getLocator().getStartChar());

                     cqlErrors.setEndErrorInLine(cte.getLocator().getEndLine());
                     cqlErrors.setEndErrorAtOffset(cte.getLocator().getEndChar());

                     cqlErrors.setErrorMessage(cte.getMessage());
                     errors.add(cqlErrors);
                     System.out.println(cte.getMessage());

              }

              result.setCqlErrors(errors);

              return result;
       }

       /**
       * Save QD sto measure.
       *
       * @param valueSetTransferObject
       *            the value set transfer object
       * @return the save update code list result
       */
       @Override
       public final SaveUpdateCQLResult saveCQLValueset(CQLValueSetTransferObject valueSetTransferObject) {
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
                     qds.setExpansionIdentifier(valueSetTransferObject.getMatValueSet().getExpansionProfile());
              }

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
       
       private List<CQLCode> sortCodeList(final List<CQLCode> finalList) {

              Collections.sort(finalList, new Comparator<CQLCode>() {
                     @Override
                     public int compare(final CQLCode o1, final CQLCode o2) {
                           return o1.getCodeName().compareToIgnoreCase(o2.getCodeName());
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
       @Override
       public SaveUpdateCQLResult saveCQLUserDefinedValueset(CQLValueSetTransferObject matValueSetTransferObject) {
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLQualityDataModelWrapper wrapper = new CQLQualityDataModelWrapper();
              matValueSetTransferObject.scrubForMarkUp();
              ValueSetNameInputValidator validator = new ValueSetNameInputValidator();
              String errorMessage = validator.validate(matValueSetTransferObject);
              if (errorMessage.isEmpty()) {
                     ArrayList<CQLQualityDataSetDTO> qdsList = new ArrayList<CQLQualityDataSetDTO>();
                     List<CQLQualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
                     boolean isQDSExist = false;
                     for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {
                           if (dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID) && dataSetDTO
                                         .getCodeListName().equalsIgnoreCase(matValueSetTransferObject.getUserDefinedText())) {
                                  isQDSExist = true;
                                  break;
                           }
                     }
                     if (!isQDSExist) {
                            wrapper.setQualityDataDTO(qdsList);
                           CQLQualityDataSetDTO qds = new CQLQualityDataSetDTO();
                           qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
                           qds.setId(UUID.randomUUID().toString());
                           qds.setCodeListName(matValueSetTransferObject.getUserDefinedText());
                           qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
                           qds.setUuid(UUID.randomUUID().toString());
                           qds.setVersion("1.0");
                           wrapper.getQualityDataDTO().add(qds);
                           String qdmXMLString = generateXmlForAppliedValueset(wrapper);
                           result.setSuccess(true);
                            result.setCqlAppliedQDMList(sortQualityDataSetList(wrapper.getQualityDataDTO()));
                           result.setXml(qdmXMLString);
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
       
       
       @Override
       public SaveUpdateCQLResult saveCQLCodes(String xml , MatCodeTransferObject codeTransferObject){
              logger.info("::: CQLServiceImpl saveCQLCodes Start :::");
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              XmlProcessor xmlProcessor = new XmlProcessor(xml);
              CQLCode appliedCode = codeTransferObject.getCqlCode();
              appliedCode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
              try {
                     /*Node existingCodeList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "//cqlLookUp/codes/code[@codeName='"+appliedCode.getCodeName() +
                     "' and @codeOID='"+ appliedCode.getCodeOID()+"' and @codeSystemName='"+
                     appliedCode.getCodeSystemName()+"' and @codeSystemVersion ='"+
                     appliedCode.getCodeSystemVersion() +"' and @displayName = '"
                     + appliedCode.getDisplayName()+"' ]");*/
                     Node existingCodeList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(),"//cqlLookUp/codes/code[@codeOID='"+ appliedCode.getCodeOID()+"' ]");
                     if(existingCodeList!= null){
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
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
              logger.info("::: CQLServiceImpl saveCQLCodes End :::");
              return result;
       }
       
       @Override
       public SaveUpdateCQLResult saveCQLCodeSystem(String xml , CQLCodeSystem codeSystem){
              logger.info("::: CQLServiceImpl saveCQLCodeSystem Start :::");
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              XmlProcessor xmlProcessor = new XmlProcessor(xml);
              
              codeSystem.setId(UUID.randomUUID().toString().replaceAll("-", ""));
              try {
                     /*Node existingCodeSystemList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "//cqlLookUp/codeSystems/codeSystem[@codeSystem='"+codeSystem.getCodeSystem() +
                     "' and @codeSystemName='"+ codeSystem.getCodeSystemName()+"' and @codeSystemVersion='"+
                     codeSystem.getCodeSystemVersion()+"' ]");*/
                     
                     Node existingCodeSystemList = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), "//cqlLookUp/codeSystems/codeSystem[@codeSystemName='"+ codeSystem.getCodeSystemName()+"' and @codeSystemVersion='"+
                                  codeSystem.getCodeSystemVersion()+"' ]");
                     
                     if(existingCodeSystemList!= null){
                           logger.info("::: CodeSystem Already added :::");
                           result.setSuccess(false);
                     } else {
                           CQLCodeSystemWrapper wrapper = new CQLCodeSystemWrapper();
                           ArrayList<CQLCodeSystem> codeSystemList = new ArrayList<CQLCodeSystem>();
                           wrapper.setCqlCodeSystemList(codeSystemList);;
                         wrapper.getCqlCodeSystemList().add(codeSystem);
                         String codeSystemXMLString = generateXmlForAppliedCodeSystem(wrapper);
                         result.setSuccess(true);
                           result.setXml(codeSystemXMLString);
                     }
              } catch (XPathExpressionException e) {
                     // TODO Auto-generated catch block
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
              String qdmCompareName = "";

              if (isVSACValueSet) {
                     qdmCompareName = matValueSetTransferObject.getMatValueSet().getDisplayName();
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

       /*
       * (non-Javadoc)
       * 
        * @see mat.client.measure.service.CQLService#updateQDStoMeasure(mat.model.
       * CQLValueSetTransferObject)
       */
       @Override
       public final SaveUpdateCQLResult modifyCQLValueSets(CQLValueSetTransferObject matValueSetTransferObject) {
              SaveUpdateCQLResult result = null;
              matValueSetTransferObject.scrubForMarkUp();
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
                     if (matValueSetTransferObject.isVersion()) {
                            qds.setVersion(matValueSetTransferObject.getMatValueSet().getVersion());
                     } else {
                           qds.setVersion("1.0");
                     }
                     if (matValueSetTransferObject.isExpansionProfile()) {
                         qds.setExpansionIdentifier(matValueSetTransferObject.getMatValueSet().getExpansionProfile());
                     } else {
                           qds.setExpansionIdentifier(null);
                     }
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
                     if (nodesValuesets.getLength() > 1) {
                           Node parentNode = nodesValuesets.item(0).getParentNode();
                           if (parentNode.getAttributes().getNamedItem("vsacExpIdentifier") != null) {
                                  if (!StringUtils.isBlank(modifyWithDTO.getVsacExpIdentifier())) {
                                         parentNode.getAttributes().getNamedItem("vsacExpIdentifier")
                                                       .setNodeValue(modifyWithDTO.getExpansionIdentifier());
                                   } else {
                                         parentNode.getAttributes().removeNamedItem("vsacExpIdentifier");
                                  }
                           } else {
                                  if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
                                         Attr vsacExpIdentifierAttr = processor.getOriginalDoc().createAttribute("vsacExpIdentifier");
                                         vsacExpIdentifierAttr.setNodeValue(modifyWithDTO.getVsacExpIdentifier());
                                         parentNode.getAttributes().setNamedItem(vsacExpIdentifierAttr);
                                  }
                           }
                     }
                     for (int i = 0; i < nodesValuesets.getLength(); i++) {
                           Node newNode = nodesValuesets.item(i);
                           newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
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
                            newNode.getAttributes().getNamedItem("version").setNodeValue(modifyWithDTO.getVersion());
                           if (modifyWithDTO.isSuppDataElement()) {
                                  newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
                           } else {
                                  newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
                           }

                           if (newNode.getAttributes().getNamedItem("expansionIdentifier") != null) {
                                  if (!StringUtils.isBlank(modifyWithDTO.getExpansionIdentifier())) {
                                         newNode.getAttributes().getNamedItem("expansionIdentifier")
                                                       .setNodeValue(modifyWithDTO.getExpansionIdentifier());
                                  } else {
                                         newNode.getAttributes().removeNamedItem("expansionIdentifier");
                                  }
                           } else {
                                  if (!StringUtils.isEmpty(modifyWithDTO.getExpansionIdentifier())) {
                                         Attr expansionIdentifierAttr = processor.getOriginalDoc()
                                                       .createAttribute("expansionIdentifier");
                                         expansionIdentifierAttr.setNodeValue(modifyWithDTO.getExpansionIdentifier());
                                         newNode.getAttributes().setNamedItem(expansionIdentifierAttr);
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
              oldQdm.setOid(qualityDataSetDTO.getOid());
              oldQdm.setUuid(qualityDataSetDTO.getUuid());
              oldQdm.setVersion(qualityDataSetDTO.getVersion());
              oldQdm.setExpansionIdentifier(qualityDataSetDTO.getExpansionIdentifier());
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
       public CQLCodeWrapper getCQLCodes(String xmlString){
              CQLCodeWrapper cqlCodeWrapper = new CQLCodeWrapper();
              if(xmlString != null && !xmlString.isEmpty()) {
                     SaveUpdateCQLResult parsedResult = getCQLData(xmlString);
                     CQLModel cqlModel = parsedResult.getCqlModel();
                     List<CQLCode> allCodes = cqlModel.getCodeList();
                     if(parsedResult.getCqlErrors().isEmpty()){
                           GetUsedCQLArtifactsResult artifactsResult = parsedResult.getUsedCQLArtifacts();
                           List<String> usedCodes = artifactsResult.getUsedCQLcodes();
                           for(CQLCode code : allCodes){
                                  if(usedCodes.contains(code.getCodeName())){
                                         code.setUsed(true);
                                  }
                           }
                     } else {
                           for(CQLCode code : allCodes){
                                  code.setUsed(false);
                           }
                     }
                     List<CQLCode> cqlCodeDtos = CQLUtilityClass
                                  .sortCQLCodeDTO(allCodes);
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
                     CQLIncludeLibrary cqlLibObject, List<CQLIncludeLibrary> viewIncludeLibrarys) {
              SaveUpdateCQLResult result = new SaveUpdateCQLResult();
              CQLIncludeLibraryWrapper wrapper = new CQLIncludeLibraryWrapper();
              System.out.println("DELETE Include CLICK " + cqlLibObject.getAliasName());

              /*
              * MeasureXmlModel xmlModel =
              * getService().getMeasureXmlForMeasure(currentMeasureId);
              */
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
                                  /*
                                  * xmlModel.setXml(processor.getOriginalXml());
                                  * getService().saveMeasureXml(xmlModel);
                                  */

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
                     CQLUtil.getIncludedCQLExpressions(cqlModel,cqlLibraryDAO);
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
}

