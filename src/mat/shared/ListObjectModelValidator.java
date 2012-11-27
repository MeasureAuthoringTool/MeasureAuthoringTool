/**
 * 
 */
package mat.shared;

import java.util.ArrayList;
import java.util.List;

import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.shared.MatContext;
import mat.model.Code;

/**
 * @author vandavar
 * Server-Side Validator for the ListObject Model
 *
 */
public class ListObjectModelValidator {
	
	public static class  CodeModelValidator{
	    public List<String> validate(Code codemodel){
	    	List<String> messages = new ArrayList<String>();
	    	if("".equals(codemodel.getCode().trim())) {
				messages.add(MatContext.get().getMessageDelegate().getCodeRequiredMessage());
			}
			if("".equals(codemodel.getDescription().trim())) {
				messages.add(MatContext.get().getMessageDelegate().getDescriptorRequiredMessage());
			}
			return  messages;
	    }
	}
	

	
	public List<String> ValidateListObject(ManageCodeListDetailModel model){	
		 List<String> messages = new ArrayList<String>();
		if(model.getIsGrouped()){
			if("".equals(model.getName().trim())) 
				messages.add(MatContext.get().getMessageDelegate().getGroupedNameRequiredMessage());
		}else{
			if("".equals(model.getName().trim())) 
				messages.add(MatContext.get().getMessageDelegate().getNameRequiredMessage());
		}
		
		if("".equals(model.getSteward().trim())) {
			messages.add(MatContext.get().getMessageDelegate().getStewardRequiredMessage());
		}else{
			//US 413. Validate Steward Other value if any
			String stewardValue = model.getStewardValue();
			if(stewardValue != null && stewardValue.equalsIgnoreCase("Other")){
				if(model.getStewardOther()== null || "".equalsIgnoreCase(model.getStewardOther())){
					messages.add(MatContext.get().getMessageDelegate().getStewardRequiredMessage());
				}
			}
		}
		if("".equals(model.getCategory().trim())) {
			messages.add(MatContext.get().getMessageDelegate().getCategoryRequiredMessage());
		}
		if("".equals(model.getOid().trim())) {
			messages.add(MatContext.get().getMessageDelegate().getOIDRequiredMessage());
		}
		if("".equals(model.getRationale().trim())){
			messages.add(MatContext.get().getMessageDelegate().getRationaleRequiredMessage());
		}
		return messages;
		
	}
    
	public List<String> validatecodeListonlyFields(ManageCodeListDetailModel model){
		 List<String> messages = new ArrayList<String>();
			if("".equals(model.getCodeSystem().trim())) {
				messages.add(MatContext.get().getMessageDelegate().getCodeSystemRequiredMessage());
			}
			if("".equals(model.getCodeSystemVersion().trim())) {
				messages.add(MatContext.get().getMessageDelegate().getCodeVersionRequiredMessage());
			}
		 return messages;
	}
	
}
