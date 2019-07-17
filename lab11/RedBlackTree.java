public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        // TODO: YOUR CODE HERE
        // HINT: Having a case for each number of items in r might help
        // return null;
        if (r == null) {
            return null;
        } else {
            if (r.getItemCount() == 3) {
                // this node contains 3 item, which means it should have 4 children.
                RBTreeNode RBTreeRoot = new RBTreeNode(true, r.getItemAt(1));
                RBTreeRoot.left = new RBTreeNode(false, r.getItemAt(0));
                RBTreeRoot.right = new RBTreeNode(false, r.getItemAt(2));
                if (!(r.getChildrenCount() == 0)) {
                    // either 0 or 4
                    RBTreeRoot.left.left = buildRedBlackTree(r.getChildAt(0));
                    RBTreeRoot.left.right = buildRedBlackTree(r.getChildAt(1));
                    RBTreeRoot.right.left = buildRedBlackTree(r.getChildAt(2));
                    RBTreeRoot.right.right = buildRedBlackTree(r.getChildAt(3));
                }
                return RBTreeRoot;
            } else if (r.getItemCount() == 2) {
                // this node contains 2 item, which means it should have 3 children.
                RBTreeNode RBTreeRoot = new RBTreeNode(true, r.getItemAt(0));
                RBTreeRoot.right = new RBTreeNode(false, r.getItemAt(1));
                if (!(r.getChildrenCount() == 0)) {
                    // either 0 or 3
                    RBTreeRoot.left = buildRedBlackTree(r.getChildAt(0));        // !
                    RBTreeRoot.right.left = buildRedBlackTree(r.getChildAt(1));
                    RBTreeRoot.right.right = buildRedBlackTree(r.getChildAt(2));
                }
                return RBTreeRoot;
            } else {
                // this node contains 1 item, which means it should have 2 children.
                RBTreeNode RBTreeRoot = new RBTreeNode(true, r.getItemAt(0));
                if (!(r.getChildrenCount() == 0)) {
                    // either 0 or 2
                    RBTreeRoot.left = buildRedBlackTree(r.getChildAt(0));        // !
                    RBTreeRoot.right = buildRedBlackTree(r.getChildAt(1));
                }
                return RBTreeRoot;
            }
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        // return null;
        // current head's left will become the head.
        // through pointer change the structure.
        RBTreeNode newRight = new RBTreeNode<T>(false, node.item, node.left.right, node.right);
        node = new RBTreeNode<T>(node.isBlack, node.left.item, node.left.left, newRight);
        return node;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        // return null;
        RBTreeNode newLeft = new RBTreeNode<T>(false, node.item, node.left, node.right.left);
        node = new RBTreeNode<T>(node.isBlack, node.right.item, newLeft, node.right.right);
        return node;
    }

    void insert(T item) {
        // TODO: YOUR CODE HERE
        root = insert(root, item);
        root.isBlack = true;
    }

    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // Optional helper method
        // HINT: Remember to handle each of the cases from the spec
        // return null;
        if (node == null) {
            node = new RBTreeNode(false, item);
            return node;
        } else {
            int compareResult = item.compareTo(node.item);        // by this we can know which side should we search
            if (compareResult == 0) {
                // means what we are going to insert is already in the tree.
                return node;
            } else if (compareResult < 0) {
                // continue to search left
                node.left = insert(node.left, item);
            } else {
                // continue to search right
                node.right = insert(node.right, item);
            }
            
            if (isRed(node.right) && !isRed(node.left)) {
                // handle case C and "Right-leaning" situation.
                node = rotateLeft(node);
            } else if (isRed(node.left) && isRed(node.left.left)) {
                // handle case B
                 node = rotateRight(node);
            } else if (isRed(node.left) && isRed(node.right)) {
                // handle case A
                flipColors(node);
            }
            return node;
        }
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    /* Main method to help test constructor. Feel free to modify */
    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree<>();
        bTree.root = new BTree.TwoThreeFourNode<>(3, 4);
        RedBlackTree<Integer> rbTree = new RedBlackTree<>(bTree);
        System.out.println((rbTree.root != null));
        System.out.println((rbTree.root.left == null));
        System.out.println((rbTree.root.right != null));
        System.out.println((rbTree.root.isBlack));
        System.out.println((!rbTree.root.right.isBlack));
        System.out.println(3 == rbTree.root.item);
        System.out.println(4 == rbTree.root.right.item);
    }

}
