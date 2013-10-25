package mat.client.shared;

/**
 * zoom functionality delegate fields min and maxFactor establish how much or
 * how little canvas diagram dimensions can expand adjust field incrementAmount
 * to adjust the rate of canvas diagram expansion.
 * 
 * @author aschmidt
 */
public class ZoomFactorService {
	
	/** The min factor. */
	private final int minFactor = 28;
	
	/** The max factor. */
	private final int maxFactor = 100;
	
	/** The increment amount. */
	private final int incrementAmount = 8;
	
	/** The factor arr. */
	private int[] factorArr = new int[]{minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor};
	
	/** The current factor. */
	private int currentFactor = 0;
	
	/**
	 * Gets the factor.
	 * 
	 * @return the factor
	 */
	public int getFactor(){
		return factorArr[currentFactor];
	}
	
	/**
	 * Increment factor.
	 */
	public void incrementFactor(){
		if(factorArr[currentFactor]+1 <= maxFactor) factorArr[currentFactor]+=incrementAmount;
	}
	
	/**
	 * Decrement factor.
	 */
	public void decrementFactor(){
		if(factorArr[currentFactor]-1 >= minFactor) factorArr[currentFactor]-=incrementAmount;
	}
	
	/**
	 * Reset factor.
	 */
	public void resetFactor(){
		factorArr[currentFactor] = minFactor;
	}
	
	/**
	 * Sets the current factor.
	 * 
	 * @param i
	 *            the new current factor
	 */
	public void setCurrentFactor(int i){
		currentFactor = i;
	}
	
	/**
	 * Reset factor arr.
	 */
	public void resetFactorArr(){
		for(int i = 0; i < factorArr.length; i++){
			factorArr[i] = minFactor;
		}
	}
}
