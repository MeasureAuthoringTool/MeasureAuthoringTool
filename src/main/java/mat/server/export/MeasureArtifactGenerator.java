package mat.server.export;

import mat.server.logging.LogFactory;
import mat.server.service.SimpleEMeasureService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class MeasureArtifactGenerator {

	private static final Log logger = LogFactory.getLog(MeasureArtifactGenerator.class);

	private static SimpleEMeasureService eMeasureService;

	public static void seteMeasureService(SimpleEMeasureService eMeasureService) {
		MeasureArtifactGenerator.eMeasureService = eMeasureService;
	}

	public static String getHQMFArtifact(final String id, String releaseVersion) {
		Instant start = Instant.now();
		ExportResult exportResult = null;
		try {
			exportResult = releaseVersion.equals("v3") ? eMeasureService.getHQMFForv3Measure(id) : eMeasureService.getHQMF(id);
		} catch (Exception e) {
			logger.error("getHQMFArtifact: " + e.getMessage());
		}
		logger.info("create HQMF duration::" + Duration.between(start, Instant.now()).toMillis());
		return exportResult != null ? exportResult.export : null;
	}

	public static String getHumanReadableArtifact(final String id, String releaseVersion) {
		Instant start = Instant.now();
		ExportResult exportResult = null;
		try {
			exportResult = releaseVersion.equals("v3") ? eMeasureService.getEMeasureHTML(id) : eMeasureService.getHumanReadable(id, releaseVersion);
		} catch (Exception e) {
			logger.error("getHumanReadableArtifact: " + e.getMessage());
		}

		logger.info("create HR duration::" + Duration.between(start, Instant.now()).toMillis());
		return exportResult != null ? exportResult.export : null;
	}

	public static String getCQLArtifact(final String id) {
		Instant start = Instant.now();
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getCQLLibraryFile(id);
		} catch (Exception e) {
			logger.error("getCQLArtifact: " + e.getMessage());
		}

		logger.info("create CQL duration::" + Duration.between(start, Instant.now()).toMillis());
		return exportResult != null ? exportResult.export : null;
	}

	public static String getELMArtifact(final String id) {
		Instant start = Instant.now();
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getELMFile(id);
		} catch (Exception e) {
			logger.error("getELMArtifact: " + e.getMessage());
		}

		logger.info("create ELM duration::" + Duration.between(start, Instant.now()).toMillis());
		return exportResult != null ? exportResult.export : null;
	}

	public static String getJSONArtifact(final String id) {
		Instant start = Instant.now();
		ExportResult exportResult = null;
		try {
			exportResult = eMeasureService.getJSONFile(id);
		} catch (Exception e) {
			logger.error("getJSONArtifact: " + e.getMessage());
		}

		logger.info("create JSON duration::" + Duration.between(start, Instant.now()).toMillis());
		return exportResult != null ? exportResult.export : null;
	}
}
