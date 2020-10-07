package vsac.util;

import vsac.model.ValueSetWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@Slf4j
public class VsacConverter {
    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    public ValueSetWrapper toWrapper(String xml) {
        if (StringUtils.isBlank(xml)) {
            log.debug("Vsac Xml is blank");
            throw new UncheckedIOException(new IOException("Xml is blank xml: " +  xml));
        } else {
            return convertXml(xml);
        }
    }

    private ValueSetWrapper convertXml(String xml) {
        try {
            return (ValueSetWrapper) xmlMarshalUtil.convertXMLToObject("MultiValueSetMapping.xml",
                    xml,
                    ValueSetWrapper.class);
        } catch (Exception e) {
            throw new UncheckedIOException(new IOException(e));
        }
    }
}
