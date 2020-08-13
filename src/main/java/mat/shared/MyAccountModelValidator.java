package mat.shared;

import mat.client.myAccount.MyAccountModel;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MyAccountModelValidator.
 */
public class MyAccountModelValidator {
	
	//TODO to prevent overflows we should be checking sizes
	// What should the overflow bounds be?
	
	/**
	 * Validate.
	 * 
	 * @param model
	 *            the model
	 * @return the list
	 */
	public List<String> validate(MyAccountModel model){
		List<String> message = new ArrayList<String>();
		if("".equals(model.getFirstName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
		}
		if(model.getFirstName().length() < 2) {
			message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
		}
		if("".equals(model.getOrganization().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
		}
		/*if("".equals(model.getRootoid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}*/
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MatContext.get().getMessageDelegate().getEmailIdRequired());
		}
		String emailRegExp  = "^\\S+@\\S+\\.\\S+$";
		if (!(model.getEmailAddress().trim().matches(emailRegExp))){
			message.add(MatContext.get().getMessageDelegate().getEmailIdFormatIncorrectMessage());
		}
		if("".equals(model.getPhoneNumber().trim())) {
			message.add(MatContext.get().getMessageDelegate().getPhoneRequiredMessage());
		}
		
		String phoneNum = model.getPhoneNumber();
		int i, numCount;
		numCount=0;
		for(i=0;i<phoneNum.length(); i++){
			if(Character.isDigit(phoneNum.charAt(i))) {
				numCount++;
			}
		}
		if(numCount != 10) {
			message.add(MatContext.get().getMessageDelegate().getPhoneTenDigitMessage());
		}
		
		return message;
	}
	
	
}
