/**
 * 
 */
package mat.shared;

import java.util.ArrayList;
import java.util.List;
import mat.client.admin.ManageUsersDetailModel;
import mat.client.shared.MatContext;

/**
 * The Class AdminManageUserModelValidator.
 * 
 * @author jnarang
 */
public class AdminManageUserModelValidator {
	
	
	/**
	 * Checks if is valid users detail.
	 * 
	 * @param model
	 *            the model
	 * @return the list
	 */
	public List<String> isValidUsersDetail(ManageUsersDetailModel model) {
		List<String> message = new ArrayList<String>();
		
		if(!checkForMarkUp(model)){
			message.add(MatContext.get().getMessageDelegate().getNoMarkupAllowedMessage());
		}
		
		if("".equals(model.getFirstName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
		}
		if("".equals(model.getLastName().trim())) {
			message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
		}
		if("".equals(model.getEmailAddress().trim())) {
			message.add(MatContext.get().getMessageDelegate().getEmailIdRequired());
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
		
		if("".equals(model.getOrganizationId().trim())) {
			if(model.isActive()) {
				message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
			}
		}
		if("".equals(model.getOid().trim())) {
			if(model.isActive()) {
				message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
			}
		}
		/*if("".equals(model.getRootOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}*/
		if(model.getFirstName().length() < 2) {
			message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
		}
		if(model.getOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getOIDTooLongMessage());
		}
		/*if(model.getRootOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDTooLongMessage());
		}*/
		return message;
	}
	private boolean checkForMarkUp(ManageUsersDetailModel model) {
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
		noMarkupText = model.getPhoneNumber().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getPhoneNumber().trim().length() > noMarkupText.length()){
			return false;
		}
		
		return true;
	}
}
