package mat.client.measure.measuredetails.observers;

import mat.client.measure.measuredetails.views.MeasureDetailViewInterface;
import mat.client.measure.measuredetails.views.ReferencesView;

public class ReferencesObserver implements MeasureDetailsComponentObserver{
	ReferencesView referencesView;
	public ReferencesObserver(ReferencesView referencesView) {
		this.referencesView = referencesView;
	}

	@Override
	public void handleValueChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(MeasureDetailViewInterface view) {
		// TODO Auto-generated method stub
		
	}

	public void onEditClicked(String object) {
		// TODO Auto-generated method stub
		
	}

	public void onDeleteClicked(String object) {
		// TODO Auto-generated method stub
		
	}
}
