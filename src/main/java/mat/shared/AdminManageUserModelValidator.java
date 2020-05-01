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
        List<String> message = new ArrayList<>();
        if ("".equals(model.getFirstName().trim())) {
            message.add(MatContext.get().getMessageDelegate().getFirstNameRequiredMessage());
        }
        if ("".equals(model.getLastName().trim())) {
            message.add(MatContext.get().getMessageDelegate().getLastNameRequiredMessage());
        }
        if ("".equals(model.getEmailAddress().trim())) {
            message.add(MatContext.get().getMessageDelegate().getEmailIdRequired());
        }
        if ("".equals(model.getPhoneNumber().trim())) {
            message.add(MatContext.get().getMessageDelegate().getPhoneRequiredMessage());
        }
        if (!model.isExistingUser() && "".equals(model.getHarpId().trim())) {
            message.add(MatContext.get().getMessageDelegate().getHarpIdRequiredMessage());
        }

        String phoneNum = model.getPhoneNumber();
        int i, numCount;
        numCount = 0;
        for (i = 0; i < phoneNum.length(); i++) {
            if (Character.isDigit(phoneNum.charAt(i))) {
                numCount++;
            }
        }
        if (numCount != 10) {
            message.add(MatContext.get().getMessageDelegate().getPhoneTenDigitMessage());
        }

        if ("".equals(model.getOrganizationId().trim())) {
            if (model.isActive()) {
                message.add(MatContext.get().getMessageDelegate().getOrgRequiredMessage());
            }
        }
        if ("".equals(model.getOid().trim())) {
            if (model.isActive()) {
                message.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
            }
        }
		/*if("".equals(model.getRootOid().trim())) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDRequiredMessage());
		}*/
        if (model.getFirstName().length() < 2) {
            message.add(MatContext.get().getMessageDelegate().getFirstMinMessage());
        }
        if (model.getOid().length() > 50) {
            message.add(MatContext.get().getMessageDelegate().getOIDTooLongMessage());
        }
		/*if(model.getRootOid().length() > 50) {
			message.add(MatContext.get().getMessageDelegate().getRootOIDTooLongMessage());
		}*/
        return message;
    }

}
