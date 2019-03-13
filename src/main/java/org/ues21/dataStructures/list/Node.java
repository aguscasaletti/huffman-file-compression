package org.ues21.dataStructures.list;

/**
 *
 * Node para utilizar en list enlazada
 *
 * @author Agustín Aliaga
 */
public class Node<T> {
    
    private T info;
    private Node<T> next;

    public Node(T info, Node next) {
        this.info = info;
        this.next = next;
    }

    public T getInfo() {
        return info;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}
