package mat.client.clause.clauseworkspace.presenter;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.clause.clauseworkspace.model.TreeModel;
import mat.client.clause.clauseworkspace.presenter.ClauseWorkspacePresenter.XmlTreeDisplay;
import mat.client.clause.clauseworkspace.view.XmlTreeView;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class XmlTreePresenter {
	
	XmlTreeDisplay xmlTreeDisplay;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	private static final String MEASURE = "measure";
	private String rootNode;
	
	public Widget loadXmlTree(MeasureXmlModel result){
		String xml = result != null ? result.getXml() : null;
		XmlTreeView xmlTreeView = new XmlTreeView(XmlConversionlHelper.createTreeModel(xml, rootNode));
		CellTree cellTree = new CellTree(xmlTreeView, null);
		cellTree.setDefaultNodeSize(500);// this will get rid of the show more link on the bottom of the Tree
		xmlTreeView.createPageView(cellTree);
		xmlTreeDisplay = (XmlTreeDisplay) xmlTreeView;
		xmlTreeDisplay.setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
		invokeSaveHandler();
		return xmlTreeView.asWidget();
	}
	
	private MeasureXmlModel createMeasureExportModel(String xml) {
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		exportModal.setToReplaceNode(rootNode);
		exportModal.setParentNode(MEASURE);
		exportModal.setXml(xml);
		return exportModal;
	}

	private void invokeSaveHandler() {
		xmlTreeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				xmlTreeDisplay.clearMessages();
				TreeModel model = (TreeModel) xmlTreeDisplay.getXmlTree().getRootTreeNode().getChildValue(0);
				MeasureXmlModel measureXmlModel = createMeasureExportModel(XmlConversionlHelper.createXmlFromTree(model));
				
				service.saveMeasureXml(measureXmlModel,
					new AsyncCallback<Void>() {
		
						@Override
						public void onFailure(Throwable caught) {
						}
		
						@Override
						public void onSuccess(Void result) {
							xmlTreeDisplay.getSuccessMessageDisplay().setMessage("Changes are successfully saved.");
						}
				});
			}
		});
	}

	/**
	 * @return the rootNode
	 */
	public String getRootNode() {
		return rootNode;
	}

	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(String rootNode) {
		this.rootNode = rootNode;
	}
	
}
