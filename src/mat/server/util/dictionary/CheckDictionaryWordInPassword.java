/**
 * 
 */
package mat.server.util.dictionary;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mat.server.util.ResourceLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.vt.middleware.dictionary.AbstractWordList;
import edu.vt.middleware.dictionary.WordListDictionary;
import edu.vt.middleware.dictionary.WordLists;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.dictionary.sort.ArraysSort;

/**
 * @author jnarang
 *
 */
public class CheckDictionaryWordInPassword {
	private static final Log logger = LogFactory.getLog(CheckDictionaryWordInPassword.class);
	public static boolean containsDictionaryWords(String passWord) throws FileNotFoundException, IOException{
		
		String fileName = "en_US.dic";
		URL dicUrl = new ResourceLoader().getResourceAsURL(fileName);
		if(null == dicUrl){
			throw new FileNotFoundException(fileName + " doesnot exists in /WEB-INF/classes location");
		}
		AbstractWordList awl = WordLists.createFromReader(new FileReader[] {new FileReader(dicUrl.getFile())},false,new ArraysSort());
		// 	create a dictionary for searching
		WordListDictionary dict = new WordListDictionary(awl);
		DictionarySubstringRule dictRule = new DictionarySubstringRule(dict);
		dictRule.setWordLength(8); // size of words to check in the password
		List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(dictRule);

		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(passWord));

		RuleResult result = validator.validate(passwordData);
		if (result.isValid()) {
			logger.info("Password Supplied Is Valid password");
		} else {
			logger.info("Password Supplied Is Invalid password:");
		}
		return result.isValid();
	}

}
