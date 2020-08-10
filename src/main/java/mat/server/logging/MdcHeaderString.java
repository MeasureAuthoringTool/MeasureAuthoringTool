package mat.server.logging;

import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MdcHeaderString {
    public static final String MDC_PARAMS_ID = "mdc-params";

    public static Optional<String> create() {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();

        if (CollectionUtils.isEmpty(mdcMap)) {
            return Optional.empty();
        } else {
            return Optional.of(parseMap(mdcMap));
        }
    }

    public static String parseMap(Map<String, String> mdcMap) {
        return mdcMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(" , "));
    }
}
