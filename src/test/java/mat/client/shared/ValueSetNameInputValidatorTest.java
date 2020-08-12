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
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 

		matValueSetTransferObject.setUserDefinedText("");
		assertEquals("Value Set Name cannot be empty.", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText(null);
		assertEquals("Value Set Name cannot be empty.", validator.validate(matValueSetTransferObject));
	}
	
	private void testBeginning() {
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		matValueSetTransferObject.setUserDefinedText("*test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("+test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("?test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText(":test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("-test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("|test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("!test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("\"test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("%test1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
	}
	
	private void testMiddle() {
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 

		matValueSetTransferObject.setUserDefinedText("test*1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test*1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test+1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test?1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test:1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test-1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test|1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test!1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test\"1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test%1");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
	}
	
	private void testEnd() {
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		matValueSetTransferObject.setUserDefinedText("test1*");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1+");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1?");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1:");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1-");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1|");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1!");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1\"");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("test1%");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
	
	}

	private void testValid() {
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		matValueSetTransferObject.setUserDefinedText("test1");
		assertEquals("", validator.validate(matValueSetTransferObject));
	}
	
	private void testOnlyBadCharacters() {
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		ValueSetNameInputValidator validator = new ValueSetNameInputValidator(); 
		
		matValueSetTransferObject.setUserDefinedText("+");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("*");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("?");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText(":");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("-");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("|");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("!");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("\"");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
		
		matValueSetTransferObject.setUserDefinedText("%");
		assertEquals(" Value set name cannot contain any of the following characters + * ? : - | ! \" %", validator.validate(matValueSetTransferObject));
	}
	
}
