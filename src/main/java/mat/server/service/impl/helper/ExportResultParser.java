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

        if (isTargetValid(typeNode)) {
            results.add(typeNode.asText());
        } else {
            LOGGER.debug("ValueSet node found, not creating entry");
        }
    }

    private boolean isTargetValid(JsonNode typeNode) {
        JsonNode codeFilter = typeNode.get("codeFilter");

        //JsonNode pathFilterNode = null; -- what to do with path do we care

        return codeFilter == null || codeFilter.get("valueSet") == null;
    }
}

