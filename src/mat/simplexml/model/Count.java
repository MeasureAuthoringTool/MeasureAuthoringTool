package mat.simplexml.model;

import java.util.List;

import mat.shared.model.StatementTerm.Operator;

public class Count {
	protected String funcName = "COUNT";
	private Qdsel qdsel;
	private String lownum;
	private String lowinclusive;
	private String highnum;
	private String highinclusive;	
	private And and;
	private Or or;
	private String property;
	private String ttext;

	public void diagram(PrettyPrinter pp) {
		pp.concat(funcName);
		pp.incrementIndentation();
		if (getAnd() != null)
			getAnd().diagram(pp);
		if (getOr() != null)
			getOr().diagram(pp);
		if (getLownum() != null)
			pp.concat("LOWNUM", getLownum());
		if (getLowinclusive() != null)
			pp.concat("LOWINCLUSIVE", getLowinclusive());
		if (getHighnum() != null)
			pp.concat("HIGHNUM", getHighnum());
		if (getHighinclusive() != null)
			pp.concat("HIGHINCLUSIVE", getHighinclusive());
		if (getQdsel() != null)
			getQdsel().diagram(pp);
		pp.decrementIndentation();
	}
	
	public Count(Operator operator, String primitiveTerm, Qdsel qdsel, String property) throws Exception {
		setQdsel(qdsel);
		if (operator != null) {
			switch (operator) {
				case LESS_THAN:	
					setHighnum(primitiveTerm);
					setHighinclusive("false");
					break;
				case LESS_THAN_OR_EQUAL_TO:
					setHighnum(primitiveTerm);
					setHighinclusive("true");
					break;					
				case GREATER_THAN:
					setLownum(primitiveTerm);
					setLowinclusive("false");
					break;
				case GREATER_THAN_OR_EQUAL_TO:
					setLownum(primitiveTerm);
					setLowinclusive("true");
					break;
				case EQUAL_TO:
					setHighnum(primitiveTerm);
					setHighinclusive("false");	
					setLownum(primitiveTerm);
					setLowinclusive("false");				
					break;
				case NOT_EQUAL_TO:
					setHighnum(primitiveTerm);
					setHighinclusive("true");	
					setLownum(primitiveTerm);
					setLowinclusive("true");				
					break;
			}
		}
		if (property != null && property.length() > 0)
			setProperty(property);		
	}

	public Qdsel getQdsel() {
		return qdsel;
	}
	
	public void setQdsel(Qdsel qdsel) {
		if (qdsel.getIdAttr() != null) {

			handleChildQdsel(qdsel);
		}
		else {
			Id id = qdsel.getId();
			if (id == null)
				throw new IllegalArgumentException("Count.setQdsel: No ID");
			if (id.getAnd() != null)
				setAnd(id.getAnd());
			else
				if (id.getOr() != null)
					setOr(id.getOr());
				else {
					List<Qdsel> idQdsel = id.getQdsel();
					if (idQdsel != null && idQdsel.size() == 1) {
						this.qdsel = idQdsel.get(0);
					
						if (this.qdsel.getId() != null) {
							Id deeperId = this.qdsel.getId();

							if (deeperId.getAnd() != null) {
								And and = deeperId.getAnd();
								transferAttributes(this.qdsel, and);
								setAnd(and);
								
							}

							if (deeperId.getOr() != null) {
								Or or = deeperId.getOr();
								transferAttributes(this.qdsel, or);
								setOr(or);
							}
							//Fields transfer algorythm.
							//this qdsel is not required since the data is really inside 
							//child id of this qdsel.
							//This piece of code may need more enhancements to transfer 
							//additional such fields- Vasant.
							this.qdsel = null;
						} else {
							handleChildQdsel(this.qdsel);
							this.qdsel = null;
						}
					}
				}
		}
	}

	private void handleChildQdsel(Qdsel qdsel) {
		if (qdsel.getId() != null) {
			qdsel.setId(null);
		}

		And and = new And();
		and.addQdsel(qdsel);
		setAnd(and);
	}
	
	private void transferAttributes(Qdsel qdsel, LogicOp op) {
		op.setHighinclusive(qdsel.getHighinclusive());
		op.setHighnum(qdsel.getHighnum());
		if(qdsel.getHighunit()!= null && !qdsel.getHighunit().trim().equals("")){
		op.setHighunit(qdsel.getHighunit());
		op.setLowunit(qdsel.getLowunit());
		}
		op.setLowinclusive(qdsel.getLowinclusive());
		op.setLownum(qdsel.getLownum());
		
		
	}

	public String getLowinclusive() {
		return lowinclusive;
	}
	public void setLowinclusive (String lowinclusive ) {
		this.lowinclusive = lowinclusive;
	}
	public String getHighnum() {
		return highnum;
	}
	public void setHighnum(String highnum) {
		this.highnum = highnum;
	}
	public String getHighinclusive() {
		return highinclusive;
	}
	public void setHighinclusive(String highinclusive) {
		this.highinclusive = highinclusive;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}	
	public And getAnd() {
		return and;
	}
	public void setAnd(And and) {
		this.and = and;
	}
	public Or getOr() {
		return or;
	}
	public void setOr (Or or ) {
		this.or = or;
	}
	public String getTtext() {
		return ttext;
	}
	public void setTtext (String ttext ) {
		this.ttext = ttext;
	}
	public String getLownum() {
		return lownum;
	}
	public void setLownum (String lownum ) {
		this.lownum = lownum;
	}
}