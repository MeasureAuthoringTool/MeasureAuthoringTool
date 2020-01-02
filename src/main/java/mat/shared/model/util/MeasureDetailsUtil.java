package mat.shared.model.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    public static List<String> getTrimmedList(List<String> listA) {
        ArrayList<String> newAList = new ArrayList<String>();
        if ((listA != null) && (listA.size() > 0)) {
            for (String aStr : listA) {
                String val = trimToNull(aStr);
                if (null != val) {
                    newAList.add(val);
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
        return type == null || type.isEmpty() ? PRE_CQL : type;
    }

    public static boolean isValidatable(Measure measure) {
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
}
