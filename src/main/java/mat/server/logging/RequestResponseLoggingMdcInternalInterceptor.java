package mat.server.logging;


import lombok.extern.slf4j.Slf4j;
import mat.server.LoggedInUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static mat.server.logging.MdcHeaderString.MDC_PARAMS_ID;


@Slf4j
public class RequestResponseLoggingMdcInternalInterceptor extends RequestResponseLoggingInterceptor {
    @Override
    protected void processHeaders(HttpRequest request) {
        Map<String, String> headerMap = new HashMap<>();

        String loggedInLoginId = LoggedInUserUtil.getLoggedInLoginId();

        if (StringUtils.isNotEmpty(loggedInLoginId)) {
            headerMap.put("userId", loggedInLoginId);
        } else {
            log.warn("No loggedInLoginId found");
        }

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false); // true == allow create

        if (session != null) {
            headerMap.put("sessionId", session.getId());
        } else {
            log.warn("No session found");
        }

        headerMap.put("requestId", UUID.randomUUID().toString());
        headerMap.put("transactionId", UUID.randomUUID().toString());

        // requestId=9008df12-2814-40d3-b322-8ab3cc9902d9 , sessionId=3026825D9B2FC60E99A171E72F340495 , userId=CaDay5035 , transactionId=022974cb-6ed6-4186-8b1c-46845563b1bb
        String paramString = MdcHeaderString.parseMap(headerMap);
        log.debug("MDCParamString: {}", paramString);

        headerMap.forEach(MDC::put);

        request.getHeaders().add(MDC_PARAMS_ID, paramString);
    }
}

