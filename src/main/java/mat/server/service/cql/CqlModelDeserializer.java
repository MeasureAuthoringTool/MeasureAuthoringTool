package mat.server.service.cql;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;

class CqlModelDeserializer extends StdDeserializer<CQLModel> {

    public CqlModelDeserializer() {
        this(null);
    }

    public CqlModelDeserializer(Class<?> model) {
        super(model);
    }

    @Override
    public CQLModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String xml = p.getText();
        return StringUtils.isBlank(xml) ? null : CQLUtilityClass.getCQLModelFromXML(xml);
    }
}
