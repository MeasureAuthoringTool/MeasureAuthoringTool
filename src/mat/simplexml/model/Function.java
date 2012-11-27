package mat.simplexml.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mat.shared.model.MeasurementTerm;
import mat.shared.model.StatementTerm.Operator;

public class Function {

	private static Map<String, String> descriptors = new HashMap<String, String>();
		static {
			descriptors.put("ABS","Absolute value");
			descriptors.put("ADDDATE","Add to date");
			descriptors.put("ADDTIME","Add to time");
			descriptors.put("AVG","Average");
			descriptors.put("COUNTDISTINCT","Count distinct values");
			descriptors.put("COUNT","Count");
			descriptors.put("CURDATE","Current date");
			descriptors.put("CURTIME","Current time");
			descriptors.put("DATEDIFF","Difference between dates");
			descriptors.put("DAYOFMONTH","Current day of the month");
			descriptors.put("DAYOFWEEK","Current day of the week");
			descriptors.put("DAYOFYEAR","Current day of the year");
			descriptors.put("HOUR","Current hour of the day");
			descriptors.put("MAX","Maximum");
			descriptors.put("MEDIAN","Median");
			descriptors.put("MIN","Minimum");
			descriptors.put("MINUTE","Current minute of the hour");
			descriptors.put("MONTH","Current month of the year");
			descriptors.put("NOW","Current date and time");
			descriptors.put("POSITION","Current position");
			descriptors.put("ROUND","Round");
			descriptors.put("SEC","Current second of the minute");
			descriptors.put("STDDEV","Standard deviation");
			descriptors.put("SUBDATE","Subtract from date");
			descriptors.put("SUBTIME","Subtract from time");
			descriptors.put("SUM","Sum");
			descriptors.put("TIME","Time");
			descriptors.put("TIMEDIFF","Time difference");
			descriptors.put("VARIANCE","Variance");
			descriptors.put("WEEK","Current week of the month");
			descriptors.put("WEEKDAY","Current day of the week");
			descriptors.put("WEEKOFYEAR","Current week of the year");
			descriptors.put("YEAR","Current year");
			descriptors.put("YEARWEEK","Current year and week");
			descriptors.put("FIRST","First");
			descriptors.put("SECOND","Second");
			descriptors.put("THIRD","Third");
			descriptors.put("FOURTH","Fourth");
			descriptors.put("FIFTH","Fifth");
			descriptors.put("LAST","Last");
			descriptors.put("RELATIVEFIRST","Relative first");
			descriptors.put("RELATIVESECOND","Relative second");
	}
	private String idAttr;
	private String uuid =UUID.randomUUID().toString();
	private String datatype ="derivation expression";
	private String origText;
	protected String name;
	private String lownum;
	private String lowinclusive;
	private String lowunit;
	private String highnum;
	private String highinclusive;	
	private String highunit;
	private String equalnum;
	private String equalunit;
	private String equalnegationind;
	private String property;
	private String ttext;
	private String refid;
	private String value;
	private High high;
	private Low low;

	private Args args = new Args();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getOrigText() {
		return origText;
	}

	public void setOrigText(String origText) {
		this.origText = origText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void diagram(PrettyPrinter pp) {
		pp.concat(name);
		pp.incrementIndentation();
		if (args.getAnd() != null)
			args.getAnd().diagram(pp);
		if (args.getOr() != null)
			args.getOr().diagram(pp);
		if (getLownum() != null)
			pp.concat("LOWNUM", getLownum());
		if (getLowinclusive() != null)
			pp.concat("LOWINCLUSIVE", getLowinclusive());
		if(getLowunit() != null)
			pp.concat("LOWUNIT",getLowunit());
		if (getHighnum() != null)
			pp.concat("HIGHNUM", getHighnum());
		if (getHighinclusive() != null)
			pp.concat("HIGHINCLUSIVE", getHighinclusive());
		if(getHighunit() != null)
			pp.concat("HIGHUNIT",getHighunit());
		if (getQdsel() != null)
			getQdsel().diagram(pp);
		pp.decrementIndentation();
	}

	public Function() {
		
	}
	public Function(String funcName, Operator operator, MeasurementTerm mTerm, Qdsel qdsel, String property) throws Exception {
		this.name = funcName;
		this.origText = descriptors.get(funcName);
		setQdsel(qdsel);
		if (operator != null) {
			switch (operator) {
				case LESS_THAN:	
					setHighnum(mTerm.getQuantity());
					setHighinclusive("false");
					if(isUnitNotEmpty(mTerm)){
						setHighunit(mTerm.getUnit());
					}
					break;
				case LESS_THAN_OR_EQUAL_TO:
					setHighnum(mTerm.getQuantity());
					setHighinclusive("true");
					if(isUnitNotEmpty(mTerm)){
						setHighunit(mTerm.getUnit());
					}
					break;					
				case GREATER_THAN:
					setLownum(mTerm.getQuantity());
					setLowinclusive("false");
					if(isUnitNotEmpty(mTerm)){
						setLowunit(mTerm.getUnit());
					}
					break;
				case GREATER_THAN_OR_EQUAL_TO:
					setLownum(mTerm.getQuantity());
					setLowinclusive("true");
					if(isUnitNotEmpty(mTerm)){
						setLowunit(mTerm.getUnit());
					}
					break;
				case EQUAL_TO:
					setEqualnum(mTerm.getQuantity());
					if(isUnitNotEmpty(mTerm)){
						setEqualunit(mTerm.getUnit());
					}
					break;
				case NOT_EQUAL_TO:
					setEqualnum(mTerm.getQuantity());
					setEqualnegationind("true");
					if(isUnitNotEmpty(mTerm)){
						setEqualunit(mTerm.getUnit());
					}
					break;
			}
		}
	}

	public Qdsel getQdsel() {
		return this.args.getQdsel();
	}

	//<qdsel><id> buster engine
	public void setQdsel(Qdsel qdsel) {
		if (qdsel.getIdAttr() != null) {
			
			//empty qdsel cleaner..
			if (qdsel.getId() != null) {
				qdsel.setId(null);
			}
			
			args.setQdsel(qdsel);
		}
		else {
			Id id = qdsel.getId();
			if (id == null)
				throw new IllegalArgumentException("Count.setQdsel: No ID");
			if (id.getAnd() != null)
				args.setAnd(id.getAnd());
			else
				if (id.getOr() != null)
					args.setOr(id.getOr());
				else {
					List<Qdsel> idQdsel = id.getQdsel();
					if (idQdsel != null && idQdsel.size() == 1) {
						this.args.setQdsel(idQdsel.get(0));
					
						if (this.args.getQdsel().getId() != null) {
							Id deeperId = this.args.getQdsel().getId();

							if (deeperId.getAnd() != null) {
								And and = deeperId.getAnd();
								transferAttributes(this.args.getQdsel(), and);
								args.setAnd(and);
							}

							if (deeperId.getOr() != null) {
								Or or = deeperId.getOr();
								transferAttributes(this.args.getQdsel(), or);
								args.setOr(or);
							}

							//Fields transfer algorythm.
							//this qdsel is not required since the data is really inside 
							//child id of this qdsel.
							//This piece of code may need more enhancements to transfer 
							//additional such fields- Vasant.
							this.args.setQdsel(null);
						}
					}
				}
		}
	}

		private void transferAttributes(Qdsel qdsel, LogicOp op) {
			//need to determine who wins if both qdsel anf op have inclusive, num and unit attributes
			boolean doTransfer = 
				(qdsel.getHighinclusive() != null && qdsel.getHighnum() != null && qdsel.getHighunit() != null) || 
				(qdsel.getLowinclusive() != null && qdsel.getLownum() != null && qdsel.getLowunit() != null);
			if(doTransfer){
				op.setHighinclusive(qdsel.getHighinclusive());
				op.setHighnum(qdsel.getHighnum());
				op.setHighunit(qdsel.getHighunit());
		
				op.setLowinclusive(qdsel.getLowinclusive());
				op.setLownum(qdsel.getLownum());
				op.setLowunit(qdsel.getLowunit());
			}
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

	public void setIdAttr(String idAttr) {
		this.idAttr = idAttr;
	}

	public String getIdAttr() {
		return idAttr;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	public String getRefid() {
		return refid;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setHigh(High high) {
		this.high = high;
	}

	public High getHigh() {
		return high;
	}

	public void setLow(Low low) {
		this.low = low;
	}

	public Low getLow() {
		return low;
	}

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

	public String getEqualnegationind() {
		return equalnegationind;
	}

	public void setEqualnegationind(String equalnegationind) {
		this.equalnegationind = equalnegationind;
	}

	public String getLowunit() {
		return lowunit;
	}

	public void setLowunit(String lowunit) {
		this.lowunit = lowunit;
	}

	public String getHighunit() {
		return highunit;
	}

	public void setHighunit(String highunit) {
		this.highunit = highunit;
	}

	private boolean isUnitNotEmpty(MeasurementTerm mTerm){
	     return mTerm.getUnit()!= null && !mTerm.getUnit().trim().equals("");
	}
	
}