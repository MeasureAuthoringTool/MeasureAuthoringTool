package mat.model.cql;

import mat.model.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * @author jnarang
 *
 */
public class CQLParameterTransferModel implements IsSerializable , BaseModel {
	
	/**
	 * {@link CQLParameterModelObject} - Exisiting Parameter
	 */
	private CQLParameterModelObject toBeModifiedParameter;
	/**
	 * {@link CQLParameterModelObject} - Modified Parameter
	 */
	private CQLParameterModelObject modifiedParameter;
	/**
	 * String - For Measure Id.
	 */
	private String currentMeasureId;
	
	@Override
	public void scrubForMarkUp() {
		String markupRegExp = "<[^>]+>";
		if (!modifiedParameter.getIdentifier().isEmpty()) {
			String noMarkupText = modifiedParameter.getIdentifier().trim().replaceAll(markupRegExp, "");
			System.out.println("Parameter Identifier Value after scrub up :::: " + noMarkupText);
			if (modifiedParameter.getIdentifier().trim().length() > noMarkupText.length()) {
				modifiedParameter.setIdentifier(noMarkupText);
			}
		}
		if (!modifiedParameter.getTypeSpecifier().isEmpty()) {
			String noMarkupText = modifiedParameter.getTypeSpecifier().trim().replaceAll(markupRegExp, "");
			System.out.println("Parameter Type Specifier Value after scrub up :::: " + noMarkupText);
			if (modifiedParameter.getTypeSpecifier().trim().length() > noMarkupText.length()) {
				modifiedParameter.setTypeSpecifier(noMarkupText);
			}
		}
	}
	
	
	public CQLParameterModelObject getToBeModifiedParameter() {
		return toBeModifiedParameter;
	}
	
	
	public void setToBeModifiedParameter(CQLParameterModelObject toBeModifiedParameter) {
		this.toBeModifiedParameter = toBeModifiedParameter;
	}
	
	
	public CQLParameterModelObject getModifiedParameter() {
		return modifiedParameter;
	}
	
	
	public void setModifiedParameter(CQLParameterModelObject modifiedParameter) {
		this.modifiedParameter = modifiedParameter;
	}
	
	
	public String getCurrentMeasureId() {
		return currentMeasureId;
	}
	
	
	public void setCurrentMeasureId(String currentMeasureId) {
		this.currentMeasureId = currentMeasureId;
	}
}
