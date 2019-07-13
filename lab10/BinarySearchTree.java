public class BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T> {

    /* Creates an empty BST. */
    public BinarySearchTree() {
        // YOUR CODE HERE
        super();
    }

    /* Creates a BST with root as ROOT. */
    public BinarySearchTree(TreeNode root) {
        // YOUR CODE HERE
        super(root);
    }

    /* Returns true if the BST contains the given KEY. */
    public boolean contains(T key) {
        // YOUR CODE HERE
        // return false;
        return this.containsHelper(this.root, key);
    }
    
    public boolean containsHelper(TreeNode p, T key) {
        if (p == null) {
            return false;
        } else if (p.item.equals(key)) {
            return true;
        } else if (key.compareTo(p.item) < 0) {
            return containsHelper(p.left, key);
        } else {        // include right != null and right == null(false, search completed)
            return containsHelper(p.right, key);
        }
    }

    /* Adds a node for KEY iff KEY isn't in the BST already. */
    public void add(T key) {
        // YOUR CODE HERE
        if (this.root == null) {
            this.root = new TreeNode(key, null, null);
        } else {
            addHelper(this.root, key);
        }
    }
    
    public void addHelper(TreeNode p, T key) {
        if (key.compareTo(p.item) < 0) {
            if (p.left == null) {
                p.left = new TreeNode(key, null, null);
            } else {
                addHelper(p.left, key);
            }
        } else if (key.compareTo(p.item) > 0) {
            if (p.right == null) {
                p.right = new TreeNode(key, null, null);
            } else {
                addHelper(p.right, key);
            }
        } else {        // exactly equal
            return;
        }
    }

    /* Deletes a node from the BST. */
    public T delete(T key) {
        TreeNode parent = null;
        TreeNode curr = root;
        TreeNode delNode = null;
        TreeNode replacement = null;
        boolean rightSide = false;

        while (curr != null && !curr.item.equals(key)) {
            if (((Comparable<T>) curr.item).compareTo(key) > 0) {
                parent = curr;
                curr = curr.left;
                rightSide = false;
            } else {
                parent = curr;
                curr = curr.right;
                rightSide = true;
            }
        }
        delNode = curr;
        if (curr == null) {
            return null;
        }

        if (delNode.right == null) {
            if (root == delNode) {
                root = root.left;
            } else {
                if (rightSide) {
                    parent.right = delNode.left;
                } else {
                    parent.left = delNode.left;
                }
            }
        } else {
            curr = delNode.right;
            replacement = curr.left;
            if (replacement == null) {
                replacement = curr;
            } else {
                while (replacement.left != null) {
                    curr = replacement;
                    replacement = replacement.left;
                }
                curr.left = replacement.right;
                replacement.right = delNode.right;
            }
            replacement.left = delNode.left;
            if (root == delNode) {
                root = replacement;
            } else {
                if (rightSide) {
                    parent.right = replacement;
                } else {
                    parent.left = replacement;
                }
            }
        }
        return delNode.item;
    }
}
