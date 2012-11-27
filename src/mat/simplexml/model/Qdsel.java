package mat.simplexml.model;

import mat.shared.model.MeasurementTerm;
import mat.shared.model.StatementTerm.Operator;

public class Qdsel extends FunctionHolder implements IPhrase{
	private Qdsel qdsel;
	private Id id;
	private String idAttr;
	private To to;
	private String toAttr;
	private String highnum;
	private String relnegation;
	private String ttext;
	private String highinclusive;
	private String codesystemname;
	private String toorder;
	private String codesystem;
	private String order;
	private String lowinclusive;
	private String oid;
	private String highunit;
	private String name;
	private Value value;
	private String uuid;
	private String rel;
	private String datatype;
	private String lownum;
	private String lowunit;
	private Reference reference;
	private String property;
	private Properties properties;
	private String taxonomy;
	
	public String getEqualnum() {
		return equalnum;
	}
	public void setEqualnum(String equalnum) {
		this.equalnum = equalnum;
	}
	public String getEqualunit() {
		return equalunit;
	}
	public void setEqualunit(String equalunit) {
		this.equalunit = equalunit;
	}

	private String equalnum;
	private String equalunit;

	private String aritnum;
	private String aritunit;
	
	private String aritop;

	
	public String getAritop() {
		return aritop;
	}
	public void setAritop(String aritop) {
		this.aritop = aritop;
	}
	public String getAritnum() {
		return aritnum;
	}
	public void setAritnum(String aritnum) {
		this.aritnum = aritnum;
	}
	public String getAritunit() {
		return aritunit;
	}
	public void setAritunit(String aritunit) {
		this.aritunit = aritunit;
	}

	public Qdsel getQdsel() {
		return qdsel;
	}
	public void setQdsel(Qdsel qdsel) {
		this.qdsel = qdsel;
	}
	public String getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	public void diagram( PrettyPrinter pp) {
		pp.concat("QDSEL");
		pp.incrementIndentation();
		if (getQdsel() != null)
			getQdsel().diagram(pp);
		if (getId() != null) 
			getId().diagram(pp);
		if (getIdAttr() != null)
			pp.concat("IDATTR", getIdAttr());
		if (getProperty() != null)
			pp.concat("PROPERTY", getProperty());
		if (getName() != null)
			pp.concat("NAME", getName());
		if (getDatatype() != null)
			pp.concat("DATATYPE", getDatatype());
		if (getRel() != null) 
			pp.concat("REL", getRel());
		if (getHighnum() != null) {
			pp.concat("HIGHNUM", getHighnum());
			pp.concat("HIGHUNIT", getHighunit());
		}
		if (getHighinclusive() != null) {
			pp.concat("HIGHINCLUSIVE", getHighinclusive());
			pp.concat("HIGHUNIT", getHighunit());
		}
		if (getLownum() != null) {
			pp.concat("LOWNUM", getLownum());
			pp.concat("LOWUNIT", getLowunit());
		}
		if (getLowinclusive() != null) {
			pp.concat("LOWINCLUSIVE", getLowinclusive());
			pp.concat("LOWUNIT", getLowunit());
		}
		if (getTo() != null)
			getTo().diagram(pp);
		if (getToAttr() != null)
			pp.concat("TOATTR", getToAttr());
		if (getValue() != null)
			getValue().diagram(pp);
		if (getReference() != null)
			getReference().diagram(pp);
		pp.decrementIndentation();
	}
	
	public Qdsel() {
	}
	public Qdsel(String id) {
		setIdAttr(id);
	}
	public Id getId() {
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public To getTo() {
		return to;
	}
	public void setTo (To to ) {
		this.to = to;
	}
	public String getHighnum() {
   	return highnum;
}
	public void setHighnum (String highnum ) {
		this.highnum = highnum;
	}
	public String getRelnegation() {
   	return relnegation;
}
	public void setRelnegation (String relnegation ) {
		this.relnegation = relnegation;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public String getHighinclusive() {
   	return highinclusive;
}
	public void setHighinclusive (String highinclusive ) {
		this.highinclusive = highinclusive;
	}
	public String getCodesystemname() {
   	return codesystemname;
}
	public void setCodesystemname (String codesystemname ) {
		this.codesystemname = codesystemname;
	}
	public String getIdAttr() {
   	return idAttr;
}
	public void setIdAttr (String idAttr ) {
		this.idAttr = idAttr;
	}
	public String getToAttr() {
		return toAttr;
	}
	public void setToAttr(String toAttr) {
		this.toAttr = toAttr;
	}
	public String getToorder() {
   	return toorder;
}
	public void setToorder (String toorder ) {
		this.toorder = toorder;
	}
	public String getCodesystem() {
   	return codesystem;
}
	public void setCodesystem (String codesystem ) {
		this.codesystem = codesystem;
	}
	public String getOrder() {
   	return order;
}
	public void setOrder (String order ) {
		this.order = order;
	}
	public String getLowinclusive() {
   	return lowinclusive;
}
	public void setLowinclusive (String lowinclusive ) {
		this.lowinclusive = lowinclusive;
	}
	public String getOid() {
   	return oid;
}
	public void setOid (String oid ) {
		this.oid = oid;
	}
	public String getHighunit() {
   	return highunit;
}
	public void setHighunit (String highunit ) {
		this.highunit = highunit;
	}
	public String getName() {
   	return name;
}
	public void setName (String name ) {
		this.name = name;
	}
	
	public Value getValue() {
		return value;
	}
	public void setValue (Value value ) {
		this.value = value;
	}
	public void setValue(String quantity, String unit) {
		setValue(new Value(quantity, unit));
	}
	public void setValueAttr(MeasurementTerm measurementTerm, Operator operator) {
		switch (operator) {
			case LESS_THAN:	
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("false");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());
				}
				break;
			case LESS_THAN_OR_EQUAL_TO:
				setHighnum(measurementTerm.getQuantity());
				setHighinclusive("true");
				if(isUnitNotEmpty(measurementTerm)){
					setHighunit(measurementTerm.getUnit());
				}
				break;					
			case GREATER_THAN:
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("false");
				if(isUnitNotEmpty(measurementTerm)){
					setLowunit(measurementTerm.getUnit());
				}
				break;
			case GREATER_THAN_OR_EQUAL_TO:
				setLownum(measurementTerm.getQuantity());
				setLowinclusive("true");
				if(isUnitNotEmpty(measurementTerm)){
					setLowunit(measurementTerm.getUnit());
				}
				break;
			case EQUAL_TO:
				setEqualnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setEqualunit(measurementTerm.getUnit());
				}
				break;
			case NOT_EQUAL_TO:
				setEqualnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setEqualunit(measurementTerm.getUnit());
				}
				break;
			case PLUS:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(PLUS_OP);
				break;
			case MINUS:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(MINUS_OP);
				break;
			case TIMES:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(MULTIPLY_OP);
				break;
			case DIVIDE_BY:
				setAritnum(measurementTerm.getQuantity());
				if(isUnitNotEmpty(measurementTerm)){
					setAritunit(measurementTerm.getUnit());
				}
				setAritop(DIVIDE_OP);
				break;
		}
	}	
	public String getUuid() {
   	return uuid;
}
	public void setUuid (String uuid ) {
		this.uuid = uuid;
	}
	public String getRel() {
   	return rel;
}
	public void setRel (String rel ) {
		this.rel = rel;
	}
	public String getDatatype() {
   	return datatype;
}
	public void setDatatype (String datatype ) {
		this.datatype = datatype;
	}
	public String getLownum() {
   	return lownum;
}
	public void setLownum (String lownum ) {
		this.lownum = lownum;
	}
	public String getLowunit() {
   	return lowunit;	
}
	public void setLowunit (String lowunit ) {
		this.lowunit = lowunit;
	}

	public Reference getReference() {
		return reference;
	}
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getProperty() {
		return property;
	}
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties ps) {
		this.properties = ps;
	}
	
	public boolean equals(Object temp) {
		if (temp == null || !(temp instanceof Qdsel)) return false;
		
		Qdsel tempQDS = (Qdsel)temp;

		if (tempQDS.getIdAttr() != null && getIdAttr() != null  && 
				tempQDS.getIdAttr().trim().equals(getIdAttr().trim())) {
			return true;
		}

		return false;
	}
	
	private boolean isUnitNotEmpty(MeasurementTerm mTerm){
	     return mTerm.getUnit()!= null && !mTerm.getUnit().trim().equals("");
	}
}