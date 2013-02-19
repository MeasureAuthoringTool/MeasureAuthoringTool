package mat.client.clause.clauseworkspace.presenter;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.modal.MeasureExportModal;
import mat.client.clause.clauseworkspace.view.XmlTree;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplay;

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
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ClauseWorkspacePresenter implements MatPresenter {

	public static interface XmlTreeDisplay {
		public Tree getXmlTree();

		public Button getSaveButton();

		public Widget asWidget();
		
		public SuccessMessageDisplay getSuccessMessageDisplay();
		
		public ErrorMessageDisplay getErrorMessageDisplay();
		
		public void clearMessages();
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
		if (MatContext.get().getCurrentMeasureId() != null
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			panel.clear();
			service.getMeasureExoportForMeasure(MatContext.get()
					.getCurrentMeasureId(),
					new AsyncCallback<MeasureExportModal>() {// Loading the measure's SimpleXML from the Measure_XML table 

						@Override
						public void onSuccess(MeasureExportModal result) {
							XmlTree xmlTree;
							if (null != result && result.getXml().length() > 0) {// if xml not null
								setMeasureExportModal(result);
								xmlTree = new XmlTree(createTreeFromXml(result
										.getXml()));
								Tree tree = xmlTree.getTree();
								tree.setSelectedItem(tree.getItemCount() > 0 ? tree
										.getItem(0) : null);
							} else {
								setMeasureExportModal(createMeasureExportModal()); 
								xmlTree = new XmlTree(new Tree());
							}

							panel.add(xmlTree.asWidget());
							xmlTreeDisplay = (XmlTreeDisplay) xmlTree;
							processXmlTreeHandlers();
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});

		} else {
			Mat.hideLoadingMessage();
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("ClauseWorkspaceTree");
		Mat.focusSkipLists("MeasureComposer");
	}

	private MeasureExportModal createMeasureExportModal() {
		MeasureExportModal exportModal = new MeasureExportModal();
		exportModal.setMeasureId(MatContext.get().getCurrentMeasureId());
		return exportModal;
	}

	private void processXmlTreeHandlers() {
		xmlTreeDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				xmlTreeDisplay.clearMessages();
				Tree tree = xmlTreeDisplay.getXmlTree();
				if (tree != null && tree.getItemCount() > 0) {
					TreeItem treeItem = tree.getSelectedItem();
					treeItem.setSelected(false);
					String nodetext = null;
						nodetext = treeItem.getText()
								.replaceFirst("\u25ba", "").trim();
						treeItem.setText(nodetext);
				}
				
//				try{
					measureExportModal.setXml(createXmlFromTree());	
//				}catch (Exception e) {
//					System.out.println(e.getCause().getMessage().contains("InvalidCharacterError"));
//					xmlTreeDisplay.getErrorMessageDisplay().setMessage("Invalid Character added");
//				}
				
				
				service.saveMeasureExport(measureExportModal,
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
	 * Method creates GWT Tree from XML 
	 * @param xml
	 * @return Tree Widget
	 */
	private Tree createTreeFromXml(String xml) {
		Tree xmlTree = new Tree();
		Document xmlDocument = XMLParser.parse(xml);
		String name = xmlDocument.getFirstChild().getNodeName();
		if ("xml".equalsIgnoreCase(name)) {
			name = xmlDocument.getFirstChild().getNextSibling().getNodeName();
		}
		Node root = xmlDocument.getElementsByTagName(name).item(0);
		xmlTree.addItem(getTreeItem(root));
		xmlTree.setTitle("xml tree");

		return xmlTree;
	}
	

	/**
	 * Iterating through DOM object to create the GWT Tree
	 * @param root
	 * @return
	 */
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
		NamedNodeMap namedNodeMap = root.getAttributes();
		if(namedNodeMap != null && namedNodeMap.getLength() > 0){
			  TreeItem item = ti.addItem("attributes");
			for (int i = 0; i < namedNodeMap.getLength(); i++) {
				item.addItem(namedNodeMap.item(i).getNodeName()+" = "+namedNodeMap.item(i).getNodeValue());
			}
		}
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
	
	/**
	 * Creating XML from GWT tree using GWT Document object
	 * @return XML String
	 */
	private String createXmlFromTree(){
		Document doc = XMLParser.createDocument();
		String xml = null;
		Tree tree = xmlTreeDisplay.getXmlTree();
		if(tree != null){
			for (int i = 0; i < tree.getItemCount(); i++) {
				xml = createXmlFromTree(xmlTreeDisplay.getXmlTree().getItem(i), doc, null, null);
				xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n" + xml;
			}
		}
		return xml;
	}



	/**
	 * Iterating through the Tree's Children to create the Document Element, Nodes and Attributes.
	 * @param treeItem
	 * @param doc
	 * @param node
	 * @param element
	 * @return
	 */
	private String createXmlFromTree(TreeItem treeItem, Document doc, Node node,
			Element element) {
		String nodetext = "";
		
		nodetext = treeItem.getText().trim().replaceAll("/n", "");

		if (null == element) {//First Node creation. Executed for the First time
			element = doc.createElement(nodetext);
			node = doc.appendChild(element);
		} else if(nodetext.length() > 0){// if nodeText is not null
			if (nodetext.equalsIgnoreCase("attributes")) {// if Attributes
				int attrCnt = treeItem.getChildCount();
				for (int i = 0; i < attrCnt; i++) {
					String[] attrPair = treeItem.getChild(i).getText()
							.split("=");// concat with "="
					element.setAttribute(attrPair[0].trim(), attrPair[1].trim());// set it to the element as Attribute.
				}
			} else if (treeItem.getParentItem() == null	|| !treeItem.getParentItem().getText()
							.equalsIgnoreCase("attributes")) {		// if not atrributes 		 
				if (treeItem.getChildCount() == 0) {// if child count is 0 then consider this as XML value text, else it will be node.
					node.appendChild(doc.createTextNode(nodetext));
				} else {
					element = doc.createElement(nodetext);
					node = node.appendChild(element);
				}
			}
		}

		for (int i = 0; i < treeItem.getChildCount(); i++) {
			createXmlFromTree(treeItem.getChild(i), doc, node, element);
		}
		return doc.toString();
	}

	@Override
	public void beforeClosingDisplay() {

	}

	@Override
	public Widget getWidget() {
		return panel;
	}

	private void displayEmpty() {
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
	 * @param measureExportModal
	 *            the measureExportModal to set
	 */
	public void setMeasureExportModal(MeasureExportModal measureExportModal) {
		this.measureExportModal = measureExportModal;
	}

}
