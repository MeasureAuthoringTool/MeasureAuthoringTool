package mat.server.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public final class MdcPairParser {
    private MdcPairParser() {
        //util class
    }

    public static void parseAndSetInMdc(String params) {
        if (StringUtils.isBlank(params)) {
            log.warn("Params string is blank");
        } else {
            String[] paramsArray = params.split(",");

            List<MdcPair> nameValuePairs = Arrays.stream(paramsArray)
                    .filter(MdcPairParser::checkParam)
                    .map(MdcPairParser::parseParam)
                    .collect(Collectors.toList());

            nameValuePairs.forEach(n -> MDC.put(n.getName(), n.getValue()));
        }

        String uuid = UUID.randomUUID().toString();
        log.info("Adding requestId UUID: {}", uuid);
        MDC.put("requestId", uuid);

        addMissingDefaultParamsToMDC();
    }

    public static void addMissingDefaultParamsToMDC() {
        addIfMissing("transactionId");
    }

    private static void addIfMissing(String key) {
        if (MDC.get(key) == null) {
            String uuid = UUID.randomUUID().toString();
            log.info("Adding default UUID: {} for MDC key: {}", uuid, key);
            MDC.put(key, uuid);
        }
    }

    private static boolean checkParam(String param) {
        if (StringUtils.isBlank(param)) {
            log.warn("Param string is blank");
            return false;
        }

        int matches = StringUtils.countMatches(param, "=");

        if (matches == 1) {
            return true;
        } else {
            log.warn("Cannot parse param string: {}", param);
            return false;
        }
    }

    private static MdcPair parseParam(String param) {
        String[] paramsArray = param.split("=");

        return MdcPair.builder()
                .name(paramsArray[0].trim())
                .value(paramsArray[1].trim())
                .build();
    }
}
