package org.ues21.aed2.soporte.third;

public class BSTNode<T extends Comparable>{
    private T data;
    private BSTNode leftChild;
    private BSTNode rightChild;
    private int height;

    public BSTNode(T data) {
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BSTNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BSTNode firstChild) {
        this.leftChild = firstChild;
    }

    public BSTNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(BSTNode secondChild) {
        this.rightChild = secondChild;
    }

    public boolean isLeaf() {
        return rightChild == null && leftChild == null;
    }

    public int getBalanceFactor() {
        int leftSubtreeHeight = leftChild == null ? -1 : leftChild.getHeight();
        int rightSubtreeHeight = rightChild == null ? -1 : rightChild.getHeight();
        int balanceFactor = leftSubtreeHeight - rightSubtreeHeight;

        return balanceFactor;
    }
}