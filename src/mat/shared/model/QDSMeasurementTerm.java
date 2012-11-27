package mat.shared.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class QDSMeasurementTerm extends StatementTerm {
	//US171 including Relative Association Operators
	public enum QDSOperator {QDS, EAE, EBS, ECW, SBOD, SAE, SAS, SBS, DURING, 
		LINKEDTO, EAS, EBOD, EDU, SCW, SDU, CONCURRENT, AUTH, CAUS, DRIV, GOAL, OUTC};
	QDSOperator qDSOperator;
	private List<Property>properties;
	
	public QDSMeasurementTerm() {
	}
	
	public QDSMeasurementTerm(Decision lfTerm, QDSOperator qDSOperator, Decision rtTerm) {
		super(lfTerm, rtTerm);
		this.qDSOperator = qDSOperator;
	}
	
	public QDSOperator getQDSOperator() {
		return qDSOperator;
	}

	public void setQDSOperator(QDSOperator operator) {
		qDSOperator = operator;
	}

	public IQDSTerm getLfQDS() {
		return (IQDSTerm) getLfTerm();
	}

	public void setLfQDS(IQDSTerm lfQDS) {
		this.lfTerm = lfQDS;
	}

	public IQDSTerm getRtQDS() {
		return (IQDSTerm)getRtTerm();
	}

	public void setRtQDS(IQDSTerm rtQDS) {
		this.rtTerm = rtQDS;
	}

	public String getProperty() {
		if (properties == null)
			return null;
		return properties.get(0).getName();
	}
	
	public String getProperty(int i) {
		if (properties == null)
			return null;
		return (i >= 0 && i < properties.size()) 
		? properties.get(i).getName()
		: null;
	}	
	
	public List<Property> getProperties() {
		if (properties == null)
			properties = new ArrayList<Property>();
		return properties;
	}
	
	public void addProperty(String p) {
		getProperties().add(new Property(p));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("QDSMeasurementTerm");
		if (properties != null)
			sb.append(".").append(properties);
		sb.append("\n");
		sb.append(lfTerm.toString());
		if (qDSOperator != null)
			sb.append(qDSOperator).append(" ");
		sb.append(rtTerm.toString());
		return sb.toString();
	}	
}
