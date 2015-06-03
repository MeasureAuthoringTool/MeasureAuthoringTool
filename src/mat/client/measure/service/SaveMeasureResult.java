package mat.client.measure.service;

import java.util.List;
import mat.client.shared.GenericResult;
import mat.model.Author;
import mat.model.MeasureType;
import mat.shared.ConstantMessages;

/**
 * The Class SaveMeasureResult.
 */
public class SaveMeasureResult extends GenericResult {
	
	/** The Constant ID_NOT_UNIQUE. */
	public static final int ID_NOT_UNIQUE = ConstantMessages.ID_NOT_UNIQUE;
	
	/** The Constant REACHED_MAXIMUM_VERSION. */
	public static final int REACHED_MAXIMUM_VERSION = ConstantMessages.REACHED_MAXIMUM_VERSION;
	
	/** The Constant REACHED_MAXIMUM_MAJOR_VERSION. */
	public static final int REACHED_MAXIMUM_MAJOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MAJOR_VERSION;
	
	/** The Constant REACHED_MAXIMUM_MINOR_VERSION. */
	public static final int REACHED_MAXIMUM_MINOR_VERSION = ConstantMessages.REACHED_MAXIMUM_MINOR_VERSION;
	
	/** The Constant INVALID_VALUE_SET_DATE. */
	public static final int INVALID_VALUE_SET_DATE = ConstantMessages.INVALID_VALUE_SET_DATE;
	
	/** The Constant INVALID_VALUE_SET_DATE. */
	public static final int INVALID_DATA = ConstantMessages.INVALID_DATA;
	
	/** The id. */
	private String id;
	
	/** The author list. */
	private List<Author> authorList;
	
	/** The measure type list. */
	private List<MeasureType> measureTypeList;
	
	/** The version str. */
	private String versionStr;
	
	/**
	 * Gets the author list.
	 * 
	 * @return the author list
	 */
	public List<Author> getAuthorList() {
		return authorList;
	}
	
	/**
	 * Sets the author list.
	 * 
	 * @param authorList
	 *            the new author list
	 */
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
	
	/**
	 * Gets the measure type list.
	 * 
	 * @return the measure type list
	 */
	public List<MeasureType> getMeasureTypeList() {
		return measureTypeList;
	}
	
	/**
	 * Sets the measure type list.
	 * 
	 * @param measureTypeList
	 *            the new measure type list
	 */
	public void setMeasureTypeList(List<MeasureType> measureTypeList) {
		this.measureTypeList = measureTypeList;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the version str.
	 * 
	 * @return the version str
	 */
	public String getVersionStr() {
		return versionStr;
	}
	
	/**
	 * Sets the version str.
	 * 
	 * @param versionStr
	 *            the new version str
	 */
	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}
	
}
