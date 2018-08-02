package mat.client.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.shared.MatConstants;

public class ManageCompositeMeasureModelValidator extends ManageMeasureModelValidator {
	public static final String ERR_COMPOSITE_MEASURE_SCORE_REQUIRED = "Composite Scoring Method is required. ";
	public static final String ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED = "A composite measure must have more than one component measure.";
	public static final String ERR_COMPONENT_MEASURE_DOES_NOT_CONTAIN_PACKAGE = " does not have a measure package and can not be used as a component measure.";
	public static final String ERR_COMPONENT_MEASURES_MUST_HAVE_SAME_PATIENT_BASED_INDICATOR = "All component measures must have the same patient-based indicator setting";
	public static final String ERR_COMPONENT_MEASURES_MUST_ALL_HAVE_ALIAS = "An alias is required for each component measure.";
	public static final String ERR_COMPONENT_MEASURE_CANNOT_BE_COMPOSITE = "A component measure can not be a composite measure.";
	
	public List<String> validateMeasureWithClone(ManageCompositeMeasureDetailModel model, boolean isClone) {
		List<String> message = performCommonMeasureValidation(model);
		if(!isClone) {
			message.addAll(validateNQF(model));
		}

		return message;
	}
	
	public List<String> validateCompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		List<String> message = performCommonMeasureValidation(manageCompositeMeasureDetailModel);
		if(!compositeMeasureContainsMoreThanOneComponentMeasure(manageCompositeMeasureDetailModel)) {
			message.add(ERR_MORE_THAN_ONE_COMPONENT_MEASURE_REQUIRED);
		}

		for(ManageMeasureSearchModel.Result appliedComponentMeasure: manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			if(!appliedComponentMeasureContainsAPackage(appliedComponentMeasure, manageCompositeMeasureDetailModel)) {
				message.add(appliedComponentMeasure.getName() + ERR_COMPONENT_MEASURE_DOES_NOT_CONTAIN_PACKAGE); //TODO
				break;
			}
		}
		
		if(compositeMeasureIncludedLibrariesHaveSameNameAndDifferentVersion(manageCompositeMeasureDetailModel)) {
			//TODO implement this
		}
		
		//check qdm version
		if(!manageCompositeMeasureDetailModel.getQdmVersion().equals(MatContext.get().getCurrentQDMVersion())) {
			message.add("The measure " + manageCompositeMeasureDetailModel.getName() + " is not using the correct version of the QDM");
		} else {
			for(ManageMeasureSearchModel.Result appliedComponentMeasure: manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
				if(!appliedComponentMeasure.getQdmVersion().equals(MatContext.get().getCurrentQDMVersion())) {
					message.add("The measure " + appliedComponentMeasure.getName() + " is not using the correct version of the QDM");
					break;
				}
			}
		}
		
		Set<Boolean> patientBasedSet = new HashSet<>();
		for(ManageMeasureSearchModel.Result appliedComponentMeasure: manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			patientBasedSet.add(appliedComponentMeasure.isPatientBased());
		}
		if(patientBasedSet.size() > 1) {
			message.add(ERR_COMPONENT_MEASURES_MUST_HAVE_SAME_PATIENT_BASED_INDICATOR);
		}
		
		if(!allComponentMeasuresHaveAnAlias(manageCompositeMeasureDetailModel)) {
			message.add(ERR_COMPONENT_MEASURES_MUST_ALL_HAVE_ALIAS);
		}
		
		//TODO check for spaces, special characters, cql keywords, etc. in alias **
		
		
		if(anyComponentMeasureIsACompositeMeasure(manageCompositeMeasureDetailModel)) {
			message.add(ERR_COMPONENT_MEASURE_CANNOT_BE_COMPOSITE);
		}

		
		//TODO check the measure scoring of the component measure based on the scoring type of the composite measure
		//manageCompositeMeasureDetailModel.getMeasScoring()?
		
		//TODO check that component measures only have one measure grouping

		return message;
	}
	
	private boolean anyComponentMeasureIsACompositeMeasure(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		for(ManageMeasureSearchModel.Result appliedComponentMeasure: manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			if(appliedComponentMeasure.getIsComposite()) {
				return true;
			}
		}
		return false;
	}

	private boolean allComponentMeasuresHaveAnAlias(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		for(ManageMeasureSearchModel.Result appliedComponentMeasure: manageCompositeMeasureDetailModel.getAppliedComponentMeasures()) {
			Map<String, String> aliasMapping = manageCompositeMeasureDetailModel.getAliasMapping();
			if(!aliasMapping.containsKey(appliedComponentMeasure.getId())) {
				return false;
			}
		}
		return true;
	}

	private boolean compositeMeasureIncludedLibrariesHaveSameNameAndDifferentVersion(
			ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean appliedComponentMeasureContainsAPackage(Result appliedComponentMeasure, ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		if(!manageCompositeMeasureDetailModel.getPackageMap().containsKey(appliedComponentMeasure.getId())) {
			return false;
		} else {
			MeasurePackageOverview packageOverview = manageCompositeMeasureDetailModel.getPackageMap().get(appliedComponentMeasure.getId());
			if(packageOverview.getPackages() != null && packageOverview.getPackages().size() > 0) {
				return true;
			}
		}

		return false;
	}

	private boolean compositeMeasureContainsMoreThanOneComponentMeasure(ManageCompositeMeasureDetailModel model) {
		boolean containsMoreThanOne = false;
		if(model.getAppliedComponentMeasures().size() > 1) {
			containsMoreThanOne = true;
		}
		return containsMoreThanOne;
	}
	
	private List<String> performCommonMeasureValidation(ManageCompositeMeasureDetailModel model) {
		List<String> message = new ArrayList<String>();

		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getMeasureNameRequiredMessage());
		}
		if ((model.getShortName() == null)
				|| "".equals(model.getShortName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getAbvNameRequiredMessage());
		}
		
		String compositeScoring = model.getCompositeScoringMethod();
		if((compositeScoring == null) || !isValidValue(compositeScoring)) {
			MatContext.get().getMessageDelegate();
			message.add(ERR_COMPOSITE_MEASURE_SCORE_REQUIRED);
		}
		
		String scoring = model.getMeasScoring();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.s_ERR_MEASURE_SCORE_REQUIRED);
		}
		
		if((scoring.equalsIgnoreCase(MatConstants.CONTINUOUS_VARIABLE) && (model.isPatientBased()))) {
			MatContext.get().getMessageDelegate();
			message.add(MessageDelegate.CONTINOUS_VARIABLE_IS_NOT_PATIENT_BASED_ERROR);
		}
		return message;
	}
}
