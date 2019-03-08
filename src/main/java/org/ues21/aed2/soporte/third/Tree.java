package org.ues21.aed2.soporte.third;

public interface Tree {

    void insert(Comparable number);

    void delete(Comparable number);

    void debugPrint();

    void print();

    BSTNode getRoot();

}
