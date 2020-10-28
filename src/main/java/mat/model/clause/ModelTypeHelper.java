package mat.model.clause;

public final class ModelTypeHelper {

    public static final String FHIR = "FHIR";
    public static final String QDM = "QDM";
    public static final String PRE_CQL = "Pre-CQL";

    private ModelTypeHelper() {
        // Not for instantiation.
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
