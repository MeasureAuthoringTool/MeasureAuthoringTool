package mat.server.export;

import org.slf4j.LoggerFactory;
import mat.server.service.SimpleEMeasureService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MeasureArtifactGenerator {

	private static final Logger logger = LoggerFactory.getLogger(MeasureArtifactGenerator.class);

	private static SimpleEMeasureService eMeasureService;

	public static void seteMeasureService(SimpleEMeasureService eMeasureService) {
		MeasureArtifactGenerator.eMeasureService = eMeasureService;
	}

	public static String getHQMFArtifact(final String id, String releaseVersion) {
		ExportResult exportResult = null;
		try {
			exportResult = releaseVersion.equals("v3") ? eMeasureService.getHQMFForv3Measure(id) : eMeasureService.getHQMF(id);
		} catch (Exception e) {
			logger.error("getHQMFArtifact: " + e.getMessage());
		}

		return exportResult != null ? exportResult.getExport() : null;
	}


	public static String getHumanReadableArtifact(final String id, String releaseVersion) {
		ExportResult exportResult = null;
		try {
			exportResult = releaseVersion.equals("v3") ? eMeasureService.getEMeasureHTML(id) : eMeasureService.getHumanReadable(id, releaseVersion);
		} catch (Exception e) {
			logger.error("getHumanReadableArtifact: " + e.getMessage());
		}

		return exportResult != null ? exportResult.getExport() : null;
	}

	public static String getCQLArtifact(final String id) {
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getCQLLibraryFile(id);
		} catch (Exception e) {
			logger.error("getCQLArtifact: " + e.getMessage());
		}

		return exportResult != null ? exportResult.getExport() : null;
	}

	public static String getELMArtifact(final String id) {
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getELMFile(id);
		} catch (Exception e) {
			logger.error("getELMArtifact: " + e.getMessage());
		}

		return exportResult != null ? exportResult.getExport() : null;
	}

	public static String getJSONArtifact(final String id) {
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getJSONFile(id);
		} catch (Exception e) {
			logger.error("getJSONArtifact: " + e.getMessage());
		}

		return exportResult != null ? exportResult.getExport() : null;
	}
}
