package mat.server.service.cql;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;

class CqlModelSerializer extends StdSerializer<CQLModel> {

    public CqlModelSerializer() {
        this(null);
    }

    public CqlModelSerializer(Class<CQLModel> model) {
        super(model);
    }

    @Override
    public void serialize(CQLModel model, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(CQLUtilityClass.getXMLFromCQLModel(model));
    }
}
