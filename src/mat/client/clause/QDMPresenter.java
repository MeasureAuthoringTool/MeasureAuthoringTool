package mat.client.clause;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;

import mat.client.shared.MatContext;

public class QDMPresenter implements MatPresenter{
	private ClauseController clauseController;
	private SimplePanel QDMContentWidget = new SimplePanel();
	
	public QDMPresenter(ClauseController clauseController){
		
		this.setClauseController(clauseController);
		
	}
	
	
	@Override
	public void beforeDisplay() {
		String currentMeasureId = MatContext.get().getCurrentMeasureId();		
		if(currentMeasureId != null && !"".equals(currentMeasureId)) {
			QDMContentWidget.clear();
			clauseController.displaySearch();
			QDMContentWidget.add(clauseController.getWidget());
			
		}	else {
			Mat.hideLoadingMessage();
		}
	}

	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getWidget() {
		return QDMContentWidget;
	}


	/**
	 * @param clauseController the clauseController to set
	 */
	public void setClauseController(ClauseController clauseController) {
		this.clauseController = clauseController;
	}


	/**
	 * @return the clauseController
	 */
	public ClauseController getClauseController() {
		return clauseController;
	}
	

}
