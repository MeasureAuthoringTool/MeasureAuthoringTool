/**
 * 
 */
package mat.shared;

import mat.client.admin.ManageOrganizationDetailModel;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;

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
	public List<String> getValidationErrors(ManageOrganizationDetailModel model) {
		List<String> message = new ArrayList<String>();
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
	
	/** Checks if is valid Organization detail.
	 * 
	 * @param model the model
	 * @return boolean */
	public boolean isManageOrganizationDetailModelValid(ManageOrganizationDetailModel model) {
		boolean isModelValid = true;
		List<String> validationErrors = getValidationErrors(model);
		if(validationErrors.size() > 0) {
			isModelValid = false;
		}
		return isModelValid;
	}
}
