package mat.server.bonnie.api.result;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BonnieMeasureResult implements IsSerializable{
	private Boolean measureExsists;

	public BonnieMeasureResult(Boolean measureExsists) {
		this.measureExsists = measureExsists;
	}
	
	public BonnieMeasureResult() {
		
	}
	
	public Boolean getMeasureExsists() {
		return measureExsists;
	}

	public void setMeasureExsists(Boolean measureExsists) {
		this.measureExsists = measureExsists;
	}
	
}
