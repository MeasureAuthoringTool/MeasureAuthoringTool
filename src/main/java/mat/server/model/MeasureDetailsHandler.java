package mat.server.model;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * The Class MeasureDetailsHandler.
 */
public class MeasureDetailsHandler extends GeneralizedFieldHandler{

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
	 */
	@Override
	public Object convertUponSet(Object arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class getFieldType() {
		return null;
	}

}
