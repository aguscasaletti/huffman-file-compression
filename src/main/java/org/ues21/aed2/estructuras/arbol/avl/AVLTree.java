package org.ues21.aed2.estructuras.arbol.avl;


public class AVLTree extends BinarySearchTree implements Tree {

    /**
     * Performs right rotation on the specified Node, updates heights and returns the new root of the subtree
     */
    private BSTNode rightRotate(BSTNode x) {
        BSTNode y = x.getLeftChild();
        BSTNode grandchild = y.getRightChild();

        y.setRightChild(x);
        x.setLeftChild(grandchild);

        // Update heights (first update the new child's height, and later the parent's height).
        x.setHeight(Math.max(x.getLeftChild() != null ? x.getLeftChild().getHeight(): -1, x.getRightChild() != null ? x.getRightChild().getHeight(): -1) + 1);
        y.setHeight(Math.max(y.getLeftChild() != null ? y.getLeftChild().getHeight(): -1, y.getRightChild() != null ? y.getRightChild().getHeight(): -1) + 1);

        return y;
    }

    /**
     * Performs left rotation on the specified Node, updates heights and returns the new root of the subtree
     */
    private BSTNode leftRotate(BSTNode x) {
        BSTNode y = x.getRightChild();
        BSTNode grandchild = y.getLeftChild();

        y.setLeftChild(x);
        x.setRightChild(grandchild);

        // Update heights (first update the new child's height, and later the parent's height).
        x.setHeight(Math.max(x.getLeftChild() != null ? x.getLeftChild().getHeight(): -1, x.getRightChild() != null ? x.getRightChild().getHeight(): -1) + 1);
        y.setHeight(Math.max(y.getLeftChild() != null ? y.getLeftChild().getHeight(): -1, y.getRightChild() != null ? y.getRightChild().getHeight(): -1) + 1);

        return y;
    }

    /**
     * Extends the BinarySearchTree insert method by checking
     * the balance factors when backtracking and performing the rotations.
     */
    protected BSTNode insert(BSTNode node, Comparable value) {
        // Perform regular BST Insert
        BSTNode n = super.insert(node, value);

        // Check balance factor when backtracking from recursive method.
        int balance = n.getBalanceFactor();

        // Left Left Case
        if (balance > 1 && value.compareTo(n.getLeftChild().getData()) < 0) {
            return rightRotate(n);
        }

        // Right Right Case
        if (balance < -1 && value.compareTo(n.getRightChild().getData()) > 0) {
            return leftRotate(n);
        }

        // Left Right Case
        if (balance > 1 && value.compareTo(n.getLeftChild().getData()) > 0) {
            n.setLeftChild(leftRotate(n.getLeftChild()));
            return rightRotate(n);
        }

        // Right Left Case
        if (balance < -1 && value.compareTo(n.getRightChild().getData()) < 0) {
            n.setRightChild(rightRotate(n.getRightChild()));
            return leftRotate(n);
        }

        return n;
    }

    /**
     * Extends the BinarySearchTree deleteNode method by checking
     * the balance factors when backtracking and performing the rotations.
     */
    protected BSTNode deleteNode(BSTNode node, int value) {
        // Perform regular BST delete.
        BSTNode n = super.deleteNode(node, value);

        if (n == null) {
            return null;
        }

        // Check balance factor when backtracking from recursive method.
        int balance = n.getBalanceFactor();

        // Left Left Case
        if (balance > 1 && n.getLeftChild().getBalanceFactor() >= 0) {
            return rightRotate(n);
        }

        // Left Right Case
        if (balance > 1 && n.getLeftChild().getBalanceFactor() < 0) {
            n.setLeftChild(leftRotate(n.getLeftChild()));
            return rightRotate(n);
        }

        // Right Right Case
        if (balance < -1 && n.getRightChild().getBalanceFactor() <= 0) {
            return leftRotate(n);
        }

        // Right Left Case
        if (balance < -1 && n.getRightChild().getBalanceFactor() > 0) {
            n.setRightChild(rightRotate(n.getRightChild()));
            return leftRotate(n);
        }

        return n;
    }

}