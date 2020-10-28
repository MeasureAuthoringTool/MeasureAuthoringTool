package mat.client.clause.clauseworkspace.presenter;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNodeImpl;
import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.shared.MatContext;
import mat.shared.UUIDUtilClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CQLXmlConversionlHelper {

	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";

	public static CQLCellTreeNode createCQLCellTreeNode(String xml, String tagName) {
		
		CQLCellTreeNode mainNode = new CQLCellTreeNodeImpl();
		if ((xml != null) && (xml.trim().length() > 0)) {
			Document document = XMLParser.parse(xml);
			mainNode = createCQLCellTreeNode(document, tagName);
		}
		return mainNode;
	}

	public static CQLCellTreeNode createCQLCellTreeNode(Document doc, String tagName) {
		Node node = null;
		CQLCellTreeNode mainNode = new CQLCellTreeNodeImpl();
		List<CQLCellTreeNode> childs = new ArrayList<CQLCellTreeNode>();
		
		NodeList nodeList = doc.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			node = nodeList.item(0);
		}
		if (node != null) {
			mainNode.setName(tagName);
			createCQLCellTreeNodeChilds(mainNode, node, childs);
		}
		
		if (node == null) {
			mainNode.setName(tagName);
			childs.add(createRootNode(tagName));
			mainNode.setChilds(childs);
		}
		return mainNode;
	}
	
	public static CQLCellTreeNode createCQLCellTreeNode(Node node, String tagName) {
		
		CQLCellTreeNode mainNode = new CQLCellTreeNodeImpl();
		List<CQLCellTreeNode> childs = new ArrayList<CQLCellTreeNode>();
		
		if (node != null) {
			mainNode.setName(tagName);
			createCQLCellTreeNodeChilds(mainNode, node, childs);
		}
		
		if (node == null) {
			mainNode.setName(tagName);
			childs.add(createRootNode(tagName));
			mainNode.setChilds(childs);
		}
		return mainNode;
	}

	public static CQLCellTreeNode createRootClauseNode() {
		CQLCellTreeNode parent = new CQLCellTreeNodeImpl();
		CQLCellTreeNode mainNode = new CQLCellTreeNodeImpl();
		List<CQLCellTreeNode> childs = new ArrayList<CQLCellTreeNode>();
		List<CQLCellTreeNode> parentchilds = new ArrayList<CQLCellTreeNode>();
		parent.setName(PopulationWorkSpaceConstants.CLAUSE);
		parent.setLabel(PopulationWorkSpaceConstants.CLAUSE);
		parent.setNodeType(CQLCellTreeNode.SUBTREE_ROOT_NODE);
		parent.setChilds(parentchilds);
		parent.setOpen(true);
		childs.add(parent);
		mainNode.setChilds(childs);
		return mainNode;
	}
	
	public static CQLCellTreeNode createOccurenceClauseNode(CQLCellTreeNode clauseNode) {
		CQLCellTreeNode occurrenceNode = new CQLCellTreeNodeImpl();
		List<CQLCellTreeNode> parentchilds = new ArrayList<CQLCellTreeNode>();
		occurrenceNode.setName(clauseNode.getName());
		occurrenceNode.setLabel(clauseNode.getLabel());
		occurrenceNode.setUUID(UUIDUtilClient.uuid());
		occurrenceNode.setNodeType(clauseNode.getNodeType());
		occurrenceNode.setChilds(parentchilds);
		occurrenceNode.setOpen(true);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qdmVariable", "true");
		occurrenceNode.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
		return occurrenceNode;
	}
	

	private static CQLCellTreeNode createRootNode(String tagName) {
		CQLCellTreeNode parent = new CQLCellTreeNodeImpl();
		List<CQLCellTreeNode> childs = new ArrayList<CQLCellTreeNode>();
		if (tagName.equalsIgnoreCase("populations")) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CQLCellTreeNode.MASTER_ROOT_NODE);
			for (int i = 0; i < PopulationWorkSpaceConstants.getPopulationsChildren().length; i++) {
				String nodeValue = PopulationWorkSpaceConstants.getPopulationsChildren()[i];
				//Adding Root nodes under Population.
				CQLCellTreeNode child = createChild(nodeValue, nodeValue, CQLCellTreeNode.ROOT_NODE, parent);
				childs.add(child);
				//Clause Nodes should not have 's' in end. For example 'Numerators' child should be 'Numerator'.
				String name = nodeValue.substring(0, nodeValue.lastIndexOf('s')) + " "  + 1;
				//Adding Clause Nodes
				List<CQLCellTreeNode> subChilds = new ArrayList<CQLCellTreeNode>();
				subChilds.add(createChild(name, name, CQLCellTreeNode.CLAUSE_NODE, child));
				// Adding First 'AND' under clause node.
				for (int j = 0; j < subChilds.size(); j++) {
					List<CQLCellTreeNode> logicalOp = new ArrayList<CQLCellTreeNode>();
					logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
							CQLCellTreeNode.LOGICAL_OP_NODE, subChilds.get(j)));
					subChilds.get(j).setChilds(logicalOp);
				}
				child.setChilds(subChilds);
			}
			parent.setChilds(childs);
		} else if ("measureObservations".equals(tagName)) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CQLCellTreeNode.ROOT_NODE);
			CQLCellTreeNode clauseNode = createChild("Measure Observation 1",
					"Measure Observation 1", CQLCellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CQLCellTreeNode> logicalOp = new ArrayList<CQLCellTreeNode>();
			logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
					CQLCellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		} else if ("strata".equalsIgnoreCase(tagName)) {
			parent.setName(PopulationWorkSpaceConstants.get(tagName));
			parent.setLabel(PopulationWorkSpaceConstants.get(tagName));
			parent.setNodeType(CQLCellTreeNode.MASTER_ROOT_NODE);
			
			CQLCellTreeNode child = createChild("Stratification 1", "Stratification 1", CQLCellTreeNode.ROOT_NODE, parent);
			child.setUUID(UUIDUtilClient.uuid());
			childs.add(child);
			parent.setChilds(childs);
			
			List<CQLCellTreeNode> clauseNode = new ArrayList<CQLCellTreeNode>();
			clauseNode.add(createChild("Stratum 1", "Stratum 1", CQLCellTreeNode.CLAUSE_NODE, child));
			clauseNode.get(0).setUUID(UUIDUtilClient.uuid());
			child.setChilds(clauseNode);
			
			for (int j = 0; j < clauseNode.size(); j++) {
				List<CQLCellTreeNode> logicalOp = new ArrayList<CQLCellTreeNode>();
				logicalOp.add(createChild(PopulationWorkSpaceConstants.AND, PopulationWorkSpaceConstants.AND,
						CQLCellTreeNode.LOGICAL_OP_NODE, clauseNode.get(j)));
				clauseNode.get(j).setChilds(logicalOp);
			}
			
			
		}
		return parent;
	}
	
	private static CQLCellTreeNode createChild(String name, String label, short nodeType, CQLCellTreeNode parent) {
		CQLCellTreeNode child = new CQLCellTreeNodeImpl();
		child.setName(name);
		child.setLabel(label);
		child.setParent(parent);
		child.setNodeType(nodeType);
		return child;
	}

	@SuppressWarnings("unchecked")
	private static void createCQLCellTreeNodeChilds(CQLCellTreeNode parent, Node root, List<CQLCellTreeNode> childs) {
		String nodeName = root.getNodeName();
		String nodeValue = root.hasAttributes()
				? root.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue() : nodeName;
				
				CQLCellTreeNode child = new CQLCellTreeNodeImpl(); //child Object
				if (nodeValue.length() > 0) {
					setCQLCellTreeNodeValues(root, parent, child, childs); // Create complete child Object with parent and sub Childs
				}
				
				parent.setChilds(childs); // set parent's childs
				NodeList nodes = root.getChildNodes(); // get Child nodes for the Processed node and repeat the process
				for (int i = 0; i < nodes.getLength(); i++) {
					if (i == 0) {
						if (child.getChilds() == null) {
							childs = new ArrayList<CQLCellTreeNode>();
						} else {
							childs  = child.getChilds();
						}
					}
					Node node = nodes.item(i);
					String name = node.getNodeName().replaceAll("\n\r", "").trim();
					//if(!(name.equalsIgnoreCase("#text") && name.isEmpty())){
					if ((name.length() > 0) && !name.equalsIgnoreCase("#text") && !name.equalsIgnoreCase("attribute")
							&& !name.equalsIgnoreCase("comment")) {
						createCQLCellTreeNodeChilds(child, node, childs);
					}
					/**
					 * This part for QDM node attributes. The attribute XML node is not to be displayed on the CellTree.
					 */
					else if (name.equalsIgnoreCase("attribute")) {
						Object attributes = child.getExtraInformation("attributes");
						if (attributes == null) {
							attributes = new ArrayList<CQLCellTreeNode>();
							child.setExtraInformation("attributes", attributes);
						}
						List<CQLCellTreeNode> attributeList = (List<CQLCellTreeNode>) attributes;
						CQLCellTreeNode cellNode = new CQLCellTreeNodeImpl();
						NamedNodeMap attNodeMap = node.getAttributes();
						for (int j = 0; j < attNodeMap.getLength(); j++) {
							Node attrib = attNodeMap.item(j);
							cellNode.setExtraInformation(attrib.getNodeName(), attrib.getNodeValue());
						}
						attributeList.add(cellNode);
					} else if (name.equalsIgnoreCase(PopulationWorkSpaceConstants.COMMENTS)) {
						Object comments = child.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
						if (comments == null) {
							comments = new ArrayList<CQLCellTreeNode>();
							child.setExtraInformation(PopulationWorkSpaceConstants.COMMENTS, comments);
						}
						List<CQLCellTreeNode> commentList = (List<CQLCellTreeNode>) comments;
						CQLCellTreeNode cellNode = new CQLCellTreeNodeImpl();
						if (node.hasChildNodes()) {
							cellNode.setNodeText(node.getChildNodes().item(0).getNodeValue());
						} else {
							cellNode.setNodeText(StringUtils.EMPTY);
						}
						cellNode.setNodeType(CQLCellTreeNode.COMMENT_NODE);
						cellNode.setName(PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						commentList.add(cellNode);
					}
				}
	}
	

	public static String createXmlFromTree(CQLCellTreeNode model) {
		Document doc = XMLParser.createDocument();
		if (model != null) {
			String returnXml = NAMESPACE_XML + createXmlFromTree(model, doc, null);
			return returnXml;
		}
		
		return null;
	}

	private static String createXmlFromTree(CQLCellTreeNode CQLCellTreeNode, Document doc, Node node) {
		Element element = getNodeName(CQLCellTreeNode, doc);
		
		if (node != null) {
			node = node.appendChild(element);
		} else {
			node = doc.appendChild(element);
		}
		
		if ((CQLCellTreeNode.getChilds() != null) && (CQLCellTreeNode.getChilds().size() > 0)) {
			for (CQLCellTreeNode model : CQLCellTreeNode.getChilds()) {
				createXmlFromTree(model, doc, node);
			}
		}
		return doc.toString();
	}
	

	private static void setCQLCellTreeNodeValues(Node node, CQLCellTreeNode parent, CQLCellTreeNode child, List<CQLCellTreeNode> childs) {
		String nodeName = node.getNodeName();
		String nodeValue = node.hasAttributes()
				? node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.DISPLAY_NAME).getNodeValue() : nodeName;
				short CQLCellTreeNodeType = 0;
				String uuid = "";
				if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.MASTER_ROOT_NODE_POPULATION)||
						nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.MASTER_ROOT_NODE_STRATA) ) {
					CQLCellTreeNodeType =  CQLCellTreeNode.MASTER_ROOT_NODE;
				} else if (PopulationWorkSpaceConstants.ROOT_NODES.contains(nodeName)) {
					CQLCellTreeNodeType =  CQLCellTreeNode.ROOT_NODE;
					
					if((node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID) != null) &&
							(node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue() != null) )
					{
						uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
					}
					
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CLAUSE_TYPE)) {
					CQLCellTreeNodeType =  CQLCellTreeNode.CLAUSE_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_DEFINITION_TYPE)) {
					CQLCellTreeNodeType =  CQLCellTreeNode.CQL_DEFINITION_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				}else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_FUNCTION_TYPE)) {
					CQLCellTreeNodeType =  CQLCellTreeNode.CQL_FUNCTION_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.CQL_AGG_FUNCTION_TYPE)){
					CQLCellTreeNodeType =  CQLCellTreeNode.CQL_AGG_FUNCTION_NODE;
				}
				else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.LOG_OP)) {
					CQLCellTreeNodeType = CQLCellTreeNode.LOGICAL_OP_NODE;
				}  else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SET_OP)) {
					CQLCellTreeNodeType = CQLCellTreeNode.SET_OP_NODE;
				} else if(nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SUBTREE_NAME)) {
					CQLCellTreeNodeType = CQLCellTreeNode.SUBTREE_NODE;
					uuid = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.UUID).getNodeValue();
					HashMap<String, String> map = new HashMap<String, String>();
					if (node.hasAttributes()) {
						NamedNodeMap nodeMap = node.getAttributes();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							if(key.equalsIgnoreCase("qdmVariable")) {
								String value = nodeMap.item(i).getNodeValue();
								map.put(key, value);
							}
						}
					}
					child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
				}
				else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.RELATIONAL_OP)) {
					String type = node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.TYPE).getNodeValue();
					String longName = MatContext.get().operatorMapKeyShort.get(type);
					if (MatContext.get().timings.contains(longName)) {
						CQLCellTreeNodeType = CQLCellTreeNode.TIMING_NODE;
						NamedNodeMap nodeMap = node.getAttributes();
						HashMap<String, String> map = new HashMap<String, String>();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							String value = nodeMap.item(i).getNodeValue();
							map.put(key, value);
						}
						child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
					} else {
						CQLCellTreeNodeType = CQLCellTreeNode.RELATIONSHIP_NODE;
					}
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.ELEMENT_REF)) {
					CQLCellTreeNodeType = CQLCellTreeNode.ELEMENT_REF_NODE;
					uuid =  node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.ID).getNodeValue();
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.SUBTREE_REF)) {
					CQLCellTreeNodeType = CQLCellTreeNode.SUBTREE_REF_NODE;
					uuid =  node.getAttributes().getNamedItem(PopulationWorkSpaceConstants.ID).getNodeValue();
					//in case if SubTree with uuid has been changed.
					nodeValue = PopulationWorkSpaceConstants.subTreeLookUpName.get(uuid);
				} else if (nodeName.equalsIgnoreCase(PopulationWorkSpaceConstants.FUNC_NAME)) {
					CQLCellTreeNodeType = CQLCellTreeNode.FUNCTIONS_NODE;
					HashMap<String, String> map = new HashMap<String, String>();
					if (node.hasAttributes()) {
						NamedNodeMap nodeMap = node.getAttributes();
						for (int i = 0; i < nodeMap.getLength(); i++) {
							String key = nodeMap.item(i).getNodeName();
							String value = nodeMap.item(i).getNodeValue();
							map.put(key, value);
						}
					}
					child.setExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES, map);
				}
				child.setName(nodeValue); //set the name to Child
				child.setLabel(nodeValue);
				child.setNodeType(CQLCellTreeNodeType);
				child.setParent(parent); // set parent in child
				child.setUUID(uuid);
				childs.add(child); // add child to child list
	}

	@SuppressWarnings("unchecked")
	private static Element getNodeName(CQLCellTreeNode CQLCellTreeNode, Document document) {
		Element element = null;
		System.out.println("Node name:"+CQLCellTreeNode.getName());
		System.out.println("Node type:"+CQLCellTreeNode.getNodeType());
		System.out.println();
		switch (CQLCellTreeNode.getNodeType()) {
			case CellTreeNode.MASTER_ROOT_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.get(CQLCellTreeNode.getName()));
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				break;
			case CellTreeNode.ROOT_NODE:
				
				if(CQLCellTreeNode.getName().contains("Stratification"))
				{
					
					element = document.createElement(CQLCellTreeNode.getName().substring(0, CQLCellTreeNode.getName().lastIndexOf(" ")).replaceAll("S", "s"));
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
					element.setAttribute(PopulationWorkSpaceConstants.UUID, CQLCellTreeNode.getUUID());
					
				}
				else
				{
					element = document.createElement(PopulationWorkSpaceConstants.get(CQLCellTreeNode.getName()));
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				}
				
				break;
			case CellTreeNode.CQL_DEFINITION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_DEFINITION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());				
				element.setAttribute(PopulationWorkSpaceConstants.UUID, CQLCellTreeNode.getUUID());
				break;
			case CellTreeNode.CQL_FUNCTION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_FUNCTION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());				
				element.setAttribute(PopulationWorkSpaceConstants.UUID, CQLCellTreeNode.getUUID());
				break;
			case CellTreeNode.CQL_AGG_FUNCTION_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CQL_AGG_FUNCTION_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());				
				break;
			case CellTreeNode.CLAUSE_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.CLAUSE_TYPE);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE,
						toCamelCase(CQLCellTreeNode.getName().substring(0, CQLCellTreeNode.getName().lastIndexOf(" "))));
				element.setAttribute(PopulationWorkSpaceConstants.UUID, CQLCellTreeNode.getUUID());
				break;
			case CellTreeNode.LOGICAL_OP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.LOG_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, toCamelCase(CQLCellTreeNode.getName()));
				List<CQLCellTreeNode> commentList = (List<CQLCellTreeNode>) CQLCellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
				if (commentList != null) {
					for (CQLCellTreeNode commentNode:commentList) {
						Element commentElement = document.createElement(
								PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						Text commentNodeText = document.createTextNode(commentNode.getNodeText());
						commentElement.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
								, commentNode.getName());
						commentElement.setAttribute(PopulationWorkSpaceConstants.TYPE, commentNode.getName());
						commentElement.appendChild(commentNodeText);
						element.appendChild(commentElement);
						
					}
				}
				break;
			case CellTreeNode.SET_OP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SET_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, capWords(CQLCellTreeNode.getName(), null));
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, toCamelCase(CQLCellTreeNode.getName()));
				break;
			case CellTreeNode.TIMING_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.RELATIONAL_OP);
				HashMap<String, String> map = (HashMap<String, String>) CQLCellTreeNode.getExtraInformation(
						PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				if (map != null) {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME,
							map.get(PopulationWorkSpaceConstants.DISPLAY_NAME));
					String typeValue = map.get(PopulationWorkSpaceConstants.TYPE);
					element.setAttribute(PopulationWorkSpaceConstants.TYPE, typeValue);
					if (map.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						element.setAttribute(PopulationWorkSpaceConstants.OPERATOR_TYPE,
								map.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					if (map.containsKey(PopulationWorkSpaceConstants.QUANTITY)) {
						element.setAttribute(PopulationWorkSpaceConstants.QUANTITY,
								map.get(PopulationWorkSpaceConstants.QUANTITY));
					}
					if (map.containsKey(PopulationWorkSpaceConstants.UNIT)) {
						element.setAttribute(PopulationWorkSpaceConstants.UNIT,
								map.get(PopulationWorkSpaceConstants.UNIT));
					}
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(CQLCellTreeNode.getName()));
				}
				break;
			case CellTreeNode.RELATIONSHIP_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.RELATIONAL_OP);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				if(MatContext.get().relationships.contains(CQLCellTreeNode.getName())){
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(CQLCellTreeNode.getName()));
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().removedRelationshipTypes.get(CQLCellTreeNode.getName()));
				}
				break;
			case CellTreeNode.ELEMENT_REF_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.ELEMENT_REF);
				//			Node idNode = ClauseConstants.getElementLookUps()
				//.get(CQLCellTreeNode.getName()).getAttributes().getNamedItem("uuid");
				element.setAttribute(PopulationWorkSpaceConstants.ID, CQLCellTreeNode.getUUID()); // TBD if we need this
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, "qdm"); //this can change
				
				List<CQLCellTreeNode> attributeList = (List<CQLCellTreeNode>) CQLCellTreeNode.getExtraInformation("attributes");
				if (attributeList != null) {
					for (CQLCellTreeNode attribNode:attributeList) {
						Element attribElement = document.createElement(PopulationWorkSpaceConstants.ATTRIBUTE);
						for (String name:((CQLCellTreeNodeImpl) attribNode).getExtraInformationMap().keySet()) {
							attribElement.setAttribute(name, (String) attribNode.getExtraInformation(name));
						}
						element.appendChild(attribElement);
					}
				}
				break;
			case CellTreeNode.FUNCTIONS_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.FUNC_NAME);
				
				HashMap<String, String> functionMap = (HashMap<String, String>) CQLCellTreeNode.getExtraInformation(
						PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
				if (functionMap != null) {
					String operator = null;
					if (functionMap.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						operator = MatContext.get().operatorMapKeyLong.get(
								functionMap.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME,
							capWords(functionMap.get(PopulationWorkSpaceConstants.DISPLAY_NAME), operator));
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							functionMap.get(PopulationWorkSpaceConstants.TYPE));
					if (functionMap.containsKey(PopulationWorkSpaceConstants.OPERATOR_TYPE)) {
						element.setAttribute(PopulationWorkSpaceConstants.OPERATOR_TYPE,
								functionMap.get(PopulationWorkSpaceConstants.OPERATOR_TYPE));
					}
					if (functionMap.containsKey(PopulationWorkSpaceConstants.QUANTITY)) {
						element.setAttribute(PopulationWorkSpaceConstants.QUANTITY,
								functionMap.get(PopulationWorkSpaceConstants.QUANTITY));
					}
					if (functionMap.containsKey(PopulationWorkSpaceConstants.UNIT)) {
						element.setAttribute(PopulationWorkSpaceConstants.UNIT,
								functionMap.get(PopulationWorkSpaceConstants.UNIT));
					}
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
							, capWords(CQLCellTreeNode.getName(), null));
					element.setAttribute(PopulationWorkSpaceConstants.TYPE,
							MatContext.get().operatorMapKeyLong.get(CQLCellTreeNode.getName()));
				}
				break;
			case CellTreeNode.SUBTREE_ROOT_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_ROOT_NAME);
				break;
			case CellTreeNode.SUBTREE_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_NAME);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.UUID, CQLCellTreeNode.getUUID());
				if (CQLCellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES) != null) {
					HashMap <String, String> qdmVariableMap = (HashMap<String, String>)
							CQLCellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.EXTRA_ATTRIBUTES);
					element.setAttribute(PopulationWorkSpaceConstants.CLAUSE_QDM_VARIABLE,
							qdmVariableMap.get("qdmVariable"));
				} else {
					element.setAttribute(PopulationWorkSpaceConstants.CLAUSE_QDM_VARIABLE, "false");
				}
				break;
			case CellTreeNode.SUBTREE_REF_NODE:
				element = document.createElement(PopulationWorkSpaceConstants.SUBTREE_REF);
				element.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME, CQLCellTreeNode.getName());
				element.setAttribute(PopulationWorkSpaceConstants.ID, CQLCellTreeNode.getUUID());
				element.setAttribute(PopulationWorkSpaceConstants.TYPE, "subTree");
				List<CQLCellTreeNode> commentInSubTreeList = (List<CQLCellTreeNode>)
						CQLCellTreeNode.getExtraInformation(PopulationWorkSpaceConstants.COMMENTS);
				if (commentInSubTreeList != null) {
					for (CQLCellTreeNode commentNode:commentInSubTreeList) {
						Element commentElement = document.createElement(
								PopulationWorkSpaceConstants.COMMENT_NODE_NAME);
						Text commentNodeText = document.createTextNode(commentNode.getNodeText());
						commentElement.setAttribute(PopulationWorkSpaceConstants.DISPLAY_NAME
								, commentNode.getName());
						commentElement.setAttribute(PopulationWorkSpaceConstants.TYPE, commentNode.getName());
						commentElement.appendChild(commentNodeText);
						element.appendChild(commentElement);
					}
				}
				break;
			default:
				element = document.createElement(CQLCellTreeNode.getName());
				break;
		}
		return element;
	}
	
	private static String toCamelCase(String name) {
		name = name.toLowerCase();
		String[] parts = name.split(" ");
		String camelCaseString = parts[0].substring(0, 1).toLowerCase() + parts[0].substring(1);
		for (int i = 1; i < parts.length; i++) {
			camelCaseString = camelCaseString + toProperCase(parts[i]);
		}
		return camelCaseString;
	}
	
	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase()
				+ s.substring(1).toLowerCase();
	}

	private static String capWords(String strToConvert , String operator) {
		if ((strToConvert == null)) {
			return strToConvert;
		} else {
			StringBuilder sb = new StringBuilder();
			if (operator != null) {
				if (strToConvert.contains(operator)) {
					String[] newString = strToConvert.split(operator);
					// For functional Ops, Function name is always contains in index 0.
					//This will not work for RelOp/Timing.
					String token = newString[0];
					for (String tokenSpace : token.split(" ")) {
						if (tokenSpace.isEmpty()) {
							if (sb.length() > 0) {
								sb.append(" ");
							}
						} else {
							if (sb.length() > 0) {
								sb.append(" ");
							}
							sb.append(Character.toUpperCase(tokenSpace.charAt(0)));
							if (tokenSpace.length() > 1) {
								sb.append(tokenSpace.substring(1).toLowerCase());
							}
						}
					}
				}
				int indexOfOperator = strToConvert.indexOf(operator);
				String subString = strToConvert.substring(indexOfOperator);
				sb.append(subString);
			} else {
				for (String token : strToConvert.split(" ")) {
					if (token.isEmpty()) {
						if (sb.length() > 0) {
							sb.append(" ");
						}
					} else {
						if (sb.length() > 0) {
							sb.append(" ");
						}
						sb.append(Character.toUpperCase(token.charAt(0)));
						if (token.length() > 1) {
							sb.append(token.substring(1).toLowerCase());
						}
					}
				}
			}
			return sb.toString();
		}
	}
}
