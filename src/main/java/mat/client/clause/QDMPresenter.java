package mat.client.clause;


import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.TabObserver;
import mat.client.event.MeasureSelectedEvent;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SpacerWidget;
import mat.shared.ConstantMessages;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * The Class QDMPresenter.
 */
public class QDMPresenter implements MatPresenter, TabObserver{
	private QDSCodeListSearchPresenter codeListSearchPresenter;
	private QDSAppliedListPresenter qdsAppliedListPresenter;
	private SimplePanel QDMContentWidget = new SimplePanel();
	private MatTabLayoutPanel tabLayout;
	private List<MatPresenter> presenterList;
	
	/**
	 * Instantiates a new qDM presenter.
	 */
	public QDMPresenter() {
		QDSCodeListSearchView qdsCodeList = new QDSCodeListSearchView();
		codeListSearchPresenter = new QDSCodeListSearchPresenter(qdsCodeList);
		QDSAppliedListView appliedListView = new QDSAppliedListView();
		qdsAppliedListPresenter = new QDSAppliedListPresenter(appliedListView);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return QDMContentWidget;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void beforeDisplay() {
		presenterList = new LinkedList<MatPresenter>();
		tabLayout = new MatTabLayoutPanel(this);
		tabLayout.getElement().setAttribute("id", "qdmElementTabLayout");
		tabLayout.add(codeListSearchPresenter.getWidget(),"Create Element", true);
		presenterList.add(codeListSearchPresenter);
		tabLayout.add(qdsAppliedListPresenter.getWidget(),"Applied Elements", true);
		presenterList.add(qdsAppliedListPresenter);
		tabLayout.setHeight("98%");
		MatContext.get().tabRegistry.put("Old QDM Elements",tabLayout);
		MatContext.get().enableRegistry.put(tabLayout,this);
		tabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			@SuppressWarnings("rawtypes")
			public void onSelection(final SelectionEvent event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				String newToken = "Old QDM Elements" + index;
				if(!History.getToken().equals(newToken)){
					MeasureSelectedEvent mse = MatContext.get().getCurrentMeasureInfo();
					String msg = " [measure] "+mse.getMeasureName()+" [version] "+mse.getMeasureVersion();
					String mid = mse.getMeasureId();
					MatContext.get().recordTransactionEvent(mid, null, "MEASURE_TAB_EVENT", newToken+msg, ConstantMessages.DB_LOG);
					History.newItem(newToken, false);
				}
			}});
		if(MatContext.get().getCurrentMeasureId() != null && !"".equals(MatContext.get().getCurrentMeasureId())) {
			if(MatContext.get().isCurrentMeasureEditable()){
			    MatContext.get().getMeasureLockService().setMeasureLock();
			}
			FlowPanel fp = new FlowPanel();
			fp.add(new SpacerWidget());
			fp.add(tabLayout);
			QDMContentWidget.clear();
			QDMContentWidget.add(fp);
			MeasureComposerPresenter.setSubSkipEmbeddedLink("subContainerPanel");
			Mat.focusSkipLists("MeasureComposure");
			tabLayout.selectTab(presenterList.indexOf(codeListSearchPresenter));
			codeListSearchPresenter.beforeDisplay();
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		notifyCurrentTabOfClosing();
		tabLayout.updateHeaderSelection(0);
		tabLayout.setSelectedIndex(0);
		
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void updateOnBeforeSelection() {
		MatPresenter presenter = presenterList.get(tabLayout.getSelectedIndex());
		if (presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
		}
	}

	@Override
	public void showUnsavedChangesError() {}

	@Override
	public void notifyCurrentTabOfClosing() {
		MatPresenter oldPresenter = presenterList.get(tabLayout.getSelectedIndex());
		if (oldPresenter != null) {
			MatContext.get().setAriaHidden(oldPresenter.getWidget(), "true");
			oldPresenter.beforeClosingDisplay();
		}	
	}
}
