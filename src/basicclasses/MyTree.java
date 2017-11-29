package basicclasses;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Class to extend JTable, add one new method
 * @see JTree
 */
public class MyTree extends JTree {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of class.
	 * @param root a TreeNode object
	 */
	public MyTree(DefaultMutableTreeNode root) {
		super(root);
	}

	/**
	 * Method to expand or collapse all tree structure.
	 * @param parent the parent node
	 * @param mode 1 - expand, other number - collapse
	 */
	public void expandAll(TreePath parent, int mode) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				this.expandAll(path, mode);
			}
		}
		if (mode==1) this.expandPath(parent);
		else this.collapsePath(parent);
	}
}
