package mat.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.GenericResult;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveUpdateCQLResult.
 */
public class SaveUpdateCQLResult extends GenericResult{
	
	/** The cql model. */
	private CQLModel cqlModel;
	
	private CQLObject cqlObject;
	
	private String expIdentifier;
	
	private String setId;
	
	private String xml;
	
	/** The cql string. */
	private String cqlString;
	
	/** The cql errors. */
	private List<CQLErrors> cqlErrors = new ArrayList<CQLErrors>();
	
	/** The definition. */
	private CQLDefinition definition;
	
	/** The parameter. */
	private CQLParameter parameter;
	
	/** The function. */
	private CQLFunctions function;
	
	private CQLIncludeLibrary includeLibrary;
	
	private GetUsedCQLArtifactsResult usedCQLArtifacts = new GetUsedCQLArtifactsResult();
	
	/**  The start line of the result. */
	private int startLine; 
	
	/**  The end line of the result. */
	private int endLine; 
	
	/** The Constant NAME_NOT_UNIQUE. */
	public static final int NAME_NOT_UNIQUE = 1;
	
	/** The Constant NODE_NOT_FOUND. */
	public static final int NODE_NOT_FOUND = 2;
	
	/** The Constant NO_SPECIAL_CHAR. */
	public static final int NO_SPECIAL_CHAR = 3;
	
	/** The Constant SERVER_SIDE_VALIDATION. */
	public static final int SERVER_SIDE_VALIDATION = 4;
	
	/** The Constant ALREADY_EXISTS. */
	public static final int ALREADY_EXISTS = 5;
	
	/** The Constant SERVER_SIDE_VALIDATION. */
	public static final int FUNCTION_ARGUMENT_INVALID = 6;
	
	public static final int DUPLICATE_CODE=7;
	
	/** The cql applied QDM list. */
	List<CQLQualityDataSetDTO> cqlAppliedQDMList ;
	
	/** The cql quality data set DTO. */
	CQLQualityDataSetDTO cqlQualityDataSetDTO;
	
	List<CQLCode> cqlCodeList;
	
	CQLCode cqlCode;

	private String elmString = "";
	
	private boolean isDatatypeUsedCorrectly = true;
	
	/**
	 * Gets the cql string.
	 *
	 * @return the cql string
	 */
	public String getCqlString() {
		return cqlString;
	}
	
	/**
	 * Sets the cql string.
	 *
	 * @param cqlString the new cql string
	 */
	public void setCqlString(String cqlString) {
		this.cqlString = cqlString;
	}
	
	/**
	 * Gets the cql errors.
	 *
	 * @return the cql errors
	 */
	public List<CQLErrors> getCqlErrors() {
		return cqlErrors;
	}

	/**
	 * Sets the cql errors.
	 *
	 * @param cqlErrors the new cql errors
	 */
	public void setCqlErrors(List<CQLErrors> cqlErrors) {
		this.cqlErrors = cqlErrors;
	}

	/**
	 * Gets the cql model.
	 *
	 * @return the cql model
	 */
	public CQLModel getCqlModel() {
		return cqlModel;
	}
	
	/**
	 * Sets the cql model.
	 *
	 * @param cqlModel the new cql model
	 */
	public void setCqlModel(CQLModel cqlModel) {
		this.cqlModel = cqlModel;
	}
	
	
	
	
	/**
	 * Gets the definition.
	 *
	 * @return the definition
	 */
	public CQLDefinition getDefinition() {
		return definition;
	}
	
	/**
	 * Sets the definition.
	 *
	 * @param definition the new definition
	 */
	public void setDefinition(CQLDefinition definition) {
		this.definition = definition;
	}
	
	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public CQLParameter getParameter() {
		return parameter;
	}
	
	/**
	 * Sets the parameter.
	 *
	 * @param parameter the new parameter
	 */
	public void setParameter(CQLParameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * Gets the function.
	 *
	 * @return the function
	 */
	public CQLFunctions getFunction() {
		return function;
	}

	/**
	 * Sets the function.
	 *
	 * @param function the new function
	 */
	public void setFunction(CQLFunctions function) {
		this.function = function;
	}
	
	/**
	 * Sets the start line.
	 *
	 * @param startLine the new start line
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine; 
	}
	
	/**
	 * Gets the start line.
	 *
	 * @return the start line
	 */
	public int getStartLine() {
		return this.startLine;
	}
	
	/**
	 * Sets the end line.
	 *
	 * @param endLine the new end line
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine; 
	}
	
	/**
	 * Gets the end L ine.
	 *
	 * @return the end L ine
	 */
	public int getEndLIne() {
		return this.endLine; 
	}
	
	
	/**
	 * Gets the cql applied QDM list.
	 *
	 * @return the cql applied QDM list
	 */
	public List<CQLQualityDataSetDTO> getCqlAppliedQDMList() {
		return cqlAppliedQDMList;
	}

	/**
	 * Sets the cql applied QDM list.
	 *
	 * @param cqlAppliedQDMList the new cql applied QDM list
	 */
	public void setCqlAppliedQDMList(List<CQLQualityDataSetDTO> cqlAppliedQDMList) {
		this.cqlAppliedQDMList = cqlAppliedQDMList;
	}

	public List<CQLCode> getCqlCodeList() {
		return cqlCodeList;
	}

	public void setCqlCodeList(List<CQLCode> cqlCodeList) {
		this.cqlCodeList = cqlCodeList;
	}

	public CQLCode getCqlCode() {
		return cqlCode;
	}

	public void setCqlCode(CQLCode cqlCode) {
		this.cqlCode = cqlCode;
	}

	/**
	 * Gets the cql quality data set DTO.
	 *
	 * @return the cql quality data set DTO
	 */
	public CQLQualityDataSetDTO getCqlQualityDataSetDTO() {
		return cqlQualityDataSetDTO;
	}

	/**
	 * Sets the cql quality data set DTO.
	 *
	 * @param cqlQualityDataSetDTO the new cql quality data set DTO
	 */
	public void setCqlQualityDataSetDTO(CQLQualityDataSetDTO cqlQualityDataSetDTO) {
		this.cqlQualityDataSetDTO = cqlQualityDataSetDTO;
	}

	public CQLIncludeLibrary getIncludeLibrary() {
		return includeLibrary;
	}

	public void setIncludeLibrary(CQLIncludeLibrary includeLibrary) {
		this.includeLibrary = includeLibrary;
	}

	public GetUsedCQLArtifactsResult getUsedCQLArtifacts() {
		return usedCQLArtifacts;
	}

	public void setUsedCQLArtifacts(GetUsedCQLArtifactsResult usedCQLArtifacts) {
		this.usedCQLArtifacts = usedCQLArtifacts;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getElmString() {
		return elmString;
	}

	public void setElmString(String elmString) {
		this.elmString = elmString;
	}

	public String getExpIdentifier() {
		return expIdentifier;
	}

	public void setExpIdentifier(String expIdentifier) {
		this.expIdentifier = expIdentifier;
	}

	/**
	 * @return the setId
	 */
	public String getSetId() {
		return setId;
	}

	/**
	 * @param setId the setId to set
	 */
	public void setSetId(String setId) {
		this.setId = setId;
	}

	public boolean isDatatypeUsedCorrectly() {
		return isDatatypeUsedCorrectly;
	}

	public void setDatatypeUsedCorrectly(boolean isDatatypeUsedCorrectly) {
		this.isDatatypeUsedCorrectly = isDatatypeUsedCorrectly;
	}

	public CQLObject getCqlObject() {
		return cqlObject;
	}

	public void setCqlObject(CQLObject cqlObject) {
		this.cqlObject = cqlObject;
	}
	
}
