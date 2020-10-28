package mat.server.model;

import mat.client.measure.ReferenceTextAndType;
import mat.shared.measure.measuredetails.models.MeasureReferenceType;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class MeasureDetailsReferencesHandler extends GeneralizedFieldHandler {

    @Override
    public Object convertUponGet(Object value) {
        return value;
    }

    @Override
    public Object convertUponSet(Object arg0) {
        if (arg0 == null) {
            return null;
        }
        return new ReferenceTextAndType(arg0.toString(), MeasureReferenceType.UNKNOWN);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getFieldType() {
        return ReferenceTextAndType.class;
    }

}
