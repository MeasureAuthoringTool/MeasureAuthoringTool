/**
 * 
 */
package mat.shared;

import java.util.ArrayList;
import java.util.List;
import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.shared.MatContext;

/**
 * The Class AdminManageUserModelValidator.
 * 
 * @author jnarang
 */
public class AdminManageOrganizationModelValidator {
	
	
	/** Checks if is valid Organization detail.
	 * 
	 * @param model the model
	 * @return the list */
	public List<String> isValidOrganizationDetail(ManageOrganizationDetailModel model) {
		List<String> message = new ArrayList<String>();
		if(!checkForMarkUp(model)){
			message.add(MatContext.get().getMessageDelegate().getNoMarkupAllowedMessage());
		}
		if ("".equals(model.getOrganization().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
		}
		
		if(model.getOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getOIDTooLongMessage());
		}
		return message;
	}
	/**
	 * @param model
	 * @return
	 */
	private boolean checkForMarkUp(ManageOrganizationDetailModel model) {
		String markupRegExp = "<[^>]+>";
		
		String noMarkupText = model.getOrganization().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getOrganization().trim().length() > noMarkupText.length()){
			return false;
		}
		noMarkupText = model.getOid().trim().replaceAll(markupRegExp, "");
		System.out.println(noMarkupText);
		if(model.getOid().trim().length() > noMarkupText.length()){
			return false;
		}
		return true;
	}
}
