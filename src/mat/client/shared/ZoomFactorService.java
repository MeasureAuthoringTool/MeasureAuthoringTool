package mat.client.shared;

/**
 * zoom functionality delegate
 * fields min and maxFactor establish how much or how little canvas diagram dimensions can expand
 * adjust field incrementAmount to adjust the rate of canvas diagram expansion
 * @author aschmidt
 *
 */
public class ZoomFactorService {
	
	private final int minFactor = 28;
	private final int maxFactor = 100;
	private final int incrementAmount = 8;
	
	private int[] factorArr = new int[]{minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor,minFactor};
	private int currentFactor = 0;
	
	public int getFactor(){
		return factorArr[currentFactor];
	}
	public void incrementFactor(){
		if(factorArr[currentFactor]+1 <= maxFactor) factorArr[currentFactor]+=incrementAmount;
	}
	public void decrementFactor(){
		if(factorArr[currentFactor]-1 >= minFactor) factorArr[currentFactor]-=incrementAmount;
	}
	public void resetFactor(){
		factorArr[currentFactor] = minFactor;
	}
	public void setCurrentFactor(int i){
		currentFactor = i;
	}
	public void resetFactorArr(){
		for(int i = 0; i < factorArr.length; i++){
			factorArr[i] = minFactor;
		}
	}
}
