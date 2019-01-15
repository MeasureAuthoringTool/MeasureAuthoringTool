package mat.client.expressionbuilder.observer;

import com.google.gwt.core.shared.GWT;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.modal.ExpressionBuilderModal;
import mat.client.expressionbuilder.modal.RetrieveBuilderModal;
import mat.client.expressionbuilder.model.ExpressionBuilderModel;

public class BuildButtonObserver {
	private ExpressionBuilderModal parent;
	private ExpressionBuilderModel model;
	
	public BuildButtonObserver(ExpressionBuilderModal parent, ExpressionBuilderModel model) {
		this.model = model;
		this.parent = parent;
	}

	public void onBuildButtonClick(String string) {
		if(string.equals(ExpressionType.RETRIEVE.getValue())) {
			ExpressionBuilderModal retrieveModal = new RetrieveBuilderModal(this.parent, this.model);
			retrieveModal.show();
		}
	}
}
