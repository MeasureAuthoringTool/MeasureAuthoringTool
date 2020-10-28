package mat.client.shared;

import mat.model.CQLValueSetTransferObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueSetNameInputValidatorTest {

	@Test
	public void test() {
		testEmpty();
		testBeginning();
		testMiddle();
		testEnd();
		testValid();
		testOnlyBadCharacters();
	}
	
	private void testEmpty() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 

		ValueSetTransferObject.setUserDefinedText("");
		assertEquals("Value Set Name cannot be empty.", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText(null);
		assertEquals("Value Set Name cannot be empty.", validator.validate(ValueSetTransferObject));
	}
	
	private void testBeginning() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		ValueSetTransferObject.setUserDefinedText("*test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("+test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("?test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText(":test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("-test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("|test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("!test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("\"test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("%test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
	}
	
	private void testMiddle() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 

		ValueSetTransferObject.setUserDefinedText("test*1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test*1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test+1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test?1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test:1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test-1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test|1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test!1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test\"1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test%1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
	}
	
	private void testEnd() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		ValueSetTransferObject.setUserDefinedText("test1*");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1+");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1?");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1:");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1-");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1|");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1!");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1\"");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("test1%");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
	
	}

	private void testValid() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		ValueSetTransferObject.setUserDefinedText("test1");
		assertEquals("", validator.validate(ValueSetTransferObject));
	}
	
	private void testOnlyBadCharacters() {
		CQLValueSetTransferObject ValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		ValueSetTransferObject.setUserDefinedText("+");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("*");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("?");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText(":");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("-");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("|");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("!");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("\"");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
		
		ValueSetTransferObject.setUserDefinedText("%");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(ValueSetTransferObject));
	}
	
}
