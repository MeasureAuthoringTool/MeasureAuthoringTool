package org.ifmc.mat.client.clause;

import java.util.List;

import org.ifmc.mat.model.clause.QDSAttributes;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QDSAttributesServiceAsync {
	void getAllDataTypeAttributes(String dataTypeName, AsyncCallback<List<QDSAttributes>> callback);
	void getAllDataFlowAttributeName(AsyncCallback<List<QDSAttributes>> callback);
}
