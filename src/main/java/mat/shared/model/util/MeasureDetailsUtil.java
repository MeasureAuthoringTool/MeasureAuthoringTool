package mat.shared.model.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import mat.client.measure.ReferenceTextAndType;
import mat.model.clause.Measure;
import mat.shared.CompositeMethodScoringConstant;
import mat.shared.ConstantMessages;

/**
 * The Class MeasureDetailsUtil.
 */
public class MeasureDetailsUtil {

    private static final String PATIENT_LINEAR_ABBREVIATION = "LINEARSCR";
    private static final String OPPORTUNITY_ABBREVIATION = "OPPORSCR";
    private static final String ALL_OR_NOTHING_ABBREVIATION = "ALLORNONESCR";
    private static final String COHORT_ABBREVIATION = "COHORT";
    private static final String RATIO_ABBREVIATION = "RATIO";
    private static final String CONTINUOUS_VARIABLE_ABBREVIATION = "CONTVAR";
    private static final String PROPORTION_ABBREVIATION = "PROPOR";
    public static final String FHIR = "FHIR";
    public static final String QDM = "QDM";
    public static final String PRE_CQL = "Pre-CQL";
    public static final String MAT_ON_FHIR = "MAT_ON_FHIR";
    public static final String FHIR_CQL = "FHIR / CQL";
    public static final String QDM_QDM = "QDM / QDM";
    public static final String QDM_CQL = "QDM / CQL";



    public static final BigDecimal RUN_FHIR_VALIDATION_VERSION = new BigDecimal("5.8");
    public static final BigDecimal RUN_FHIR_VALIDATION_QDM_VERSION = new BigDecimal("5.5");

    /**
     * Gets the scoring abbr.
     *
     * @param scoring the scoring
     * @return the scoring abbr
     */
    public static String getScoringAbbr(String scoring) {
        String abbr = "";
        if (scoring.equalsIgnoreCase(ConstantMessages.CONTINUOUS_VARIABLE_SCORING)) {
            abbr = CONTINUOUS_VARIABLE_ABBREVIATION;
        } else if (scoring.equalsIgnoreCase(ConstantMessages.PROPORTION_SCORING)) {
            abbr = PROPORTION_ABBREVIATION;
        } else if (scoring.equalsIgnoreCase(ConstantMessages.RATIO_SCORING)) {
            abbr = RATIO_ABBREVIATION;
        } else if (scoring.equalsIgnoreCase(ConstantMessages.COHORT_SCORING)) {
            abbr = COHORT_ABBREVIATION;
        }
        return abbr;
    }

    public static String getCompositeScoringAbbreviation(String scoring) {
        String abbreviation = "";
        if (scoring.equalsIgnoreCase(CompositeMethodScoringConstant.ALL_OR_NOTHING)) {
            abbreviation = ALL_OR_NOTHING_ABBREVIATION;
        } else if (scoring.equalsIgnoreCase(CompositeMethodScoringConstant.OPPORTUNITY)) {
            abbreviation = OPPORTUNITY_ABBREVIATION;
        } else if (scoring.equalsIgnoreCase(CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR)) {
            abbreviation = PATIENT_LINEAR_ABBREVIATION;
        }

        return abbreviation;
    }

    /**
     * Gets the trimmed list.
     *
     * @param listA the list a
     * @return the trimmed list
     */
    public static List<ReferenceTextAndType> getTrimmedList(List<ReferenceTextAndType> listA) {
        ArrayList<ReferenceTextAndType> newAList = new ArrayList<>();
        if ((listA != null) && (listA.size() > 0)) {
            for (ReferenceTextAndType aRef : listA) {
                String val = trimToNull(aRef.getReferenceText());
                if (null != val) {
                    newAList.add(new ReferenceTextAndType(val, aRef.getReferenceType()));
                }
            }
        }
        return newAList;
    }

    /**
     * Trim to null.
     *
     * @param value the value
     * @return the string
     */
    private static String trimToNull(String value) {
        if (null != value) {
            value = value.replaceAll("[\r\n]", "");
            value = value.equals("") ? null : value.trim();

        }
        return value;
    }

    public static String defaultTypeIfBlank(String type) {
        return type == null || type.trim().isEmpty() ? PRE_CQL : type;
    }

    public static boolean isPreQL(String type) {
        return PRE_CQL.equalsIgnoreCase(defaultTypeIfBlank(type));
    }

    public static boolean isFhir(String type) {
        return FHIR.equalsIgnoreCase(defaultTypeIfBlank(type));
    }

    public static boolean isQdm(String type) {
        return QDM.equalsIgnoreCase(defaultTypeIfBlank(type));
    }

    public static String getModelTypeDisplayName(String type) {
        if (FHIR.equalsIgnoreCase(type)) {
            return FHIR_CQL;
        } else if (QDM.equalsIgnoreCase(type)) {
            return QDM_CQL;
        } else {
            return QDM_QDM;
        }
    }

    public static boolean isValidatable(Measure measure) {
        if (measure.getMeasureModel() == null || measure.getQdmVersion() == null || measure.getReleaseVersion() == null) {
            return false;
        }
        BigDecimal matVersion = asDecimalVersion(measure.getReleaseVersion());
        BigDecimal qdmVersion = asDecimalVersion(measure.getQdmVersion());

        return (measure.isDraft() && measure.getMeasureModel().equals(FHIR))
                || (!measure.isDraft() && measure.getMeasureModel().equals(QDM)
                && RUN_FHIR_VALIDATION_VERSION.compareTo(matVersion) <= 0
                && RUN_FHIR_VALIDATION_QDM_VERSION.compareTo(qdmVersion) <= 0);
    }

    private static BigDecimal asDecimalVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(version.trim().replaceAll("v", ""));
        } catch (NullPointerException | IllegalArgumentException e) {
            return BigDecimal.ZERO;
        }
    }

    public static boolean isPackageable(String currentMeasureModel, boolean packageFeatureFlagStatus) {
        return FHIR.equalsIgnoreCase(defaultTypeIfBlank(currentMeasureModel)) && !packageFeatureFlagStatus;
    }
}
