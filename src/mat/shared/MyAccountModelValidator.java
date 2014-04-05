package mat.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.myAccount.MyAccountModel;
import mat.client.shared.MatContext;

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
				
		if(!checkForMarkUp(model)){
			message.add(MatContext.get().getMessageDelegate().getNoMarkupAllowedMessage());
		}
		
		if("".equals(model.getFirstName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
		}
		if(model.getFirstName().length() < 2) {
			message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
		}
		if("".equals(model.getOrganisation().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
		}
		/*if("".equals(model.getRootoid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}*/
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLoginIDRequiredMessage());
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
			if(Character.isDigit(phoneNum.charAt(i)))
				numCount++;
		}
		if(numCount != 10) {
			message.add(MatContext.get().getMessageDelegate().getPhoneTenDigitMessage());
		}
		
		return message;
	}

	private boolean checkForMarkUp(MyAccountModel model) {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = model.getFirstName().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getFirstName().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getLastName().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getLastName().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getMiddleInitial().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getMiddleInitial().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getTitle().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);		
		if(model.getTitle().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getEmailAddress().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getEmailAddress().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getOid().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getOid().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getOrganisation().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getOrganisation().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getPhoneNumber().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getPhoneNumber().trim().length() > noMarkupText.length()){
			return false;
		}
		
		return true;
	}
	
}
