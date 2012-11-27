package org.ifmc.mat.client.measure.service;

import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.client.measure.ManageMeasureSearchModel;
import org.ifmc.mat.client.shared.MatException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */	
@RemoteServiceRelativePath("measureCloning")
public interface MeasureCloningService extends RemoteService {
	ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException;
}

