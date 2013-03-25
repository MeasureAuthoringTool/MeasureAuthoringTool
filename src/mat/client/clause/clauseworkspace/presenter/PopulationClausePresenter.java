package mat.client.clause.clauseworkspace.presenter;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopulationClausePresenter extends XmlTreePresenter implements MatPresenter{
	
	FlowPanel panel = new FlowPanel();
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	private static final String POPULATIONS_TAG = "populations";
	
	public PopulationClausePresenter() {
		setRootNode(POPULATIONS_TAG);
	}

	@Override
	public void beforeDisplay() {
		panel.clear();
		if (MatContext.get().getCurrentMeasureId() != null
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			
			service.getMeasureXmlForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureXmlModel>() {// Loading the measure's SimpleXML from the Measure_XML table 

						@Override
						public void onSuccess(MeasureXmlModel result) {
							panel.clear();
							panel.add(loadXmlTree(result));
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}
					});
		} else {
			Mat.hideLoadingMessage();
		}
		
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspaceTree");
		Mat.focusSkipLists("MeasureComposer");
		
	}

	
	@Override
	public void beforeClosingDisplay() {
		
	}

	@Override
	public Widget getWidget() {
		return panel;
	}


}
