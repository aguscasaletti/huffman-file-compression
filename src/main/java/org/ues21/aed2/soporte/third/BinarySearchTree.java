package org.ues21.aed2.soporte.third;


public class BinarySearchTree implements Tree {
    protected BSTNode root;
    protected int size = 0;

    public BSTNode getRoot() {
        return root;
    }

    public void setRoot(BSTNode root) {
        this.root = root;
    }

    /**
     * Recursive method to insert a Node. Returns the new root of the subtree where the Node was inserted.
     * It also updates the heights while backtracking.
     */
    protected BSTNode insert(BSTNode node, Comparable value) {
        if (node == null) {
            // Create the new Node and start backtracking.
            size ++;
            return new BSTNode(value);
        }

        if (value.compareTo(node.getData()) < 0) {
            node.setLeftChild(insert(node.getLeftChild(), value));
        } else if (value.compareTo(node.getData()) > 0) {
            node.setRightChild(insert(node.getRightChild(), value));
        } else {
            return node;
        }

        // Update the heights while backtracking.
        int leftChildHeight = node.getLeftChild() != null ? node.getLeftChild().getHeight() : -1;
        int rightChildHeight = node.getRightChild() != null ? node.getRightChild().getHeight() : -1;
        node.setHeight(1 + Math.max(leftChildHeight, rightChildHeight));

        return node;
    }

    public void debugPrint(BSTNode n, int parent, String child) {
        if (n == null) {
            return;
        }

        if (child.equals("NONE")) {
            System.out.println("--------------------------------");
            System.out.println(
                    String.format(
                            "Data: %s  -  Height: %s  -  Balance Factor: %s  --- ROOT",
                            n.getData(),
                            n.getHeight(),
                            n.getBalanceFactor()
                    )
            );
        } else {
            System.out.println(
                    String.format(
                            "Data: %s  -  Height: %s  -  Balance Factor: %s --- %s of %s",
                            n.getData(),
                            n.getHeight(),
                            n.getBalanceFactor(),
                            child,
                            parent
                    )
            );
        }

//        debugPrint(n.getLeftChild(), n.getData(), "left child");
//        debugPrint(n.getRightChild(), n.getData(), "right child");
    }

    @Override
    public void insert(Comparable number) {
        root = insert(root, number);
    }

    protected BSTNode minValueNode(BSTNode node) {
        BSTNode current = node;

        while (current.getLeftChild() != null) {
            current = current.getLeftChild();
        }

        return current;
    }

    /**
     * Recursive method to delete Node. Returns null when the number is found as a Leaf.
     * Otherwise it returns the new root of the subtree where the node was deleted.
     * It also updates the heights while backtracking.
     */
    protected BSTNode deleteNode(BSTNode node, Comparable number) {
        if (node == null) {
            return null;
        }

        if (number.compareTo(node.getData()) < 0) {
            node.setLeftChild(deleteNode(node.getLeftChild(), number));
        } else if (number.compareTo(node.getData()) > 0) {
            node.setRightChild(deleteNode(node.getRightChild(), number));
        } else {
            if ((node.getLeftChild() == null) || (node.getRightChild() == null)) {
                if (node.isLeaf()) {
                    return null;
                }

                BSTNode temp;
                if (node.getLeftChild() == null) {
                    temp = node.getRightChild();
                } else {
                    temp = node.getLeftChild();
                }

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                BSTNode temp = minValueNode(node.getRightChild());
                node.setData(temp.getData());
                node.setRightChild(deleteNode(node.getRightChild(), temp.getData()));
            }
        }


        // Update the heights while backtracking
        int leftChildHeight = node.getLeftChild() != null ? node.getLeftChild().getHeight() : -1;
        int rightChildHeight = node.getRightChild() != null ? node.getRightChild().getHeight() : -1;
        node.setHeight(1 + Math.max(leftChildHeight, rightChildHeight));

        return node;
    }

    @Override
    public void delete(Comparable number) {
        this.root = deleteNode(this.root, number);
    }

    @Override
    public void debugPrint() {
        debugPrint(root, -1, "NONE");
    }

    public BSTNode find(Comparable value) {
        return find(this.root, value);
    }

    private BSTNode find(BSTNode node, Comparable value) {
        if (node == null) {
            return null;
        }

        if (node.getData().equals(value)) {
            return node;
        } else if (value.compareTo(node.getData()) < 0) {
            return find(node.getLeftChild(), value);
        } else {
            return find(node.getRightChild(), value);
        }
    }

    /**
     * INORDER print
     */
    private void inorderPrint(BSTNode node) {
        if (node == null) {
            return;
        }
        inorderPrint(node.getLeftChild());
        System.out.print(node.getData() + "(" + node.getBalanceFactor() + ") ");
        inorderPrint(node.getRightChild());
    }

    @Override
    public void print() {
        System.out.println("");
        inorderPrint(root);
    }

    public int getSize() {
        return size;
    }
}