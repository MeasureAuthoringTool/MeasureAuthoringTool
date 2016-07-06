package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;
import mat.server.util.XmlProcessor;

/**
 * The Class CQLbasedHQMFGenerator
 * 
 * @author jmeyer
 *
 */
public class CQLbasedHQMFGenerator implements Generator {

	/**
	 * Generate hqmf for CQL Based measures (version 5.0 and greater)
	 */
	@Override
	public String generate(MeasureExport me) {
		System.out.println("Exporting CQL based HQMF");

		String hqmfXML = "";
		try {

			// MAT 6911: Export CQL based HQMF w/ Meta Data Section
			String eMeasureDetailsXML = new CQLbasedHQMFMeasureDetailsGenerator().generate(me);
			// Inline comments are added after the end of last componentOf tag.
			// This is removed in this method
			eMeasureDetailsXML = replaceInlineCommentFromEnd(eMeasureDetailsXML);
			hqmfXML += eMeasureDetailsXML;

			// TODO Export more Stuff

		} catch (Exception e) {
			LOG.error("Unable to generate human readable. Exception Stack Strace is as followed : ");
			e.printStackTrace();
		}
		return hqmfXML;
	}

	/**
	 * Inline comments are added after the end of last componentOf tag. This is
	 * removed in this method
	 * 
	 * @param eMeasureDetailsXML
	 *            - String eMeasureDetailsXML.
	 * @return String eMeasureDetailsXML.
	 */
	private String replaceInlineCommentFromEnd(String eMeasureDetailsXML) {
		int indexOfComponentOf = eMeasureDetailsXML.lastIndexOf("</componentOf>");
		eMeasureDetailsXML = eMeasureDetailsXML.substring(0, indexOfComponentOf);
		eMeasureDetailsXML = eMeasureDetailsXML.concat("</componentOf></QualityMeasureDocument>");
		return eMeasureDetailsXML;
	}

}
