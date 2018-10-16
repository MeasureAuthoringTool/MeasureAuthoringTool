
package mat.server.util.dictionary;

import edu.vt.middleware.dictionary.Dictionary;


/**
 * The Class DictionarySubstringRule.
 */
public class DictionarySubstringRule extends AbstractDictionaryRule
{

  /** Default word length. */
  public static final int DEFAULT_WORD_LENGTH = 8;

  /** Default word length. */
  public static final int DEFAULT_MIN_WORD_LENGTH = 3;

  /**
   * Minimum substring size to consider as a possible word within the password.
   */
  private int minWordLength = DEFAULT_MIN_WORD_LENGTH;

  /**
   * Maximum substring size to consider as a possible word within the password.
   */
  private int wordLength = DEFAULT_WORD_LENGTH;

  /**
   * Creates a new dictionary substring rule. The dictionary should be set using
   * the {@link #setDictionary(Dictionary)} method.
   */
  public DictionarySubstringRule() {}


  /**
	 * Creates a new dictionary substring rule. The dictionary should be ready
	 * to use when passed to this constructor.
	 * 
	 * @param dict
	 *            the dict
	 */
  public DictionarySubstringRule(final Dictionary dict)
  {
    dictionary = dict;
  }


  /**
	 * Create a new dictionary substring rule. The dictionary should be ready to
	 * use when passed to this constructor. See {@link #setWordLength(int)}.
	 * 
	 * @param dict
	 *            the dict
	 * @param maxWordLength
	 *            the max word length
	 */
  public DictionarySubstringRule(final Dictionary dict, final int maxWordLength)
  {
    super.setDictionary(dict);
    super.setWordLength(maxWordLength);
  }


  /**
	 * Sets the minimum number of characters that constitute a word in a
	 * password. If n = 5 and the password contains 'test', then the password is
	 * valid. However if n = 4 then 'test' will be found in the dictionary. The
	 * default value is 4.
	 * 
	 * @param maxWordLength
	 *            the new maximum substring size to consider as a possible word
	 *            within the password
	 */
  public void setWordLength(final int maxWordLength)
  {
    if (maxWordLength >= 1) {
      wordLength = maxWordLength;
    } else {
      throw new IllegalArgumentException("wordLength must be >= 1");
    }
  }


  /**
   * Returns the number of characters that constitute a word in a password.
   *
   * @return  minimum number of characters to check in each dictionary word
   */
  public int getWordLength()
  {
    return wordLength;
  }


  /**
	 * Returns Largest matched Dictionary word found in Password.
	 * 
	 * @param text
	 *            the text
	 * @return Largest Dictionary word found.
	 */
  @Override
  protected String doWordSearch(final String text)
  {
	String largestWord = null;
	
    for (int maxWordLength = wordLength; maxWordLength >= minWordLength; maxWordLength--) {
      for (int j = 0; j + maxWordLength <= text.length();j=j+1) {
	    final String subStringInPwd = text.substring(j, j+maxWordLength);
	    if (dictionary.search(subStringInPwd))
        {
        	// check largest word.
	    	if(largestWord == null)
		    		largestWord = subStringInPwd;
	       
	    	if(subStringInPwd.length() >= largestWord.length()){
        		largestWord=subStringInPwd;
        	}
        }
      }
    }
   
    return largestWord;
  }

  
 
}
