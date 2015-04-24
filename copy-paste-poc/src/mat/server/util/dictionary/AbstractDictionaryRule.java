
package mat.server.util.dictionary;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.vt.middleware.dictionary.Dictionary;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;



/**
 * The Class AbstractDictionaryRule.
 */
public abstract class AbstractDictionaryRule implements Rule
{
	
	/** The Constant LOGGER. */
	private static final Log LOGGER = LogFactory.getLog(AbstractDictionaryRule.class);
	
  /** Error code for matching dictionary word. */
	public static final String ERROR_CODE = "ILLEGAL_WORD";

 
  /** Dictionary of words. */
	protected Dictionary dictionary;
  /** Default word length. */
    public static final int DEFAULT_WORD_LENGTH = 8;
  /**
   * Minimum substring size to consider as a possible word within the password.
  */
   private int wordLength = DEFAULT_WORD_LENGTH;

  /**
	 * Sets the dictionary used to search for passwords.
	 * 
	 * @param dict
	 *            the new dictionary of words
	 */
	public void setDictionary(final Dictionary dict)
	{
		dictionary = dict;
	}


  /**
	 * Sets the minimum substring size to consider as a possible word within the
	 * password.
	 * 
	 * @param wordLength
	 *            the wordLength to set
	 */
	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}


	/**
	 * Gets the minimum substring size to consider as a possible word within the
	 * password.
	 * 
	 * @return the wordLength
	 */
	public int getWordLength() {
		return wordLength;
	}


	/**
	 * Returns the dictionary used to search for passwords.
	 *
	 * @return  dictionary used for searching
	 */
	public Dictionary getDictionary()
	{
		return dictionary;
	}
	

  /** {@inheritDoc} */
  @Override
  	public RuleResult validate(final PasswordData passwordData)
  	{
	  
	  final RuleResult result = new RuleResult(true);
	  final String passwordText = passwordData.getPassword().getText();
    
	  final String dictionaryWord = doWordSearch(passwordText);
	  
	  if(dictionaryWord != null && dictionaryWord.length()>0){
		  LOGGER.info("Dictionay word found in Password :"+dictionaryWord);
    	  final String[] wordsPreFixSuffix = passwordText.split(dictionaryWord);
    	  boolean isPreFixAWord = false;
    	  boolean isSufFixAWord = false;
    	 
    	  for(int index=0;index < wordsPreFixSuffix.length; index++){
    		  final boolean foundWord = checkSuffixPrefix(wordsPreFixSuffix[index]);
    	  	  if(index == 0){
    	  		isPreFixAWord =foundWord;
    	  	  }
    	  	  else{
    	  		isSufFixAWord=foundWord;
    	  	  }
    	  }
    	  LOGGER.info("Dictionay word found in Prefix  :"+isPreFixAWord);
    	  LOGGER.info("Dictionay word found in Suffix  :"+isSufFixAWord);
    	  if(isPreFixAWord || isSufFixAWord ) {
    	  		result.setValid(true);
    	  }else{
    	  		result.setValid(false);
    	  }
      }else{
    	  LOGGER.info("No Dictionay word is found in Password :"+passwordData.getPassword());
      }
     return result;
  }
 
  /**
	 * Check suffix prefix.
	 * 
	 * @param text
	 *            the text
	 * @return true, if successful
	 */
  protected boolean checkSuffixPrefix(final String text)
  {
	  LOGGER.info("checkSuffixPrefix - Word to Be Validated : "+ text);
	  boolean foundWord = false;
	  
	  int maxWordLength = 8;
	  //for (maxWordLength = 8; maxWordLength >= 3; maxWordLength--) {
	    while(maxWordLength >=3 && !foundWord){
		 // if(!foundWord){
			  for (int j = 0; j + maxWordLength <= text.length();j=j+1) {
				  final String subString = text.substring(j, j+maxWordLength);
				  if (dictionary.search(subString))
				  {
					  LOGGER.info("checkSuffixPrefix - SubString Found in Dictionary : "+ subString );
					  foundWord = true;
					  break;
				  }
      			}
		  	//}
		  maxWordLength--;
 	  }
	  return foundWord;
  	}

  /**
	 * Searches the dictionary with the supplied text.
	 * 
	 * @param text
	 *            the text
	 * @return matching word
	 */
  protected abstract String doWordSearch(final String text);


 
}
