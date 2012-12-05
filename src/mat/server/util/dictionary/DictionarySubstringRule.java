
package mat.server.util.dictionary;

import edu.vt.middleware.dictionary.Dictionary;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.RuleResult;


public class DictionarySubstringRule extends AbstractDictionaryRule
{

  /** Default word length. */
  public static final int DEFAULT_WORD_LENGTH = 8;

  /**
   * Minimum substring size to consider as a possible word within the password.
   */
  private int wordLength = DEFAULT_WORD_LENGTH;


  /**
   * Creates a new dictionary substring rule. The dictionary should be set using
   * the {@link #setDictionary(Dictionary)} method.
   */
  public DictionarySubstringRule() {}


  /**
   * Creates a new dictionary substring rule. The dictionary should be ready to
   * use when passed to this constructor.
   *
   * @param  dict  to use for searching
   */
  public DictionarySubstringRule(final Dictionary dict)
  {
    dictionary = dict;
  }


  /**
   * Create a new dictionary substring rule. The dictionary should be ready to
   * use when passed to this constructor. See {@link #setWordLength(int)}.
   *
   * @param  dict  to use for searching
   * @param  n  number of characters to check in each dictionary word
   */
  public DictionarySubstringRule(final Dictionary dict, final int n)
  {
    setDictionary(dict);
    setWordLength(n);
  }


  /**
   * Sets the minimum number of characters that constitute a word in a password.
   * If n = 5 and the password contains 'test', then the password is valid.
   * However if n = 4 then 'test' will be found in the dictionary. The default
   * value is 4.
   *
   * @param  n  minimum number of characters to check in each dictionary word
   */
  public void setWordLength(final int n)
  {
    if (n >= 1) {
      wordLength = n;
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


  /** {@inheritDoc} */
  @Override
  protected String doWordSearch(final String text)
  {
	String largestWord = null;
    for (int i = wordLength; i >= 3; i--) {
      for (int j = 0; j + i <= text.length();j=j+1) {
	    final String s = text.substring(j, j+i);
	    if (dictionary.search(s))
        {
        	// check largest word.
	    	if(largestWord == null)
		    		largestWord = s;
	       
	    	if(s.length() >= largestWord.length()){
        		largestWord=s;
        	}
        }
      }
    }
   
    return largestWord;
  }

  
 
}
