package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XmlConversionlHelper {

	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";
	
	
	
	/**
	 * Creates CellTreeNode object which has list of children objects and a parent object from the XML
	 * @param xml
	 * @return CellTreeNode
	 */ 
	public static CellTreeNode createCellTreeNode(String xml, String tagName){
		Node node = null;
		CellTreeNode parentParent = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		if(xml != null && xml.trim().length() > 0){
			Document document = XMLParser.parse(xml);
			NodeList nodeList = document.getElementsByTagName(tagName);
			if(nodeList.getLength() > 0){
				for (int i = 0; i < nodeList.getLength(); i++) {
					if("measure".equals(nodeList.item(i).getParentNode().getNodeName())){//Gets the node based on tag name passed and the parent is Measure
						node = nodeList.item(i);
					}
				}
			}
			if(node != null){
				parentParent.setName(tagName);
//				createChilds(parentParent, node, childs);
				createCellTreeNodeChilds(parentParent, node, childs);
			}
		}
		if(node == null){
			parentParent.setName(tagName);
			childs.add(createRootNode(tagName));
			parentParent.setChilds(childs);
		}
		return parentParent;
	}
	
	
	private static CellTreeNode createRootNode(String tagName){
		CellTreeNode parent = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		if(tagName.equalsIgnoreCase("populations")){
			parent.setName(tagName);
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.MASTER_ROOT_NODE);
			/*for (int i = 0; i < ClauseConstants.getPopulationsChildren().length; i++) {
				CellTreeNode child = createChild(ClauseConstants.getPopulationsChildren()[i], parent, false, false);
				childs.add(child);
				String key = ClauseConstants.getPopulationsChildren()[i] + "" + 1;				
				List<CellTreeNode> subChilds = new ArrayList<CellTreeNode>();
				subChilds.add(createChild(key, child, false, false));
				child.setChilds(subChilds);
			}*/
			parent.setChilds(childs);
		}else if("measureObservations".equals(tagName)){
			parent.setName(tagName);
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.ROOT_NODE);
			CellTreeNode clauseNode = createChild("measureObservation", "Stratum 1", CellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
			logicalOp.add(createChild("AND", "AND", CellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		}else if("strata".equalsIgnoreCase(tagName)){
			parent.setName(ClauseConstants.get(tagName));
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.ROOT_NODE);
			CellTreeNode clauseNode = createChild("Stratum 1", "Stratum 1", CellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
			logicalOp.add(createChild("AND", "AND", CellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		}
		return parent;
	}
	
	
	private static CellTreeNode createChild(String name, String label, short nodeType, CellTreeNode parent){
		CellTreeNode child = new CellTreeNodeImpl();
		child.setName(name);
		child.setLabel(label);
		child.setParent(parent);
		child.setNodeType(nodeType);
		return child;
	}
	
	/**
	 * Creating all CellTreeNode Child Objects
	 * @param cellTreeNode Parent Object
	 * @param root Xml Node
	 * @param childs List of Childs for @CellTreeNode
	 */
	/*private static void createChilds(CellTreeNode parent, Node root, List<CellTreeNode> childs){
		
		CellTreeNode child = new CellTreeNodeImpl();//child Object
		String name = null;
		
		if(root.getNodeName().equalsIgnoreCase("#text")){//if node is an Value node
			name = root.getNodeValue().replaceAll("\n\r", "").trim();
		}else{//Element node
			name = root.getNodeName();
		}
		if(name.length() > 0){
			setCellTreeNodeValues(root, parent, child, childs);// Create complete child Object with parent and sub Childs
		}

		parent.setChilds(childs);// set parent's childs
		
		NodeList nodes = root.getChildNodes();// get Child nodes for the Processed node and repeat the process
		for(int i = 0; i < nodes.getLength(); i++){
			if(i == 0){
				if(child.getChilds() == null){ 
					childs = new ArrayList<CellTreeNode>();
				}else{
					childs  = child.getChilds();
				}
			}
			Node node = nodes.item(i);
			String nodeName = node.getNodeName().replaceAll("\n\r", "").trim();
			if(!(nodeName.equalsIgnoreCase("#text") && nodeName.isEmpty())){	
				createChilds(child, node, childs);
			}
		}
	}
	*/
	
	private static void createCellTreeNodeChilds(CellTreeNode parent, Node root, List<CellTreeNode> childs){
		String nodeName = root.getNodeName();
		String nodeValue = root.hasAttributes() ? root.getAttributes().getNamedItem("displayName").getNodeValue() : nodeName;
		
		CellTreeNode child = new CellTreeNodeImpl();//child Object
		if(nodeValue.length() > 0){
			setCellTreeNodeValues(root, parent, child, childs);// Create complete child Object with parent and sub Childs
		}

		
		
		parent.setChilds(childs);// set parent's childs
		NodeList nodes = root.getChildNodes();// get Child nodes for the Processed node and repeat the process
		for(int i = 0; i < nodes.getLength(); i++){
			if(i == 0){
				if(child.getChilds() == null){ 
					childs = new ArrayList<CellTreeNode>();
				}else{
					childs  = child.getChilds();
				}
			}
			Node node = nodes.item(i);
			String name = node.getNodeName().replaceAll("\n\r", "").trim();
			if(!(name.equalsIgnoreCase("#text") && name.isEmpty())){	
				createCellTreeNodeChilds(child, node, childs);
			}
		}
		
	}
	
	
	/**
	 * Creating XML from GWT tree using GWT Document object
	 * @return XML String
	 */
	public static String createXmlFromTree(CellTreeNode model){
		Document doc = XMLParser.createDocument();
		if(model != null){
			String returnXml = NAMESPACE_XML + createXmlFromTree(model, doc, null);
			return returnXml;
		}
		
		return null;
	}



	/**
	 * Iterating through the Tree's Children to create the Document Element, Nodes and Attributes.
	 * @param treeItem
	 * @param doc
	 * @param node
	 * @param element
	 * @return
	 */
	private static String createXmlFromTree(CellTreeNode cellTreeNode, Document doc, Node node) {
		Element element = getNodeName(cellTreeNode, doc);
		
		if(node != null){
			node = node.appendChild(element);
		}else{
			node = doc.appendChild(element);
		}
		
		if(cellTreeNode.getChilds() != null && cellTreeNode.getChilds().size() > 0){
			for (CellTreeNode model : cellTreeNode.getChilds()) {
				createXmlFromTree(model, doc, node);
			}
		}
		return doc.toString();
	}
	
	
	
	
	private static void setCellTreeNodeValues(Node node, CellTreeNode parent, CellTreeNode child, List<CellTreeNode> childs){
		String nodeName = node.getNodeName();
		String nodeValue = node.hasAttributes() ? node.getAttributes().getNamedItem("displayName").getNodeValue() : nodeName;
		short cellTreeNodeType = 0;
		
		if(nodeName.equalsIgnoreCase(ClauseConstants.STRATA)){
			cellTreeNodeType =  CellTreeNode.ROOT_NODE;				
		}else if(nodeName.equalsIgnoreCase(ClauseConstants.CLAUSE_TYPE)){
			cellTreeNodeType =  CellTreeNode.CLAUSE_NODE;
		}else if(nodeName.equalsIgnoreCase(ClauseConstants.LOG_OP)){
			cellTreeNodeType = CellTreeNode.LOGICAL_OP_NODE;			
		}
		
		child.setName(nodeValue);//set the name to Child
		child.setLabel(nodeValue);
		child.setNodeType(cellTreeNodeType);		
		child.setParent(parent);// set parent in child
		childs.add(child);// add child to child list
	}
	
	
	private static Element getNodeName(CellTreeNode cellTreeNode, Document document){
		Element element = null;
		switch (cellTreeNode.getNodeType()) {
		case CellTreeNode.ROOT_NODE:
			element = document.createElement(ClauseConstants.get(cellTreeNode.getName()));
			element.setAttribute("displayName", cellTreeNode.getName());
			break;
		case CellTreeNode.CLAUSE_NODE:
			element = document.createElement(ClauseConstants.CLAUSE_TYPE);
			element.setAttribute("displayName", cellTreeNode.getName());
			element.setAttribute("type", cellTreeNode.getName().split(" ")[0]);
			break;
		case CellTreeNode.LOGICAL_OP_NODE:
			element = document.createElement(ClauseConstants.LOG_OP);
			element.setAttribute("displayName", cellTreeNode.getName());
			element.setAttribute("type", cellTreeNode.getName());
			break;
		default:
			element = document.createElement(cellTreeNode.getName());
			break;
		}
		return element;
	}
	
	
	
	private static String splitAndGetLabel(String nodeName, String parentName){
		String clauseName = nodeName.replace(ClauseConstants.getClauseTypeNodeName(parentName), "");
		return ClauseConstants.get(ClauseConstants.getClauseTypeNodeName(parentName)) + " " + clauseName;
	}
}
