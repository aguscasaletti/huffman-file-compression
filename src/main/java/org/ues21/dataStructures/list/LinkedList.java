package org.ues21.dataStructures.list;

/**
 * Lista enlazada básica
 */
public class LinkedList {

    private Node first;
    private int size;

    public LinkedList(Node first) {
        this.first = first;
    }

    public void insertLast(Node node) {
        if (this.first == null) {
            this.first = node;
            return;
        }

        Node aux = this.first;
        while (aux.getNext() != null) {
            aux = aux.getNext();
        }

        aux.setNext(node);
        size ++;
    }

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }
}
