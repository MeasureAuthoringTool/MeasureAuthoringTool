package mat.client.cql;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLLibraryDataSetObject;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageCQLLibrarySearchModel.
 */
public class ManageCQLLibrarySearchModel implements IsSerializable{

	
	/** The cql library data set objects. */
	List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects;
	
	/** The results total. */
	private int resultsTotal;

	/**
	 * Gets the cql library data set objects.
	 *
	 * @return the cql library data set objects
	 */
	public List<CQLLibraryDataSetObject> getCqlLibraryDataSetObjects() {
		return cqlLibraryDataSetObjects;
	}

	/**
	 * Sets the cql library data set objects.
	 *
	 * @param cqlLibraryDataSetObjects the new cql library data set objects
	 */
	public void setCqlLibraryDataSetObjects(List<CQLLibraryDataSetObject> cqlLibraryDataSetObjects) {
		this.cqlLibraryDataSetObjects = cqlLibraryDataSetObjects;
	}


	/**
	 * Gets the results total.
	 *
	 * @return the results total
	 */
	public int getResultsTotal() {
		return resultsTotal;
	}

	/**
	 * Sets the results total.
	 *
	 * @param resultsTotal the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
	
	
	
	
	
}
