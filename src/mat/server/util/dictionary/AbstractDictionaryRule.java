
package mat.server.util.dictionary;


import java.util.LinkedHashMap;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.vt.middleware.dictionary.Dictionary;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;



public abstract class AbstractDictionaryRule implements Rule
{
	
	private static final Log logger = LogFactory.getLog(AbstractDictionaryRule.class);
	
  /** Error code for matching dictionary word. */
	public static final String ERROR_CODE = "ILLEGAL_WORD";

  /** Error code for matching reversed dictionary word. */
	public static final String ERROR_CODE_REVERSED = "ILLEGAL_WORD_REVERSED";

  /** Dictionary of words. */
	protected Dictionary dictionary;

  /**
   * Sets the dictionary used to search for passwords.
   *
   * @param  dict  to use for searching
   */
	public void setDictionary(final Dictionary dict)
	{
		dictionary = dict;
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
	  String text = passwordData.getPassword().getText();
    
	  String matchingWord = doWordSearch(text);
	  if(matchingWord != null && matchingWord.length()>0){
		  	logger.info("Dictionay word found in Password :"+matchingWord);
    	  
		  //Add code to remove special characters and number in password.
    	  	String[] allPrefixAndSuffix = text.split(matchingWord);
    	  	boolean isPreFixAWord = false;
    	  	boolean isSufFixAWord = false;
    	  	for(int index=0;index < allPrefixAndSuffix.length; index++){
    	  		
    	  		boolean foundWord = checkSuffixPrefix(allPrefixAndSuffix[index]);
    	  		if(index == 0){
    	  			isPreFixAWord =foundWord;
    	  		}
    	  		else{
    	  			isSufFixAWord=foundWord;
    	  		}
    	  			
    	  	}
    	  	logger.info("Dictionay word found in Prefix  :"+isPreFixAWord);
    	  	logger.info("Dictionay word found in Suffix  :"+isSufFixAWord);
    	  	
    	  	if(isPreFixAWord == false && isSufFixAWord == false) {
    	  		result.setValid(false);
    	  	}else{
    	  		result.setValid(true);
    	  	}
      }
    
	  return result;
  }

  /**
   * Creates the parameter data for the rule result detail.
   *
   * @param  word  matching word
   *
   * @return  map of parameter name to value
   */
  protected Map<String, ?> createRuleResultDetailParameters(final String word)
  {
	  final Map<String, Object> m = new LinkedHashMap<String, Object>();
	  m.put("matchingWord", word);
	  return m;
  }
  protected boolean checkSuffixPrefix(final String text)
  {
	  logger.info("Inside checkSuffixPrefix");
	  logger.info("checkSuffixPrefix - Word to Be Validated : "+ text);
	  boolean foundWord = false;
	  for (int i = 8; i >= 3; i--) {
		  for (int j = 0; j + i <= text.length();j=j+1) {
		     final String subString = text.substring(j, j+i);
    	 	  if (dictionary.search(subString))
			  {
    	 		 logger.info("checkSuffixPrefix - SubString Found in Dictionary : "+ subString );
    	 		 foundWord = true;
				  break;
			  }
      		}
		  if(foundWord)
			  break;
	  }
	  
	  return foundWord;
  	}

  /**
   * Searches the dictionary with the supplied text.
   *
   * @param  text  to search dictionary with
   *
   * @return  matching word
   */
  protected abstract String doWordSearch(final String text);


 
}
