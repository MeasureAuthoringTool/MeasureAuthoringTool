package mat.client.clause.clauseworkspace.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.Author;
import mat.model.MeasureSteward;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureDetailResult.
 */
public class MeasureDetailResult implements IsSerializable{
	
	
	/** The used steward id. */
	private MeasureSteward usedSteward;
	
	/** The used author list. */
	private List<Author> usedAuthorList;
	
	/** The all author list. */
	private List<Author> allAuthorList;	
	
	/** The all steward list. */
	private List<MeasureSteward> allStewardList;
	
	
		
	/**
	 * Gets the used author list.
	 *
	 * @return the used author list
	 */
	public List<Author> getUsedAuthorList() {
		return usedAuthorList;
	}
	
	/**
	 * Sets the used author list.
	 *
	 * @param usedAuthorList the new used author list
	 */
	public void setUsedAuthorList(List<Author> usedAuthorList) {
		this.usedAuthorList = usedAuthorList;
	}

	/**
	 * Gets the all steward list.
	 *
	 * @return the all steward list
	 */
	public List<MeasureSteward> getAllStewardList() {
		return allStewardList;
	}

	/**
	 * Sets the all steward list.
	 *
	 * @param allStewardList the new all steward list
	 */
	public void setAllStewardList(List<MeasureSteward> allStewardList) {
		this.allStewardList = allStewardList;
	}

	/**
	 * Gets the all author list.
	 *
	 * @return the all author list
	 */
	public List<Author> getAllAuthorList() {
		return allAuthorList;
	}

	/**
	 * Sets the all author list.
	 *
	 * @param allAuthorList the new all author list
	 */
	public void setAllAuthorList(List<Author> allAuthorList) {
		this.allAuthorList = allAuthorList;
	}

	/**
	 * Gets the used steward.
	 *
	 * @return the used steward
	 */
	public MeasureSteward getUsedSteward() {
		return usedSteward;
	}

	/**
	 * Sets the used steward.
	 *
	 * @param usedSteward the new used steward
	 */
	public void setUsedSteward(MeasureSteward usedSteward) {
		this.usedSteward = usedSteward;
	}

}
