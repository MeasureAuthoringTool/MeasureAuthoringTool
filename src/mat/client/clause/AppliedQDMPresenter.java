package mat.client.clause;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import mat.shared.ConstantMessages;

// TODO: Auto-generated Javadoc
/**
 * The Class AppliedQDMPresenter.
 */
public class AppliedQDMPresenter implements MatPresenter {

	/** The tab layout. */
	private MatTabLayoutPanel tabLayout;

	/** The QDM content widget. */
	private SimplePanel QDMContentWidget = new SimplePanel();
	
	QDMAppliedSelectionPresenter  vsacProfileSelectionPresenter;

	/**
	 * Instantiates a new applied qdm presenter.
	 */
	public AppliedQDMPresenter() {
	 QDMAppliedSelectionView vascProfileSelectionView = new QDMAppliedSelectionView();
     vsacProfileSelectionPresenter = new QDMAppliedSelectionPresenter(vascProfileSelectionView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
//		tabLayout.close();
//		tabLayout.updateHeaderSelection(1);
//		tabLayout.setSelectedIndex(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void beforeDisplay() {
		tabLayout = new MatTabLayoutPanel(true);
		tabLayout.setId("qdmElementTabLayout");
		tabLayout.setHeight("98%");
		MatContext.get().tabRegistry.put("QDM Elements", tabLayout);
		MatContext.get().enableRegistry.put(tabLayout, this);
		tabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
			@SuppressWarnings("rawtypes")
			public void onSelection(final SelectionEvent event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				String newToken = "QDM Elements" + index;
				if (!History.getToken().equals(newToken)) {
					MeasureSelectedEvent mse = MatContext.get()
							.getCurrentMeasureInfo();
					String msg = " [measure] " + mse.getMeasureName()
							+ " [version] " + mse.getMeasureVersion();
					String mid = mse.getMeasureId();
					MatContext.get().recordTransactionEvent(mid, null,
							"MEASURE_TAB_EVENT", newToken + msg,
							ConstantMessages.DB_LOG);
					History.newItem(newToken, false);
				}
			}
		});
		if (MatContext.get().getCurrentMeasureId() != null
				&& !"".equals(MatContext.get().getCurrentMeasureId())) {
			if (MatContext.get().isCurrentMeasureEditable()) {
				MatContext.get().getMeasureLockService().setMeasureLock();
			}
			FlowPanel fp = new FlowPanel();
			fp.add(new SpacerWidget());
			fp.add(tabLayout);
			QDMContentWidget.clear();
			QDMContentWidget.add(fp);
			MeasureComposerPresenter
					.setSubSkipEmbeddedLink("subQDMAPPliedListContainerPanel");
			Mat.focusSkipLists("MeasureComposure");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return QDMContentWidget;
	}

}
