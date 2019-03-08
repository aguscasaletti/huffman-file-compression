package org.ues21.aed2.estructuras.arbol.avl;

public interface Tree {

    void insert(Comparable number);

    void delete(Comparable number);

    void debugPrint();

    void print();

    BSTNode getRoot();

}
