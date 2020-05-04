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
     * @param model the model
     * @return the list
     */
    public List<String> isValidUsersDetail(ManageUsersDetailModel model) {
        List<String> message = new ArrayList<>();
        if (model.getFirstName() == null || model.getFirstName().trim().isEmpty()) {
            message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
        }
        if (model.getLastName() == null || model.getLastName().trim().isEmpty()) {
            message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
        }
        if (model.getEmailAddress() == null || model.getEmailAddress().trim().isEmpty()) {
            message.add(MatContext.get().getMessageDelegate().getEmailIdRequired());
        }
        if (model.getPhoneNumber() == null || model.getPhoneNumber().trim().isEmpty()) {
            message.add(MatContext.get().getMessageDelegate().getPhoneRequiredMessage());
        }
        if (!model.isExistingUser() && (model.getHarpId() == null || model.getHarpId().trim().isEmpty())) {
            message.add(MatContext.get().getMessageDelegate().getHarpIdRequiredMessage());
        }
        int numCount = 0;
        String phoneNum = model.getPhoneNumber();
        if (phoneNum != null) {
            int i;
            for (i = 0; i < phoneNum.length(); i++) {
                if (Character.isDigit(phoneNum.charAt(i))) {
                    numCount++;
                }
            }
        }
        if (numCount != 10) {
            message.add(MatContext.get().getMessageDelegate().getPhoneTenDigitMessage());
        }

        if (model.getOrganizationId() == null || model.getOrganizationId().trim().isEmpty()) {
            if (model.isActive()) {
                message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
            }
        }
        if (model.getOid() == null || model.getOid().trim().isEmpty()) {
            if (model.isActive()) {
                message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
            }
        }
		/*if("".equals(model.getRootOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}*/
        if (model.getFirstName() == null || model.getFirstName().length() < 2) {
            message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
        }
        if (model.getOid() != null && model.getOid().length() > 50) {
            message.add(MatContext.get().getMessageDelegate().getOIDTooLongMessage());
        }
		/*if(model.getRootOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDTooLongMessage());
		}*/
        return message;
    }

}
