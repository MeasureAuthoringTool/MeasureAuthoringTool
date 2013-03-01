package mat.server.model;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class MeasureDetailsHandler extends GeneralizedFieldHandler{

	@Override
	public Object convertUponGet(Object value) {
		if(value instanceof Integer){
			if((Integer)value == 0){
				return null;
			}
		}else if (value instanceof Boolean){
			return ((Boolean)value) ? "Yes" : "No";
		}
		return value;
	}

	@Override
	public Object convertUponSet(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getFieldType() {
		// TODO Auto-generated method stub
		return null;
	}

}
