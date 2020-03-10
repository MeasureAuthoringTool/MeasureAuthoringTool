package mat.server.util;

import java.text.DecimalFormat;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import mat.model.clause.ModelTypeHelper;
import mat.shared.StringUtility;

public class MeasureUtility {

    private static DecimalFormat revisionFormat = new DecimalFormat("000");

    /**
     * Gets the version text.
     *
     * @param version the version
     * @param isDraft the is draft
     * @return the version text
     */
    public static String getVersionText(String version, boolean isDraft) {

        String mVersion = formatVersionText(version);

        if (isDraft) {
            return "Draft based on v" + mVersion;
        } else {
            return "v" + mVersion;
        }
    }

    /**
     * Format version text.
     *
     * @param version the version
     * @return the string
     */
    public static String formatVersionText(String version) {
        StringUtility su = new StringUtility();
        String[] versionArr = version.split("\\.");
        String majorVersion = su.trimLeadingZeros(versionArr[0]);
        String minorVersion = su.trimLeadingZeros(versionArr[1]);
        return majorVersion + "." + minorVersion;
    }

    /**
     * Gets the version text with revision number.
     *
     * @param orgVersionNumber the org version number
     * @param revisionNumber   the revision number
     * @param draft            the draft
     * @return the version text with revision number
     */
    public static String getVersionTextWithRevisionNumber(String orgVersionNumber, String revisionNumber, boolean draft) {
        String mVersion = "v" + formatVersionText(orgVersionNumber);
        return draft ? ("Draft " + mVersion + "." + revisionFormat.format(Integer.parseInt(revisionNumber))) : mVersion;
    }

    /**
     * Format version text.
     *
     * @param revisionNumber the revision number
     * @param version        the version
     * @return the string
     */
    public static String formatVersionText(String revisionNumber, String version) {
        String mVersion = "v" + formatVersionText(version);
        return mVersion + "." + revisionFormat.format(Integer.parseInt(revisionNumber));
    }

    /**
     * This method will take a String and remove all non-alphabet/non-numeric characters
     * except underscore ("_") characters.
     *
     * @param originalString
     * @return cleanedString
     */
    public static String cleanString(String originalString) {
        originalString = originalString.replaceAll(" ", "");

        String cleanedString = "";
        for (int i = 0; i < originalString.length(); i++) {
            char c = originalString.charAt(i);
            int intc = (int) c;

            if (c == '_' || (intc >= 48 && intc <= 57) || (intc >= 65 && intc <= 90) || (intc >= 97 && intc <= 122)) {

                if (!(cleanedString.isEmpty() && Character.isDigit(c))) {
                    cleanedString = cleanedString + "" + c;
                }
            }
        }

        return cleanedString;
    }

    /**
     * Update CQL version.
     *
     * @param processor the processor
     * @param version
     */
    public static void updateCQLVersion(XmlProcessor processor, String version) throws XPathExpressionException {
        Node versionNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/version");
        if (versionNode != null) {
            versionNode.setTextContent(version);
        }
    }


    /**
     * Method to set latest model version in Draft's or clones of CQL type measure or CQL Stand Alone Library.
     **/
    public static void updateModelVersion(XmlProcessor processor, boolean isFhirMeasure) throws XPathExpressionException {

        String modelType = isFhirMeasure ? ModelTypeHelper.FHIR : ModelTypeHelper.QDM;
        String modelVersion = isFhirMeasure ? MATPropertiesService.get().getFhirVersion() : MATPropertiesService.get().getQdmVersion();

        Node cqlLibraryModelVersionNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/usingModelVersion");

        if (cqlLibraryModelVersionNode != null) {
            cqlLibraryModelVersionNode.setTextContent(modelVersion);
        }

        Node cqlLibraryModelNode = processor.findNode(processor.getOriginalDoc(), "//cqlLookUp/usingModel");

        if (cqlLibraryModelNode != null) {
            cqlLibraryModelNode.setTextContent(modelType);
        }

    }

}
