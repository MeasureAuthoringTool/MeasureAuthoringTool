/**
 * 
 */
package mat.server.util.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		File file = new File("abc.xml");
		logger.info("file path ==="+file.getPath());
		AbstractWordList awl = WordLists.createFromReader(new FileReader[] {new FileReader("en_US.dic")},false,new ArraysSort());
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
			System.out.println("Valid password");
		} else {
			System.out.println("Invalid password:");
			for (String msg : validator.getMessages(result)) {
				System.out.println(msg);
			}
		}
		return result.isValid();
	}

}
