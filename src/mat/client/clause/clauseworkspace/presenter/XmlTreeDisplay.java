package mat.client.clause.clauseworkspace.presenter;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.SuccessMessageDisplay;

import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public interface XmlTreeDisplay {
		
		public CellTree getXmlTree();

		public Button getSaveButton();

		public Widget asWidget();
		
		public SuccessMessageDisplay getSuccessMessageDisplay();
		
		public ErrorMessageDisplay getErrorMessageDisplay();
		
		public void clearMessages();
		
		public void setEnabled(boolean enable);
		
		public CellTreeNode getSelectedNode();
		
		public CellTreeNode addNode(String name, String label, short nodeType);

		public void removeNode();
		
		public void copy();
		
		public void paste();
		
		public CellTreeNode getCopiedNode();

		void refreshCellTreeAfterAdding(CellTreeNode selectedNode);
		
		public void editNode(String name, String label);
		
		public void setCopiedNode(CellTreeNode cellTreeNode);
		
		public void setDirty(boolean isDirty);
		
		public boolean isDirty();
		
		public void expandSelected(TreeNode treeNode);
		
		public CellTreeNode addNode(String name, String label, String uuid, short nodeType);
		
		public void editNode(String name, String label, String uuid);

		Button getValidateBtn();

		boolean validateCellTreeNodes(TreeNode treeNode);

		void closeNodes(TreeNode node);

		void openAllNodes(TreeNode treeNode);

		void editNode(boolean isValideNodeValue, CellTreeNode node,
				TreeNode subTree);
		
	}
