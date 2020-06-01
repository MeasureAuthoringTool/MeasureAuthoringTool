package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import mat.client.shared.MatRuntimeException;
import mat.model.clause.CQLLibraryExport;
import mat.model.clause.MeasureExport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.xerces.impl.dv.util.Base64;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class ExportUtility {

    private FhirContext context;

    public ExportUtility(FhirContext context) {
        this.context = context;
    }

    public String getFhirXml(MeasureExport export) {
        String result = null;
        if (StringUtils.isNotBlank(export.getMeasureJson())) {
            var parsers = buildParsers();
            Measure measure = parsers.getLeft().parseResource(Measure.class, export.getMeasureJson());
            result = parsers.getRight().encodeResourceToString(measure);
        }
        return result;
    }

    public String getIncludedLibsXml(MeasureExport export) {
        String result = null;
        if (StringUtils.isNotBlank(export.getFhirIncludedLibsJson())) {
            var parsers = buildParsers();
            Bundle libs = parsers.getLeft().parseResource(Bundle.class, export.getFhirIncludedLibsJson());
            result = parsers.getRight().encodeResourceToString(libs);
        }
        return result;
    }

    public String getFhirXml(CQLLibraryExport export) {
        String result = null;
        if (StringUtils.isNotBlank(export.getFhirJson())) {
            var parsers = buildParsers();
            Library lib = parsers.getLeft().parseResource(Library.class, export.getFhirJson());
            result = parsers.getRight().encodeResourceToString(lib);
        }
        return result;
    }

    public String getElmJson(CQLLibraryExport export) {
        String result = null;
        if (StringUtils.isNotBlank(export.getFhirJson())) {
            var parsers = buildParsers();
            Library lib = parsers.getLeft().parseResource(Library.class, export.getFhirJson());
            for (Attachment a : lib.getContent()) {
                if (StringUtils.equalsIgnoreCase(a.getContentType(),"application/elm+xml")) {
                    result = decodeBase64(a.getData());
                    break;
                }
            }
        }
        return result;
    }

    private String decodeBase64(byte[] bytes) {
        try {
        return new String(Base64.decode(new String(bytes,"utf-8")),"utf-8");
        } catch (UnsupportedEncodingException u) {
            throw new MatRuntimeException(u);
        }
    }

    private Pair<IParser,IParser> buildParsers() {
        var result = Pair.of(context.newJsonParser(),context.newXmlParser());
        result.getLeft().setPrettyPrint(true);
        result.getRight().setPrettyPrint(true);
        return result;
    }
}
