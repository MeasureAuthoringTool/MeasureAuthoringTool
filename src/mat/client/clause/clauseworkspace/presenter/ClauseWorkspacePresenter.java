package mat.client.clause.clauseworkspace.presenter;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.modal.MeasureExportModal;
import mat.client.clause.clauseworkspace.view.XmlTree;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ClauseWorkspacePresenter implements MatPresenter{

	public static interface XmlTreeDisplay{
		public Tree getXmlTree();
		public Button getSaveButton();
		public Widget asWidget();
	}
	
	
	private SimplePanel emptyWidget = new SimplePanel();
	private SimplePanel panel = new SimplePanel();
	private XmlTreeDisplay xmlTreeDisplay;
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	MeasureExportModal measureExportModal;
	
	public ClauseWorkspacePresenter() {
		emptyWidget.add(new Label("No Measure Selected"));
		panel.setStyleName("contentPanel");
		
	}
	
	@Override
	public void beforeDisplay() {
		if(MatContext.get().getCurrentMeasureId() != null && !MatContext.get().getCurrentMeasureId().equals("")) {
			panel.clear();
			service.getMeasureExoportForMeasure(MatContext.get().getCurrentMeasureId(), new AsyncCallback<MeasureExportModal>() {
				
				@Override
				public void onSuccess(MeasureExportModal result) {
					XmlTree xmlTree;
					if(null != result){
						setMeasureExportModal(result);
						xmlTree = new XmlTree(createTreeFromXml(result.getXml()));
					}else{
						setMeasureExportModal(createMeasureExportModal());
						xmlTree = new XmlTree(new Tree());
					}
					panel.add(xmlTree.asWidget());
					xmlTreeDisplay = (XmlTreeDisplay)xmlTree;
					processXmlTreeHandlers();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}else {
			Mat.hideLoadingMessage();
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspaceTree");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	private MeasureExportModal createMeasureExportModal(){
		MeasureExportModal exportModal = new MeasureExportModal();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		return exportModal;
	}
	
	
	private void processXmlTreeHandlers() {
		xmlTreeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				TreeItem treeItem = xmlTreeDisplay.getXmlTree().getSelectedItem();
				String nodetext = null;
				if(treeItem != null){
					if(treeItem.getWidget() != null){
						nodetext = ((Label)treeItem.getWidget()).getText().replaceFirst("\u25ba", "").trim();
						treeItem.setWidget(new Label(nodetext));
					}else{
						nodetext = treeItem.getText().replaceFirst("\u25ba", "").trim();
						treeItem.setText(nodetext);
					}
				}
				
				measureExportModal.setXml(createXmlFromTree().toString());
				service.saveMeasureExport(measureExportModal, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
			}
		});
	}
	
	
	private Tree createTreeFromXml(String xml){
		Tree xmlTree = new Tree();
		Document xmlDocument = XMLParser.parse(xml);
		String name = xmlDocument.getFirstChild().getNodeName();
		if("xml".equalsIgnoreCase(name)){
			name = xmlDocument.getFirstChild().getNextSibling().getNodeName();
		}
			Node root = xmlDocument.getElementsByTagName(name).item(0);
			xmlTree.addItem(getTreeItem(root));
			xmlTree.setTitle("xml tree");	
				
		return xmlTree;
	}
	
	private TreeItem getTreeItem(Node root){
		if(root.getNodeName().equalsIgnoreCase("#text")){
			String val = root.getNodeValue().replaceAll("\n\r", "").trim();
			if(val.length() > 0){
				return new TreeItem(root.getNodeValue());
			}else{
				return null;
			}
		}
		TreeItem ti = new TreeItem(root.getNodeName());
		NodeList nodes = root.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			String name = node.getNodeName().replaceAll("\n\r", "").trim();
			if(!(name.equalsIgnoreCase("#text") && name.isEmpty())){	
				TreeItem item = getTreeItem(node);
				if(null != item){
					ti.addItem(item);
				}
			}
		}
		return ti;
	}
	
	
	
	private StringBuilder createXmlFromTree(){
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>").append("\r\n");
		Tree tree = xmlTreeDisplay.getXmlTree();
		int treeItemCount = tree.getItemCount();
		TreeItem treeItem = null;
		for (int i = 0; i < treeItemCount; i++) {
			treeItem = tree.getItem(i);
			getChildren(treeItem,xmlBuilder,"");
		}
//		System.out.println(xmlBuilder);
		return xmlBuilder;
	}
	
	private void getChildren(TreeItem treeItem, StringBuilder xmlBuilder,String indent){
		String newline = "";
		String nodetext = null;
		if(treeItem.getWidget() != null){
			nodetext = ((Label)treeItem.getWidget()).getText().trim().replaceAll("/n", "");
		}else{
			nodetext = treeItem.getText();
		}
			
		if(treeItem.getChildCount() == 0){
			if(nodetext.trim().length() > 0){
				nodetext = nodetext.replaceAll("&", "&amp;");
				xmlBuilder.append(indent).append(nodetext+newline);
			}
			return;
		}else{
			if(nodetext.trim().length() > 0){
				xmlBuilder.append(indent).append("<"+nodetext+">"+newline);
			}
		}
		for (int i = 0; i < treeItem.getChildCount(); i++) {
			getChildren(treeItem.getChild(i),xmlBuilder,indent+"    ");
		}
		if(nodetext.trim().length() > 0){
			xmlBuilder.append(indent).append("</"+nodetext+">"+newline);
		}
	}
	
	
	
	
	
	@Override 
	public void beforeClosingDisplay() {
		
	}
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	private void displayEmpty(){
		panel.clear();
		panel.add(emptyWidget);
	}

	/**
	 * @return the measureExportModal
	 */
	public MeasureExportModal getMeasureExportModal() {
		return measureExportModal;
	}

	/**
	 * @param measureExportModal the measureExportModal to set
	 */
	public void setMeasureExportModal(MeasureExportModal measureExportModal) {
		this.measureExportModal = measureExportModal;
	}
	

}
