package org.ues21.dataStructures.huffman;

import org.ues21.dataStructures.list.Node;
import org.ues21.dataStructures.hashTable.SymbolsTableItem;
import org.ues21.dataStructures.hashTable.SymbolsHashTable;
import org.ues21.view.ProgressListener;

/**
 *
 * @author Agustín Aliaga
 *
 * Class that builds the Huffman tree
 *
 */
public class Huffman {

    private HuffmanList huffmanList = new HuffmanList();

    private SymbolsHashTable symbolsTable = new SymbolsHashTable(SymbolsHashTable.TableMode.KEY_SYMBOL);

    private ProgressListener progressListener;

    private String text;

    /**
     * @param text the initial message to be compressed
     */
    public Huffman(final String text) {
        this.text = text;
        this.buildHuffmanTree();
    }

    /**
     * @param text the initial message to be compressed
     * @param listener progress listener
     */
    public Huffman(final String text, ProgressListener listener){
        this.progressListener = listener;
        this.text = text;
        this.buildHuffmanTree();
    }

    public String getText() {
        return text;
    }

    /**
     * Method that builds the huffman tree
     */
    private void buildHuffmanTree() {
        for (char c : this.text.toCharArray()) {
            HuffmanNode node = new HuffmanNode(Character.toString(c), 1, null, null);
            huffmanList.addWithOrder(node);
        }

        int freq = 0;
        Node p1, p2;
        HuffmanNode left = null, right = null;
        int size = huffmanList.getSize();
        int total = huffmanList.getSize();

        while (size > 1) {
            p1 = huffmanList.get(1);
            p2 = huffmanList.get(2);

            if (this.progressListener != null) {
                int progress = (total - size) * 100 / total;
                this.progressListener.onProgressUpdate(progress);
            }

            if (p1 != null) {
                left = (HuffmanNode) p1.getInfo();
                freq = left.getFreq();
            }
            if (p2 != null) {
                right = (HuffmanNode) p2.getInfo();
                freq += right.getFreq();
            }

            HuffmanNode nodo1 = new HuffmanNode(null, freq, left, right);
            huffmanList.addWithOrder(nodo1);
            huffmanList.deleteFirst();
            huffmanList.deleteFirst();
            size = huffmanList.getSize();
        }

        if (this.huffmanList.getFirst() != null) {
            HuffmanNode p = this.huffmanList.getFirst().getInfo();
            createSymbolsTable(p, this.symbolsTable, "");
        }

        if (this.progressListener != null) {
            this.progressListener.onComplete();
        }
    }

    /**
     * Recursive method to create symbols table
     */
    private void createSymbolsTable(HuffmanNode p, SymbolsHashTable symbolsTable, String huffmanCode) {
        if (p != null) {
            if (p.getLeft() == null && p.getRight() == null) {
                symbolsTable.insert(p.getCharacter(), new SymbolsTableItem(p.getCharacter().charAt(0), huffmanCode));
            }
            createSymbolsTable(p.getLeft(), symbolsTable, huffmanCode + "0");
            createSymbolsTable(p.getRight(), symbolsTable, huffmanCode + "1");
        }
    }

    public SymbolsHashTable getSymbolsTable() {
        return symbolsTable;
    }
}
