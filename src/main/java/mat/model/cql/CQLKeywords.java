package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLKeywords.
 */
public class CQLKeywords implements IsSerializable{
	
	/** The cql data type list. */
	private  List<String> cqlDataTypeList = new ArrayList<String>();
	
	/** The cql timing list. */
	private List<String> cqlTimingList = new ArrayList<String>();
	
	/** The cql functions list. */
	private List<String> cqlFunctionsList = new ArrayList<String>();
	
	/** The cql keywords list. */
	private List<String> cqlKeywordsList = new ArrayList<String>();
	
	/**
	 * Gets the cql data type list.
	 *
	 * @return the cql data type list
	 */
	public List<String> getCqlDataTypeList() {
		return cqlDataTypeList;
	}
	
	/**
	 * Sets the cql data type list.
	 *
	 * @param cqlDataTypeList2 the new cql data type list
	 */
	public void setCqlDataTypeList(List<String> cqlDataTypeList2) {
		cqlDataTypeList = cqlDataTypeList2;
	}
	
	/**
	 * Gets the cql timing list.
	 *
	 * @return the cql timing list
	 */
	public List<String> getCqlTimingList() {
		return cqlTimingList;
	}
	
	/**
	 * Sets the cql timing list.
	 *
	 * @param cqlTimingList the new cql timing list
	 */
	public void setCqlTimingList(List<String> cqlTimingList) {
		this.cqlTimingList = cqlTimingList;
	}
	
	/**
	 * Gets the cql functions list.
	 *
	 * @return the cql functions list
	 */
	public List<String> getCqlFunctionsList() {
		return cqlFunctionsList;
	}
	
	/**
	 * Sets the cql functions list.
	 *
	 * @param cqlFunctionsList the new cql functions list
	 */
	public void setCqlFunctionsList(List<String> cqlFunctionsList) {
		this.cqlFunctionsList = cqlFunctionsList;
	}

	/**
	 * Gets the cql keywords list.
	 *
	 * @return the cql keywords list
	 */
	public List<String> getCqlKeywordsList() {
		return cqlKeywordsList;
	}

	/**
	 * Sets the cql keywords list.
	 *
	 * @param cqlKeywordsList the new cql keywords list
	 */
	public void setCqlKeywordsList(List<String> cqlKeywordsList) {
		this.cqlKeywordsList = cqlKeywordsList;
	}
}
