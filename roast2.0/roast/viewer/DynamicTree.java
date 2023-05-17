package roast.viewer;

import roast.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

/**
* PENDING
*/
public class DynamicTree extends JTree {

protected DefaultMutableTreeNode rootNode;
protected DefaultMutableTreeNode lastNode;
protected DefaultTreeModel treeModel;
private Toolkit toolkit = Toolkit.getDefaultToolkit();

/**
* The maximum message level currently stored in the tree.
*/
public int maxLevel = 0;

/**
* Construct a modifiable JTree.
*/
public DynamicTree() 
{
	rootNode = new DefaultMutableTreeNode("Log Messages");
	lastNode = rootNode;
	treeModel = new DefaultTreeModel(rootNode);

	DefaultTreeCellRenderer renderer = new MyRenderer();
	renderer.setClosedIcon(null);
	renderer.setOpenIcon(null);
	renderer.setLeafIcon(null);

	setModel(treeModel);
	setEditable(false);
	setBorder(BorderFactory.createLineBorder(Color.black));
	getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
	setRootVisible(true);
	setShowsRootHandles(true);
	setCellRenderer(renderer);

}

/** 
* Remove all nodes except the root node. 
*/
void clear() 
{
	rootNode.removeAllChildren();
	treeModel.reload();
}

DefaultMutableTreeNode addLogMessage(LogMessage child) 
{
	DefaultMutableTreeNode parentNode = rootNode;
	if (child instanceof UtilityMessage) {
		UtilityMessage uChild = (UtilityMessage)child;
		//insert level nodes
		for (int i = 0; i < uChild.getLevel(); i++) {
			if (parentNode.getChildCount() > 0) { 
				parentNode = (DefaultMutableTreeNode)
					parentNode.getLastChild();
			} else {
				parentNode = addObject(parentNode,"Level "+i);
			}
		}
		if (uChild.getLevel() > maxLevel) {
			maxLevel = uChild.getLevel();
		} 
	} else { // child is a FailureMessage
		while (parentNode.getChildCount() > 0) {
			parentNode = (DefaultMutableTreeNode)
				parentNode.getLastChild();
		}
		if (parentNode.getParent() != null) {
			parentNode = (DefaultMutableTreeNode)
				parentNode.getParent();
		}
	}
	return addObject(parentNode, child);
}

DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
	Object child)
{
	DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

	if (parent == null) {
		parent = rootNode;
	}

	treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

	return childNode;
}

void setLevelVisible(int level)
{
	scrollPathToVisible(new TreePath(rootNode.getPath()));
	expandPath(new TreePath(rootNode.getPath()));
}

}


class MyRenderer extends DefaultTreeCellRenderer {
ImageIcon failureIcon = new ImageIcon("roast/viewer/failureMessage.gif");
ImageIcon utilityIcon = new ImageIcon("roast/viewer/utilityMessage.gif");
	public Component getTreeCellRendererComponent(
		JTree tree, Object value, boolean sel,
		boolean expanded, boolean leaf, int row,
		boolean hasFocus) 
	{
		super.getTreeCellRendererComponent(tree, value, 
			sel, expanded, leaf, row, hasFocus);
		Object nodeObject = ((DefaultMutableTreeNode)value).
			getUserObject();
		if (nodeObject instanceof FailureMessage) {
			setIcon(failureIcon);
		} else if (nodeObject instanceof UtilityMessage) {
			setIcon(utilityIcon);
		}
		return this;
	}

}
