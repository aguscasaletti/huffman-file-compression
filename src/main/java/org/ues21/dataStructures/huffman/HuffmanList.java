package org.ues21.dataStructures.huffman;
import org.ues21.dataStructures.list.Node;

/**
 * @author Agustín Aliaga
 *
 * Class to be used as a priority queue to build the huffman tree
 *
 */
public class HuffmanList {

    private Node first = null;
    private int size = 0;

    public void addWithOrder(final HuffmanNode info) {
        Node<HuffmanNode> current = this.getFirst();
        Node previous = null;
        boolean found = false;
        Node newNode = new Node(info, null);

        if (current == null) {
            this.setFirst(newNode);
            this.size ++;
            return;
        }

        Node insert = null;

        while (current != null) {
            if (info.getCharacter() != null &&
                    (info.getCharacter().equals(current.getInfo().getCharacter()))) {
                found = true;
                current.getInfo().setFreq(current.getInfo().getFreq() + 1);
                while (current.getNext() != null &&
                        current.getInfo().getFreq()
                                > (current.getNext().getInfo()).getFreq()) {
                    Node aux = current.getNext();
                    if (previous != null) {
                        previous.setNext(aux);
                    } else {
                        // Es el primer nodo
                        this.first = aux;
                    }
                    current.setNext(aux.getNext());
                    aux.setNext(current);
                    previous = aux;
                }
                break;
            }

            if (current.getInfo().getFreq() < info.getFreq()) {
                insert = current;
            }

            previous = current;
            current = current.getNext();
        }

        if (!found) {
            if (insert == null) {
                // Es el primer Node a insert
                newNode.setNext(this.first);
                this.first = newNode;
            } else {
                Node aux = insert.getNext();
                insert.setNext(newNode);
                newNode.setNext(aux);
            }

            this.size ++;
        }

    }

    public Node get(int pos) {
        Node p = this.getFirst();
        int i = 1;
        while (p != null) {
            if (i == pos) {
                return p;
            }
            p = p.getNext();
            i++;
        }
        return null;
    }


    public HuffmanNode deleteFirst() {
        if (this.getFirst() != null) {
            Node<HuffmanNode> p = this.getFirst();
            HuffmanNode y =  p.getInfo();
            this.setFirst(this.getFirst().getNext());
            p.setNext(null);

            this.size --;
            return y;
        } else {
            return null;
        }

    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the first
     */
    public Node<HuffmanNode> getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(Node first) {
        this.first = first;
    }

}
