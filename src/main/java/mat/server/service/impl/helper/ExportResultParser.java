package mat.server.service.impl.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mat.server.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Reads over the Data Requirements for a FHIR measure and builds a
 * unique List of Data Requirements that do not have a backing Value Set
 * or Direct Reference Code (DRC).
 *
 * A Data Requirement is considered to be without a Value Set or Code when
 * either the Data Requirement's Code Filter is null OR its Code Filter's Value Set and Code
 * values are both null.
 *
 * Based on the QMIG, the dataRequirement list should be a unique list of all retrieves with or without
 * value set or DRC.
 */
public class ExportResultParser {
    private static final Log LOGGER = LogFactory.getLog(ExportResultParser.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String json;

    private Set<String> results;

    public ExportResultParser(String json) {
        this.json = json;
    }

    public List<String> parseDataRequirement() {
        try {
            results = new TreeSet<>();

            JsonNode rootObjectNode = OBJECT_MAPPER.readTree(json);
            JsonNode entryArrayNode = rootObjectNode.get("entry");

            if (entryArrayNode != null && entryArrayNode.isArray()) {
                entryArrayNode.forEach(this::processResource);
            } else {
                LOGGER.debug("No entry ArrayNode found");
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Cannot process Data Requirements", e);
        }

        return List.copyOf(results);
    }

    private void processResource(JsonNode node) {
        ObjectNode resourceObjectNode = (ObjectNode) node.get("resource");

        if (resourceObjectNode != null) {
            processDataRequirement(resourceObjectNode);
        } else {
            LOGGER.trace("No resource ObjectNode found");
        }
    }

    private void processDataRequirement(ObjectNode node) {
        JsonNode dataRequirementArrayNode = node.get("dataRequirement");

        if (dataRequirementArrayNode != null && dataRequirementArrayNode.isArray()) {
            dataRequirementArrayNode.forEach(this::processTargetNode);
        } else {
            LOGGER.debug("No dataRequirement ArrayNode found");
        }
    }

    private void processTargetNode(JsonNode node) {
        JsonNode typeNode = node.get("type");

        if (typeNode == null) {
            LOGGER.debug("No dataRequirement ArrayNode found");
            return;
        }

        if (isTargetValid(node)) {
            results.add(typeNode.asText());
        } else {
            LOGGER.debug("ValueSet/Code node found, not creating entry");
        }
    }

    private boolean isTargetValid(JsonNode node) {
        JsonNode codeFilter = node.get("codeFilter");

        //JsonNode pathFilterNode = null; -- what to do with path do we care

        return codeFilter == null || codeFilter.size() < 1 || (codeFilter.get(0).get("valueSet") == null && codeFilter.get(0).get("code") == null);
    }
}

