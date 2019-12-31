package mat.shared.model.util;

import java.util.ArrayList;
import java.util.List;

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

}
