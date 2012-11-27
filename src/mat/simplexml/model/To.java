package mat.simplexml.model;

import java.util.ArrayList;
import java.util.List;

public class To extends FunctionHolder {
	private To to;
	private String toAttr;
	private String id;
	private String highnum;
	private And and;
	private Or or;
	private String ttext;
	private String highunit;
	private String highinclusive;
	private String rel;
	private String property;
	private List<Qdsel> qdsel;

	public To() {
	}
	
	public To(String id) {
		this.id = id;
	}

	public void diagram(PrettyPrinter pp) {
		pp.concat("TO");
		pp.incrementIndentation();
		if (getId() != null)
			pp.concat("ID", getId());
		if (getProperty() != null)
			pp.concat("PROPERTY", getProperty());
		if (getAnd() != null)
			getAnd().diagram(pp);
		if (getOr() != null)
			getOr().diagram(pp);
		diagramFunctions(pp);
		if (getRel() != null)
			pp.concat("REL", getRel());
		int saveIndentation = pp.getIndentation();
		List<Qdsel>qdselList = this.getQdsel();
		if (qdselList != null)
			for (Qdsel qdsel : qdselList) {
				qdsel.diagram(pp);
				pp.setIndentation(saveIndentation);
			}			
		if (getTo() != null)
			getTo().diagram(pp);
		if (getToAttr() != null)
			pp.concat("TO", getToAttr());
		pp.decrementIndentation();
	}
	
	public To getTo() {
		return to;
	}
	public void setTo(To to) {
		this.to = to;
	}
	public String getToAttr() {
		return toAttr;
	}
	public void setToAttr (String toAttr ) {
		this.toAttr = toAttr;
	}
	public String getId() {
		return id;
	}
	public void setId (String id ) {
		this.id = id;
	}
	public String getHighnum() {
		return highnum;
	}
	public void setHighnum (String highnum ) {
		this.highnum = highnum;
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
	public String getHighunit() {
		return highunit;
	}
	public void setHighunit (String highunit ) {
		this.highunit = highunit;
	}
	public String getHighinclusive() {
		return highinclusive;
	}
	public void setHighinclusive (String highinclusive ) {
		this.highinclusive = highinclusive;
	}
	public String getRel() {
		return rel;
	}
	public void setRel (String rel ) {
		this.rel = rel;
	}
	public void setLogicOp(LogicOp logicOp) {
		if (logicOp instanceof And)
			setAnd((And)logicOp);
		else
			setOr((Or)logicOp);
	}	
	public void setAnd(And and) {
		this.and = and;
	}
	public And getAnd() {
		return and;
	}
	public List<Qdsel> getQdsel() {
		return qdsel;
	}
	public void setQdsel(List<Qdsel> qdsel) {
		this.qdsel = qdsel;	
	}
	public void addQdsel(Qdsel q) {
		qdsel = new ArrayList<Qdsel>();
		if(q.getIdAttr() != null ){
			this.qdsel.add(q);
		}
		else{
			if (q.getToAttr() != null || q.getIdAttr() != null ) {
	
				if (q.getTo() != null) {
					q.setTo(null);
				}
				if (q.getId() != null) {
					q.setId(null);
				}
				this.qdsel.add(q);
			}
		}
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	private void setQdsel(Qdsel qdsel) {
		if (qdsel.getToAttr() != null) {
			handleToAttr(qdsel);
		} else {
			handleTo(qdsel);			
		}

		if (qdsel.getIdAttr() != null) {
			handleIdAttr(qdsel);
		} else {
			handleId(qdsel);
		}
	}

	private void handleToAttr(Qdsel qdsel) {
		if (qdsel.getTo() != null) {
			qdsel.setTo(null);
		}
		this.addQdsel(qdsel);
	}

	private void handleIdAttr(Qdsel qdsel) {
		if (qdsel.getId() != null) {
			qdsel.setId(null);
		}
		this.addQdsel(qdsel);
	}

	private void handleTo(Qdsel qdsel) {
		To to = qdsel.getTo();
		if (to == null)
			throw new IllegalArgumentException("Count.setQdsel: No To");
		if (to.getAnd() != null)
			setAnd(to.getAnd());
		else
			if (to.getOr() != null)
				setOr(to.getOr());
			else {
				List<Qdsel> toQdsel = to.getQdsel();
				if (toQdsel != null && toQdsel.size() == 1) {
					Qdsel tempQdsel = toQdsel.get(0);
				
					if (tempQdsel.getTo() != null) {
						To deeperTo = tempQdsel.getTo();

						if (deeperTo.getAnd() != null) {
							And and = deeperTo.getAnd();
							transferAttributes(tempQdsel, and);
							setAnd(and);
							
						}

						if (deeperTo.getOr() != null) {
							Or or = deeperTo.getOr();
							transferAttributes(tempQdsel, or);
							setOr(or);
						}
						//Fields transfer algorythm.
						//this qdsel is not required since the data is really inside 
						//child id of this qdsel.
						//This piece of code may need more enhancements to transfer 
						//additional such fields- Vasant.
						this.qdsel = null;
					}
				}
			}
	}

	private void handleId(Qdsel qdsel) {
		Id id = qdsel.getId();
		if (id == null)
			throw new IllegalArgumentException("To.setQdsel: No Id");
		if (id.getAnd() != null)
			setAnd(id.getAnd());
		else
			if (id.getOr() != null)
				setOr(id.getOr());
			else {
				List<Qdsel> idQdsel = id.getQdsel();
				if (idQdsel != null && idQdsel.size() == 1) {
					Qdsel tempQdsel = idQdsel.get(0);
				
					if (tempQdsel.getId() != null) {
						Id deeperId = tempQdsel.getId();

						if (deeperId.getAnd() != null) {
							And and = deeperId.getAnd();
							transferAttributes(tempQdsel, and);
							setAnd(and);
							
						}

						if (deeperId.getOr() != null) {
							Or or = deeperId.getOr();
							transferAttributes(tempQdsel, or);
							setOr(or);
						}
						//Fields transfer algorythm.
						//this qdsel is not required since the data is really inside 
						//child id of this qdsel.
						//This piece of code may need more enhancements to transfer 
						//additional such fields- Vasant.
						this.qdsel = null;
					}
				}
			}
	}
	
	private void transferAttributes(Qdsel qdsel, LogicOp op) {
		op.setHighinclusive(qdsel.getHighinclusive());
		op.setHighnum(qdsel.getHighnum());
		op.setHighunit(qdsel.getHighunit());

		op.setLowinclusive(qdsel.getLowinclusive());
		op.setLownum(qdsel.getLownum());
		op.setLowunit(qdsel.getLowunit());
		
	}
}