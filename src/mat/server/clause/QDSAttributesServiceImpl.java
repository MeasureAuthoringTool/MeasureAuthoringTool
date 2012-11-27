package mat.server.clause;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mat.client.clause.QDSAttributesService;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.clause.QDSAttributes;
import mat.server.SpringRemoteServiceServlet;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class QDSAttributesServiceImpl extends SpringRemoteServiceServlet implements QDSAttributesService {
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;

	@Override
	public List<QDSAttributes> getAllDataTypeAttributes(String dataTypeName) {
		List<QDSAttributes> attrs = getDAO().findByDataType(dataTypeName, context);
		List<QDSAttributes> attrs1 = getAllDataFlowAttributeName();
		Collections.sort(attrs, attributeComparator);
		Collections.sort(attrs1, attributeComparator);
		attrs.addAll(attrs1);
		//Collections.sort(attrs, attributeComparator);
		return attrs;
	}

	private Comparator<QDSAttributes> attributeComparator = new Comparator<QDSAttributes>(){
		@Override
		public int compare(QDSAttributes arg0, QDSAttributes arg1) {
			return arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());
		}
	};
	
	@Override
	public List<QDSAttributes> getAllDataFlowAttributeName() {
		return getDAO().getAllDataFlowAttributeName();
	}
	
	public QDSAttributesDAO getDAO() {
		return (QDSAttributesDAO)context.getBean("qDSAttributesDAO");
	}

}
