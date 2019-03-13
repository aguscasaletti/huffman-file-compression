package org.ues21.dataStructures.hashTable;

import org.ues21.dataStructures.list.LinkedList;
import org.ues21.dataStructures.list.Node;

import java.util.Iterator;

/**
 *
 * Hash table used for encoding - decoding data and near O(n) search time complexity for small alphabets.
 * Uses a linked list to resolve collisions.
 *
 */
public class SymbolsHashTable implements Iterable<SymbolsTableItem> {

    private int m = 500;
    private int size = 0;
    private LinkedList[] map;
    private TableMode tableMode;

    public enum TableMode {
        KEY_SYMBOL,
        KEY_HUFFMAN_CODE
    }

    public SymbolsHashTable(TableMode tableMode) {
        this.tableMode = tableMode;
        this.map = new LinkedList[this.m];
    }

    /**
     * Insert an element to the table
     *
     * @param key Given key. Can be either the symbol or huffman code
     * @param value the table entry
     */
    public void insert(String key, SymbolsTableItem value) {
        int hash = hash(key);
        Node<SymbolsTableItem> node = new Node<>(value, null);

        if (this.map[hash] == null) {
            LinkedList l = new LinkedList(node);
            this.map[hash] = l;
        } else {
            this.map[hash].insertLast(node);
        }

        this.size ++;
    }

    /**
     * Get the table entry for a given key.
     */
    public SymbolsTableItem find(String key) {
        int hash = hash(key);

        if (this.map[hash] == null) {
            return null;
        }

        Node node = this.map[hash].getFirst();
        while (node != null) {
            SymbolsTableItem item = (SymbolsTableItem) node.getInfo();

            if (
                    (this.tableMode == TableMode.KEY_SYMBOL ? String.valueOf(item.getSymbol()) : item.getHuffmanCode()).equals(key)
            ) {
                return (SymbolsTableItem) node.getInfo();
            }

            node = node.getNext();
        }

        return null;
    }

    /**
     * Hash function
     * @param k input
     * @return native String hashCode() module this.m
     */
    private int hash(String k) {
        return Math.abs(k.hashCode() % this.m);
    }

    public int getSize() {
        return size;
    }

    /**
     * Make this HashTable iterable
     */
    @Override
    public Iterator<SymbolsTableItem> iterator() {
        return new Iterator<SymbolsTableItem>() {
            Node<SymbolsTableItem> current;
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                int index = currentIndex;
                if (current == null) {
                    return size != 0;
                }

                if (current.getNext() != null) {
                    return true;
                }

                while (index < map.length - 1) {
                    index++;
                    if (map[index] != null) {
                        return map[index].getFirst().getInfo() != null;
                    }
                }

                return false;
            }

            @Override
            public SymbolsTableItem next() {
                if (current == null) {
                    for (int i = 0; i < map.length; i++) {
                        if (map[i] != null) {
                            currentIndex = i;
                            current = map[i].getFirst();
                            return current.getInfo();
                        }
                    }
                }

                if (current.getNext() != null) {
                    current = current.getNext();
                    return current.getInfo();
                }

                while (currentIndex < map.length - 1) {
                    currentIndex ++;
                    try {
                        if (map[currentIndex] != null) {
                            current = map[currentIndex].getFirst();
                            return current.getInfo();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                return null;
            }
        };
    }
}
