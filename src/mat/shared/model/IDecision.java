package mat.shared.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface IDecision extends IsSerializable {
	public List<Decision> getDecisions();
	public void setDecisions(List<Decision> decisions);
}
